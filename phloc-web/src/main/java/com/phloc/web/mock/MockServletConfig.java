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
package com.phloc.web.mock;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Mock implementation of the {@link ServletConfig} interface.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class MockServletConfig implements ServletConfig
{
  private final ServletContext m_aSC;
  private final String m_sServletName;
  private final Map <String, String> m_aServletInitParams = new LinkedHashMap <String, String> ();

  /**
   * Constructor without servlet init parameters.
   * 
   * @param aSC
   *        Base servlet context. May not be <code>null</code>.
   * @param sServletName
   *        Name of the servlet. May neither be <code>null</code> nor empty.
   */
  public MockServletConfig (@Nonnull final ServletContext aSC, @Nonnull @Nonempty final String sServletName)
  {
    this (aSC, sServletName, null);
  }

  /**
   * Constructor
   * 
   * @param aSC
   *        Base servlet context. May not be <code>null</code>.
   * @param sServletName
   *        Name of the servlet. May neither be <code>null</code> nor empty.
   * @param aServletInitParams
   *        The map with all servlet init parameters. May be <code>null</code>
   *        or empty.
   */
  public MockServletConfig (@Nonnull final ServletContext aSC,
                            @Nonnull @Nonempty final String sServletName,
                            @Nullable final Map <String, String> aServletInitParams)
  {
    if (aSC == null)
      throw new NullPointerException ("servletContext");
    if (StringHelper.hasNoText (sServletName))
      throw new NullPointerException ("servletName");

    m_aSC = aSC;
    m_sServletName = sServletName;
    if (aServletInitParams != null)
      m_aServletInitParams.putAll (aServletInitParams);
  }

  @Nonnull
  @Nonempty
  public String getServletName ()
  {
    return m_sServletName;
  }

  @Nonnull
  public ServletContext getServletContext ()
  {
    return m_aSC;
  }

  @Nullable
  public String getInitParameter (@Nullable final String sName)
  {
    return m_aServletInitParams.get (sName);
  }

  @Nonnull
  public Enumeration <String> getInitParameterNames ()
  {
    return ContainerHelper.getEnumeration (m_aServletInitParams.keySet ());
  }

  public void addInitParameter (@Nonnull @Nonempty final String sName, @Nonnull final String sValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (sValue == null)
      throw new NullPointerException ("value");
    m_aServletInitParams.put (sName, sValue);
  }

  @Nonnull
  public EChange removeInitParameter (@Nullable final String sName)
  {
    return EChange.valueOf (m_aServletInitParams.remove (sName) != null);
  }

  @Nonnull
  @Nonempty
  public Map <String, String> getAllInitParameters ()
  {
    return ContainerHelper.newMap (m_aServletInitParams);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("servletContext", m_aSC)
                                       .append ("servletName", m_sServletName)
                                       .append ("servletInitParams", m_aServletInitParams)
                                       .toString ();
  }
}
