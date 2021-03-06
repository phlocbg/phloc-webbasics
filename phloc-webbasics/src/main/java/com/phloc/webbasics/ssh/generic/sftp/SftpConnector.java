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
package com.phloc.webbasics.ssh.generic.sftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.phloc.appbasics.auth.credentials.IAuthCredentials;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;
import com.phloc.webbasics.ssh.generic.IConnectorFileBased;

public class SftpConnector implements IConnectorFileBased <ChannelSftp, ChannelSftp.LsEntry>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SftpConnector.class);
  private final ISftpConnectionDestination m_aDestination;
  private ChannelSftp m_aChannel;

  public SftpConnector (@Nonnull final String sHostname)
  {
    this (new SftpConnectionDestination (sHostname));
  }

  public SftpConnector (@Nonnull final String sHostname, final int nPort)
  {
    this (new SftpConnectionDestination (sHostname, nPort));
  }

  public SftpConnector (@Nonnull final ISftpConnectionDestination aDestination)
  {
    if (aDestination == null)
      throw new NullPointerException ("destination");
    m_aDestination = aDestination;
  }

  @Nonnull
  public ISftpConnectionDestination getDestination ()
  {
    return m_aDestination;
  }

  @Nullable
  public ChannelSftp getHandle ()
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
  public ESuccess getData (@Nonnull final String sFilename, @Nonnull final OutputStream aOS)
  {
    try
    {
      if (m_aChannel != null)
        try
        {
          m_aChannel.get (sFilename, aOS);
          s_aLogger.info ("Successfully got data '" + sFilename + "'");
          return ESuccess.SUCCESS;
        }
        catch (final SftpException ex)
        {
          s_aLogger.error ("Error in get data '" + sFilename + "'", ex);
        }
      return ESuccess.FAILURE;
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Nonnull
  public ESuccess putData (@Nonnull final String sFilename, @Nonnull final InputStream aIS)
  {
    try
    {
      if (m_aChannel != null)
        try
        {
          m_aChannel.put (aIS, sFilename);
          s_aLogger.info ("Successfully put data '" + sFilename + "'");
          return ESuccess.SUCCESS;
        }
        catch (final SftpException ex)
        {
          s_aLogger.error ("Error putting data '" + sFilename + "'", ex);
        }
      return ESuccess.FAILURE;
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  @Nonnull
  public ESuccess changeWorkingDirectory (@Nonnull final String sDirectory)
  {
    if (m_aChannel != null)
      try
      {
        m_aChannel.cd (sDirectory);
        s_aLogger.info ("Successfully changed directory to '" + sDirectory + "'");
        return ESuccess.SUCCESS;
      }
      catch (final SftpException ex)
      {
        s_aLogger.error ("Error changing directory to '" + sDirectory + "'", ex);
      }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public ESuccess changeToParentDirectory ()
  {
    return changeWorkingDirectory ("..");
  }

  @Nonnull
  public ESuccess deleteFile (@Nonnull final String sFilename)
  {
    if (m_aChannel != null)
      try
      {
        m_aChannel.rm (sFilename);
        s_aLogger.info ("Successfully deleted file '" + sFilename + "'");
        return ESuccess.SUCCESS;
      }
      catch (final SftpException ex)
      {
        s_aLogger.error ("Error deleting file '" + sFilename + "'", ex);
      }
    return ESuccess.FAILURE;
  }

  @Nonnull
  public ESuccess listFiles (@Nullable final IFilter <ChannelSftp.LsEntry> aFilter,
                             @Nonnull final List <ChannelSftp.LsEntry> aTargetList)
  {
    if (aTargetList == null)
      throw new NullPointerException ("targetList");

    if (m_aChannel != null)
    {
      try
      {
        final Vector <?> aFiles = m_aChannel.ls (".");
        for (final Object aFile : aFiles)
        {
          final ChannelSftp.LsEntry aEntry = (ChannelSftp.LsEntry) aFile;
          if (aFilter == null || aFilter.matchesFilter (aEntry))
            aTargetList.add (aEntry);
        }
        s_aLogger.info ("Successfully listed " + aTargetList.size () + " files");
        return ESuccess.SUCCESS;
      }
      catch (final SftpException ex)
      {
        s_aLogger.error ("Error listing files", ex);
      }
    }
    return ESuccess.FAILURE;
  }
}
