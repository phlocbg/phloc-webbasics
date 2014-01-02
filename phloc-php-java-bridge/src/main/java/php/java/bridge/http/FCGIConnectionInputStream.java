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

package php.java.bridge.http;

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
import java.io.InputStream;

import php.java.bridge.NotImplementedException;
import php.java.bridge.http.FCGIConnectionPool.Connection;

/**
 * Default InputStream used by the connection pool.
 * 
 * @author jostb
 */
public class FCGIConnectionInputStream extends InputStream
{
  protected Connection connection;
  private InputStream in;

  protected void setConnection (final Connection connection) throws FCGIConnectionException
  {
    this.connection = connection;
    try
    {
      this.in = connection.channel.getInputStream ();
    }
    catch (final IOException e)
    {
      throw new FCGIConnectionException (connection, e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int read (final byte buf[]) throws FCGIConnectionException
  {
    return read (buf, 0, buf.length);
  }

  /** {@inheritDoc} */
  @Override
  public int read (final byte buf[], final int off, final int buflength) throws FCGIConnectionException
  {
    try
    {
      final int count = in.read (buf, off, buflength);
      if (count == -1)
      {
        connection.setIsClosed ();
      }
      return count;
    }
    catch (final IOException ex)
    {
      throw new FCGIConnectionException (connection, ex);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int read () throws FCGIConnectionException
  {
    throw new NotImplementedException ();
  }

  /** {@inheritDoc} */
  @Override
  public void close () throws FCGIConnectionException
  {
    connection.state |= 1;
    if (connection.state == connection.ostate)
      connection.close ();
  }
}
