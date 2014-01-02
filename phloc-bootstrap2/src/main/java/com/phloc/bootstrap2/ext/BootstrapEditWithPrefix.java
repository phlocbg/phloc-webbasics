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
package com.phloc.bootstrap2.ext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.CBootstrapCSS;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.form.RequestField;

public class BootstrapEditWithPrefix implements IHCNodeBuilder
{
  private final IHCNode m_aPrefix;
  private final IHCNode m_aInput;

  public BootstrapEditWithPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull final RequestField aRF)
  {
    this (sPrefix, new HCEdit (aRF));
  }

  public BootstrapEditWithPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull final IHCNodeBuilder aInput)
  {
    this (sPrefix, aInput.build ());
  }

  public BootstrapEditWithPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull final IHCNode aInput)
  {
    if (StringHelper.hasNoText (sPrefix))
      throw new IllegalArgumentException ("prefix");
    if (aInput == null)
      throw new NullPointerException ("input");
    m_aPrefix = new HCTextNode (sPrefix);
    m_aInput = aInput;
  }

  public BootstrapEditWithPrefix (@Nonnull final IHCNode aPrefix, @Nonnull final RequestField aRF)
  {
    this (aPrefix, new HCEdit (aRF));
  }

  public BootstrapEditWithPrefix (@Nonnull final IHCNode aPrefix, @Nonnull final IHCNodeBuilder aInput)
  {
    this (aPrefix, aInput.build ());
  }

  public BootstrapEditWithPrefix (@Nonnull final IHCNode aPrefix, @Nonnull final IHCNode aInput)
  {
    if (aPrefix == null)
      throw new NullPointerException ("prefix");
    if (aInput == null)
      throw new NullPointerException ("input");
    m_aPrefix = aPrefix;
    m_aInput = aInput;
  }

  @Nonnull
  public IHCNode getPrefix ()
  {
    return m_aPrefix;
  }

  @Nonnull
  public IHCNode getInput ()
  {
    return m_aInput;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv aDiv = new HCDiv ().addClass (CBootstrapCSS.INPUT_PREPEND);
    IHCElement <?> aElement;
    if (m_aPrefix instanceof IHCElement <?>)
      aElement = (IHCElement <?>) m_aPrefix;
    else
      aElement = HCSpan.create (m_aPrefix);
    aDiv.addChild (aElement.addClass (CBootstrapCSS.ADD_ON));
    aDiv.addChild (m_aInput);
    return aDiv;
  }
}
