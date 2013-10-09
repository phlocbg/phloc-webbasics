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
package com.phloc.bootstrap2.derived;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.AbstractBootstrapTable;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.impl.HCFormLabel;
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

  @Deprecated
  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String sValue)
  {
    addBodyRow ().addCell (aLabel).addCell (sValue);
  }

  @Deprecated
  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final String... aValues)
  {
    addBodyRow ().addCell (aLabel).addCell (aValues);
  }

  @Deprecated
  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode aValue)
  {
    addBodyRow ().addCell (aLabel).addCell (aValue);
  }

  @Deprecated
  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final IHCNode... aValues)
  {
    addBodyRow ().addCell (aLabel).addCell (aValues);
  }

  @Deprecated
  public void addItemRow (@Nullable final IFormLabel aLabel, @Nullable final Iterable <? extends IHCNode> aValues)
  {
    addBodyRow ().addCell (aLabel).addCell (aValues);
  }

  @Deprecated
  public void addItemRow (@Nonnull final String sLabel, @Nullable final String sValue)
  {
    addItemRow (HCFormLabel.create (sLabel), sValue);
  }

  @Deprecated
  public void addItemRow (@Nonnull final String sLabel, @Nullable final String... aValue)
  {
    addItemRow (HCFormLabel.create (sLabel), aValue);
  }

  @Deprecated
  public void addItemRow (@Nonnull final String sLabel, @Nullable final IHCNode aValue)
  {
    addItemRow (HCFormLabel.create (sLabel), aValue);
  }

  @Deprecated
  public void addItemRow (@Nonnull final String sLabel, @Nullable final IHCNode... aValues)
  {
    addItemRow (HCFormLabel.create (sLabel), aValues);
  }

  @Deprecated
  public void addItemRow (@Nonnull final String sLabel, @Nullable final Iterable <? extends IHCNode> aValues)
  {
    addItemRow (HCFormLabel.create (sLabel), aValues);
  }
}
