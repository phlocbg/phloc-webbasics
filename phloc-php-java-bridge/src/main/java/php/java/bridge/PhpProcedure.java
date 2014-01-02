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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;

/**
 * This class takes the supplied PHP environment and creates a dynamic proxy for
 * calling PHP code.
 */
public final class PhpProcedure implements InvocationHandler
{

  private final IJavaBridgeFactory bridge;
  private final long object;
  private final Map <?, ?> names;
  protected String name;

  protected PhpProcedure (final IJavaBridgeFactory bridge, final long object, final String name, final Map <?, ?> names)
  {
    this.bridge = bridge;
    this.object = object;
    this.names = names;
    this.name = name;
  }

  /**
   * Called from java_closure().
   * 
   * @param bridge
   *        - The request handling bridge
   * @param name
   *        - The name, e.g. java_closure($this, "alwaysCallMe")
   * @param names
   *        - A map of names, e.g. java_closure($this, array("javaName1" =>
   *        "php_name1", ...);
   * @param interfaces
   *        - The list of interfaces that this proxy must implement, may be
   *        empty. E.g. java_closure($this, null, null, array(new
   *        Java("java.awt.event.ActionListener"));
   * @param object
   *        - An opaque object ID (protocol-level).
   * @return A new proxy instance.
   */
  protected static Object createProxy (final IJavaBridgeFactory bridge,
                                       final String name,
                                       final Map <?, ?> names,
                                       final Class <?> interfaces[],
                                       final long object)
  {
    final PhpProcedure handler = new PhpProcedure (bridge, object, name, names);
    final ClassLoader loader = Util.getContextClassLoader ();

    final Object proxy = Proxy.newProxyInstance (loader, interfaces, handler);
    return proxy;
  }

  /**
   * Called from getInterface().
   * 
   * @param interfaces
   *        - The list of interfaces that this proxy must implement, may be
   *        empty.
   * @param proc
   *        - A procedure obtained from java_closure().
   * @return A new proxy instance.
   */
  public static Object createProxy (final Class <?> interfaces[], final PhpProcedure proc)
  {
    return createProxy (proc.bridge, proc.name, proc.names, interfaces, proc.object);
  }

  private Object invoke (final String method, final Class <?> returnType, final Object [] args) throws Throwable
  {
    final JavaBridge bridge = this.bridge.getBridge ();
    if (bridge.logLevel > 3)
      bridge.logDebug ("invoking callback: " + method);
    String cname;
    if (name != null)
    {
      cname = name;
    }
    else
    {
      cname = (String) names.get (method);
      if (cname == null)
        cname = method;
    }
    bridge.request.response.setResultProcedure (object, cname, method, args);
    final Object [] result = bridge.request.handleSubRequests ();
    if (bridge.logLevel > 3)
      bridge.logDebug ("result from cb: " + Arrays.asList (result));
    return bridge.coerce (returnType, result[0], bridge.request.response);
  }

  private void checkPhpContinuation () throws IllegalStateException
  {
    if (bridge.isNew ())
      throw new IllegalStateException ("Cannot call closure anymore: the closed-over PHP script continuation has been terminated.");
  }

  /**
   * Invoke a PHP function or a PHP method.
   * 
   * @param proxy
   *        The php environment or the PHP object
   * @param method
   *        The php method name
   * @param args
   *        the arguments
   * @return the result or null.
   * @throws Throwable
   *         script exception.
   */
  public Object invoke (final Object proxy, final String method, final Object [] args) throws Throwable
  {
    checkPhpContinuation ();

    return invoke (method, Object.class, args);
  }

  /** {@inheritDoc} */
  public Object invoke (final Object proxy, final Method method, final Object [] args) throws Throwable
  {
    checkPhpContinuation ();

    return invoke (method.getName (), method.getReturnType (), args);
  }

  static long unwrap (final Object ob)
  {
    final InvocationHandler handler = Proxy.getInvocationHandler (ob);
    final PhpProcedure proc = (PhpProcedure) handler;
    return proc.object;
  }
}
