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
package com.phloc.webpages.monitoring;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.AbstractHCButton;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.validation.error.FormErrors;
import com.phloc.web.smtp.IEmailAttachment;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.IReadonlyEmailAttachmentList;
import com.phloc.web.smtp.failed.FailedMailData;
import com.phloc.web.smtp.failed.FailedMailQueue;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableDateTime;
import com.phloc.webpages.AbstractWebPageFormExt;
import com.phloc.webpages.EWebPageText;
import com.phloc.webscopes.smtp.ScopedMailAPI;

/**
 * Show all failed mails.
 * 
 * @author Philip Helger
 */
public class BasePageFailedMails <WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPageFormExt <FailedMailData, WPECTYPE>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_REFRESH ("Aktualisieren", "Refresh"),
    BUTTON_DELETE ("Löschen", "Delete"),
    BUTTON_RESEND ("Erneut versenden", "Resend"),
    BUTTON_RESEND_ALL ("Alle erneut versenden", "Resend all"),
    BUTTON_DELETE_ALL ("Alle löschen", "Delete all"),
    MSG_ID ("ID", "ID"),
    MSG_ERROR_DT ("Fehler-Datum", "Error date"),
    MSG_SMTP_SETTINGS ("SMTP-Einstellungen", "SMTP settings"),
    MSG_SENDING_DT ("Sendedatum", "Sending date"),
    MSG_EMAIL_TYPE ("E-Mail Typ", "Email type"),
    MSG_FROM ("Von", "From"),
    MSG_REPLY_TO ("Antwort an", "Reply to"),
    MSG_TO ("An", "To"),
    MSG_CC ("Cc", "Cc"),
    MSG_BCC ("Bcc", "Bcc"),
    MSG_SUBJECT ("Betreff", "Subject"),
    MSG_BODY ("Inhalt", "Body"),
    MSG_ATTACHMENTS ("Beilagen", "Attachments"),
    MSG_ERROR ("Fehlermeldung", "Error message"),
    RESENT_SUCCESS ("Das E-Mail wurde erneut versendet.", "The email was scheduled for resending."),
    RESENT_ALL_SUCCESS_1 ("Es wurde 1 E-Mail erneut versendet.", "1 email was scheduled for resending."),
    RESENT_ALL_SUCCESS_N ("Es wurden {0} E-Mails erneut versendet.", "{0} emails were scheduled for resending."),
    DELETE_SUCCESS ("Das E-Mail wurde erfolgreich gelöscht.", "The email was successfully deleted."),
    DELETE_ALL_SUCCESS_1 ("Es wurde 1 E-Mail erfolgreich gelöscht.", "1 email was successfully deleted."),
    DELETE_ALL_SUCCESS_N ("Es wurden {0} E-Mails erfolgreich gelöscht.", "{0} emails were successfully deleted.");

    @Nonnull
    private final ITextProvider m_aTP;

    private EText (@Nonnull final String sDE, @Nonnull final String sEN)
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

  private static final Logger s_aLogger = LoggerFactory.getLogger (BasePageFailedMails.class);
  private static final String ACTION_RESEND = "resend";
  private static final String ACTION_RESEND_ALL = "resend-all";

  private final FailedMailQueue m_aFailedMailQueue;

  public BasePageFailedMails (@Nonnull @Nonempty final String sID, @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_FAILED_MAILS.getAsMLT ());
    m_aFailedMailQueue = ValueEnforcer.notNull (aFailedMailQueue, "FailedMailQueue");
  }

  public BasePageFailedMails (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    super (sID, sName);
    m_aFailedMailQueue = ValueEnforcer.notNull (aFailedMailQueue, "FailedMailQueue");
  }

  public BasePageFailedMails (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nullable final String sDescription,
                              @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    super (sID, sName, sDescription);
    m_aFailedMailQueue = ValueEnforcer.notNull (aFailedMailQueue, "FailedMailQueue");
  }

  public BasePageFailedMails (@Nonnull @Nonempty final String sID,
                              @Nonnull final IReadonlyMultiLingualText aName,
                              @Nullable final IReadonlyMultiLingualText aDescription,
                              @Nonnull final FailedMailQueue aFailedMailQueue)
  {
    super (sID, aName, aDescription);
    m_aFailedMailQueue = ValueEnforcer.notNull (aFailedMailQueue, "FailedMailQueue");
  }

  @Override
  protected boolean isEditAllowed (@Nonnull final WPECTYPE aWPEC, @Nullable final FailedMailData aSelectedObject)
  {
    return false;
  }

  @Override
  @Nullable
  protected FailedMailData getSelectedObject (@Nonnull final WPECTYPE aWPEC, @Nullable final String sID)
  {
    return m_aFailedMailQueue.getFailedMailOfID (sID);
  }

  @Override
  protected void modifyViewToolbar (@Nonnull final WPECTYPE aWPEC,
                                    @Nonnull final FailedMailData aSelectedObject,
                                    @Nonnull final IButtonToolbar <?> aToolbar)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    aToolbar.addButton (EText.BUTTON_RESEND.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref ()
                             .add (CHCParam.PARAM_ACTION, ACTION_RESEND)
                             .add (CHCParam.PARAM_OBJECT, aSelectedObject.getID ()),
                        EDefaultIcon.YES);
    aToolbar.addButton (EText.BUTTON_DELETE.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref ()
                             .add (CHCParam.PARAM_ACTION, ACTION_DELETE)
                             .add (CHCParam.PARAM_OBJECT, aSelectedObject.getID ()),
                        EDefaultIcon.DELETE);
  }

  @Nullable
  private static IHCNode _getAsString (@Nonnull final List <? extends IEmailAddress> aList)
  {
    if (aList.isEmpty ())
      return null;

    final HCNodeList ret = new HCNodeList ();
    for (final IEmailAddress aEmailAddress : aList)
      ret.addChild (new HCDiv ().addChild (aEmailAddress.getDisplayName ()));
    return ret;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WPECTYPE aWPEC, @Nonnull final FailedMailData aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final IEmailData aEmailData = aSelectedObject.getEmailData ();
    final Throwable aError = aSelectedObject.getError ();

    final IHCTableFormView <?> aTable = aNodeList.addAndReturnChild (getStyler ().createTableFormView (new HCCol (170),
                                                                                                       HCCol.star ()));
    aTable.createItemRow ().setLabel (EText.MSG_ID.getDisplayText (aDisplayLocale)).setCtrl (aSelectedObject.getID ());
    aTable.createItemRow ()
          .setLabel (EText.MSG_ERROR_DT.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getErrorTimeDisplayText (aDisplayLocale));
    aTable.createItemRow ()
          .setLabel (EText.MSG_SMTP_SETTINGS.getDisplayText (aDisplayLocale))
          .setCtrl (aSelectedObject.getSMTPServerDisplayText ());
    aTable.createItemRow ()
          .setLabel (EText.MSG_SENDING_DT.getDisplayText (aDisplayLocale))
          .setCtrl (PDTToString.getAsString (aSelectedObject.getOriginalSentDateTime (), aDisplayLocale));
    if (aEmailData != null)
    {
      aTable.createItemRow ()
            .setLabel (EText.MSG_EMAIL_TYPE.getDisplayText (aDisplayLocale))
            .setCtrl (aEmailData.getEmailType ().getID ());

      aTable.createItemRow ()
            .setLabel (EText.MSG_FROM.getDisplayText (aDisplayLocale))
            .setCtrl (aEmailData.getFrom ().getDisplayName ());

      final IHCNode aReplyTo = _getAsString (aEmailData.getReplyTo ());
      if (aReplyTo != null)
        aTable.createItemRow ().setLabel (EText.MSG_REPLY_TO.getDisplayText (aDisplayLocale)).setCtrl (aReplyTo);

      aTable.createItemRow ()
            .setLabel (EText.MSG_TO.getDisplayText (aDisplayLocale))
            .setCtrl (_getAsString (aEmailData.getTo ()));

      final IHCNode aCc = _getAsString (aEmailData.getCc ());
      if (aCc != null)
        aTable.createItemRow ().setLabel (EText.MSG_CC.getDisplayText (aDisplayLocale)).setCtrl (aCc);

      final IHCNode aBcc = _getAsString (aEmailData.getBcc ());
      if (aBcc != null)
        aTable.createItemRow ().setLabel (EText.MSG_BCC.getDisplayText (aDisplayLocale)).setCtrl (aBcc);

      aTable.createItemRow ()
            .setLabel (EText.MSG_SUBJECT.getDisplayText (aDisplayLocale))
            .setCtrl (aEmailData.getSubject ());

      List <? extends IHCNode> aBody = null;
      switch (aEmailData.getEmailType ())
      {
        case TEXT:
          aBody = HCUtils.nl2divList (aEmailData.getBody ());
          break;
        case HTML:
          aBody = ContainerHelper.newList (new HCTextNode (aEmailData.getBody ()));
          break;
      }
      aTable.createItemRow ().setLabel (EText.MSG_BODY.getDisplayText (aDisplayLocale)).setCtrl (aBody);

      // Show attachment details
      final IReadonlyEmailAttachmentList aAttachments = aEmailData.getAttachments ();
      if (aAttachments != null && !aAttachments.isEmpty ())
      {
        final HCNodeList aAttachmentNodeList = new HCNodeList ();
        for (final IEmailAttachment aAttachment : aAttachments.getAllAttachments ())
        {
          String sText = aAttachment.getFilename ();
          if (aAttachment.getCharset () != null)
            sText += " (" + aAttachment.getCharset ().name () + ")";
          if (StringHelper.hasText (aAttachment.getContentType ()))
            sText += " [" + aAttachment.getContentType () + "]";
          sText += "; disposition=" + aAttachment.getDisposition ().getID ();
          aAttachmentNodeList.addChild (new HCDiv ().addChild (sText));
        }
        aTable.createItemRow ()
              .setLabel (EText.MSG_ATTACHMENTS.getDisplayText (aDisplayLocale))
              .setCtrl (aAttachmentNodeList);
      }
    }
    if (aError != null)
    {
      aTable.createItemRow ().setLabel (EText.MSG_ERROR.getDisplayText (aDisplayLocale)).setCtrl (aError.getMessage ());
    }
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WPECTYPE aWPEC,
                                                 @Nullable final FailedMailData aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WPECTYPE aWPEC,
                                @Nullable final FailedMailData aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected boolean handleDeleteAction (@Nonnull final WPECTYPE aWPEC, @Nonnull final FailedMailData aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Delete a single failed mail without querying
    if (m_aFailedMailQueue.remove (aSelectedObject.getID ()) != null)
    {
      s_aLogger.info ("Deleted single failed mail with ID " + aSelectedObject.getID () + "!");
      aNodeList.addChild (getStyler ().createSuccessBox (aWPEC, EText.DELETE_SUCCESS.getDisplayTextWithArgs (aDisplayLocale)));
    }
    return true;
  }

  @Override
  protected boolean handleCustomActions (@Nonnull final WPECTYPE aWPEC, @Nullable final FailedMailData aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    if (aWPEC.hasAction (ACTION_DELETE_ALL))
    {
      // Delete all failed mails
      final List <FailedMailData> aFailedMails = m_aFailedMailQueue.removeAll ();
      if (!aFailedMails.isEmpty ())
      {
        s_aLogger.info ("Deleted " + aFailedMails.size () + " failed mails!");
        final String sSuccessMsg = aFailedMails.size () == 1 ? EText.DELETE_ALL_SUCCESS_1.getDisplayText (aDisplayLocale)
                                                            : EText.DELETE_ALL_SUCCESS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                 Integer.toString (aFailedMails.size ()));
        aNodeList.addChild (getStyler ().createSuccessBox (aWPEC, sSuccessMsg));
      }
    }
    else
      if (aWPEC.hasAction (ACTION_RESEND) && aSelectedObject != null)
      {
        // Resend a single failed mail
        final FailedMailData aFailedMailData = m_aFailedMailQueue.remove (aSelectedObject.getID ());
        if (aFailedMailData != null)
        {
          s_aLogger.info ("Trying to resend single failed mail with ID " + aFailedMailData.getID () + "!");
          ScopedMailAPI.getInstance ().queueMail (aFailedMailData.getSMTPSettings (), aFailedMailData.getEmailData ());
          aNodeList.addChild (getStyler ().createSuccessBox (aWPEC, EText.RESENT_SUCCESS.getDisplayTextWithArgs (aDisplayLocale)));
        }
      }
      else
        if (aWPEC.hasAction (ACTION_RESEND_ALL))
        {
          // Resend all failed mails
          final List <FailedMailData> aFailedMails = m_aFailedMailQueue.removeAll ();
          if (!aFailedMails.isEmpty ())
          {
            s_aLogger.info ("Trying to resend " + aFailedMails.size () + " failed mails!");
            for (final FailedMailData aFailedMailData : aFailedMails)
              ScopedMailAPI.getInstance ().queueMail (aFailedMailData.getSMTPSettings (),
                                                      aFailedMailData.getEmailData ());
            final String sSuccessMsg = aFailedMails.size () == 1 ? EText.RESENT_ALL_SUCCESS_1.getDisplayText (aDisplayLocale)
                                                                : EText.RESENT_ALL_SUCCESS_N.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                     Integer.toString (aFailedMails.size ()));
            aNodeList.addChild (getStyler ().createSuccessBox (aWPEC, sSuccessMsg));
          }
        }
    return true;
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Refresh button
    final boolean bDisabled = m_aFailedMailQueue.getAllFailedMails ().isEmpty ();
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aWPEC);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref ());
    aToolbar.addButton (EText.BUTTON_RESEND_ALL.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref ().add (CHCParam.PARAM_ACTION, ACTION_RESEND_ALL),
                        EDefaultIcon.YES);
    ((AbstractHCButton <?>) aToolbar.getLastChild ()).setDisabled (bDisabled);
    aToolbar.addButton (EText.BUTTON_DELETE_ALL.getDisplayText (aDisplayLocale),
                        aWPEC.getSelfHref ().add (CHCParam.PARAM_ACTION, ACTION_DELETE_ALL),
                        EDefaultIcon.DELETE);
    ((AbstractHCButton <?>) aToolbar.getLastChild ()).setDisabled (bDisabled);
    aNodeList.addChild (aToolbar);

    final IHCTable <?> aTable = getStyler ().createTable (new HCCol (70),
                                                          new HCCol (120),
                                                          new HCCol (120),
                                                          HCCol.star (),
                                                          HCCol.star ()).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EText.MSG_ERROR_DT.getDisplayText (aDisplayLocale),
                                     EText.MSG_SMTP_SETTINGS.getDisplayText (aDisplayLocale),
                                     EText.MSG_SUBJECT.getDisplayText (aDisplayLocale),
                                     EText.MSG_ERROR.getDisplayText (aDisplayLocale));

    for (final FailedMailData aItem : m_aFailedMailQueue.getAllFailedMails ())
    {
      final ISimpleURL aViewURL = createViewURL (aWPEC, aItem);
      final IEmailData aEmailData = aItem.getEmailData ();
      final Throwable aError = aItem.getError ();

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewURL).addChild (aItem.getID ()));
      aRow.addCell (aItem.getErrorTimeDisplayText (aDisplayLocale));
      aRow.addCell (aItem.getSMTPServerDisplayText ());
      aRow.addCell (aEmailData == null ? null : aEmailData.getSubject ());
      aRow.addCell (aError == null ? null : aError.getMessage ());
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (1)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableDateTime (aDisplayLocale));
    aDataTables.setInitialSorting (0, ESortOrder.DESCENDING);
    aNodeList.addChild (aDataTables);
  }
}
