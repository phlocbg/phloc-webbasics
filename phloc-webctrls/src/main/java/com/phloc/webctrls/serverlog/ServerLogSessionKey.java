package com.phloc.webctrls.serverlog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.random.VerySecureRandom;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webscopes.singleton.SessionWebSingleton;

public final class ServerLogSessionKey extends SessionWebSingleton
{
  private final String m_sKey;

  @Deprecated
  @UsedViaReflection
  public ServerLogSessionKey ()
  {
    final byte [] aKey = new byte [8];
    VerySecureRandom.getInstance ().nextBytes (aKey);
    m_sKey = StringHelper.getHexEncoded (aKey);
  }

  @Nonnull
  public static ServerLogSessionKey getInstance ()
  {
    return getSessionSingleton (ServerLogSessionKey.class);
  }

  @Nonnull
  public String getKey ()
  {
    return m_sKey;
  }

  @Nullable
  public static String getSessionKey ()
  {
    final ServerLogSessionKey aObj = (ServerLogSessionKey) getSingletonIfInstantiated (ServerLogSessionKey.class);
    return aObj == null ? null : aObj.m_sKey;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("key", m_sKey).toString ();
  }
}
