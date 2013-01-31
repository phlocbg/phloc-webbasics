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
package com.phloc.appbasics.app.page;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;

/**
 * Abstract base implementation for {@link IPage}.
 * 
 * @author philip
 */
public abstract class AbstractPage implements IPage
{
  /** The name of the window where the help opens up */
  public static final String HELP_WINDOW_NAME = "simplehelpwindow";

  private final String m_sID;
  private IReadonlyMultiLingualText m_aName;

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID)
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
  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    this (sID, new ReadonlyMultiLingualText (ContainerHelper.newMap (CGlobal.LOCALE_INDEPENDENT, sName)));
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
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
  public final void setName (@Nonnull final IReadonlyMultiLingualText aName)
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
    return m_aName.getTextWithLocaleFallback (aContentLocale);
  }

  @OverrideOnDemand
  public boolean isHelpAvailable ()
  {
    return false;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID).append ("name", m_aName).toString ();
  }
}
