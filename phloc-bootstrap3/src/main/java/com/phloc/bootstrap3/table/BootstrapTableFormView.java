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
package com.phloc.bootstrap3.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.table.HCTableFormViewItemRow;
import com.phloc.webctrls.custom.table.IHCTableFormView;

public class BootstrapTableFormView extends AbstractBootstrapTable <BootstrapTableFormView> implements IHCTableFormView <BootstrapTableFormView>
{
  private void _init ()
  {
    setHover (true);
    setStriped (true);
  }

  public BootstrapTableFormView ()
  {
    super ();
    _init ();
  }

  public BootstrapTableFormView (@Nullable final HCCol aCol)
  {
    super (aCol);
    _init ();
  }

  public BootstrapTableFormView (@Nullable final HCCol... aCols)
  {
    super (aCols);
    _init ();
  }

  public BootstrapTableFormView (@Nullable final Iterable <? extends HCCol> aCols)
  {
    super (aCols);
    _init ();
  }

  @Nonnull
  public HCTableFormViewItemRow createItemRow ()
  {
    final HCTableFormViewItemRow aRow = new HCTableFormViewItemRow (false);
    addBodyRow (aRow);
    return aRow;
  }

  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    createItemRow ().setLabel (aLabel).setCtrl (sValue);
  }

  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String... aValues)
  {
    createItemRow ().setLabel (aLabel).setCtrl (aValues);
  }

  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aValue)
  {
    createItemRow ().setLabel (aLabel).setCtrl (aValue);
  }

  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode... aValues)
  {
    createItemRow ().setLabel (aLabel).setCtrl (aValues);
  }

  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aValues)
  {
    createItemRow ().setLabel (aLabel).setCtrl (aValues);
  }

  public void addItemRow (@Nonnull final String sLabel, @Nullable final String sValue)
  {
    createItemRow ().setLabel (sLabel).setCtrl (sValue);
  }

  public void addItemRow (@Nonnull final String sLabel, @Nullable final String... aValue)
  {
    createItemRow ().setLabel (sLabel).setCtrl (aValue);
  }

  public void addItemRow (@Nonnull final String sLabel, @Nullable final IHCNode aValue)
  {
    createItemRow ().setLabel (sLabel).setCtrl (aValue);
  }

  public void addItemRow (@Nonnull final String sLabel, @Nullable final IHCNode... aValues)
  {
    createItemRow ().setLabel (sLabel).setCtrl (aValues);
  }

  public void addItemRow (@Nonnull final String sLabel, @Nullable final Iterable <? extends IHCNode> aValues)
  {
    createItemRow ().setLabel (sLabel).setCtrl (aValues);
  }
}
