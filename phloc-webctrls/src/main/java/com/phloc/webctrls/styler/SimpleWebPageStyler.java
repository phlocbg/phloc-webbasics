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
package com.phloc.webctrls.styler;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.UnsupportedOperation;
import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.gfx.ImageDataManager;
import com.phloc.commons.gfx.ScalableSize;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLValidator;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCTable;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCButton;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCImg;
import com.phloc.html.hc.html.HCTable;
import com.phloc.html.hc.htmlext.HCA_MailTo;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.HCTableForm;
import com.phloc.webctrls.custom.table.HCTableFormView;
import com.phloc.webctrls.custom.table.IHCTableForm;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.custom.toolbar.IButtonToolbar;
import com.phloc.webctrls.custom.toolbar.SimpleButtonToolbar;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public class SimpleWebPageStyler implements IWebPageStyler
{
  public static final ICSSClassProvider CSS_CLASS_ERRORBOX = DefaultCSSClassProvider.create ("errorbox");
  public static final ICSSClassProvider CSS_CLASS_INFOBOX = DefaultCSSClassProvider.create ("infobox");
  public static final ICSSClassProvider CSS_CLASS_SUCCESSBOX = DefaultCSSClassProvider.create ("successbox");
  public static final ICSSClassProvider CSS_CLASS_QUESTIONBOX = DefaultCSSClassProvider.create ("questionbox");

  @Nonnull
  public IHCNode createImageView (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                  @Nullable final UserDataObject aUDO,
                                  @Nonnull final Locale aDisplayLocale)
  {
    return createImageView (aRequestScope, aUDO, 200, aDisplayLocale);
  }

  @Nonnull
  public IHCNode createImageView (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                  @Nullable final UserDataObject aUDO,
                                  final int nMaxWidth,
                                  @Nonnull final Locale aDisplayLocale)
  {
    if (aUDO == null)
      return HCEM.create (EWebPageStylerText.IMAGE_NONE.getDisplayText (aDisplayLocale));

    final HCImg aImg = new HCImg ().setSrc (aUDO.getAsURL (aRequestScope));
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
  public IHCNode createEmailLink (@Nullable final String sEmailAddress)
  {
    if (StringHelper.hasNoText (sEmailAddress))
      return null;
    return HCA_MailTo.createLinkedEmail (sEmailAddress);
  }

  @Nullable
  public IHCNode createEmailLink (@Nullable final IEmailAddress aEmail)
  {
    if (aEmail == null)
      return null;
    return HCA_MailTo.createLinkedEmail (aEmail.getAddress (), aEmail.getPersonal ());
  }

  @Nullable
  public IHCNode createWebLink (@Nullable final String sWebSite)
  {
    return createWebLink (sWebSite, HCA_Target.BLANK);
  }

  @Nullable
  public IHCNode createWebLink (@Nullable final String sWebSite, @Nullable final HCA_Target aTarget)
  {
    if (StringHelper.hasNoText (sWebSite))
      return null;
    if (!URLValidator.isValid (sWebSite))
      return new HCTextNode (sWebSite);
    return new HCA (new SimpleURL (sWebSite)).setTarget (aTarget).addChild (sWebSite);
  }

  @Nonnull
  public final IHCNode createIncorrectInputBox (@Nonnull final Locale aDisplayLocale)
  {
    return createErrorBox (EWebBasicsText.MSG_ERR_INCORRECT_INPUT.getDisplayText (aDisplayLocale));
  }

  @Nonnull
  public IHCElement <?> createErrorBox (@Nullable final String sText)
  {
    return HCDiv.create (sText).addClass (CSS_CLASS_ERRORBOX);
  }

  @Nonnull
  public IHCElement <?> createInfoBox (@Nullable final String sText)
  {
    return HCDiv.create (sText).addClass (CSS_CLASS_INFOBOX);
  }

  @Nonnull
  public IHCElement <?> createSuccessBox (@Nullable final String sText)
  {
    return HCDiv.create (sText).addClass (CSS_CLASS_SUCCESSBOX);
  }

  @Nonnull
  public IHCElement <?> createQuestionBox (@Nullable final String sText)
  {
    return HCDiv.create (sText).addClass (CSS_CLASS_QUESTIONBOX);
  }

  @Nonnull
  public IHCTable <?> createTable (@Nullable final HCCol... aCols)
  {
    return new HCTable (aCols);
  }

  @Nonnull
  public IHCTableForm <?> createTableForm (@Nullable final HCCol... aCols)
  {
    return new HCTableForm (aCols);
  }

  @Nonnull
  public IHCTableFormView <?> createTableFormView (@Nullable final HCCol... aCols)
  {
    return new HCTableFormView (aCols);
  }

  @Nonnull
  public DataTables createDefaultDataTables (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull final IHCTable <?> aTable,
                                             @Nonnull final Locale aDisplayLocale)
  {
    final DataTables ret = new DataTables (aTable);
    ret.setDisplayLocale (aDisplayLocale);
    ret.addAllColumns (aTable);
    return ret;
  }

  @Nonnull
  public IHCElement <?> createUploadButton (@Nonnull final Locale aDisplayLocale)
  {
    return new HCButton (EWebBasicsText.FILE_SELECT.getDisplayText (aDisplayLocale));
  }

  @Nonnull
  public IButtonToolbar <?> createToolbar ()
  {
    return new SimpleButtonToolbar ();
  }

  @Nonnull
  @UnsupportedOperation
  public ITabBox <?> createTabBox ()
  {
    throw new UnsupportedOperationException ();
  }
}
