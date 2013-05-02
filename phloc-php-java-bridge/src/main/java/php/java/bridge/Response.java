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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class is used to write the response to the front-end.
 * 
 * @author jostb
 */
public final class Response
{

  /**
   * A specialized writer which writes arrays as values. Used by getValues() and
   * in php 4.
   * 
   * @see JavaBridge#getValues(Object)
   */
  public static final int VALUES_WRITER = 1;

  /**
   * A specialized writer which casts the value. Used by cast().
   * 
   * @see JavaBridge#cast(Object, Class)
   */
  public static final int COERCE_WRITER = 2;

  // used in getFirstBytes() only
  static final byte append_for_OutBuf_getFirstBytes[] = new byte [] { '.', '.', '.' };
  static final byte append_none_for_OutBuf_getFirstBytes[] = new byte [0];

  private static class Base64OutputBuffer extends Base64EncodingOutputBuffer
  {
    Base64OutputBuffer (final JavaBridge bridge)
    {
      super (bridge);
    }

    @Override
    protected void appendQuoted (final byte s[])
    {
      appendBase64 (s);
    }
  }

  protected HexOutputBuffer buf;
  long peer;
  protected JavaBridge bridge;
  private boolean hasLastAsyncException, hasLastAsyncExceptionSet;

  static final String MSG = "FATAL: Undeclared java.lang.RuntimeException detected.";

  private static final class UndeclaredThrowableErrorMarker extends RuntimeException
  {
    private static final long serialVersionUID = -578332461418889089L;

    public UndeclaredThrowableErrorMarker (final Throwable e)
    {
      super (MSG, e);
    }

    @Override
    public String toString ()
    {
      return MSG + " " + String.valueOf (getCause ());
    }

    @Override
    public String getMessage ()
    {
      return getCause ().getMessage ();
    }

    @Override
    public String getLocalizedMessage ()
    {
      return getCause ().getLocalizedMessage ();
    }

    @Override
    public StackTraceElement [] getStackTrace ()
    {
      return getCause ().getStackTrace ();
    }
  }

  protected final Object wrapUndeclared (final Throwable o, final boolean hasDeclaredExceptions)
  {
    if (hasDeclaredExceptions || bridge.options.preferValues ())
      return o;
    bridge.setLastAsyncException (o);
    bridge.warn (MSG + " " + o);
    return new UndeclaredThrowableErrorMarker (o);
  }

  protected abstract class DelegateWriter
  {
    protected Class <?> staticType;

    public abstract boolean setResult (Object value);

    /**
     * @param type
     *        - The result type
     */
    public void setType (final Class <?> type)
    {
      this.staticType = type;
    }
  }

  protected abstract class Writer extends DelegateWriter
  {
    protected boolean hasDeclaredExceptions;

    public boolean isAsync ()
    {
      return false;
    }

    public void setResultProcedure (final long object, final String cname, final String name, final Object [] args)
    {
      final int argsLength = args == null ? 0 : args.length;
      writeApplyBegin (object, cname, name, argsLength);
      for (int i = 0; i < argsLength; i++)
      {
        writePairBegin ();
        setResult (args[i], args[i].getClass (), true); // PHP backed methods
                                                        // are always
                                                        // synchronous, they
                                                        // don't need to declare
                                                        // that they throw
                                                        // exceptions
        writePairEnd ();
      }
      writeApplyEnd ();
    }

    public void setResultException (final Throwable o, final boolean hasDeclaredExceptions)
    {
      this.hasDeclaredExceptions = hasDeclaredExceptions;
      writeException (wrapUndeclared (o, hasDeclaredExceptions), hasDeclaredExceptions);
    }

    public void setResultObject (final Object value)
    {
      writeObject (value);
    }

    public void setResultClass (final Class <?> value)
    {
      writeClass (value);
    }

    public void setResult (final Object value, final Class <?> type, final boolean hasDeclaredExceptions)
    {
      this.hasDeclaredExceptions = hasDeclaredExceptions;
      setType (type);
      setResult (value);
    }

    public void setFinish (final boolean keepAlive)
    {
      writeFinish (keepAlive);
    }

    public void reset ()
    {
      writer = currentWriter;
      buf.reset ();
    }

