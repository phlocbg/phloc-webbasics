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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import php.java.bridge.ISession;
import php.java.bridge.Request;
import php.java.bridge.SessionFactory;

/**
 * Create session contexts for servlets.
 * <p>
 * This ContextFactory can be used in environments where no custom class loaders
 * and no threads are allowed.
 * 
 * @see php.java.bridge.http.ContextFactory
 * @see php.java.bridge.http.ContextServer
 * @author jostb
 */
public class RemoteHttpContextFactory extends SessionFactory implements IContextFactory, Serializable
{
  /** The response */
  private transient HttpResponse out;

  private static final long serialVersionUID = -7009009517347609467L;
  private IContext context;
  private IContextFactoryVisitor impl;
  private final String id;

  public RemoteHttpContextFactory (final HttpRequest req, final HttpResponse res)
  {
    super ();
    this.out = res;
    this.id = ContextFactory.EMPTY_CONTEXT_NAME; // dummy
  }

  protected void accept (final IContextFactoryVisitor impl)
  {
    this.impl = impl;
    impl.visit (this);
  }

  /**
   * Create and add a new ContextFactory.
   * 
   * @param req
   *        The HttpRequest
   * @param res
   *        The HttpResponse
   * @return The created ContextFactory
   */
  public static IContextFactory addNew (final HttpRequest req, final HttpResponse res, final IContextFactoryVisitor impl)
  {
    final RemoteHttpContextFactory factory = new RemoteHttpContextFactory (req, res);
    factory.accept (impl);
    return factory;
  }

  /** {@inheritDoc} */
  public String getId ()
  {
    return id;
  }

  /** {@inheritDoc} */
  public void initialize ()
  {/* empty */}

  /** {@inheritDoc} */
  @Override
  public void invalidate ()
  {/* empty */}

  /** {@inheritDoc} */
  public void recycle (final String id)
  {/* empty */}

  /** {@inheritDoc} */
  public void release ()
  {
    if (impl != null)
      impl.release ();
  }

  /** {@inheritDoc} */
  public void releaseManaged () throws InterruptedException
  {
    if (impl != null)
      impl.releaseManaged ();
  }

  /** {@inheritDoc} */
  public void waitFor (final long timeout) throws InterruptedException
  {}

  /**
   * {@inheritDoc}
   */
  @Override
  public void parseHeader (final Request req, final InputStream in) throws IOException
  {
    throw new IllegalStateException ("parseHeader");
  }

  /** {@inheritDoc} */
  public void setContext (final IContext context)
  {
    if (impl != null)
      impl.setContext (context);
    this.context = context;
  }

  /** {@inheritDoc} */
  @Override
  public IContext getContext ()
  {
    if (context != null)
      return context;
    if (impl != null)
      context = impl.getContext ();
    else
      setContext (createContext ());

    context.setAttribute (IContext.JAVA_BRIDGE, getBridge (), IContext.ENGINE_SCOPE);

    return context;
  }

  /** {@inheritDoc} */
  @Override
  public void destroy ()
  {
    super.destroy ();
  }

  /**
   * Return an emulated JSR223 context.
   * 
   * @return The context.
   * @see php.java.servlet.HttpContext
   */
  private IContext createContext ()
  {
    return new Context ();
  }

  /** {@inheritDoc} */
  @Override
  public void flushBuffer () throws IOException
  {
    out.flushBuffer ();
  }

  /**
   * Set the current response
   * 
   * @param out
   *        the PhpJavaServlet response
   */
  public void setResponse (final HttpResponse out)
  {
    this.out = out;
  }

  /** {@inheritDoc} */
  public ISession getSimpleSession (final String name, final short clientIsNew, final int timeout)
  {
    return super.getSession (name, clientIsNew, timeout);
  }
}
