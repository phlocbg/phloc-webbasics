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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Maps php iterator to java iterator.
 * 
 * @author jostb
 */
abstract class PhpMap
{
  JavaBridge _bridge;
  Object value;
  Class <?> componentType;
  boolean keyType; // false: key is integer (array), true: key is string (hash)

  protected PhpMap (final JavaBridge bridge, final Object value, final boolean keyType)
  {
    this._bridge = bridge;
    this.value = value;
    this.keyType = keyType;
    this.componentType = value.getClass ().getComponentType ();
    init ();
  }

  protected Object coerce (final Object val)
  {
    return _bridge.coerce (componentType, val, _bridge.request.response);
  }

  protected abstract void init ();

  /**
   * Returns the object at the current position.
   * 
   * @return The current object.
   */
  public abstract Object currentData ();

  /**
   * Returns the key at the current position.
   * 
   * @return The current key, either a string or a number.
   */
  public abstract Object currentKey ();

  /**
   * Forward one element.
   * 
   * @return true if move was possible, false otherwise.
   */
  public abstract boolean moveForward ();

  /**
   * Checks if it is possible to advance one element
   * 
   * @return true if next element exists, false otherwise
   */
  public abstract boolean hasMore ();

  /**
   * Returns the key type.
   * 
   * @return false if key is integer (array index), true if key is string (hash
   *         key)
   */
  public boolean getType ()
  {
    return keyType;
  }

  /**
   * Returns a PhpMap for a given value.
   * 
   * @param value
   *        The value, must be an array or implement Map or Collection
   * @param bridge
   *        The bridge instance
   * @return The PhpMap
   */
  public static PhpMap getPhpMap (final Object value, final JavaBridge bridge)
  {
    if (bridge.logLevel > 3)
      bridge.logDebug ("returning map for " + value.getClass ());

    if (value.getClass ().isArray ())
    {
      return new PhpMap (bridge, value, false)
      {
        boolean valid;
        int i;
        int length;

        @Override
        protected void init ()
        {
          i = 0;
          length = Array.getLength (this.value);
          valid = length > 0;
        }

        @Override
        public Object currentData ()
        {
          if (!valid)
            return null;
          return Array.get (this.value, i);
        }

        @Override
        public Object currentKey ()
        {
          if (!valid)
            return null;
          return _bridge.castToExact (new Integer (i));
        }

        @Override
        public boolean moveForward ()
        {
          valid = ++i < length;
          return valid ? true : false;
        }

        @Override
        public boolean hasMore ()
        {
          return valid ? true : false;
        }
      };
    }
    if (value instanceof Collection)
    {
      return new PhpMap (bridge, value, false)
      {
        int i;
        boolean valid;
        Iterator iter;

        @Override
        protected void init ()
        {
          iter = ((Collection) (this.value)).iterator ();
          i = 0;
          valid = false;
          if (iter.hasNext ())
          {
            valid = true;
            this.value = iter.next ();
          }
        }

        @Override
        public Object currentData ()
        {
          return this.value;
        }

        @Override
        public Object currentKey ()
        {
          return _bridge.castToExact (new Integer (i));
        }

        @Override
        public boolean moveForward ()
        {
          if (iter.hasNext ())
          {
            i++;
            this.value = iter.next ();
            return valid = true;
          }
          else
          {
            return valid = false;
          }
        }

        @Override
        public boolean hasMore ()
        {
          return valid;
        }
      };
    }
    if (value instanceof Map)
    {
      return new PhpMap (bridge, value, true)
      {
        Object currentKey;
        Iterator iter;

        @Override
        protected void init ()
        {
          iter = ((Map) (this.value)).keySet ().iterator ();
          currentKey = null;
          if (iter.hasNext ())
          {
            currentKey = iter.next ();
          }
        }

        @Override
        public Object currentData ()
        {
          if (currentKey == null)
            return null;
          return ((Map) (this.value)).get (currentKey);
        }

        @Override
        public Object currentKey ()
        {
          return new SimplePhpString (_bridge, String.valueOf (currentKey));
        }

        @Override
        public boolean moveForward ()
        {
          currentKey = iter.hasNext () ? iter.next () : null;
          return currentKey == null ? false : true;
        }

        @Override
        public boolean hasMore ()
        {
          return currentKey == null ? false : true;
        }
      };
    }
    return null;
  }

}