    /**
     * Called at the end of each packed.
     * 
     * @throws IOException
     */
    public void flush () throws IOException
    {
      if (bridge.logLevel >= 4)
      {
        bridge.logDebug (" <-- " + newString (buf.getFirstBytes ()));
      }
      buf.writeTo (bridge.out);
      bridge.getFactory ().flushBuffer ();

      reset ();
    }
  }

  protected abstract class WriterWithDelegate extends Writer
  {
    protected DelegateWriter delegate;

    @Override
    public void setType (final Class <?> type)
    {
      super.setType (type);
      delegate.setType (type);
    }
  }

  protected class ArrayWriter extends DelegateWriter
  {
    @Override
    public boolean setResult (final Object value)
    {
      return false;
    }
  }

  protected class ArrayValuesWriter extends DelegateWriter
  {
    @Override
    public boolean setResult (final Object value)
    {
      if (value.getClass ().isArray ())
      {
        final long length = Array.getLength (value);
        writeCompositeBegin_a ();
        for (int i = 0; i < length; i++)
        {
          writePairBegin ();
          writer.setResult (Array.get (value, i));
          writePairEnd ();
        }
        writeCompositeEnd ();
      }
      else
        if (value instanceof java.util.Map)
        {
          final Map <?, ?> ht = (Map <?, ?>) value;
          writeCompositeBegin_h ();
          for (final Map.Entry <?, ?> entry : ht.entrySet ())
          {
            final Object key = entry.getKey ();
            final Object val = entry.getValue ();
            if (key instanceof Number && !(key instanceof Double || key instanceof Float))
            {
              writePairBegin_n (((Number) key).intValue ());
              writer.setResult (val);
            }
            else
            {
              writePairBegin_s (String.valueOf (key));
              writer.setResult (ht.get (key));
            }
            writePairEnd ();
          }
          writeCompositeEnd ();
        }
        else
          if (value instanceof Collection)
          {
            final Collection <?> ht = (Collection <?>) value;
            writeCompositeBegin_h ();
            int counter = 0;
            for (final Object val : ht)
            {
              writePairBegin_n (counter++);
              writer.setResult (val);
              writePairEnd ();
            }
            writeCompositeEnd ();
          }
          else
          {
            return false;
          }
      return true;
    }
  }

  protected abstract class IncompleteClassicWriter extends WriterWithDelegate
  {
    @Override
    public boolean setResult (final Object value)
    {
      if (value == null)
      {
        writeNull ();
      }
      else
        if (value instanceof byte [])
        {
          writeString ((byte []) value);
        }
        else
          if (value instanceof String)
          {
            writeString ((String) value);
          }
          else
            if (value instanceof PhpString)
            {
              writeString (((PhpString) value).getBytes ());
            }
            else
              if (value instanceof Number)
              {
                if (value instanceof Integer || value instanceof Short || value instanceof Byte)
                {
                  writeLong (((Number) value).longValue ());
                }
                else
                {
                  /* Float, Double, BigDecimal, BigInteger, Double, Long, ... */
                  writeDouble (((Number) value).doubleValue ());
                }
              }
              else
                if (value instanceof Boolean)
                {
                  writeBoolean (((Boolean) value).booleanValue ());
                }
                else
                {
                  return false;
                }
      return true;
    }
  }

  protected abstract class IncompleteArrayValueWriter extends WriterWithDelegate
  {

    @Override
    public boolean setResult (final Object value)
    {
      if (value == null)
      {
        writeNull ();
      }
      else
        if (value instanceof PhpString)
        {
          writeString (((PhpString) value).getBytes ());
        }
        else
          if (value instanceof PhpExactNumber)
          {
            writeLong (((PhpExactNumber) value).longValue ());
          }
          else
          {
            return false;
          }
      return true;
    }
  }

  protected class ClassicWriter extends IncompleteClassicWriter
  {

    @Override
    public boolean setResult (final Object value)
    {
      if (super.setResult (value))
        return true;
      if (!delegate.setResult (value))
        writeObject (value);
      return true;
    }
  }

  protected class DefaultWriter extends WriterWithDelegate
  {

