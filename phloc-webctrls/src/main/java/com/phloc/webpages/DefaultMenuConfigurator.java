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
package com.phloc.webpages;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.migration.SystemMigrationManager;
import com.phloc.appbasics.security.audit.IAuditManager;
import com.phloc.commons.filter.IFilter;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webbasics.app.page.system.PageShowChildren;
import com.phloc.webbasics.smtp.NamedSMTPSettingsManager;
import com.phloc.webpages.data.BasePageDataCountries;
import com.phloc.webpages.data.BasePageDataCurrencies;
import com.phloc.webpages.data.BasePageDataLanguages;
import com.phloc.webpages.data.BasePageDataTimeZones;
import com.phloc.webpages.monitoring.BasePageAudit;
import com.phloc.webpages.monitoring.BasePageFailedMails;
import com.phloc.webpages.monitoring.BasePageLoginInfo;
import com.phloc.webpages.monitoring.BasePageScopes;
import com.phloc.webpages.monitoring.BasePageSessions;
import com.phloc.webpages.monitoring.BasePageSystemMigrations;
import com.phloc.webpages.security.BasePageRoleManagement;
import com.phloc.webpages.security.BasePageUserGroupManagement;
import com.phloc.webpages.security.BasePageUserManagement;
import com.phloc.webpages.settings.BasePageSettingsGlobal;
import com.phloc.webpages.settings.BasePageSettingsSMTP;
import com.phloc.webpages.sysinfo.BasePageSysInfoChangeLogs;
import com.phloc.webpages.sysinfo.BasePageSysInfoEnvironmentVariables;
import com.phloc.webpages.sysinfo.BasePageSysInfoNetwork;
import com.phloc.webpages.sysinfo.BasePageSysInfoRequest;
import com.phloc.webpages.sysinfo.BasePageSysInfoSystemProperties;
import com.phloc.webpages.sysinfo.BasePageSysInfoThirdPartyLibraries;
import com.phloc.webpages.sysinfo.BasePageSysInfoThreads;

@Immutable
public final class DefaultMenuConfigurator
{
  public static final String MENU_ADMIN_SECURITY = "admin_security";
  public static final String MENU_ADMIN_SECURITY_USER = "admin_security_user";
  public static final String MENU_ADMIN_SECURITY_USER_GROUP = "admin_security_usergroup";
  public static final String MENU_ADMIN_SECURITY_ROLE = "admin_security_role";
  public static final String MENU_ADMIN_MONITORING = "admin_monitoring";
  public static final String MENU_ADMIN_MONITORING_AUDIT = "admin_monitoring_audit";
  public static final String MENU_ADMIN_MONITORING_FAILEDMAILS = "admin_monitoring_failedmails";
  public static final String MENU_ADMIN_MONITORING_LOGININFO = "admin_monitoring_logininfo";
  public static final String MENU_ADMIN_MONITORING_SCOPES = "admin_monitoring_scopes";
  public static final String MENU_ADMIN_MONITORING_SESSIONS = "admin_monitoring_sessions";
  public static final String MENU_ADMIN_MONITORING_SYSTEMMIGRATIONS = "admin_monitoring_systemmigrations";
  public static final String MENU_ADMIN_SETTINGS = "admin_settings";
  public static final String MENU_ADMIN_SETTINGS_GLOBAL = "admin_settings_global";
  public static final String MENU_ADMIN_SETTINGS_SMTP = "admin_settings_smtp";
  public static final String MENU_ADMIN_SYSINFO = "admin_sysinfo";
  public static final String MENU_ADMIN_SYSINFO_CHANGELOGS = "admin_sysinfo_changelog";
  public static final String MENU_ADMIN_SYSINFO_ENVVARS = "admin_sysinfo_envvars";
  public static final String MENU_ADMIN_SYSINFO_NETWORK = "admin_sysinfo_network";
  public static final String MENU_ADMIN_SYSINFO_REQUEST = "admin_sysinfo_request";
  public static final String MENU_ADMIN_SYSINFO_SYSPROPS = "admin_sysinfo_sysprops";
  public static final String MENU_ADMIN_SYSINFO_THIRDPARTYLIBS = "admin_sysinfo_thirdpartylibs";
  public static final String MENU_ADMIN_SYSINFO_THREADS = "admin_sysinfo_threads";
  public static final String MENU_ADMIN_DATA = "admin_data";
  public static final String MENU_ADMIN_DATA_COUNTRIES = "admin_data_countries";
  public static final String MENU_ADMIN_DATA_CURRENCIES = "admin_data_currencies";
  public static final String MENU_ADMIN_DATA_LANGUAGES = "admin_data_languages";
  public static final String MENU_ADMIN_DATA_TIMEZONES = "admin_data_timezones";

