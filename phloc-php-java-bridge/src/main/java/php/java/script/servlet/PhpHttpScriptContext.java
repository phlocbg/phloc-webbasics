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
package php.java.script.servlet;

/*-*- mode: Java; tab-width:8 -*-*/

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.script.ScriptContext;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import php.java.bridge.ILogger;
import php.java.bridge.NotImplementedException;
import php.java.bridge.Util;
import php.java.bridge.http.AbstractHeaderParser;
import php.java.bridge.http.ContextServer;
import php.java.bridge.http.WriterOutputStream;
import php.java.script.AbstractContinuation;
import php.java.script.AbstractPhpScriptContextDecorator;
import php.java.script.IPhpScriptContext;
import php.java.script.PhpScriptWriter;
import php.java.script.ResultProxy;
import php.java.servlet.ContextLoaderListener;
import php.java.servlet.ServletUtil;

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

/**
 * An example decorator for compiled script engines running in a servlet
 * environment. Use <blockquote> <code>
 * static final CompiledScript script = ((Compilable)(new ScriptEngineManager().getEngineByName("php-invocable"))).compile("<?php ...?>");<br>
 * <br>
 * script.eval(new php.java.script.servlet.PhpCompiledHttpScriptContext(script.getEngine().getContext(),this,application,request,response));
 * </code> </blockquote>
 * 
 * @author jostb
 */
public class PhpHttpScriptContext extends AbstractPhpScriptContextDecorator
{

  /**
   * Create a new PhpCompiledScriptContext using an existing PhpScriptContext
   * 
   * @param ctx
   *        the script context to be decorated
   */
  public PhpHttpScriptContext (final ScriptContext ctx,
                               final Servlet servlet,
                               final ServletContext context,
                               final HttpServletRequest request,
                               final HttpServletResponse response)
  {
    super ((IPhpScriptContext) ctx);
    this.request = request;
    this.response = response;
    this.context = context;
    this.servlet = servlet;
  }

  /** {@inheritDoc} */
  @Override
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
    {
      final ContextLoaderListener listener = ContextLoaderListener.getContextLoaderListener ((ServletContext) getServletContext ());
      cont = new HttpFastCGIProxy (env, out, err, headerParser, result, listener.getConnectionPool ());
    }
    else
      cont = super.createContinuation (reader, env, out, err, headerParser, result, logger, isCompiled);