    @Override
    public boolean setResult (final Object value)
    {
      if (staticType.isPrimitive ())
      {
        if (staticType == Boolean.TYPE)
          writeBoolean (((Boolean) value).booleanValue ());
        else
          if (staticType == Byte.TYPE || staticType == Short.TYPE || staticType == Integer.TYPE)
            writeLong (((Number) value).longValue ());
          else
            if (staticType == Long.TYPE)
              writeDouble (((Number) value).doubleValue ());
            else
              if (staticType == Float.TYPE || staticType == Double.TYPE)
                writeDouble (((Number) value).doubleValue ());
              else
                if (staticType == Character.TYPE)
                  writeString (Util.stringValueOf (value));
                else
                  if (staticType == java.lang.Void.TYPE)
                    writeVoid ();
                  else
                  {
                    Util.logFatal ("Unknown type");
                    writeObject (value);
                  }
      }
      else
        if (value instanceof PhpString)
        {// No need to check for Request.PhpNumber, this cannot happen.
          writeString (((PhpString) value).getBytes ());
        }
        else
          if (!delegate.setResult (value))
          {
            writeObject (value);
          }
      return true;
    }
  }

  protected class ArrayValueWriter extends IncompleteArrayValueWriter
  {
    @Override
    public void setResult (final Object value, final Class <?> type, final boolean hasDeclaredExceptions)
    {
      if (!delegate.setResult (value))
        setResultArray (value);
    }

    @Override
    public boolean setResult (final Object value)
    {
      if (!super.setResult (value))
        writeObject (value);
      return true;
    }

    private boolean setResultArray (final Object value)
    {
      writeCompositeBegin_a ();
      writePairBegin ();
      setResult (value);
      writePairEnd ();
      writeCompositeEnd ();
      return true;
    }
  }

  /**
   * Writer used by the async protocol. It always returns <V ...> or <O ...>,
   * even for NULL, Class values, although Exceptions are handled. When the
   * client-side cache is enabled, the client will select an Async or
   * AsyncVoidWriter in the next call.
   */
  protected class DefaultObjectWriter extends Writer
  {
    @Override
    public void setResultObject (final Object value)
    {
      if (staticType == java.lang.Void.TYPE)
      {
        writeVoid (hasDeclaredExceptions);
        return;
      }

      writeObject (value == null ? Request.PHPNULL : value, hasDeclaredExceptions);
    }

    @Override
    public void setResultClass (final Class <?> value)
    {
      writeClass (value == null ? Request.PhpNull.class : value, hasDeclaredExceptions);
    }

    @Override
    public boolean setResult (final Object value)
    {
      setResultObject (value);
      return true;
    }
  }

  /**
   * Writer used by the async protocol (begin/end document). It always returns
   * <V ...> or <O ...>, even for NULL, Class or Exception values. When the
   * client-side cache is enabled, the client will select an Async or
   * AsyncVoidWriter in the next call.
   */
  protected final class ObjectWriter extends DefaultObjectWriter
  {
    @Override
    public void setResultException (final Throwable o, final boolean hasDeclaredExceptions)
    {
      this.hasDeclaredExceptions = hasDeclaredExceptions;
      setResultObject (wrapUndeclared (o, hasDeclaredExceptions));
    }
  }

  /**
   * Writer used by the async protocol. It writes nothing but stores the result
   * in global ref
   */
  protected final class AsyncWriter extends Writer
  {
    @Override
    public boolean isAsync ()
    {
      return true;
    }

    @Override
    public void setResultException (final Throwable o, final boolean hasDeclaredExceptions)
    {
      this.hasDeclaredExceptions = hasDeclaredExceptions;
      setResultObject (wrapUndeclared (o, hasDeclaredExceptions));
    }

    @Override
    public void setResultObject (final Object pvalue)
    {
      if (staticType == java.lang.Void.TYPE)
        throw new IllegalStateException ("Use the AsyncVoidWriter instead");

      Object value = pvalue;
      if (value == null)
        value = Request.PHPNULL;
      if (bridge.logLevel >= 4)
      {
        writeObject (value);
      }
      else
      {
        bridge.globalRef.append (value);
      }
    }

    @Override
    public void setResultClass (final Class <?> pvalue)
    {
      Class <?> value = pvalue;
      if (value == null)
        value = Request.PhpNull.class;
      if (bridge.logLevel >= 4)
      {
        writeClass (value);
      }
      else
      {
        bridge.globalRef.append (value);
      }
    }

