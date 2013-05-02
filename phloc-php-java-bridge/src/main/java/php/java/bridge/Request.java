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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to handle requests from the front-end.
 * 
 * @author jostb
 */
public final class Request implements IDocHandler
{
  private final Parser parser;
  private JavaBridge defaultBridge, bridge;
  protected static final IntegerComparator PHP_ARRAY_KEY_COMPARATOR = new IntegerComparator ();

  // Only used when the async. protocol is enabled.
  protected static final class PhpNull
  {
    @Override
    public String toString ()
    {
      return ""; // evaluates to false
    }
  }

  protected static final PhpNull PHPNULL = new PhpNull ();

  protected Object getGlobalRef (final int i)
  {
    final Object ref = bridge.globalRef.get (i);
    if (ref == PHPNULL)
      return null;
    return ref;
  }

  static final Object [] ZERO_ARGS = new Object [0];

  private static class SimpleContext
  {
    /**
     * @param string
     */
    public void parseID (final ParserString string)
    {}

    /**
     * @param response
     */
    public void setID (final Response response)
    {}
  }

  private static class Context extends SimpleContext
  {
    private long id;

    @Override
    public void parseID (final ParserString string)
    {
      this.id = string.getLongValue ();
    }

    @Override
    public void setID (final Response response)
    {
      response.setID (id);
    }
  }

  private SimpleContext contextCache;

  SimpleContext getContext ()
  {
    if (contextCache != null)
      return contextCache;
    if (bridge.options.passContext ())
    {
      return contextCache = new Context ();
    }
    return contextCache = new SimpleContext ();
  }

  private abstract class Arg
  {
    protected byte type;
    protected Object callObject;
    protected String method;
    protected SimpleContext id = getContext ();
    protected byte predicate;
    protected Object key;
    protected byte composite;

    public abstract void add (Object val);

    public abstract Object [] getArgs ();

    public abstract void reset ();
  }

  private final class SimpleArg extends Arg
  {
    private List <Object> list;

    @Override
    public void add (final Object val)
    {
      if (list == null)
        list = new ArrayList <Object> ();
      list.add (val);
    }

    @Override
    public Object [] getArgs ()
    {
      return (list == null) ? Request.ZERO_ARGS : list.toArray ();
    }

    @Override
    public void reset ()
    {
      list = null;
      composite = 0;
      type = 0;
      callObject = null;
      method = null;
      key = null;
    }
  }

  private final class CompositeArg extends Arg
  {
    private PhpArray ht = null;
    private int count = 0;
    private final Arg parent;

    public CompositeArg (final Arg parent)
    {
      this.parent = parent;
    }

    @Override
    public void add (final Object val)
    {
      if (ht == null)
        ht = new PhpArray ();
      if (key != null)
      {
        ht.put (key, val);
      }
      else
      {
        ht.put (Integer.valueOf (count++), val);
      }

    }

    protected Arg pop ()
    {
      if (ht == null)
        ht = new PhpArray ();
      parent.add (ht);
      return parent;
    }

    /*
     * (non-Javadoc)
     * @see php.java.bridge.Request.Arg#getArgs()
     */
    @Override
    public Object [] getArgs ()
    {
      bridge.logError ("Protocol error: getArgs");
      return ZERO_ARGS;
    }

    /*
     * (non-Javadoc)
     * @see php.java.bridge.Request.Arg#reset()
     */
    @Override
    public void reset ()
    {
      bridge.logError ("Protocol error: reset");
    }
  }

  private Arg arg;

  /**
   * The current response handle or null. There is only one response handle for
   * each request object. <code>response.reset()</code> or
   * <code>response.flush()</code> must be called at the end of each packet.
   */
  Response response = null;

  /**
   * Creates an empty request object.
   * 
   * @param bridge
   *        The bridge instance.
   * @see Request#init(InputStream, OutputStream)
   */
  public Request (final JavaBridge bridge)
  {
    this.bridge = bridge;
    this.parser = new Parser (bridge, this);
  }

  static final byte [] ZERO = { 0 };
  static final Object ZERO_OBJECT = new Object ();

  /**
   * This method must be called with the current header option byte. It
   * initializes the request object.
   * 
   * @param ch
   *        the current php options.
   */
  public void init (final byte ch)
  {
    parser.initOptions (ch);
  }

