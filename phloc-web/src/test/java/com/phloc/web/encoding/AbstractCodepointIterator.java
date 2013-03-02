/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package com.phloc.web.encoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.annotation.Nullable;

/**
 * Provides an iterator over Unicode Codepoints
 */
public abstract class AbstractCodepointIterator implements Iterator <Codepoint>
{

  /**
   * Get a CodepointIterator for the specified char array
   */
  public static AbstractCodepointIterator forCharArray (final char [] array)
  {
    return new CharArrayCodepointIterator (array);
  }

  /**
   * Get a CodepointIterator for the specified CharSequence
   */
  public static AbstractCodepointIterator forCharSequence (final CharSequence seq)
  {
    return new CharSequenceCodepointIterator (seq);
  }

  /**
   * Get a CodepointIterator for the specified byte array, using the default
   * charset
   */
  public static AbstractCodepointIterator forByteArray (final byte [] array)
  {
    return new ByteArrayCodepointIterator (array);
  }

  /**
   * Get a CodepointIterator for the specified byte array, using the specified
   * charset
   */
  public static AbstractCodepointIterator forByteArray (final byte [] array, final String charset)
  {
    return new ByteArrayCodepointIterator (array, charset);
  }

  /**
   * Get a CodepointIterator for the specified CharBuffer
   */
  public static AbstractCodepointIterator forCharBuffer (final CharBuffer buffer)
  {
    return new CharBufferCodepointIterator (buffer);
  }

  /**
   * Get a CodepointIterator for the specified ReadableByteChannel
   */
  public static AbstractCodepointIterator forReadableByteChannel (final ReadableByteChannel channel)
  {
    return new ReadableByteChannelCodepointIterator (channel);
  }

  /**
   * Get a CodepointIterator for the specified ReadableByteChannel
   */
  public static AbstractCodepointIterator forReadableByteChannel (final ReadableByteChannel channel,
                                                                  final String charset)
  {
    return new ReadableByteChannelCodepointIterator (channel, charset);
  }

  /**
   * Get a CodepointIterator for the specified InputStream
   */
  public static AbstractCodepointIterator forInputStream (final InputStream in)
  {
    return new ReadableByteChannelCodepointIterator (Channels.newChannel (in));
  }

  /**
   * Get a CodepointIterator for the specified InputStream using the specified
   * charset
   */
  public static AbstractCodepointIterator forInputStream (final InputStream in, final String charset)
  {
    return new ReadableByteChannelCodepointIterator (Channels.newChannel (in), charset);
  }

  /**
   * Get a CodepointIterator for the specified Reader
   */
  public static AbstractCodepointIterator forReader (final Reader in)
  {
    return new ReaderCodepointIterator (in);
  }

  public static AbstractCodepointIterator restrict (final AbstractCodepointIterator ci, final ICodepointFilter filter)
  {
    return new RestrictedCodepointIterator (ci, filter, false);
  }

  public static AbstractCodepointIterator restrict (final AbstractCodepointIterator ci,
                                                    final ICodepointFilter filter,
                                                    final boolean scanning)
  {
    return new RestrictedCodepointIterator (ci, filter, scanning);
  }

  public static AbstractCodepointIterator restrict (final AbstractCodepointIterator ci,
                                                    final ICodepointFilter filter,
                                                    final boolean scanning,
                                                    final boolean invert)
  {
    return new RestrictedCodepointIterator (ci, filter, scanning, invert);
  }

  protected int m_nPosition = -1;
  protected int m_nLimit = -1;

  public AbstractCodepointIterator restrict (final ICodepointFilter filter)
  {
    return restrict (this, filter);
  }

  public AbstractCodepointIterator restrict (final ICodepointFilter filter, final boolean scanning)
  {
    return restrict (this, filter, scanning);
  }

  public AbstractCodepointIterator restrict (final ICodepointFilter filter, final boolean scanning, final boolean invert)
  {
    return restrict (this, filter, scanning, invert);
  }

  /**
   * Get the next char
   */
  protected abstract char get ();

  /**
   * Get the specified char
   */
  protected abstract char get (int index);

  /**
   * True if there are codepoints remaining
   */
  public boolean hasNext ()
  {
    return remaining () > 0;
  }

  /**
   * Return the final index position
   */
  public int lastPosition ()
  {
    final int p = position ();
    return (p > -1) ? (p >= limit ()) ? p : p - 1 : -1;
  }

  /**
   * Return the next chars. If the codepoint is not supplemental, the char array
   * will have a single member. If the codepoint is supplemental, the char array
   * will have two members, representing the high and low surrogate chars
   */
  @Nullable
  public char [] nextChars ()
  {
    if (hasNext ())
    {
      if (_isNextSurrogate ())
      {
        final char c1 = get ();
        if (Character.isHighSurrogate (c1) && position () < limit ())
        {
          final char c2 = get ();
          if (Character.isLowSurrogate (c2))
            return new char [] { c1, c2 };
          throw new InvalidCharacterException (c2);
        }
        else
          if (Character.isLowSurrogate (c1) && position () > 0)
          {
            final char c2 = get (position () - 2);
            if (Character.isHighSurrogate (c2))
              return new char [] { c1, c2 };
            throw new InvalidCharacterException (c2);
          }
      }
      return new char [] { get () };
    }
    return null;
  }

