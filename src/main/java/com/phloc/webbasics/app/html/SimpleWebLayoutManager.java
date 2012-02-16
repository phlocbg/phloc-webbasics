package com.phloc.webbasics.app.html;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;

/**
 * This class handles the mapping of the area ID to a content provider.
 * 
 * @author philip
 */
public final class SimpleWebLayoutManager {
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  private static final Map <String, IAreaContentProvider> s_aContentProviders = new LinkedHashMap <String, IAreaContentProvider> ();

  private SimpleWebLayoutManager () {}

  public static void registerAreaContentProvider (@Nonnull final String sAreaID,
                                                  @Nonnull final IAreaContentProvider aContentProvider) {
    if (StringHelper.hasNoText (sAreaID))
      throw new IllegalArgumentException ("areaID");
    if (aContentProvider == null)
      throw new NullPointerException ("contentProvider");

    s_aRWLock.writeLock ().lock ();
    try {
      if (s_aContentProviders.containsKey (sAreaID))
        throw new IllegalArgumentException ("A content provider for the area ID '" +
                                            sAreaID +
                                            "' is already registered!");
      s_aContentProviders.put (sAreaID, aContentProvider);
    }
    finally {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <String> getAllAreaIDs () {
    return ContainerHelper.newList (s_aContentProviders.keySet ());
  }

  @Nullable
  public static IHCNode getContentOfArea (@Nonnull final String sAreaID, @Nonnull final Locale aDisplayLocale) {
    if (sAreaID == null)
      throw new NullPointerException ("areaID");

    s_aRWLock.readLock ().lock ();
    try {
      final IAreaContentProvider aContentProvider = s_aContentProviders.get (sAreaID);
      return aContentProvider == null ? null : aContentProvider.getContent (aDisplayLocale);
    }
    finally {
      s_aRWLock.readLock ().unlock ();
    }
  }
}