    @Override
    public boolean setResult (final Object value)
    {
      setResultObject (value);
      return true;
    }

    /**
     * Called at the end of each packed.
     * 
     * @throws IOException
     */
    @Override
    public void flush () throws IOException
    {
      if (bridge.logLevel >= 4)
      {
        bridge.logDebug (" |<- " + newString (buf.getFirstBytes ()));
      }
      reset ();
    }
  }

  /**
   * Writer used by the async protocol. It writes nothing and doesn't create a
   * result
   */
  protected final class AsyncVoidWriter extends Writer
  {
    @Override
    public boolean isAsync ()
    {
      return true;
    }

    @Override
    public void setResultProcedure (final long object, final String cname, final String name, final Object [] args)
    {
      throw new IllegalStateException ("Cannot call " + name + ": callbacks not allowed in stream mode");
    }

    @Override
    public void setResultException (final Throwable o, final boolean hasDeclaredExceptions)
    {
      this.hasDeclaredExceptions = hasDeclaredExceptions;
      wrapUndeclared (o, hasDeclaredExceptions);
    }

    @Override
    public void setResultObject (final Object value)
    {}

    @Override
    public void setResultClass (final Class <?> value)
    {}

    @Override
    public boolean setResult (final Object value)
    {
      return true;
    }

    @Override
    public void flush () throws IOException
    {
      if (bridge.logLevel >= 4)
      {
        bridge.logDebug (" |<- <NONE>");
      }
      reset ();
    }
  }

  protected final class CoerceWriter extends Writer
  {
    @Override
    public void setResult (final Object value, final Class <?> resultType, final boolean hasDeclaredExceptions)
    {
      // ignore resultType and use the coerce type
      setResult (value);
    }

    @Override
    public boolean setResult (final Object pvalue)
    {
      Object value = pvalue;
      if (value instanceof PhpString)
        value = ((PhpString) value).getString ();

      if (staticType.isPrimitive ())
      {
        if (staticType == Boolean.TYPE)
        {
          if (value instanceof Boolean)
            writeBoolean (((Boolean) value).booleanValue ());
          else
          {
            if (value == null)
            {
              writeBoolean (false);
            }
            else
              if (value instanceof byte [])
              {
                writeBoolean (((byte []) value).length != 0);
              }
              else
                if (value instanceof java.lang.String)
                {
                  writeBoolean (((String) value).length () != 0);
                }
                else
                  if (value instanceof PhpString)
                  {
                    writeBoolean (((PhpString) value).getBytes ().length != 0);
                  }
                  else
                    if (value instanceof java.lang.Number)
                    {
                      if (value instanceof java.lang.Integer ||
                          value instanceof java.lang.Short ||
                          value instanceof java.lang.Byte)
                      {
                        writeBoolean (((Number) value).longValue () != 0);
                      }
                      else
                      {
                        /*
                         * Float, Double, BigDecimal, BigInteger, Double, Long,
                         * ...
                         */
                        writeBoolean (((Number) value).doubleValue () != 0.0);
                      }
                    }
                    else
                    {
                      writeBoolean (true);
                    }
          }
        }
        else
          if (staticType == Byte.TYPE ||
              staticType == Short.TYPE ||
              staticType == Integer.TYPE ||
              staticType == Long.TYPE)
          {
            if (value instanceof Number)
              writeLong (((Number) value).longValue ());
            else
            {
              try
              {
                writeLong (Long.valueOf (Util.stringValueOf (value)).longValue ());
              }
              catch (final NumberFormatException n)
              {
                writeLong (0);
              }
            }
          }
          else
            if (staticType == Float.TYPE || staticType == Double.TYPE)
            {
              if (value instanceof Number)
                writeDouble (((Number) value).doubleValue ());
              else
              {
                try
                {
                  writeDouble (Double.valueOf (Util.stringValueOf (value)).doubleValue ());
                }
                catch (final NumberFormatException n)
                {
                  writeDouble (0.0);
                }
              }
            }
            else
              if (staticType == Character.TYPE)
              {
                writeString (Util.stringValueOf (value));
              }
              else
                if (staticType == java.lang.Void.TYPE)
                {
                  writeVoid ();
                }
                else
                {
                  Util.logFatal ("Unknown type");
                  writeObject (value);
                }
      }
      else
        if (staticType == String.class)
        {
          if (value instanceof byte [])
            writeString ((byte []) value);
          else
            writeString (Util.stringValueOf (value));
        }
        else
        {
          writeObject (value);
        }
      return true;
    }
  }

