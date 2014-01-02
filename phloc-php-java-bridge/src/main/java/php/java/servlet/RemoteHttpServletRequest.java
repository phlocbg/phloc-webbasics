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
/**
 * 
 */
package php.java.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

final class RemoteHttpServletRequest extends HttpServletRequestWrapper
{

  private final SimpleServletContextFactory factory;

  public RemoteHttpServletRequest (final SimpleServletContextFactory factory, final HttpServletRequest req)
  {
    super (req);
    this.factory = factory;
  }

  /*
   * Return the session obtained from the servlet.
   */
  @Override
  public HttpSession getSession ()
  {
    return factory.getSession ();
  }

  /*
   * Return the old session or give up.
   */
  @Override
  public HttpSession getSession (final boolean clientIsNew)
  {
    final HttpSession session = getSession ();
    if (clientIsNew && !session.isNew ())
      throw new IllegalStateException ("To obtain a new session call java_session(null, true) at the beginning of your PHP script.");
    return session;
  }

}
