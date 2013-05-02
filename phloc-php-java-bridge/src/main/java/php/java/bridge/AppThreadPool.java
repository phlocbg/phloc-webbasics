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

/**
 * A specialized thread pool which automatically terminates all group threads at
 * the end of the request. When this thread pool is used, users cannot create
 * long-running threads from PHP scripts
 * 
 * @author jostb
 */
public final class AppThreadPool extends ThreadPool
{
  /** Every Delegate has its own locked ThreadGroup. */
  static final class Group extends ThreadGroup
  {
    private boolean isLocked = false;

    private void _init ()
    {
      setDaemon (true);
    }

    public Group (final String name)
    {
      super (name);
      _init ();
    }

    public Group (final ThreadGroup group, final String name)
    {
      super (group, name);
      _init ();
    }

    void lock ()
    {
      isLocked = true;
    }

    void unlock ()
    {
      isLocked = false;
    }

    public boolean isLocked ()
    {
      return isLocked;
    }
  }

  /** Application threads belong to this group */
  static final class AppGroup extends ThreadGroup
  {
    private void _init ()
    {
      setDaemon (true);
    }

    public AppGroup (final String name)
    {
      super (name);
      _init ();
    }

    public AppGroup (final ThreadGroup group, final String name)
    {
      super (group, name);
      _init ();
    }
  }

  /**
   * A specialized delegate which can handle persistent connections and
   * interrupts application threads when end() or terminate() is called.
   */
  final class Delegate extends ThreadPool.Delegate
  {
    private ThreadGroup appGroup = null;

    /**
     * Create a new delegate. The thread runs until terminatePersistent() is
     * called.
     * 
     * @param name
     *        The name of the delegate.
     */
    protected Delegate (final String name)
    {
      super (new Group (name), name);
      ((Group) getThreadGroup ()).lock ();
    }

    /**
     * Return the app group for this delegate. All user-created threads live in
     * this group and receive an interrupt (which should terminate them), when
     * the request is done.
     * 
     * @return The application group
     */
    public ThreadGroup getAppGroup ()
    {
      if (appGroup != null)
        return appGroup;
      final Group group = (Group) getThreadGroup ();
      group.unlock ();
      appGroup = new AppGroup ("JavaBridgeThreadPoolAppGroup");
      group.lock ();
      return appGroup;
    }

    /**
     * Make this thread a daemon thread. A daemon is not visible but still
     * managed by the thread pool.
     */
    public void setPersistent ()
    {
      if (!checkReserve () && !terminate)
      {
        // pool is nearly full, store new long-running clients outside
        terminate = true;
        String name = getName ();
        setName (name + ",isDaemon=true");
        if (Util.logLevel > 5)
          name += "+";
        createThread (name);
      }
      end ();
    }

    @Override
    protected void createThread (final String name)
    {
      final Group group = (Group) getThreadGroup ();
      group.unlock ();
      super.createThread (name);
      group.lock ();
    }

    @Override
    protected void terminate ()
    {
      if (Util.logLevel > 4)
        Util.logDebug ("term (thread removed from pool): " + this);
      final ThreadGroup group = appGroup;
      if (group != null)
      {
        try
        {
          group.interrupt ();
        }
        catch (final SecurityException e)
        {
          return;
        }
        try
        {
          group.destroy ();
        }
        catch (final SecurityException e)
        {
          /* ignore */
        }
        catch (final IllegalThreadStateException e1)
        {
          Util.printStackTrace (e1);
        }
        catch (final Exception e2)
        {
          Util.printStackTrace (e2);
        }
        finally
        {
          appGroup = null;
        }
      }
    }

    @Override
    protected void end ()
    {
      super.end ();
      if (Util.logLevel > 4)
        Util.logDebug ("end (thread returned to pool): " + this);
      final ThreadGroup group = appGroup;
      if (group != null)
        try
        {
          group.interrupt ();
        }
        catch (final SecurityException e)
        {
          /* ignore */
        }
        catch (final Exception e2)
        {
          Util.printStackTrace (e2);
        }
        finally
        {
          appGroup = null;
        }
    }

  }

  @Override
  protected ThreadPool.Delegate createDelegate (final String name)
  {
    return new Delegate (name);
  }

  /**
   * Creates a new thread pool.
   * 
   * @param name
   *        - The name of the pool threads.
   * @param poolMaxSize
   *        - The max. number of threads, must be >= 1.
   */
  public AppThreadPool (final String name, final int poolMaxSize)
  {
    super (name, poolMaxSize);
  }
}
