/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webbasics.action;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;

/**
 * Abstract base class in case there will be some common functionality some
 * time.
 * 
 * @author philip
 */
public abstract class AbstractActionExecutor implements IActionExecutor
{
  public void initExecution (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {}

  @Nullable
  public DateTime getLastModificationDateTime ()
  {
    return null;
  }
}
