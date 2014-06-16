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
package com.phloc.webpages.settings;

import java.nio.charset.Charset;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.CGlobal;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.email.EmailAddress;
import com.phloc.commons.email.EmailAddressUtils;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.validation.error.FormErrors;
import com.phloc.web.CWeb;
import com.phloc.web.CWebCharset;
import com.phloc.web.port.CNetworkPort;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.web.smtp.impl.ReadonlySMTPSettings;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.form.RequestField;
import com.phloc.webbasics.form.RequestFieldBoolean;
import com.phloc.webbasics.smtp.CNamedSMTPSettings;
import com.phloc.webbasics.smtp.NamedSMTPSettings;
import com.phloc.webbasics.smtp.NamedSMTPSettingsManager;
import com.phloc.webctrls.autonumeric.HCAutoNumericInt;
import com.phloc.webctrls.autosize.HCTextAreaAutosize;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.famfam.EFamFamIcon;
import com.phloc.webpages.AbstractWebPageFormExt;
import com.phloc.webpages.EWebPageText;
import com.phloc.webpages.ui.HCCharsetSelect;
import com.phloc.webscopes.smtp.ScopedMailAPI;

public class BasePageSettingsSMTP extends AbstractWebPageFormExt <NamedSMTPSettings>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_CREATE_NEW ("Neue SMTP-Einstellungen anlegen", "Create new SMTP settings"),
    HEADER_NAME ("Name", "Name"),
    HEADER_HOST ("Host-Name", "Host name"),
    HEADER_USERNAME ("Benutzername", "User name"),
    HEADER_DETAILS ("Details von SMTP-Einstellungen ''{0}''", "Details of SMTP settings ''{0}''"),
    LABEL_NAME ("Name", "Name"),
    LABEL_HOSTNAME ("Host", "Host name"),
    LABEL_PORT ("Port", "Port"),
    LABEL_USERNAME ("Benutzername", "User name"),
    LABEL_PASSWORD ("Passwort", "Password"),
    LABEL_CHARSET ("Zeichensatz", "Character set"),
    LABEL_SSL ("Verwende SSL?", "Use SSL?"),
    LABEL_STARTTLS ("Verwende STARTTLS?", "Use STARTTLS?"),
    LABEL_CONNECTION_TIMEOUT ("Verbindungs-Timeout (ms)", "Connection timeout (ms)"),
    LABEL_SOCKET_TIMEOUT ("Sende-Timeout (ms)", "Socket timeout (ms)"),
    MSG_NO_PASSWORD_SET ("keines gesetzt", "none defined"),
    TITLE_CREATE ("Neue SMTP-Einstellungen anlegen", "Create new SMTP settings"),
    TITLE_EDIT ("SMTP-Einstellungen ''{0}'' bearbeiten", "Edit SMTP settings ''{0}''"),
    ERROR_NAME_EMPTY ("Es muss ein Name für diese SMTP-Einstellungen angegeben werden!", "A name must be provided for these SMTP settings!"),
    ERROR_HOSTNAME_EMPTY ("Es muss ein Host-Name oder eine IP-Adresse des SMTP-Servers angegeben werden!", "A name or IP address of the SMTP server must be provided!"),
    ERROR_PORT_INVALID ("Der angegebene Port ist ungültig. Gültige Ports liegen zwischen {0} und {1}!", "The provided port is invalid. Valid ports must be between {0} and {1}!"),
    ERROR_CHARSET_INVALID ("Der ausgewählte Zeichensatz ist ungültig!", "The selected character set is invalid!"),
    ERROR_CONNECTION_TIMEOUT_INVALID ("Das Verbindungs-Timeout muss größer oder gleich 0 sein!", "The connection timeout must be greater or equal to 0!"),
    ERROR_SOCKET_TIMEOUT_INVALID ("Das Verbindungs-Timeout muss größer oder gleich 0 sein!", "The connection timeout must be greater or equal to 0!"),
    SUCCESS_CREATE ("Die neue SMTP-Einstellungen wurden erfolgreich angelegt!", "Successfully created the new SMTP settings!"),
    SUCCESS_EDIT ("Die SMTP-Einstellungen wurde erfolgreich bearbeitet!", "Sucessfully edited the SMTP settings!"),
    DELETE_QUERY ("Sollen die SMTP-Einstellungen ''{0}'' wirklich gelöscht werden?", "Are you sure to delete the SMTP settings ''{0}''?"),
    DELETE_SUCCESS ("Die SMTP-Einstellungen ''{0}'' wurden erfolgreich gelöscht!", "The SMTP settings ''{0}'' were successfully deleted!"),
    DELETE_ERROR ("Fehler beim Löschen der SMTP-Einstellungen ''{0}''!", "Error deleting the SMTP settings ''{0}''!"),
    MSG_SEND_TEST_MAIL ("Test-E-Mail senden", "Send test mail"),
    BUTTON_SEND_TEST_MAIL ("Test-E-Mail senden", "Send test mail"),
    MSG_SENDER ("Absender", "Sender"),
    MSG_RECEIVER ("Empfänger", "Receiver"),
    MSG_SUBJECT ("Betreff", "Subject"),
    MSG_BODY ("Inhalt", "Body"),
    ERR_SENDER_INVALID ("Es muss eine gültige E-Mail-Adresse angegeben werden.", "A valid email address must be provided"),
    ERR_RECEIVER_INVALID ("Es muss eine gültige E-Mail-Adresse angegeben werden.", "A valid email address must be provided"),
    ERR_SUBJECT_INVALID ("Es muss Betreff angegeben werden.", "An email subject must be provided"),
    ERR_BODY_INVALID ("Es muss eine gültige Nachricht angegeben werden.", "A valid email message must be provided"),
    SUCCESS_TEST_MAIL ("Die Test-Nachricht wurde zum Versand übermittelt.", "The test email message was scheduled for sending.");

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

    @Nullable
    public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
    {
      return DefaultTextResolver.getTextWithArgs (this, m_aTP, aContentLocale, aArgs);
    }
  }

  private static final long serialVersionUID = -1l;

  private static final String FIELD_NAME = "name";
  private static final String FIELD_HOSTNAME = "hostname";
  private static final String FIELD_PORT = "port";
  private static final String FIELD_USERNAME = "username";
  private static final String FIELD_PASSWORD = "password";
  private static final String FIELD_CHARSET = "charset";
  private static final String FIELD_SSL = "ssl";
  private static final String FIELD_STARTTLS = "starttls";
  private static final String FIELD_CONNECTION_TIMEOUT = "ctimeout";
  private static final String FIELD_SOCKET_TIMEOUT = "stimeout";

  private static final String FIELD_TEST_SENDER = "tsender";
  private static final String FIELD_TEST_RECEIVER = "treceiver";
  private static final String FIELD_TEST_SUBJECT = "tsubject";
  private static final String FIELD_TEST_BODY = "tbody";

  private static final String DEFAULT_CHARSET = CWebCharset.CHARSET_SMTP;
  private static final String ACTION_TEST_MAIL = "testmail";

  private final transient NamedSMTPSettingsManager m_aMgr;

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr, @Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SETTINGS_SMTP.getAsMLT ());
    m_aMgr = ValueEnforcer.notNull (aMgr, "NamedSMTPSettingsManager");
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
    m_aMgr = ValueEnforcer.notNull (aMgr, "NamedSMTPSettingsManager");
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull final String sName,
                               @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
    m_aMgr = ValueEnforcer.notNull (aMgr, "NamedSMTPSettingsManager");
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull final IReadonlyMultiLingualText aName,
                               @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
    m_aMgr = ValueEnforcer.notNull (aMgr, "NamedSMTPSettingsManager");
  }

  @Override
  @Nullable
  protected String getObjectDisplayName (@Nonnull final NamedSMTPSettings aSelectedObject)
  {
    return aSelectedObject.getName ();
  }

  @Override
  @Nullable
  protected NamedSMTPSettings getSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final String sID)
  {
    return m_aMgr.getSettings (sID);
  }

  private static boolean _canDelete (@Nonnull final NamedSMTPSettings aSettings)
  {
    return !aSettings.getID ().equals (CNamedSMTPSettings.NAMED_SMTP_SETTINGS_DEFAULT_ID);
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final NamedSMTPSettings aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final ISMTPSettings aSettings = aSelectedObject.getSMTPSettings ();

    final IHCTableFormView <?> aTable = aNodeList.addAndReturnChild (getStyler ().createTableFormView (new HCCol (170),
                                                                                                       HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  aSelectedObject.getName ()));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_NAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getName ());

    aTable.createItemRow ()
          .setLabel (EText.LABEL_HOSTNAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSettings.getHostName ());

    aTable.createItemRow ()
          .setLabel (EText.LABEL_PORT.getDisplayText (aDisplayLocale))
          .setCtrl (Integer.toString (aSettings.getPort ()));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_USERNAME.getDisplayText (aDisplayLocale))
          .setCtrl (aSettings.getUserName ());

    aTable.createItemRow ()
          .setLabel (EText.LABEL_PASSWORD.getDisplayText (aDisplayLocale))
          .setCtrl (StringHelper.hasText (aSettings.getPassword ()) ? "***"
                                                                   : EText.MSG_NO_PASSWORD_SET.getDisplayText (aDisplayLocale));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_CHARSET.getDisplayText (aDisplayLocale))
          .setCtrl (aSettings.getCharset ());

    aTable.createItemRow ()
          .setLabel (EText.LABEL_SSL.getDisplayText (aDisplayLocale))
          .setCtrl (EWebBasicsText.getYesOrNo (aSettings.isSSLEnabled (), aDisplayLocale));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_STARTTLS.getDisplayText (aDisplayLocale))
          .setCtrl (EWebBasicsText.getYesOrNo (aSettings.isSTARTTLSEnabled (), aDisplayLocale));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_CONNECTION_TIMEOUT.getDisplayText (aDisplayLocale))
          .setCtrl (Long.toString (aSettings.getConnectionTimeoutMilliSecs ()));

    aTable.createItemRow ()
          .setLabel (EText.LABEL_SOCKET_TIMEOUT.getDisplayText (aDisplayLocale))
          .setCtrl (Long.toString (aSettings.getTimeoutMilliSecs ()));
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final NamedSMTPSettings aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final String sName = aWPEC.getAttr (FIELD_NAME);
    final String sHostName = aWPEC.getAttr (FIELD_HOSTNAME);
    final String sPort = aWPEC.getAttr (FIELD_PORT);
    final int nPort = StringParser.parseInt (sPort, CGlobal.ILLEGAL_UINT);
    final String sUserName = aWPEC.getAttr (FIELD_USERNAME);
    String sPassword = aWPEC.getAttr (FIELD_PASSWORD);
    if (sPassword == null && aSelectedObject != null)
    {
      // Password is not changed
      sPassword = aSelectedObject.getSMTPSettings ().getPassword ();
    }
    final String sCharset = aWPEC.getAttr (FIELD_CHARSET);
    Charset aCharset = null;
    try
    {
      aCharset = CharsetManager.getCharsetFromName (sCharset);
    }
    catch (final IllegalArgumentException ex)
    {}
    final boolean bSSLEnabled = aWPEC.getCheckBoxAttr (FIELD_SSL, EmailGlobalSettings.isUseSSL ());
    final boolean bSTARTTLSEnabled = aWPEC.getCheckBoxAttr (FIELD_STARTTLS, EmailGlobalSettings.isUseSTARTTLS ());
    final long nConnectionTimeoutMS = aWPEC.getLongAttr (FIELD_CONNECTION_TIMEOUT, CGlobal.ILLEGAL_ULONG);
    final long nSocketTimeoutMS = aWPEC.getLongAttr (FIELD_SOCKET_TIMEOUT, CGlobal.ILLEGAL_ULONG);

    if (StringHelper.hasNoText (sName))
      aFormErrors.addFieldError (FIELD_NAME, EText.ERROR_NAME_EMPTY.getDisplayText (aDisplayLocale));

    if (StringHelper.hasNoText (sHostName))
      aFormErrors.addFieldError (FIELD_HOSTNAME, EText.ERROR_HOSTNAME_EMPTY.getDisplayText (aDisplayLocale));

    if (nPort < CNetworkPort.MINIMUM_PORT_NUMBER || nPort > CNetworkPort.MAXIMUM_PORT_NUMBER)
      aFormErrors.addFieldError (FIELD_PORT,
                                 EText.ERROR_PORT_INVALID.getDisplayTextWithArgs (aDisplayLocale,
                                                                                  Integer.toString (CNetworkPort.MINIMUM_PORT_NUMBER),
                                                                                  Integer.toString (CNetworkPort.MAXIMUM_PORT_NUMBER)));

    if (aCharset == null)
      aFormErrors.addFieldError (FIELD_CHARSET, EText.ERROR_CHARSET_INVALID.getDisplayText (aDisplayLocale));

    if (nConnectionTimeoutMS < 0)
      aFormErrors.addFieldError (FIELD_CONNECTION_TIMEOUT,
                                 EText.ERROR_CONNECTION_TIMEOUT_INVALID.getDisplayText (aDisplayLocale));

    if (nSocketTimeoutMS < 0)
      aFormErrors.addFieldError (FIELD_SOCKET_TIMEOUT,
                                 EText.ERROR_CONNECTION_TIMEOUT_INVALID.getDisplayText (aDisplayLocale));

    if (aFormErrors.isEmpty ())
    {
      // All fields are valid -> save
      final ReadonlySMTPSettings aSMTPSettings = new ReadonlySMTPSettings (sHostName,
                                                                           nPort,
                                                                           sUserName,
                                                                           sPassword,
                                                                           sCharset,
                                                                           bSSLEnabled,
                                                                           bSTARTTLSEnabled,
                                                                           nConnectionTimeoutMS,
                                                                           nSocketTimeoutMS);

      if (bEdit)
      {
        // We're editing an existing object
        if (m_aMgr.updateSettings (aSelectedObject.getID (), sName, aSMTPSettings).isChanged ())
          aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_EDIT.getDisplayText (aDisplayLocale)));
      }
      else
      {
        // We're creating a new object
        m_aMgr.addSettings (sName, aSMTPSettings);
        aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_CREATE.getDisplayText (aDisplayLocale)));
      }
    }
  }

  @SuppressWarnings ("null")
  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final NamedSMTPSettings aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final ISMTPSettings aSettings = aSelectedObject == null ? null : aSelectedObject.getSMTPSettings ();

    final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (170),
                                                                                           HCCol.star (),
                                                                                           new HCCol (20)));
    aTable.setSpanningHeaderContent (bEdit ? EText.TITLE_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                                      aSelectedObject.getName ())
                                          : EText.TITLE_CREATE.getDisplayText (aDisplayLocale));

    {
      final String sName = EText.LABEL_NAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sName)
            .setCtrl (new HCEdit (new RequestField (FIELD_NAME, aSelectedObject == null ? null
                                                                                       : aSelectedObject.getName ())).setPlaceholder (sName))
            .setErrorList (aFormErrors.getListOfField (FIELD_NAME));
    }

    {
      final String sHostName = EText.LABEL_HOSTNAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sHostName)
            .setCtrl (new HCEdit (new RequestField (FIELD_HOSTNAME, aSettings == null ? null : aSettings.getHostName ())).setPlaceholder (sHostName))
            .setErrorList (aFormErrors.getListOfField (FIELD_HOSTNAME));
    }

    {
      final String sPort = EText.LABEL_PORT.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sPort)
            .setCtrl (new HCAutoNumericInt (new RequestField (FIELD_PORT, aSettings == null ? CWeb.DEFAULT_PORT_SMTP
                                                                                           : aSettings.getPort ()),
                                            aDisplayLocale).setMin (CNetworkPort.MINIMUM_PORT_NUMBER)
                                                           .setMax (CNetworkPort.MAXIMUM_PORT_NUMBER))
            .setErrorList (aFormErrors.getListOfField (FIELD_PORT));
    }

    {
      final String sUserName = EText.LABEL_USERNAME.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sUserName)
            .setCtrl (new HCEdit (new RequestField (FIELD_USERNAME, aSettings == null ? null : aSettings.getUserName ())).setPlaceholder (sUserName))
            .setErrorList (aFormErrors.getListOfField (FIELD_USERNAME));
    }

    {
      final String sPassword = EText.LABEL_PASSWORD.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sPassword)
            .setCtrl (new HCEditPassword (FIELD_PASSWORD).setPlaceholder (sPassword))
            .setErrorList (aFormErrors.getListOfField (FIELD_PASSWORD));
    }

    {
      final String sCharset = EText.LABEL_CHARSET.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sCharset)
            .setCtrl (new HCCharsetSelect (new RequestField (FIELD_CHARSET, aSettings == null ? DEFAULT_CHARSET
                                                                                             : aSettings.getCharset ()),
                                           true,
                                           aDisplayLocale))
            .setErrorList (aFormErrors.getListOfField (FIELD_CHARSET));
    }

    {
      final String sSSL = EText.LABEL_SSL.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sSSL)
            .setCtrl (new HCCheckBox (new RequestFieldBoolean (FIELD_SSL,
                                                               aSettings == null ? EmailGlobalSettings.isUseSSL ()
                                                                                : aSettings.isSSLEnabled ())))
            .setErrorList (aFormErrors.getListOfField (FIELD_SSL));
    }

    {
      final String sSTARTTLS = EText.LABEL_STARTTLS.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabel (sSTARTTLS)
            .setCtrl (new HCCheckBox (new RequestFieldBoolean (FIELD_STARTTLS,
                                                               aSettings == null ? EmailGlobalSettings.isUseSTARTTLS ()
                                                                                : aSettings.isSTARTTLSEnabled ())))
            .setErrorList (aFormErrors.getListOfField (FIELD_STARTTLS));
    }

    {
      final String sConnectionTimeout = EText.LABEL_CONNECTION_TIMEOUT.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sConnectionTimeout)
            .setCtrl (new HCAutoNumericInt (new RequestField (FIELD_CONNECTION_TIMEOUT,
                                                              aSettings == null ? EmailGlobalSettings.getConnectionTimeoutMilliSecs ()
                                                                               : aSettings.getConnectionTimeoutMilliSecs ()),
                                            aDisplayLocale).setMin (0))
            .setErrorList (aFormErrors.getListOfField (FIELD_CONNECTION_TIMEOUT));
    }

    {
      final String sSocketTimeout = EText.LABEL_SOCKET_TIMEOUT.getDisplayText (aDisplayLocale);
      aTable.createItemRow ()
            .setLabelMandatory (sSocketTimeout)
            .setCtrl (new HCAutoNumericInt (new RequestField (FIELD_SOCKET_TIMEOUT,
                                                              aSettings == null ? EmailGlobalSettings.getTimeoutMilliSecs ()
                                                                               : aSettings.getTimeoutMilliSecs ()),
                                            aDisplayLocale).setMin (0))
            .setErrorList (aFormErrors.getListOfField (FIELD_SOCKET_TIMEOUT));
    }
  }

  @Nullable
  @OverrideOnDemand
  protected IHCNode getTestMailIcon ()
  {
    return EFamFamIcon.EMAIL_GO.getAsNode ();
  }

  @Override
  protected boolean handleDeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                        @Nonnull final NamedSMTPSettings aSelectedObject)
  {
    if (!_canDelete (aSelectedObject))
      throw new IllegalStateException ("Cannot delete this object!");

    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    if (aWPEC.hasSubAction (CHCParam.ACTION_SAVE))
    {
      if (m_aMgr.removeSettings (aSelectedObject.getID ()).isChanged ())
        aNodeList.addChild (getStyler ().createSuccessBox (EText.DELETE_SUCCESS.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                        aSelectedObject.getName ())));
      else
        aNodeList.addChild (getStyler ().createErrorBox (EText.DELETE_ERROR.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                    aSelectedObject.getName ())));
      return true;
    }

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf (aWPEC));
    aForm.addChild (getStyler ().createQuestionBox (EText.DELETE_QUERY.getDisplayTextWithArgs (aDisplayLocale,
                                                                                               aSelectedObject.getName ())));
    final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar (aWPEC));
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aDisplayLocale);
    aToolbar.addButtonNo (aDisplayLocale);
    return false;
  }

  @Override
  protected boolean handleCustomActions (@Nonnull final WebPageExecutionContext aWPEC,
                                         @Nullable final NamedSMTPSettings aSelectedObject)
  {
    if (aWPEC.hasAction (ACTION_TEST_MAIL) && aSelectedObject != null)
    {
      final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
      final HCNodeList aNodeList = aWPEC.getNodeList ();
      final FormErrors aFormErrors = new FormErrors ();

      if (aWPEC.hasSubAction (ACTION_PERFORM))
      {
        final String sSender = aWPEC.getAttr (FIELD_TEST_SENDER);
        final String sReceiver = aWPEC.getAttr (FIELD_TEST_RECEIVER);
        final String sSubject = aWPEC.getAttr (FIELD_TEST_SUBJECT);
        final String sBody = aWPEC.getAttr (FIELD_TEST_BODY);

        if (!EmailAddressUtils.isValid (sSender))
          aFormErrors.addFieldError (FIELD_TEST_SENDER, EText.ERR_SENDER_INVALID.getDisplayText (aDisplayLocale));
        if (!EmailAddressUtils.isValid (sReceiver))
          aFormErrors.addFieldError (FIELD_TEST_RECEIVER, EText.ERR_RECEIVER_INVALID.getDisplayText (aDisplayLocale));
        if (StringHelper.hasNoText (sSubject))
          aFormErrors.addFieldError (FIELD_TEST_SUBJECT, EText.ERR_SUBJECT_INVALID.getDisplayText (aDisplayLocale));
        if (StringHelper.hasNoText (sBody))
          aFormErrors.addFieldError (FIELD_TEST_BODY, EText.ERR_BODY_INVALID.getDisplayText (aDisplayLocale));

        if (aFormErrors.isEmpty ())
        {
          final EmailData aMailData = new EmailData (EEmailType.TEXT);
          aMailData.setFrom (new EmailAddress (sSender));
          aMailData.setTo (new EmailAddress (sReceiver));
          aMailData.setSubject (sSubject);
          aMailData.setBody (sBody);
          ScopedMailAPI.getInstance ().queueMail (aSelectedObject.getSMTPSettings (), aMailData);

          aNodeList.addChild (getStyler ().createSuccessBox (EText.SUCCESS_TEST_MAIL.getDisplayText (aDisplayLocale)));

          return true;
        }
      }

      // Show question
      final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf (aWPEC));

      final IHCTableForm <?> aTable = aForm.addAndReturnChild (getStyler ().createTableForm (new HCCol (170),
                                                                                             HCCol.star ()));
      aTable.createItemRow ()
            .setLabelMandatory (EText.MSG_SENDER.getDisplayText (aDisplayLocale))
            .setCtrl (new HCEdit (new RequestField (FIELD_TEST_SENDER)))
            .setErrorList (aFormErrors.getListOfField (FIELD_TEST_SENDER));
      aTable.createItemRow ()
            .setLabelMandatory (EText.MSG_RECEIVER.getDisplayText (aDisplayLocale))
            .setCtrl (new HCEdit (new RequestField (FIELD_TEST_RECEIVER)))
            .setErrorList (aFormErrors.getListOfField (FIELD_TEST_RECEIVER));
      aTable.createItemRow ()
            .setLabelMandatory (EText.MSG_SUBJECT.getDisplayText (aDisplayLocale))
            .setCtrl (new HCEdit (new RequestField (FIELD_TEST_SUBJECT)))
            .setErrorList (aFormErrors.getListOfField (FIELD_TEST_SUBJECT));
      aTable.createItemRow ()
            .setLabelMandatory (EText.MSG_BODY.getDisplayText (aDisplayLocale))
            .setCtrl (new HCTextAreaAutosize (new RequestField (FIELD_TEST_BODY)).setRows (5))
            .setErrorList (aFormErrors.getListOfField (FIELD_TEST_BODY));

      final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar (aWPEC));
      aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_TEST_MAIL);
      aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
      aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_PERFORM);
      aToolbar.addSubmitButton (EText.BUTTON_SEND_TEST_MAIL.getDisplayText (aDisplayLocale));
      aToolbar.addButtonCancel (aDisplayLocale);
      return false;
    }
    return true;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    // Toolbar on top
    final IButtonToolbar <?> aToolbar = aNodeList.addAndReturnChild (getStyler ().createToolbar (aWPEC));
    aToolbar.addButtonNew (EText.BUTTON_CREATE_NEW.getDisplayText (aDisplayLocale), createCreateURL (aWPEC));

    final IHCTable <?> aTable = getStyler ().createTable (HCCol.star (),
                                                          new HCCol (200),
                                                          new HCCol (150),
                                                          createActionCol (1)).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.HEADER_NAME.getDisplayText (aDisplayLocale),
                                     EText.HEADER_HOST.getDisplayText (aDisplayLocale),
                                     EText.HEADER_USERNAME.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));
    for (final NamedSMTPSettings aCurObject : m_aMgr.getAllSettings ().values ())
    {
      final ISMTPSettings aSettings = aCurObject.getSMTPSettings ();
      final ISimpleURL aViewLink = createViewURL (aWPEC, aCurObject);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aCurObject.getName ()));
      aRow.addCell (aSettings.getHostName () + ":" + aSettings.getPort ());
      aRow.addCell (aSettings.getUserName ());

      final IHCCell <?> aActionCell = aRow.addCell ();
      aActionCell.addChild (createEditLink (aWPEC,
                                            aCurObject,
                                            EWebPageText.OBJECT_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                                             aCurObject.getName ())));
      if (_canDelete (aCurObject))
        aActionCell.addChild (createDeleteLink (aWPEC,
                                                aCurObject,
                                                EWebPageText.OBJECT_DELETE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                   aCurObject.getName ())));
      else
        aActionCell.addChild (createEmptyAction ());

      aActionCell.addChild (new HCA (aWPEC.getSelfHref ()
                                          .add (CHCParam.PARAM_ACTION, ACTION_TEST_MAIL)
                                          .add (CHCParam.PARAM_OBJECT, aCurObject.getID ())).setTitle (EText.MSG_SEND_TEST_MAIL.getDisplayText (aDisplayLocale))
                                                                                            .addChild (getTestMailIcon ()));
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (3).setSortable (false).addClass (CSS_CLASS_ACTION_COL);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
