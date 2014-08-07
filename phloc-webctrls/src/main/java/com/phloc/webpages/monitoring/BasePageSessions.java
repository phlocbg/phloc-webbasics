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
package com.phloc.webpages.monitoring;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.scopes.domain.ISessionApplicationScope;
import com.phloc.scopes.domain.ISessionScope;
import com.phloc.scopes.mgr.ScopeSessionManager;
import com.phloc.validation.error.FormErrors;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageForm;
import com.phloc.webpages.EWebPageText;
import com.phloc.webpages.UITextFormatter;

/**
 * Show information on all active sessions
 *
 * @author Philip Helger
 */
public class BasePageSessions <WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPageForm <ISessionScope, WPECTYPE>
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_REFRESH ("Aktualisieren", "Refresh"),
    MSG_SESSION ("Session Kontext", "Session scope"),
    MSG_SESSION_APPLICATION_SCOPE ("Session Application Kontext ''{0}''", "Session app scope ''{0}''"),
    MSG_ID ("ID", "ID"),
    MSG_SCOPE_ID ("Kontext ID", "Scope ID"),
    MSG_SCOPE_VALID ("Kontext gültig?", "Scope valid?"),
    MSG_SCOPE_IN_DESTRUCTION ("Kontext in Zerstörung?", "Scope in destruction?"),
    MSG_SCOPE_DESTROYED ("Kontext zerstört?", "Scope destroyed?"),
    MSG_SESSION_APPLICATION_SCOPES ("Session Application Kontexte", "Session application scopes"),
    MSG_SCOPE_ATTRS ("Attribute", "Attributes"),
    MSG_NAME ("Name", "Wert"),
    MSG_TYPE ("Typ", "Type"),
    MSG_VALUE ("Wert", "Value");

    private final TextProvider m_aTP;

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

  public BasePageSessions (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_SESSIONS.getAsMLT ());
  }

  public BasePageSessions (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageSessions (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSessions (@Nonnull @Nonempty final String sID,
                           @Nonnull final IReadonlyMultiLingualText aName,
                           @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  @Nullable
  protected ISessionScope getSelectedObject (@Nonnull final WPECTYPE aWPEC, @Nullable final String sID)
  {
    return ScopeSessionManager.getInstance ().getSessionScopeOfID (sID);
  }

  @Override
  protected final boolean isEditAllowed (@Nonnull final WPECTYPE aWPEC, @Nullable final ISessionScope aSessionScope)
  {
    return false;
  }

  @Nonnull
  private IHCNode _getSessionScopeInfo (@Nonnull final WPECTYPE aWPEC, @Nonnull final ISessionScope aScope)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList ret = new HCNodeList ();

    final IHCTableFormView <?> aTableScope = ret.addAndReturnChild (getStyler ().createTableFormView (new HCCol (220),
                                                                                                      HCCol.star ()));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_ID.getDisplayText (aDisplayLocale))
               .setCtrl (aScope.getID ());
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_VALID.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isValid (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_IN_DESTRUCTION.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isInDestruction (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_DESTROYED.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isDestroyed (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SESSION_APPLICATION_SCOPES.getDisplayText (aDisplayLocale))
               .setCtrl (Integer.toString (aScope.getSessionApplicationScopeCount ()));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_ATTRS.getDisplayText (aDisplayLocale))
               .setCtrl (Integer.toString (aScope.getAttributeCount ()));

    // All scope attributes
    final IHCTableFormView <?> aTableAttrs = getStyler ().createTableFormView (HCCol.star (),
                                                                               HCCol.star (),
                                                                               HCCol.star ()).setID ("sessionscope-" +
                                                                                                     aScope.getID ());
    aTableAttrs.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                          EText.MSG_TYPE.getDisplayText (aDisplayLocale),
                                          EText.MSG_VALUE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <String, Object> aEntry : aScope.getAllAttributes ().entrySet ())
      aTableAttrs.addBodyRow ()
                 .addCell (aEntry.getKey ())
                 .addCell (CGStringHelper.getClassLocalName (aEntry.getValue ()))
                 .addCell (UITextFormatter.getToStringContent (aEntry.getValue ()));
    ret.addChild (aTableAttrs);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTableAttrs);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    ret.addChild (aDataTables);

    return ret;
  }

  @Nonnull
  private IHCNode _getSessionApplicationScopeInfo (@Nonnull final WPECTYPE aWPEC,
                                                   @Nonnull final ISessionApplicationScope aScope)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = new HCNodeList ();

    final IHCTableFormView <?> aTableScope = getStyler ().createTableFormView (new HCCol (200), HCCol.star ());
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_ID.getDisplayText (aDisplayLocale))
               .setCtrl (aScope.getID ());
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_VALID.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isValid (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_IN_DESTRUCTION.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isInDestruction (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_DESTROYED.getDisplayText (aDisplayLocale))
               .setCtrl (EWebBasicsText.getYesOrNo (aScope.isDestroyed (), aDisplayLocale));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_ATTRS.getDisplayText (aDisplayLocale))
               .setCtrl (Integer.toString (aScope.getAttributeCount ()));
    aNodeList.addChild (aTableScope);

    // All scope attributes
    final IHCTableFormView <?> aTableAttrs = getStyler ().createTableFormView (HCCol.star (),
                                                                               HCCol.star (),
                                                                               HCCol.star ()).setID ("sessionappscope" +
                                                                                                     aScope.getID ());
    aTableAttrs.addHeaderRow ().addCells (EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                          EText.MSG_TYPE.getDisplayText (aDisplayLocale),
                                          EText.MSG_VALUE.getDisplayText (aDisplayLocale));
    for (final Map.Entry <String, Object> aEntry : aScope.getAllAttributes ().entrySet ())
      aTableAttrs.addBodyRow ()
                 .addCell (aEntry.getKey ())
                 .addCell (CGStringHelper.getClassLocalName (aEntry.getValue ()))
                 .addCell (UITextFormatter.getToStringContent (aEntry.getValue ()));
    aNodeList.addChild (aTableAttrs);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTableAttrs);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);

    return aNodeList;
  }

  @Override
  protected void showSelectedObject (@Nonnull final WPECTYPE aWPEC, @Nonnull final ISessionScope aScope)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    // Refresh button
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aWPEC);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), createViewURL (aWPEC, aScope));
    aNodeList.addChild (aToolbar);

    final ITabBox <?> aTabBox = getStyler ().createTabBox (aWPEC);
    aTabBox.addTab (EText.MSG_SESSION.getDisplayText (aDisplayLocale), _getSessionScopeInfo (aWPEC, aScope));
    for (final ISessionApplicationScope aSessionAppScope : ContainerHelper.getSortedByKey (aScope.getAllSessionApplicationScopes ())
                                                                          .values ())
      aTabBox.addTab (EText.MSG_SESSION_APPLICATION_SCOPES.getDisplayTextWithArgs (aDisplayLocale,
                                                                                   aSessionAppScope.getID ()),
                      _getSessionApplicationScopeInfo (aWPEC, aSessionAppScope));
    aNodeList.addChild (aTabBox);
  }

  @Override
  protected void validateAndSaveInputParameters (@Nonnull final WPECTYPE aWPEC,
                                                 @Nullable final ISessionScope aSelectedObject,
                                                 @Nonnull final FormErrors aFormErrors,
                                                 final boolean bEdit)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showInputForm (@Nonnull final WPECTYPE aWPEC,
                                @Nullable final ISessionScope aSelectedObject,
                                @Nonnull final HCForm aForm,
                                final boolean bEdit,
                                final boolean bCopy,
                                @Nonnull final FormErrors aFormErrors)
  {
    throw new UnsupportedOperationException ();
  }

  @Override
  protected void showListOfExistingObjects (@Nonnull final WPECTYPE aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    // Refresh button
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aWPEC);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref ());
    aNodeList.addChild (aToolbar);

    final IHCTable <?> aTable = getStyler ().createTable (HCCol.star (), createActionCol (1)).setID (getID ());
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EWebBasicsText.MSG_ACTIONS.getDisplayText (aDisplayLocale));
    for (final ISessionScope aSessionScope : ScopeSessionManager.getInstance ().getAllSessionScopes ())
    {
      final ISimpleURL aViewLink = createViewURL (aWPEC, aSessionScope);

      final HCRow aRow = aTable.addBodyRow ();
      aRow.addCell (new HCA (aViewLink).addChild (aSessionScope.getID ()));

      // Actions
      aRow.addCell ();
    }

    aNodeList.addChild (aTable);

    final DataTables aDataTables = getStyler ().createDefaultDataTables (aWPEC, aTable);
    aDataTables.getOrCreateColumnOfTarget (1).addClass (CSS_CLASS_ACTION_COL).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
