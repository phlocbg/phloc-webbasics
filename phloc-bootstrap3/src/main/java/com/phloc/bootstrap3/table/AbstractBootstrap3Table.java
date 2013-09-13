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
package com.phloc.bootstrap3.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.hc.html.AbstractHCTable;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;

public abstract class AbstractBootstrap3Table <THISTYPE extends AbstractHCTable <THISTYPE>> extends AbstractHCTable <THISTYPE>
{
  private void _init ()
  {
    addClass (CBootstrap3CSS.TABLE);
    setCondensed (true);
  }

  public AbstractBootstrap3Table ()
  {
    super ();
    _init ();
  }

  public AbstractBootstrap3Table (@Nullable final HCCol aCol)
  {
    super (aCol);
    _init ();
  }

  public AbstractBootstrap3Table (@Nullable final HCCol... aCols)
  {
    super (aCols);
    _init ();
  }

  public AbstractBootstrap3Table (@Nullable final Iterable <? extends HCCol> aCols)
  {
    super (aCols);
    _init ();
  }

  @Nonnull
  public THISTYPE setStriped (final boolean bStriped)
  {
    if (bStriped)
      addClass (CBootstrap3CSS.TABLE_STRIPED);
    else
      removeClass (CBootstrap3CSS.TABLE_STRIPED);
    return thisAsT ();
  }

  @Nonnull
  public THISTYPE setBordered (final boolean bBordered)
  {
    if (bBordered)
      addClass (CBootstrap3CSS.TABLE_BORDERED);
    else
      removeClass (CBootstrap3CSS.TABLE_BORDERED);
    return thisAsT ();
  }

  @Nonnull
  public THISTYPE setHover (final boolean bHover)
  {
    if (bHover)
      addClass (CBootstrap3CSS.TABLE_HOVER);
    else
      removeClass (CBootstrap3CSS.TABLE_HOVER);
    return thisAsT ();
  }

  @Nonnull
  public THISTYPE setCondensed (final boolean bCondensed)
  {
    if (bCondensed)
      addClass (CBootstrap3CSS.TABLE_CONDENSED);
    else
      removeClass (CBootstrap3CSS.TABLE_CONDENSED);
    return thisAsT ();
  }

  @Nonnull
  public HCDiv getAsResponsiveTable ()
  {
    return new HCDiv ().addClass (CBootstrap3CSS.TABLE_RESPONSIVE).addChild (this);
  }
}
