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
package com.phloc.webpages.security;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.errorhandling.FormErrors;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.appbasics.security.login.LoginInfo;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.bootstrap.EBootstrapIcon;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableFormView;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableDateTime;
import com.phloc.webpages.AbstractWebPageForm;

public class BasePageLoginInfo extends AbstractWebPageForm <LoginInfo>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    MSG_USERNAME ("Benutzername", "User name"),
    MSG_LOGINDT ("Anmeldezeit", "Login time"),
    MSG_LASTACCESSDT ("Letzter Zugriff", "Last access"),
    HEADER_DETAILS ("Details des angemeldeten Benutzers", "Details of logged in user"),
    MSG_USERID ("Benutzer-ID", "User ID"),
    MSG_LOGOUTDT ("Abmeldezeit", "Logout time"),
    MSG_ATTRS ("Attribute", "Attributes"),
    MSG_NAME ("Name", "Wert"),
    MSG_VALUE ("Wert", "Value"),
    MSG_LOGOUT_USER ("Benutzer {0} abmelden", "Log out user {0}");

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

  private static final String ACTION_LOGOUT_USER = "logoutuser";

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID,
                            @Nonnull final String sName,
                            @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageLoginInfo (@Nonnull @Nonempty final String sID,
                            @Nonnull final IReadonlyMultiLingualText aName,
                            @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  @Nullable
  protected LoginInfo getSelectedObject (final WebPageExecutionContext aWPEC, @Nullable final String sID)
  {
    return LoggedInUserManager.getInstance ().getLoginInfo (sID);
  }

  @Override
  protected final boolean isEditAllowed (@Nullable final LoginInfo aLoginInfo)
  {
    return false;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WebPageExecutionContext aWPEC,
                                     @Nonnull final LoginInfo aSelectedObject)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapTableFormView aTable = aNodeList.addAndReturnChild (new BootstrapTableFormView (new HCCol (170),
                                                                                                   HCCol.star ()));
    aTable.setSpanningHeaderContent (EText.HEADER_DETAILS.getDisplayText (aDisplayLocale));

    aTable.addItemRow (EText.MSG_USERID.getDisplayText (aDisplayLocale), aSelectedObject.getUserID ());
    aTable.addItemRow (EText.MSG_USERNAME.getDisplayText (aDisplayLocale), aSelectedObject.getUser ().getDisplayName ());
    aTable.addItemRow (EText.MSG_LOGINDT.getDisplayText (aDisplayLocale),
                       PDTToString.getAsString (aSelectedObject.getLoginDT (), aDisplayLocale));
    aTable.addItemRow (EText.MSG_LASTACCESSDT.getDisplayText (aDisplayLocale),
                       PDTToString.getAsString (aSelectedObject.getLastAccessDT (), aDisplayLocale));
    if (aSelectedObject.getLogoutDT () != null)
      aTable.addItemRow (EText.MSG_LOGOUTDT.getDisplayText (aDisplayLocale),
                         PDTToString.getAsString (aSelectedObject.getLogoutDT (), aDisplayLocale));

    // Add custom attributes
    final Map <String, Object> aAttrs = aSelectedObject.getAllAttributes ();
    if (!aAttrs.isEmpty ())
    {
      final BootstrapTable aCustomAttrTable = new BootstrapTable (new HCCol (170), HCCol.star ()).setID (aSelectedObject.getID ());
      aCustomAttrTable.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                                 EText.MSG_VALUE.getDisplayText (aDisplayLocale));
      for (final Map.Entry <String, Object> aEntry : aAttrs.entrySet ())
        aCustomAttrTable.addBodyRow ().addCells (aEntry.getKey (), String.valueOf (aEntry.getValue ()));

      final DataTables aDataTables = createDefaultDataTables (aCustomAttrTable, aDisplayLocale);
      aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);

      aTable.addItemRow (EText.MSG_ATTRS.getDisplayText (aDisplayLocale), aCustomAttrTable, aDataTables.build ());
    }
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WebPageExecutionContext aWPEC,
                                                 @Nullable final LoginInfo aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WebPageExecutionContext aWPEC,
                                @Nullable final LoginInfo aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  protected final boolean canLogoutUser (@Nonnull final IUser aUser)
  {
    return aUser.isEnabled () && !aUser.isDeleted () && !aUser.isAdministrator ();
  }

  @Nullable
  @OverrideOnDemand
  protected IHCNode getLogoutUserIcon ()
  {
    return EBootstrapIcon.LOCK.getAsNode ();
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    final BootstrapTable aTable = new BootstrapTable (HCCol.star (),
                                                      new HCCol (150),
                                                      new HCCol (150),
                                                      createActionCol (2)).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_USERNAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_LOGINDT.getDisplayText (aDisplayLocale),
                                     EText.MSG_LASTACCESSDT.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));
    final Collection <LoginInfo> aLoginInfos = LoggedInUserManager.getInstance ().getAllLoginInfos ();
    for (final LoginInfo aLoginInfo : aLoginInfos)
    {
      final ISimpleURL aViewLink = createViewURL (aLoginInfo);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aLoginInfo.getUser ().getDisplayName ()));
      aRow.addCell (PDTToString.getAsString (aLoginInfo.getLoginDT (), aDisplayLocale));
      aRow.addCell (PDTToString.getAsString (aLoginInfo.getLastAccessDT (), aDisplayLocale));

      final AbstractHCCell aActionCell = aRow.addCell ();
      if (canLogoutUser (aLoginInfo.getUser ()))
      {
        aActionCell.addChild (new HCA (LinkUtils.getSelfHref ()
                                                .add (CHCParam.PARAM_ACTION, ACTION_LOGOUT_USER)
                                                .add (CHCParam.PARAM_OBJECT, aLoginInfo.getUserID ())).setTitle (EText.MSG_LOGOUT_USER.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                                                                               aLoginInfo.getUser ()
                                                                                                                                                                         .getDisplayName ()))
                                                                                                      .addChild (getLogoutUserIcon ()));
      }
      else
      {
        aActionCell.addChild (createEmptyAction ());
      }
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getOrCreateColumnOfTarget (1)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableDateTime (aDisplayLocale));
    aDataTables.getOrCreateColumnOfTarget (2)
               .addClass (CSS_CLASS_RIGHT)
               .setComparator (new ComparatorTableDateTime (aDisplayLocale));
    aDataTables.setInitialSorting (1, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
