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
package com.phloc.webbasics.smtp;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.type.ObjectType;

/**
 * Constants for named SMTP settings handling
 * 
 * @author Philip Helger
 */
@Immutable
public final class CNamedSMTPSettings
{
  // Object types
  public static final ObjectType OT_NAMED_SMTP_SETTINGS = new ObjectType ("named-smtp-settings");

  // Default named SMTP settings data
  public static final String NAMED_SMTP_SETTINGS_DEFAULT_ID = "default";
  public static final String NAMED_SMTP_SETTINGS_DEFAULT_NAME = "default";
  public static final String NAMED_SMTP_SETTINGS_DEFAULT_HOST = "smtp.ebox.at";

  private CNamedSMTPSettings ()
  {}
}