    return cont;
  }

  @Override
  public void startContinuation ()
  {
    final ContextLoaderListener listener = ContextLoaderListener.getContextLoaderListener ((ServletContext) getServletContext ());
    listener.getThreadPool ().start (getContinuation ());
  }

  /** Integer value for the level of SCRIPT_SCOPE */
  public static final int REQUEST_SCOPE = 0;

  /** Integer value for the level of SESSION_SCOPE */
  public static final int SESSION_SCOPE = 150;

  /** Integer value for the level of APPLICATION_SCOPE */
  public static final int APPLICATION_SCOPE = 175;

  protected HttpServletRequest request;
  protected HttpServletResponse response;
  protected ServletContext context;
  protected Servlet servlet;

  /** {@inheritDoc} */
  @Override
  public Object getAttribute (final String key, final int scope)
  {
    if (scope == REQUEST_SCOPE)
    {
      return request.getAttribute (key);
    }
    else
      if (scope == SESSION_SCOPE)
      {
        return request.getSession ().getAttribute (key);
      }
      else
        if (scope == APPLICATION_SCOPE)
        {
          return context.getAttribute (key);
        }
        else
        {
          return super.getAttribute (key, scope);
        }
  }

  /** {@inheritDoc} */
  @Override
  public Object getAttribute (final String name) throws IllegalArgumentException
  {
    Object result;
    if (name == null)
    {
      throw new IllegalArgumentException ("name cannot be null");
    }
    if ((result = super.getAttribute (name)) != null)
      return result;

    if ((result = request.getAttribute (name)) != null)
    {
      return result;
    }
    else
      if ((result = request.getSession ().getAttribute (name)) != null)
      {
        return result;
      }
      else
        if ((result = context.getAttribute (name)) != null)
        {
          return result;
        }
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void setAttribute (final String key, final Object value, final int scope) throws IllegalArgumentException
  {
    if (scope == REQUEST_SCOPE)
    {
      request.setAttribute (key, value);
    }
    else
      if (scope == SESSION_SCOPE)
      {
        request.getSession ().setAttribute (key, value);
      }
      else
        if (scope == APPLICATION_SCOPE)
        {
          context.setAttribute (key, value);
        }
        else
        {
          super.setAttribute (key, value, scope);
        }
  }

  /**
   * Get the servlet response
   * 
   * @return The HttpServletResponse
   */
  public HttpServletResponse getResponse ()
  {
    return response;
  }

  /**
   * Get the HttpServletRequest
   * 
   * @return The HttpServletRequest
   */
  public HttpServletRequest getRequest ()
  {
    return request;
  }

  /**
   * Get the ServletContext
   * 
   * @return The current ServletContext
   */
  public ServletContext getContext ()
  {
    return context;
  }

  protected Writer writer;

  /** {@inheritDoc} */
  @Override
  public Writer getWriter ()
  {
    if (writer == null)
    {
      try
      {
        setWriter (response.getWriter ());
      }
      catch (final IOException e)
      {
        Util.printStackTrace (e);
      }
    }
    return writer;
  }

  /** {@inheritDoc} */
  @Override
  public void setWriter (final Writer pwriter)
  {
    Writer writer = pwriter;
    if (!(writer instanceof PhpScriptWriter))
    {
      writer = new PhpScriptWriter (new WriterOutputStream (writer));
    }
    this.writer = writer;
    super.setWriter (writer);
  }

  protected Writer errorWriter;

  /** {@inheritDoc} */
  @Override
  public Writer getErrorWriter ()
  {
    if (errorWriter == null)
    {
      setErrorWriter (PhpScriptLogWriter.getWriter (new php.java.servlet.Logger ()));
    }
    return errorWriter;
  }

  /** {@inheritDoc} */
  @Override
  public void setErrorWriter (final Writer perrorWriter)
  {
    Writer errorWriter = perrorWriter;
    if (!(errorWriter instanceof PhpScriptWriter))
    {
      errorWriter = new PhpScriptWriter (new WriterOutputStream (errorWriter));
    }
    this.errorWriter = errorWriter;
    super.setErrorWriter (errorWriter);
  }

  protected Reader reader;

  /** {@inheritDoc} */
  @Override
  public Reader getReader ()
  {
    if (reader == null)
    {
      try
      {
        reader = request.getReader ();
      }
      catch (final IOException e)
      {
        Util.printStackTrace (e);
      }
    }
    return reader;
  }

  @Override
  public void setReader (final Reader reader)
  {
    super.setReader (this.reader = reader);
  }

  /** {@inheritDoc} */
  @Override
  public Object init (final Object callable) throws Exception
  {
    return php.java.bridge.http.Context.getManageable (callable);
  }

  /** {@inheritDoc} */
  @Override
  public void onShutdown (final Closeable closeable)
  {
    php.java.servlet.HttpContext.handleManaged (closeable, context);
  }

  /**
   * Return the http servlet response
   * 
   * @return The http servlet reponse
   */
  @Override
  public Object getHttpServletResponse ()
  {
    return response;
  }

  /**
   * Return the http servlet request
   * 
   * @return The http servlet request
   */
  @Override
  public Object getHttpServletRequest ()
  {
    return request;
  }

  /**
   * Return the http servlet
   * 
   * @return The http servlet
   */
  @Override
  public Object getServlet ()
  {
    return servlet;
  }

  /**
   * Return the servlet config
   * 
   * @return The servlet config
   */
  @Override
  public Object getServletConfig ()
  {
    return servlet.getServletConfig ();
  }

  /**
   * Return the servlet context
   * 
   * @return The servlet context
   */
  @Override
  public Object getServletContext ()
  {
    return context;
  }

  /** {@inheritDoc} */
  @Override
  public String getRealPath (final String path)
  {
    return ServletUtil.getRealPath (context, path);
  }

  /** @deprecated */
  @Deprecated
  @Override
  public String getRedirectString (final String webPath)
  {
    throw new NotImplementedException ();
  }

  /** @deprecated */
  @Deprecated
  @Override
  public String getRedirectString ()
  {
    throw new NotImplementedException ();
  }

  /** {@inheritDoc} */
  @Override
  public String getRedirectURL (final String webPath)
  {
    final StringBuilder buf = new StringBuilder ();
    buf.append (getSocketName ());
    buf.append ("/");
    buf.append (webPath);
    try
    {
      final URI uri = new URI (request.isSecure () ? "https:127.0.0.1" : "http:127.0.0.1", buf.toString (), null);
      return uri.toASCIIString ();
    }
    catch (final URISyntaxException e)
    {
      Util.printStackTrace (e);
      throw new RuntimeException (e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public String getSocketName ()
  {
    return String.valueOf (ServletUtil.getLocalPort (request));
  }

  /** {@inheritDoc} */
  @Override
  public ContextServer getContextServer ()
  {
    return ContextLoaderListener.getContextLoaderListener (context).getContextServer ();
  }
}