  private DelegateWriter getDefaultDelegate ()
  {
    return new ArrayWriter ();
  }

  private Writer getDefaultWriter ()
  {
    if (bridge.options.preferValues ())
    {
      WriterWithDelegate writer;
      writer = new DefaultWriter ();
      writer.delegate = getDefaultDelegate ();
      return writer;
    }
    return getDefaultObjectWriter ();
  }

  private Writer defaultWriter;
  private Writer writer, currentWriter, arrayValuesWriter = null, arrayValueWriter = null, coerceWriter = null;
  private Writer asyncWriter = null, asyncVoidWriter = null, objectWriter = null;

  protected HexOutputBuffer createBase64OutputBuffer ()
  {
    return new Base64OutputBuffer (bridge);
  }

  protected HexOutputBuffer createOutputBuffer ()
  {
    if (bridge.options.base64Data ())
      return createBase64OutputBuffer ();
    return new HexOutputBuffer (bridge);
  }

  /**
   * Creates a new response object. The object is re-used for each packed.
   * 
   * @param bridge
   *        The bridge.
   */
  public Response (final JavaBridge bridge)
  {
    this.bridge = bridge;
    buf = createOutputBuffer ();
    currentWriter = writer = defaultWriter = getDefaultWriter ();
  }

  protected Response (final JavaBridge bridge, final HexOutputBuffer buf)
  {
    this.bridge = bridge;
    this.buf = buf;
    this.currentWriter = this.writer = this.defaultWriter = getDefaultWriter ();
  }

  /**
   * Flush the current output buffer and create a new Response object where are
   * writers have their default value
   * 
   * @return the fresh response
   * @throws IOException
   */
  public Response copyResponse () throws IOException
  {
    flush ();
    return new Response (bridge, buf);
  }

  /**
   * Set the result packet.
   * 
   * @param object
   *        The result object.
   * @param cname
   *        The php name of the procedure
   * @param name
   *        The java name of the procedure
   * @param args
   *        The arguments
   */
  public void setResultProcedure (final long object, final String cname, final String name, final Object [] args)
  {
    writer.setResultProcedure (object, cname, name, args);
  }

  /**
   * Set the result packet.
   * 
   * @param value
   *        The throwable
   * @param hasDeclaredExceptions
   *        true if the method has declared to throw exception(s), false
   *        otherwise
   */
  public void setResultException (final Throwable value, final boolean hasDeclaredExceptions)
  {
    writer.setResultException (value, hasDeclaredExceptions);
  }

  /**
   * Set the result packet.
   * 
   * @param value
   *        The result object.
   */
  public void setResultClass (final Class <?> value)
  {
    writer.setResultClass (value);
  }

  /**
   * Set the result packet.
   * 
   * @param value
   *        The result object.
   * @param type
   *        The type of the result object.
   * @param hasDeclaredExceptions
   *        true if the method/procedure has declared exceptions, false
   *        otherwise
   */
  public void setResult (final Object value, final Class <?> type, final boolean hasDeclaredExceptions)
  {
    writer.setResult (value, type, hasDeclaredExceptions);
  }

  /**
   * Checks whether the asynchronous protocol is used
   * 
   * @return true if the current writer is an AsyncWriter, false otherwise
   */
  public boolean isAsync ()
  {
    return writer.isAsync ();
  }

  protected void setFinish (final boolean keepAlive)
  {
    if (!hasLastAsyncExceptionSet)
      hasLastAsyncException = bridge.lastAsyncException != null;
    setDefaultWriter ();
    writer.setFinish (keepAlive);
    hasLastAsyncException = hasLastAsyncExceptionSet = false;
  }

  /**
   * Selects a specialized writer which writes objects as an array. Used by
   * castToArray().
   * 
   * @see JavaBridge#castToArray(Object)
   */
  protected Writer setArrayValueWriter ()
  {
    return writer = getArrayValueWriter ();
  }

