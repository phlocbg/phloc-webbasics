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
package com.phloc.webctrls.bootstrap;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Represent a single tab box
 * 
 * @author Philip Helger
 */
public class BootstrapTabBox implements IHCNodeBuilder
{
  public static final class Tab implements IHasID <String>, Serializable
  {
    private final String m_sID;
    private final boolean m_bHasGeneratedID;
    private IHCNode m_aLabel;
    private IHCNode m_aContent;
    private boolean m_bDisabled;

    public Tab (@Nullable final String sID,
                @Nullable final IHCNode aLabel,
                @Nullable final IHCNode aContent,
                final boolean bDisabled)
    {
      if (StringHelper.hasText (sID))
      {
        m_sID = sID;
        m_bHasGeneratedID = false;
      }
      else
      {
        m_sID = GlobalIDFactory.getNewStringID ();
        m_bHasGeneratedID = true;
      }
      m_aLabel = aLabel;
      m_aContent = aContent;
      m_bDisabled = bDisabled;
    }

    @Nonnull
    @Nonempty
    public String getID ()
    {
      return m_sID;
    }

    public boolean isGeneratedID ()
    {
      return m_bHasGeneratedID;
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

    @Nonnull
    public Tab setLabel (@Nullable final IHCNode aLabel)
    {
      m_aLabel = aLabel;
      return this;
    }

    @Nullable
    public IHCNode getContent ()
    {
      return m_aContent;
    }

    @Nonnull
    public Tab setContent (@Nullable final IHCNode aContent)
    {
      m_aContent = aContent;
      return this;
    }

    public boolean isDisabled ()
    {
      return m_bDisabled;
    }

    @Nonnull
    public Tab setDisabled (final boolean bDisabled)
    {
      m_bDisabled = bDisabled;
      return this;
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (this).append ("ID", m_sID)
                                         .append ("generatedID", m_bHasGeneratedID)
                                         .append ("label", m_aLabel)
                                         .append ("content", m_aContent)
                                         .append ("disabled", m_bDisabled)
                                         .toString ();
    }
  }

  /** By default a tab is not active */
  public static final boolean DEFAULT_ACTIVE = false;
  /** By default a tab is not disabled */
  public static final boolean DEFAULT_DISABLED = false;

  private static final Logger s_aLogger = LoggerFactory.getLogger (BootstrapTabBox.class);

  private EBootstrapTabBoxType m_eType = EBootstrapTabBoxType.TOP;
  private final Map <String, Tab> m_aTabs = new LinkedHashMap <String, Tab> ();
  private String m_sActiveTabID;

  public BootstrapTabBox ()
  {
    super ();
  }

  @Nonnull
  public EBootstrapTabBoxType getType ()
  {
    return m_eType;
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
    if (StringHelper.hasText (sID) && !m_aTabs.containsKey (sID))
      s_aLogger.warn ("No tab with ID '" + sID + "' to be set active!");
    return this;
  }

  @Nonnull
  public Tab addTab (@Nullable final String sLabel, @Nullable final IHCNode aContent)
  {
    return addTab (null, new HCTextNode (sLabel), aContent, DEFAULT_ACTIVE);
  }

  @Nonnull
  public Tab addTab (@Nullable final String sLabel, @Nullable final IHCNode aContent, final boolean bActive)
  {
    return addTab (null, new HCTextNode (sLabel), aContent, bActive);
  }

  @Nonnull
  public Tab addTab (@Nullable final String sLabel,
                     @Nullable final IHCNode aContent,
                     final boolean bActive,
                     final boolean bDisabled)
  {
    return addTab (null, new HCTextNode (sLabel), aContent, bActive, bDisabled);
  }

  @Nonnull
  public Tab addTab (@Nullable final String sID,
                     @Nullable final String sLabel,
                     @Nullable final IHCNode aContent,
                     final boolean bActive)
  {
    return addTab (sID, new HCTextNode (sLabel), aContent, bActive);
  }

  @Nonnull
  public Tab addTab (@Nullable final IHCNode aLabel, @Nullable final IHCNode aContent)
  {
    return addTab (null, aLabel, aContent, DEFAULT_ACTIVE);
  }

  @Nonnull
  public Tab addTab (@Nullable final IHCNode aLabel, @Nullable final IHCNode aContent, final boolean bActive)
  {
    return addTab (null, aLabel, aContent, bActive);
  }

  @Nonnull
  public Tab addTab (@Nullable final IHCNode aLabel,
                     @Nullable final IHCNode aContent,
                     final boolean bActive,
                     final boolean bDisabled)
  {
    return addTab (null, aLabel, aContent, bActive, bDisabled);
  }

