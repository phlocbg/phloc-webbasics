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
package com.phloc.webctrls.custom;

import javax.annotation.Nonnull;

import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;

/**
 * A base interface for icons
 * 
 * @author philip
 */
public interface IIcon extends ICSSClassProvider
{
  /**
   * @return The icon as self-contained node
   */
  @Nonnull
  IHCNode getAsNode ();

  /**
   * Apply the respective CSS classes to the passed node
   * 
   * @param aElement
   *        The element to apply the CSS classes to. May not be
   *        <code>null</code>.
   * @return The passed element. Never <code>null</code>.
   */
  @Nonnull
  <T extends IHCElement <?>> T applyToNode (@Nonnull T aElement);
}
