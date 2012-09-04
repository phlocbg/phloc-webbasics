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
package com.phloc.webctrls.bootstrap.derived;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.webctrls.bootstrap.BootstrapAlert;

/**
 * Bootstrap warning box
 * 
 * @author philip
 */
public class BootstrapWarnBox extends BootstrapAlert
{
  private void _init ()
  {
    // Warning requires no specific type
    setBlock (true);
    setShowClose (true);
  }

  public BootstrapWarnBox ()
  {
    super ();
    _init ();
  }

  public BootstrapWarnBox (@Nonnull final IPredefinedLocaleTextProvider aChild)
  {
    this (aChild.getText ());
  }

  public BootstrapWarnBox (@Nullable final String sChild)
  {
    super (sChild);
    _init ();
  }

  public BootstrapWarnBox (@Nullable final String... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapWarnBox (@Nullable final IHCNode aChild)
  {
    super (aChild);
    _init ();
  }

  public BootstrapWarnBox (@Nullable final IHCNode... aChildren)
  {
    super (aChildren);
    _init ();
  }

  public BootstrapWarnBox (@Nullable final Iterable <? extends IHCNode> aChildren)
  {
    super (aChildren);
    _init ();
  }
}
