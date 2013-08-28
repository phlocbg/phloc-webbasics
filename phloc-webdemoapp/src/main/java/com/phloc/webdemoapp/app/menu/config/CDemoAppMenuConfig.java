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
package com.phloc.webdemoapp.app.menu.config;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class CDemoAppMenuConfig
{
  // Menu item IDs
  public static final String MENU_ADMIN = "admin";
  public static final String MENU_ADMIN_SECURITY = "admin_security";
  public static final String MENU_ADMIN_SECURITY_LOGININFO = "admin_security_logininfo";
  public static final String MENU_ADMIN_SECURITY_USER = "admin_security_user";
  public static final String MENU_ADMIN_SECURITY_ROLE = "admin_security_role";
  public static final String MENU_ADMIN_SECURITY_USERGROUP = "admin_security_usergroup";
  public static final String MENU_ADMIN_MONITORING = "admin_monitoring";
  public static final String MENU_ADMIN_MONITORING_AUDIT = "admin_monitoring_audit";
  public static final String MENU_ADMIN_SYSINFO = "admin_sysinfo";
  public static final String MENU_ADMIN_SYSINFO_ENVVARS = "admin_sysinfo_envvars";
  public static final String MENU_ADMIN_SYSINFO_REQUEST = "admin_sysinfo_request";
  public static final String MENU_ADMIN_SYSINFO_SYSPROPS = "admin_sysinfo_sysprops";
  public static final String MENU_ADMIN_SYSINFO_THREADS = "admin_sysinfo_threads";
  public static final String MENU_ADMIN_SYSINFO_TIMEZONES = "admin_sysinfo_timezones";

  public static final String MENU_SAVED_STATES = "saved_states";

  private CDemoAppMenuConfig ()
  {}
}
