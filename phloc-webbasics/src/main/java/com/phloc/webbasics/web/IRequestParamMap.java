/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * A request parameter map. It handles complex request parameters and lets you
 * iterate the structure.
 * 
 * @author philip
 */
public interface IRequestParamMap extends IHasSize
{
  boolean contains (@Nonnull @Nonempty String... aPath);

  @Nullable
  Object getObject (@Nonnull @Nonempty String... aPath);

  @Nullable
  String getString (@Nonnull @Nonempty String... aPath);

  /**
   * Get a nested map for the specified path.
   * 
   * @param aPath
   *        The path to be resolved
   * @return <code>null</code> if the path could not be resolved.
   */
  @Nullable
  IRequestParamMap getMap (@Nonnull @Nonempty String... aPath);

  /**
   * Check if this map, contains the passed key. This will be true both for
   * nested maps as well as for values.
   * 
   * @param sKey
   *        The key to check.
   * @return <code>true</code> if such a key is contained, <code>false</code> if
   *         not
   */
  boolean containsKey (@Nullable String sKey);

  /**
   * @return A set of all contained key. Never <code>null</code> but maybe
   *         empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  Set <String> keySet ();

  /**
   * @return A collection of all values of this map. The type of the value is
   *         usually either {@link String}, file item (from upload) or
   *         {@link Map} from String to Object.
   */
  @Nonnull
  @ReturnsMutableCopy
  Collection <Object> values ();

  /**
   * @deprecated Use {@link #getAsObjectMap()} instead
   */
  @Deprecated
  @Nonnull
  @ReturnsMutableCopy
  Map <String, Object> asObjectMap ();

  /**
   * @return A copy of the contained map. For the value types see
   *         {@link #values()}
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, Object> getAsObjectMap ();

  /**
   * @deprecated Use {@link #getAsValueMap()} instead
   */
  @Deprecated
  @Nonnull
  @ReturnsMutableCopy
  Map <String, String> asValueMap ();

  /**
   * @return A key/value map, with enforced values. If this map contains a
   *         nested map, this method will fail with a ClassCastException!
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, String> getAsValueMap ();
}
