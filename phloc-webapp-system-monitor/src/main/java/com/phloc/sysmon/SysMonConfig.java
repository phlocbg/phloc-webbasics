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
package com.phloc.sysmon;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.email.EmailAddress;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.string.StringHelper;
import com.phloc.scopes.singleton.GlobalSingleton;
import com.phloc.settings.ISettings;
import com.phloc.settings.xchange.properties.SettingsPersistenceProperties;
import com.phloc.web.CWeb;
import com.phloc.web.smtp.impl.SMTPSettings;

/**
 * Access to the configuration file.
 * 
 * @author Philip Helger
 */
public class SysMonConfig extends GlobalSingleton {
  private static final ISettings s_aSettings;

  static {
    s_aSettings = new SettingsPersistenceProperties ().readSettings (new ClassPathResource ("sysmon.properties"));
  }

  @Deprecated
  @UsedViaReflection
  private SysMonConfig () {}

  @Nullable
  public static String getGlobalTrace () {
    return s_aSettings.getStringValue ("global.trace");
  }

  @Nullable
  public static String getGlobalDebug () {
    return s_aSettings.getStringValue ("global.debug");
  }

  @Nullable
  public static String getGlobalProduction () {
    return s_aSettings.getStringValue ("global.production");
  }

  @Nullable
  public static String getDataPath () {
    return s_aSettings.getStringValue ("global.datapath");
  }

  @Nullable
  public static IEmailAddress getEmailSender () {
    final String sFrom = s_aSettings.getStringValue ("email.from");
    if (StringHelper.hasText (sFrom))
      return new EmailAddress (sFrom);
    return null;
  }

  @Nullable
  public static List <IEmailAddress> getEmailReceivers () {
    final String sTo = s_aSettings.getStringValue ("email.to");
    if (StringHelper.hasText (sTo)) {
      final List <IEmailAddress> ret = new ArrayList <IEmailAddress> ();
      for (final String sPart : StringHelper.getExploded (',', sTo))
        ret.add (new EmailAddress (sPart));
      return ret;
    }
    return null;
  }

  @Nonnull
  public static SMTPSettings getSMTPSettings () {
    return new SMTPSettings (s_aSettings.getStringValue ("smtp.hostname", "brzmg-fb00.brz.gv.at"),
                             s_aSettings.getIntValue ("smtp.port", CWeb.DEFAULT_PORT_SMTP),
                             s_aSettings.getStringValue ("smtp.username", null),
                             s_aSettings.getStringValue ("smtp.password", null),
                             s_aSettings.getStringValue ("smtp.charset", CCharset.CHARSET_UTF_8),
                             s_aSettings.getBooleanValue ("smtp.usessl", false));
  }
}
