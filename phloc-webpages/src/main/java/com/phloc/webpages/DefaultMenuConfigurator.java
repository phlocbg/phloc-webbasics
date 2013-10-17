package com.phloc.webpages;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.menu.IMenuItem;
import com.phloc.appbasics.app.menu.IMenuItemPage;
import com.phloc.appbasics.app.menu.IMenuObject;
import com.phloc.appbasics.app.menu.IMenuTree;
import com.phloc.appbasics.security.audit.IAuditManager;
import com.phloc.commons.filter.IFilter;
import com.phloc.webbasics.app.page.system.PageShowChildren;
import com.phloc.webpages.monitoring.BasePageAudit;
import com.phloc.webpages.monitoring.BasePageLoginInfo;
import com.phloc.webpages.monitoring.BasePageScopes;
import com.phloc.webpages.monitoring.BasePageSessions;
import com.phloc.webpages.security.BasePageRoleManagement;
import com.phloc.webpages.security.BasePageUserGroupManagement;
import com.phloc.webpages.security.BasePageUserManagement;
import com.phloc.webpages.settings.BasePageSettingsGlobal;
import com.phloc.webpages.sysinfo.BasePageSysInfoChangeLogs;
import com.phloc.webpages.sysinfo.BasePageSysInfoEnvironmentVariables;
import com.phloc.webpages.sysinfo.BasePageSysInfoLanguages;
import com.phloc.webpages.sysinfo.BasePageSysInfoRequest;
import com.phloc.webpages.sysinfo.BasePageSysInfoSystemProperties;
import com.phloc.webpages.sysinfo.BasePageSysInfoThirdPartyLibraries;
import com.phloc.webpages.sysinfo.BasePageSysInfoThreads;
import com.phloc.webpages.sysinfo.BasePageSysInfoTimeZones;

@Immutable
public final class DefaultMenuConfigurator
{
  public static final String MENU_ADMIN_SECURITY = "admin_security";
  public static final String MENU_ADMIN_SECURITY_USER = "admin_security_user";
  public static final String MENU_ADMIN_SECURITY_USER_GROUP = "admin_security_usergroup";
  public static final String MENU_ADMIN_SECURITY_ROLE = "admin_security_role";
  public static final String MENU_ADMIN_MONITORING = "admin_monitoring";
  public static final String MENU_ADMIN_MONITORING_AUDIT = "admin_monitoring_audit";
  public static final String MENU_ADMIN_MONITORING_LOGININFO = "admin_monitoring_logininfo";
  public static final String MENU_ADMIN_MONITORING_SCOPES = "admin_monitoring_scopes";
  public static final String MENU_ADMIN_MONITORING_SESSIONS = "admin_monitoring_sessions";
  public static final String MENU_ADMIN_SETTINGS = "admin_settings";
  public static final String MENU_ADMIN_SETTINGS_GLOBAL = "admin_settings_global";
  public static final String MENU_ADMIN_SYSINFO = "admin_sysinfo";
  public static final String MENU_ADMIN_SYSINFO_CHANGELOGS = "admin_sysinfo_changelog";
  public static final String MENU_ADMIN_SYSINFO_ENVVARS = "admin_sysinfo_envvars";
  public static final String MENU_ADMIN_SYSINFO_LANGUAGES = "admin_sysinfo_languages";
  public static final String MENU_ADMIN_SYSINFO_REQUEST = "admin_sysinfo_request";
  public static final String MENU_ADMIN_SYSINFO_SYSPROPS = "admin_sysinfo_sysprops";
  public static final String MENU_ADMIN_SYSINFO_THIRDPARTYLIBS = "admin_sysinfo_thirdpartylibs";
  public static final String MENU_ADMIN_SYSINFO_THREADS = "admin_sysinfo_threads";
  public static final String MENU_ADMIN_SYSINFO_TIMEZONES = "admin_sysinfo_timezones";

  private DefaultMenuConfigurator ()
  {}

