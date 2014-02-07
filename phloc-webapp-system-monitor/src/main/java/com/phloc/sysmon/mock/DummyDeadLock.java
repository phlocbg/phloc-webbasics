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
package com.phloc.sysmon.mock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.phloc.commons.concurrent.ThreadUtils;

public final class DummyDeadLock
{
  private static final class A
  {
    private final Lock lock1 = new ReentrantLock ();
    private final Lock lock2 = new ReentrantLock ();

    public void f ()
    {
      lock1.lock ();
      try
      {
        lock2.lock ();
        try
        {}
        finally
        {
          lock2.unlock ();
        }
      }
      finally
      {
        lock1.unlock ();
      }
    }

    public void g ()
    {
      lock2.lock ();
      try
      {
        f ();
      }
      finally
      {
        lock2.unlock ();
      }
    }
  }

  private DummyDeadLock ()
  {}

  public static void triggerDummyDeadlock (final int nSecondsDelay)
  {
    new Thread ()
    {
      @Override
      public void run ()
      {
        ThreadUtils.sleep (nSecondsDelay, TimeUnit.SECONDS);

        try
        {
          final A a = new A ();
          final Thread t1 = new Thread (new Runnable ()
          {
            public void run ()
            {
              while (true)
                a.f ();
            }
          }, "t1");
          final Thread t2 = new Thread (new Runnable ()
          {
            public void run ()
            {
              a.g ();
            }
          }, "t2");
          t1.start ();
          t2.start ();
          t1.join ();
          t2.join ();
        }
        catch (final InterruptedException ex)
        {
          ex.printStackTrace ();
        }
      }
    }.start ();
  }
}
