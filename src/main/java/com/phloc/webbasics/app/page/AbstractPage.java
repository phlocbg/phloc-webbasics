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
package com.phloc.webbasics.app.page;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.EqualsUtils;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.impl.ConstantTextProvider;
import com.phloc.webbasics.app.scope.ScopeManager;

/**
 * Abstract base implementation for {@link IPage}.
 * 
 * @author philip
 */
public abstract class AbstractPage implements IPage
{
  private static final Map <String, IPage> ALL_IDS = new HashMap <String, IPage> ();
  private final String m_sID;
  private IHasDisplayText m_aName;

  public AbstractPage (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    final IPage aOldPage = ALL_IDS.put (sID, this);
    if (aOldPage != null)
      throw new IllegalArgumentException ("A page with the ID '" + sID + "' is already registered to " + aOldPage);
    m_sID = sID;
  }

  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    this (sID, new ConstantTextProvider (sName));
  }

  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    this (sID);
    setName (aName);
  }

  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  public final void setName (@Nonnull final IHasDisplayText aName)
  {
    if (aName == null)
      throw new NullPointerException ("name");
    m_aName = aName;
  }

  @Nullable
  public final String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getDisplayText (aContentLocale);
  }

  /**
   * Get the value of the request parameter with the given name.
   * 
   * @param sName
   *        Request parameter name
   * @return The value of the passed request parameter
   */
  @Nullable
  protected final String getAttr (final String sName)
  {
    return ScopeManager.getRequestScope ().getAttributeAsString (sName);
  }

  protected final int getIntAttr (final String sName, final int nDefault)
  {
    return ScopeManager.getRequestScope ().getAttributeAsInt (sName, nDefault);
  }

  protected final boolean hasAttr (final String sName, final String sValue)
  {
    return EqualsUtils.nullSafeEquals (sValue, getAttr (sName));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID).append ("name", m_aName).toString ();
  }

  @Nonnull
  @ReturnsImmutableObject
  public static Map <String, IPage> getAllPages ()
  {
    return ContainerHelper.makeUnmodifiable (ALL_IDS);
  }
}
