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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import php.java.bridge.Util;
import php.java.bridge.http.IContext;

/**
 * This class implements the ScriptEngine.
 * <p>
 * Example:
 * <p>
 * <code>
 * ScriptEngine e = (new ScriptEngineManager()).getEngineByName("php");<br>
 * try { e.eval(&lt;?php foo() ?&gt;"); } catch (ScriptException e) { ... }<br>
 * </code>
 * 
 * @author jostb
 */
public class PhpScriptEngine extends AbstractPhpScriptEngine implements Closeable
{

  /**
   * Create a new ScriptEngine with a default context.
   */
  public PhpScriptEngine ()
  {
    super (new PhpScriptEngineFactory ());
  }

  /**
   * Create a new ScriptEngine from a factory.
   * 
   * @param factory
   *        The factory
   * @see #getFactory()
   */
  public PhpScriptEngine (final PhpScriptEngineFactory factory)
  {
    super (factory);
  }

  /**
   * Create a new ScriptEngine with bindings.
   * 
   * @param n
   *        the bindings
   */
  public PhpScriptEngine (final Bindings n)
  {
    this ();
    setBindings (n, ScriptContext.ENGINE_SCOPE);
  }

  @Override
  protected Reader getLocalReader (final Reader reader, final boolean embedJavaInc) throws IOException
  {
    /*
     * header: <?
     * require_once("http://localhost:<ourPort>/JavaBridge/java/Java.inc"); ?>
     */
    final ByteArrayOutputStream out = new ByteArrayOutputStream ();
    Writer w = new OutputStreamWriter (out);
    try
    {
      Reader localReader = null;
      final char [] buf = new char [Util.BUF_SIZE];
      int c;
      final String stdHeader = embedJavaInc ? null : ((IContext) getContext ()).getRedirectURL ("/JavaBridge");
      localReader = new StringReader (getStandardHeader (stdHeader));

      while ((c = localReader.read (buf)) > 0)
        w.write (buf, 0, c);
      localReader.close ();
      localReader = null;

      /* the script: */
      while ((c = reader.read (buf)) > 0)
        w.write (buf, 0, c);
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
    if ((continuation != null) || (reader == null))
      release ();
    if (reader == null)
      return null;

    setNewContextFactory ();
    Reader localReader = null;

    try
    {
      localReader = getLocalReader (reader, false);
      this.script = doEval (localReader, context);
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

      // release the engine, so that any error reported by the script can
      // trigger a Java exception
      release ();
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
    try
    {
      this.script = doEval (reader, context);
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

      // release the engine, so that any error reported by the script can
      // trigger a Java exception
      release ();
    }

    return resultProxy;
  }
}
