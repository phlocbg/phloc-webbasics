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
package com.phloc.appbasics.mock;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.scopes.mock.ScopeTestRule;

/**
 * Non-web scope aware test rule, with a defined storage root directory
 * 
 * @author Philip Helger
 */
public class AppBasicTestRule extends ScopeTestRule
{
  private final File m_aDataPath;
  private final File m_aServletContextPath;

  /**
   * Ctor using the default storage path from {@link ScopeTestRule}
   */
  public AppBasicTestRule ()
  {
    this (ScopeTestRule.STORAGE_PATH, ScopeTestRule.STORAGE_PATH);
  }

  /**
   * Ctor with an arbitrary path
   * 
   * @param aDataPath
   *        The data path to be used. May not be <code>null</code>.
   * @param aServletContextPath
   *        The servlet context path to be used. May not be <code>null</code>.
   */
  public AppBasicTestRule (@Nonnull final File aDataPath, @Nonnull final File aServletContextPath)
  {
    ValueEnforcer.notNull (aDataPath, "DataPath");
    ValueEnforcer.notNull (aServletContextPath, "ServletContextPath");
    m_aDataPath = aDataPath.getAbsoluteFile ();
    m_aServletContextPath = aServletContextPath.getAbsoluteFile ();
  }

  /**
   * @return The used storage path. Never <code>null</code>.
   */
  @Nonnull
  public File getStoragePath ()
  {
    return m_aDataPath;
  }

  @Override
  public void before ()
  {
    super.before ();
    AppBasicTestInit.initAppBasics (m_aDataPath, m_aServletContextPath);
  }

  @Override
  public void after ()
  {
    AppBasicTestInit.shutdownAppBasics ();
    super.after ();
  }
}
