package com.phloc.webctrls.serverlog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.random.VerySecureRandom;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webscopes.singleton.SessionWebSingleton;

/**
 * Session singleton to create unique keys for server logging. Each generated
 * String has a length of 16.
 * 
 * @author philip
 */
public final class ServerLogSessionKey extends SessionWebSingleton
{
  private final String m_sGeneratedKey;

  @Deprecated
  @UsedViaReflection
  public ServerLogSessionKey ()
  {
    final byte [] aKey = new byte [8];
    VerySecureRandom.getInstance ().nextBytes (aKey);
    m_sGeneratedKey = StringHelper.getHexEncoded (aKey);
  }

  @Nonnull
  public static ServerLogSessionKey getInstance ()
  {
    return getSessionSingleton (ServerLogSessionKey.class);
  }

  /**
   * @return The generated session key and never <code>null</code>.
   */
  @Nonnull
  public String getGeneratedKey ()
  {
    return m_sGeneratedKey;
  }

  /**
   * @return The generated session key or <code>null</code> if no
   *         {@link ServerLogSessionKey} was created yet.
   */
  @Nullable
  public static String getGeneratedSessionKey ()
  {
    final ServerLogSessionKey aObj = getSingletonIfInstantiated (ServerLogSessionKey.class);
    return aObj == null ? null : aObj.m_sGeneratedKey;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("generatedKey", m_sGeneratedKey).toString ();
  }
}
