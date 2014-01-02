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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache [Entry(byte[], enc) -> String]. No synchronization, so use this class
 * per thread or per request only.
 */
final class StringCache
{
  private Map <Entry, String> map;
  @SuppressWarnings ("unused")
  private final JavaBridge bridge;

  private void init ()
  {
    map = new HashMap <Entry, String> ();
  }

  /**
   * Create a new StringCache
   * 
   * @param bridge
   *        The JavaBridge
   */
  public StringCache (final JavaBridge bridge)
  {
    this.bridge = bridge;
    init ();
  }

  /**
   * A cache entry.
   */
  private static final class Entry
  {
    byte [] name;
    final Charset enc;
    int start;
    final int length;
    private boolean hasResult = false;
    private int result = 1;

    private Entry (final byte [] name, final int start, final int length, final Charset enc)
    {
      this.name = name;
      this.start = start;
      this.length = length;
      // how to check that enc is really a symbol?
      this.enc = enc;
    }

    @Override
    public int hashCode ()
    {
      if (hasResult)
        return result;
      final int d = start + length;
      for (int i = start; i < d; i++)
      {
        result = result * 7 + name[i];
      }
      result = result * 31 + length;
      result = result * 31 + enc.hashCode ();
      hasResult = true;
      return result;
    }

    @Override
    public boolean equals (final Object o)
    {
      if (o == this)
        return true;
      if (!(o instanceof Entry))
        return false;
      final Entry that = (Entry) o;
      if (enc != that.enc)
        return false;
      if (length != that.length)
        return false;
      for (int i = length; i-- > 0;)
      {
        // that.name.start is always 0
        if (name[i + start] != that.name[i])
          return false;
      }
      return true;
    }

    @Override
    public String toString ()
    {
      return new String (name, start, length, enc);
    }
  }

  /**
   * Get the method for the entry
   * 
   * @param entry
   *        The entry
   * @return The method
   */
  private String get (final Entry entry)
  {
    return map.get (entry);
  }

  /**
   * Store a constructor with an entry
   * 
   * @param entry
   *        The cache entry
   * @param method
   *        The method
   */
  private void put (final Entry entry, final String method)
  {
    final byte [] b = new byte [entry.length];
    System.arraycopy (entry.name, entry.start, b, 0, entry.length);
    entry.start = 0;
    entry.name = b;
    map.put (entry, method);
  }

  private static Entry _createEntry (final byte [] name, final int start, final int length, final Charset enc)
  {
    return new Entry (name, start, length, enc);
  }

  private static String _createString (final byte [] name, final int start, final int length, final Charset encoding)
  {
    return new String (name, start, length, encoding);
  }

  /**
   * Get a string from the string cache.
   * 
   * @param name
   *        The representation of the string
   * @param start
   *        The start position within the byte array
   * @param length
   *        The length of the array
   * @param encoding
   *        The file.encoding.
   * @return the cached string.
   */
  public String getString (final byte [] name, final int start, final int length, final Charset encoding)
  {
    final Entry e = _createEntry (name, start, length, encoding);
    String s = get (e);
    if (s == null)
    {
      s = _createString (name, start, length, encoding);
      put (e, s);
    }
    return s;
  }

  /**
   * Removes all mappings from this cache.
   */
  public void clear ()
  {
    init ();
  }
}
