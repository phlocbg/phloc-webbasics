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
package com.phloc.webctrls.bootstrap;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Represent a single tab box
 * 
 * @author philip
 */
public class BootstrapTabBox extends AbstractHCDiv <BootstrapTabBox>
{
  private static final class Tab implements IHasID <String>
  {
    private final String m_sID;
    private final IHCNode m_aLabel;
    private final IHCNode m_aContent;

    public Tab (@Nullable final String sID, @Nullable final IHCNode aLabel, @Nullable final IHCNode aContent)
    {
      m_sID = StringHelper.hasText (sID) ? sID : GlobalIDFactory.getNewStringID ();
      m_aLabel = aLabel;
      m_aContent = aContent;
    }

    @Nonnull
    @Nonempty
    public String getID ()
    {
      return m_sID;
    }

    @Nonnull
    public ISimpleURL getLinkURL ()
    {
      return new SimpleURL ("#" + m_sID);
    }

    @Nullable
    public IHCNode getLabel ()
    {
      return m_aLabel;
    }

    @Nullable
    public IHCNode getContent ()
    {
      return m_aContent;
    }
  }

  public static final boolean DEFAULT_ACTIVE = false;

  private EBootstrapTabBoxType m_eType = EBootstrapTabBoxType.TOP;
  private final List <Tab> m_aTabs = new ArrayList <Tab> ();
  private String m_sActiveTabID;

  private void _init ()
  {
    addClass (CBootstrapCSS.TABBABLE);
  }

  public BootstrapTabBox ()
  {
    super ();
    _init ();
  }

  @Nonnull
  public BootstrapTabBox setType (@Nonnull final EBootstrapTabBoxType eType)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    m_eType = eType;
    return this;
  }

  @Nullable
  public String getActiveTabID ()
  {
    return m_sActiveTabID;
  }

  @Nonnull
  public BootstrapTabBox setActiveTabID (@Nullable final String sID)
  {
    m_sActiveTabID = sID;
    return this;
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final String sLabel, @Nullable final IHCNode aContent)
  {
    return addTab (null, new HCTextNode (sLabel), aContent, DEFAULT_ACTIVE);
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final String sLabel, @Nullable final IHCNode aContent, final boolean bActive)
  {
    return addTab (null, new HCTextNode (sLabel), aContent, bActive);
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final String sID,
                                 @Nullable final String sLabel,
                                 @Nullable final IHCNode aContent,
                                 final boolean bActive)
  {
    return addTab (sID, new HCTextNode (sLabel), aContent, bActive);
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final IHCNode aLabel, @Nullable final IHCNode aContent)
  {
    return addTab (null, aLabel, aContent, DEFAULT_ACTIVE);
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final IHCNode aLabel, @Nullable final IHCNode aContent, final boolean bActive)
  {
    return addTab (null, aLabel, aContent, bActive);
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nullable final String sID,
                                 @Nullable final IHCNode aLabel,
                                 @Nullable final IHCNode aContent,
                                 final boolean bActive)
  {
    final Tab aTab = new Tab (sID, aLabel, aContent);
    m_aTabs.add (aTab);
    if (bActive)
      m_sActiveTabID = aTab.getID ();
    return this;
  }

  @Override
  protected boolean canConvertToNode (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    return !m_aTabs.isEmpty ();
  }

  @Override
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void prepareOnceBeforeCreateElement (@Nonnull final IHCConversionSettings aConversionSettings)
  {
    super.prepareOnceBeforeCreateElement (aConversionSettings);
    addClass (m_eType);

    String sActiveTabID = m_sActiveTabID;
    if (StringHelper.hasNoText (sActiveTabID))
    {
      // Activate first tab by default
      sActiveTabID = ContainerHelper.getFirstElement (m_aTabs).getID ();
    }

    // Build code for tabs and content
    final HCUL aTabs = new HCUL ().addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_TABS);
    final HCDiv aContent = new HCDiv ().addClass (CBootstrapCSS.TAB_CONTENT);
    for (final Tab aTab : m_aTabs)
    {
      final boolean bIsActiveTab = aTab.getID ().equals (sActiveTabID);

      // header
      final HCLI aToggleLI = aTabs.addItem ();
      if (bIsActiveTab)
        aToggleLI.addClass (CBootstrapCSS.ACTIVE);
      aToggleLI.addChild (new HCA (aTab.getLinkURL ()).setCustomAttr ("data-toggle", "tab").addChild (aTab.getLabel ()));

      // content
      final HCDiv aPane = aContent.addAndReturnChild (new HCDiv ().addChild (aTab.getContent ())
                                                                  .addClass (CBootstrapCSS.TAB_PANE)
                                                                  .setID (aTab.getID ()));
      if (bIsActiveTab)
        aPane.addClass (CBootstrapCSS.ACTIVE);
    }

    // Determine order of elements
    if (m_eType.emitTabsBeforeContent ())
      addChildren (aTabs, aContent);
    else
      addChildren (aContent, aTabs);
  }
}
