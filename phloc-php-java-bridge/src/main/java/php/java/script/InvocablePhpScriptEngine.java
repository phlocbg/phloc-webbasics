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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import php.java.bridge.PhpProcedure;
import php.java.bridge.Util;
import php.java.bridge.Util.UtilThread;
import php.java.bridge.http.IContext;

/**
 * This class implements the ScriptEngine and the Invocable interface.
 * <p>
 * Example: <blockquote> <code>
 * ScriptEngine e = (new ScriptEngineManager()).getEngineByName("php-invocable");<br>
 * e.eval(&lt;? function f() {return java_server_name();}?&gt;<br>
 * System.out.println(((Invocable)e).invokeFunction("f", new Object[]{}));<br>
 * ((Closeable)e).close();<br>
 * </code> </blockquote><br>
 * Another example which invokes a remote PHP "method" bound in the closed-over
 * PHP environment. The PHP script "hello.php": <blockquote> <code>
 * &lt;?php require_once("java/Java.inc");<br>
 * function f() {return java_server_name();};<br>
 * java_call_with_continuation(java_closure());<br>
 * ?&gt;<br>
 * </code> </blockquote><br>
 * The Java code: <blockquote> <code>
 * ScriptEngine e = (new ScriptEngineManager()).getEngineByName("php-invocable");<br>
 * e.eval(new php.java.script.URLReader(new URL("http://localhost/hello.php")));<br>
 * System.out.println(((Invocable)e).invokeMethod(e.get("php.java.bridge.PhpProcedure"), "f", new Object[]{}));<br>
 * ((Closeable)e).close();<br>
 * </code> </blockquote>
 */
public class InvocablePhpScriptEngine extends AbstractPhpScriptEngine implements Invocable
{
  private static final String X_JAVABRIDGE_INCLUDE = Util.X_JAVABRIDGE_INCLUDE;
  private static final String PHP_JAVA_CONTEXT_CALL_JAVA_CLOSURE = "<?php java_context()->call(java_closure()); ?>";
  protected static final String EMPTY_INCLUDE = "@";
  private static boolean registeredHook = false;
  private static final List <InvocablePhpScriptEngine> engines = new ArrayList <InvocablePhpScriptEngine> ();
  private static final String PHP_EMPTY_SCRIPT = "<?php ?>";

  /**
   * Create a new ScriptEngine with a default context.
   */
  public InvocablePhpScriptEngine ()
  {
    this (new PhpScriptEngineFactory ());
  }

  /**
   * Create a new ScriptEngine from a factory.
   * 
   * @param factory
   *        The factory
   * @see #getFactory()
   */
  public InvocablePhpScriptEngine (final PhpScriptEngineFactory factory)
  {
    super (factory);
  }

  /**
   * Create a new ScriptEngine with bindings.
   * 
   * @param n
   *        the bindings
   */
  public InvocablePhpScriptEngine (final Bindings n)
  {
    this ();
    setBindings (n, ScriptContext.ENGINE_SCOPE);
  }

  /*
   * (non-Javadoc)
   * @see javax.script.Invocable#call(java.lang.String, java.lang.Object[])
   */
  protected Object invoke (final String methodName, final Object [] args) throws ScriptException, NoSuchMethodException
  {
    if (methodName == null)
    {
      release ();
      return null;
    }

    if (scriptClosure == null)
    {
      if (Util.logLevel > 4)
        Util.warn ("Evaluating an empty script either because eval() has not been called or release() has been called.");
      eval (PHP_EMPTY_SCRIPT);
    }
    try
    {
      return invoke (scriptClosure, methodName, args);
    }
    catch (final php.java.bridge.Request.AbortException e)
    {
      release ();
      throw new ScriptException (e);
    }
    catch (final NoSuchMethodError e)
    { // conform to jsr223
      throw new NoSuchMethodException (String.valueOf (e.getMessage ()));
    }
  }

