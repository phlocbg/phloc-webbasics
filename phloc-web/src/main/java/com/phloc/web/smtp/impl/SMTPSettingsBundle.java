package com.phloc.web.smtp.impl;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.LockedContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.ISMTPSettingsBundle;

@ThreadSafe
public class SMTPSettingsBundle implements ISMTPSettingsBundle
{
  private static final long serialVersionUID = 3404367482557580110L;
  private final ReentrantReadWriteLock m_aLock = new ReentrantReadWriteLock ();

  private final Set <ISMTPSettings> m_aSMTPs = ContainerHelper.newOrderedSet ();

  public EChange add (@Nonnull final ISMTPSettings aSMTP)
  {
    return EChange.valueOf (LockedContainerHelper.add (this.m_aSMTPs, aSMTP, this.m_aLock));
  }

  @Override
  @Nonnull
  public Set <ISMTPSettings> getAll ()
  {
    return LockedContainerHelper.getOrderedSet (this.m_aSMTPs, this.m_aLock);
  }

  @Override
  @Nullable
  public ISMTPSettings getFirst ()
  {
    return LockedContainerHelper.getFirst (this.m_aSMTPs, this.m_aLock);
  }

  @Override
  public boolean isValid ()
  {
    this.m_aLock.readLock ().lock ();
    try
    {
      if (ContainerHelper.isEmpty (this.m_aSMTPs))
      {
        return false;
      }
      for (final ISMTPSettings aSettings : this.m_aSMTPs)
      {
        if (!aSettings.areRequiredFieldsSet ())
        {
          return false;
        }
      }
      return true;
    }
    finally
    {
      this.m_aLock.readLock ().unlock ();
    }
  }
}
