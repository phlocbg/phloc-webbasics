package com.phloc.web.networkinterface;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;

/**
 * Some utility methods for {@link NetworkInterface}
 * 
 * @author Philip Helger
 */
@Immutable
public final class NetworkInterfaceUtils
{
  private NetworkInterfaceUtils ()
  {}

  /**
   * Create a hierarchical tree of the network interfaces.
   * 
   * @return The created tree and never <code>null</code>.
   * @throws IllegalStateException
   *         In case an internal error occurred.
   */
  @Nonnull
  public static DefaultTreeWithGlobalUniqueID <String, NetworkInterface> createNetworkInterfaceTree ()
  {
    final DefaultTreeWithGlobalUniqueID <String, NetworkInterface> ret = new DefaultTreeWithGlobalUniqueID <String, NetworkInterface> ();

    // Build basic level - all IFs without a parent
    final List <NetworkInterface> aRemainingNIs = new ArrayList <NetworkInterface> ();
    try
    {
      for (final NetworkInterface aNI : ContainerHelper.getIterator (NetworkInterface.getNetworkInterfaces ()))
        if (aNI.getParent () == null)
          ret.getRootItem ().createChildItem (aNI.getName (), aNI);
        else
          aRemainingNIs.add (aNI);
    }
    catch (final Throwable t)
    {
      throw new IllegalStateException ("Failed to get all network interfaces", t);
    }

    int nNotFound = 0;
    while (!aRemainingNIs.isEmpty ())
    {
      final NetworkInterface aNI = aRemainingNIs.remove (0);
      final DefaultTreeItemWithID <String, NetworkInterface> aParentItem = ret.getItemWithID (aNI.getParent ()
                                                                                                 .getName ());
      if (aParentItem != null)
      {
        // We found the parent
        aParentItem.createChildItem (aNI.getName (), aNI);

        // Reset counter
        nNotFound = 0;
      }
      else
      {
        // Add again at the end
        aRemainingNIs.add (aNI);

        // Parent not found
        nNotFound++;

        // We tried too many times without success - we iterated the whole
        // remaining list and found no parent tree item
        if (nNotFound > aRemainingNIs.size ())
          throw new IllegalStateException ("Seems like we have a data structure inconsistency! Remaining are: " +
                                           aRemainingNIs);
      }
    }
    return ret;
  }
}
