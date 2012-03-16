/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpSession;

/**
 * Session scope
 * 
 * @author philip
 */
public interface ISessionScope extends IScope
{
  /**
   * Get the underlying HTTP session. Important: do not use it to access the
   * attributes within the session. Use only the scope API for this, so that the
   * synchronization is consistent!
   * 
   * @return The underlying HTTP session. Never <code>null</code>.
   */
  @Nonnull
  HttpSession getSession ();
}
