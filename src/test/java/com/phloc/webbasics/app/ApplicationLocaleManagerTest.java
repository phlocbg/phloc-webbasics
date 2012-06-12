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
package com.phloc.webbasics.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import com.phloc.commons.locale.LocaleCache;

/**
 * Test class for class {@link ApplicationLocaleManager}.
 * 
 * @author philip
 */
public final class ApplicationLocaleManagerTest
{
  @BeforeClass
  public static void init ()
  {
    ApplicationLocaleManager.registerLocale (LocaleCache.getLocale ("de", "AT"));
  }

  @Test
  public void testIsSupportedLocale ()
  {
    assertEquals (LocaleCache.getLocale ("de", "AT"), ApplicationLocaleManager.getDefaultLocale ());
    assertTrue (ApplicationLocaleManager.isSupportedLocale (LocaleCache.getLocale ("de", "AT")));
    assertFalse (ApplicationLocaleManager.isSupportedLocale (LocaleCache.getLocale ("de")));
    assertFalse (ApplicationLocaleManager.isSupportedLocale (null));
    assertFalse (ApplicationLocaleManager.isSupportedLocale (Locale.CANADA));
  }
}