  Writer setArrayValuesWriter ()
  {
    return writer = getArrayValuesWriter ();
  }

  Writer setCoerceWriter ()
  {
    return writer = getCoerceWriter ();
  }

  /**
   * Selects a specialized writer which does not write anything. Used by async.
   * protocol.
   * 
   * @return The async. writer
   */
  public Writer setAsyncWriter ()
  {
    return writer = getAsyncWriter ();
  }

  /**
   * Selects a specialized writer which does not write anything and does not
   * generate a result proxy Used by async. protocol.
   * 
   * @return The async. writer
   */
  public Writer setAsyncVoidWriter ()
  {
    return writer = getAsyncVoidWriter ();
  }

  /**
   * Selects a specialized writer which always writes <O ...> or <V ..>, even
   * for NULL, Class and Exception values. This triggers the client cache so
   * that it selects the AsyncNull or AsyncWriter.
   * 
   * @return The async. writer
   */
  public Writer setObjectWriter ()
  {
    return currentWriter = getObjectWriter ();
  }

  /**
   * Selects the default writer
   * 
   * @return The default writer
   */
  public Writer setDefaultWriter ()
  {
    return writer = currentWriter = defaultWriter;
  }

  /**
   * @param id
   */
  protected final void setID (final long id)
  {}

  static final byte [] SELF_CLOSED = "\"/>".getBytes (Util.ASCII);
  static final byte [] CLOSE = "\">".getBytes (Util.ASCII);
  static final byte [] I = "\" i=\"".getBytes (Util.ASCII);
  static final byte [] STRING = "<S v=\"".getBytes (Util.ASCII);
  static final byte [] BOOLEAN = "<B v=\"".getBytes (Util.ASCII);
  static final byte [] LONG = "<L v=\"".getBytes (Util.ASCII);
  static final byte [] DOUBLE = "<D v=\"".getBytes (Util.ASCII);
  static final byte [] EXCEPTION = "<E v=\"".getBytes (Util.ASCII);
  static final byte [] OBJECT = "<O v=\"".getBytes (Util.ASCII);
  static final byte [] N = "<N i=\"".getBytes (Util.ASCII);
  static final byte [] V = "<V i=\"".getBytes (Util.ASCII);
  static final byte [] NULL = "<N />".getBytes (Util.ASCII);
  static final byte [] VOID_FALSE = "<V n=\"F\"/>".getBytes (Util.ASCII);
  static final byte [] VOID_TRUE = "<V n=\"T\"/>".getBytes (Util.ASCII);
  static final byte [] ATTR_MSG = "\" m=\"".getBytes (Util.ASCII);
  static final byte [] M_TRUE = "\" m=\"T".getBytes (Util.ASCII);
  static final byte [] M_FALSE = "\" m=\"F".getBytes (Util.ASCII);
  static final byte [] ATTR_NUM = "\" n=\"".getBytes (Util.ASCII);
  static final byte [] nT = "\" n=\"T".getBytes (Util.ASCII);
  static final byte [] nF = "\" n=\"F".getBytes (Util.ASCII);
  static final byte [] APPLY_POS = "\" p=\"".getBytes (Util.ASCII);
  static final byte [] pa = "\" p=\"A".getBytes (Util.ASCII);
  static final byte [] pc = "\" p=\"C".getBytes (Util.ASCII);
  static final byte [] pe = "\" p=\"E".getBytes (Util.ASCII);
  static final byte [] po = "\" p=\"O".getBytes (Util.ASCII);
  static final byte [] COMPOSITE_BEGIN_a = "<X t=\"A".getBytes (Util.ASCII);
  static final byte [] COMPOSITE_BEGIN_h = "<X t=\"H".getBytes (Util.ASCII);
  static final byte [] COMPOSITE_END = "</X>".getBytes (Util.ASCII);
  static final byte [] APPLY_BEGIN = "<A v=\"".getBytes (Util.ASCII);
  static final byte [] APPLY_END = "</A>".getBytes (Util.ASCII);
  static final byte [] PAIR_BEGIN = "<P>".getBytes (Util.ASCII);
  static final byte [] PAIR_BEGIN_n = "<P t=\"N\" v=\"".getBytes (Util.ASCII);
  static final byte [] PAIR_BEGIN_s = "<P t=\"S\" v=\"".getBytes (Util.ASCII);
  static final byte [] PAIR_END = "</P>".getBytes (Util.ASCII);
  static final byte [] FINISHA = "<F p=\"A\"/>".getBytes (Util.ASCII);
  static final byte [] FINISHa = "<F p=\"a\"/>".getBytes (Util.ASCII);
  static final byte [] FINISHE = "<F p=\"E\"/>".getBytes (Util.ASCII);
  static final byte [] FINISHe = "<F p=\"e\"/>".getBytes (Util.ASCII);
  static final byte [] quote = "&quot;".getBytes (Util.ASCII);
  static final byte [] amp = "&amp;".getBytes (Util.ASCII);

