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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.bridge;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// Note: Make sure that classes like Util or JavaBridge are only accessed if something fails
// The default path should only load the Standalone and the Socket classes
class TCPServerSocket implements ISocketFactory
{

  static int TCP_PORT_BASE = 9267; // try to find a free port in the range 9267,
                                   // ..., 9366
  private ServerSocket sock;
  private int port;
  boolean local;

  public static ISocketFactory create (final String name, final int backlog) throws IOException
  {
    if (name == null)
      throw new NullPointerException ("name");

    boolean local = false;
    int p;

    if (name.startsWith ("INET:"))
      p = Integer.parseInt (name.substring (5));
    else
      if (name.startsWith ("INET_LOCAL:"))
      {
        local = true;
        p = Integer.parseInt (name.substring (11));
      }
      else
        p = Integer.parseInt (name);

    final TCPServerSocket s = new TCPServerSocket (p, backlog, local);
    return s;
  }

  private ServerSocket newServerSocket (final int port, final int backlog) throws java.io.IOException
  {
    try
    {
      if (local)
        return new ServerSocket (port, backlog, InetAddress.getByName ("127.0.0.1"));
    }
    catch (final java.net.UnknownHostException e)
    {/* cannot happen */}
    return new ServerSocket (port, backlog);
  }

  private void findFreePort (final int start, final int backlog)
  {
    for (int port = start; port < start + 100; port++)
    {
      try
      {
        this.sock = newServerSocket (port, backlog);
        this.port = port;
        return;
      }
      catch (final IOException e)
      {
        continue;
      }

    }
  }

  private TCPServerSocket (final int port, final int backlog, final boolean local) throws IOException
  {
    this.local = local;
    if (port == 0)
    {
      findFreePort (TCP_PORT_BASE, backlog);
    }
    else
    {
      this.sock = newServerSocket (port, backlog);
      this.port = port;
    }
  }

  public void close () throws IOException
  {
    sock.close ();
  }

  public Socket accept () throws IOException
  {
    final Socket s = sock.accept ();
    s.setTcpNoDelay (true);
    return s;
  }

  public String getSocketName ()
  {
    return String.valueOf (port);
  }

  @Override
  public String toString ()
  {
    return (local ? "INET_LOCAL:" : "INET:") + getSocketName ();
  }
}
