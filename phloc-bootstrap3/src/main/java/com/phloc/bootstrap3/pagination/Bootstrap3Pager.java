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

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;

public class Bootstrap3Pager extends HCUL
{
  public Bootstrap3Pager ()
  {
    addClass (CBootstrap3CSS.PAGER);
  }

  @Nonnull
  public Bootstrap3Pager addItemPrev (@Nullable final IHCNode aContent, final boolean bDisabled)
  {
    final HCLI aItem = addAndReturnItem (aContent).addClass (CBootstrap3CSS.PREVIOUS);
    if (bDisabled)
      aItem.addClass (CBootstrap3CSS.DISABLED);
    return this;
  }

  @Nonnull
  public Bootstrap3Pager addItemNext (@Nullable final IHCNode aContent, final boolean bDisabled)
  {
    final HCLI aItem = addAndReturnItem (aContent).addClass (CBootstrap3CSS.NEXT);
    if (bDisabled)
      aItem.addClass (CBootstrap3CSS.DISABLED);
    return this;
  }
}
