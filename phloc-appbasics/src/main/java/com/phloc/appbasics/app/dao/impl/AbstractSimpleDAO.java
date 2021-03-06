/**
 * Copyright (C) 2006-2014 phloc systems
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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.dao.IDAOReadExceptionHandler;
import com.phloc.appbasics.app.dao.IDAOWriteExceptionHandler;
import com.phloc.appbasics.app.io.ConstantHasFilename;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileIOError;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.microdom.IMicroComment;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroComment;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.IStatisticsHandlerTimer;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.serialize.IXMLWriterSettings;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Base class for a simple DAO.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractSimpleDAO extends AbstractDAO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractSimpleDAO.class);

  private final IStatisticsHandlerCounter s_aStatsCounterInitTotal = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                          "$init-total");
  private final IStatisticsHandlerCounter s_aStatsCounterInitSuccess = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                            "$init-success");
  private final IStatisticsHandlerTimer s_aStatsCounterInitTimer = StatisticsManager.getTimerHandler (getClass ().getName () +
                                                                                                      "$init");
  private final IStatisticsHandlerCounter s_aStatsCounterReadTotal = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                          "$read-total");
  private final IStatisticsHandlerCounter s_aStatsCounterReadSuccess = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                            "$read-success");
  private final IStatisticsHandlerTimer s_aStatsCounterReadTimer = StatisticsManager.getTimerHandler (getClass ().getName () +
                                                                                                      "$read");
  private final IStatisticsHandlerCounter s_aStatsCounterWriteTotal = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                           "$write-total");
  private final IStatisticsHandlerCounter s_aStatsCounterWriteSuccess = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                             "$write-success");
  private final IStatisticsHandlerCounter s_aStatsCounterWriteExceptions = StatisticsManager.getCounterHandler (getClass ().getName () +
                                                                                                                "$write-exceptions");
  private final IStatisticsHandlerTimer s_aStatsCounterWriteTimer = StatisticsManager.getTimerHandler (getClass ().getName () +
                                                                                                       "$write");

  private final IHasFilename m_aFilenameProvider;
  private String m_sPreviousFilename;
  private int m_nInitCount = 0;
  private DateTime m_aLastInitDT;
  private int m_nReadCount = 0;
  private DateTime m_aLastReadDT;
  private int m_nWriteCount = 0;
  private DateTime m_aLastWriteDT;

  protected AbstractSimpleDAO (@Nullable final String sFilename)
  {
    this (new ConstantHasFilename (sFilename));
  }

  protected AbstractSimpleDAO (@Nonnull final IHasFilename aFilenameProvider)
  {
    super (DAOWebFileIO.getInstance ());
    m_aFilenameProvider = ValueEnforcer.notNull (aFilenameProvider, "FilenameProvider");
  }

  /**
   * @return The filename provider used internally to build filenames. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final IHasFilename getFilenameProvider ()
  {
    return m_aFilenameProvider;
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
   *        The XML document to read from. Never <code>null</code>.
   * @return {@link EChange#CHANGED} if reading the data changed something in
   *         the internal structures that requires a writing.
   */
  @Nonnull
  protected abstract EChange onRead (@Nonnull IMicroDocument aDoc);

  @Nonnull
  protected final File getSafeFile (@Nonnull final String sFilename, @Nonnull final EMode eMode) throws DAOException
  {
    ValueEnforcer.notNull (sFilename, "Filename");
    ValueEnforcer.notNull (eMode, "Mode");

    final File aFile = getIO ().getFileIO ().getFile (sFilename);
    if (aFile.exists ())
    {
      // file exist -> must be a file!
      if (!aFile.isFile ())
        throw new DAOException ("The passed filename '" +
                                sFilename +
                                "' is not a file - maybe a directory? Path is '" +
                                aFile.getAbsolutePath () +
                                "'");

      switch (eMode)
      {
        case READ:
          // Check for read-rights
          if (!FileUtils.canRead (aFile))
            throw new DAOException ("The DAO of class " +
                                    getClass ().getName () +
                                    " has no access rights to read from '" +
                                    aFile.getAbsolutePath () +
                                    "'");
          break;
        case WRITE:
          // Check for write-rights
          if (!FileUtils.canWrite (aFile))
            throw new DAOException ("The DAO of class " +
                                    getClass ().getName () +
                                    " has no access rights to write to '" +
                                    aFile.getAbsolutePath () +
                                    "'");
          break;
      }
    }
    else
    {
      // Ensure the parent directory is present
      final File aParentDir = aFile.getParentFile ();
      if (aParentDir != null)
      {
        final FileIOError aError = getIO ().getFileOperationMgr ().createDirRecursiveIfNotExisting (aParentDir);
        if (aError.isFailure ())
          throw new DAOException ("The DAO of class " +
                                  getClass ().getName () +
                                  " failed to create parent directory '" +
                                  aParentDir +
                                  "': " +
                                  aError);
      }
    }

    return aFile;
  }

  /**
   * Call this method inside the constructor to read the file contents directly.
   * This method is write locked!
   * 
   * @throws DAOException
   *         in case initialization or reading failed!
   */
  @MustBeLocked (ELockType.WRITE)
  protected final void initialRead () throws DAOException
  {
    boolean bIsInitialization = false;

    File aFile = null;
    final String sFilename = m_aFilenameProvider.getFilename ();
    if (sFilename == null)
    {
      // required for testing
      s_aLogger.warn ("This DAO of class " + getClass ().getName () + " will not be able to read from a file");

      // do not return - run initialization anyway
    }
    else
    {
      // Check consistency
      aFile = getSafeFile (sFilename, EMode.READ);
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      ESuccess eWriteSuccess = ESuccess.SUCCESS;
      bIsInitialization = aFile == null || !aFile.exists ();
      if (bIsInitialization)
      {
        // initial setup for non-existing file
        if (GlobalDebug.isDebugMode ())
          s_aLogger.info ("Trying to initialize DAO XML file '" + aFile + "'");

        beginWithoutAutoSave ();
        try
        {
          s_aStatsCounterInitTotal.increment ();
          final StopWatch aSW = new StopWatch (true);

          if (onInit ().isChanged ())
            if (aFile != null)
              eWriteSuccess = _writeToFile ();

          s_aStatsCounterInitTimer.addTime (aSW.stopAndGetMillis ());
          s_aStatsCounterInitSuccess.increment ();
          m_nInitCount++;
          m_aLastInitDT = PDTFactory.getCurrentDateTime ();
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
          s_aLogger.info ("Trying to read DAO XML file '" + aFile + "'");

        s_aStatsCounterReadTotal.increment ();
        final IMicroDocument aDoc = MicroReader.readMicroXML (aFile);
        if (aDoc == null)
          s_aLogger.error ("Failed to read XML document from file '" + aFile + "'");
        else
        {
          // Valid XML - start interpreting
          beginWithoutAutoSave ();
          try
          {
            final StopWatch aSW = new StopWatch (true);

            if (onRead (aDoc).isChanged ())
              eWriteSuccess = _writeToFile ();

            s_aStatsCounterReadTimer.addTime (aSW.stopAndGetMillis ());
            s_aStatsCounterReadSuccess.increment ();
            m_nReadCount++;
            m_aLastReadDT = PDTFactory.getCurrentDateTime ();
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
        s_aLogger.warn ("File '" + aFile + "' has pending changes after initialRead!");
    }
    catch (final Throwable t)
    {
      // Custom exception handler for reading
      final IDAOReadExceptionHandler aExceptionHandlerRead = getCustomExceptionHandlerRead ();
      if (aExceptionHandlerRead != null)
      {
        final IReadableResource aRes = aFile == null ? null : new FileSystemResource (aFile);
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
      throw new DAOException ("Error " + (bIsInitialization ? "initializing" : "reading") + " the file '" + aFile + "'",
                              t);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Called after a successful write of the file, if the filename is different
   * from the previous filename. This can e.g. be used to clear old data.
   * 
   * @param sPreviousFilename
   *        The previous filename. May be <code>null</code>.
   * @param sNewFilename
   *        The new filename. Never <code>null</code>.
   */
  @OverrideOnDemand
  @MustBeLocked (ELockType.WRITE)
  protected void onFilenameChange (@Nullable final String sPreviousFilename, @Nonnull final String sNewFilename)
  {}

  /**
   * Create the XML document that should be saved to the file. This method is
   * only called within a write lock!
   * 
   * @return The non-<code>null</code> document to write to the file.
   */
  @Nonnull
  @MustBeLocked (ELockType.WRITE)
  protected abstract IMicroDocument createWriteData ();

  /**
   * Modify the created document by e.g. adding some comment or digital
   * signature or whatsoever.
   * 
   * @param aDoc
   *        The created non-<code>null</code> document.
   */
  @OverrideOnDemand
  @MustBeLocked (ELockType.WRITE)
  protected void modifyWriteData (@Nonnull final IMicroDocument aDoc)
  {
    final IMicroComment aComment = new MicroComment ("This file was generated automatically - do NOT modify!\n" +
                                                     "Written at " +
                                                     PDTToString.getAsString (PDTFactory.getCurrentDateTimeUTC (),
                                                                              Locale.US));
    final IMicroElement eRoot = aDoc.getDocumentElement ();
    // Add a small comment
    if (eRoot != null)
      aDoc.insertBefore (aComment, eRoot);
    else
      aDoc.appendChild (aComment);
  }

  /**
   * Optional callback method that is invoked before the file handle gets
   * opened. This method can e.g. be used to create backups.
   * 
   * @param sFilename
   *        The filename provided by the internal filename provider. Never
   *        <code>null</code>.
   * @param aFile
   *        The resolved file. It is already consistency checked. Never
   *        <code>null</code>.
   */
  @OverrideOnDemand
  @MustBeLocked (ELockType.WRITE)
  protected void beforeWriteToFile (@Nonnull final String sFilename, @Nonnull final File aFile)
  {}

  /**
   * @return The {@link IXMLWriterSettings} to be used to serialize the data.
   */
  @Nonnull
  @OverrideOnDemand
  protected IXMLWriterSettings getXMLWriterSettings ()
  {
    return XMLWriterSettings.DEFAULT_XML_SETTINGS;
  }

  /**
   * @return The filename to which was written last. May be <code>null</code> if
   *         no wrote action was performed yet.
   */
  @Nullable
  public final String getLastFilename ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_sPreviousFilename;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * The main method for writing the new data to a file. This method may only be
   * called within a write lock!
   * 
   * @return {@link ESuccess} and never <code>null</code>.
   */
  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  @MustBeLocked (ELockType.WRITE)
  private ESuccess _writeToFile ()
  {
    // Build the filename to write to
    final String sFilename = m_aFilenameProvider.getFilename ();
    if (sFilename == null)
    {
      // We're not operating on a file! Required for testing
      s_aLogger.warn ("The DAO of class " + getClass ().getName () + " cannot write to a file");
      return ESuccess.FAILURE;
    }

    // Check for a filename change before writing
    if (!sFilename.equals (m_sPreviousFilename))
    {
      onFilenameChange (m_sPreviousFilename, sFilename);
      m_sPreviousFilename = sFilename;
    }

    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Trying to write DAO file '" + sFilename + "'");

    File aFile = null;
    IMicroDocument aDoc = null;
    try
    {
      // Get the file handle
      aFile = getSafeFile (sFilename, EMode.WRITE);

      s_aStatsCounterWriteTotal.increment ();
      final StopWatch aSW = new StopWatch (true);

      // Create XML document to write
      aDoc = createWriteData ();
      if (aDoc == null)
        throw new DAOException ("Failed to create data to write to file");

      // Generic modification
      modifyWriteData (aDoc);

      // Perform optional stuff like backup etc. Must be done BEFORE the output
      // stream is opened!
      beforeWriteToFile (sFilename, aFile);

      // Get the output stream
      final OutputStream aOS = FileUtils.getOutputStream (aFile);
      if (aOS == null)
      {
        // Happens, when another application has the file open!
        // Logger warning already emitted
        throw new DAOException ("Failed to open output stream");
      }

      // Write to file (closes the OS)
      final IXMLWriterSettings aXWS = getXMLWriterSettings ();
      if (MicroWriter.writeToStream (aDoc, aOS, aXWS).isFailure ())
        throw new DAOException ("Failed to write DAO XML data to file");

      s_aStatsCounterWriteTimer.addTime (aSW.stopAndGetMillis ());
      s_aStatsCounterWriteSuccess.increment ();
      m_nWriteCount++;
      m_aLastWriteDT = PDTFactory.getCurrentDateTime ();
      return ESuccess.SUCCESS;
    }
    catch (final Throwable t)
    {
      final String sErrorFilename = aFile != null ? aFile.getAbsolutePath () : sFilename;

      s_aLogger.error ("The DAO of class " +
                       getClass ().getName () +
                       " failed to write the DAO data to '" +
                       sErrorFilename +
                       "'", t);

      // Check if a custom exception handler is present
      final IDAOWriteExceptionHandler aExceptionHandlerWrite = getCustomExceptionHandlerWrite ();
      if (aExceptionHandlerWrite != null)
      {
        final IReadableResource aRes = new FileSystemResource (sErrorFilename);
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
      s_aStatsCounterWriteExceptions.increment ();
      return ESuccess.FAILURE;
    }
  }

  /**
   * This method must be called everytime something changed in the DAO. It
   * triggers the writing to a file if auto-save is active. This method must be
   * called within a write-lock as it is not locked!
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
      {
        s_aLogger.error ("The DAO of class " +
                         getClass ().getName () +
                         " still has pending changes after markAsChanged!");
      }
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
          s_aLogger.error ("The DAO of class " +
                           getClass ().getName () +
                           " still has pending changes after writeToFileOnPendingChanges!");
      }
      finally
      {
        m_aRWLock.writeLock ().unlock ();
      }
    }
  }

  @Nonnegative
  public int getInitCount ()
  {
    return m_nInitCount;
  }

  @Nullable
  public final DateTime getLastInitDateTime ()
  {
    return m_aLastInitDT;
  }

  @Nonnegative
  public int getReadCount ()
  {
    return m_nReadCount;
  }

  @Nullable
  public final DateTime getLastReadDateTime ()
  {
    return m_aLastReadDT;
  }

  @Nonnegative
  public int getWriteCount ()
  {
    return m_nWriteCount;
  }

  @Nullable
  public final DateTime getLastWriteDateTime ()
  {
    return m_aLastWriteDT;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("filenameProvider", m_aFilenameProvider)
                            .append ("previousFilename", m_sPreviousFilename)
                            .append ("initCount", m_nInitCount)
                            .appendIfNotNull ("lastInitDT", m_aLastInitDT)
                            .append ("readCount", m_nReadCount)
                            .appendIfNotNull ("lastReadDT", m_aLastReadDT)
                            .append ("writeCount", m_nWriteCount)
                            .appendIfNotNull ("lastWriteDT", m_aLastWriteDT)
                            .toString ();
  }
}