  /**
   * This method must be called with the current input and output streams. It
   * reads the protocol header and initializes the request object.
   * 
   * @param in
   *        The input stream.
   * @param out
   *        The output stream.
   * @return true, if the protocol header was valid, false otherwise.
   * @throws IOException
   */
  public boolean init (final InputStream in, final OutputStream out) throws IOException
  {
    switch (parser.initOptions (in, out))
    {
      case Parser.PING:
        bridge.logDebug ("PING - PONG - Closing Request");
        out.write (ZERO, 0, 1);
        return false;
      case Parser.IO_ERROR:
        bridge.logDebug ("IO_ERROR - Closing Request");
        return false;
      case Parser.EOF:
        bridge.logDebug ("EOF - Closing Request");
        return false;
    }
    return true;
  }

  private long getClassicPhpLong (final ParserString st[])
  {
    return st[0].getClassicLongValue ();
  }

  private Object createClassicExact (final ParserString st[])
  {
    return Integer.valueOf (st[0].getClassicIntValue ());
  }

  private long getPhpLong (final ParserString st[])
  {
    long val = st[0].getLongValue ();
    if (st[1].string[st[1].off] != 'O')
      val *= -1;
    return val;
  }

  private Object createExact (final ParserString st[])
  {
    {
      int val = st[0].getIntValue ();
      if (st[1].string[st[1].off] != 'O')
        val *= -1;
      return (Integer.valueOf (val));
    }
  }

  /** {@inheritDoc} */
  public boolean begin (final ParserTag [] tag)
  {
    boolean reply = true;
    final ParserString [] st = tag[2].strings;
    byte ch;
    switch (ch = tag[0].strings[0].string[0])
    {
      case 'G':
      case 'Y':
      {
        arg.type = ch;
        arg.predicate = st[0].string[st[0].off];
        final int i = st[1].getIntValue ();
        arg.callObject = i == 0 ? bridge : getGlobalRef (i);
        arg.method = st[2].getCachedStringValue ();
        arg.id.parseID (st[3]);
        break;
      }
      case 'I':
      {
        arg.type = ch;
        final int i = st[0].getIntValue ();
        arg.callObject = i == 0 ? bridge : getGlobalRef (i);
        arg.method = st[1].getCachedStringValue ();
        arg.predicate = st[2].string[st[2].off];
        arg.id.parseID (st[3]);
        break;
      }
      case 'H':
      case 'K':
      {
        arg.type = ch;
        arg.predicate = st[0].string[st[0].off];
        arg.callObject = st[1].getCachedStringValue ();
        arg.id.parseID (st[2]);
        break;
      }
      case 'C':
      {
        arg.type = ch;
        arg.callObject = st[0].getCachedStringValue ();
        arg.predicate = st[1].string[st[1].off];
        arg.id.parseID (st[2]);
        break;
      }
      case 'F':
      {
        arg.type = ch;
        arg.predicate = st[0].string[st[0].off];
        break;
      }
      case 'R':
      {
        arg.type = ch;
        arg.id.parseID (st[0]);
        break;
      }

      case 'X':
      {
        arg = new CompositeArg (arg);
        arg.composite = st[0].string[st[0].off];
        break;
      }
      case 'P':
      {
        if (arg.composite == 'H')
        {// hash
          if (st[0].string[st[0].off] == 'S')
            arg.key = st[1].getCachedStringValue ();
          else
          {
            arg.key = Integer.valueOf (st[1].getIntValue ());
          }
        }
        else
          // array
          arg.key = null;
        break;
      }

      case 'U':
      {
        final int i = st[0].getIntValue ();
        if (Util.logLevel > 4)
        {
          final Object obj = bridge.globalRef.get (i);
          if (!(obj instanceof java.lang.reflect.Proxy))
            Util.logDebug ("unref: " + obj);
        }
        bridge.globalRef.remove (i);
        reply = false; // U is the only top-level request which doesn't need a
                       // reply
        break;
      }
      case 'S':
        if (tag[2].n > 0)
        { // handle <S v=... />
          if (arg.composite != 'H')
            arg.add (new PhpParserString (bridge, st[0]));
          else
            // hash has no type information
            arg.add (st[0].getStringValue ());
        }
        break;
      case 'B':
      {
        arg.add (new Boolean (st[0].string[st[0].off] == 'T'));
        break;
      }
      case 'T':
      {
        arg.add (new Boolean (st[0].string[st[0].off] == '1'));
        break;
      }
      case 'L':
      {
        if (arg.composite != 'H')
          arg.add (new PhpExactNumber (getPhpLong (st)));
        else
          // hash has no type information
          arg.add (createExact (st));
        break;
      }
      case 'J':
      {
        if (arg.composite != 'H')
          arg.add (new PhpExactNumber (getClassicPhpLong (st)));
        else
          // hash has no type information
          arg.add (createClassicExact (st));
        break;
      }
      case 'D':
      {
        arg.add (Double.valueOf (st[0].getDoubleValue ()));
        break;
      }
      case 'E':
      {
        if (0 == st[0].length)
          arg.callObject = new Exception (st[1].getStringValue ());
        else
        {
          final int i = st[0].getIntValue ();
          if (0 == i)
          {
            arg.callObject = new Exception (st[1].getStringValue ());
          }
          else
            arg.callObject = getGlobalRef (i);
        }
        break;
      }
      case 'O':
      {
        if (0 == st[0].length)
          arg.add (null);
        else
        {
          final int i = st[0].getIntValue ();
          if (0 == i)
            arg.add (null);
          else
            arg.add (getGlobalRef (i));
        }
        break;
      }
    }
    return reply;
  }

