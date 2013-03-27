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
  public static final String MENU_ADMIN_SECURITY_USER = "admin_user";
  public static final String MENU_ADMIN_MONITORING = "admin_monitoring";
  public static final String MENU_ADMIN_MONITORING_AUDIT = "admin_audit";

  // Person management
  public static final String MENU_BASEPERSONMGMT = "person_basegmt";
  public static final String MENU_PERSONMGMT = "person_personmgmt";
  public static final String MENU_COMPANYMGMT = "person_companymgmt";

  public static final String MENU_CONFIG = "config";
  public static final String MENU_CONFIG_MASTERDATA = "config_masterdata";
  public static final String MENU_CONFIG_PERSONCATEGORYMGMT = "config_categorymgmt";
  public static final String MENU_CONFIG_BANKACCOUNTMGMT = "config_bankaccountmgmt";

  public static final String MENU_SHOP = "shop";
  public static final String MENU_SHOP_CONFIG = "shop_config";
  public static final String MENU_SHOP_ORDERWORKFLOWMGMT = "shop_orderworkflowmgmt";
  public static final String MENU_SHOP_ARTICLECATALOGUEMGMT = "shop_articlecataloguemgmt";
  public static final String MENU_SHOP_ARTICLEMGMT = "shop_articlemgmt";
  public static final String MENU_SHOP_ARTICLE_EXPORT = "shop_article_export";
  public static final String MENU_SHOP_ARTICLE_IMPORT = "shop_article_import";
  public static final String MENU_SHOP_ORDERMGMT = "shop_ordermgmt";

  public static final String MENU_SAVED_STATES = "saved_states";

  private CDemoAppMenuConfig ()
  {}
}
