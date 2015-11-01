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

import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.phloc.commons.state.EChange;
import com.phloc.commons.stats.IStatisticsHandlerCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.webbasics.ssh.connection.IServerConnectionSettingsPassword;

@Immutable
public final class SftpSessionFactory
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SftpSessionFactory.class);
  private static final IStatisticsHandlerCounter s_aStatsCounterCreated = StatisticsManager.getCounterHandler (SftpSessionFactory.class +
                                                                                                               "$created");
  private static final IStatisticsHandlerCounter s_aStatsCounterDestroyed = StatisticsManager.getCounterHandler (SftpSessionFactory.class +
                                                                                                                 "$created");

  private SftpSessionFactory ()
  {}

  @Nonnull
  private static String _debugSession (@Nonnull final Session aSession)
  {
    return "[Session@" +
           Integer.toHexString (System.identityHashCode (aSession)) +
           " - " +
           (aSession.isConnected () ? "connected" : "free") +
           "]";
  }

  @Nonnull
  public static Session createSession (@Nonnull final IServerConnectionSettingsPassword aSettings) throws JSchException
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");

    final Session aSession = new JSch ().getSession (aSettings.getUserName (),
                                                     aSettings.getServerAddress (),
                                                     aSettings.getServerPort ());
    aSession.setPassword (aSettings.getPassword ());

    //
    // Setup Strict HostKeyChecking to no so we don't get the
    // unknown host key exception
    //
    final Properties aConfig = new Properties ();
    aConfig.put ("StrictHostKeyChecking", "no");
    aSession.setConfig (aConfig);
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Created new session " + _debugSession (aSession));
    aSession.connect ();
    s_aStatsCounterCreated.increment ();
    return aSession;
  }

  @Nonnull
  public static EChange destroySession (@Nonnull final Session aSession)
  {
    if (aSession == null)
      throw new NullPointerException ("session");

    if (!aSession.isConnected ())
      return EChange.UNCHANGED;

    aSession.disconnect ();
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Closed session " + _debugSession (aSession));
    s_aStatsCounterDestroyed.increment ();
    return EChange.CHANGED;
  }
}
