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
package com.phloc.webpages;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ISimpleMultiLingualText;
import com.phloc.commons.text.impl.ReadonlyMultiLingualText;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

/**
 * Contains some web page base class texts.
 * 
 * @author Philip Helger
 */
@Translatable
public enum EWebPageText implements IHasDisplayText, IHasDisplayTextWithArgs
{
  OBJECT_COPY ("Kopiere ''{0}''", "Copy ''{0}''"),
  OBJECT_DELETE ("Lösche ''{0}''", "Delete ''{0}''"),
  OBJECT_EDIT ("Bearbeite ''{0}''", "Edit ''{0}''"),
  IMAGE_NONE ("keines", "none"),
  PAGE_NAME_MONITORING ("Überwachung", "Monitoring"),
  PAGE_NAME_MONITORING_AUDIT ("Audit-Einträge", "Audit items"),
  PAGE_NAME_MONITORING_LOGIN_INFO ("Angemeldete Benutzer", "Logged in users"),
  PAGE_NAME_MONITORING_SCOPES ("Kontexte", "Scopes"),
  PAGE_NAME_MONITORING_SESSIONS ("Sessions", "Sessions"),
  PAGE_NAME_SECURITY ("Sicherheit", "Security"),
  PAGE_NAME_SECURITY_ROLES ("Rollenverwaltung", "Role management"),
  PAGE_NAME_SECURITY_USER_GROUPS ("Benutzergruppenverwaltung", "User group management"),
  PAGE_NAME_SECURITY_USERS ("Benutzerverwaltung", "User management"),
  PAGE_NAME_SETTINGS ("System Einstellungen", "System settings"),
  PAGE_NAME_SETTINGS_GLOBAL ("Globale Einstellungen", "Global settings"),
  PAGE_NAME_SYSINFO ("System Informationen", "System information"),
  PAGE_NAME_SYSINFO_CHANGELOGS ("Änderungshistorie", "Change logs"),
  PAGE_NAME_SYSINFO_ENV_VARS ("Umgebungsvariablen", "Environment variables"),
  PAGE_NAME_SYSINFO_LANGUAGES ("Sprachen", "Languages"),
  PAGE_NAME_SYSINFO_NETWORK ("Netzwerk", "Network"),
  PAGE_NAME_SYSINFO_REQUEST ("Request", "Request"),
  PAGE_NAME_SYSINFO_SYSPROPS ("Systemeinstellungen", "System properties"),
  PAGE_NAME_SYSINFO_THIRDPARTYLIBS ("Externe Module", "Thirdparty libraries"),
  PAGE_NAME_SYSINFO_THREADS ("Threads", "Threads"),
  PAGE_NAME_SYSINFO_TIMEZONES ("Zeitzonen", "Time zones");

  private final ISimpleMultiLingualText m_aTP;

  private EWebPageText (final String sDE, final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  @Nonnull
  public IReadonlyMultiLingualText getAsMLT ()
  {
    return new ReadonlyMultiLingualText (m_aTP);
  }

  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }

  @Nullable
  public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
  }
}
