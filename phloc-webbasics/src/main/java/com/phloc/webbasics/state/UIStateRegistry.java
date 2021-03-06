/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.state;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.scopes.IScopeRenewalAware;
import com.phloc.webscopes.session.ISessionWebScopeDontPassivate;
import com.phloc.webscopes.singleton.SessionWebSingleton;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Per session registry for UI control state data.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class UIStateRegistry extends SessionWebSingleton implements IScopeRenewalAware, ISessionWebScopeDontPassivate
{
  /** ObjectType */
  public static final ObjectType OT_HCNODE = new ObjectType ("hcnode");
  private static final Logger s_aLogger = LoggerFactory.getLogger (UIStateRegistry.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <ObjectType, Map <String, IHasUIState>> m_aMap = new HashMap <ObjectType, Map <String, IHasUIState>> ();

  @UsedViaReflection
  @Deprecated
  public UIStateRegistry ()
  {}

  @Nonnull
  public static UIStateRegistry getCurrent ()
  {
    return getSessionSingleton (UIStateRegistry.class);
  }

  /**
   * Get the state with the passed ID for the current session. In case the state
   * is a {@link UIStateWrapper} instance, it is returned as is.
   * 
   * @param aOT
   *        The ObjectType to be resolved. May be <code>null</code>.
   * @param sStateID
   *        The state ID to be searched
   * @return the {@link IHasUIState} for the specified control ID, if already
   *         registered or <code>null</code>
   */
  @Nullable
  public IHasUIState getState (@Nullable final ObjectType aOT, @Nullable final String sStateID)
  {
    if (aOT == null)
      return null;
    if (StringHelper.hasNoText (sStateID))
      return null;

    this.m_aRWLock.readLock ().lock ();
    try
    {
      IHasUIState ret = null;

      // Get mapping for requested ObjectType
      final Map <String, IHasUIState> aMap = this.m_aMap.get (aOT);
      if (aMap != null)
      {
        // Lookup control ID for this object type
        ret = aMap.get (sStateID);
        if (ret == null)
        {
          // Try regular expressions (required for auto suggests in ebiz)
          for (final Map.Entry <String, IHasUIState> aEntry : aMap.entrySet ())
            if (RegExHelper.stringMatchesPattern (aEntry.getKey (), sStateID))
            {
              ret = aEntry.getValue ();
              break;
            }
        }
      }
      return ret;
    }
    finally
    {
      this.m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the state object in the specified type. If the saved state is a
   * {@link UIStateWrapper} instance, the contained value is returned!
   *
   * @param <T>
   *        The expected state type
   * @param aOT
   *        The ObjectType to be resolved. May be <code>null</code>.
   * @param sStateID
   *        The state ID to be resolved.
   * @return <code>null</code> if no such object was found.
   */
  @Nullable
  public <T> T getCastedState (@Nullable final ObjectType aOT, @Nullable final String sStateID)
  {
    final IHasUIState aObject = getState (aOT, sStateID);
    if (aObject == null)
      return null;

    // Special handling for UI state wrapper to retrieve the contained object
    if (aObject instanceof UIStateWrapper <?>)
      return ((UIStateWrapper <?>) aObject).<T> getCastedObject ();

    // Regular cast
    return GenericReflection.<IHasUIState, T> uncheckedCast (aObject);
  }

  /**
   * Registers a new control for the passed tree ID
   *
   * @param sStateID
   *        the ID of the state in register. May neither be <code>null</code>
   *        nor empty.
   * @param aNewState
   *        The state to set. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if the control was registered<br>
   *         {@link EChange#UNCHANGED} if an illegal argument was passed or a
   *         control has already been registered for that ID
   */
  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public EChange registerState (@Nonnull @Nonempty final String sStateID, @Nonnull final IHasUIState aNewState)
  {
    ValueEnforcer.notEmpty (sStateID, "StateID");
    ValueEnforcer.notNull (aNewState, "NewState");

    final ObjectType aOT = aNewState.getTypeID ();
    if (aOT == null)
      throw new IllegalStateException ("Object has no typeID: " + aNewState);

    this.m_aRWLock.writeLock ().lock ();
    try
    {
      Map <String, IHasUIState> aMap = this.m_aMap.get (aOT);
      if (aMap == null)
      {
        aMap = new HashMap <String, IHasUIState> ();
        this.m_aMap.put (aOT, aMap);
      }

      if (s_aLogger.isDebugEnabled () && aMap.containsKey (sStateID))
        s_aLogger.debug ("Overwriting " + aOT.getObjectTypeName () + " with ID " + sStateID + " with new object");

      aMap.put (sStateID, aNewState);
      return EChange.CHANGED;
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Register a state for the passed HC element, using the internal ID of the
   * element.
   *
   * @param aNewElement
   *        The element to be added to the registry. May not be
   *        <code>null</code>.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public EChange registerState (@Nonnull final IHCElement <?> aNewElement)
  {
    ValueEnforcer.notNull (aNewElement, "NewElement");

    return registerState (aNewElement.ensureID ().getID (), aNewElement);
  }

  @Nonnull
  public EChange registerState (@Nonnull @Nonempty final String sStateID, @Nonnull final IHCNode aNewNode)
  {
    ValueEnforcer.notNull (aNewNode, "NewNode");

    return registerState (sStateID, UIStateWrapper.create (OT_HCNODE, aNewNode));
  }

  @Nonnull
  public EChange removeState (@Nullable final ObjectType aObjectType, @Nonnull @Nonempty final String sStateID)
  {
    ValueEnforcer.notEmpty (sStateID, "StateID");

    this.m_aRWLock.writeLock ().lock ();
    try
    {
      final Map <String, IHasUIState> aMap = this.m_aMap.get (aObjectType);
      if (aMap == null)
        return EChange.UNCHANGED;
      return EChange.valueOf (aMap.remove (sStateID) != null);
    }
    finally
    {
      this.m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UIStateRegistry))
      return false;
    final UIStateRegistry rhs = (UIStateRegistry) o;
    return this.m_aMap.equals (rhs.m_aMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_aMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("map", this.m_aMap).toString ();
  }
}
