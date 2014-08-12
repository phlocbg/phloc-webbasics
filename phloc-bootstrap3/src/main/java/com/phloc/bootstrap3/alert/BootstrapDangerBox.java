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
package com.phloc.bootstrap3.alert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;

/**
 * Bootstrap3 danger box
 *
 * @author Philip Helger
 */
public class BootstrapDangerBox extends AbstractBootstrapAlert <BootstrapDangerBox>
{
  public BootstrapDangerBox ()
  {
    super (EBootstrapAlertType.DANGER);
  }

  /**
   * Create a new element with the passed child text
   *
   * @param sChild
   *        The child to be appended. May be <code>null</code>
   * @return The created BootstrapDangerBox element and never <code>null</code>
   */
  @Nonnull
  public static BootstrapDangerBox create (@Nullable final String sChild)
  {
    return new BootstrapDangerBox ().addChild (sChild);
  }

  /**
   * Create a new element with the passed child texts
   *
   * @param aChildren
   *        The child texts to be appended. May be <code>null</code>
   * @return The created BootstrapDangerBox element and never <code>null</code>
   */
  @Nonnull
  public static BootstrapDangerBox create (@Nullable final String... aChildren)
  {
    return new BootstrapDangerBox ().addChildren (aChildren);
  }

  /**
   * Create a new element with the passed child node
   *
   * @param aChild
   *        The child node to be appended. May be <code>null</code>
   * @return The created BootstrapDangerBox element and never <code>null</code>
   */
  @Nonnull
  public static BootstrapDangerBox create (@Nullable final IHCNode aChild)
  {
    return new BootstrapDangerBox ().addChild (aChild);
  }

  /**
   * Create a new element with the passed child nodes
   *
   * @param aChildren
   *        The child nodes to be appended. May be <code>null</code>
   * @return The created BootstrapDangerBox element and never <code>null</code>
   */
  @Nonnull
  public static BootstrapDangerBox create (@Nullable final IHCNode... aChildren)
  {
    return new BootstrapDangerBox ().addChildren (aChildren);
  }

  /**
   * Create a new element with the passed child nodes
   *
   * @param aChildren
   *        The child nodes to be appended. May be <code>null</code>
   * @return The created BootstrapDangerBox element and never <code>null</code>
   */
  @Nonnull
  public static BootstrapDangerBox create (@Nullable final Iterable <? extends IHCNode> aChildren)
  {
    return new BootstrapDangerBox ().addChildren (aChildren);
  }
}
