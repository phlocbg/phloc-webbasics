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
package com.phloc.webctrls.custom.table;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.IError;
import com.phloc.validation.error.IErrorList;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.impl.HCFormLabel;

public class HCTableFormItemRow extends HCRow
{
  private final IHCCell <?> m_aLabelCell;
  private List <IHCNode> m_aCtrls;
  private IErrorList m_aErrorList;
  private final IHCCell <?> m_aCtrlCell;
  private final IHCCell <?> m_aNoteCell;

  public HCTableFormItemRow (final boolean bHeader, final boolean bHasNoteColumn)
  {
    super (bHeader);
    m_aLabelCell = addCell ();
    m_aCtrlCell = addCell ();
    m_aNoteCell = bHasNoteColumn ? addCell () : null;
  }

  @Nonnull
  public final HCTableFormItemRow setLabel (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return setLabel (aTextProvider.getText ());
  }

  @Nonnull
  public final HCTableFormItemRow setLabel (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel));
  }

  @Nonnull
  public final HCTableFormItemRow setLabel (@Nullable final String sLabel, @Nonnull final ELabelType eLabelType)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.create (sLabel, eLabelType));
  }

  @Nonnull
  public final HCTableFormItemRow setLabelMandatory (@Nullable final String sLabel)
  {
    return setLabel (sLabel == null ? null : HCFormLabel.createMandatory (sLabel));
  }

  /**
   * Called after the label cell was altered.
   * 
   * @param aLabel
   *        The newly set label. May be <code>null</code>
   */
  @OverrideOnDemand
  protected void onLabelModified (@Nullable final IFormLabel aLabel)
  {}

  @Nonnull
  public final HCTableFormItemRow setLabel (@Nullable final IFormLabel aLabel)
  {
    m_aLabelCell.removeAllChildren ().addChild (aLabel);
    onLabelModified (aLabel);
    return this;
  }

  @Nonnull
  public final IHCCell <?> getLabelCell ()
  {
    return m_aLabelCell;
  }

  /**
   * Modify the passed controls for a certain row
   * 
   * @param aCtrls
   *        The list of controls for a cell. Never <code>null</code> but maybe
   *        empty
   * @param bHasErrors
   *        <code>true</code> if at least one error is contained in this row.
   */
  @OverrideOnDemand
  protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
  {}

  @Nullable
  @OverrideOnDemand
  protected IHCNode createErrorNode (@Nonnull final IError aError)
  {
    return new HCDiv ().addChild (aError.getErrorText ());
  }

  /**
   * Called after the controls cell was altered
   * 
   * @param aResolvedCtrls
   *        The controls added to the cell. Never <code>null</code>.
   * @param bHasErrors
   *        <code>true</code> if this line contains errors
   */
  @OverrideOnDemand
  protected void onCtrlsModified (@Nonnull final List <IHCNode> aResolvedCtrls, final boolean bHasErrors)
  {}

  private void _updateCtrlCell ()
  {
    // Clear existing content
    m_aCtrlCell.removeAllChildren ();

    final boolean bHasErrors = m_aErrorList != null && m_aErrorList.containsAtLeastOneError ();

    // Flatten all HCNodeLists away :)
    final List <IHCNode> aResolvedCtrls = HCUtils.getAsFlattenedList (m_aCtrls);

    // Customize controls
    modifyControls (aResolvedCtrls, bHasErrors);

    // Replace existing children with flattened list
    m_aCtrlCell.addChildren (aResolvedCtrls);

    // Create all potential error nodes
    if (m_aErrorList != null)
      for (final IError aError : m_aErrorList.getAllItems ())
        m_aCtrlCell.addChild (createErrorNode (aError));

    onCtrlsModified (aResolvedCtrls, bHasErrors);
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final String sValue)
  {
    return setCtrl (new HCTextNode (sValue));
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final String... aValues)
  {
    return setCtrl (HCNodeList.create (aValues));
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final IHCNodeBuilder aCtrlBuilder)
  {
    return setCtrl (aCtrlBuilder == null ? null : aCtrlBuilder.build ());
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final IHCNode aCtrl)
  {
    m_aCtrls = ContainerHelper.newList (aCtrl);
    _updateCtrlCell ();
    return this;
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final IHCNode... aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    _updateCtrlCell ();
    return this;
  }

  @Nonnull
  public final HCTableFormItemRow setCtrl (@Nullable final Iterable <? extends IHCNode> aCtrls)
  {
    m_aCtrls = ContainerHelper.newList (aCtrls);
    _updateCtrlCell ();
    return this;
  }

  @Nullable
  public final List <IHCNode> getCtrls ()
  {
    return ContainerHelper.newList (m_aCtrls);
  }

  @Nonnull
  public final HCTableFormItemRow setErrorList (@Nullable final IErrorList aErrorList)
  {
    m_aErrorList = aErrorList;
    _updateCtrlCell ();
    return this;
  }

  @Nullable
  public final IErrorList getErrorList ()
  {
    return m_aErrorList;
  }

  @Nonnull
  public final IHCCell <?> getCtrlCell ()
  {
    return m_aCtrlCell;
  }

  /**
   * Called after the note cell was altered.
   * 
   * @param aNote
   *        The new note
   */
  @OverrideOnDemand
  protected void onNoteModified (@Nullable final IHCNode aNote)
  {}

  @Nonnull
  public final HCTableFormItemRow setNote (@Nullable final IHCNode aNote)
  {
    if (m_aNoteCell == null)
      throw new IllegalStateException ("This table has no note column!");

    m_aNoteCell.removeAllChildren ().addChild (aNote);
    onNoteModified (aNote);
    return this;
  }

  @Nullable
  public final IHCCell <?> getNoteCell ()
  {
    return m_aNoteCell;
  }
}
