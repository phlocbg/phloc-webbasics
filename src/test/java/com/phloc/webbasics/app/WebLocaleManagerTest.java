package com.phloc.webbasics.app;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import com.phloc.commons.locale.LocaleCache;

/**
 * Test class for class {@link WebLocaleManager}.
 * 
 * @author philip
 */
public final class WebLocaleManagerTest
{
  @BeforeClass
  public static void init ()
  {
    WebLocaleManager.registerLocale (LocaleCache.get ("de", "AT"));
  }

  @Test
  public void testIsSupportedLocale ()
  {
    assertTrue (WebLocaleManager.isSupportedLocale (LocaleCache.get ("de", "AT")));
    assertFalse (WebLocaleManager.isSupportedLocale (LocaleCache.get ("de")));
    assertFalse (WebLocaleManager.isSupportedLocale (null));
    assertFalse (WebLocaleManager.isSupportedLocale (Locale.CANADA));
  }
}
