package com.phloc.webctrls.custom.table;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCBaseTable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.impl.HCFormLabel;

public class HCTableFormViewItemRowBuilder
{
  private final IHCBaseTable <?> m_aOwner;
  private IFormLabel m_aLabel;
  private List <IHCNode> m_aCtrls;

  public HCTableFormViewItemRowBuilder (@Nonnull final IHCBaseTable <?> aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setLabel (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return setLabel (aTextProvider.getText ());
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel));
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setLabel (@Nullable final IFormLabel aLabel)
  {
    m_aLabel = aLabel;
    return this;
  }

  @Nullable
  public IFormLabel getLabel ()
  {
    return m_aLabel;
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final String sValue)
  {
    return setCtrl (new HCTextNode (sValue));
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final String... aValues)
  {
    return setCtrl (HCNodeList.create (aValues));
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return setCtrl (aCtrlBuilder == null ? null : aCtrlBuilder.build ());
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrls = ContainerHelper.newList (aCtrl);
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final IHCNode... aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRowBuilder setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    return this;
  }

  @Nullable
  public List <IHCNode> getCtrls ()
  {
    return ContainerHelper.newList (m_aCtrls);
  }

  public void appendToTable ()
  {
    m_aOwner.addBodyRow ().addCell (m_aLabel).addCell (m_aCtrls);
  }
}
