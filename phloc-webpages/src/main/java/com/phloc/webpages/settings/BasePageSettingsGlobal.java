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
package com.phloc.webpages.settings;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with global basic settings
 * 
 * @author Philip Helger
 */
public class BasePageSettingsGlobal extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_HEADER_GLOBAL ("Globale Einstellungen", "Global settings"),
    MSG_GLOBAL_DEBUG ("Debug-Modus", "Debug mode"),
    MSG_GLOBAL_PRODUCTION ("Produktions-Modus", "Production mode"),
    MSG_HEADER_EMAIL ("E-Mail Standard Einstellungen", "Email default settings"),
    MSG_EMAIL_MAILQUEUE_LENGTH ("E-Mail Queue Länge", "Email queue length"),
    MSG_EMAIL_MAX_SEND_COUNT ("Maximal versendete Mails", "Maximum send count"),
    MSG_EMAIL_USE_SSL ("SSL", "SSL"),
    MSG_EMAIL_USE_STARTTLS ("STARTTLS", "STARTTLS"),
    MSG_EMAIL_CONNECTION_TIMEOUT ("Verbindungs-Timeout", "Connection timeout"),
    MSG_EMAIL_SOCKET_TIMEOUT ("Socket-Timeout", "Socket timeout"),
    MSG_EMAIL_CONNECTION_LISTENER ("ConnectionListener", "ConnectionListener"),
    MSG_EMAIL_TRANSPORT_LISTENER ("TransportListener", "TransportListener"),
    MSG_CHANGE_SUCCESS ("Die Einstellungen wurden erfolgreich gespeichert.", "Changes were changed successfully.");

    private final ITextProvider m_aTP;

    private EText (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  private static final String FIELD_GLOBAL_DEBUG = "global-debug";
  private static final String FIELD_GLOBAL_PRODUCTION = "global-production";

  public BasePageSettingsGlobal (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SETTINGS_GLOBAL.getAsMLT ());
  }

  public BasePageSettingsGlobal (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSettingsGlobal (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSettingsGlobal (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    if (aWPEC.hasAttr (CHCParam.PARAM_ACTION, CHCParam.ACTION_SAVE))
    {
      // Save changes
      final boolean bGlobalDebug = aWPEC.getCheckBoxAttr (FIELD_GLOBAL_DEBUG, GlobalDebug.isDebugMode ());
      final boolean bGlobalProduction = aWPEC.getCheckBoxAttr (FIELD_GLOBAL_PRODUCTION, GlobalDebug.isProductionMode ());

      GlobalDebug.setDebugModeDirect (bGlobalDebug);
      GlobalDebug.setProductionModeDirect (bGlobalProduction);

      aNodeList.addChild (getStyler ().createSuccessBox (EText.MSG_CHANGE_SUCCESS.getDisplayText (aDisplayLocale)));
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    final ITabBox <?> aTabBox = getStyler ().createTabBox ();

    // GlobalDebug
    {
      final IHCTableForm <?> aTable = getStyler ().createTableForm (new HCCol (170), HCCol.star ());
      aTable.createItemRow ()
            .setLabel (EText.MSG_GLOBAL_DEBUG.getDisplayText (aDisplayLocale))
            .setCtrl (new HCCheckBox (FIELD_GLOBAL_DEBUG, GlobalDebug.isDebugMode ()));
      aTable.createItemRow ()
            .setLabel (EText.MSG_GLOBAL_PRODUCTION.getDisplayText (aDisplayLocale))
            .setCtrl (new HCCheckBox (FIELD_GLOBAL_PRODUCTION, GlobalDebug.isProductionMode ()));
      aTabBox.addTab (EText.MSG_HEADER_GLOBAL.getDisplayText (aDisplayLocale), aTable);
    }

    // Email global settings
    {
      final IHCTableForm <?> aTable = getStyler ().createTableForm (new HCCol (170), HCCol.star ());
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_MAILQUEUE_LENGTH.getDisplayText (aDisplayLocale))
            .setCtrl (Long.toString (EmailGlobalSettings.getMaxMailQueueLength ()));
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_MAX_SEND_COUNT.getDisplayText (aDisplayLocale))
            .setCtrl (Long.toString (EmailGlobalSettings.getMaxMailSendCount ()));
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_USE_SSL.getDisplayText (aDisplayLocale))
            .setCtrl (EWebBasicsText.getYesOrNo (EmailGlobalSettings.isUseSSL (), aDisplayLocale));
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_USE_STARTTLS.getDisplayText (aDisplayLocale))
            .setCtrl (EWebBasicsText.getYesOrNo (EmailGlobalSettings.isUseSTARTTLS (), aDisplayLocale));
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_CONNECTION_TIMEOUT.getDisplayText (aDisplayLocale))
            .setCtrl (Long.toString (EmailGlobalSettings.getConnectionTimeoutMilliSecs ()) + "ms");
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_SOCKET_TIMEOUT.getDisplayText (aDisplayLocale))
            .setCtrl (Long.toString (EmailGlobalSettings.getTimeoutMilliSecs ()) + "ms");
      aTabBox.addTab (EText.MSG_HEADER_EMAIL.getDisplayText (aDisplayLocale), aTable);
    }

    aForm.addChild (aTabBox);

    final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, CHCParam.ACTION_SAVE);
    aToolbar.addSubmitButtonSave (aDisplayLocale);
  }
}
