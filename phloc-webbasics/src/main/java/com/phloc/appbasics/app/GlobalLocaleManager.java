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
package com.phloc.appbasics.app;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * This class manages the locales available in the application.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class GlobalLocaleManager extends GlobalSingleton implements ILocaleManager
{
  private final LocaleManager m_aProxy = new LocaleManager ();

  @Deprecated
  @UsedViaReflection
  public GlobalLocaleManager ()
  {}

  @Nonnull
  public static GlobalLocaleManager getInstance ()
  {
    return getGlobalSingleton (GlobalLocaleManager.class);
  }

  @Nonnull
  public EChange registerLocale (@Nonnull final Locale aLocale)
  {
    return m_aProxy.registerLocale (aLocale);
  }

  @Nonnull
  public EChange setDefaultLocale (@Nonnull final Locale aDefaultLocale)
  {
    return m_aProxy.setDefaultLocale (aDefaultLocale);
  }

  @Nullable
  public Locale getDefaultLocale ()
  {
    return m_aProxy.getDefaultLocale ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Locale> getAllAvailableLocales ()
  {
    return m_aProxy.getAllAvailableLocales ();
  }

  public boolean hasLocales ()
  {
    return m_aProxy.hasLocales ();
  }

  public boolean isSupportedLocale (@Nullable final Locale aLocale)
  {
    return m_aProxy.isSupportedLocale (aLocale);
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("proxy", m_aProxy).toString ();
  }
}
