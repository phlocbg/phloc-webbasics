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

/**
 * A listener interfaces that is invoked before a scope is destroyed. If an
 * object implementing this interface is added into a scope, this destruction
 * method is automatically called!
 * 
 * @author philip
 */
public interface IScopeDestructionAware
{
  /**
   * Called before the owning scope is destroyed. You may perform some cleanup
   * work in here.
   */
  void onScopeDestruction () throws Exception;
}
