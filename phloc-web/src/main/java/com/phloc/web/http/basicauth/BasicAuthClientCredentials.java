/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.http.basicauth;

import java.io.Serializable;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.base64.Base64Helper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Credentials for HTTP basic authentication
 * 
 * @author Boris Gregorcic
 */
@Immutable
public class BasicAuthClientCredentials implements Serializable
{
  private static final long serialVersionUID = -5973068742749421254L;
  private final String m_sUserName;
  private final String m_sPassword;

  /**
   * Create credentials with a user name only and no password.
   * 
   * @param sUserName
   *        The user name to use. May neither be <code>null</code> nor empty.
   */
  public BasicAuthClientCredentials (@Nonnull @Nonempty final String sUserName)
  {
    this (sUserName, null);
  }

  /**
   * Create credentials with a user name and a password.
   * 
   * @param sUserName
   *        The user name to use. May neither be <code>null</code> nor empty.
   * @param sPassword
   *        The password to use. May be <code>null</code> or empty to indicate
   *        that no password is present.
   */
  public BasicAuthClientCredentials (@Nonnull @Nonempty final String sUserName, @Nullable final String sPassword)
  {
    this.m_sUserName = ValueEnforcer.notEmpty (sUserName, "UserName"); //$NON-NLS-1$
    // No difference between null and empty string
    this.m_sPassword = StringHelper.hasNoText (sPassword) ? null : sPassword;
  }

  /**
   * @return The user name. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getUserName ()
  {
    return this.m_sUserName;
  }

  /**
   * @return The password. May be <code>null</code> or empty.
   */
  @Nullable
  public String getPassword ()
  {
    return this.m_sPassword;
  }

  /**
   * @return <code>true</code> if a non-<code>null</code> non-empty password is
   *         present.
   */
  public boolean hasPassword ()
  {
    return this.m_sPassword != null;
  }

  /**
   * Create the request HTTP header value for use with the
   * {@link com.phloc.web.http.CHTTPHeader#AUTHORIZATION} header name.
   * 
   * @return The HTTP header value to use. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getRequestValue ()
  {
    return getRequestValue (null);
  }

  /**
   * Create the request HTTP header value for use with the
   * {@link com.phloc.web.http.CHTTPHeader#AUTHORIZATION} header name.
   * 
   * @param aCharset
   *        Character set to use (defaults to
   *        {@link HTTPBasicAuth#getCharset()})
   * @return The HTTP header value to use. Neither <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getRequestValue (@Nullable final Charset aCharset)
  {
    final String sCombined = StringHelper.getConcatenatedOnDemand (this.m_sUserName,
                                                                   HTTPBasicAuth.USERNAME_PASSWORD_SEPARATOR,
                                                                   this.m_sPassword);
    return HTTPBasicAuth.HEADER_VALUE_PREFIX_BASIC +
           " " + //$NON-NLS-1$
           Base64Helper.safeEncode (sCombined, aCharset == null ? HTTPBasicAuth.getCharset () : aCharset);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof BasicAuthClientCredentials))
      return false;
    final BasicAuthClientCredentials rhs = (BasicAuthClientCredentials) o;
    return this.m_sUserName.equals (rhs.m_sUserName) && EqualsUtils.equals (this.m_sPassword, rhs.m_sPassword);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (this.m_sUserName).append (this.m_sPassword).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("userName", this.m_sUserName).appendPassword ("password").toString (); //$NON-NLS-1$ //$NON-NLS-2$
  }
}
