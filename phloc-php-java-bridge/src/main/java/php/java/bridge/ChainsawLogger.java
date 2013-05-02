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

package php.java.bridge;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * A logger class which connects to chainsaw -- chainsaw is a log4j viewer.
 * Requires that log4j.jar is in the classpath. Start chainsaw, for example by
 * clicking on Applications -&gt; Programming -&gt; Chainsaw or via:<blockquote>
 * <code>java -classpath
 * /usr/share/java/log4j.jar org.apache.log4j.chainsaw.Main</code></blockquote>
 * and then start the PHP/Java Bridge:<br>
 * <blockquote>
 * <code>java -classpath /usr/share/java/log4j.jar:/usr/share/java/JavaBridge.jar php.java.bridge.JavaBridge</code>
 * </blockquote> or set the PHP .ini option <blockquote>
 * <code>java.log_file=@127.0.0.1:4445</code></blockquote>.
 */
public class ChainsawLogger extends SimpleLog4jLogger
{
  /** The default chainsaw port */
  public static final int DEFAULT_PORT = 4445;
  /** The default chainsaw port */
  public static final String DEFAULT_PORT_NAME = String.valueOf (DEFAULT_PORT);
  /** The default cainsaw host */
  public static final String DEFAULT_HOST = "127.0.0.1";

  /** Eg: java -Dchainsaw.port=14445 ... */
  private int configuredPort;

  /**
   * override this method, if you want to connect to a different host or port
   * 
   * @param defaultHost
   *        The default host
   * @param configuredPort
   *        The default port
   * @throws Exception
   *         If chainsaw isn't running.
   */
  public void configure (final String defaultHost, final int configuredPort) throws Exception
  {
    this.configuredPort = configuredPort;
    final Socket s = new Socket (defaultHost, configuredPort);
    s.close ();
    Class <?> clazz = Class.forName ("org.apache.log4j.net.SocketAppender");
    final Constructor <?> constructor = clazz.getConstructor (new Class [] { String.class, int.class });
    final Object socketAppender = constructor.newInstance (new Object [] { defaultHost, new Integer (configuredPort) });
    clazz = Class.forName ("org.apache.log4j.BasicConfigurator");
    Method method = clazz.getMethod ("resetConfiguration", Util.ZERO_PARAM);
    method.invoke (clazz, Util.ZERO_ARG);
    final Class <?> appender = Class.forName ("org.apache.log4j.Appender");
    method = clazz.getMethod ("configure", new Class [] { appender });
    method.invoke (clazz, new Object [] { socketAppender });
  }

  protected void init () throws Exception
  {
    configure (DEFAULT_HOST, Integer.parseInt (System.getProperty ("chainsaw.port", DEFAULT_PORT_NAME)));
    logger = new LoggerProxy ();
  }

  /**
   * Create a new chainsaw logger.
   * 
   * @see php.java.bridge.Util#setDefaultLogger(ILogger)
   * @throws UnknownHostException
   *         If the host does not exist.
   */
  protected ChainsawLogger ()
  {
    super ();
  }

  /**
   * Create a new chainsaw logger.
   * 
   * @return The chainsaw logger
   * @see php.java.bridge.Util#setDefaultLogger(ILogger)
   * @throws Exception
   *         If chainsaw isn't running.
   */
  public static ChainsawLogger createChainsawLogger () throws Exception
  {
    final ChainsawLogger logger = new ChainsawLogger ();
    logger.init ();
    return logger;
  }

  /** {@inheritDoc} */
  @Override
  public String toString ()
  {
    return "Chainsaw logger, host: " + DEFAULT_HOST + ", port: " + configuredPort;
  }
}
