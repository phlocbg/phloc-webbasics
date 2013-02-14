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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.impl.HCTextNode;

public class BootstrapTable extends AbstractHCTable <BootstrapTable>
{
  private void _init ()
  {
    addClass (CBootstrapCSS.TABLE);
    setCondensed (true);
  }

  public BootstrapTable ()
  {
    super ();
    _init ();
  }

  public BootstrapTable (@Nullable final HCCol aCol)
  {
    super (aCol);
    _init ();
  }

  public BootstrapTable (@Nullable final HCCol... aCols)
  {
    super (aCols);
    _init ();
  }

  public BootstrapTable (@Nullable final Iterable <HCCol> aCols)
  {
    super (aCols);
    _init ();
  }

  @Nonnull
  public final BootstrapTable setStriped (final boolean bStriped)
  {
    if (bStriped)
      addClass (CBootstrapCSS.TABLE_STRIPED);
    else
      removeClass (CBootstrapCSS.TABLE_STRIPED);
    return this;
  }

  @Nonnull
  public final BootstrapTable setBordered (final boolean bBordered)
  {
    if (bBordered)
      addClass (CBootstrapCSS.TABLE_BORDERED);
    else
      removeClass (CBootstrapCSS.TABLE_BORDERED);
    return this;
  }

  @Nonnull
  public final BootstrapTable setHover (final boolean bHover)
  {
    if (bHover)
      addClass (CBootstrapCSS.TABLE_HOVER);
    else
      removeClass (CBootstrapCSS.TABLE_HOVER);
    return this;
  }

  @Nonnull
  public final BootstrapTable setCondensed (final boolean bCondensed)
  {
    if (bCondensed)
      addClass (CBootstrapCSS.TABLE_CONDENSED);
    else
      removeClass (CBootstrapCSS.TABLE_CONDENSED);
    return this;
  }

  @Nonnull
  public BootstrapTable setSpanningHeaderContent (@Nullable final String sText)
  {
    return setSpanningHeaderContent (new HCTextNode (sText));
  }

  @Nonnull
  public BootstrapTable setSpanningHeaderContent (@Nullable final IHCNode aNode)
  {
    removeAllHeaderRows ().addHeaderRow ().addAndReturnCell (aNode).setColspan (getColumnCount ());
    return this;
  }

  @Nonnull
  public BootstrapTable addSpanningBodyContent (@Nullable final String sText)
  {
    return addSpanningBodyContent (new HCTextNode (sText));
  }

  @Nonnull
  public BootstrapTable addSpanningBodyContent (@Nullable final IHCNode aNode)
  {
    addBodyRow ().addAndReturnCell (aNode).setColspan (getColumnCount ());
    return this;
  }

  @Nonnull
  public BootstrapTable setSpanningFooterContent (@Nullable final String sText)
  {
    return setSpanningFooterContent (new HCTextNode (sText));
  }

  @Nonnull
  public BootstrapTable setSpanningFooterContent (@Nullable final IHCNode aNode)
  {
    removeAllFooterRows ().addFooterRow ().addAndReturnCell (aNode).setColspan (getColumnCount ());
    return this;
  }
}
