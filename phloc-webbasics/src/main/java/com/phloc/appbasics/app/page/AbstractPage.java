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
package com.phloc.appbasics.app.page;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;

/**
 * Abstract base implementation for {@link IPage}.
 *
 * @author Philip Helger
 */
public abstract class AbstractPage implements IPage
{
  /** The name of the window where the help opens up */
  public static final String HELP_WINDOW_NAME = "simplehelpwindow";

  private final String m_sID;
  private IReadonlyMultiLingualText m_aName;
  private IReadonlyMultiLingualText m_aDescription;

  @Nonnull
  private static ReadonlyMultiLingualText _getAsMLT (@Nonnull final String sText)
  {
    return new ReadonlyMultiLingualText (ContainerHelper.newMap (CGlobal.LOCALE_INDEPENDENT, sText));
  }

  /**
   * Constructor
   *
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
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
    this (sID, _getAsMLT (sName));
  }

  /**
   * Constructor
   *
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param sName
   *        The constant (non-translatable) name of the page. May not be
   *        <code>null</code>.
   * @param sDescription
   *        The constant (non-translatable) description of the page. May be
   *        <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID,
                       @Nonnull final String sName,
                       @Nullable final String sDescription)
  {
    this (sID, _getAsMLT (sName), sDescription == null ? null : _getAsMLT (sDescription));
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
    this (sID, aName, null);
  }

  /**
   * Constructor
   *
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   * @param aDescription
   *        Optional description of the page. May be <code>null</code>.
   */
  public AbstractPage (@Nonnull @Nonempty final String sID,
                       @Nonnull final IReadonlyMultiLingualText aName,
                       @Nullable final IReadonlyMultiLingualText aDescription)
  {
    this (sID);
    setName (aName);
    setDescription (aDescription);
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
    m_aName = ValueEnforcer.notNull (aName, "Name");
  }

  /**
   * @return The complete name of the page in all available locales. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final IReadonlyMultiLingualText getName ()
  {
    return m_aName;
  }

  /*
   * Get the name of the page in the passed locale.
   */
  @Nullable
  public final String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aName.getTextWithLocaleFallback (aContentLocale);
  }

  /**
   * Set the description of the page.
   *
   * @param aDescription
   *        The multilingual description of the page. May be <code>null</code>.
   */
  public final void setDescription (@Nullable final IReadonlyMultiLingualText aDescription)
  {
    m_aDescription = aDescription;
  }

  /**
   * @return The complete description of the page in all available locales. May
   *         be <code>null</code>.
   */
  @Nullable
  public final IReadonlyMultiLingualText getDescription ()
  {
    return m_aDescription;
  }

  @Nullable
  public final String getDescription (@Nonnull final Locale aContentLocale)
  {
    return m_aDescription.getTextWithLocaleFallback (aContentLocale);
  }

  @OverrideOnDemand
  public boolean isHelpAvailable ()
  {
    return false;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("name", m_aName)
                                       .appendIfNotNull ("description", m_aDescription)
                                       .toString ();
  }
}
