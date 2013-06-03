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

import java.io.InputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.IDAODataProvider;
import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.dao.IDAOReadExceptionHandler;
import com.phloc.appbasics.app.dao.IDAOWriteExceptionHandler;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.callback.INonThrowingCallable;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.factory.FactoryConstantValue;
import com.phloc.commons.factory.IFactory;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is the base class for all kind of data access managers that are file
 * based.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class DefaultDAO extends AbstractDAO
{
  /** By default the last 3 files are kept for backup */
  public static final int DEFAULT_BACKUP_COUNT = 3;
  private static final Logger s_aLogger = LoggerFactory.getLogger (DefaultDAO.class);

  private static IFactory <? extends IDAOIO> s_aDAOIOFactory = FactoryConstantValue.create (new DAOWebIO ());

  /** The file from which we read/to which we write */
  private final IHasFilename m_aFilenameProvider;

  /** The main provider to handle the content specific parts */
  private final IDAODataProvider m_aDataProvider;

  /** Number of backup files to keep. */
  private final int m_nBackupCount;

  private String m_sPreviousFilename;

  public DefaultDAO (@Nonnull final IHasFilename aFilenameProvider, @Nonnull final IDAODataProvider aDataProvider)
  {
    this (aFilenameProvider, aDataProvider, DEFAULT_BACKUP_COUNT);
  }

  public DefaultDAO (@Nonnull final IHasFilename aFilenameProvider,
                     @Nonnull final IDAODataProvider aDataProvider,
                     @Nonnegative final int nBackupCount)
  {
    this (aFilenameProvider, aDataProvider, nBackupCount, s_aDAOIOFactory.create ());
  }

  public DefaultDAO (@Nonnull final IHasFilename aFilenameProvider,
                     @Nonnull final IDAODataProvider aDataProvider,
                     @Nonnegative final int nBackupCount,
                     @Nonnull final IDAOIO aDAOIO)
  {
    super (aDAOIO);
    if (aFilenameProvider == null)
      throw new NullPointerException ("filenameProvider");
    if (aDataProvider == null)
      throw new NullPointerException ("No data provider passed");
    if (nBackupCount < 0)
      throw new IllegalArgumentException ("Negative backup count is not valid: " + nBackupCount);
    m_aFilenameProvider = aFilenameProvider;
    m_aDataProvider = aDataProvider;
    m_nBackupCount = nBackupCount;
    m_sPreviousFilename = aFilenameProvider.getFilename ();
  }

  @Nonnull
  public static IFactory <? extends IDAOIO> getDAOIOFactory ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDAOIOFactory;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setDAOIOFactory (@Nonnull final IFactory <? extends IDAOIO> aFactory)
  {
    if (aFactory == null)
      throw new NullPointerException ("factory");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aDAOIOFactory = aFactory;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  protected final IDAODataProvider getDataProvider ()
  {
    return m_aDataProvider;
  }

  /**
   * Initially read the content from the file. Should only be called from the
   * constructor. That's why no locking is needed.
   * 
   * @throws DAOException
   *         In case reading fails
   */
  protected final void readFromFile () throws DAOException
  {
    final String sFilename = m_aFilenameProvider.getFilename ();
    if (sFilename == null)
    {
      // required for testing
      s_aLogger.error ("This DAO of class " + getClass ().getName () + " will not be able to read from a file");

      // do not return, in case "openInputStream" has a special solution for
      // null filenames...
    }

    boolean bIsInitialization = true;
    EChange eChange;
    try
    {
      if (GlobalDebug.isDebugMode ())
        s_aLogger.info ("Trying to read DAO file '" + sFilename + "'");

      final InputStream aIS = getIO ().openInputStream (sFilename);
      if (aIS == null)
      {
        // Failed to open file
        if (sFilename == null)
        {
          // we're not operating on a file - no init!
          return;
        }

        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Initializing DAO for file '" + sFilename + "'");

        // The file was not yet found - run initialization
        eChange = performWithoutAutoSave (new INonThrowingCallable <EChange> ()
        {
          @Nonnull
          public EChange call ()
          {
            try
            {
              return m_aDataProvider.initForFirstTimeUsage ();
            }
            catch (final Throwable t)
            {
              throw new InitializationException ("Error initializing the file '" +
                                                 sFilename +
                                                 "' for the first time usage", t);
            }
            finally
            {
              // reset any pending changes, because the initialization should
              // be read-only. If the implementing class changed something, the
              // return value is what counts
              internalSetPendingChanges (false);
            }
          }
        });
      }
      else
      {
        // ESCA-JAVA0174:
        // An input stream was open
        bIsInitialization = false;
        try
        {
          // Read the file but disable auto-save
          eChange = performWithoutAutoSave (new INonThrowingCallable <EChange> ()
          {
            @Nonnull
            public EChange call ()
            {
              try
              {
                // try read it from the stream
                return m_aDataProvider.readFromStream (aIS);
              }
              catch (final Throwable t)
              {
                throw new InitializationException ("Error reading the file '" + sFilename + "' from an input stream", t);
              }
              finally
              {
                // reset any pending changes, because the initialization should
                // be read-only. If the implementing class changed something,
                // the return value is what counts
                internalSetPendingChanges (false);
              }
            }
          });
        }
        finally
        {
          // don't forget to close the stream if it is open!
          StreamUtils.close (aIS);
        }
      }

      // Write to file, after the input stream is closed!!!
      if (eChange.isChanged ())
        markAsChanged ();
    }
    catch (final Throwable t)
    {
      final IDAOReadExceptionHandler aExceptionHandlerRead = getCustomExceptionHandlerRead ();
      if (aExceptionHandlerRead != null)
      {
        final IReadableResource aRes = sFilename == null ? null : getIO ().getReadableResource (sFilename);
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
                              sFilename +
                              "'", t);
    }
  }

  /**
   * Called after a successful write of the file, if the filename is different
   * from the previous filename. This can e.g. be used to clear old data.
   */
  @OverrideOnDemand
  protected void onFilenameChange ()
  {}

  /**
   * Overwrite the file passed in the constructor and save the current state. In
   * every case, a backup of the old document is done before the new state is
   * written into it.
   * 
   * @return {@link ESuccess}
   */
  @Nonnull
  private ESuccess _writeToFile ()
  {
    // Build the filename to write to
    final String sFilename = m_aFilenameProvider.getFilename ();
    if (sFilename == null)
    {
      // We're not operating on a file!
      // required for testing
      s_aLogger.error ("This DAO of class " + getClass ().getName () + " will not be able to write to a file");
      return ESuccess.FAILURE;
    }

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Trying to write DAO file '" + sFilename + "'");

    // Check for a filename change before writing
    if (!sFilename.equals (m_sPreviousFilename))
    {
      onFilenameChange ();
      m_sPreviousFilename = sFilename;
    }

    final StringBuilder aFileContent = new StringBuilder ();
    try
    {
      // get content to save before the file is renamed in case of an exception
      m_aDataProvider.fillStringBuilderForSaving (aFileContent);

      // Check content
      final String sContent = aFileContent.toString ();
      if (!m_aDataProvider.isContentValidForSaving (sContent))
      {
        s_aLogger.warn ("Data provider stopped saving data to file '" +
                        sFilename +
                        "'. Created content was:\n" +
                        sContent);
        return ESuccess.FAILURE;
      }

      // rename old files to have a backup
      for (int i = m_nBackupCount; i > 0; --i)
        if (i == 1)
          getIO ().renameFile (sFilename, sFilename + "." + i);
        else
          getIO ().renameFile (sFilename + "." + (i - 1), sFilename + "." + i);

      // write to file
      return getIO ().saveFile (sFilename, sContent, m_aDataProvider.getCharset ());
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to write to file '" + sFilename + "'", t);
      // Check if a custom exception handler is present
      final IDAOWriteExceptionHandler aExceptionHandlerWrite = getCustomExceptionHandlerWrite ();
      if (aExceptionHandlerWrite != null)
      {
        final IReadableResource aRes = getIO ().getReadableResource (sFilename);
        try
        {
          aExceptionHandlerWrite.onDAOWriteException (t, aRes, aFileContent);
        }
        catch (final Throwable t2)
        {
          s_aLogger.error ("Error in custom exception handler for writing " + aRes.toString (), t2);
        }
      }
      return ESuccess.FAILURE;
    }
  }

  private void _internalWriteToFileOnPendingChanges ()
  {
    // Do we have any changes?
    if (internalHasPendingChanges ())
    {
      // Main write to the file
      if (_writeToFile ().isSuccess ())
        internalSetPendingChanges (false);
      else
      {
        // Only notify if we're operating on a file
        final String sFilename = m_aFilenameProvider.getFilename ();
        if (sFilename != null)
          s_aLogger.error ("Object '" + sFilename + "' has still pending changes!");
      }
    }
  }

  /**
   * Mark this DAO as changed. Must be called outside a writeLock, as this
   * method locks itself!
   */
  protected final void markAsChanged ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      internalSetPendingChanges (true);
      if (internalIsAutoSaveEnabled ())
        _internalWriteToFileOnPendingChanges ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Write pending changes to the file. Must be called outside a writeLock, as
   * this method locks itself!
   */
  public final void writeToFileOnPendingChanges ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      _internalWriteToFileOnPendingChanges ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final DefaultDAO rhs = (DefaultDAO) o;
    return m_aFilenameProvider.equals (rhs.m_aFilenameProvider) &&
           m_aDataProvider.equals (rhs.m_aDataProvider) &&
           m_nBackupCount == rhs.m_nBackupCount;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFilenameProvider)
                                       .append (m_aDataProvider)
                                       .append (m_nBackupCount)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("filenameProvider", m_aFilenameProvider)
                            .append ("dataProvider", m_aDataProvider)
                            .append ("backupCount", m_nBackupCount)
                            .toString ();
  }
}
