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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.impl.ConstantTextProvider;

/**
 * Abstract base implementation for {@link IBasePage}.
 * 
 * @author philip
 */
public abstract class AbstractBasePage implements IBasePage
{
  private final String m_sID;
  private IHasDisplayText m_aName;

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractBasePage (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param sName
   *        The constant (non-translatable) name of the page. May not be
   *        <code>null</code>.
   */
  public AbstractBasePage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    this (sID, new ConstantTextProvider (sName));
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   */
  public AbstractBasePage (@Nonnull @Nonempty final String sID, @Nonnull final IHasDisplayText aName)
  {
    this (sID);
    setName (aName);
  }

  /*
   * Get the unique page ID
   */
  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sID;
  }

  /**
   * Set the name of the page.
   * 
   * @param aName
   *        The multilingual name of the page. May not be <code>null</code>.
   */
  public final void setName (@Nonnull final IHasDisplayText aName)
  {
    if (aName == null)
      throw new NullPointerException ("name");
    m_aName = aName;
  }

  /*
   * Get the name of the page in the passed locale.
   */
  @Nullable
  public final String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getDisplayText (aContentLocale);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID).append ("name", m_aName).toString ();
  }
}
