package com.phloc.web.networkinterface;

import java.net.NetworkInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.parent.IParentProvider;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of {@link IParentProvider} for
 * {@link NetworkInterface}.
 * 
 * @author Philip Helger
 */
@Immutable
public class ParentProviderNetworkInterface implements IParentProvider <NetworkInterface>
{
  @Nullable
  public NetworkInterface getParent (@Nonnull final NetworkInterface aCurrent)
  {
    return aCurrent.getParent ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
