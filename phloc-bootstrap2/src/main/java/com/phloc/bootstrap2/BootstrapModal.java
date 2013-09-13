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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.EHTMLRole;
import com.phloc.html.entities.EHTMLEntity;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCEntityNode;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapModal implements IHCNodeBuilder
{
  public static final boolean DEFAULT_HIDE = true;
  public static final boolean DEFAULT_FADE = true;
  public static final boolean DEFAULT_SHOW_CLOSE_BUTTON = true;

  private String m_sID;
  private boolean m_bHide = DEFAULT_HIDE;
  private boolean m_bFade = DEFAULT_FADE;
  private boolean m_bShowCloseButton = DEFAULT_SHOW_CLOSE_BUTTON;
  private IHCNode m_aHeaderContent;
  private IHCNode m_aBodyContent;
  private IHCNode m_aFooterContent;

  public BootstrapModal ()
  {
    m_sID = GlobalIDFactory.getNewStringID ();
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public BootstrapModal setID (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
    return this;
  }

  public boolean isHide ()
  {
    return m_bHide;
  }

  @Nonnull
  public BootstrapModal setHide (final boolean bHide)
  {
    m_bHide = bHide;
    return this;
  }

  public boolean isFade ()
  {
    return m_bFade;
  }

  @Nonnull
  public BootstrapModal setFade (final boolean bFade)
  {
    m_bFade = bFade;
    return this;
  }

  public boolean isShowCloseButton ()
  {
    return m_bShowCloseButton;
  }

  @Nonnull
  public BootstrapModal setShowCloseButton (final boolean bShowCloseButton)
  {
    m_bShowCloseButton = bShowCloseButton;
    return this;
  }

  @Nullable
  public IHCNode getHeaderContent ()
  {
    return m_aHeaderContent;
  }

  @Nonnull
  public BootstrapModal setHeaderContent (@Nullable final String sText)
  {
    return setHeaderContent (StringHelper.hasText (sText) ? new HCTextNode (sText) : null);
  }

  @Nonnull
  public BootstrapModal setHeaderContent (@Nullable final IHCNode aHeaderContent)
  {
    m_aHeaderContent = aHeaderContent;
    return this;
  }

  @Nullable
  public IHCNode getBodyContent ()
  {
    return m_aBodyContent;
  }

  @Nonnull
  public BootstrapModal setBodyContent (@Nullable final String sText)
  {
    return setBodyContent (StringHelper.hasText (sText) ? new HCTextNode (sText) : null);
  }

  @Nonnull
  public BootstrapModal setBodyContent (@Nullable final IHCNode aBodyContent)
  {
    m_aBodyContent = aBodyContent;
    return this;
  }

  @Nullable
  public IHCNode getFooterContent ()
  {
    return m_aFooterContent;
  }

  @Nonnull
  public BootstrapModal setFooterContent (@Nullable final String sText)
  {
    return setFooterContent (StringHelper.hasText (sText) ? new HCTextNode (sText) : null);
  }

  @Nonnull
  public BootstrapModal setFooterContent (@Nullable final IHCNode aFooterContent)
  {
    m_aFooterContent = aFooterContent;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCDiv ret = new HCDiv ().setID (m_sID).setRole (EHTMLRole.DIALOG);
    ret.addClass (CBootstrapCSS.MODAL);
    ret.addClass (m_bHide ? CBootstrapCSS.HIDE : CBootstrapCSS.SHOW);
    if (m_bFade)
      ret.addClass (CBootstrapCSS.FADE);
    if (m_bHide)
      ret.setCustomAttr (CHTMLAttributes.ARIA_HIDDEN, "true");

    if (m_aHeaderContent != null || m_bShowCloseButton)
    {
      final HCDiv aHeader = ret.addAndReturnChild (new HCDiv ());
      aHeader.addClass (CBootstrapCSS.MODAL_HEADER);

      if (m_bShowCloseButton)
        aHeader.addChild (new HCButton ().addClass (CBootstrapCSS.CLOSE)
                                         .setCustomAttr ("data-dismiss", "modal")
                                         .setCustomAttr (CHTMLAttributes.ARIA_HIDDEN, "true")
                                         .addChild (new HCEntityNode (EHTMLEntity.times, "x")));

      if (m_aHeaderContent != null)
      {
        IHCElement <?> aElement;
        if (m_aHeaderContent instanceof IHCElement <?>)
          aElement = (IHCElement <?>) m_aHeaderContent;
        else
          aElement = HCSpan.create (m_aHeaderContent);
        aElement.setID (m_sID + "Label");
        aHeader.addChild (aElement);
        ret.setCustomAttr (CHTMLAttributes.ARIA_LABELLEDBY, aElement.getID ());
      }
    }

    if (m_aBodyContent != null)
    {
      final HCDiv aBody = ret.addAndReturnChild (new HCDiv ());
      aBody.addClass (CBootstrapCSS.MODAL_BODY);
      aBody.addChild (m_aBodyContent);
    }

    if (m_aFooterContent != null)
    {
      final HCDiv aFooter = ret.addAndReturnChild (new HCDiv ());
      aFooter.addClass (CBootstrapCSS.MODAL_FOOTER);
      aFooter.addChild (m_aFooterContent);
    }

    return ret;
  }

  /**
   * @return A close button to be used e.g. inside the footer. Never
   *         <code>null</code>. Note: the button has no content (like label)!
   */
  @Nonnull
  public static BootstrapButton_Button createCloseButton ()
  {
    final BootstrapButton_Button ret = new BootstrapButton_Button ();
    ret.setCustomAttr ("data-dismiss", "modal");
    ret.setCustomAttr (CHTMLAttributes.ARIA_HIDDEN, "true");
    return ret;
  }
}
