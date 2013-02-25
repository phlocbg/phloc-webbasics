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
package com.phloc.web.smtp;

import java.io.UnsupportedEncodingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.phloc.commons.email.IEmailAddress;

/**
 * This class handles a single email address. It is split into an address part
 * and an optional name. The personal name is optional and may be
 * <code>null</code>.
 * 
 * @author philip
 */
@Immutable
public final class InternetAddressUtils
{
  private InternetAddressUtils ()
  {}

  @Nonnull
  public static InternetAddress getAsInternetAddress (@Nonnull final IEmailAddress aAddress,
                                                      @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                                      AddressException
  {
    return getAsInternetAddress (aAddress.getAddress (), aAddress.getPersonal (), sCharset);
  }

  @Nonnull
  public static InternetAddress getAsInternetAddress (@Nonnull final String sAddress,
                                                      @Nullable final String sPersonal,
                                                      @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                                      AddressException
  {
    final InternetAddress ret = new InternetAddress (sAddress, sPersonal, sCharset);
    ret.validate ();
    return ret;
  }
}
