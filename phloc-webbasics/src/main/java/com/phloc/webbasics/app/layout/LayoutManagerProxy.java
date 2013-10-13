/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webbasics.app.layout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.IHCNode;

/**
 * This class handles the mapping of the area ID to a content provider.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class LayoutManagerProxy implements ILayoutManager
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private final Map <String, ILayoutAreaContentProvider> m_aContentProviders = new LinkedHashMap <String, ILayoutAreaContentProvider> ();

  public LayoutManagerProxy ()
  {}

  public void registerAreaContentProvider (@Nonnull final String sAreaID,
                                           @Nonnull final ILayoutAreaContentProvider aContentProvider)
  {
    if (StringHelper.hasNoText (sAreaID))
      throw new IllegalArgumentException ("areaID");
    if (aContentProvider == null)
      throw new NullPointerException ("contentProvider");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aContentProviders.containsKey (sAreaID))
        throw new IllegalArgumentException ("A content provider for the area ID '" +
                                            sAreaID +
                                            "' is already registered!");
      m_aContentProviders.put (sAreaID, aContentProvider);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllAreaIDs ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aContentProviders.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public IHCNode getContentOfArea (@Nonnull final LayoutExecutionContext aLEC, @Nonnull @Nonempty final String sAreaID)
  {
    if (sAreaID == null)
      throw new NullPointerException ("areaID");

    m_aRWLock.readLock ().lock ();
    try
    {
      final ILayoutAreaContentProvider aContentProvider = m_aContentProviders.get (sAreaID);
      return aContentProvider == null ? null : aContentProvider.getContent (aLEC);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("contentProviders", m_aContentProviders).toString ();
  }
}
