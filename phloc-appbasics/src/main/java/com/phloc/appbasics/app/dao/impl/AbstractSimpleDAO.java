/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.appbasics.app.dao.impl;

import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.IDAOReadExceptionHandler;
import com.phloc.appbasics.app.dao.IDAOWriteExceptionHandler;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroComment;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Base class for a simple DAO.
 * 
 * @author Philip Helger
 */
public abstract class AbstractSimpleDAO extends AbstractDAO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractSimpleDAO.class);

  private final File m_aFile;

  protected AbstractSimpleDAO (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (new DAOWebFileIO ());
    m_aFile = WebFileIO.getFile (sFilename);
    if (m_aFile.exists ())
    {
      // file exist -> must be a file!
      if (!m_aFile.isFile ())
        throw new DAOException ("The passed filename '" + sFilename + "' is not a file (maybe a directory?)");
    }
    else
    {
      // Ensure the parent directory is present
      final File aParentDir = m_aFile.getParentFile ();
      if (aParentDir != null)
        if (WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aParentDir).isFailure ())
          throw new DAOException ("Failed to create parent directory '" + aParentDir + "'");
    }
  }

  /**
   * Custom initialization routine. Called only if the underlying file does not
   * exist yet. This method is only called within a write lock!
   * 
   * @return {@link EChange#CHANGED} if something was modified inside this
   *         method
   */
  @Nonnull
  @OverrideOnDemand
  protected EChange onInit ()
  {
    return EChange.UNCHANGED;
  }

  /**
   * Fill the internal structures with from the passed XML document. This method
   * is only called within a write lock!
   * 
   * @param aDoc
   *        The XML document to read from
   * @return {@link EChange#CHANGED} if reading the data changed something in
   *         the internal structures that requires a writing.
   */
  @Nonnull
  protected abstract EChange onRead (@Nonnull IMicroDocument aDoc);

  /**
   * Call this method inside the constructor to read the file contents directly.
   * This method is write locked!
   * 
   * @throws DAOException
   *         in case initialization or reading failed!
   */
  protected final void initialRead () throws DAOException
  {
    boolean bIsInitialization = false;
    m_aRWLock.writeLock ().lock ();
    try
    {
      ESuccess eWriteSuccess = ESuccess.SUCCESS;
      bIsInitialization = !m_aFile.exists ();
      if (bIsInitialization)
      {
        // initial setup for non-existing file
        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Trying to initialize DAO XML file '" + m_aFile + "'");

        beginWithoutAutoSave ();
        try
        {
          if (onInit ().isChanged ())
            eWriteSuccess = _writeToFile ();
        }
        finally
        {
          endWithoutAutoSave ();
          // reset any pending changes, because the initialization should
          // be read-only. If the implementing class changed something,
          // the return value of onInit() is what counts
          internalSetPendingChanges (false);
        }
      }
      else
      {
        // Read existing file
        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Trying to read DAO XML file '" + m_aFile + "'");

        final IMicroDocument aDoc = MicroReader.readMicroXML (new FileSystemResource (m_aFile));
        if (aDoc == null)
          s_aLogger.error ("Failed to read XML document from file '" + m_aFile + "'");
        else
        {
          // Valid XML - start interpreting
          beginWithoutAutoSave ();
          try
          {
            if (onRead (aDoc).isChanged ())
              eWriteSuccess = _writeToFile ();
          }
          finally
          {
            endWithoutAutoSave ();
            // reset any pending changes, because the initialization should
            // be read-only. If the implementing class changed something,
            // the return value of onRead() is what counts
            internalSetPendingChanges (false);
          }
        }
      }

      // Check if writing was successful on any of the 2 branches
      if (eWriteSuccess.isSuccess ())
        internalSetPendingChanges (false);
      else
        s_aLogger.warn ("File '" + m_aFile + "' has pending changes after initialRead!");
    }
    catch (final Throwable t)
    {
      // Custom exception handler for reading
      final IDAOReadExceptionHandler aExceptionHandlerRead = getCustomExceptionHandlerRead ();
      if (aExceptionHandlerRead != null)
      {
        final IReadableResource aRes = m_aFile == null ? null : new FileSystemResource (m_aFile);
        try
        {
          aExceptionHandlerRead.onDAOReadException (t, bIsInitialization, aRes);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Error in custom exception handler for reading " +
                               (aRes == null ? "memory-only" : aRes.toString ()),
                           t2);
        }
      }
      throw new DAOException ("Error " +
                              (bIsInitialization ? "initializing" : "reading") +
                              " the file '" +
                              m_aFile +
                              "'", t);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Create the XML document that should be saved to the file. This method is
   * only called within a write lock!
   * 
   * @return The non-<code>null</code> document to write to the file.
   */
  @Nonnull
  protected abstract IMicroDocument createWriteData ();

  /**
   * Modify the created document by e.g. adding some comment or digital
   * signature or whatsoever.
   * 
   * @param aDoc
   *        The created non-<code>null</code> document.
   */
  @OverrideOnDemand
  protected void modifyWriteData (@Nonnull final IMicroDocument aDoc)
  {
    // Add a small comment
    aDoc.insertBefore (new MicroComment ("This file was generated automatically - do NOT modify!\n" +
                                         "Written at " +
                                         PDTToString.getAsString (PDTFactory.getCurrentDateTimeUTC (), Locale.US)),
                       aDoc.getDocumentElement ());
  }

  /**
   * The main method for writing the new data to a file. This method may only be
   * called within a write lock!
   * 
   * @return {@link ESuccess} and never <code>null</code>.
   */
  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  private ESuccess _writeToFile ()
  {
    IMicroDocument aDoc = null;
    try
    {
      // Create XML document to write
      aDoc = createWriteData ();
      if (aDoc == null)
      {
        s_aLogger.error ("Failed to create data to write to '" + m_aFile + "'!");
        return ESuccess.FAILURE;
      }

      // Generic modification
      modifyWriteData (aDoc);

      // Get the output stream
      final OutputStream aOS = FileUtils.getOutputStream (m_aFile);
      if (aOS == null)
      {
        // Happens, when another application has the file open!
        // Logger warning already emitted
        return ESuccess.FAILURE;
      }

      // rename old files to have a backup
      // for (int i = m_nBackupCount; i > 0; --i)
      // if (i == 1)
      // getIO ().renameFile (sFilename, sFilename + "." + i);
      // else
      // getIO ().renameFile (sFilename + "." + (i - 1), sFilename + "." + i);

      // Write to file
      if (MicroWriter.writeToStream (aDoc, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS).isFailure ())
      {
        s_aLogger.error ("Failed to write DAO XML data to '" + m_aFile + "'");
        return ESuccess.FAILURE;
      }

      return ESuccess.SUCCESS;
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to write the DAO data to  '" + m_aFile + "'", t);

      // Check if a custom exception handler is present
      final IDAOWriteExceptionHandler aExceptionHandlerWrite = getCustomExceptionHandlerWrite ();
      if (aExceptionHandlerWrite != null)
      {
        final IReadableResource aRes = new FileSystemResource (m_aFile);
        try
        {
          aExceptionHandlerWrite.onDAOWriteException (t,
                                                      aRes,
                                                      aDoc == null ? "no XML document created"
                                                                  : MicroWriter.getXMLString (aDoc));
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Error in custom exception handler for writing " + aRes.toString (), t2);
        }
      }
      return ESuccess.FAILURE;
    }
  }

  /**
   * This method must be called everytime something changed in the DAO. This
   * method must be called within a write-lock as it is not locked!
   */
  @MustBeLocked (ELockType.WRITE)
  protected final void markAsChanged ()
  {
    // Just remember that something changed
    internalSetPendingChanges (true);
    if (internalIsAutoSaveEnabled ())
    {
      // Auto save
      if (_writeToFile ().isSuccess ())
        internalSetPendingChanges (false);
      else
        s_aLogger.error ("File '" + m_aFile + "' still has pending changes after markAsChanged!");
    }
  }

  /**
   * In case there are pending changes write them to the file. This method is
   * write locked!
   */
  public final void writeToFileOnPendingChanges ()
  {
    if (hasPendingChanges ())
    {
      m_aRWLock.writeLock ().lock ();
      try
      {
        // Write to file
        if (_writeToFile ().isSuccess ())
          internalSetPendingChanges (false);
        else
          s_aLogger.error ("File '" + m_aFile + "' still has pending changes after writeToFileOnPendingChanges!");
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("file", m_aFile).toString ();
  }
}