  private DefaultMenuConfigurator ()
  {}

  @Nonnull
  public static <WPECTYPE extends IWebPageExecutionContext> IMenuItemPage addMonitoringItems (@Nonnull final IMenuTree aMenuTree,
                                                                                              @Nonnull final IMenuItem aParent,
                                                                                              @Nullable final IFilter <IMenuObject> aDisplayFilter,
                                                                                              @Nonnull final IAuditManager aAuditMgr,
                                                                                              @Nonnull final FailedMailQueue aFailedMailQueue,
                                                                                              @Nonnull final SystemMigrationManager aSystemMigrationMgr)
  {
    final IMenuItemPage aAdminMonitoring = aMenuTree.createItem (aParent,
                                                                 new PageShowChildren <WPECTYPE> (MENU_ADMIN_MONITORING,
                                                                                                  EWebPageText.PAGE_NAME_MONITORING.getAsMLT (),
                                                                                                  aMenuTree))
                                                    .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageAudit <WPECTYPE> (MENU_ADMIN_MONITORING_AUDIT, aAuditMgr))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring,
                          new BasePageFailedMails <WPECTYPE> (MENU_ADMIN_MONITORING_FAILEDMAILS, aFailedMailQueue))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageLoginInfo <WPECTYPE> (MENU_ADMIN_MONITORING_LOGININFO))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageScopes <WPECTYPE> (MENU_ADMIN_MONITORING_SCOPES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageSessions <WPECTYPE> (MENU_ADMIN_MONITORING_SESSIONS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring,
                          new BasePageSystemMigrations <WPECTYPE> (MENU_ADMIN_MONITORING_SYSTEMMIGRATIONS,
                                                                   aSystemMigrationMgr))
             .setDisplayFilter (aDisplayFilter);
    return aAdminMonitoring;
  }

  @Nonnull
  public static <WPECTYPE extends IWebPageExecutionContext> IMenuItemPage addSecurityItems (@Nonnull final IMenuTree aMenuTree,
                                                                                            @Nonnull final IMenuItem aParent,
                                                                                            @Nullable final IFilter <IMenuObject> aDisplayFilter,
                                                                                            @Nonnull final Locale aDefaultLocale)
  {
    final IMenuItemPage aAdminSecurity = aMenuTree.createItem (aParent,
                                                               new PageShowChildren <WPECTYPE> (MENU_ADMIN_SECURITY,
                                                                                                EWebPageText.PAGE_NAME_SECURITY.getAsMLT (),
                                                                                                aMenuTree))
                                                  .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity,
                          new BasePageUserManagement <WPECTYPE> (MENU_ADMIN_SECURITY_USER).setDefaultUserLocale (aDefaultLocale))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity, new BasePageUserGroupManagement <WPECTYPE> (MENU_ADMIN_SECURITY_USER_GROUP))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity, new BasePageRoleManagement <WPECTYPE> (MENU_ADMIN_SECURITY_ROLE))
             .setDisplayFilter (aDisplayFilter);
    return aAdminSecurity;
  }

  @Nonnull
  public static <WPECTYPE extends IWebPageExecutionContext> IMenuItemPage addSettingsItems (@Nonnull final IMenuTree aMenuTree,
                                                                                            @Nonnull final IMenuItem aParent,
                                                                                            @Nullable final IFilter <IMenuObject> aDisplayFilter,
                                                                                            @Nullable final NamedSMTPSettingsManager aNamedSMTPSettingsMgr)
  {
    final IMenuItemPage aAdminSettings = aMenuTree.createItem (aParent,
                                                               new PageShowChildren <WPECTYPE> (MENU_ADMIN_SETTINGS,
                                                                                                EWebPageText.PAGE_NAME_SETTINGS.getAsMLT (),
                                                                                                aMenuTree))
                                                  .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSettings, new BasePageSettingsGlobal <WPECTYPE> (MENU_ADMIN_SETTINGS_GLOBAL))
             .setDisplayFilter (aDisplayFilter);

    if (aNamedSMTPSettingsMgr != null)
      aMenuTree.createItem (aAdminSettings,
                            new BasePageSettingsSMTP <WPECTYPE> (aNamedSMTPSettingsMgr, MENU_ADMIN_SETTINGS_SMTP))
               .setDisplayFilter (aDisplayFilter);

    return aAdminSettings;
  }

  @Nonnull
  public static <WPECTYPE extends IWebPageExecutionContext> IMenuItemPage addSysInfoItems (@Nonnull final IMenuTree aMenuTree,
                                                                                           @Nonnull final IMenuItem aParent,
                                                                                           @Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    final IMenuItemPage aAdminSysInfo = aMenuTree.createItem (aParent,
                                                              new PageShowChildren <WPECTYPE> (MENU_ADMIN_SYSINFO,
                                                                                               EWebPageText.PAGE_NAME_SYSINFO.getAsMLT (),
                                                                                               aMenuTree))
                                                 .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoChangeLogs <WPECTYPE> (MENU_ADMIN_SYSINFO_CHANGELOGS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo,
                          new BasePageSysInfoEnvironmentVariables <WPECTYPE> (MENU_ADMIN_SYSINFO_ENVVARS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoNetwork <WPECTYPE> (MENU_ADMIN_SYSINFO_NETWORK))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoRequest <WPECTYPE> (MENU_ADMIN_SYSINFO_REQUEST))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoSystemProperties <WPECTYPE> (MENU_ADMIN_SYSINFO_SYSPROPS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo,
                          new BasePageSysInfoThirdPartyLibraries <WPECTYPE> (MENU_ADMIN_SYSINFO_THIRDPARTYLIBS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoThreads <WPECTYPE> (MENU_ADMIN_SYSINFO_THREADS))
             .setDisplayFilter (aDisplayFilter);
    return aAdminSysInfo;
  }

  @Nonnull
  public static <WPECTYPE extends IWebPageExecutionContext> IMenuItemPage addDataItems (@Nonnull final IMenuTree aMenuTree,
                                                                                        @Nonnull final IMenuItem aParent,
                                                                                        @Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    final IMenuItemPage aAdminData = aMenuTree.createItem (aParent,
                                                           new PageShowChildren <WPECTYPE> (MENU_ADMIN_DATA,
                                                                                            EWebPageText.PAGE_NAME_DATA.getAsMLT (),
                                                                                            aMenuTree))
                                              .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminData, new BasePageDataCountries <WPECTYPE> (MENU_ADMIN_DATA_COUNTRIES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminData, new BasePageDataCurrencies <WPECTYPE> (MENU_ADMIN_DATA_CURRENCIES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminData, new BasePageDataLanguages <WPECTYPE> (MENU_ADMIN_DATA_LANGUAGES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminData, new BasePageDataTimeZones <WPECTYPE> (MENU_ADMIN_DATA_TIMEZONES))
             .setDisplayFilter (aDisplayFilter);
    return aAdminData;
  }
}
