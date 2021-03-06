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
package com.phloc.webbasics.app.error;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.concurrent.ComparatorThreadID;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.commons.microdom.IHasMicroNodeRepresentation;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.timing.StopWatch;

/**
 * This class contains a list of {@link ThreadDescriptor} objects.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class ThreadDescriptorList implements IHasStringRepresentation, IHasMicroNodeRepresentation
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (ThreadDescriptorList.class);

  private final List <ThreadDescriptor> m_aList = new ArrayList <ThreadDescriptor> ();
  private String m_sError;

  public ThreadDescriptorList ()
  {}

  public void addDescriptor (@Nonnull final ThreadDescriptor aDescriptor)
  {
    if (aDescriptor == null)
      throw new NullPointerException ("descriptor");
    m_aList.add (aDescriptor);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <ThreadDescriptor> getAllDescriptors ()
  {
    return ContainerHelper.newList (m_aList);
  }

  public void setError (@Nullable final String sError)
  {
    m_sError = sError;
  }

  @Nullable
  public String getError ()
  {
    return m_sError;
  }

  @Nonnull
  @ReturnsMutableCopy
  private Map <State, Set <Long>> _getStateMap ()
  {
    // Group threads by state
    final Map <State, Set <Long>> aStateMap = new EnumMap <State, Set <Long>> (State.class);
    for (final ThreadDescriptor aDescriptor : m_aList)
    {
      final State eState = aDescriptor.getThreadState ();
      Set <Long> aThreadIDs = aStateMap.get (eState);
      if (aThreadIDs == null)
      {
        aThreadIDs = new TreeSet <Long> ();
        aStateMap.put (eState, aThreadIDs);
      }
      aThreadIDs.add (Long.valueOf (aDescriptor.getThreadID ()));
    }
    return aStateMap;
  }

  @Nonnull
  @Nonempty
  public String getAsString ()
  {
    final StringBuilder aSB = new StringBuilder ();

    // Error always shown first!
    if (StringHelper.hasText (m_sError))
      aSB.append ("ERROR retrieving all thread stack traces: ").append (m_sError).append ("\n\n");

    // Total thread count
    aSB.append ("Total thread count: ").append (m_aList.size ()).append ('\n');

    // Emit thread IDs grouped by state
    final Map <State, Set <Long>> aStateMap = _getStateMap ();
    for (final State eState : State.values ())
    {
      final Set <Long> aThreadIDs = aStateMap.get (eState);
      final int nSize = ContainerHelper.getSize (aThreadIDs);
      aSB.append ("Thread state ").append (eState).append (" [").append (nSize).append (']');
      if (nSize > 0)
        aSB.append (": ").append (aThreadIDs.toString ());
      aSB.append ('\n');
    }

    // Append all stack traces at the end
    for (final ThreadDescriptor aDescriptor : m_aList)
      aSB.append ('\n').append (aDescriptor.getAsString ());
    return aSB.toString ();
  }

  @Nonnull
  public IMicroElement getAsMicroNode ()
  {
    final IMicroElement eRet = new MicroElement ("threadlist");
    if (StringHelper.hasText (m_sError))
      eRet.appendElement ("error").appendText (m_sError);

    // Overall thread count
    eRet.setAttribute ("threadcount", m_aList.size ());

    // Emit thread IDs grouped by state
    final Map <State, Set <Long>> aStateMap = _getStateMap ();
    for (final State eState : State.values ())
    {
      final Set <Long> aThreadIDs = aStateMap.get (eState);
      final int nSize = ContainerHelper.getSize (aThreadIDs);

      final IMicroElement eThreadState = eRet.appendElement ("threadstate");
      eThreadState.setAttribute ("id", eState.toString ());
      eThreadState.setAttribute ("threadcount", nSize);
      if (nSize > 0)
        eThreadState.appendText (StringHelper.getImploded (",", aThreadIDs));
    }

    // Append all stack traces at the end
    for (final ThreadDescriptor aDescriptor : m_aList)
      eRet.appendChild (aDescriptor.getAsMicroNode ());
    return eRet;
  }

  @Nonnull
  @Nonempty
  private static String _getAsString (@Nonnull final Throwable t)
  {
    return t.getMessage () + " -- " + t.getClass ().getName ();
  }

  @Nonnull
  public static ThreadDescriptorList createWithAllThreads ()
  {
    // add dump of all threads
    final StopWatch aSW = new StopWatch (true);
    final ThreadDescriptorList ret = new ThreadDescriptorList ();
    try
    {
      // Get all stack traces, sorted by thread ID
      for (final Map.Entry <Thread, StackTraceElement []> aEntry : ContainerHelper.getSortedByKey (Thread.getAllStackTraces (),
                                                                                                   new ComparatorThreadID ())
                                                                                  .entrySet ())
      {
        final StackTraceElement [] aStackTrace = aEntry.getValue ();
        final String sStackTrace = ArrayHelper.isEmpty (aStackTrace) ? "No stack trace available!\n"
                                                                    : StackTraceHelper.getStackAsString (aStackTrace,
                                                                                                         false);
        ret.addDescriptor (new ThreadDescriptor (aEntry.getKey (), sStackTrace));
      }
    }
    catch (final Throwable t)
    {
      s_aLogger.error ("Error collecting all thread descriptors", t);
      ret.setError ("Error collecting all thread descriptors: " + _getAsString (t));
    }
    finally
    {
      final long nMillis = aSW.stopAndGetMillis ();
      if (nMillis > 1000)
        s_aLogger.warn ("Took " + nMillis + " ms to get all thread descriptors!");
    }
    return ret;
  }
}
