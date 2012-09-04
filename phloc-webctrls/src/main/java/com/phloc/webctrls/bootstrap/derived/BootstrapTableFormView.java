/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.bootstrap.derived;

import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.custom.label.IFormLabel;

public class BootstrapTableFormView extends BootstrapTable
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

  public BootstrapTableFormView (@Nullable final Iterable <HCCol> aCols)
  {
    super (aCols);
    _init ();
  }

  public void addItemRow (final IFormLabel aLabel, final String sValue)
  {
    addItemRow (aLabel, new HCTextNode (sValue));
  }

  public void addItemRow (final IFormLabel aLabel, final IHCNode aValue)
  {
    addBodyRow ().addCells (aLabel, aValue);
  }

  public void addItemRow (final IFormLabel aLabel, final Iterable <? extends IHCNode> aValues)
  {
    addBodyRow ().addCell (aLabel).addCell (aValues);
  }
}
