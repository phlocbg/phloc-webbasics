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

import java.io.Closeable;

/*
 * Copyright (C) 2003-2009 Jost Boekemeier
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
 * Classes which implement this interface receive a notification before their
 * container terminates. This usually happens when the
 * ContextLoaderListener.contextDestroyed(javax.servlet.ServletContextEvent) is
 * called or right before the VM terminates.
 * 
 * @author jostb
 */
public interface IManaged
{

  /**
   * Initialize a library. This method may be called via
   * java_context()->init(...) to initialize a library. Within init()
   * onShutdown() may be called to register a shutdown hook for the library.
   * 
   * @param callable
   *        Its call() method will be called synchronized.
   * @return The result of the call() invocation.
   * @throws Exception
   *         The result of the call() invocation.
   */
  Object init (Object callable) throws Exception;

  /**
   * Register a shutdown hook for the library. This method may be called via
   * java_context()->onShutdown(...) to register a shutdown hook during init().
   * 
   * @param closeable
   *        Its close() method will be called before the context or the VM
   *        terminates.
   */
  void onShutdown (Closeable closeable);
}
