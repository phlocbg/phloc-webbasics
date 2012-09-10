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
package com.phloc.appbasics.app.menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.idfactory.GlobalIDFactory;

/**
 * Default implementation of the {@link IMenuSeparator} interface.
 * 
 * @author philip
 */
@NotThreadSafe
public final class MenuSeparator extends AbstractMenuObject implements IMenuSeparator
{
  public MenuSeparator ()
  {
    this (GlobalIDFactory.getNewStringID ());
  }

  public MenuSeparator (@Nonnull @Nonempty final String sID)
  {
    super (sID);
  }

  @Nonnull
  public MenuSeparator setDisplayFilter (@Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    m_aDisplayFilter = aDisplayFilter;
    return this;
  }
}
