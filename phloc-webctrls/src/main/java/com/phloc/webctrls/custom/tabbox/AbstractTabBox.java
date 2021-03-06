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
package com.phloc.webctrls.custom.tabbox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Represent a single tab box
 * 
 * @author Philip Helger
 */
public abstract class AbstractTabBox <IMPLTYPE extends ITabBox <IMPLTYPE>> implements ITabBox <IMPLTYPE>
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractTabBox.class);

  protected final Map <String, Tab> m_aTabs = new LinkedHashMap <String, Tab> ();
  private String m_sActiveTabID;

  public AbstractTabBox ()
  {}

  @Nonnull
  protected final IMPLTYPE thisAsT ()
  {
    // Avoid the unchecked cast warning in all places
    return GenericReflection.<AbstractTabBox <IMPLTYPE>, IMPLTYPE> uncheckedCast (this);
  }

  public String getActiveTabID ()
  {
    return m_sActiveTabID;
  }

  @Nonnull
  public IMPLTYPE setActiveTabID (@Nullable final String sID)
  {
    m_sActiveTabID = sID;
    if (StringHelper.hasText (sID) && !m_aTabs.containsKey (sID))
      s_aLogger.warn ("No tab with ID '" + sID + "' to be set active!");
    return thisAsT ();
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
  public IMPLTYPE addTab (@Nonnull final Tab aTab, final boolean bActive)
  {
    ValueEnforcer.notNull (aTab, "Tab");

    // Tab ID may be generated, if null was provided
    final String sTabID = aTab.getID ();
    m_aTabs.put (sTabID, aTab);
    if (bActive)
      m_sActiveTabID = sTabID;
    return thisAsT ();
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

  public boolean hasNoTabs ()
  {
    return m_aTabs.isEmpty ();
  }

  @Nonnegative
  public int getTabCount ()
  {
    return m_aTabs.size ();
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
}
