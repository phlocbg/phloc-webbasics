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
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.scopes.domain.IApplicationScope;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;
import com.phloc.webpages.UITextFormatter;
import com.phloc.webscopes.domain.IGlobalWebScope;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Show information on global and application scopes
 * 
 * @author Philip Helger
 */
public class BasePageScopes extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText, IHasDisplayTextWithArgs
  {
    BUTTON_REFRESH ("Aktualisieren", "Refresh"),
    MSG_GLOBAL_SCOPE ("Globaler Kontext ''{0}''", "Global scope ''{0}''"),
    MSG_APPLICATION_SCOPE ("Application Kontext ''{0}''", "Application scope ''{0}''"),
    MSG_SCOPE_ID ("Kontext ID", "Scope ID"),
    MSG_SCOPE_VALID ("Kontext gültig?", "Scope valid?"),
    MSG_SCOPE_IN_DESTRUCTION ("Kontext in Zerstörung?", "Scope in destruction?"),
    MSG_SCOPE_DESTROYED ("Kontext zerstört?", "Scope destroyed?"),
    MSG_APPLICATION_SCOPES ("Application Kontexte", "Application scopes"),
    MSG_SCOPE_ATTRS ("Attribute", "Attributes"),
    MSG_NAME ("Name", "Wert"),
    MSG_TYPE ("Typ", "Type"),
    MSG_VALUE ("Wert", "Value");

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

  public BasePageScopes (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_MONITORING_SCOPES.getAsMLT ());
  }

  public BasePageScopes (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageScopes (@Nonnull @Nonempty final String sID,
                         @Nonnull final String sName,
                         @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageScopes (@Nonnull @Nonempty final String sID,
                         @Nonnull final IReadonlyMultiLingualText aName,
                         @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  private IHCNode _getGlobalScopeInfo (@Nonnull final WebPageExecutionContext aWPEC,
                                       @Nonnull final IGlobalWebScope aScope)
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
               .setLabel (EText.MSG_APPLICATION_SCOPES.getDisplayText (aDisplayLocale))
               .setCtrl (Integer.toString (aScope.getApplicationScopeCount ()));
    aTableScope.createItemRow ()
               .setLabel (EText.MSG_SCOPE_ATTRS.getDisplayText (aDisplayLocale))
               .setCtrl (Integer.toString (aScope.getAttributeCount ()));
    aNodeList.addChild (aTableScope);

    // All scope attributes
    final IHCTableFormView <?> aTableAttrs = getStyler ().createTableFormView (HCCol.star (),
                                                                               HCCol.star (),
                                                                               HCCol.star ()).setID ("globalscope");
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

  @Nonnull
  private IHCNode _getApplicationScopeInfo (@Nonnull final WebPageExecutionContext aWPEC,
                                            @Nonnull final IApplicationScope aScope)
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
                                                                               HCCol.star ()).setID ("appscope" +
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
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final IGlobalWebScope aGlobalScope = WebScopeManager.getGlobalScope ();

    // Refresh button
    final IButtonToolbar <?> aToolbar = getStyler ().createToolbar (aWPEC);
    aToolbar.addButton (EText.BUTTON_REFRESH.getDisplayText (aDisplayLocale), aWPEC.getSelfHref ());
    aNodeList.addChild (aToolbar);

    final ITabBox <?> aTabBox = getStyler ().createTabBox ();
    // Global scope
    aTabBox.addTab (EText.MSG_GLOBAL_SCOPE.getDisplayTextWithArgs (aDisplayLocale, aGlobalScope.getID ()),
                    _getGlobalScopeInfo (aWPEC, aGlobalScope));

    // Application scopes
    for (final IApplicationScope aAppScope : ContainerHelper.getSortedByKey (aGlobalScope.getAllApplicationScopes ())
                                                            .values ())
      aTabBox.addTab (EText.MSG_APPLICATION_SCOPE.getDisplayTextWithArgs (aDisplayLocale, aAppScope.getID ()),
                      _getApplicationScopeInfo (aWPEC, aAppScope));

    aNodeList.addChild (aTabBox);
  }
}
