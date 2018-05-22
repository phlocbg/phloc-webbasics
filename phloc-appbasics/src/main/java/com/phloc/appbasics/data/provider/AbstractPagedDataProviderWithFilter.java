/**
 * Copyright (C) 2006-2018 phloc systems
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
package com.phloc.appbasics.data.provider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.data.select.IHasOrderAndLimit;
import com.phloc.appbasics.data.select.ISelectFilterable;
import com.phloc.appbasics.data.select.SelectConstraints;
import com.phloc.commons.annotations.OverrideOnDemand;

public abstract class AbstractPagedDataProviderWithFilter <T> implements IPagedDataProviderWithFilter <T>
{
  private static final long serialVersionUID = -4759093584402814052L;
  @Nonnull
  private final SelectConstraints m_aSC = new SelectConstraints ();

  protected AbstractPagedDataProviderWithFilter ()
  {}

  @Override
  @Nullable
  public final ISelectFilterable getFilter ()
  {
    return this.m_aSC.getFilter ();
  }

  @Override
  public final void setFilter (@Nullable final ISelectFilterable aFilter)
  {
    if (isAllowSetFilter (aFilter))
    {
      this.m_aSC.setFilter (aFilter);
    }
  }

  @OverrideOnDemand
  protected boolean isAllowSetFilter (@Nullable final ISelectFilterable aFilter)
  {
    return true;
  }

  @Nonnull
  protected final SelectConstraints getSelectConstraints (final IHasOrderAndLimit aTable)
  {
    // order
    this.m_aSC.setOrderBy (aTable.getOrderBy ());

    // limits must be evaluated every time, since the filter may influence it!
    this.m_aSC.setLimit (aTable.getLimit ());
    return this.m_aSC;
  }
}
