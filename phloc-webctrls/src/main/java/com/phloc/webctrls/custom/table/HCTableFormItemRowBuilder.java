package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.IFormNote;
import com.phloc.webctrls.custom.impl.HCFormLabel;

public final class HCTableFormItemRowBuilder
{
  private final IHCTableForm <?> m_aOwner;
  private IFormLabel m_aLabel;
  private IHCNode m_aCtrl;
  private IFormNote m_aNote;
  private IErrorList m_aErrorList;

  public HCTableFormItemRowBuilder (@Nonnull final IHCTableForm <?> aOwner)
  {
    if (aOwner == null)
      throw new NullPointerException ("owner");
    m_aOwner = aOwner;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setLabel (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return setLabel (aTextProvider.getText ());
  }

  @Nonnull
  public HCTableFormItemRowBuilder setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel));
  }

  @Nonnull
  public HCTableFormItemRowBuilder setLabel (@Nullable final IFormLabel aLabel)
  {
    m_aLabel = aLabel;
    return this;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final String sValue)
  {
    return setCtrl (new HCTextNode (sValue));
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final String... aValues)
  {
    return setCtrl (HCNodeList.create (aValues));
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return setCtrl (aCtrlBuilder == null ? null : aCtrlBuilder.build ());
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrl = aCtrl;
    return this;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final IHCNode... aCtrls)
  {
    return setCtrl (HCNodeList.create (aCtrls));
  }

  @Nonnull
  public HCTableFormItemRowBuilder setNote (@Nullable final IFormNote aNote)
  {
    m_aNote = aNote;
    return this;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setErrorList (@Nullable final IErrorList aErrorList)
  {
    m_aErrorList = aErrorList;
    return this;
  }

  public void appendToTable ()
  {
    if (m_aNote == null)
      m_aOwner.addItemRow (m_aLabel, m_aCtrl, m_aErrorList);
    else
      m_aOwner.addItemRowWithNote (m_aLabel, m_aCtrl, m_aNote, m_aErrorList);
  }
}
