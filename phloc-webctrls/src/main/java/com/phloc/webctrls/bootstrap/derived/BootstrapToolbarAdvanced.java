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
package com.phloc.webctrls.bootstrap.derived;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.url.SimpleURL;
import com.phloc.webbasics.app.LinkUtils;

/**
 * Bootstrap block help.
 * 
 * @author philip
 */
public class BootstrapToolbarAdvanced extends BootstrapToolbar
{
  private final SimpleURL m_aSelfHref;

  public BootstrapToolbarAdvanced ()
  {
    this (LinkUtils.getSelfHref ());
  }

  public BootstrapToolbarAdvanced (@Nonnull final SimpleURL aSelfHref)
  {
    super ();
    if (aSelfHref == null)
      throw new NullPointerException ("selfHref");
    m_aSelfHref = aSelfHref;
  }

  @Nonnull
  public BootstrapToolbarAdvanced addButtonBack (@Nonnull final Locale aDisplayLocale)
  {
    addButtonBack (aDisplayLocale, m_aSelfHref);
    return this;
  }

  @Nonnull
  public BootstrapToolbarAdvanced addButtonCancel (@Nonnull final Locale aDisplayLocale)
  {
    addButtonCancel (aDisplayLocale, m_aSelfHref);
    return this;
  }

  @Nonnull
  public BootstrapToolbarAdvanced addButtonNo (@Nonnull final Locale aDisplayLocale)
  {
    addButtonNo (aDisplayLocale, m_aSelfHref);
    return this;
  }
}
