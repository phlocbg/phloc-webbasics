package com.phloc.webctrls.custom.table;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.IFormNote;
import com.phloc.webctrls.custom.impl.HCFormLabel;

public class HCTableFormItemRowBuilder
{
  private final IHCTableForm <?> m_aOwner;
  private IFormLabel m_aLabel;
  private List <IHCNode> m_aCtrls;
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

  @Nullable
  public IFormLabel getLabel ()
  {
    return m_aLabel;
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
    m_aCtrls = ContainerHelper.newList (aCtrl);
    return this;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final IHCNode... aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    return this;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    return this;
  }

  @Nullable
  public List <IHCNode> getCtrls ()
  {
    return ContainerHelper.newList (m_aCtrls);
  }

  @Nonnull
  public HCTableFormItemRowBuilder setNote (@Nullable final IFormNote aNote)
  {
    m_aNote = aNote;
    return this;
  }

  @Nullable
  public IFormNote getNote ()
  {
    return m_aNote;
  }

  @Nonnull
  public HCTableFormItemRowBuilder setErrorList (@Nullable final IErrorList aErrorList)
  {
    m_aErrorList = aErrorList;
    return this;
  }

  @Nullable
  public IErrorList getErrorList ()
  {
    return m_aErrorList;
  }

  public void appendToTable ()
  {
    m_aOwner.addItemRowWithNote (m_aLabel, m_aCtrls, m_aNote, m_aErrorList);
  }
}
