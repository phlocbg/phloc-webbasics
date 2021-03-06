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
package com.phloc.bootstrap3.inputgroup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.button.BootstrapButton;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapInputGroup implements IHCNodeBuilder
{
  private final EBootstrapInputGroupSize m_eSize;
  private final List <IHCNode> m_aPrefixes = new ArrayList <IHCNode> ();
  private final IHCNode m_aInput;
  private final List <IHCNode> m_aSuffixes = new ArrayList <IHCNode> ();

  public BootstrapInputGroup (@Nonnull final IHCNodeBuilder aNodeBuilder)
  {
    this (EBootstrapInputGroupSize.DEFAULT, aNodeBuilder);
  }

  public BootstrapInputGroup (@Nonnull final IHCNode aInput)
  {
    this (EBootstrapInputGroupSize.DEFAULT, aInput);
  }

  public BootstrapInputGroup (@Nonnull final EBootstrapInputGroupSize eSize, @Nonnull final IHCNodeBuilder aNodeBuilder)
  {
    this (eSize, aNodeBuilder.build ());
  }

  public BootstrapInputGroup (@Nonnull final EBootstrapInputGroupSize eSize, @Nonnull final IHCNode aInput)
  {
    m_eSize = ValueEnforcer.notNull (eSize, "Size");
    m_aInput = ValueEnforcer.notNull (aInput, "Input");
  }

  @Nonnull
  public EBootstrapInputGroupSize getSize ()
  {
    return m_eSize;
  }

  @Nonnull
  public BootstrapInputGroup setPrefix (@Nullable final String sPrefix)
  {
    return setPrefix (HCTextNode.createOnDemand (sPrefix));
  }

  @Nonnull
  public BootstrapInputGroup setPrefix (@Nullable final IHCNode aPrefix)
  {
    m_aPrefixes.clear ();
    return addPrefix (aPrefix);
  }

  @Nonnull
  public BootstrapInputGroup addPrefix (@Nullable final String sPrefix)
  {
    return addPrefix (HCTextNode.createOnDemand (sPrefix));
  }

  @Nonnull
  public BootstrapInputGroup addPrefix (@Nullable final IHCNode aPrefix)
  {
    if (aPrefix != null)
      m_aPrefixes.add (aPrefix);
    return this;
  }

  @Nonnull
  public BootstrapInputGroup addPrefix (@Nonnegative final int nIndex, @Nullable final String sPrefix)
  {
    return addPrefix (nIndex, HCTextNode.createOnDemand (sPrefix));
  }

  @Nonnull
  public BootstrapInputGroup addPrefix (@Nonnegative final int nIndex, @Nullable final IHCNode aPrefix)
  {
    if (nIndex < 0)
      throw new IllegalArgumentException ("Index too small: " + nIndex);

    if (aPrefix != null)
      if (nIndex >= getPrefixCount ())
        m_aPrefixes.add (aPrefix);
      else
        m_aPrefixes.add (nIndex, aPrefix);
    return this;
  }

  @Nonnull
  public EChange removePrefixAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aPrefixes.size ())
      return EChange.UNCHANGED;
    m_aPrefixes.remove (nIndex);
    return EChange.CHANGED;
  }

  @Nonnull
  public BootstrapInputGroup removeAllPrefixes ()
  {
    m_aPrefixes.clear ();
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IHCNode> getAllPrefixes ()
  {
    return ContainerHelper.newList (m_aPrefixes);
  }

  public boolean hasPrefixes ()
  {
    return !m_aPrefixes.isEmpty ();
  }

  @Nonnegative
  public int getPrefixCount ()
  {
    return m_aPrefixes.size ();
  }

  @Nonnull
  public IHCNode getInput ()
  {
    return m_aInput;
  }

  @Nonnull
  public BootstrapInputGroup setSuffix (@Nullable final String sSuffix)
  {
    return setSuffix (HCTextNode.createOnDemand (sSuffix));
  }

  @Nonnull
  public BootstrapInputGroup setSuffix (@Nullable final IHCNode aSuffix)
  {
    m_aSuffixes.clear ();
    return addSuffix (aSuffix);
  }

  @Nonnull
  public BootstrapInputGroup addSuffix (@Nullable final String sSuffix)
  {
    return addSuffix (HCTextNode.createOnDemand (sSuffix));
  }

  @Nonnull
  public BootstrapInputGroup addSuffix (@Nullable final IHCNode aSuffix)
  {
    if (aSuffix != null)
      m_aSuffixes.add (aSuffix);
    return this;
  }

  @Nonnull
  public BootstrapInputGroup addSuffix (@Nonnegative final int nIndex, @Nullable final String sSuffix)
  {
    return addSuffix (nIndex, HCTextNode.createOnDemand (sSuffix));
  }

  @Nonnull
  public BootstrapInputGroup addSuffix (@Nonnegative final int nIndex, @Nullable final IHCNode aSuffix)
  {
    if (nIndex < 0)
      throw new IllegalArgumentException ("Index too small: " + nIndex);

    if (aSuffix != null)
      if (nIndex >= getSuffixCount ())
        m_aSuffixes.add (aSuffix);
      else
        m_aSuffixes.add (nIndex, aSuffix);
    return this;
  }

  @Nonnull
  public EChange removeSuffixAtIndex (@Nonnegative final int nIndex)
  {
    if (nIndex < 0 || nIndex >= m_aSuffixes.size ())
      return EChange.UNCHANGED;
    m_aSuffixes.remove (nIndex);
    return EChange.CHANGED;
  }

  @Nonnull
  public BootstrapInputGroup removeAllSuffixes ()
  {
    m_aSuffixes.clear ();
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IHCNode> getAllSuffixes ()
  {
    return ContainerHelper.newList (m_aSuffixes);
  }

  public boolean hasSuffixes ()
  {
    return !m_aSuffixes.isEmpty ();
  }

  @Nonnegative
  public int getSuffixCount ()
  {
    return m_aSuffixes.size ();
  }

  @Nullable
  public IHCNode build ()
  {
    if (m_aPrefixes.isEmpty () && m_aSuffixes.isEmpty ())
      return m_aInput;

    final HCDiv aInputGroup = new HCDiv ().addClasses (CBootstrapCSS.INPUT_GROUP, m_eSize);

    for (final IHCNode aPrefix : m_aPrefixes)
    {
      if (aPrefix instanceof BootstrapButton)
        aInputGroup.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_BTN).addChild (aPrefix));
      else
        aInputGroup.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_ADDON).addChild (aPrefix));
    }

    aInputGroup.addChild (m_aInput);

    for (final IHCNode aSuffix : m_aSuffixes)
    {
      if (aSuffix instanceof BootstrapButton)
        aInputGroup.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_BTN).addChild (aSuffix));
      else
        aInputGroup.addChild (new HCSpan ().addClass (CBootstrapCSS.INPUT_GROUP_ADDON).addChild (aSuffix));
    }
    return aInputGroup;
  }
}
