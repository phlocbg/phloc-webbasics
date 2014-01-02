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

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import php.java.bridge.Util;

/**
 * An output stream which writes data in HTTP chunks.
 * 
 * @author jostb
 */
public class ChunkedOutputStream extends FilterOutputStream
{

  /**
   * Create new new chunked output stream
   * 
   * @param out
   */
  public ChunkedOutputStream (final OutputStream out)
  {
    super (new BufferedOutputStream (out));
  }

  private static final byte [] RN0 = "0\r\n\r\n".getBytes (Util.ISO88591);

  private void writeEOF () throws IOException
  {
    out.write (RN0);
  }

  private final byte [] buf = new byte [8];

  /**
   * Write a length as hex digits.
   * 
   * @param plength
   *        the length, must be > 0
   * @throws IOException
   */
  private void writeHex (final int plength) throws IOException
  {
    int length = plength;
    int i = buf.length - 1;
    for (; length > 0; i--)
    {
      buf[i] = Util.HEX_DIGITS[0xF & length];
      length >>>= 4;
    }
    i++;
    out.write (buf, i, buf.length - i);
  }

  /** {@inheritDoc} */
  @Override
  public void write (final byte [] buf, final int pos, final int len) throws IOException
  {
    if (len == 0)
    {
      writeEOF ();
    }
    else
    {
      writeHex (len);
      out.write (Util.RN);
      out.write (buf, pos, len);
      out.write (Util.RN);
    }
    out.flush ();
  }

  /**
   * Write trailing 0\r\n and flush the underlying output stream
   */
  public void eof () throws IOException
  {
    writeEOF ();
    super.flush ();
  }
}
