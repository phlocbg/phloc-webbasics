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
package com.phloc.web.encoding.i18n;

/**
 * Base implementation of a CodepointIterator that filters the output of another
 * CodpointIterator
 * 
 * @author Apache Abdera
 */
public abstract class AbstractDelegatingCodepointIterator extends AbstractCodepointIterator
{
  private final AbstractCodepointIterator m_aInternal;

  protected AbstractDelegatingCodepointIterator (final AbstractCodepointIterator internal)
  {
    m_aInternal = internal;
  }

  @Override
  protected char get ()
  {
    return m_aInternal.get ();
  }

  @Override
  protected char get (final int index)
  {
    return m_aInternal.get (index);
  }

  @Override
  public boolean hasNext ()
  {
    return m_aInternal.hasNext ();
  }

  @Override
  public boolean isHigh (final int index)
  {
    return m_aInternal.isHigh (index);
  }

  @Override
  public boolean isLow (final int index)
  {
    return m_aInternal.isLow (index);
  }

  @Override
  public int limit ()
  {
    return m_aInternal.limit ();
  }

  @Override
  public Codepoint next ()
  {
    return m_aInternal.next ();
  }

  @Override
  public char [] nextChars ()
  {
    return m_aInternal.nextChars ();
  }

  @Override
  public Codepoint peek ()
  {
    return m_aInternal.peek ();
  }

  @Override
  public Codepoint peek (final int index)
  {
    return m_aInternal.peek (index);
  }

  @Override
  public char [] peekChars ()
  {
    return m_aInternal.peekChars ();
  }

  @Override
  public int position ()
  {
    return m_aInternal.position ();
  }

  @Override
  public int remaining ()
  {
    return m_aInternal.remaining ();
  }

  @Override
  public void position (final int position)
  {
    m_aInternal.position (position);
  }
}