  /** {@inheritDoc} */
  public Object invokeFunction (final String methodName, final Object... args) throws ScriptException,
                                                                              NoSuchMethodException
  {
    return invoke (methodName, args);
  }

  private void checkPhpClosure (final Object thiz)
  {
    if (thiz == null)
      throw new IllegalStateException ("PHP script did not pass its continuation to us!. Please check if the previous call to eval() reported any errors. Or else check if it called OUR continuation.");
  }

  /*
   * (non-Javadoc)
   * @see javax.script.Invocable#call(java.lang.String, java.lang.Object,
   * java.lang.Object[])
   */
  protected Object invoke (final Object thiz, final String methodName, final Object [] args) throws ScriptException,
                                                                                            NoSuchMethodException
  {
    checkPhpClosure (thiz);
    final PhpProcedure proc = (PhpProcedure) (Proxy.getInvocationHandler (thiz));
    try
    {
      return proc.invoke (script, methodName, args);
    }
    catch (final ScriptException e)
    {
      throw e;
    }
    catch (final NoSuchMethodException e)
    {
      throw e;
    }
    catch (final RuntimeException e)
    {
      throw e; // don't wrap RuntimeException
    }
    catch (final NoSuchMethodError e)
    { // conform to jsr223
      throw new NoSuchMethodException (String.valueOf (e.getMessage ()));
    }
    catch (final Error er)
    {
      throw er;
    }
    catch (final Throwable e)
    {
      throw new PhpScriptException ("Invocation threw exception ", e);
    }
  }

  /** {@inheritDoc} */
  public Object invokeMethod (final Object thiz, final String methodName, final Object... args) throws ScriptException,
                                                                                               NoSuchMethodException
  {
    return invoke (thiz, methodName, args);
  }

  /** {@inheritDoc} */
  public <T> T getInterface (final Class <T> clasz)
  {
    checkPhpClosure (script);
    return getInterface (script, clasz);
  }

  /** {@inheritDoc} */
  public <T> T getInterface (final Object thiz, final Class <T> clasz)
  {
    checkPhpClosure (thiz);
    final Class <?> [] interfaces = clasz == null ? new Class <?> [0] : new Class <?> [] { clasz };
    return (T) PhpProcedure.createProxy (interfaces, (PhpProcedure) Proxy.getInvocationHandler (thiz));
  }

  @Override
  protected Reader getLocalReader (final Reader reader, final boolean embedJavaInc) throws IOException
  {
    final ByteArrayOutputStream out = new ByteArrayOutputStream ();
    Writer w = new OutputStreamWriter (out);

    final String stdHeader = embedJavaInc ? null : ((IContext) getContext ()).getRedirectURL ("/JavaBridge");
    Reader localReader = new StringReader (getStandardHeader (stdHeader));

    final char [] buf = new char [Util.BUF_SIZE];
    int c;
    try
    {
      /*
       * header: <?
       * require_once("http://localhost:<ourPort>/JavaBridge/java/Java.inc"); ?>
       */
      while ((c = localReader.read (buf)) > 0)
        w.write (buf, 0, c);
      localReader.close ();
      localReader = null;

      /* the script: */
      while ((c = reader.read (buf)) > 0)
        w.write (buf, 0, c);

      /*
       * get the default, top-level, closure and call it, to stop the script
       * from terminating
       */
      localReader = new StringReader (PHP_JAVA_CONTEXT_CALL_JAVA_CLOSURE);
      while ((c = localReader.read (buf)) > 0)
        w.write (buf, 0, c);
      localReader.close ();
      localReader = null;
      w.close ();
      w = null;

      /* now evaluate our script */
      localReader = new InputStreamReader (new ByteArrayInputStream (out.toByteArray ()));
      return localReader;
    }
    finally
    {
      if (w != null)
        try
        {
          w.close ();
        }
        catch (final IOException e)
        {/* ignore */}
    }
  }

