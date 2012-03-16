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
package com.phloc.webbasics.app.scope;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;

/**
 * Base interface for all scopes.
 * 
 * @author philip
 */
public interface IScope
{
  /**
   * Get the attribute within the scope with the given name
   * 
   * @param sName
   *        The attribute name to search
   * @return <code>null</code> if no such attribute is present
   */
  @Nullable
  Object getAttributeObject (@Nullable String sName);

  /**
   * Get the attribute within the scope with the given name, casted to the
   * desired type.
   * 
   * @param sName
   *        The attribute name to search
   * @return <code>null</code> if no such attribute is present
   */
  @Nullable
  <T> T getCastedAttribute (@Nullable String sName);

  @Nullable
  String getAttributeAsString (@Nullable String sName);

  int getAttributeAsInt (@Nullable String sName, int nDefault);

  /**
   * Set an attribute within this scope
   * 
   * @param sName
   *        The name of the attribute
   * @param aValue
   *        If the value is <code>null</code> the attribute will be removed from
   *        the scope otherwise it is set within the scope. If another attribute
   *        with this name is already present it is overwritten.
   */
  void setAttribute (@Nonnull String sName, @Nullable Object aValue);

  /**
   * Remove an attribute from the scope.
   * 
   * @param sName
   *        The name of the attribute to be removed.
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeAttribute (@Nullable String sName);

  /**
   * Call when the scope is not needed any longer.
   */
  void destroyScope ();

  /**
   * Check if this scope is valid. A valid scope is determined by that it is not
   * in destruction (@see {@link #isInDestruction()}) and not destroyed (@see
   * {@link #isDestroyed()}).
   * 
   * @return <code>true</code> if the scope is neither in destruction nor
   *         destroyed.
   */
  boolean isValid ();

  /**
   * Check if this scope is in the middle of destruction.
   * 
   * @return <code>true</code> only if the destruction process is just
   *         performing and <code>false</code> if the scope is OK or if it is
   *         already destroyed!
   */
  boolean isInDestruction ();

  /**
   * Check if this scope is already destroyed.
   * 
   * @return <code>true</code> if the scope is already destroyed,
   *         <code>false</code> otherwise.
   */
  boolean isDestroyed ();
}
