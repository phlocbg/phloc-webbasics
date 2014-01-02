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


final class PhpParserString extends AbstractPhpString
{
  ParserString st;
  private final JavaBridge bridge;

  /**
   * Create a new php parser string
   * 
   * @param bridge
   *        The JavaBridge
   * @param st
   *        The ParserString
   */
  public PhpParserString (final JavaBridge bridge, final ParserString st)
  {
    this.bridge = bridge;
    getBytes (st);
  }

  private byte [] bytes;

  private void getBytes (final ParserString st)
  {
    if (bytes == null)
    {
      bytes = new byte [st.length];
      System.arraycopy (st.string, st.off, bytes, 0, bytes.length);
    }
  }

  @Override
  public byte [] getBytes ()
  {
    return bytes;
  }

  private String newString (final byte [] b)
  {
    return bridge.getString (b, 0, b.length);
  }

  /**
   * Get the encoded string representation
   * 
   * @return The encoded string.
   */
  @Override
  public String getString ()
  {
    return newString (getBytes ());
  }

  /**
   * Use UTF-8 encoding, for debugging only
   * 
   * @return The string UTF-8 encoded
   */
  @Override
  public String toString ()
  {
    return new String (getBytes (), Util.UTF8);
  }
}
