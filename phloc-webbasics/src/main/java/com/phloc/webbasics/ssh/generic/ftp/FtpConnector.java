/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.ssh.generic.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.auth.credentials.IAuthCredentials;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.webbasics.ssh.generic.IConnectorFileBased;

public class FtpConnector implements IConnectorFileBased <FTPClient, FTPFile>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FtpConnector.class);
  private final IFtpConnectionDestination m_aDestination;
  private FTPClient m_aChannel;

  public FtpConnector (@Nonnull final String sHostname)
  {
    this (new FftpConnectionDestination (sHostname));
  }

  public FtpConnector (@Nonnull final String sHostname, final boolean bEnterLocalPassiveMode)
  {
    this (new FftpConnectionDestination (sHostname, bEnterLocalPassiveMode));
  }

  public FtpConnector (@Nonnull final String sHostname, final int nPort)
  {
    this (new FftpConnectionDestination (sHostname, nPort));
  }

  public FtpConnector (@Nonnull final String sHostname, final int nPort, final boolean bEnterLocalPassiveMode)
  {
    this (new FftpConnectionDestination (sHostname, nPort, bEnterLocalPassiveMode));
  }

  public FtpConnector (@Nonnull final IFtpConnectionDestination aDestination)
  {
    if (aDestination == null)
      throw new NullPointerException ("destination");
    m_aDestination = aDestination;
  }

  @Nonnull
  public IFtpConnectionDestination getDestination ()
  {
    return m_aDestination;
  }

  @Nullable
  public FTPClient getHandle ()
  {
    return m_aChannel;
  }

  @Nonnull
  public EChange openConnection (@Nonnull final IAuthCredentials aCredentials)
  {
    // already open?
    if (isConnectionOpen ())
      return EChange.UNCHANGED;

    // open connection
    m_aChannel = m_aDestination.openConnection (aCredentials);
    return EChange.valueOf (isConnectionOpen ());
  }

  @Nonnull
  public EChange closeConnection ()
  {
    if (isConnectionOpen () && m_aDestination.closeConnection (m_aChannel).isChanged ())
    {
      m_aChannel = null;
      return EChange.CHANGED;
    }

    return EChange.UNCHANGED;
  }

  public boolean isConnectionOpen ()
  {
    return m_aChannel != null;
  }

  @Nonnull
  public ESuccess getData (@Nonnull final String sFilename, @Nonnull @WillClose final OutputStream aOS)
  {
    try
    {
      if (m_aChannel != null)
        try
        {
          // Does not close the output-stream!
          if (m_aChannel.retrieveFile (sFilename, aOS))
          {
            s_aLogger.info ("Successfully got data '" + sFilename + "'");
            return ESuccess.SUCCESS;
          }
          s_aLogger.warn ("Failed to get data '" + sFilename + "': " + m_aChannel.getReplyString ());
        }
        catch (final IOException ex)
        {
          s_aLogger.error ("Error in get data '" + sFilename + "': " + m_aChannel.getReplyString (), ex);
        }
      return ESuccess.FAILURE;
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Nonnull
  public ESuccess putData (@Nonnull final String sFilename, @Nonnull @WillClose final InputStream aIS)
  {
    try
    {
      if (m_aChannel != null)
        try
        {
          // Does not close the input-stream!
          if (m_aChannel.storeFile (sFilename, aIS))
          {
            s_aLogger.info ("Successfully put data '" + sFilename + "'");
            return ESuccess.SUCCESS;
          }
          s_aLogger.warn ("Failed to put data '" + sFilename + "': " + m_aChannel.getReplyString ());
        }
        catch (final IOException ex)
        {
          s_aLogger.error ("Error putting data '" + sFilename + "': " + m_aChannel.getReplyString (), ex);
        }
      return ESuccess.FAILURE;
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  @Nonnull
  public ESuccess changeWorkingDirectory (final String sDirectory)
  {
    if (m_aChannel != null)
      try
      {
        if (m_aChannel.changeWorkingDirectory (sDirectory))
        {
          s_aLogger.info ("Successfully changed directory to '" + sDirectory + "'");
          return ESuccess.SUCCESS;
        }
        s_aLogger.warn ("Failed to change working directory to '" + sDirectory + "': " + m_aChannel.getReplyString ());
      }
      catch (final IOException ex)
      {
        s_aLogger.error ("Error changing working directory to '" + sDirectory + "': " + m_aChannel.getReplyString (),
                         ex);
      }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public ESuccess changeToParentDirectory ()
  {
    if (m_aChannel != null)
      try
      {
        if (m_aChannel.changeToParentDirectory ())
        {
          s_aLogger.info ("Successfully changed directory to parent directory");
          return ESuccess.SUCCESS;
        }
        s_aLogger.warn ("Failed to change to parent directory: " + m_aChannel.getReplyString ());
      }
      catch (final IOException ex)
      {
        s_aLogger.error ("Error changing to parent directory: " + m_aChannel.getReplyString (), ex);
      }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public ESuccess deleteFile (@Nullable final String sFilename)
  {
    if (m_aChannel != null)
      try
      {
        // listNames may return null
        if (ArrayHelper.contains (m_aChannel.listNames (null), sFilename))
        {
          if (!m_aChannel.deleteFile (sFilename))
          {
            s_aLogger.error ("Failed to delete file '" + sFilename + "': " + m_aChannel.getReplyString ());
            return ESuccess.FAILURE;
          }
          s_aLogger.info ("Successfully deleted file '" + sFilename + "'");
        }

        // Return success if file is not on server
        return ESuccess.SUCCESS;
      }
      catch (final IOException ex)
      {
        s_aLogger.error ("Error deleting file '" + sFilename + "': " + m_aChannel.getReplyString (), ex);
      }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public ESuccess listFiles (@Nullable final IFilter <FTPFile> aFilter, @Nonnull final List <FTPFile> aTargetList)
  {
    if (aTargetList == null)
      throw new NullPointerException ("targetList");

    if (m_aChannel != null)
    {
      try
      {
        final FTPFile [] aFiles = m_aChannel.listFiles (null, FTPFileFilterFromIFilter.create (aFilter));
        for (final FTPFile aFile : aFiles)
          aTargetList.add (aFile);
        s_aLogger.info ("Successfully listed " + aTargetList.size () + " files");
        return ESuccess.SUCCESS;
      }
      catch (final IOException ex)
      {
        s_aLogger.error ("Error listing files: " + m_aChannel.getReplyString (), ex);
      }
    }
    return ESuccess.FAILURE;
  }
}
