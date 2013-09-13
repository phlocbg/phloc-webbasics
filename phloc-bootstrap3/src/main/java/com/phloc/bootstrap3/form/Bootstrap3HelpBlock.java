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
package com.phloc.bootstrap3.form;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCSpan;

/**
 * Wrapper for a Bootstrap3 help block.
 * 
 * @author Philip Helger
 */
public class Bootstrap3HelpBlock extends AbstractHCSpan <Bootstrap3HelpBlock>
{
  public Bootstrap3HelpBlock ()
  {
    addClass (CBootstrap3CSS.HELP_BLOCK);
  }

  /**
   * Create a new element with the passed child text
   * 
   * @param aChild
   *        The child text provider to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final IPredefinedLocaleTextProvider aChild)
  {
    return new Bootstrap3HelpBlock ().addChild (aChild);
  }

  /**
   * Create a new element with the passed child texts
   * 
   * @param aChildren
   *        The child texts to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final IPredefinedLocaleTextProvider... aChildren)
  {
    return new Bootstrap3HelpBlock ().addChildren (aChildren);
  }

  /**
   * Create a new element with the passed child text
   * 
   * @param sChild
   *        The child to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final String sChild)
  {
    return new Bootstrap3HelpBlock ().addChild (sChild);
  }

  /**
   * Create a new element with the passed child texts
   * 
   * @param aChildren
   *        The child texts to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final String... aChildren)
  {
    return new Bootstrap3HelpBlock ().addChildren (aChildren);
  }

  /**
   * Create a new element with the passed child node
   * 
   * @param aChild
   *        The child node to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final IHCNode aChild)
  {
    return new Bootstrap3HelpBlock ().addChild (aChild);
  }

  /**
   * Create a new element with the passed child nodes
   * 
   * @param aChildren
   *        The child nodes to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final IHCNode... aChildren)
  {
    return new Bootstrap3HelpBlock ().addChildren (aChildren);
  }

  /**
   * Create a new element with the passed child nodes
   * 
   * @param aChildren
   *        The child nodes to be appended. May be <code>null</code>
   * @return The created BootstrapWell element and never <code>null</code>
   */
  @Nonnull
  public static Bootstrap3HelpBlock create (@Nullable final Iterable <? extends IHCNode> aChildren)
  {
    return new Bootstrap3HelpBlock ().addChildren (aChildren);
  }
}
