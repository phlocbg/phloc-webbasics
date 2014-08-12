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
package com.phloc.bootstrap3.modal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.base.BootstrapCloseIcon;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH4;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.JQuery;

public class BootstrapModal implements IHCNodeBuilder, IHasID <String>
{
  public static final boolean DEFAULT_FADE = true;
  public static final boolean DEFAULT_SHOW_CLOSE = true;

  public static final String JS_EVENT_SHOW = "show.bs.modal";
  public static final String JS_EVENT_SHOWN = "shown.bs.modal";
  public static final String JS_EVENT_HIDE = "hide.bs.modal";
  public static final String JS_EVENT_HIDDEN = "hidden.bs.modal";
  public static final String JS_EVENT_LOADED = "loaded.bs.modal";

  private final String m_sID;
  private final EBootstrapModalSize m_eSize;
  private boolean m_bFade = DEFAULT_FADE;
  private boolean m_bShowClose = DEFAULT_SHOW_CLOSE;
  private IHCNode m_aHeader;
  private IHCNode m_aBody;
  private IHCNode m_aFooter;

  public BootstrapModal ()
  {
    this (EBootstrapModalSize.DEFAULT);
  }

  public BootstrapModal (@Nonnull final EBootstrapModalSize eSize)
  {
    ValueEnforcer.notNull (eSize, "Size");
    m_sID = GlobalIDFactory.getNewStringID ();
    m_eSize = eSize;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public EBootstrapModalSize getSize ()
  {
    return m_eSize;
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

  public boolean isShowClose ()
  {
    return m_bShowClose;
  }

  @Nonnull
  public BootstrapModal setShowClose (final boolean bShowClose)
  {
    m_bShowClose = bShowClose;
    return this;
  }

  @Nonnull
  public BootstrapModal setHeader (@Nullable final String sHeader)
  {
    return setHeader (HCTextNode.createOnDemand (sHeader));
  }

  @Nonnull
  public BootstrapModal setHeader (@Nullable final IHCNode aHeader)
  {
    m_aHeader = aHeader;
    return this;
  }

  @Nonnull
  public BootstrapModal setHeader (@Nullable final IHCNode... aFooter)
  {
    return setHeader (HCNodeList.create (aFooter));
  }

  @Nonnull
  public BootstrapModal setBody (@Nullable final String sBody)
  {
    return setBody (HCTextNode.createOnDemand (sBody));
  }

  @Nonnull
  public BootstrapModal setBody (@Nullable final IHCNode aBody)
  {
    m_aBody = aBody;
    return this;
  }

  @Nonnull
  public BootstrapModal setBody (@Nullable final IHCNode... aFooter)
  {
    return setBody (HCNodeList.create (aFooter));
  }

  @Nonnull
  public BootstrapModal setFooter (@Nullable final String sFooter)
  {
    return setFooter (HCTextNode.createOnDemand (sFooter));
  }

  @Nonnull
  public BootstrapModal setFooter (@Nullable final IHCNode aFooter)
  {
    m_aFooter = aFooter;
    return this;
  }

  @Nonnull
  public BootstrapModal setFooter (@Nullable final IHCNode... aFooter)
  {
    return setFooter (HCNodeList.create (aFooter));
  }

  @Nullable
  public HCDiv build ()
  {
    final HCDiv ret = new HCDiv ().addClass (CBootstrapCSS.MODAL)
                                  .setRole (EHTMLRole.DIALOG)
                                  .setCustomAttr (CHTMLAttributes.ARIA_HIDDEN, "true")
                                  .setID (m_sID);
    if (m_bFade)
      ret.addClass (CBootstrapCSS.FADE);

    final HCDiv aDialog = ret.addAndReturnChild (new HCDiv ().addClasses (CBootstrapCSS.MODAL_DIALOG, m_eSize));
    final HCDiv aContent = aDialog.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.MODAL_CONTENT));
    if (m_aHeader != null)
    {
      final String sTitleID = m_sID + "t";
      ret.setCustomAttr (CHTMLAttributes.ARIA_LABELLEDBY, sTitleID);
      final HCDiv aHeader = aContent.addAndReturnChild (new HCDiv ().addClass (CBootstrapCSS.MODAL_HEADER));
      if (m_bShowClose)
        aHeader.addChild (new BootstrapCloseIcon ().setDataAttr ("dismiss", "modal"));
      aHeader.addChild (new HCH4 ().addClass (CBootstrapCSS.MODAL_TITLE).setID (sTitleID).addChild (m_aHeader));
    }
    if (m_aBody != null)
      aContent.addChild (new HCDiv ().addClass (CBootstrapCSS.MODAL_BODY).addChild (m_aBody));
    if (m_aFooter != null)
      aContent.addChild (new HCDiv ().addClass (CBootstrapCSS.MODAL_FOOTER).addChild (m_aFooter));
    return ret;
  }

  /**
   * @return JS invocation to open this modal dialog.
   */
  @Nonnull
  public JSInvocation jsModal ()
  {
    return JQuery.idRef (m_sID).invoke ("modal");
  }

  /**
   * Activates your content as a modal. Accepts an optional options object.
   *
   * @param aBackdrop
   *        Includes a modal-backdrop element. Alternatively, specify static for
   *        a backdrop which doesn't close the modal on click.
   * @param aKeyboard
   *        Closes the modal when escape key is pressed
   * @param aShow
   *        Shows the modal when initialized.
   * @param sRemotePath
   *        If a remote URL is provided, content will be loaded one time via
   *        jQuery's load method and injected into the .modal-content div.
   * @return JS invocation to open this modal dialog with the specified options.
   */
  @Nonnull
  public JSInvocation openModal (@Nullable final EBootstrapModalOptionBackdrop aBackdrop,
                                 @Nullable final Boolean aKeyboard,
                                 @Nullable final Boolean aShow,
                                 @Nullable final String sRemotePath)
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    if (aBackdrop != null)
      aOptions.add ("backdrop", aBackdrop.getJSExpression ());
    if (aKeyboard != null)
      aOptions.add ("keyboard", aKeyboard.booleanValue ());
    if (aShow != null)
      aOptions.add ("show", aShow.booleanValue ());
    if (StringHelper.hasText (sRemotePath))
      aOptions.add ("remote", sRemotePath);
    return jsModal ().arg (aOptions);
  }

  /**
   * Manually toggles a modal. Returns to the caller before the modal has
   * actually been shown or hidden (i.e. before the shown.bs.modal or
   * hidden.bs.modal event occurs).
   *
   * @return JS invocation
   */
  @Nonnull
  public JSInvocation jsModalToggle ()
  {
    return jsModal ().arg ("toggle");
  }

  /**
   * Manually opens a modal. Returns to the caller before the modal has actually
   * been shown (i.e. before the shown.bs.modal event occurs).
   *
   * @return JS invocation
   */
  @Nonnull
  public JSInvocation jsModalShow ()
  {
    return jsModal ().arg ("show");
  }

  /**
   * Manually hides a modal. Returns to the caller before the modal has actually
   * been hidden (i.e. before the hidden.bs.modal event occurs).
   *
   * @return JS invocation
   */
  @Nonnull
  public JSInvocation jsModalHide ()
  {
    return jsModal ().arg ("hide");
  }
}
