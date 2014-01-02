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
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;

import php.java.bridge.ILogger;
import php.java.bridge.NotImplementedException;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.ContextServer;

/**
 * Abstract class for IPhpScriptContexts. The abstract class itself provides
 * default methods that pass all requests to the contained IPhpScriptContext.
 * Subclasses of PhpScriptContextDecoratorDecorator should override some of
 * these methods and may also provide additional methods and fields.
 * 
 * @author jostb
 */
public abstract class AbstractPhpScriptContextDecorator implements IPhpScriptContext
{

  private final IPhpScriptContext ctx;

  /**
   * Create a new PhpScriptContextDecorator
   * 
   * @param ctx
   *        the PhpScriptContext to decorate.
   */
  public AbstractPhpScriptContextDecorator (final IPhpScriptContext ctx)
  {
    this.ctx = ctx;
  }

  /** {@inheritDoc} */
  public Object getAttribute (final String name) throws IllegalArgumentException
  {
    return ctx.getAttribute (name);
  }

  /** {@inheritDoc} */
  public Object getAttribute (final String name, final int scope) throws IllegalArgumentException
  {
    return ctx.getAttribute (name, scope);
  }

  /** {@inheritDoc} */
  public int getAttributesScope (final String name)
  {
    return ctx.getAttributesScope (name);
  }

  /** {@inheritDoc} */
  public Bindings getBindings (final int scope)
  {
    return ctx.getBindings (scope);
  }

  /** {@inheritDoc} */
  public Writer getErrorWriter ()
  {
    return ctx.getErrorWriter ();
  }

  /** {@inheritDoc} */
  public Reader getReader ()
  {
    return ctx.getReader ();
  }

  /** {@inheritDoc} */
  public List <Integer> getScopes ()
  {
    return ctx.getScopes ();
  }

  /** {@inheritDoc} */
  public Writer getWriter ()
  {
    return ctx.getWriter ();
  }

  /** {@inheritDoc} */
  public Object removeAttribute (final String name, final int scope) throws IllegalArgumentException
  {
    return ctx.removeAttribute (name, scope);
  }

  /** {@inheritDoc} */
  public void setAttribute (final String key, final Object value, final int scope) throws IllegalArgumentException
  {
    ctx.setAttribute (key, value, scope);
  }

  /** {@inheritDoc} */
  public void setBindings (final Bindings namespace, final int scope) throws IllegalArgumentException
  {
    ctx.setBindings (namespace, scope);
  }

  /** {@inheritDoc} */
  public void setErrorWriter (final Writer writer)
  {
    ctx.setErrorWriter (writer);
  }

  /** {@inheritDoc} */
  public void setReader (final Reader reader)
  {
    ctx.setReader (reader);
  }

  /** {@inheritDoc} */
  public void setWriter (final Writer writer)
  {
    ctx.setWriter (writer);
  }

  /** {@inheritDoc} */
  public AbstractContinuation getContinuation ()
  {
    return ctx.getContinuation ();
  }

  /** {@inheritDoc} */
  public void setContinuation (final AbstractContinuation kont)
  {
    ctx.setContinuation (kont);
  }

  /** {@inheritDoc} */
  public Object init (final Object callable) throws Exception
  {
    return ctx.init (callable);
  }

  /** {@inheritDoc} */
  public void onShutdown (final Closeable closeable)
  {
    ctx.onShutdown (closeable);
  }

  /** {@inheritDoc} */
  public boolean call (final Object kont) throws Exception
  {
    return ctx.call (kont);
  }

  /** {@inheritDoc} */
  public Object get (final String key)
  {
    return ctx.get (key);
  }

  /** {@inheritDoc} */
  public Map <String, Object> getAll ()
  {
    return ctx.getAll ();
  }

  /** {@inheritDoc} */
  public Object getHttpServletRequest ()
  {
    return ctx.getHttpServletRequest ();
  }

  /** {@inheritDoc} */
  public Object getHttpServletResponse ()
  {
    return ctx.getHttpServletResponse ();
  }

  /** {@inheritDoc} */
  public String getRealPath (final String path)
  {
    return ctx.getRealPath (path);
  }

  /** {@inheritDoc} */
  public Object getServlet ()
  {
    return ctx.getServlet ();
  }

  /** {@inheritDoc} */
  public Object getServletConfig ()
  {
    return ctx.getServletConfig ();
  }

  /** {@inheritDoc} */
  public Object getServletContext ()
  {
    return ctx.getServletContext ();
  }

  /** {@inheritDoc} */
  public void put (final String key, final Object val)
  {
    ctx.put (key, val);
  }

  /** {@inheritDoc} */
  public void putAll (final Map <String, Object> map)
  {
    ctx.putAll (map);
  }

  /** {@inheritDoc} */
  public void remove (final String key)
  {
    ctx.remove (key);
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
    return ctx.createContinuation (reader, env, out, err, headerParser, result, logger, isCompiled);
  }

  /** @deprecated */
  @Deprecated
  public String getRedirectString (final String webPath)
  {
    throw new NotImplementedException ();
  }

  /** @deprecated */
  @Deprecated
  public String getRedirectString ()
  {
    throw new NotImplementedException ();
  }

  /** {@inheritDoc} */
  public String getSocketName ()
  {
    return ctx.getSocketName ();
  }

  /** {@inheritDoc} */
  public String getRedirectURL (final String webPath)
  {
    return ctx.getRedirectURL (webPath);
  }

  /** {@inheritDoc} */
  public ContextServer getContextServer ()
  {
    return ctx.getContextServer ();
  }

  /** {@inheritDoc} */
  public void startContinuation ()
  {
    ctx.startContinuation ();
  }
}
