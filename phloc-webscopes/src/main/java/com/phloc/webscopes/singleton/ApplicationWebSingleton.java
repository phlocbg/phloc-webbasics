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
package com.phloc.webscopes.singleton;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.scopes.AbstractSingleton;
import com.phloc.webscopes.domain.IApplicationWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * This is the base class for singleton objects that reside in the application
 * scope. This is the same for web scopes and non-web scopes, as application
 * scopes are managed in the global scope which is also identical for web scopes
 * and non-web scopes.
 * 
 * @see com.phloc.webscopes.mgr.EWebScope#APPLICATION
 * @author Philip Helger
 */
public abstract class ApplicationWebSingleton extends AbstractSingleton
{
  protected ApplicationWebSingleton ()
  {
    super ("getApplicationSingleton");
  }

  /**
   * @param bCreateIfNotExisting
   *        if <code>true</code> the scope will be created if it is not existing
   * @return The scope to be used for this type of singleton.
   */
  @Nullable
  private static IApplicationWebScope _getStaticScope (final boolean bCreateIfNotExisting)
  {
    return WebScopeManager.getApplicationScope (bCreateIfNotExisting);
  }

  /**
   * Get the singleton object in the current application web scope, using the
   * passed class. If the singleton is not yet instantiated, a new instance is
   * created.
   * 
   * @param aClass
   *        The class to be used. May not be <code>null</code>. The class must
   *        be public as needs to have a public no-argument constructor.
   * @return The singleton object and never <code>null</code>.
   */
  @Nonnull
  protected static final <T extends ApplicationWebSingleton> T getApplicationSingleton (@Nonnull final Class <T> aClass)
  {
    return getSingleton (_getStaticScope (true), aClass);
  }

  /**
   * Get the singleton object if it is already instantiated inside the current
   * application web scope or <code>null</code> if it is not instantiated.
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return The singleton for the specified class is already instantiated,
   *         <code>null</code> otherwise.
   * @deprecated Use {@link #getApplicationSingletonIfInstantiated(Class)}
   *             instead
   */
  @Deprecated
  @Nullable
  public static final <T extends ApplicationWebSingleton> T getSingletonIfInstantiated (@Nonnull final Class <T> aClass)
  {
    return getApplicationSingletonIfInstantiated (aClass);
  }

  /**
   * Get the singleton object if it is already instantiated inside the current
   * application web scope or <code>null</code> if it is not instantiated.
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return The singleton for the specified class is already instantiated,
   *         <code>null</code> otherwise.
   */
  @Nullable
  public static final <T extends ApplicationWebSingleton> T getApplicationSingletonIfInstantiated (@Nonnull final Class <T> aClass)
  {
    return getSingletonIfInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Check if a singleton is already instantiated inside the current application
   * web scope
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return <code>true</code> if the singleton for the specified class is
   *         already instantiated, <code>false</code> otherwise.
   * @deprecated Use {@link #isApplicationSingletonInstantiated(Class)} instead
   */
  @Deprecated
  public static final boolean isSingletonInstantiated (@Nonnull final Class <? extends ApplicationWebSingleton> aClass)
  {
    return isApplicationSingletonInstantiated (aClass);
  }

  /**
   * Check if a singleton is already instantiated inside the current application
   * web scope
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return <code>true</code> if the singleton for the specified class is
   *         already instantiated, <code>false</code> otherwise.
   */
  public static final boolean isApplicationSingletonInstantiated (@Nonnull final Class <? extends ApplicationWebSingleton> aClass)
  {
    return isSingletonInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Get all singleton objects registered in the current application web scope.
   * 
   * @return A non-<code>null</code> list with all instances of this class in
   *         the current application web scope.
   * @deprecated Use {@link #getAllApplicationSingletons()} instead
   */
  @Deprecated
  @Nonnull
  public static final List <ApplicationWebSingleton> getAllSingletons ()
  {
    return getAllApplicationSingletons ();
  }

  /**
   * Get all singleton objects registered in the current application web scope.
   * 
   * @return A non-<code>null</code> list with all instances of this class in
   *         the current application web scope.
   */
  @Nonnull
  public static final List <ApplicationWebSingleton> getAllApplicationSingletons ()
  {
    return getAllSingletons (_getStaticScope (false), ApplicationWebSingleton.class);
  }
}
