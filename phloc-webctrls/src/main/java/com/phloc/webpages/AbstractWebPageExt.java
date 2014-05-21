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
package com.phloc.webpages;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.layout.LayoutExecutionContext;
import com.phloc.webbasics.app.page.AbstractWebPage;
import com.phloc.webbasics.app.ui.WebBasicsCSS;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.styler.IWebPageStyler;
import com.phloc.webctrls.styler.WebPageStylerManager;

public abstract class AbstractWebPageExt extends AbstractWebPage
{
  /** The width of a single action column in pixels */
  public static final int DEFAULT_ACTION_COL_WIDTH = 20;
  @Deprecated
  public static final int ACTION_COL_WIDTH = DEFAULT_ACTION_COL_WIDTH;
  public static final String ACTION_CANCEL = "cancel";
  public static final String ACTION_COLLAPSE = CHCParam.ACTION_COLLAPSE;
  public static final String ACTION_COPY = "copy";
  public static final String ACTION_CREATE = CHCParam.ACTION_CREATE;
  public static final String ACTION_DELETE = CHCParam.ACTION_DELETE;
  public static final String ACTION_DELETE_ALL = "delete-all";
  public static final String ACTION_EDIT = CHCParam.ACTION_EDIT;
  public static final String ACTION_EXPAND = CHCParam.ACTION_EXPAND;
  public static final String ACTION_PERFORM = CHCParam.ACTION_PERFORM;
  public static final String ACTION_SAVE = CHCParam.ACTION_SAVE;
  public static final String ACTION_UNDELETE = "undelete";
  public static final String ACTION_UNDELETE_ALL = "undelete-all";
  public static final String ACTION_VIEW = CHCParam.ACTION_VIEW;

  protected static final ICSSClassProvider CSS_CLASS_LEFT = WebBasicsCSS.CSS_CLASS_LEFT;
  protected static final ICSSClassProvider CSS_CLASS_CENTER = WebBasicsCSS.CSS_CLASS_CENTER;
  protected static final ICSSClassProvider CSS_CLASS_RIGHT = WebBasicsCSS.CSS_CLASS_RIGHT;
  protected static final ICSSClassProvider CSS_CLASS_ACTION_COL = WebBasicsCSS.CSS_CLASS_ACTION_COL;
  protected static final ICSSClassProvider CSS_CLASS_EMPTY_ACTION = DefaultCSSClassProvider.create ("empty-action");

  private static int s_nActionColWidth = DEFAULT_ACTION_COL_WIDTH;

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID,
                             @Nonnull final String sName,
                             @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID,
                             @Nonnull final IReadonlyMultiLingualText aName,
                             @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  public static final IWebPageStyler getStyler ()
  {
    return WebPageStylerManager.getStyler ();
  }

  @Nonnegative
  public static int getActionColWidth ()
  {
    return s_nActionColWidth;
  }

  public static void setActionColWidth (@Nonnegative final int nActionColWidth)
  {
    s_nActionColWidth = nActionColWidth;
  }

  /**
   * Create a HCCol (table column) for the specified number of actions. Each
   * action represents a width of {@link #getActionColWidth()} pixels. At least
   * the width of 3 actions is displayed, so that the header text fits :)
   * 
   * @param nActions
   *        Number of actions. Must be &ge; 0.
   * @return The column with the according column width.
   */
  @Nonnull
  public static HCCol createActionCol (@Nonnegative final int nActions)
  {
    // Assume each action icon is 20 pixels (incl. margin) - at least 3 column
    // widths are required for the header
    final int nWidth = getActionColWidth () * Math.max (3, nActions);
    return new HCCol (nWidth);
  }