  void writeString (final byte [] s)
  {
    buf.appendString (s);
    buf.append (SELF_CLOSED);
  }

  void writeString (final String s)
  {
    writeString (bridge.options.getBytes (s));
  }

  void writeBoolean (final boolean b)
  {
    buf.append (BOOLEAN);
    buf.write (b == true ? 'T' : 'F');
    buf.append (SELF_CLOSED);
  }

  void writeLong (final long l)
  {
    buf.appendLong (l);
    buf.append (SELF_CLOSED);
  }

  void writeDouble (final double d)
  {
    buf.append (DOUBLE);
    buf.append (d);
    buf.append (SELF_CLOSED);
  }

  void writeVoid (final boolean hasDeclaredExceptions)
  {
    buf.append (hasDeclaredExceptions ? VOID_TRUE : VOID_FALSE);
  }

  void writeVoid ()
  {
    buf.append (VOID_TRUE);
  }

  void writeNull ()
  {
    buf.append (NULL);
  }

  protected byte [] getType (final Class <?> type)
  {
    if (type.isArray () || List.class.isAssignableFrom (type) || Map.class.isAssignableFrom (type))
    {
      return pa;
    }
    else
      if (Collection.class.isAssignableFrom (type))
      {
        return pc;
      }
      else
        if (Throwable.class.isAssignableFrom (type))
        {
          return pe;
        }
    return po;
  }

  /** write object (class, interface, object) */
  void writeObject (final Object o, final boolean hasDeclaredExceptions)
  {
    if (o == null)
    {
      writeNull ();
      return;
    }
    final Class <?> dynamicType = o.getClass ();
    buf.append (OBJECT);
    buf.append (this.bridge.globalRef.append (o));
    buf.append (ATTR_MSG);
    buf.append (Util.toBytes (dynamicType.getName ()));
    buf.append (getType (dynamicType));
    buf.append (hasDeclaredExceptions ? nT : nF);
    buf.append (SELF_CLOSED);
  }

  void writeObject (final Object o)
  {
    if (o == null)
    {
      writeNull ();
      return;
    }
    final Class <?> dynamicType = o.getClass ();
    buf.append (OBJECT);
    buf.append (this.bridge.globalRef.append (o));
    buf.append (ATTR_MSG);
    buf.append (Util.toBytes (dynamicType.getName ()));
    buf.append (getType (dynamicType));
    buf.append (nT);
    buf.append (SELF_CLOSED);
  }

  /** write object of type class. Only called from new JavaClass(...) */
  void writeClass (final Class <?> o, final boolean hasDeclaredExceptions)
  {
    if (o == null)
    {
      writeNull ();
      return;
    }
    buf.append (OBJECT);
    buf.append (this.bridge.globalRef.append (o));
    buf.append (ATTR_MSG);
    buf.append (Util.toBytes (o.getName ()));
    buf.append (po);
    buf.append (hasDeclaredExceptions ? nT : nF);
    buf.append (SELF_CLOSED);
  }

  void writeClass (final Class <?> o)
  {
    if (o == null)
    {
      writeNull ();
      return;
    }
    buf.append (OBJECT);
    buf.append (this.bridge.globalRef.append (o));
    buf.append (ATTR_MSG);
    buf.append (Util.toBytes (o.getName ()));
    buf.append (po);
    buf.append (nT);
    buf.append (SELF_CLOSED);
  }

