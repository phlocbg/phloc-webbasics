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
package php.java.script;

/**
 * Classes implementing this interface represent the script continuation; they
 * can be used to allocate php scripts on a HTTP- or FastCGI server.
 * 
 * @author jostb
 */
public interface IContinuation
{

  /**
   * The PHP script must call this function with the current continuation as an
   * argument.
   * <p>
   * Example:
   * <p>
   * <code>
   * java_context()-&gt;call(java_closure());<br>
   * </code>
   * 
   * @param script
   *        - The php continuation
   * @throws InterruptedException
   */
  public void call (Object script) throws InterruptedException;

  /**
   * One must call this function if one is interested in the php continuation.
   * 
   * @return The php continuation.
   * @throws Exception
   */
  public Object getPhpScript () throws Exception;

  /**
   * This function must be called to release the allocated php continuation.
   * Note that simply calling this method does not guarantee that the script is
   * finished, as the ContextRunner may still produce output. Use
   * contextFactory.waitFor() to wait for the script to terminate.
   * 
   * @throws InterruptedException
   */
  public void release () throws InterruptedException;

}