  @Nonnull
  public static IMenuItemPage addMonitoringItems (@Nonnull final IMenuTree aMenuTree,
                                                  @Nonnull final IMenuItem aParent,
                                                  @Nullable final IFilter <IMenuObject> aDisplayFilter,
                                                  @Nonnull final IAuditManager aAuditManager)
  {
    final IMenuItemPage aAdminMonitoring = aMenuTree.createItem (aParent,
                                                                 new PageShowChildren (MENU_ADMIN_MONITORING,
                                                                                       EWebPageText.PAGE_NAME_MONITORING.getAsMLT (),
                                                                                       aMenuTree))
                                                    .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageAudit (MENU_ADMIN_MONITORING_AUDIT, aAuditManager))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageLoginInfo (MENU_ADMIN_MONITORING_LOGININFO))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageScopes (MENU_ADMIN_MONITORING_SCOPES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminMonitoring, new BasePageSessions (MENU_ADMIN_MONITORING_SESSIONS))
             .setDisplayFilter (aDisplayFilter);
    return aAdminMonitoring;
  }

  @Nonnull
  public static IMenuItemPage addSecurityItems (@Nonnull final IMenuTree aMenuTree,
                                                @Nonnull final IMenuItem aParent,
                                                @Nullable final IFilter <IMenuObject> aDisplayFilter,
                                                @Nonnull final Locale aDefaultLocale)
  {
    final IMenuItemPage aAdminSecurity = aMenuTree.createItem (aParent,
                                                               new PageShowChildren (MENU_ADMIN_SECURITY,
                                                                                     EWebPageText.PAGE_NAME_SECURITY.getAsMLT (),
                                                                                     aMenuTree))
                                                  .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity,
                          new BasePageUserManagement (MENU_ADMIN_SECURITY_USER).setDefaultUserLocale (aDefaultLocale))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity, new BasePageUserGroupManagement (MENU_ADMIN_SECURITY_USER_GROUP))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSecurity, new BasePageRoleManagement (MENU_ADMIN_SECURITY_ROLE))
             .setDisplayFilter (aDisplayFilter);
    return aAdminSecurity;
  }

  @Nonnull
  public static IMenuItemPage addSettingsItems (@Nonnull final IMenuTree aMenuTree,
                                                @Nonnull final IMenuItem aParent,
                                                @Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    final IMenuItemPage aAdminSettings = aMenuTree.createItem (aParent,
                                                               new PageShowChildren (MENU_ADMIN_SETTINGS,
                                                                                     EWebPageText.PAGE_NAME_SETTINGS.getAsMLT (),
                                                                                     aMenuTree))
                                                  .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSettings, new BasePageSettingsGlobal (MENU_ADMIN_SETTINGS_GLOBAL))
             .setDisplayFilter (aDisplayFilter);
    return aAdminSettings;
  }

  @Nonnull
  public static IMenuItemPage addSysInfoItems (@Nonnull final IMenuTree aMenuTree,
                                               @Nonnull final IMenuItem aParent,
                                               @Nullable final IFilter <IMenuObject> aDisplayFilter)
  {
    final IMenuItemPage aAdminSysInfo = aMenuTree.createItem (aParent,
                                                              new PageShowChildren (MENU_ADMIN_SYSINFO,
                                                                                    EWebPageText.PAGE_NAME_SYSINFO.getAsMLT (),
                                                                                    aMenuTree))
                                                 .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoChangeLogs (MENU_ADMIN_SYSINFO_CHANGELOGS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoEnvironmentVariables (MENU_ADMIN_SYSINFO_ENVVARS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoLanguages (MENU_ADMIN_SYSINFO_LANGUAGES))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoRequest (MENU_ADMIN_SYSINFO_REQUEST))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoSystemProperties (MENU_ADMIN_SYSINFO_SYSPROPS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoThirdPartyLibraries (MENU_ADMIN_SYSINFO_THIRDPARTYLIBS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoThreads (MENU_ADMIN_SYSINFO_THREADS))
             .setDisplayFilter (aDisplayFilter);
    aMenuTree.createItem (aAdminSysInfo, new BasePageSysInfoTimeZones (MENU_ADMIN_SYSINFO_TIMEZONES))
             .setDisplayFilter (aDisplayFilter);
    return aAdminSysInfo;
  }
}