  /**
   * Peek the next chars in the iterator. If the codepoint is not supplemental,
   * the char array will have a single member. If the codepoint is supplemental,
   * the char array will have two members, representing the high and low
   * surrogate chars
   */
  public char [] peekChars ()
  {
    return _peekChars (position ());
  }

  /**
   * Peek the specified chars in the iterator. If the codepoint is not
   * supplemental, the char array will have a single member. If the codepoint is
   * supplemental, the char array will have two members, representing the high
   * and low surrogate chars
   */
  @Nullable
  private char [] _peekChars (final int pos)
  {
    if (pos < 0 || pos >= limit ())
      return null;
    final char c1 = get (pos);
    if (Character.isHighSurrogate (c1) && pos < limit ())
    {
      final char c2 = get (pos + 1);
      if (Character.isLowSurrogate (c2))
        return new char [] { c1, c2 };
      throw new InvalidCharacterException (c2);
    }
    else
      if (Character.isLowSurrogate (c1) && pos > 1)
      {
        final char c2 = get (pos - 1);
        if (Character.isHighSurrogate (c2))
          return new char [] { c2, c1 };
        throw new InvalidCharacterException (c2);
      }
      else
        return new char [] { c1 };
  }

  /**
   * Return the next codepoint
   */
  public Codepoint next ()
  {
    return _toCodepoint (nextChars ());
  }

  /**
   * Peek the next codepoint
   */
  public Codepoint peek ()
  {
    return _toCodepoint (peekChars ());
  }

  /**
   * Peek the specified codepoint
   */
  public Codepoint peek (final int index)
  {
    return _toCodepoint (_peekChars (index));
  }

  @Nullable
  private Codepoint _toCodepoint (final char [] chars)
  {
    return chars == null || chars.length == 0 ? null : chars.length == 1 ? new Codepoint (chars[0])
                                                                        : new Codepoint (chars[0], chars[1]);
  }

  /**
   * Set the iterator position
   */
  public void position (final int n)
  {
    if (n < 0 || n > limit ())
      throw new ArrayIndexOutOfBoundsException (n);
    m_nPosition = n;
  }

  /**
   * Get the iterator position
   */
  public int position ()
  {
    return m_nPosition;
  }

  /**
   * Return the iterator limit
   */
  public int limit ()
  {
    return m_nLimit;
  }

  /**
   * Return the remaining iterator size
   */
  public int remaining ()
  {
    return m_nLimit - position ();
  }

  private boolean _isNextSurrogate ()
  {
    if (!hasNext ())
      return false;
    final char c = get (position ());
    return Character.isHighSurrogate (c) || Character.isLowSurrogate (c);
  }

  /**
   * Returns true if the char at the specified index is a high surrogate
   */
  public boolean isHigh (final int index)
  {
    if (index < 0 || index > limit ())
      throw new ArrayIndexOutOfBoundsException (index);
    return Character.isHighSurrogate (get (index));
  }

  /**
   * Returns true if the char at the specified index is a low surrogate
   */
  public boolean isLow (final int index)
  {
    if (index < 0 || index > limit ())
      throw new ArrayIndexOutOfBoundsException (index);
    return Character.isLowSurrogate (get (index));
  }

  public void remove ()
  {
    throw new UnsupportedOperationException ();
  }

  static class ByteArrayCodepointIterator extends CharArrayCodepointIterator
  {
    public ByteArrayCodepointIterator (final byte [] bytes)
    {
      this (bytes, Charset.defaultCharset ());
    }

    public ByteArrayCodepointIterator (final byte [] bytes, final String charset)
    {
      this (bytes, Charset.forName (charset));
    }

    public ByteArrayCodepointIterator (final byte [] bytes, final Charset charset)
    {
      final CharBuffer cb = charset.decode (ByteBuffer.wrap (bytes));
      m_aBuffer = cb.array ();
      m_nPosition = cb.position ();
      m_nLimit = cb.limit ();
    }
  }

  static class CharArrayCodepointIterator extends AbstractCodepointIterator
  {
    protected char [] m_aBuffer;

    protected CharArrayCodepointIterator ()
    {}

    public CharArrayCodepointIterator (final char [] buffer)
    {
      this (buffer, 0, buffer.length);
    }

    public CharArrayCodepointIterator (final char [] buffer, final int n, final int e)
    {
      m_aBuffer = buffer;
      m_nPosition = n;
      m_nLimit = Math.min (buffer.length - n, e);
    }

    @Override
    protected char get ()
    {
      return (m_nPosition < m_nLimit) ? m_aBuffer[m_nPosition++] : (char) -1;
    }

    @Override
    protected char get (final int index)
    {
      if (index < 0 || index >= m_nLimit)
        throw new ArrayIndexOutOfBoundsException (index);
      return m_aBuffer[index];
    }
  }

