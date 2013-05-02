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

package php.java.script;

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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import php.java.bridge.Util;
import php.java.bridge.Util.UtilProcess;
import php.java.bridge.Util.UtilProcessWithErrorHandler;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.AbstractOutputStreamFactory;

/**
 * This class can be used to run a PHP CGI binary. Used only when running local
 * php scripts. To allocate and invoke remote scripts please use a HttpProxy and
 * a URLReader instead.
 * 
 * @author jostb
 * @see php.java.bridge.http.AbstractHttpServer
 * @see php.java.script.URLReader
 * @see php.java.script.HttpProxy
 */

public class CGIRunner extends AbstractContinuation
{
  protected final Reader reader;

  protected CGIRunner (final Reader reader,
                       final Map <String, String> env,
                       final OutputStream out,
                       final OutputStream err,
                       final AbstractHeaderParser headerParser,
                       final ResultProxy resultProxy)
  {
    super (env, out, err, headerParser, resultProxy);
    this.reader = reader;
  }

  private Writer writer;

  @Override
  protected void doRun () throws IOException, Util.UtilProcess.PhpException
  {
    final UtilProcess proc = UtilProcessWithErrorHandler.start (new String [] { null },
                                                                false,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                env,
                                                                true,
                                                                true,
                                                                err);

    InputStream natIn = null;
    try
    {
      natIn = proc.getInputStream ();
      final OutputStream natOut = proc.getOutputStream ();
      writer = new BufferedWriter (new OutputStreamWriter (natOut));

      new Thread ()
      { // write the script asynchronously to avoid deadlock
        public void doRun () throws IOException
        {
          final char [] cbuf = new char [Util.BUF_SIZE];
          int n;
          while ((n = reader.read (cbuf)) != -1)
          {
            // System.err.println("SCRIPT:::"+new String(cbuf, 0, n));
            writer.write (cbuf, 0, n);
          }
        }

        @Override
        public void run ()
        {
          try
          {
            doRun ();
          }
          catch (final IOException e)
          {
            Util.printStackTrace (e);
          }
          finally
          {
            try
            {
              writer.close ();
            }
            catch (final IOException ex)
            {
              /* ignore */
            }
          }
        }
      }.start ();

      final byte [] buf = new byte [Util.BUF_SIZE];
      AbstractHeaderParser.parseBody (buf, natIn, new AbstractOutputStreamFactory ()
      {
        @Override
        public OutputStream getOutputStream () throws IOException
        {
          return out;
        }
      }, headerParser);
      proc.waitFor ();
      resultProxy.setResult (proc.exitValue ());
    }
    catch (final IOException e)
    {
      Util.printStackTrace (e);
      throw e;
    }
    catch (final InterruptedException e)
    {
      /* ignore */
    }
    finally
    {
      if (natIn != null)
        try
        {
          natIn.close ();
        }
        catch (final IOException ex)
        {/* ignore */}
      try
      {
        proc.destroy ();
      }
      catch (final Exception e)
      {
        Util.printStackTrace (e);
      }
    }

    proc.checkError ();
  }
}
