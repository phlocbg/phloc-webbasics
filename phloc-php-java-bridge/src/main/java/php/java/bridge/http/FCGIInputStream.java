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

import php.java.bridge.Util;

/**
 * A FastCGI input stream
 * 
 * @author jostb
 */
public class FCGIInputStream extends FCGIConnectionInputStream
{
  private final IFCGIProcessFactory processFactory;

  /**
   * Create
   * 
   * @param processFactory
   */
  public FCGIInputStream (final IFCGIProcessFactory processFactory)
  {
    this.processFactory = processFactory;
  }

  private StringBuilder error;

  public StringBuilder getError ()
  {
    return error;
  }

  public String checkError ()
  {
    return error == null ? null : Util.checkError (error.toString ());
  }

  @Override
  public int read (final byte buf[]) throws FCGIConnectionException
  {
    try
    {
      return doRead (buf);
    }
    catch (final FCGIConnectionException ex)
    {
      throw ex;
    }
    catch (final IOException e)
    {
      throw new FCGIConnectionException (connection, e);
    }
  }

  private final byte header[] = new byte [FCGIUtil.FCGI_HEADER_LEN];

  public int doRead (final byte buf[]) throws IOException
  {
    int n, i;
    // assert if(buf.length!=FCGI_BUF_SIZE) throw new
    // IOException("Invalid block size");
    for (n = 0; (i = read (header, n, FCGIUtil.FCGI_HEADER_LEN - n)) > 0;)
      n += i;
    if (FCGIUtil.FCGI_HEADER_LEN != n)
      throw new IOException ("Protocol error");
    final int type = header[1] & 0xFF;
    int contentLength = ((header[4] & 0xFF) << 8) | (header[5] & 0xFF);
    final int paddingLength = header[6] & 0xFF;
    switch (type)
    {
      case FCGIUtil.FCGI_STDERR:
      case FCGIUtil.FCGI_STDOUT:
      {
        for (n = 0; (i = read (buf, n, contentLength - n)) > 0;)
          n += i;
        if (n != contentLength)
          throw new IOException ("Protocol error while reading FCGI data");
        if (type == FCGIUtil.FCGI_STDERR)
        {
          final String s = new String (buf, 0, n, Util.ASCII);
          this.processFactory.log (s);
          contentLength = 0;

          if (error == null)
            error = new StringBuilder (s);
          else
            error.append (s);
        }
        if (paddingLength > 0)
        {
          final byte b[] = new byte [paddingLength];
          for (n = 0; (i = read (b, n, b.length - n)) > 0;)
            n += i;
          if (n != paddingLength)
            throw new IOException ("Protocol error while reading FCGI padding");
        }
        return contentLength;
      }
      case FCGIUtil.FCGI_END_REQUEST:
      {
        for (n = 0; (i = read (buf, n, contentLength - n)) > 0;)
          n += i;
        if (n != contentLength)
          throw new IOException ("Protocol error while reading EOF data");
        if (paddingLength > 0)
        {
          n = super.read (buf, 0, paddingLength);
          if (n != paddingLength)
            throw new IOException ("Protocol error while reading EOF padding");
        }
        return -1;
      }
    }
    throw new IOException ("Received unknown type");
  }
}
