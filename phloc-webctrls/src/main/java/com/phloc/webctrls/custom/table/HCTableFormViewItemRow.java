package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
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
  private final IHCCell <?> m_aLabelCell;
  private final IHCCell <?> m_aCtrlCell;

  public HCTableFormViewItemRow (final boolean bHeader)
  {
    super (bHeader);
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

  /**
   * Called after the label cell was altered
   */
  @OverrideOnDemand
  protected void onLabelModified ()
  {}

  @Nonnull
  public HCTableFormViewItemRow setLabel (@Nullable final IFormLabel aLabel)
  {
    m_aLabelCell.removeAllChildren ().addChild (aLabel);
    onLabelModified ();
    return this;
  }

  @Nonnull
  public IHCCell <?> getLabelCell ()
  {
    return m_aLabelCell;
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

  /**
   * Called after the control cell was altered
   */
  @OverrideOnDemand
  protected void onCtrlsModified ()
  {}

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrlCell.removeAllChildren ().addChild (aCtrl);
    onCtrlsModified ();
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final IHCNode... aCtrls)
  {
    m_aCtrlCell.removeAllChildren ().addChildren (aCtrls);
    onCtrlsModified ();
    return this;
  }

  @Nonnull
  public HCTableFormViewItemRow setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    m_aCtrlCell.removeAllChildren ().addChildren (aCtrls);
    onCtrlsModified ();
    return this;
  }

  @Nonnull
  public IHCCell <?> getCtrlCell ()
  {
    return m_aCtrlCell;
  }
}
