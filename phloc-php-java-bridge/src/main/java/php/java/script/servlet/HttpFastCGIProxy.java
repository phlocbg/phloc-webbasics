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

package php.java.script.servlet;

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
import java.io.OutputStream;
import java.util.Map;

import php.java.bridge.Util;
import php.java.bridge.http.FCGIConnectionPool;
import php.java.bridge.http.FCGIInputStream;
import php.java.bridge.http.FCGIOutputStream;
import php.java.bridge.http.FCGIUtil;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.AbstractOutputStreamFactory;
import php.java.script.AbstractContinuation;
import php.java.script.ResultProxy;

/**
 * This class can be used to connect to a FastCGI server.
 * 
 * @author jostb
 * @see php.java.script.FastCGIProxy
 */

public class HttpFastCGIProxy extends AbstractContinuation
{
  private final FCGIConnectionPool fcgiConnectionPool;

  public HttpFastCGIProxy (final Map <String, String> env,
                           final OutputStream out,
                           final OutputStream err,
                           final AbstractHeaderParser headerParser,
                           final ResultProxy resultProxy,
                           final FCGIConnectionPool fcgiConnectionPool)
  {
    super (env, out, err, headerParser, resultProxy);
    this.fcgiConnectionPool = fcgiConnectionPool;
  }

  @Override
  protected void doRun () throws IOException, Util.UtilProcess.PhpException
  {
    final byte [] buf = new byte [FCGIUtil.FCGI_BUF_SIZE];

    FCGIInputStream natIn = null;
    FCGIOutputStream natOut = null;

    FCGIConnectionPool.Connection connection = null;

    try
    {
      connection = fcgiConnectionPool.openConnection ();
      natOut = (FCGIOutputStream) connection.getOutputStream ();
      natIn = (FCGIInputStream) connection.getInputStream ();

      natOut.writeBegin ();
      natOut.writeParams (env);
      natOut.write (FCGIUtil.FCGI_STDIN, FCGIUtil.FCGI_EMPTY_RECORD);
      natOut.close ();
      AbstractHeaderParser.parseBody (buf, natIn, new AbstractOutputStreamFactory ()
      {
        @Override
        public OutputStream getOutputStream () throws IOException
        {
          return out;
        }
      }, headerParser);
      natIn.close ();
    }
    catch (final InterruptedException e)
    {
      /* ignore */
    }
    catch (final Throwable t)
    {
      t.printStackTrace ();
    }
  }
}
