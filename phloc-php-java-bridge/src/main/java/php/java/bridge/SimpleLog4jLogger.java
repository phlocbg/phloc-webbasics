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

import java.lang.reflect.Method;

/**
 * A logger which uses the log4j default appender. Requires that log4j.jar is in
 * the classpath.<br>
 */
public abstract class SimpleLog4jLogger implements ILogger
{
  protected LoggerProxy logger;

  protected class LoggerProxy
  {
    protected Object nlogger;
    protected Class <?> priority;
    protected Object fatal, error, warn, info, debug;

    protected LoggerProxy () throws Exception
    {
      Class <?> c = Class.forName ("org.apache.log4j.Logger");
      final Method m = c.getMethod ("getLogger", new Class [] { String.class });
      nlogger = m.invoke (c, new Object [] { "php.java.bridge.JavaBridge" });
      c = priority = Class.forName ("org.apache.log4j.Priority");
      fatal = c.getField ("FATAL").get (c);
      error = c.getField ("ERROR").get (c);
      warn = c.getField ("WARN").get (c);
      info = c.getField ("INFO").get (c);
      debug = c.getField ("DEBUG").get (c);
    }

    private Method errorMethod;

    public synchronized void error (final String string, final Throwable t) throws Exception
    {
      if (errorMethod == null)
        errorMethod = nlogger.getClass ().getMethod ("error", new Class [] { Object.class, Throwable.class });
      errorMethod.invoke (nlogger, new Object [] { string, t });
    }

    private Method logMethod;

    public synchronized void log (final int level, final String msg) throws Exception
    {
      if (logMethod == null)
        logMethod = nlogger.getClass ().getMethod ("log", new Class [] { priority, Object.class });
      switch (level)
      {
        case 1:
          logMethod.invoke (nlogger, new Object [] { fatal, msg });
          break;
        case 2:
          logMethod.invoke (nlogger, new Object [] { error, msg });
          break;
        case 3:
          logMethod.invoke (nlogger, new Object [] { info, msg });
          break;
        case 4:
          logMethod.invoke (nlogger, new Object [] { debug, msg });
          break;
        default:
          logMethod.invoke (nlogger, new Object [] { warn, msg });
          break;
      }
    }
  }

  /**
   * Create a new log4j logger using the default appender.
   * 
   * @see php.java.bridge.Util#setDefaultLogger(ILogger)
   */
  protected SimpleLog4jLogger ()
  {}

  /** {@inheritDoc} */
  public void printStackTrace (final Throwable t)
  {
    try
    {
      logger.error ("JavaBridge exception ", t);
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
      throw new RuntimeException (e);
    }
  }

  /** {@inheritDoc} */
  public void log (final int level, final String msg)
  {
    try
    {
      logger.log (level, msg);
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
      throw new RuntimeException (e);
    }
  }

  /** {@inheritDoc} */
  public void warn (final String msg)
  {
    try
    {
      logger.log (-1, msg);
    }
    catch (final Exception e)
    {
      e.printStackTrace ();
      throw new RuntimeException (e);
    }
  }
}
