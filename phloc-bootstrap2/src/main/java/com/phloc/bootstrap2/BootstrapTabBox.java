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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.webctrls.custom.tabbox.AbstractTabBox;
import com.phloc.webctrls.custom.tabbox.Tab;

/**
 * Represent a single tab box
 * 
 * @author Philip Helger
 */
public class BootstrapTabBox extends AbstractTabBox <BootstrapTabBox>
{
  private EBootstrapTabBoxType m_eType = EBootstrapTabBoxType.TOP;

  public BootstrapTabBox ()
  {}

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
  public HCDiv build ()
  {
    if (m_aTabs.isEmpty ())
      return null;

    final HCDiv aDiv = new HCDiv ().addClasses (CBootstrapCSS.TABBABLE, m_eType);

    String sActiveTabID = getActiveTabID ();
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
        aToggleLI.addChild (new HCA (aTab.getLinkURL ()).setDataAttr ("toggle", "tab").addChild (aTab.getLabel ()));
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
