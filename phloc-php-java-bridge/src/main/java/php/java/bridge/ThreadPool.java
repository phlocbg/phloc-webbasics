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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.bridge;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * A standard thread pool, accepts runnables and runs them in a thread
 * environment. Example:<br>
 * <code>
 * ThreadPool pool = new ThreadPool("MyThreadPool", 20);<br>
 * pool.start(new YourRunnable());<br>
 * </code>
 *
 * @author jostb
 */
public class ThreadPool
{
  private String name;
  private int threads = 0, idles = 0, poolMaxSize, poolReserve;
  private final List <Runnable> runnables = new ArrayList <Runnable> ();
  private final List <Delegate> threadList = new ArrayList <Delegate> ();

  /**
   * Threads continue to pull runnables and run them in the thread environment.
   */
  protected class Delegate extends Thread
  {
    protected boolean terminate = false;

    public Delegate (final String name)
    {
      super (name);
    }

    public Delegate (final ThreadGroup group, final String name)
    {
      super (group, name);
    }

    protected void terminate ()
    {}

    protected void end ()
    {}

    protected void createThread (final String name)
    {
      startNewThread (name);
    }

    @Override
    public void run ()
    {
      try
      {
        while (!terminate)
        {
          getNextRunnable ().run ();
          end ();
        }
      }
      catch (final InterruptedException e)
      {
        /* ignore */
      }
      catch (final Throwable t)
      {
        Util.printStackTrace (t);
        createThread (getName ());
      }
      finally
      {
        terminate ();
      }
    }
  }

  protected Delegate createDelegate (final String name)
  {
    return new Delegate (name);
  }

  protected void startNewThread (final String name)
  {
    final Delegate d = createDelegate (name);
    threadList.add (d);
    d.start ();
  }

  protected synchronized boolean checkReserve ()
  {
    return threads - idles < poolReserve;
  }

  /*
   * Helper: Pull a runnable off the list of runnables. If there's no work,
   * sleep the thread until we receive a notify.
   */
  private synchronized Runnable getNextRunnable () throws InterruptedException
  {
    while (runnables.isEmpty ())
    {
      idles++;
      wait ();
      idles--;
    }
    return runnables.remove (0);
  }

  /**
   * Push a runnable to the list of runnables. The notify will fail if all
   * threads are busy. Since the pool contains at least one thread, it will pull
   * the runnable off the list when it becomes available.
   *
   * @param r
   *        - The runnable
   */
  public synchronized void start (final Runnable r)
  {
    runnables.add (r);
    if (idles == 0 && threads < poolMaxSize)
    {
      threads++;
      startNewThread (name + "#" + String.valueOf (threads));
    }
    else
      notify ();
  }

  protected void init (final String name, final int poolMaxSize)
  {
    this.name = name;
    this.poolMaxSize = poolMaxSize;
    this.poolReserve = (poolMaxSize >>> 2) * 3;
  }

  /** Terminate all threads in the pool. */
  public void destroy ()
  {
    for (final Delegate d : threadList)
    {
      d.terminate = true;
      d.interrupt ();
    }
  }

  /**
   * Creates a new thread pool.
   *
   * @param name
   *        - The name of the pool threads.
   * @param poolMaxSize
   *        - The max. number of threads, must be >= 1.
   */
  public ThreadPool (final String name, final int poolMaxSize)
  {
    if (poolMaxSize < 1)
      throw new IllegalArgumentException ("poolMaxSize must be >0");
    init (name, poolMaxSize);
  }
}
