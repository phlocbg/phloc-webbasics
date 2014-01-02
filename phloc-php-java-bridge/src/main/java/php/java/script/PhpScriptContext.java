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
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.Map;

import javax.script.ScriptContext;

import php.java.bridge.ILogger;
import php.java.bridge.JavaBridgeRunner;
import php.java.bridge.NotImplementedException;
import php.java.bridge.Util;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.ContextServer;
import php.java.bridge.http.IContext;

/**
 * This class implements a simple script context for PHP. It starts a standalone
 * <code>JavaBridgeRunner</code> which listens for requests from php instances.
 * <p>
 * In a servlet environment please use a
 * <code>php.java.script.http.PhpSimpleHttpScriptContext</code> instead.
 * 
 * @see php.java.script.PhpScriptContext
 * @see php.java.bridge.JavaBridgeRunner
 * @author jostb
 */

public final class PhpScriptContext extends AbstractPhpScriptContext
{
  public PhpScriptContext (final ScriptContext ctx)
  {
    super (ctx);
  }

  /** {@inheritDoc} */
  public Object init (final Object callable) throws Exception
  {
    return php.java.bridge.http.Context.getManageable (callable);
  }

  /** {@inheritDoc} */
  public void onShutdown (final Closeable closeable)
  {
    php.java.bridge.http.Context.handleManaged (closeable);
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

  /** {@inheritDoc} */
  public String getRealPath (final String path)
  {
    return php.java.bridge.http.Context.getRealPathInternal (path);
  }

  /** {@inheritDoc} */
  public Object get (final String key)
  {
    return getBindings (IContext.ENGINE_SCOPE).get (key);
  }

  /** {@inheritDoc} */
  public void put (final String key, final Object val)
  {
    getBindings (IContext.ENGINE_SCOPE).put (key, val);
  }

  /** {@inheritDoc} */
  public void remove (final String key)
  {
    getBindings (IContext.ENGINE_SCOPE).remove (key);
  }

  /** {@inheritDoc} */
  public void putAll (final Map <String, Object> map)
  {
    getBindings (IContext.ENGINE_SCOPE).putAll (map);
  }

  /** {@inheritDoc} */
  public Map <String, Object> getAll ()
  {
    return Collections.unmodifiableMap (getBindings (IContext.ENGINE_SCOPE));
  }

  /** {@inheritDoc} */
  public AbstractContinuation createContinuation (final Reader reader,
                                                  final Map <String, String> env,
                                                  final OutputStream out,
                                                  final OutputStream err,
                                                  final AbstractHeaderParser headerParser,
                                                  final ResultProxy result,
                                                  final ILogger logger,
                                                  final boolean isCompiled)
  {
    AbstractContinuation cont;

    if (isCompiled)
      cont = new FastCGIProxy (env, out, err, headerParser, result);
    else
      cont = new HttpProxy (reader, env, out, err, headerParser, result);

    return cont;
  }

  private static JavaBridgeRunner httpServer;

  private static synchronized final JavaBridgeRunner getHttpServer ()
  {
    if (httpServer != null)
      return httpServer;
    try
    {
      return httpServer = JavaBridgeRunner.getRequiredInstance ();
    }
    catch (final IOException e)
    {
      Util.printStackTrace (e);
      return null;
    }
  }

  /** {@inheritDoc} */
  public String getSocketName ()
  {
    return getHttpServer ().getSocket ().getSocketName ();
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

  /** {@inheritDoc} */
  public ContextServer getContextServer ()
  {
    return getHttpServer ().getContextServer ();
  }
}
