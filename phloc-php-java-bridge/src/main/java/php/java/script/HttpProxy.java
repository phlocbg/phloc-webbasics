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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Map;

import php.java.bridge.ILogger;
import php.java.bridge.Util.Process.PhpException;
import php.java.bridge.http.HeaderParser;

/**
 * Represents the script continuation. This class can be used to allocate php
 * scripts on a HTTP server. Although this class accidentally inherits from
 * <code>CGIRunner</code> it doesn't necessarily run CGI binaries. If you pass a
 * URLReader, it calls its read method which opens a URLConnection to the remote
 * server and holds the allocated remote script instance hostage until release
 * is called.
 * 
 * @author jostb
 */
public class HttpProxy extends CGIRunner
{
  /**
   * Create a HTTP proxy which can be used to allocate a php script from a HTTP
   * server
   * 
   * @param reader
   *        - The reader, for example a URLReader
   * @param env
   *        - The environment, must contain values for X_JAVABRIDGE_CONTEXT. It
   *        may contain X_JAVABRIDGE_OVERRIDE_HOSTS.
   * @param out
   *        - The OutputStream
   * @param err
   *        The error stream
   * @param headerParser
   *        The header parser
   * @param resultProxy
   *        The return value proxy
   */
  public HttpProxy (final Reader reader,
                    final Map <String, String> env,
                    final OutputStream out,
                    final OutputStream err,
                    final HeaderParser headerParser,
                    final ResultProxy resultProxy,
                    final ILogger logger)
  {
    super (reader, env, out, err, headerParser, resultProxy);
  }

  @Override
  protected void doRun () throws IOException, PhpException
  {
    if (reader instanceof URLReader)
    {
      ((URLReader) reader).read (env, out, headerParser);
    }
    else
    {
      super.doRun ();
    }
  }
}
