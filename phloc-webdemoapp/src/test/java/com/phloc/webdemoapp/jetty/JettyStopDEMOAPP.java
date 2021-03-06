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
package com.phloc.webdemoapp.jetty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JettyStopDEMOAPP
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (JettyStopDEMOAPP.class);

  public static void main (final String [] args) throws IOException
  {
    try
    {
      final Socket s = new Socket (InetAddress.getByName (null), JettyMonitor.STOP_PORT);
      s.setSoLinger (false, 0);

      final OutputStream out = s.getOutputStream ();
      s_aLogger.info ("Sending jetty stop request");
      out.write ((JettyMonitor.STOP_KEY + "\r\nstop\r\n").getBytes ());
      out.flush ();
      s.close ();
    }
    catch (final ConnectException ex)
    {
      s_aLogger.warn ("Jetty is not running");
    }
  }
}
