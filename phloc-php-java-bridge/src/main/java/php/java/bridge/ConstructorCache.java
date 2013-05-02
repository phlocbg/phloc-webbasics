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

package php.java.bridge;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache [Entry(method, parameters) -> Method]. No synchronization, so use this
 * class per thread or per request only.
 */
final class ConstructorCache
{
  private static final Entry noCache = new NoCache ();
  private Map <Entry, CachedConstructor> map;

  private void _init ()
  {
    map = new HashMap <Entry, CachedConstructor> ();
  }

  /**
   * Create a new ConstructorCache
   */
  public ConstructorCache ()
  {
    _init ();
  }

  private static final class CachedConstructor
  {
    private final Constructor <?> method;
    private Class <?> [] typeCache;

    public CachedConstructor (final Constructor <?> method)
    {
      this.method = method;
    }

    public Constructor <?> get ()
    {
      return method;
    }

    public Class <?> [] getParameterTypes ()
    {
      if (typeCache != null)
        return typeCache;
      return typeCache = method.getParameterTypes ();
    }
  }

  /**
   * A cache entry
   */
  public static class Entry
  {
    private String name;
    private Class <?> params[];
    private boolean hasResult = false;
    private int result = 1;
    private CachedConstructor cache;

    protected Entry ()
    {}

    protected Entry (final String name, final Class <?> params[])
    {
      this.name = name; // intern() is ~10% slower than lazy string comparison
      this.params = params;
    }

    @Override
    public int hashCode ()
    {
      if (hasResult)
        return result;
      for (final Class <?> param : params)
      {
        result = result * 31 + (param == null ? 0 : param.hashCode ());
      }
      result = result * 31 + name.hashCode ();
      hasResult = true;
      return result;
    }

    @Override
    public boolean equals (final Object o)
    {
      if (o == this)
        return true;
      if (o == null || !getClass ().equals (o.getClass ()))
        return false;
      final Entry rhs = (Entry) o;
      if (params.length != rhs.params.length)
        return false;
      if (!name.equals (rhs.name))
        return false;
      for (int i = 0; i < params.length; i++)
      {
        if (params[i] != rhs.params[i])
          return false;
      }
      return true;
    }

    public void setMethod (final CachedConstructor cache)
    {
      this.cache = cache;
    }

    /**
     * @param method
     * @return
     */
    public Class <?> [] getParameterTypes (final Constructor <?> method)
    {
      return cache.getParameterTypes ();
    }
  }

  private static final class NoCache extends Entry
  {
    @Override
    public Class <?> [] getParameterTypes (final Constructor <?> method)
    {
      return method.getParameterTypes ();
    }
  }

  /**
   * Get the constructor for the entry
   * 
   * @param entry
   *        The entry
   * @return The constructor
   */
  public Constructor <?> get (final Entry entry)
  {
    if (entry == noCache)
      return null;
    final CachedConstructor cache = map.get (entry);
    if (cache == null)
      return null;
    entry.setMethod (cache);
    return cache.get ();
  }

  /**
   * Store a constructor with an entry
   * 
   * @param entry
   *        The cache entry
   * @param method
   *        The constructor
   */
  public void put (final Entry entry, final Constructor <?> method)
  {
    if (entry != noCache)
    {
      final CachedConstructor cache = new CachedConstructor (method);
      entry.setMethod (cache);
      map.put (entry, cache);
    }
  }

  /**
   * Get a cache entry from a name args pair
   * 
   * @param name
   *        The constructor name
   * @param args
   *        The arguments
   * @return A cache entry.
   */
  public Entry getEntry (final String name, final Object [] args)
  {
    final Class <?> params[] = new Class <?> [args.length];
    for (int i = 0; i < args.length; i++)
    {
      final Class <? extends Object> c = args[i] == null ? null : args[i].getClass ();
      if (c == PhpArray.class)
        return noCache;
      params[i] = c;
    }
    return new Entry (name, params);
  }

  /**
   * Removes all mappings from this cache.
   */
  public void clear ()
  {
    _init ();
  }
}
