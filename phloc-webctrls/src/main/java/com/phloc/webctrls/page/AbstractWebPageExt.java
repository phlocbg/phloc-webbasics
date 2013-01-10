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
package com.phloc.webctrls.page;

import java.util.Locale;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.userdata.UserDataObject;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.gfx.ImageDataManager;
import com.phloc.commons.gfx.ScalableSize;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLValidator;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCImg;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.htmlext.HCA_MailTo;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.masterdata.email.IReadonlyExtendedEmailAddress;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.page.AbstractWebPage;
import com.phloc.webbasics.app.ui.WebBasicsCSS;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webctrls.bootstrap.EBootstrapButtonType;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.custom.EDefaultIcon;

public abstract class AbstractWebPageExt extends AbstractWebPage
{
  public static final String ACTION_CANCEL = "cancel";
  public static final String ACTION_COPY = "copy";
  public static final String ACTION_CREATE = CHCParam.ACTION_CREATE;
  public static final String ACTION_DELETE = CHCParam.ACTION_DELETE;
  public static final String ACTION_DELETE_ALL = "delete-all";
  public static final String ACTION_EDIT = CHCParam.ACTION_EDIT;
  public static final String ACTION_PERFORM = CHCParam.ACTION_PERFORM;
  public static final String ACTION_SAVE = CHCParam.ACTION_SAVE;
  public static final String ACTION_UNDELETE = "undelete";
  public static final String ACTION_UNDELETE_ALL = "undelete-all";
  public static final String ACTION_VIEW = CHCParam.ACTION_VIEW;
  protected static final ICSSClassProvider CSS_CLASS_RIGHT = WebBasicsCSS.CSS_CLASS_RIGHT;

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public AbstractWebPageExt (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  @Nullable
  protected final String getAction ()
  {
    return getAttr (CHCParam.PARAM_ACTION);
  }

  protected final boolean hasAction (@Nullable final String sAction)
  {
    return hasAttr (CHCParam.PARAM_ACTION, sAction);
  }

  protected final boolean hasSubAction (@Nullable final String sSubAction)
  {
    return hasAttr (CHCParam.PARAM_SUBACTION, sSubAction);
  }

  @Nonnull
  protected static final HCCol createActionCol (@Nonnegative final int nActions)
  {
    // Assume each action icon is 20 pixels (incl. margin)
    final int nWidth = 20 * nActions;
    // Min width of 60!
    return new HCCol (Math.max (nWidth, 60));
  }

  @Nonnull
  protected static final SimpleURL createCreateLink (@Nonnull final String sMenuItemID)
  {
    return LinkUtils.getLinkToMenuItem (sMenuItemID).add (CHCParam.PARAM_ACTION, ACTION_CREATE);
  }

  @Nonnull
  protected static final SimpleURL createCreateLink ()
  {
    return LinkUtils.getSelfHref (new SMap (CHCParam.PARAM_ACTION, ACTION_CREATE));
  }

  @Nonnull
  protected static final ISimpleURL createViewLink (@Nonnull final IHasID <String> aCurObject)
  {
    return LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION, ACTION_VIEW).add (CHCParam.PARAM_OBJECT,
                                                                                            aCurObject.getID ()));
  }

  @Nonnull
  protected static final <T extends IHasID <String> & IHasDisplayName> HCA createEditLink (@Nonnull final T aCurObject,
                                                                                           @Nonnull final Locale aDisplayLocale)
  {
    return createEditLink (aCurObject,
                           EWebPageText.OBJECT_EDIT.getDisplayTextWithArgs (aDisplayLocale,
                                                                            aCurObject.getDisplayName ()));
  }

  @Nonnull
  protected static final IHCNode getEditImg ()
  {
    return EDefaultIcon.EDIT.getIcon ().getAsNode ();
  }

  @Nonnull
  protected static final HCA createEditLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aEditURL = LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION, ACTION_EDIT)
                                                                  .add (CHCParam.PARAM_OBJECT, aCurObject.getID ()));
    return new HCA (aEditURL).setTitle (sTitle).addChild (getEditImg ());
  }

  @Nonnull
  protected static final <T extends IHasID <String> & IHasDisplayName> HCA createCopyLink (@Nonnull final T aCurObject,
                                                                                           @Nonnull final Locale aDisplayLocale)
  {
    return createCopyLink (aCurObject,
                           EWebPageText.OBJECT_COPY.getDisplayTextWithArgs (aDisplayLocale,
                                                                            aCurObject.getDisplayName ()));
  }

  @Nonnull
  protected static final IHCNode getCopyImg ()
  {
    return EDefaultIcon.COPY.getIcon ().getAsNode ();
  }

  @Nonnull
  protected static final HCA createCopyLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aCopyURL = LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION, ACTION_COPY)
                                                                  .add (CHCParam.PARAM_OBJECT, aCurObject.getID ()));
    return new HCA (aCopyURL).setTitle (sTitle).addChild (getCopyImg ());
  }

  @Nonnull
  protected static final <T extends IHasID <String> & IHasDisplayName> HCA createDeleteLink (@Nonnull final T aCurObject,
                                                                                             @Nonnull final Locale aDisplayLocale)
  {
    return createDeleteLink (aCurObject,
                             EWebPageText.OBJECT_DELETE.getDisplayTextWithArgs (aDisplayLocale,
                                                                                aCurObject.getDisplayName ()));
  }

  @Nonnull
  protected static final IHCNode getDeleteImg ()
  {
    return EDefaultIcon.DELETE.getIcon ().getAsNode ();
  }

  @Nonnull
  protected static final HCA createDeleteLink (@Nonnull final IHasID <String> aCurObject, @Nullable final String sTitle)
  {
    final ISimpleURL aURL = LinkUtils.getSelfHref (new SMap ().add (CHCParam.PARAM_ACTION, ACTION_DELETE)
                                                              .add (CHCParam.PARAM_OBJECT, aCurObject.getID ()));
    return new HCA (aURL).setTitle (sTitle).addChild (getDeleteImg ());
  }

  @Nonnull
  protected static final IHCNode getCreateImg ()
  {
    return EDefaultIcon.NEW.getIcon ().getAsNode ();
  }

  @Nonnull
  protected static final HCA createNestedCreateLink (@Nonnull final IHasID <String> aCurObject,
                                                     @Nullable final String sTitle)
  {
    final ISimpleURL aURL = createCreateLink ().add (CHCParam.PARAM_OBJECT, aCurObject.getID ());
    return new HCA (aURL).setTitle (sTitle).addChild (getCreateImg ());
  }

  @Nonnull
  protected static IHCNode createIncorrectInputBox (final Locale aDisplayLocale)
  {
    return BootstrapErrorBox.create (EWebBasicsText.MSG_ERR_INCORRECT_INPUT.getDisplayText (aDisplayLocale));
  }

  /**
   * @param aDisplayLocale
   * @return Never <code>null</code>
   */
  @Nonnull
  public static IHCElement <?> createUploadButton (@Nonnull final Locale aDisplayLocale)
  {
    return new HCSpan ().addClasses (CBootstrapCSS.BTN, EBootstrapButtonType.SUCCESS)
                        .addChild (EWebBasicsText.FILE_SELECT.getDisplayText (aDisplayLocale));
  }

  @Nonnull
  public static IHCNode createImageView (@Nullable final UserDataObject aUDO, @Nonnull final Locale aDisplayLocale)
  {
    return createImageView (aUDO, 200, aDisplayLocale);
  }

  @Nonnull
  public static IHCNode createImageView (@Nullable final UserDataObject aUDO,
                                         final int nMaxWidth,
                                         @Nonnull final Locale aDisplayLocale)
  {
    if (aUDO == null)
      return HCEM.create (EWebPageText.IMAGE_NONE.getDisplayText (aDisplayLocale));

    final HCImg aImg = new HCImg ().setSrc (aUDO.getAsURL ());
    ScalableSize aSize = ImageDataManager.getImageSize (aUDO.getAsResource ());
    if (aSize != null)
    {
      if (nMaxWidth > 0 && nMaxWidth < aSize.getWidth ())
        aSize = aSize.getScaledToWidth (nMaxWidth);
      aImg.setExtent (aSize);
    }
    return aImg;
  }

  @Nullable
  protected static IHCNode createEmailLink (@Nullable final String sEmailAddress)
  {
    if (StringHelper.hasNoText (sEmailAddress))
      return null;
    return HCA_MailTo.createLinkedEmail (sEmailAddress);
  }

  @Nullable
  protected static IHCNode createEmailLink (@Nullable final IReadonlyExtendedEmailAddress aEmail)
  {
    if (aEmail == null)
      return null;
    return HCA_MailTo.createLinkedEmail (aEmail.getAddress (), aEmail.getPersonal ());
  }

  @Nullable
  protected static IHCNode createWebLink (@Nullable final String sWebSite)
  {
    if (StringHelper.hasNoText (sWebSite))
      return null;
    if (!URLValidator.isValid (sWebSite))
      return new HCTextNode (sWebSite);
    return new HCA (new SimpleURL (sWebSite)).setTarget (HCA_Target.BLANK).addChild (sWebSite);
  }
}
