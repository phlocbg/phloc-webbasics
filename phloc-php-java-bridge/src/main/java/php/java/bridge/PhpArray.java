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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

final class PhpArray extends AbstractMap <Object, Object>
{
  // for PHP's array()
  private TreeMap <Integer, Object> t = new TreeMap <Integer, Object> (Request.PHP_ARRAY_KEY_COMPARATOR);
  private Map <Object, Object> m = null;

  @Override
  public Object put (final Object key, final Object value)
  {
    if (m != null)
      return m.put (key, value);
    try
    {
      return t.put ((Integer) key, value);
    }
    catch (final ClassCastException e)
    {
      m = new HashMap <Object, Object> (t);
      t = null;
      return m.put (key, value);
    }
  }

  @Override
  public Set entrySet ()
  {
    if (t != null)
      return t.entrySet ();
    return m.entrySet ();
  }

  public int arraySize ()
  {
    if (t != null)
    {
      if (t.size () == 0)
        return 0;
      return 1 + t.lastKey ().intValue ();
    }
    throw new IllegalArgumentException ("The passed PHP \"array\" is not a sequence but a dictionary");
  }
}