  @Override
  protected Object doEvalPhp (final Reader reader, final ScriptContext context) throws ScriptException
  {
    if (reader instanceof URLReader)
      return eval ((URLReader) reader, context);

    if ((continuation != null) || (reader == null))
      release ();
    if (reader == null)
      return null;

    setNewContextFactory ();
    env.put (X_JAVABRIDGE_INCLUDE, EMPTY_INCLUDE);
    Reader localReader = null;
    try
    {
      localReader = getLocalReader (reader, false);
      this.script = doEval (localReader, context);
      if (this.script != null)
      {
        /*
         * get the proxy, either the one from the user script or our default
         * proxy
         */
        this.scriptClosure = script;
      }
    }
    catch (final Exception e)
    {
      Util.printStackTrace (e);
      if (e instanceof RuntimeException)
        throw (RuntimeException) e;
      if (e instanceof ScriptException)
        throw (ScriptException) e;
      throw new ScriptException (e);
    }
    finally
    {
      if (localReader != null)
        try
        {
          localReader.close ();
        }
        catch (final IOException e)
        {/* ignore */}
      handleRelease ();
    }
    return resultProxy;
  }

  @Override
  protected Object doEvalCompiledPhp (final Reader reader, final ScriptContext context) throws ScriptException
  {
    if ((continuation != null) || (reader == null))
      release ();
    if (reader == null)
      return null;

    setNewContextFactory ();
    env.put (X_JAVABRIDGE_INCLUDE, EMPTY_INCLUDE);
    try
    {
      this.script = doEval (reader, context);
      if (this.script != null)
      {
        /*
         * get the proxy, either the one from the user script or our default
         * proxy
         */
        this.scriptClosure = script;
      }
    }
    catch (final Exception e)
    {
      Util.printStackTrace (e);
      if (e instanceof RuntimeException)
        throw (RuntimeException) e;
      if (e instanceof ScriptException)
        throw (ScriptException) e;
      throw new ScriptException (e);
    }
    finally
    {
      handleRelease ();
    }
    return resultProxy;
  }

  protected Object eval (final URLReader reader, final ScriptContext context) throws ScriptException
  {
    if ((continuation != null) || (reader == null))
      release ();
    if (reader == null)
      return null;

    setNewContextFactory ();
    env.put (X_JAVABRIDGE_INCLUDE, EMPTY_INCLUDE);

    final ByteArrayOutputStream out = new ByteArrayOutputStream ();
    final Writer w = new OutputStreamWriter (out);
    try
    {
      this.script = doEval (reader, context);
      if (this.script != null)
      {
        /*
         * get the proxy, either the one from the user script or our default
         * proxy
         */
        this.scriptClosure = script;
      }
    }
    catch (final Exception e)
    {
      Util.printStackTrace (e);
      if (e instanceof RuntimeException)
        throw (RuntimeException) e;
      if (e instanceof ScriptException)
        throw (ScriptException) e;
      throw new ScriptException (e);
    }
    finally
    {
      try
      {
        w.close ();
      }
      catch (final IOException e)
      {/* ignore */}
      handleRelease ();
    }
    return resultProxy;
  }

  protected void handleRelease ()
  {
    // make sure to properly release them upon System.exit().
    synchronized (engines)
    {
      if (!registeredHook)
      {
        registeredHook = true;
        try
        {
          Runtime.getRuntime ().addShutdownHook (new UtilThread ()
          {
            @Override
            public void run ()
            {
              if (engines == null)
                return;
              synchronized (engines)
              {
                for (final Iterator <InvocablePhpScriptEngine> ii = engines.iterator (); ii.hasNext (); ii.remove ())
                {
                  final InvocablePhpScriptEngine e = ii.next ();
                  e.releaseInternal ();
                }
              }
            }
          });
        }
        catch (final SecurityException e)
        {/* ignore */}
      }
      engines.add (this);
    }
  }

  private void releaseInternal ()
  {
    super.release ();
  }

  /** {@inheritDoc} */
  @Override
  public void release ()
  {
    synchronized (engines)
    {
      releaseInternal ();
      engines.remove (this);
    }
  }
}
