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

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.validation.error.FormErrors;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webbasics.smtp.NamedSMTPSettings;
import com.phloc.webbasics.smtp.NamedSMTPSettingsManager;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageForm;
import com.phloc.webpages.EWebPageText;

public class BasePageSettingsSMTP extends AbstractWebPageForm <NamedSMTPSettings>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    HEADER_NAME ("Name", "Name"),
    HEADER_HOST ("Host", "Host name"),
    HEADER_USERNAME ("Benutzername", "User name"),
    HEADER_DETAILS ("Details von SMTP-Einstellungen ''{0}''", "Details of SMTP settings ''{0}''"),
    LABEL_NAME ("Name", "Name"),
    LABEL_HOSTNAME ("Host", "Host name"),
    LABEL_PORT ("Port", "Port"),
    LABEL_USERNAME ("Benutzername", "User name"),
    LABEL_PASSWORD ("Passwort", "Password"),
    LABEL_CHARSET ("Zeichensatz", "Charset"),
    LABEL_SSL ("SSL?", "SSL?"),
    LABEL_STARTTLS ("STARTTLS?", "STARTTLS?"),
    LABEL_CONNECTION_TIMEOUT ("Verbindungs-Timeout", "Connection timeout"),
    LABEL_SOCKET_TIMEOUT ("Sende-Timeout", "Socket timeout"),
    MSG_NO_PASSWORD_SET ("keines gesetzt", "none defined"),
    MSG_MILLISECONDS (" ms", " ms"),
    DELETE_QUERY ("Sollen die SMTP-Einstellungen ''{0}'' wirklich gelöscht werden?", "Are you sure to delete the SMTP settings ''{0}''?"),
    DELETE_SUCCESS ("Die SMTP-Einstellungen ''{0}'' wurden erfolgreich gelöscht!", "The SMTP settings ''{0}'' were successfully deleted!"),
    DELETE_ERROR ("Fehler beim Löschen der SMTP-Einstellungen ''{0}''!", "Error deleting the SMTP settings ''{0}''!");

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

  private final NamedSMTPSettingsManager m_aMgr;

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr, @Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SETTINGS_SMTP.getAsMLT ());
    if (aMgr == null)
      throw new NullPointerException ("NamedSMTPSettingsManager");
    m_aMgr = aMgr;
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
    if (aMgr == null)
      throw new NullPointerException ("NamedSMTPSettingsManager");
    m_aMgr = aMgr;
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull final String sName,
                               @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
    if (aMgr == null)
      throw new NullPointerException ("NamedSMTPSettingsManager");
    m_aMgr = aMgr;
  }

  public BasePageSettingsSMTP (@Nonnull final NamedSMTPSettingsManager aMgr,
                               @Nonnull @Nonempty final String sID,
                               @Nonnull final IReadonlyMultiLingualText aName,
                               @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
    if (aMgr == null)
      throw new NullPointerException ("NamedSMTPSettingsManager");
    m_aMgr = aMgr;
  }

  @Override
  @Nullable
  protected NamedSMTPSettings getSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final String sID)
  {
    return m_aMgr.getSettings (sID);
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
          .setCtrl (Long.toString (aSettings.getConnectionTimeoutMilliSecs ()) +
                    EText.MSG_MILLISECONDS.getDisplayText (aDisplayLocale));
    aTable.createItemRow ()
          .setLabel (EText.LABEL_SOCKET_TIMEOUT.getDisplayText (aDisplayLocale))
          .setCtrl (Long.toString (aSettings.getTimeoutMilliSecs ()) +
                    EText.MSG_MILLISECONDS.getDisplayText (aDisplayLocale));
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final NamedSMTPSettings aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final NamedSMTPSettings aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected boolean handleDeleteAction (@Nonnull final WebPageExecutionContext aWPEC,
                                        @Nonnull final NamedSMTPSettings aSelectedObject)
  {
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

    final HCForm aForm = aNodeList.addAndReturnChild (createFormSelf ());
    aForm.addChild (getStyler ().createQuestionBox (EText.DELETE_QUERY.getDisplayTextWithArgs (aDisplayLocale,
                                                                                               aSelectedObject.getName ())));
    final IButtonToolbar <?> aToolbar = aForm.addAndReturnChild (getStyler ().createToolbar ());
    aToolbar.addHiddenField (CHCParam.PARAM_ACTION, ACTION_DELETE);
    aToolbar.addHiddenField (CHCParam.PARAM_OBJECT, aSelectedObject.getID ());
    aToolbar.addHiddenField (CHCParam.PARAM_SUBACTION, ACTION_SAVE);
    aToolbar.addSubmitButtonYes (aDisplayLocale);
    aToolbar.addButtonNo (aDisplayLocale);
    return false;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

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
      final ISimpleURL aViewLink = createViewURL (aCurObject);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aCurObject.getName ()));
      aRow.addCell (aCurObject.getSMTPSettings ().getHostName () + ":" + aCurObject.getSMTPSettings ().getPort ());
      aRow.addCell (aCurObject.getSMTPSettings ().getUserName ());

      final IHCCell <?> aActionCell = aRow.addCell ();
      aActionCell.addChild (createDeleteLink (aCurObject,
                                              EWebPageText.OBJECT_DELETE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                 aCurObject.getName ())));
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (3).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
