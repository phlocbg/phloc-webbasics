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

package php.java.script;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier and others.
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

import javax.servlet.ServletException;

import php.java.bridge.Util;
import php.java.bridge.http.AbstractHeaderParser;

/**
 * Read data from a URL or from a servlet and write the result to the output
 * stream and a header parser.
 * 
 * @author jostb
 */
public interface IScriptReader
{
  /** These header values appear in the environment map passed to PHP */
  String [] HEADER = new String [] { Util.X_JAVABRIDGE_CONTEXT,
                                    Util.X_JAVABRIDGE_OVERRIDE_HOSTS,
                                    Util.X_JAVABRIDGE_INCLUDE_ONLY,
                                    Util.X_JAVABRIDGE_INCLUDE,
                                    Util.X_JAVABRIDGE_REDIRECT,
                                    Util.X_JAVABRIDGE_OVERRIDE_HOSTS_REDIRECT };

  /**
   * Read from the URL and write the data to out.
   * 
   * @param env
   *        The environment, must contain values for X_JAVABRIDGE_CONTEXT. It
   *        may contain X_JAVABRIDGE_OVERRIDE_HOSTS.
   * @param out
   *        The OutputStream.
   * @param headerParser
   *        The header parser
   * @throws IOException
   * @throws ServletException
   */
  void read (Map <String, String> env, OutputStream out, AbstractHeaderParser headerParser) throws IOException;
}
