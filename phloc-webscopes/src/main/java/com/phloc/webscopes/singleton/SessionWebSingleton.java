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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.scopes.AbstractSingleton;
import com.phloc.webscopes.domain.ISessionWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * This is the base class for singleton objects that reside in the session web
 * scope.
 * 
 * @see com.phloc.webscopes.mgr.EWebScope#SESSION
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public abstract class SessionWebSingleton extends AbstractSingleton implements Serializable
{
  protected SessionWebSingleton ()
  {
    super ("getSessionSingleton"); //$NON-NLS-1$
  }

  private void writeObject (@Nonnull final ObjectOutputStream aOOS) throws IOException
  {
    writeAbstractSingletonFields (aOOS);
  }

  private void readObject (@Nonnull final ObjectInputStream aOIS) throws IOException
  {
    readAbstractSingletonFields (aOIS);
  }

  /**
   * @param bCreateIfNotExisting
   *        if <code>true</code> the scope will be created if it is not
   *        existing, <code>false</code> if not.
   * @return The scope to be used for this type of singleton.
   */
  @Nonnull
  private static ISessionWebScope _getStaticScope (final boolean bCreateIfNotExisting)
  {
    return WebScopeManager.getSessionScope (bCreateIfNotExisting);
  }

  /**
   * Get the singleton object in the current session web scope, using the passed
   * class. If the singleton is not yet instantiated, a new instance is created.
   * 
   * @param <T>
   *        Implementation type of the singleton
   * @param aClass
   *        The class to be used. May not be <code>null</code>. The class must
   *        be public as needs to have a public no-argument constructor.
   * @return The singleton object and never <code>null</code>.
   */
  @Nonnull
  protected static final <T extends SessionWebSingleton> T getSessionSingleton (@Nonnull final Class <T> aClass)
  {
    return getSingleton (_getStaticScope (true), aClass);
  }

  /**
   * Get the singleton object if it is already instantiated inside the current
   * session web scope or <code>null</code> if it is not instantiated.
   * 
   * @param <T>
   *        Implementation type of the singleton
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return The singleton for the specified class is already instantiated,
   *         <code>null</code> otherwise.
   */
  @Nullable
  public static final <T extends SessionWebSingleton> T getSessionSingletonIfInstantiated (@Nonnull final Class <T> aClass)
  {
    return getSingletonIfInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Check if a singleton is already instantiated inside the current session web
   * scope
   * 
   * @param aClass
   *        The class to be checked. May not be <code>null</code>.
   * @return <code>true</code> if the singleton for the specified class is
   *         already instantiated, <code>false</code> otherwise.
   */
  public static final boolean isSessionSingletonInstantiated (@Nonnull final Class <? extends SessionWebSingleton> aClass)
  {
    return isSingletonInstantiated (_getStaticScope (false), aClass);
  }

  /**
   * Get all singleton objects registered in the current session web scope.
   * 
   * @return A non-<code>null</code> list with all instances of this class in
   *         the current session web scope.
   */
  @Nonnull
  public static final List <SessionWebSingleton> getAllSessionSingletons ()
  {
    return getAllSingletons (_getStaticScope (false), SessionWebSingleton.class);
  }
}
