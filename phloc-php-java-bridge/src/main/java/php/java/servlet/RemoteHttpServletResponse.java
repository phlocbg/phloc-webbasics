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

package php.java.servlet;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * A servlet response which writes its output to an internal buffer. The buffer
 * can be fetched using "getBufferContents()". May be used by remote PHP scripts
 * (those accessing PhpJavaServlet) through the
 * "java_context()->getHttpServletResponse()" API. Also used by the
 * "java_virtual()" API.
 * 
 * @author jostb
 */
public final class RemoteHttpServletResponse extends HttpServletResponseWrapper implements BufferedResponse
{
  private final ByteArrayOutputStream buffer;
  private ServletOutputStream out = null;
  private PrintWriter writer = null;
  private boolean committed;

  public RemoteHttpServletResponse (final HttpServletResponse res)
  {
    super (res);
    this.buffer = new ByteArrayOutputStream ();
  }

  public byte [] getBufferContents () throws IOException
  {
    committed = true;
    flushBuffer ();
    return buffer.toByteArray ();
  }

  @Override
  public void flushBuffer () throws IOException
  {
    getWriter ().flush ();
  }

  @Override
  public int getBufferSize ()
  {
    return buffer.size ();
  }

  @Override
  public ServletOutputStream getOutputStream () throws IOException
  {
    if (out != null)
      return out;
    return out = new ServletOutputStream ()
    {
      @Override
      public void write (final byte [] arg0, final int arg1, final int arg2) throws IOException
      {
        buffer.write (arg0, arg1, arg2);
      }

      @Override
      public void write (final int arg0) throws IOException
      {
        buffer.write (arg0);
      }
    };
  }

  @Override
  public PrintWriter getWriter () throws IOException
  {
    if (writer != null)
      return writer;
    return writer = new PrintWriter (getOutputStream ());
  }

  @Override
  public boolean isCommitted ()
  {
    return committed;
  }

  @Override
  public void reset ()
  {
    buffer.reset ();
  }

  @Override
  public void resetBuffer ()
  {
    reset ();
  }

  @Override
  public void setBufferSize (final int arg0)
  {}
}
