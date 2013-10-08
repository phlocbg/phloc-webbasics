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
package com.phloc.bootstrap3.pagination;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.html.HCUL;

public class BootstrapPagination extends HCUL
{
  public BootstrapPagination ()
  {
    this (EBootstrapPaginationType.DEFAULT);
  }

  public BootstrapPagination (@Nonnull final EBootstrapPaginationType eType)
  {
    addClasses (CBootstrapCSS.PAGINATION, eType);
  }

  @Nonnull
  public BootstrapPagination addItemActive (@Nullable final String sContent)
  {
    if (StringHelper.hasText (sContent))
      addItemActive (HCSpan.create (sContent));
    return this;
  }

  @Nonnull
  public BootstrapPagination addItemActive (@Nullable final IHCNode aContent)
  {
    addAndReturnItem (aContent).addClass (CBootstrapCSS.ACTIVE);
    return this;
  }

  @Nonnull
  public BootstrapPagination addItemDisabled (@Nullable final String sContent)
  {
    if (StringHelper.hasText (sContent))
      addItemDisabled (HCSpan.create (sContent));
    return this;
  }

  @Nonnull
  public BootstrapPagination addItemDisabled (@Nullable final IHCNode aContent)
  {
    addAndReturnItem (aContent).addClass (CBootstrapCSS.DISABLED);
    return this;
  }
}
