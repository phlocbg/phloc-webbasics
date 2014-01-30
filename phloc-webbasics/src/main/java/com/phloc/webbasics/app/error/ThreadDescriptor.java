/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webbasics.app.error;

import java.lang.Thread.State;
import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.commons.microdom.IHasMicroNodeRepresentation;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;

final class ThreadDescriptor implements IHasStringRepresentation, IHasMicroNodeRepresentation
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ThreadDescriptor.class);
  private static final ThreadMXBean THREAD_MX = ManagementFactory.getThreadMXBean ();
  private final long m_nID;
  private final String m_sName;
  private final State m_eState;
  private final int m_nPriority;
  private final String m_sThreadGroup;
  private final String m_sStackTrace;
  private final ThreadInfo m_aThreadInfo;

  public ThreadDescriptor (@Nonnull final Thread aThread, @Nullable final String sStackTrace)
  {
    if (aThread == null)
      throw new NullPointerException ("thread");

    m_nID = aThread.getId ();
    m_sName = aThread.getName ();
    m_eState = aThread.getState ();
    m_nPriority = aThread.getPriority ();
    final ThreadGroup aTG = aThread.getThreadGroup ();
    m_sThreadGroup = aTG != null ? aTG.getName () : "none - DIED!";
    m_sStackTrace = sStackTrace;
    ThreadInfo aThreadInfo = null;
    try
    {
      // This takes forever. Disabled as a performance improvement
      if (false)
        aThreadInfo = THREAD_MX.getThreadInfo (new long [] { m_nID }, true, true)[0];
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Failed to get ThreadInfo for thread " + m_nID + ": " + t.toString ());
    }
    m_aThreadInfo = aThreadInfo;
  }

  public long getThreadID ()
  {
    return m_nID;
  }

  public State getThreadState ()
  {
    return m_eState;
  }

  @Nonnull
  @Nonempty
  public String getDescriptor ()
  {
    return "Thread[" + m_nID + "][" + m_sName + "][" + m_eState + "][" + m_nPriority + "][" + m_sThreadGroup + "]";
  }

  @Nullable
  public String getStackTrace ()
  {
    return m_sStackTrace;
  }

  @Nonnull
  @Nonempty
  public String getStackTraceNotNull ()
  {
    return StringHelper.hasText (m_sStackTrace) ? m_sStackTrace : "No stack trace available\n";
  }

  @Nonnull
  public String getLockInfo ()
  {
    final StringBuilder aSB = new StringBuilder ();
    if (m_aThreadInfo != null)
      try
      {
        final MonitorInfo [] aMonitorInfos = m_aThreadInfo.getLockedMonitors ();
        if (ArrayHelper.isNotEmpty (aMonitorInfos))
        {
          aSB.append ("Information on " + aMonitorInfos.length + " monitors:\n");
          for (final MonitorInfo aMonitorInfo : aMonitorInfos)
          {
            aSB.append ("  monitor: " +
                        aMonitorInfo.getClassName () +
                        "@" +
                        Integer.toHexString (aMonitorInfo.getIdentityHashCode ()) +
                        " at " +
                        aMonitorInfo.getLockedStackFrame () +
                        " [" +
                        (aMonitorInfo.getLockedStackDepth ()) +
                        "]\n");
          }
        }
        final LockInfo [] aSynchronizers = m_aThreadInfo.getLockedSynchronizers ();
        if (ArrayHelper.isNotEmpty (aSynchronizers))
        {
          aSB.append ("Information on " + aSynchronizers.length + " synchronizers:\n");
          for (final LockInfo aSynchronizer : aSynchronizers)
          {
            aSB.append ("  lock:" +
                        aSynchronizer.getClassName () +
                        "@" +
                        Integer.toHexString (aSynchronizer.getIdentityHashCode ()) +
                        "\n");
          }
        }
      }
      catch (final Throwable t)
      {
        aSB.append ("Error retrieving infos: " + t.toString ());
      }
    return aSB.toString ();
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    final String sDescriptor = getDescriptor ();
    final String sStackTrace = getStackTraceNotNull ();
    final String sLockInfo = getLockInfo ();
    return sDescriptor + "\n" + sStackTrace + sLockInfo;
  }

  @Nonnull
  public IMicroElement getAsMicroNode ()
  {
    final IMicroElement eRet = new MicroElement ("thread");
    eRet.setAttribute ("id", m_nID);
    eRet.setAttribute ("name", m_sName);
    if (m_eState != null)
      eRet.setAttribute ("state", m_eState.toString ());
    eRet.setAttribute ("priority", m_nPriority);
    eRet.setAttribute ("threadgroup", m_sThreadGroup);
    eRet.appendElement ("stacktrace").appendText (getStackTraceNotNull ());
    if (m_aThreadInfo != null)
    {
      final IMicroElement eThreadInfo = eRet.appendElement ("threadinfo");
      try
      {
        final MonitorInfo [] aMonitorInfos = m_aThreadInfo.getLockedMonitors ();
        if (ArrayHelper.isNotEmpty (aMonitorInfos))
        {
          final IMicroElement eMonitorInfos = eThreadInfo.appendElement ("monitorinfos");
          eMonitorInfos.setAttribute ("count", aMonitorInfos.length);
          for (final MonitorInfo aMonitorInfo : aMonitorInfos)
          {
            final IMicroElement eMonitor = eMonitorInfos.appendElement ("monitor");
            eMonitor.setAttribute ("classname", aMonitorInfo.getClassName ());
            eMonitor.setAttribute ("identity", Integer.toHexString (aMonitorInfo.getIdentityHashCode ()));
            if (aMonitorInfo.getLockedStackFrame () != null)
              eMonitor.setAttribute ("stackframe", aMonitorInfo.getLockedStackFrame ().toString ());
            if (aMonitorInfo.getLockedStackDepth () >= 0)
              eMonitor.setAttribute ("stackdepth", aMonitorInfo.getLockedStackDepth ());
          }
        }

        final LockInfo [] aSynchronizers = m_aThreadInfo.getLockedSynchronizers ();
        if (ArrayHelper.isNotEmpty (aSynchronizers))
        {
          final IMicroElement eSynchronizers = eThreadInfo.appendElement ("synchronizers");
          eSynchronizers.setAttribute ("count", aSynchronizers.length);
          for (final LockInfo aSynchronizer : aSynchronizers)
          {
            final IMicroElement eSynchronizer = eSynchronizers.appendElement ("synchronizer");
            eSynchronizer.setAttribute ("classname", aSynchronizer.getClassName ());
            eSynchronizer.setAttribute ("identity", Integer.toHexString (aSynchronizer.getIdentityHashCode ()));
          }
        }
      }
      catch (final Throwable t)
      {
        eThreadInfo.setAttribute ("error", t.getMessage ()).appendText (StackTraceHelper.getStackAsString (t));
      }
    }
    return eRet;
  }
}
