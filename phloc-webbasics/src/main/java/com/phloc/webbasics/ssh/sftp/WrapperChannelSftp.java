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
package com.phloc.webbasics.ssh.sftp;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.phloc.commons.state.ESuccess;
import com.phloc.webbasics.ssh.connection.IServerConnectionSettingsPassword;

@Immutable
public final class WrapperChannelSftp
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WrapperChannelSftp.class);

  private WrapperChannelSftp ()
  {}

  /**
   * Upload a file to the server.
   * 
   * @param aSettings
   *        Connections settings to the server.
   * @param aRunnable
   *        The callback that performs the actions via SFTP. May not be
   *        <code>null</code>.
   * @return {@link ESuccess#SUCCESS} if operation succeeded,
   *         {@link ESuccess#FAILURE} otherwise.
   * @throws JSchException
   *         If some general connection handling stuff goes wrong.
   */
  @Nonnull
  public static ESuccess execute (@Nonnull final IServerConnectionSettingsPassword aSettings,
                                  @Nonnull final IChannelSftpRunnable aRunnable) throws JSchException
  {
    if (aRunnable == null)
      throw new NullPointerException ("runnable");

    Session aSession = null;
    Channel aChannel = null;
    ChannelSftp aSFTPChannel = null;
    try
    {
      // get session from pool
      aSession = SftpSessionFactory.createSession (aSettings);
      if (aSession == null)
        throw new IllegalStateException ("Failed to get session from pool");

      // Open the SFTP channel
      aChannel = aSession.openChannel ("sftp");
      aChannel.connect ();
      aSFTPChannel = (ChannelSftp) aChannel;

      // call callback
      aRunnable.execute (aSFTPChannel);
      return ESuccess.SUCCESS;
    }
    catch (final SftpException ex)
    {
      s_aLogger.error ("Error peforming SFTP action: " + aRunnable.getDisplayName (), ex);
      return ESuccess.FAILURE;
    }
    finally
    {
      // end SFTP session
      if (aSFTPChannel != null)
        aSFTPChannel.quit ();

      // close channel
      if (aChannel != null && aChannel.isConnected ())
        aChannel.disconnect ();

      // destroy session to the pool
      if (aSession != null)
        SftpSessionFactory.destroySession (aSession);
    }
  }
}
