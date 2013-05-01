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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/** numbers are base 16 */
class HexOutputBuffer extends ByteArrayOutputStream
{
  /**
     * 
     */
  private final JavaBridge bridge;

  HexOutputBuffer (final JavaBridge bridge)
  {
    this.bridge = bridge;
  }

  /*
   * Return up to 256 bytes. Useful for logging.
   */
  protected byte [] getFirstBytes ()
  {
    int c = super.count;
    final byte [] append = (c > 256) ? Response.append_for_OutBuf_getFirstBytes
                                    : Response.append_none_for_OutBuf_getFirstBytes;
    if (c > 256)
      c = 256;
    final byte [] ret = new byte [c + append.length];
    System.arraycopy (super.buf, 0, ret, 0, c);
    System.arraycopy (append, 0, ret, ret.length - append.length, append.length);
    return ret;
  }

  protected void append (final byte [] s)
  {
    try
    {
      write (s);
    }
    catch (final IOException e)
    {/* not possible */}
  }

  protected void appendQuoted (final byte [] s)
  {
    for (final byte element : s)
    {
      byte ch;
      switch (ch = element)
      {
        case '&':
          append (Response.amp);
          break;
        case '\"':
          append (Response.quote);
          break;
        default:
          write (ch);
      }
    }
  }

  protected void appendQuoted (final String s)
  {
    appendQuoted (bridge.options.getBytes (s));
  }

  private final byte [] buf = new byte [16];

  /** append an unsigned long number */
  protected void append (long i)
  {
    int pos = 16;
    do
    {
      buf[--pos] = Util.HEX_DIGITS[(int) (i & 0xF)];
      i >>>= 4;
    } while (i != 0);
    write (buf, pos, 16 - pos);
  }

  /**
   * append a double value, base 10 for now (not every C compiler supports the
   * C99 "a" conversion)
   */
  protected void append (final double d)
  {
    append (Double.toString (d).getBytes ());
  }

  /** append a long number */
  protected void appendLong (final long l)
  {
    append (Response.L);
    if (l < 0)
    {
      append (-l);
      append (Response.pa);
    }
    else
    {
      append (l);
      append (Response.po);
    }
  }

  /** append a string */
  protected void appendString (final byte s[])
  {
    append (Response.S);
    appendQuoted (s);
  }
}
