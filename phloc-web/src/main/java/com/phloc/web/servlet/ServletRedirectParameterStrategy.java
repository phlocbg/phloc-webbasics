package com.phloc.web.servlet;

import java.util.Set;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.scopes.singleton.GlobalSingleton;

public class ServletRedirectParameterStrategy extends GlobalSingleton
{
  private final Set <String> m_aCopyOnRedirect = ContainerHelper.newSet ();

  @UsedViaReflection
  public ServletRedirectParameterStrategy () // NOPMD
  {
    super ();
  }

  /**
   * @return The global singleton instance of this class
   */
  @Nonnull
  public static ServletRedirectParameterStrategy getInstance ()
  {
    return getGlobalSingleton (ServletRedirectParameterStrategy.class);
  }

  public EChange registerCopyOnRedirect (@Nonnull @Nonempty final String sParameter)
  {
    return EChange.valueOf (this.m_aCopyOnRedirect.add (ValueEnforcer.notEmpty (sParameter, "parameter")));
  }

  public boolean isCopyOnRedirect (@Nonnull @Nonempty final String sParameter)
  {
    return this.m_aCopyOnRedirect.contains (ValueEnforcer.notEmpty (sParameter, "parameter"));
  }
}