  @Nonnull
  public static HCSpan createEmptyAction ()
  {
    return new HCSpan ().addClass (CSS_CLASS_EMPTY_ACTION)
                        .addStyle (CCSSProperties.DISPLAY_INLINE_BLOCK)
                        .addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (16)));
  }

  @Nonnull
  public static SimpleURL createCreateURL (@Nonnull final String sMenuItemID)
  {
    return LinkUtils.getLinkToMenuItem (sMenuItemID).add (CHCParam.PARAM_ACTION, ACTION_CREATE);
  }

  @Nonnull
  @Deprecated
  public static SimpleURL createCreateURL ()
  {
    return LinkUtils.getSelfHref (new SMap (CHCParam.PARAM_ACTION, ACTION_CREATE));
  }

  @Nonnull
  public static SimpleURL createCreateURL (@Nonnull final LayoutExecutionContext aLEC)
  {
    return aLEC.getSelfHref ().add (CHCParam.PARAM_ACTION, ACTION_CREATE);
  }

  @Nonnull
  public static SimpleURL createViewURL (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref ()
                    .add (CHCParam.PARAM_ACTION, ACTION_VIEW)
                    .add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
  }

  @Nonnull
  public static <T extends IHasID <String> & IHasDisplayName> HCA createEditLink (@Nonnull final T aCurObject,
                                                                                  @Nonnull final Locale aDisplayLocale)
  {
    return createEditLink (aCurObject, aDisplayLocale, (Map <String, String>) null);
  }

  @Nonnull
  public static <T extends IHasID <String> & IHasDisplayName> HCA createEditLink (@Nonnull final T aCurObject,
                                                                                  @Nonnull final Locale aDisplayLocale,
                                                                                  @Nullable final Map <String, String> aParams)
  {
    return createEditLink (aCurObject,
                           EWebPageText.OBJECT_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                            aCurObject.getDisplayName ()),
                           aParams);
  }

  @Nonnull
  public static IHCNode getEditImg ()
  {
    return EDefaultIcon.EDIT.getIcon ().getAsNode ();
  }

  @Nonnull
  public static SimpleURL createEditURL (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref ()
                    .add (CHCParam.PARAM_ACTION, ACTION_EDIT)
                    .add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
  }

  @Nonnull
  public static HCA createEditLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    return createEditLink (aCurObject, sTitle, (Map <String, String>) null);
  }

  @Nonnull
  public static HCA createEditLink (@Nonnull final IHasID <String> aCurObject,
                                    @Nullable final String sTitle,
                                    @Nullable final Map <String, String> aParams)
  {
    final ISimpleURL aEditURL = createEditURL (aCurObject).addAll (aParams);
    return new HCA (aEditURL).setTitle (sTitle).addChild (getEditImg ());
  }

  @Nonnull
  public static <T extends IHasID <String> & IHasDisplayName> HCA createCopyLink (@Nonnull final T aCurObject,
                                                                                  @Nonnull final Locale aDisplayLocale)
  {
    return createCopyLink (aCurObject,
                           EWebPageText.OBJECT_COPY.getDisplayTextWithArgs (aDisplayLocale,
                                                                            aCurObject.getDisplayName ()));
  }

  @Nonnull
  public static IHCNode getCopyImg ()
  {
    return EDefaultIcon.COPY.getIcon ().getAsNode ();
  }

  @Nonnull
  public static SimpleURL createCopyURL (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref ()
                    .add (CHCParam.PARAM_ACTION, ACTION_COPY)
                    .add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
  }

  @Nonnull
  public static HCA createCopyLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aCopyURL = createCopyURL (aCurObject);
    return new HCA (aCopyURL).setTitle (sTitle).addChild (getCopyImg ());
  }

  @Nonnull
  public static <T extends IHasID <String> & IHasDisplayName> HCA createDeleteLink (@Nonnull final T aCurObject,
                                                                                    @Nonnull final Locale aDisplayLocale)
  {
    return createDeleteLink (aCurObject,
                             EWebPageText.OBJECT_DELETE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                aCurObject.getDisplayName ()));
  }

  @Nonnull
  public static IHCNode getDeleteImg ()
  {
    return EDefaultIcon.DELETE.getIcon ().getAsNode ();
  }

  @Nonnull
  public static SimpleURL createDeleteURL (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref ()
                    .add (CHCParam.PARAM_ACTION, ACTION_DELETE)
                    .add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
  }

  @Nonnull
  public static HCA createDeleteLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aURL = createDeleteURL (aCurObject);
    return new HCA (aURL).setTitle (sTitle).addChild (getDeleteImg ());
  }

  @Nonnull
  public static SimpleURL createUndeleteURL (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref ()
                    .add (CHCParam.PARAM_ACTION, ACTION_UNDELETE)
                    .add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
  }

  @Nonnull
  public static IHCNode getCreateImg ()
  {
    return EDefaultIcon.NEW.getIcon ().getAsNode ();
  }

  @Nonnull
  public static HCA createNestedCreateLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aURL = createCreateURL ().add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
    return new HCA (aURL).setTitle (sTitle).addChild (getCreateImg ());
  }
}
