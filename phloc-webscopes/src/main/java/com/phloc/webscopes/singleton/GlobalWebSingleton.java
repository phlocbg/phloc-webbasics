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
package com.phloc.webscopes.singleton;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.scopes.AbstractSingleton;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * This is the base class for singleton objects that reside in the global scope.
 * The global scope is identical for web scope and non-web scope applications.
 * 
 * @see com.phloc.webscopes.mgr.EWebScope#GLOBAL
 * @author Philip Helger
 */
public abstract class GlobalWebSingleton extends AbstractSingleton
{
  protected GlobalWebSingleton ()
  {
    super ("getGlobalSingleton");
  }

  /**
   * @param bMustBePresent
   *        if <code>true</code> the scope must be present, <code>false</code>
   *        if it may be <code>null</code>.
   * @return The scope to be used for this type of singleton.
   */
  @Nonnull
  private static IGlobalWebScope _getStaticScope (final boolean bMustBePresent)
  {
    return bMustBePresent ? WebScopeManager.getGlobalScope () : WebScopeManager.getGlobalScopeOrNull ();
  }

  /**
   * Get the singleton object in the current global web scope, using the passed
   * class. If the singleton is not yet instantiated, a new instance is created.
   * 
   * @param aClass
   *        The class to be used. May not be <code>null</code>. The class must
   *        be public as needs to have a public no-argument constructor.
   * @return The singleton object and never <code>null</code>.
   */
  @Nonnull
  protected static final <T extends GlobalWebSingleton> T getGlobalSingleton (@Nonnull final Class <T> aClass)
  {
    return getSingleton (_getStaticScope (true), aClass);
  }

  /**
   * Get the singleton object if it is already instantiated inside the current
   * global web scope or <code>null</code> if it is not instantiated.
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return The singleton for the specified class is already instantiated,
   *         <code>null</code> otherwise.
   * @deprecated Use {@link #getGlobalSingletonIfInstantiated(Class)} instead
   */
  @Deprecated
  @Nullable
  public static final <T extends GlobalWebSingleton> T getSingletonIfInstantiated (@Nonnull final Class <T> aClass)
  {
    return getGlobalSingletonIfInstantiated (aClass);
  }

  /**
   * Get the singleton object if it is already instantiated inside the current
   * global web scope or <code>null</code> if it is not instantiated.
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return The singleton for the specified class is already instantiated,
   *         <code>null</code> otherwise.
   */
  @Nullable
  public static final <T extends GlobalWebSingleton> T getGlobalSingletonIfInstantiated (@Nonnull final Class <T> aClass)
  {
    return getSingletonIfInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Check if a singleton is already instantiated inside the current global web
   * scope
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return <code>true</code> if the singleton for the specified class is
   *         already instantiated, <code>false</code> otherwise.
   * @deprecated Use {@link #isGlobalSingletonInstantiated(Class)} instead
   */
  @Deprecated
  public static final boolean isSingletonInstantiated (@Nonnull final Class <? extends GlobalWebSingleton> aClass)
  {
    return isGlobalSingletonInstantiated (aClass);
  }

  /**
   * Check if a singleton is already instantiated inside the current global web
   * scope
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return <code>true</code> if the singleton for the specified class is
   *         already instantiated, <code>false</code> otherwise.
   */
  public static final boolean isGlobalSingletonInstantiated (@Nonnull final Class <? extends GlobalWebSingleton> aClass)
  {
    return isSingletonInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Get all singleton objects registered in the current global web scope.
   * 
   * @return A non-<code>null</code> list with all instances of this class in
   *         the current global web scope.
   * @deprecated Use {@link #getAllGlobalSingletons()} instead
   */
  @Deprecated
  @Nonnull
  public static final List <GlobalWebSingleton> getAllSingletons ()
  {
    return getAllGlobalSingletons ();
  }

  /**
   * Get all singleton objects registered in the current global web scope.
   * 
   * @return A non-<code>null</code> list with all instances of this class in
   *         the current global web scope.
   */
  @Nonnull
  public static final List <GlobalWebSingleton> getAllGlobalSingletons ()
  {
    return getAllSingletons (_getStaticScope (false), GlobalWebSingleton.class);
  }
}
