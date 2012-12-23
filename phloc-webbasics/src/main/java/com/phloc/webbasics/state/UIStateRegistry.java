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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.scopes.IScopeRenewalAware;
import com.phloc.scopes.web.singleton.SessionWebSingleton;

/**
 * Global registry for UI control state data.
 * 
 * @author philip
 */
@ThreadSafe
public final class UIStateRegistry extends SessionWebSingleton implements IScopeRenewalAware
{
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
   * Get the state with the passed ID for the current session.
   * 
   * @param sStateID
   *        The state ID to be searched
   * @return the {@link IHasUIState} for the specified control ID, if already
   *         registered or <code>null</code>
   */
  @Nullable
  public IHasUIState getState (@Nonnull final ObjectType aOT, @Nullable final String sStateID)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      IHasUIState ret = null;

      // Get mapping for requested ObjectType
      final Map <String, IHasUIState> aMap = m_aMap.get (aOT);
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
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public <T extends IHasUIState> T getCastedState (@Nonnull final ObjectType aOT, @Nullable final String sStateID)
  {
    return GenericReflection.<IHasUIState, T> uncheckedCast (getState (aOT, sStateID));
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
  public EChange registerState (@Nonnull @Nonempty final String sStateID, @Nonnull final IHasUIState aNewState)
  {
    if (StringHelper.hasNoText (sStateID))
      throw new IllegalArgumentException ("stateID may not be empty");
    if (aNewState == null)
      throw new NullPointerException ("newState");

    m_aRWLock.writeLock ().lock ();
    try
    {
      final ObjectType aOT = aNewState.getTypeID ();
      Map <String, IHasUIState> aMap = m_aMap.get (aOT);
      if (aMap == null)
      {
        aMap = new HashMap <String, IHasUIState> ();
        m_aMap.put (aOT, aMap);
      }

      if (s_aLogger.isDebugEnabled () && aMap.containsKey (sStateID))
        s_aLogger.debug ("Overwriting " + aOT.getObjectTypeName () + " with ID " + sStateID + " with new object");

      aMap.put (sStateID, aNewState);
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public EChange removeState (@Nullable final ObjectType aObjectType, @Nonnull @Nonempty final String sStateID)
  {
    if (StringHelper.hasNoText (sStateID))
      throw new IllegalArgumentException ("stateID may not be empty");

    m_aRWLock.writeLock ().lock ();
    try
    {
      final Map <String, IHasUIState> aMap = m_aMap.get (aObjectType);
      if (aMap == null)
        return EChange.UNCHANGED;
      return EChange.valueOf (aMap.remove (sStateID) != null);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
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
    return m_aMap.equals (rhs.m_aMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
