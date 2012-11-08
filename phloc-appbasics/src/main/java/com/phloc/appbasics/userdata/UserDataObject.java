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
package com.phloc.appbasics.userdata;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.scopes.web.mgr.WebScopeManager;

/**
 * Represents a single web accessible object, that was provided by the user.
 * Think of this as a file descriptor.
 * 
 * @author philip
 */
public final class UserDataObject
{
  private final String m_sPath;

  public UserDataObject (@Nonnull @Nonempty final String sPath)
  {
    if (StringHelper.hasNoText (sPath))
      throw new IllegalArgumentException ("path");
    if (sPath.startsWith ("/"))
      m_sPath = sPath;
    else
      m_sPath = '/' + sPath;
  }

  /**
   * @return The path of the object. Always starting with a "/". This method
   *         does not contain any server specific context path!
   */
  @Nonnull
  @Nonempty
  public String getPath ()
  {
    return m_sPath;
  }

  /**
   * @return The path to the user data object as an URL, including the context
   *         path. Always starting with a "/"
   */
  @Nonnull
  public ISimpleURL getAsURL ()
  {
    return new SimpleURL (WebScopeManager.getRequestScope ().getContextPath () + m_sPath);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UserDataObject))
      return false;
    final UserDataObject rhs = (UserDataObject) o;
    return m_sPath.equals (rhs.m_sPath);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPath).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("path", m_sPath).toString ();
  }
}