  static class CharBufferCodepointIterator extends CharArrayCodepointIterator
  {
    public CharBufferCodepointIterator (final CharBuffer cb)
    {
      m_aBuffer = cb.array ();
      m_nPosition = cb.position ();
      m_nLimit = cb.limit ();
    }
  }

  static class CharSequenceCodepointIterator extends AbstractCodepointIterator
  {
    private final CharSequence m_aBuffer;

    public CharSequenceCodepointIterator (final CharSequence buffer)
    {
      this (buffer, 0, buffer.length ());
    }

    public CharSequenceCodepointIterator (final CharSequence buffer, final int n, final int e)
    {
      m_aBuffer = buffer;
      m_nPosition = n;
      m_nLimit = Math.min (buffer.length () - n, e);
    }

    @Override
    protected char get ()
    {
      return m_aBuffer.charAt (m_nPosition++);
    }

    @Override
    protected char get (final int index)
    {
      return m_aBuffer.charAt (index);
    }
  }

  static class ReadableByteChannelCodepointIterator extends CharArrayCodepointIterator
  {
    public ReadableByteChannelCodepointIterator (final ReadableByteChannel channel)
    {
      this (channel, Charset.defaultCharset ());
    }

    public ReadableByteChannelCodepointIterator (final ReadableByteChannel channel, final String charset)
    {
      this (channel, Charset.forName (charset));
    }

    public ReadableByteChannelCodepointIterator (final ReadableByteChannel channel, final Charset charset)
    {
      try
      {
        final ByteBuffer buf = ByteBuffer.allocate (1024);
        final ByteArrayOutputStream out = new ByteArrayOutputStream ();
        final WritableByteChannel outc = Channels.newChannel (out);
        while (channel.read (buf) > 0)
        {
          buf.flip ();
          outc.write (buf);
        }
        final CharBuffer cb = charset.decode (ByteBuffer.wrap (out.toByteArray ()));
        m_aBuffer = cb.array ();
        m_nPosition = cb.position ();
        m_nLimit = cb.limit ();
      }
      catch (final Exception e)
      {}
    }
  }

  static class ReaderCodepointIterator extends CharArrayCodepointIterator
  {
    public ReaderCodepointIterator (final Reader reader)
    {
      try
      {
        final StringBuilder sb = new StringBuilder ();
        final char [] buf = new char [1024];
        int n = -1;
        while ((n = reader.read (buf)) > -1)
        {
          sb.append (buf, 0, n);
        }
        m_aBuffer = new char [sb.length ()];
        sb.getChars (0, sb.length (), m_aBuffer, 0);
        m_nPosition = 0;
        m_nLimit = m_aBuffer.length;
      }
      catch (final IOException e)
      {
        throw new RuntimeException (e);
      }
    }
  }

  public static class RestrictedCodepointIterator extends AbstractDelegatingCodepointIterator
  {

    private final ICodepointFilter m_aFilter;
    private final boolean m_bScanningOnly;
    private final boolean m_bNotset;

    protected RestrictedCodepointIterator (final AbstractCodepointIterator internal, final ICodepointFilter filter)
    {
      this (internal, filter, false);
    }

    protected RestrictedCodepointIterator (final AbstractCodepointIterator internal,
                                           final ICodepointFilter filter,
                                           final boolean scanningOnly)
    {
      this (internal, filter, scanningOnly, false);
    }

    protected RestrictedCodepointIterator (final AbstractCodepointIterator internal,
                                           final ICodepointFilter filter,
                                           final boolean scanningOnly,
                                           final boolean notset)
    {
      super (internal);
      m_aFilter = filter;
      m_bScanningOnly = scanningOnly;
      m_bNotset = notset;
    }

    @Override
    public boolean hasNext ()
    {
      final boolean b = super.hasNext ();
      if (m_bScanningOnly)
      {
        try
        {
          final int cp = peek (position ()).getValue ();
          if (b && cp != -1 && check (cp))
            return false;
        }
        catch (final InvalidCharacterException e)
        {
          return false;
        }
      }
      return b;
    }

    @Override
    public Codepoint next ()
    {
      final Codepoint cp = super.next ();
      final int v = cp.getValue ();
      if (v != -1 && check (v))
      {
        if (m_bScanningOnly)
        {
          position (position () - 1);
          return null;
        }
        throw new InvalidCharacterException (v);
      }
      return cp;
    }

    private boolean check (final int cp)
    {
      final boolean answer = !m_aFilter.accept (cp);
      return (!m_bNotset) ? !answer : answer;
    }

    @Override
    public char [] nextChars ()
    {
      final char [] chars = super.nextChars ();
      if (chars != null && chars.length > 0)
      {
        if (chars.length == 1 && check (chars[0]))
        {
          if (m_bScanningOnly)
          {
            position (position () - 1);
            return null;
          }
          throw new InvalidCharacterException (chars[0]);
        }
        else
          if (chars.length == 2)
          {
            final int cp = Character.toCodePoint (chars[0], chars[1]);
            if (check (cp))
            {
              if (m_bScanningOnly)
              {
                position (position () - 2);
                return null;
              }
              throw new InvalidCharacterException (cp);
            }
          }
      }
      return chars;
    }

  }

}