  /** {@inheritDoc} */
  public void end (final ParserString [] string)
  {
    switch (string[0].string[0])
    {
      case 'X':
      {
        arg = ((CompositeArg) arg).pop ();
      }
    }
  }

  private static final String SUB_FAILED = "PHP callback execution failed.";

  private void setIllegalStateException (final String s)
  {
    final IllegalStateException ex = new IllegalStateException (s);
    // PHP callbacks always generate a round-trip anyway
    response.setResultException (bridge.lastException = ex, true);
  }

  private int handleRequest () throws IOException
  {
    int retval;
    if (Parser.OK == (retval = parser.parse (bridge.in)))
    {
      arg.id.setID (response);
      switch (arg.type)
      {
        case 'I':
          try
          {
            switch (arg.predicate)
            {
              case 'P':
                bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              case 'I':
                bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'G':
          try
          {
            switch (arg.predicate)
            {
              case '2':
                response.setAsyncWriter ();
              case '1':
                bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              case '3':
                response.setAsyncVoidWriter ();
                bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'Y':
          try
          {
            switch (arg.predicate)
            {
              case '2':
                response.setAsyncWriter ();
              case '1':
                bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              case '3':
                response.setAsyncVoidWriter ();
                bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'C':
          try
          {
            switch (arg.predicate)
            {
              case 'C':
                bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
                break;
              case 'I':
                bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'H':
          try
          {
            switch (arg.predicate)
            {
              case '2':
                response.setAsyncWriter ();
              case '1':
                bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
                break;
              case '3':
                response.setAsyncVoidWriter ();
                bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'K':
          try
          {
            switch (arg.predicate)
            {
              case '2':
                response.setAsyncWriter ();
              case '1':
                bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
                break;
              case '3':
                response.setAsyncVoidWriter ();
                bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
                break;
              default:
                throw new IOException ("Protocol error");
            }
            response.flush ();
          }
          catch (final AbortException sub)
          {
            Util.printStackTrace (sub);
            retval = Parser.EOF;
          }
          break;
        case 'F':
          final IJavaBridgeFactory factory = bridge.getFactory ();
          if (arg.predicate == 'A')
          { // keep alive
            bridge.recycle ();
            try
            {
              ((AppThreadPool.Delegate) Thread.currentThread ()).setPersistent ();
            }
            catch (final ClassCastException ex)
            {/* no thread pool */}
            response.setFinish (true);
          }
          else
          { // terminate or terminate keep alive
            response.setFinish (false);
            retval = Parser.EOF;
          }
          response.flush ();
          factory.invalidate ();
          break;
        case 'R': // should not happen
          setIllegalStateException (SUB_FAILED);
          response.flush ();
          break;
      }
      arg.reset ();
    }
    return retval;
  }

  /**
   * Start handling requests until EOF. Creates a response object and handles
   * all packets.
   * 
   * @throws IOException
   */
  public void handleRequests () throws IOException
  {
    if (response == null)
      response = bridge.createResponse ();
    this.arg = new SimpleArg ();
    while (Parser.OK == handleRequest ())
    {}
  }

  /**
   * Start handling one request. Creates a response object and handles one
   * packet.
   * 
   * @throws IOException
   */
  public boolean handleOneRequest () throws IOException
  {
    if (response == null)
      response = bridge.createResponse ();
    this.arg = new SimpleArg ();
    return (Parser.OK == handleRequest ());
  }

  /**
   * This exception isn't an exception but a construct to emulate a one-shot
   * continuation in Java. It is used to quickly clear the stack and to jumb
   * back to some top-level loop.
   * <p>
   * The exception is thrown when the client exits while running a callback, it
   * aborts everything and jumps back to the top-level request-handling loop,
   * see handleRequests() above.
   * </p>
   * <p>
   * Do not catch this exception! If you must catch Exception(), check for
   * AbortException. If you invoke using the reflection interface, check for the
   * wrapped exception, unwrap it, if necessary and re-throw the exception
   * </p>
   * 
   * @see Request#handleRequest()
   */
  public static class AbortException extends RuntimeException
  {
    private static final long serialVersionUID = 7778150395848350732L;
  }

  /**
   * When within a sub request, handle an IOException as EOF
   * 
   * @return EOF if an IOException or EOF occurred, otherwise the parser status
   *         is returned
   */
  private short parseSubRequestUntilEof ()
  {
    try
    {
      return parser.parse (bridge.in);
    }
    catch (final IOException e)
    {
      bridge.printStackTrace (e);
      return Parser.EOF;
    }
  }

  /**
   * Handle protocol sub-requests, see <code>R</code> and <code>A</code> in the
   * protocol spec.
   * 
   * @return An array of one object. The object is the result of the Apply call.
   * @throws IOException
   * @throws Throwable
   *         thrown by the PHP code.
   */
  protected Object [] handleSubRequests () throws AbortException, Throwable
  {
    final Response currentResponse = response;
    final Arg current = arg;
    response = response.copyResponse (); // must keep the current response
                                         // state, for example coerceWriter for
                                         // castToString()
    arg = new SimpleArg ();
    while (Parser.OK == parseSubRequestUntilEof ())
    {
      arg.id.setID (response);
      switch (arg.type)
      {
        case 'I':
          switch (arg.predicate)
          {
            case 'P':
              bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            case 'I':
              bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'G':
          switch (arg.predicate)
          {
            case '2':
              response.setAsyncWriter ();
            case '1':
              bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            case '3':
              response.setAsyncVoidWriter ();
              bridge.GetSetProp (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'Y':
          switch (arg.predicate)
          {
            case '2':
              response.setAsyncWriter ();
            case '1':
              bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            case '3':
              response.setAsyncVoidWriter ();
              bridge.Invoke (arg.callObject, arg.method, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'C':
          switch (arg.predicate)
          {
            case 'C':
              bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
              break;
            case 'I':
              bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'H':
          switch (arg.predicate)
          {
            case '2':
              response.setAsyncWriter ();
            case '1':
              bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
              break;
            case '3':
              response.setAsyncVoidWriter ();
              bridge.CreateObject ((String) arg.callObject, false, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'K':
          switch (arg.predicate)
          {
            case '2':
              response.setAsyncWriter ();
            case '1':
              bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
              break;
            case '3':
              response.setAsyncVoidWriter ();
              bridge.CreateObject ((String) arg.callObject, true, arg.getArgs (), response);
              break;
            default:
              throw new IOException ("Protocol error");
          }
          response.flush ();
          break;
        case 'F': // may happen due to a fatal error in a sub request
          response.setFinish (false);
          response.flush ();
          response = currentResponse;
          arg = current;
          throw new AbortException ();
          // no factory.invalidate necessary; ContextRunner will terminate and
          // call factory.destroy()
        case 'R':
          final Arg ret = arg;
          arg = current;

          // remove retval from the output buffer and return it to the parent.
          response.reset ();
          response = currentResponse;
          if (ret.callObject != null)
            throw (Throwable) ret.callObject;
          return ret.getArgs ();
      }
      arg.reset ();
    }
    // should not happen
    response.setFinish (false);
    response.flush ();
    arg = current;
    response = currentResponse;
    throw new AbortException ();
  }

  /**
   * Reset the internal state so that a new input and output stream can be used
   * for the next packed. Note that request options (from init()) remain valid.
   * 
   * @see #init(InputStream, OutputStream)
   */
  public void reset ()
  {
    parser.reset ();
  }

  /**
   * Set a temporary bridge into this request. The bridge and its associated
   * session-/contextFactory will be automatically destroyed when the request is
   * done.
   * 
   * @see php.java.bridge.http.IContextFactory#recycle(String)
   * @param bridge
   *        The fresh bridge and its ContextFactory
   */
  public void setBridge (final JavaBridge bridge)
  {
    defaultBridge = this.bridge;
    bridge.in = this.bridge.in;
    bridge.out = this.bridge.out;
    this.bridge = bridge;
    this.bridge.request = this;
    response.setBridge (bridge);
    parser.setBridge (bridge);
  }

  private void resetBridge ()
  {
    if (defaultBridge != null)
    {
      bridge = defaultBridge;
      response.setBridge (bridge);
      parser.setBridge (bridge);
      defaultBridge = null;
    }
  }

  /** re-initialize for new requests */
  public void recycle ()
  {
    reset ();
    if (arg != null)
      arg.reset ();

    if (response != null)
      response.recycle ();
    resetBridge ();
  }

  /**
   * {@inheritDoc}
   */
  public void parseHeader (final InputStream in) throws IOException
  {
    bridge.getFactory ().parseHeader (this, in);
  }
}
