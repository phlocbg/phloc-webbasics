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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import php.java.bridge.IInvocable;
import php.java.bridge.IManaged;
import php.java.bridge.NotImplementedException;
import php.java.bridge.Util;
import php.java.bridge.Util.UtilThread;

/**
 * Emulates a JSR223 script context when the JSR223 classes are not available.
 * The method call(kont) returns false, so that it can be used to check if a
 * script was called from java:<br>
 * <code>
 * function toString() {return "hello java, I am a php script, but in your eyes I am an ordinary java object...";}<br>
 * java_context()-&gt;call(java_closure()) || die("This script must be called from java!");
 * </code>
 * 
 * @see php.java.script.IPhpScriptContext
 * @see javax.script.ScriptContext
 * @see php.java.script.PhpScriptContext
 * @author jostb
 */
public class Context implements IManaged, IInvocable, IContext
{
  /** Map of the scope of level GLOBAL_SCOPE */
  private Map <String, Object> globalScope;

  /** Map of the scope of level ENGINE_SCOPE */
  private Map <String, Object> engineScope;

  protected Context ()
  {}

  /** {@inheritDoc} */
  public Object getAttribute (final String name) throws IllegalArgumentException
  {
    if (name == null)
      throw new IllegalArgumentException ("name cannot be null");

    if (getEngineScope ().get (name) != null)
      return getEngineScope ().get (name);

    if (getGlobalScope ().get (name) != null)
      return getGlobalScope ().get (name);

    return null;
  }

  /** {@inheritDoc} */
  public Object getAttribute (final String name, final int scope) throws IllegalArgumentException
  {
    if (name == null)
      throw new IllegalArgumentException ("name cannot be null");

    switch (scope)
    {
      case ENGINE_SCOPE:
        return getEngineScope ().get (name);
      case GLOBAL_SCOPE:
        return getGlobalScope ().get (name);
      default:
        throw new IllegalArgumentException ("invalid scope");
    }
  }

  /** {@inheritDoc} */
  public int getAttributesScope (final String name)
  {
    if (getEngineScope ().containsKey (name))
      return ENGINE_SCOPE;

    if (getGlobalScope ().containsKey (name))
      return GLOBAL_SCOPE;

    return -1;
  }

  /** {@inheritDoc} */
  public Writer getWriter () throws IOException
  {

    // autoflush is true so that I can see the output immediately
    return new PrintWriter (System.out, true);
  }

  /** {@inheritDoc} */
  public Object removeAttribute (final String name, final int scope) throws IllegalArgumentException
  {
    if (name == null)
      throw new IllegalArgumentException ("name is null");

    switch (scope)
    {
      case ENGINE_SCOPE:
        return getEngineScope ().remove (name);
      case GLOBAL_SCOPE:
        return getGlobalScope ().remove (name);
      default:
        throw new IllegalArgumentException ("invalid scope");
    }
  }

  /** {@inheritDoc} */
  public void setAttribute (final String name, final Object value, final int scope) throws IllegalArgumentException
  {
    if (name == null)
      throw new IllegalArgumentException ("name is null");

    switch (scope)
    {
      case ENGINE_SCOPE:
        getEngineScope ().put (name, value);
        break;
      case GLOBAL_SCOPE:
        getGlobalScope ().put (name, value);
        break;
      default:
        throw new IllegalArgumentException ("invalid scope");
    }
  }

  /**
   * Throws IllegalStateException
   * 
   * @return none
   */
  public Object getHttpServletRequest ()
  {
    throw new IllegalStateException ("PHP not running in a servlet environment");
  }

  /**
   * Throws IllegalStateException
   * 
   * @return none
   */
  public Object getServletContext ()
  {
    throw new IllegalStateException ("PHP not running in a servlet environment");
  }

  /**
   * Throws IllegalStateException
   * 
   * @return none
   */
  public Object getHttpServletResponse ()
  {
    throw new IllegalStateException ("PHP not running in a servlet environment");
  }

  /**
   * Throws IllegalStateException
   * 
   * @return none
   */
  public Object getServlet ()
  {
    throw new IllegalStateException ("PHP not running in a servlet environment");
  }

  /**
   * Throws IllegalStateException
   * 
   * @return none
   */
  public Object getServletConfig ()
  {
    throw new IllegalStateException ("PHP not running in a servlet environment");
  }

