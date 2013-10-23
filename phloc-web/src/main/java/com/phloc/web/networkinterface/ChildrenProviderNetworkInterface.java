package com.phloc.web.networkinterface;

import java.net.NetworkInterface;
import java.util.Collection;
import java.util.Enumeration;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.parent.IChildrenProvider;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of {@link IChildrenProvider} for
 * {@link NetworkInterface}.
 * 
 * @author Philip Helger
 */
@Immutable
public class ChildrenProviderNetworkInterface implements IChildrenProvider <NetworkInterface>
{
  public boolean hasChildren (@Nonnull final NetworkInterface aCurrent)
  {
    return aCurrent.getSubInterfaces ().hasMoreElements ();
  }

  @Nonnegative
  public int getChildCount (@Nonnull final NetworkInterface aCurrent)
  {
    return ContainerHelper.getSize (aCurrent.getSubInterfaces ());
  }

  @Nullable
  public Collection <NetworkInterface> getChildren (@Nonnull final NetworkInterface aCurrent)
  {
    final Enumeration <NetworkInterface> aSubIFs = aCurrent.getSubInterfaces ();
    return aSubIFs.hasMoreElements () ? ContainerHelper.newList (aSubIFs) : null;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
