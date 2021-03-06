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

import php.java.bridge.IJavaBridgeFactory;
import php.java.bridge.ISession;

/**
 * Interface that the ContextFactories must implement.
 * 
 * @author jostb
 */
public interface IContextFactory extends IJavaBridgeFactory
{
  /**
   * <p>
   * Update the context factory with the new JavaBridge obtained from the
   * servlet
   * </p>
   * 
   * @param id
   *        The fresh id
   * @see php.java.bridge.http.ContextFactory#recycle()
   * @see php.java.bridge.Request#setBridge(php.java.bridge.JavaBridge)
   * @see php.java.bridge.Request#recycle()
   */
  void recycle (String id);

  /**
   * Releases the context factory. This method should be called when the factory
   * is not needed anymore. Implementations could then remove any unused context
   * factory from the classloader's list of context factories.
   */
  void release ();

  /**
   * Wait until this context is finished and release/destroy it. This method
   * returns immediately if this context is not in use yet or it is no longer in
   * use. Call this method only if Java has initiated the communication and Java
   * have full control over the connection, for example via a ScriptEngine's
   * URLReader or CGIRunner. For Apache/PHP initiated requests use a combination
   * of #waitFor(long) and #release() instead.
   * 
   * @throws InterruptedException
   * @see php.java.bridge.http.ContextRunner
   */
  void releaseManaged () throws InterruptedException;

  /**
   * Wait until this context is finished.
   * 
   * @param timeout
   *        The timeout
   * @throws InterruptedException
   * @see php.java.bridge.http.ContextRunner
   */
  void waitFor (long timeout) throws InterruptedException;

  /**
   * Return the serializable ID of the context factory
   * 
   * @return The ID
   */
  String getId ();

  /**
   * Return a JSR223 context
   * 
   * @return The context
   * @see php.java.servlet.ServletContextFactory#getContext()
   * @see php.java.bridge.http.Context
   */
  IContext getContext ();

  /**
   * Set the Context into this factory. Should be called by Context.addNew()
   * only.
   * 
   * @param context
   * @see php.java.bridge.http.ContextFactory#addNew()
   */
  void setContext (IContext context);

  /**
   * @param name
   *        The session name. If name is null, the name PHPSESSION will be used.
   * @param clientIsNew
   *        true if the client wants a new session
   * @param timeout
   *        timeout in seconds. If 0 the session does not expire.
   * @return The session
   * @see php.java.bridge.ISession
   */
  ISession getSession (String name, short clientIsNew, int timeout);

  /**
   * @param name
   *        The session name. If name is null, the name PHPSESSION will be used.
   * @param clientIsNew
   *        true if the client wants a new session
   * @param timeout
   *        timeout in seconds. If 0 the session does not expire.
   * @return The session
   * @see php.java.bridge.ISession
   */
  ISession getSimpleSession (String name, short clientIsNew, int timeout);

  /**
   * Called when the context runner starts
   * 
   * @see IContextFactory#releaseManaged()
   * @see IContextFactory#destroy()
   */
  void initialize ();
}
