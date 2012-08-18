package com.phloc.appbasics.mock;

import javax.annotation.Nonnull;

import com.phloc.appbasics.security.login.ICurrentUserIDProvider;
import com.phloc.commons.annotations.Nonempty;

/**
 * Mock implementation of {@link ICurrentUserIDProvider}.
 * 
 * @author philip
 */
public final class MockCurrentUserIDProvider implements ICurrentUserIDProvider
{
  private static final MockCurrentUserIDProvider s_aInstance = new MockCurrentUserIDProvider ();

  private MockCurrentUserIDProvider ()
  {}

  public static MockCurrentUserIDProvider getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  @Nonempty
  public String getCurrentUserID ()
  {
    return "unittest";
  }
}