  void writeException (final Object o, final boolean hasDeclaredExceptions)
  {
    buf.append (EXCEPTION);
    buf.append (this.bridge.globalRef.append (o));
    buf.append (hasDeclaredExceptions ? M_TRUE : M_FALSE);
    buf.append (SELF_CLOSED);
  }

  void writeFinish (final boolean keepAlive)
  {
    if (hasLastAsyncException)
    {
      buf.append (keepAlive ? FINISHa : FINISHe);
    }
    else
    {
      buf.append (keepAlive ? FINISHA : FINISHE);
    }
  }

  void writeCompositeBegin_a ()
  {
    buf.append (COMPOSITE_BEGIN_a);
    buf.append (CLOSE);
  }

  void writeCompositeBegin_h ()
  {
    buf.append (COMPOSITE_BEGIN_h);
    buf.append (CLOSE);
  }

  void writeCompositeEnd ()
  {
    buf.append (COMPOSITE_END);
  }

  void writePairBegin_s (final String key)
  {
    buf.append (PAIR_BEGIN_s);
    buf.appendQuoted (key);
    buf.append (CLOSE);
  }

  void writePairBegin_n (final int key)
  {
    buf.append (PAIR_BEGIN_n);
    buf.append (key);
    buf.append (CLOSE);
  }

  void writePairBegin ()
  {
    buf.append (PAIR_BEGIN);
  }

  void writePairEnd ()
  {
    buf.append (PAIR_END);
  }

  void writeApplyBegin (final long object, final String pos, final String str, final int argCount)
  {
    buf.append (APPLY_BEGIN);
    buf.append (object);
    buf.append (APPLY_POS);
    buf.appendQuoted (pos);
    buf.append (ATTR_MSG);
    buf.appendQuoted (str);
    buf.append (ATTR_NUM);
    buf.append (argCount);
    buf.append (CLOSE);
  }

  void writeApplyEnd ()
  {
    buf.append (APPLY_END);
  }

  /**
   * Write the response.
   * 
   * @throws IOException
   */
  public void flush () throws IOException
  {
    writer.flush ();
  }

  /**
   * Called at the end of each packed.
   */
  protected void reset ()
  {
    writer.reset ();
  }

  /**
   * Set the current bridge object
   * 
   * @param bridge
   *        The bridge
   */
  protected void setBridge (final JavaBridge bridge)
  {
    this.bridge = bridge;
  }

  /** re-initialize for keep alive */
  protected void recycle ()
  {
    hasLastAsyncException = bridge.lastAsyncException != null;
    hasLastAsyncExceptionSet = true;

    reset ();
    setDefaultWriter ();
  }

  /** {@inheritDoc} */
  @Override
  public String toString ()
  {
    return newString (buf.getFirstBytes ());
  }

  private String newString (final byte [] b)
  {
    return bridge.getString (b, 0, b.length);
  }

  private Writer getArrayValuesWriter ()
  {
    if (arrayValuesWriter == null)
    {
      final WriterWithDelegate writer = new ClassicWriter ();
      writer.delegate = new ArrayValuesWriter ();
      arrayValuesWriter = writer;
    }
    return arrayValuesWriter;
  }

  private Writer getArrayValueWriter ()
  {
    if (arrayValueWriter == null)
    {
      final WriterWithDelegate writer = new ArrayValueWriter ();
      writer.delegate = new ArrayValuesWriter ();
      arrayValueWriter = writer;
    }
    return arrayValueWriter;
  }

  private Writer getCoerceWriter ()
  {
    if (coerceWriter == null)
      return coerceWriter = new CoerceWriter ();
    return coerceWriter;
  }

  private Writer getDefaultObjectWriter ()
  {
    if (objectWriter == null)
      return objectWriter = new DefaultObjectWriter ();
    return objectWriter;
  }

  private Writer getObjectWriter ()
  {
    if (objectWriter == null)
      return objectWriter = new ObjectWriter ();
    return objectWriter;
  }

  private Writer getAsyncWriter ()
  {
    if (asyncWriter == null)
      return asyncWriter = new AsyncWriter ();
    return asyncWriter;
  }

  private Writer getAsyncVoidWriter ()
  {
    if (asyncVoidWriter == null)
      return asyncVoidWriter = new AsyncVoidWriter ();
    return asyncVoidWriter;
  }
}
