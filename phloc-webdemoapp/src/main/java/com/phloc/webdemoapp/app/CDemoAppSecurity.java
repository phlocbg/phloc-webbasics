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
package com.phloc.webdemoapp.app;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.security.CSecurity;
import com.phloc.commons.collections.ContainerHelper;

@Immutable
public final class CDemoAppSecurity
{
  // Security role IDs
  public static final String ROLEID_CONFIG = "config";
  public static final String ROLEID_VIEW = "view";

  // User group IDs
  public static final String USERGROUPID_SUPERUSER = CSecurity.USERGROUP_ADMINISTRATORS_ID;
  public static final String USERGROUPID_CONFIG = "ugconfig";
  public static final String USERGROUPID_VIEW = "ugview";

  public static final List <String> REQUIRED_ROLE_IDS_CONFIG = ContainerHelper.newUnmodifiableList (ROLEID_CONFIG);
  public static final List <String> REQUIRED_ROLE_IDS_VIEW = ContainerHelper.newUnmodifiableList (ROLEID_VIEW);

  private CDemoAppSecurity ()
  {}
}
