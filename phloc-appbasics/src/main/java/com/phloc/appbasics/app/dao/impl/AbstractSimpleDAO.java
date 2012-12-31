/**
 * Copyright (C) 2006-2012 phloc systems
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
import javax.annotation.meta.When;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroComment;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;

public abstract class AbstractSimpleDAO extends AbstractDAO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractSimpleDAO.class);

  private final File m_aFile;

  protected AbstractSimpleDAO (@Nonnull @Nonempty final String sFilename)
  {
    super (new DAOWebFileIO ());
    m_aFile = WebFileIO.getFile (sFilename);
    if (m_aFile.exists ())
    {
      // file exist -> must be a file!
      if (!m_aFile.isFile ())
        throw new IllegalArgumentException ("The passed filename '" + sFilename + "' is a directory and not a file!");
    }
    else
    {
      // Ensure the parent directory is present
      final File aParentFile = m_aFile.getParentFile ();
      if (aParentFile != null)
        WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aParentFile);
    }
  }

  /**
   * Custom initialization routine. Called only if the underlying file does not
   * exist yet.
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
   * Fill the internal structures with from the passed XML document.
   * 
   * @param aDoc
   *        The XML document to read from
   * @return {@link EChange#CHANGED} if reading the data changed something in
   *         the internal structures that requires a writing.
   */
  @Nonnull
  protected abstract EChange onRead (@Nonnull IMicroDocument aDoc);

  /**
   * Create the XML document that should be saved to the file.
   * 
   * @return The non-<code>null</code> document to write to the file.
   */
  @Nonnull (when = When.MAYBE)
  protected abstract IMicroDocument createWriteData ();

  @Nonnull
  private ESuccess _writeToFile ()
  {
    // Create XML document
    final IMicroDocument aDoc = createWriteData ();
    if (aDoc == null)
    {
      s_aLogger.error ("Failed to create data to write!");
      return ESuccess.FAILURE;
    }

    // Add a small comment
    aDoc.insertBefore (new MicroComment ("This file was generated automatically - do NOT modify!\n" +
                                         "Written at " +
                                         PDTToString.getAsString (PDTFactory.getCurrentDateTimeUTC (), Locale.US)),
                       aDoc.getDocumentElement ());

    // Get the output stream
    final OutputStream aOS = FileUtils.getOutputStream (m_aFile);
    if (aOS == null)
    {
      // Happens, when another application has the file open!
      // Logger warning already emitted
      return ESuccess.FAILURE;
    }

    // Write to file
    if (MicroWriter.writeToStream (aDoc, aOS, XMLWriterSettings.DEFAULT_XML_SETTINGS).isFailure ())
    {
      s_aLogger.error ("Failed to write data to " + m_aFile);
      return ESuccess.FAILURE;
    }

    return ESuccess.SUCCESS;
  }

  protected final void initialRead ()
  {
    ESuccess eWriteSuccess = ESuccess.SUCCESS;
    if (!m_aFile.exists ())
    {
      // initial setup
      if (onInit ().isChanged ())
        eWriteSuccess = _writeToFile ();
    }
    else
    {
      // Read existing file
      final IMicroDocument aDoc = MicroReader.readMicroXML (new FileSystemResource (m_aFile));
      if (aDoc == null)
        s_aLogger.error ("Failed to read XML document from file " + m_aFile);
      else
        if (onRead (aDoc).isChanged ())
          eWriteSuccess = _writeToFile ();
    }
    if (eWriteSuccess.isSuccess ())
      m_bPendingChanges = false;
    else
      s_aLogger.warn ("File has pending changes after initialization!");
  }

  // Must be called in a writeLock!
  protected final void markAsChanged ()
  {
    if (m_bAutoSaveEnabled)
    {
      // Auto save
      if (_writeToFile ().isSuccess ())
        m_bPendingChanges = false;
      else
        s_aLogger.warn ("File still has pending changes after markAsChanged!");
    }
    else
    {
      // Just remember that something changed
      m_bPendingChanges = true;
    }
  }

  /**
   * In case there are pending changes write them to the file.
   */
  public void writeToFileOnPendingChanges ()
  {
    if (hasPendingChanges ())
    {
      // Write changes
      m_aRWLock.writeLock ().lock ();
      try
      {
        if (_writeToFile ().isSuccess ())
          m_bPendingChanges = false;
        else
          s_aLogger.warn ("File still has pending changes after writeToFileOnPendingChanges!");
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
  }
}
