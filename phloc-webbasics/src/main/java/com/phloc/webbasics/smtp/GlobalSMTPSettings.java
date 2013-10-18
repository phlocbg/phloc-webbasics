package com.phloc.webbasics.smtp;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.scopes.singleton.GlobalSingleton;
import com.phloc.web.smtp.impl.SMTPSettings;

/**
 * This class contains all globally available SMTP settings. The settings are
 * identified by name.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class GlobalSMTPSettings extends GlobalSingleton implements IHasSize
{
  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  private final Map <String, SMTPSettings> m_aMap = new HashMap <String, SMTPSettings> ();

  @Deprecated
  @UsedViaReflection
  public GlobalSMTPSettings ()
  {}

  @Nonnull
  public static GlobalSMTPSettings getInstance ()
  {
    return getGlobalSingleton (GlobalSMTPSettings.class);
  }

  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean isEmpty ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.isEmpty ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, SMTPSettings> getAllSettings ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (m_aMap);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean containsSettingsWithName (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.containsKey (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public SMTPSettings getSettingsOfName (@Nullable final String sName)
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sName);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  @Nullable
  public EChange addSettings (@Nonnull @Nonempty final String sName, @Nonnull final SMTPSettings aSettings)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    m_aRWLock.writeLock ().lock ();
    try
    {
      if (m_aMap.containsKey (sName))
        return EChange.UNCHANGED;
      m_aMap.put (sName, aSettings);
      return EChange.CHANGED;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public EChange setSettings (@Nonnull @Nonempty final String sName, @Nonnull final SMTPSettings aSettings)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aSettings == null)
      throw new NullPointerException ("settings");

    m_aRWLock.writeLock ().lock ();
    try
    {
      final SMTPSettings aOldSettings = m_aMap.put (sName, aSettings);
      return EChange.valueOf (aOldSettings == null || !aOldSettings.equals (aSettings));
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
