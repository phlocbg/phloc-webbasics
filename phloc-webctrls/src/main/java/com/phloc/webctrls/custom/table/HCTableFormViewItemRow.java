package com.phloc.webctrls.custom.table;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.impl.HCFormLabel;

public class HCTableFormViewItemRow extends HCRow
{
  private IFormLabel m_aLabel;
  private final IHCCell <?> m_aLabelCell;
  private List <? extends IHCNode> m_aCtrls;
  private final IHCCell <?> m_aCtrlCell;

  public HCTableFormViewItemRow ()
  {
    super (false);
    m_aLabelCell = addCell ();
    m_aCtrlCell = addCell ();
  }

  @Nonnull
  public HCTableFormViewItemRow setLabel (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return setLabel (aTextProvider.getText ());
  }

  @Nonnull
  public HCTableFormViewItemRow setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel));
  }

  @Nonnull
  public HCTableFormViewItemRow setLabel (@Nullable final IFormLabel aLabel)
  {
    m_aLabel = aLabel;
    m_aLabelCell.removeAllChildren ().addChild (aLabel);
    return this;
  }

  @Nullable
  public IFormLabel getLabel ()
  {
    return m_aLabel;
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final String sValue)
  {
    return setCtrl (new HCTextNode (sValue));
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final String... aValues)
  {
    return setCtrl (HCNodeList.create (aValues));
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return setCtrl (aCtrlBuilder == null ? null : aCtrlBuilder.build ());
  }

  private void _setCtrls (@Nullable final List <? extends IHCNode> aCtrls)
  {
    m_aCtrls = aCtrls;
    m_aCtrlCell.removeAllChildren ().addChildren (aCtrls);
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final IHCNode aCtrl)
  {
    _setCtrls (ContainerHelper.newList (aCtrl));
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final IHCNode... aCtrls)
  {
    _setCtrls (ContainerHelper.newList (aCtrls));
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    _setCtrls (ContainerHelper.newList (aCtrls));
    return this;
  }

  @Nullable
  public List <IHCNode> getCtrls ()
  {
    return ContainerHelper.newList (m_aCtrls);
  }
}