  /**
   * @param kont
   *        dummy
   * @return false
   */
  public boolean call (final Object kont)
  {
    return false;
  }

  protected void setGlobalScope (final Map <String, Object> globalScope)
  {
    this.globalScope = globalScope;
  }

  protected Map <String, Object> getGlobalScope ()
  {
    if (globalScope == null)
      globalScope = new HashMap <String, Object> ();
    return globalScope;
  }

  protected void setEngineScope (final Map <String, Object> engineScope)
  {
    this.engineScope = engineScope;
  }

  protected Map <String, Object> getEngineScope ()
  {
    if (engineScope == null)
      engineScope = new HashMap <String, Object> ();
    return engineScope;
  }

  private static boolean registeredHook = false;
  private static List <Closeable> closeables = new ArrayList <Closeable> ();
  private static Object lockObject = new Object ();

  /**
   * Only for internal use. <br>
   * <br>
   * Used when scripts are running outside of a servlet environment: Either the
   * Standalone or the JSR223 Standalone (see PhpScriptContext). <br>
   * Within a servlet environment use the ContextLoaderListener instead: Either
   * php.java.servlet.Context or the JSR223 Context (see
   * PhpSimpleHttpScriptContext).
   * 
   * @param closeable
   *        The procedure close(), will be called before the VM terminates
   */
  public static void handleManaged (final Closeable closeable)
  {
    // make sure to properly release them upon System.exit().
    synchronized (closeables)
    {
      if (!registeredHook)
      {
        registeredHook = true;
        try
        {
          Runtime.getRuntime ().addShutdownHook (new UtilThread ()
          {
            @Override
            public void run ()
            {
              if (closeables == null)
                return;
              synchronized (closeables)
              {
                for (final Iterator <Closeable> ii = closeables.iterator (); ii.hasNext (); ii.remove ())
                {
                  final Closeable c = ii.next ();
                  try
                  {
                    c.close ();
                  }
                  catch (final Exception e)
                  {
                    Util.printStackTrace (e);
                  }
                }
              }
            }
          });
        }
        catch (final SecurityException e)
        {/* ignore */}
      }
      closeables.add (closeable);
    }
  }

  /**
   * Only for internal use
   * 
   * @param callable
   *        The callable
   * @return The result of the Callable::call().
   * @throws Exception
   */
  public static Object getManageable (final Object callable) throws Exception
  {
    synchronized (lockObject)
    {
      final Method call = callable.getClass ().getMethod ("call");
      return call.invoke (callable);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws Exception
   */
  public Object init (final Object callable) throws Exception
  {
    return getManageable (callable);
  }

  /** {@inheritDoc} */
  public void onShutdown (final Closeable closeable)
  {
    php.java.bridge.http.Context.handleManaged (closeable);
  }

  /**
   * Only for internal use
   * 
   * @param path
   *        the path
   * @return the real path
   */
  public static String getRealPathInternal (final String path)
  {
    try
    {
      return new File (path).getCanonicalPath ();
    }
    catch (final IOException e)
    {
      return new File (path).getAbsolutePath ();
    }
  }

  /** {@inheritDoc} */
  public String getRealPath (final String path)
  {
    return getRealPathInternal (path);
  }

  /** {@inheritDoc} */
  public Object get (final String key)
  {
    return getEngineScope ().get (key);
  }

  /** {@inheritDoc} */
  public void put (final String key, final Object val)
  {
    getEngineScope ().put (key, val);
  }

  /** {@inheritDoc} */
  public void remove (final String key)
  {
    getEngineScope ().remove (key);
  }

  /** {@inheritDoc} */
  public void putAll (final Map <String, Object> map)
  {
    getEngineScope ().putAll (map);
  }

  /** {@inheritDoc} */
  public Map <String, Object> getAll ()
  {
    return Collections.unmodifiableMap (getEngineScope ());
  }

  /** {@inheritDoc} */
  public String getSocketName ()
  {
    throw new NotImplementedException ("Use the JSR 223 API or a servlet environment instead");
  }

  /** @deprecated */
  @Deprecated
  public String getRedirectString ()
  {
    throw new NotImplementedException ();
  }

  /** @deprecated */
  @Deprecated
  public String getRedirectString (final String webPath)
  {
    throw new NotImplementedException ();
  }

  /** {@inheritDoc} */
  public String getRedirectURL (final String webPath)
  {
    return "http://127.0.0.1:" + getSocketName () + webPath;
  }
}
