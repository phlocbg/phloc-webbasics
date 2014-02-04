package com.phloc.bootstrap3.modal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.base.BootstrapCloseIcon;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH4;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapModal implements IHCNodeBuilder, IHasID <String>
{
  public static final boolean DEFAULT_FADE = true;
  public static final boolean DEFAULT_SHOW_CLOSE = true;

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
    if (eSize == null)
      throw new NullPointerException ("Size");
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
  public IHCNode build ()
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
}
