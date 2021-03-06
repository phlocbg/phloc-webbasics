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

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import php.java.bridge.Util;

/**
 * Create a standalone PHP script engines.
 */
public class PhpScriptEngineFactory implements ScriptEngineFactory
{
  /**
   * Create a new EngineFactory
   */
  public PhpScriptEngineFactory ()
  {}

  private static final String ENGINE_NAME = Util.EXTENSION_NAME + " php script engine for Java";

  /** {@inheritDoc} */
  public String getEngineName ()
  {
    return ENGINE_NAME;
  }

  /** {@inheritDoc} */
  public String getEngineVersion ()
  {
    return Util.VERSION;
  }

  /** {@inheritDoc} */
  public String getLanguageName ()
  {
    return "php";
  }

  /** {@inheritDoc} */
  public String getLanguageVersion ()
  {
    return "6";
  }

  /** {@inheritDoc} */
  public List <String> getExtensions ()
  {
    return getNames ();
  }

  /** {@inheritDoc} */
  public List <String> getMimeTypes ()
  {
    return Arrays.asList (new String [] {});
  }

  private List <String> names;

  /** {@inheritDoc} */
  public List <String> getNames ()
  {
    if (names == null)
      names = Arrays.asList (new String [] { getLanguageName (), "phtml", "php4", "php5", "php6" });
    return names;
  }

  /** {@inheritDoc} */
  public ScriptEngine getScriptEngine ()
  {
    return new PhpScriptEngine (this);
  }

  /** {@inheritDoc} */
  public Object getParameter (final String key)
  {
    if (key.equals (ScriptEngine.NAME))
      return getLanguageName ();
    if (key.equals (ScriptEngine.ENGINE))
      return getEngineName ();
    if (key.equals (ScriptEngine.ENGINE_VERSION))
      return getEngineVersion ();
    if (key.equals (ScriptEngine.LANGUAGE))
      return getLanguageName ();
    if (key.equals (ScriptEngine.LANGUAGE_VERSION))
      return getLanguageVersion ();
    if (key.equals ("THREADING"))
      return "STATELESS";
    throw new IllegalArgumentException ("key: '" + key + "'");
  }

  /** {@inheritDoc} */
  public String getMethodCallSyntax (final String obj, final String m, final String... args)
  {
    final StringBuilder b = new StringBuilder ();
    b.append ('$').append (obj).append ("->").append (m).append ('(');
    int i;
    for (i = 0; i < args.length - 1; i++)
      b.append (args[i]).append (',');
    b.append (args[i]).append (')');
    return b.toString ();
  }

  /** {@inheritDoc} */
  public String getOutputStatement (final String toDisplay)
  {
    return "echo(" + toDisplay + ")";
  }

  /** {@inheritDoc} */
  public String getProgram (final String... statements)
  {
    int i = 0;
    final StringBuilder b = new StringBuilder ("<?php ");

    for (i = 0; i < statements.length; i++)
    {
      b.append (statements[i]);
      b.append (";");
    }
    b.append ("?>");
    return b.toString ();
  }
}
