/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webbasics.app.layout;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.SimpleWebExecutionContext;

/**
 * This object is instantiated per page view and contains the current request
 * scope, the display locale, the selected menu item and a set of custom
 * attributes.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class LayoutExecutionContext extends SimpleWebExecutionContext
{
  private final String m_sSelectedMenuItemID;

  public LayoutExecutionContext (@Nonnull final SimpleWebExecutionContext aSWEC,
                                 @Nonnull final String sSelectedMenuItemID)
  {
    super (aSWEC.getRequestScope (), aSWEC.getDisplayLocale ());
    m_sSelectedMenuItemID = ValueEnforcer.notEmpty (sSelectedMenuItemID, "SelectedMenuItemID");
  }

  @Nonnull
  @Nonempty
  public String getSelectedMenuItemID ()
  {
    return m_sSelectedMenuItemID;
  }

  /**
   * Get the URL to the current page.
   * 
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   */
  @Nonnull
  public SimpleURL getSelfHref ()
  {
    return getLinkToMenuItem (m_sSelectedMenuItemID);
  }

  /**
   * Get the URL to the current page with the provided set of parameters.
   * 
   * @param aParams
   *        The optional request parameters to be used. May be <code>null</code>
   *        or empty.
   * @return The non-<code>null</code> URL to the current page (selected menu
   *         item) with the passed parameters.
   */
  @Nonnull
  public SimpleURL getSelfHref (@Nullable final Map <String, String> aParams)
  {
    return getLinkToMenuItem (m_sSelectedMenuItemID, aParams);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("selectedMenuItemID", m_sSelectedMenuItemID)
                            .toString ();
  }
}
