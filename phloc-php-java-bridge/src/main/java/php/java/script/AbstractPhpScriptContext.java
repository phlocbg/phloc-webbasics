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

import java.io.Writer;

import javax.script.ScriptContext;

import php.java.bridge.Util;
import php.java.bridge.http.IContext;
import php.java.bridge.http.WriterOutputStream;

/**
 * A simple ScriptContext which can be used in servlet- or standalone
 * environments.
 * 
 * @author jostb
 */
public abstract class AbstractPhpScriptContext extends ScriptContextDecorator implements IPhpScriptContext
{

  public AbstractPhpScriptContext (final ScriptContext ctx)
  {
    super (ctx);
  }

  protected Continuation kont;

  /** {@inheritDoc} */
  protected Writer writer;

  @Override
  public Writer getWriter ()
  {
    if (writer == null)
      writer = super.getWriter ();
    if (!(writer instanceof PhpScriptWriter))
      setWriter (writer);
    return writer;
  }

  /** {@inheritDoc} */
  protected Writer errorWriter;

  @Override
  public Writer getErrorWriter ()
  {
    if (errorWriter == null)
      errorWriter = super.getErrorWriter ();
    if (!(errorWriter instanceof PhpScriptWriter))
      setErrorWriter (errorWriter);
    return errorWriter;
  }

  /**
   * Ignore the default java_context()-&gt;call(java_closure()) call at the end
   * of the invocable script, if the user has provided its own.
   */
  private boolean continuationCalled;

  /** {@inheritDoc} */
  public void startContinuation ()
  {
    Util.PHP_SCRIPT_ENGINE_THREAD_POOL.start (kont);
  }

  /** @inheritDoc */
  public void setContinuation (final Continuation kont)
  {
    this.kont = kont;
    continuationCalled = false;
  }

  /** @inheritDoc */
  public Continuation getContinuation ()
  {
    return kont;
  }

  /** @inheritDoc */
  public boolean call (final Object kont) throws Exception
  {
    if (!continuationCalled)
    {
      this.setAttribute (IContext.PHP_PROCEDURE, kont, IContext.ENGINE_SCOPE);
      // prefer the user's java_context()->call(java_closure())
      continuationCalled = true;

      this.kont.call (kont);
    }
    return true;
  }

  /**
   * Sets the <code>Writer</code> for scripts to use when displaying output.
   * TODO: test
   * 
   * @param writer
   *        The new <code>Writer</code>.
   */
  @Override
  public void setWriter (final Writer writer)
  {
    super.setWriter (this.writer = new PhpScriptWriter (new WriterOutputStream (writer)));
  }

  /**
   * Sets the <code>Writer</code> used to display error output.
   * 
   * @param writer
   *        The <code>Writer</code>.
   */
  @Override
  public void setErrorWriter (final Writer writer)
  {
    super.setErrorWriter (this.errorWriter = new PhpScriptWriter (new WriterOutputStream (writer)));
  }
}