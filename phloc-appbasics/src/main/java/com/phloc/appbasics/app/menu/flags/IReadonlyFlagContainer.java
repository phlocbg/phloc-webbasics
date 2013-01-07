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
package com.phloc.appbasics.app.menu.flags;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * Base interface for a generic read-only flag container. It maps strings to any
 * Java object.
 * 
 * @author philip
 */
public interface IReadonlyFlagContainer extends Serializable
{
  /**
   * @return The number of contained flags. Always &ge; 0.
   */
  @Nonnegative
  int getFlagCount ();

  /**
   * @return <code>true</code> if this flag container does not contain any flag
   *         at all, <code>false</code> if at least one flag is contained.
   */
  boolean containsNoFlag ();

  /**
   * Check if an flag of the given name is contained.
   * 
   * @param sName
   *        name of the flag to check
   * @return <code>true</code> if the flag is contained, <code>false</code>
   *         otherwise
   */
  boolean containsFlag (@Nullable String sName);

  /**
   * @return The non-<code>null</code> set with all contained flags.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <String> getAllFlags ();
}