  @Nonnull
  public Tab addTab (@Nullable final String sID,
                     @Nullable final IHCNode aLabel,
                     @Nullable final IHCNode aContent,
                     final boolean bActive)
  {
    return addTab (sID, aLabel, aContent, bActive, DEFAULT_DISABLED);
  }

  @Nonnull
  public Tab addTab (@Nullable final String sID,
                     @Nullable final IHCNode aLabel,
                     @Nullable final IHCNode aContent,
                     final boolean bActive,
                     final boolean bDisabled)
  {
    final Tab aTab = new Tab (sID, aLabel, aContent, bDisabled);
    addTab (aTab, bActive);
    return aTab;
  }

  @Nonnull
  public BootstrapTabBox addTab (@Nonnull final Tab aTab, final boolean bActive)
  {
    if (aTab == null)
      throw new NullPointerException ("tab");

    // Tab ID may be generated, if null was provided
    final String sTabID = aTab.getID ();
    m_aTabs.put (sTabID, aTab);
    if (bActive)
      m_sActiveTabID = sTabID;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Tab> getAllTabs ()
  {
    return ContainerHelper.newList (m_aTabs.values ());
  }

  @Nullable
  public Tab getTabOfID (@Nullable final String sID)
  {
    return m_aTabs.get (sID);
  }

  /**
   * @return The tab marked as active or <code>null</code> if no tab is marked
   *         as active.
   */
  @Nullable
  public Tab getActiveTab ()
  {
    Tab aTab = null;
    // Any active tab set?
    if (m_sActiveTabID != null)
      aTab = getTabOfID (m_sActiveTabID);
    return aTab;
  }

  /**
   * @return The tab marked as active, or the first tab which will be active by
   *         default. May be <code>null</code> if no tab is contained
   */
  @Nullable
  public Tab getActiveTabOrDefault ()
  {
    Tab aTab = getActiveTab ();
    // Invalid or no active tab -> use first tab (as done below in build)
    if (aTab == null)
      aTab = ContainerHelper.getFirstValue (m_aTabs);
    return aTab;
  }

  @Nonnull
  public EChange removeTab (@Nullable final String sTabID)
  {
    if (m_aTabs.remove (sTabID) == null)
      return EChange.UNCHANGED;
    if (m_sActiveTabID != null && m_sActiveTabID.equals (sTabID))
      m_sActiveTabID = null;
    return EChange.CHANGED;
  }

  @Nullable
  public HCDiv build ()
  {
    if (m_aTabs.isEmpty ())
      return null;

    final HCDiv aDiv = new HCDiv ().addClasses (CBootstrapCSS.TABBABLE, m_eType);

    String sActiveTabID = m_sActiveTabID;
    if (StringHelper.hasNoText (sActiveTabID))
    {
      // Activate first tab by default
      sActiveTabID = ContainerHelper.getFirstKey (m_aTabs);
    }

    // Build code for tabs and content
    final HCUL aTabs = new HCUL ().addClasses (CBootstrapCSS.NAV, CBootstrapCSS.NAV_TABS);
    final HCDiv aContent = new HCDiv ().addClass (CBootstrapCSS.TAB_CONTENT);
    for (final Tab aTab : m_aTabs.values ())
    {
      final boolean bIsActiveTab = aTab.getID ().equals (sActiveTabID);

      // header
      final HCLI aToggleLI = aTabs.addItem ();
      if (bIsActiveTab)
        aToggleLI.addClass (CBootstrapCSS.ACTIVE);
      if (aTab.isDisabled ())
      {
        aToggleLI.addClass (CBootstrapCSS.DISABLED);
        aToggleLI.addChild (new HCA (aTab.getLinkURL ()).addChild (aTab.getLabel ()));
      }
      else
      {
        aToggleLI.addChild (new HCA (aTab.getLinkURL ()).setCustomAttr ("data-toggle", "tab")
                                                        .addChild (aTab.getLabel ()));
      }

      // content
      final HCDiv aPane = aContent.addAndReturnChild (new HCDiv ().addChild (aTab.getContent ())
                                                                  .addClass (CBootstrapCSS.TAB_PANE)
                                                                  .setID (aTab.getID ()));
      if (bIsActiveTab)
        aPane.addClass (CBootstrapCSS.ACTIVE);
    }

    // Determine order of elements
    if (m_eType.emitTabsBeforeContent ())
      aDiv.addChildren (aTabs, aContent);
    else
      aDiv.addChildren (aContent, aTabs);
    return aDiv;
  }
}
