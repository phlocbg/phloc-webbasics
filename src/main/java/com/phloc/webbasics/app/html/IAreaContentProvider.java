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
package com.phloc.webbasics.app.html;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.scopes.web.domain.IRequestWebScope;

/**
 * Interface for an object that provides content to an application layout area.
 * 
 * @author philip
 */
public interface IAreaContentProvider
{
  /**
   * @param aRequestScope
   *        Request scope
   * @param aDisplayLocale
   *        The display locale to be used for rendering. May not be
   *        <code>null</code>.
   * @return The content of the area based on the current state.
   */
  @Nullable
  IHCNode getContent (@Nonnull final IRequestWebScope aRequestScope, @Nonnull Locale aDisplayLocale);
}
