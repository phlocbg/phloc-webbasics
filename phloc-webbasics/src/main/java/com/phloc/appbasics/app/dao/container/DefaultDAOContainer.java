/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.app.dao.container;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.dao.IDAO;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;

/**
 * The default implementation of {@link IDAOContainer} using a list of DAOs
 * provider in the constructor.
 * 
 * @author Philip Helger
 */
public class DefaultDAOContainer extends AbstractDAOContainer
{
  @Nonnull
  private final List <IDAO> m_aDAOs;

  public DefaultDAOContainer (@Nonnull @Nonempty final IDAO... aDAOs)
  {
    ValueEnforcer.notEmptyNoNullValue (aDAOs, "DAOs");
    m_aDAOs = ContainerHelper.newUnmodifiableList (aDAOs);
  }

  public DefaultDAOContainer (@Nonnull @Nonempty final Iterable <? extends IDAO> aDAOs)
  {
    ValueEnforcer.notEmptyNoNullValue (aDAOs, "DAOs");
    m_aDAOs = ContainerHelper.newUnmodifiableList (aDAOs);
  }

  @Nonnull
  @ReturnsImmutableObject
  public Collection <IDAO> getContainedDAOs ()
  {
    // ESCA-JAVA0259:
    return m_aDAOs;
  }
}
