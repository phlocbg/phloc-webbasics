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
package com.phloc.web.smtp;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.email.EmailAddressUtils;

/**
 * Perform email address validations.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public final class EmailAddressValidator
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (EmailAddressValidator.class);
  private static final AtomicBoolean s_aPerformMXRecordCheck = new AtomicBoolean (false);

  private EmailAddressValidator ()
  {}

  /**
   * Set the global "check MX record" flag.
   * 
   * @param bPerformMXRecordCheck
   *        <code>true</code> to enable, <code>false</code> otherwise.
   */
  public static void setPerformMXRecordCheck (final boolean bPerformMXRecordCheck)
  {
    s_aPerformMXRecordCheck.set (bPerformMXRecordCheck);
    s_aLogger.info ("Email address record check is " + (bPerformMXRecordCheck ? "enabled" : "disabled"));
  }

  /**
   * @return <code>true</code> if global MX record checking is enabled
   */
  public static boolean isPerformMXRecordCheck ()
  {
    return s_aPerformMXRecordCheck.get ();
  }

  /**
   * Checks if a value is a valid e-mail address. Depending on the global value
   * for the MX record check the check is performed incl. the MX record check or
   * without.
   * 
   * @param sEmail
   *        The value validation is being performed on. A <code>null</code>
   *        value is considered invalid.
   * @return <code>true</code> if the email address is valid, <code>false</code>
   *         otherwise.
   * @see #isPerformMXRecordCheck()
   * @see #setPerformMXRecordCheck(boolean)
   */
  public static boolean isValid (@Nullable final String sEmail)
  {
    return s_aPerformMXRecordCheck.get () ? isValidWithMXCheck (sEmail) : EmailAddressUtils.isValid (sEmail);
  }

  /**
   * Checks if a value is a valid e-mail address according to a complex regular
   * expression. Additionally an MX record lookup is performed to see whether
   * this host provides SMTP services.
   * 
   * @param sEmail
   *        The value validation is being performed on. A <code>null</code>
   *        value is considered invalid.
   * @return <code>true</code> if the email address is valid, <code>false</code>
   *         otherwise.
   */
  public static boolean isValidWithMXCheck (@Nullable final String sEmail)
  {
    return EmailAddressUtils.isValid (sEmail) && MXChecker.getInstance ().isValidMXEntry (sEmail);
  }
}
