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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.script;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import php.java.bridge.JavaBridgeRunner;
import php.java.bridge.Util;
import php.java.bridge.http.HttpRequest;
import php.java.bridge.http.HttpResponse;

/**
 * This is the main entry point for the PHP/Java Bridge library. Example:<br>
 * public MyClass { <br>
 * &nbsp;&nbsp;public static void main(String s[]) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; JavaBridgeRunner runner =
 * JavaBridgeRunner.getInstance();<br>
 * &nbsp;&nbsp;&nbsp;&nbsp; // connect to port 9267 and send protocol requests
 * ... <br>
 * &nbsp;&nbsp;&nbsp;&nbsp;runner.destroy();<br>
 * &nbsp;&nbsp;}<br>
 * }<br>
 * 
 * @author jostb
 * @see php.java.script.PhpScriptContext
 */
public class JavaBridgeScriptRunner extends JavaBridgeRunner
{
  private final ScriptEngineManager m = new ScriptEngineManager ();

  protected JavaBridgeScriptRunner () throws IOException
  {
    super ();
  }

  public JavaBridgeScriptRunner (final String serverPort, final boolean isSecure) throws IOException
  {
    super (serverPort, isSecure);
  }

  public JavaBridgeScriptRunner (final String serverPort) throws IOException
  {
    super (serverPort);
  }

  /**
   * Return a instance.
   * 
   * @param serverPort
   *        The server port name
   * @param isSecure
   *        use https instead of http
   * @return a standalone runner
   * @throws IOException
   */
  public static synchronized JavaBridgeRunner getRequiredInstance (final String serverPort, final boolean isSecure) throws IOException
  {
    if (runner != null)
      return runner;
    runner = new JavaBridgeScriptRunner (serverPort, isSecure);
    return runner;
  }

  /**
   * Return a instance.
   * 
   * @param serverPort
   *        The server port name
   * @param isSecure
   *        use https instead of http
   * @return a standalone runner
   */
  public static synchronized JavaBridgeRunner getInstance (final String serverPort, final boolean isSecure)
  {
    if (runner != null)
      return runner;
    try
    {
      runner = new JavaBridgeScriptRunner (serverPort, isSecure);
    }
    catch (final IOException e)
    {
      Util.printStackTrace (e);
    }
    return runner;
  }

  /**
   * Return a instance.
   * 
   * @param serverPort
   *        The server port name
   * @return a standalone runner
   * @throws IOException
   */
  public static synchronized JavaBridgeRunner getRequiredInstance (final String serverPort) throws IOException
  {
    if (runner != null)
      return runner;
    runner = new JavaBridgeScriptRunner (serverPort);
    return runner;
  }

  /**
   * Return a instance.
   * 
   * @param serverPort
   *        The server port name
   * @return a standalone runner
   */
  public static synchronized JavaBridgeRunner getInstance (final String serverPort)
  {
    if (runner != null)
      return runner;
    try
    {
      runner = new JavaBridgeScriptRunner (serverPort);
    }
    catch (final IOException e)
    {
      Util.printStackTrace (e);
    }
    return runner;
  }

  /**
   * Return a instance.
   * 
   * @return a standalone runner
   * @throws IOException
   */
  public static synchronized JavaBridgeRunner getRequiredInstance () throws IOException
  {
    if (runner != null)
      return runner;
    runner = new JavaBridgeScriptRunner ();
    return runner;
  }

  /**
   * Evaluate the script engine. The engine is searched through the discovery
   * mechanism. Add the "php-script.jar" or some other JSR223 script engine to
   * the java ext dirs (usually /usr/share/java/ext or
   * /usr/java/packages/lib/ext) and start the HTTP server: java -jar
   * JavaBridge.jar HTTP_LOCAL:8080. Browse to http://localhost:8080/test.php.
   * 
   * @param f
   *        The full name as a file
   * @param params
   *        The request parameter
   * @param length
   *        The length of the file
   * @param req
   *        The HTTP request object
   * @param res
   *        The HTTP response object
   * @return true if the runner could evaluate the script, false otherwise.
   * @throws IOException
   */
  @Override
  protected boolean handleScriptContent (final String name,
                                         final String params,
                                         final File f,
                                         final int length,
                                         final HttpRequest req,
                                         final HttpResponse res) throws IOException
  {
    if ("show".equals (params))
      return false;

    final int extIdx = name.lastIndexOf ('.');
    if (extIdx == -1)
      return false;
    String ext = name.substring (extIdx + 1);
    try
    {
      if ("php".equals (ext))
        ext = "phtml"; // we don't want bug reports from "quercus" users
      final ScriptEngine engine = m.getEngineByExtension (ext);

      if (engine == null)
        return false;
      final ByteArrayOutputStream xout = new ByteArrayOutputStream ();
      final PrintWriter writer = new PrintWriter (new OutputStreamWriter (xout, Util.UTF8));
      ScriptContext ctx = engine.getContext ();
      ctx = new PhpJavaBridgeRunnerScriptContext (ctx, this);
      ctx.setWriter (writer);
      ctx.setErrorWriter (writer);
      if (isSecure)
        engine.setContext (new PhpSecureScriptContext (ctx));

      final StringBuilder buf = new StringBuilder ("/");
      buf.append (name);
      if (params != null)
      {
        buf.append ("?");
        buf.append (params);
      }

      FileReader r = null;
      try
      {
        r = new FileReader (f);
        engine.eval (r);
      }
      catch (final Throwable e1)
      {
        e1.printStackTrace (writer);
        Util.printStackTrace (e1);
      }
      finally
      {
        try
        {
          engine.eval ((Reader) null);
        }
        catch (final Exception e1)
        {/* ignore */}
        if (r != null)
          try
          {
            r.close ();
          }
          catch (final Exception e1)
          {/* ignore */}
      }
      res.addHeader ("Content-Type", "text/html; charset=UTF-8");
      res.setContentLength (xout.size ());
      final OutputStream out = res.getOutputStream ();
      writer.close ();
      xout.writeTo (out);
    }
    catch (final Exception e)
    {
      Util.printStackTrace (e);
      return false;
    }
    return true;
  }
}
