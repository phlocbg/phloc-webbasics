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
package com.phloc.web.encoding.tmp;

import java.util.Iterator;

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * BAse interface for codepoint iterators
 * 
 * @author philip
 */
public interface ICodepointIterator extends Iterator <Codepoint>
{
  /**
   * @return <code>true</code> if there are codepoints remaining
   */
  boolean hasNext ();

  /**
   * @return the final index position
   */
  @CheckForSigned
  int lastPosition ();

  /**
   * @return the next chars. If the codepoint is not supplemental, the char
   *         array will have a single member. If the codepoint is supplemental,
   *         the char array will have two members, representing the high and low
   *         surrogate chars
   */
  @Nullable
  char [] nextChars ();

  /**
   * @return Peek the next chars in the iterator. If the codepoint is not
   *         supplemental, the char array will have a single member. If the
   *         codepoint is supplemental, the char array will have two members,
   *         representing the high and low surrogate chars
   */
  @Nullable
  char [] peekChars ();

  /**
   * @return the next codepoint
   */
  @Nullable
  Codepoint next ();

  /**
   * @return Peek the next codepoint
   */
  @Nullable
  Codepoint peek ();

  /**
   * @return Peek the specified codepoint
   */
  @Nullable
  Codepoint peek (@Nonnegative int index);

  /**
   * Set the iterator position
   */
  void position (@Nonnegative int n);

  /**
   * @return the iterator position
   */
  @Nonnegative
  int position ();

  /**
   * @return the iterator limit
   */
  @Nonnegative
  int limit ();

  /**
   * @return the remaining iterator size
   */
  @Nonnegative
  int remaining ();

  /**
   * @return <code>true</code> if the char at the specified index is a high
   *         surrogate
   */
  boolean isHigh (@Nonnegative int index);

  /**
   * @return <code>true</code> if the char at the specified index is a low
   *         surrogate
   */
  boolean isLow (@Nonnegative int index);

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter);

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter, boolean bScanning);

  @Nonnull
  CodepointIteratorRestricted restrict (@Nonnull ICodepointFilter aFilter, boolean bScanning, boolean bInvert);
}
