package com.phloc.webpages.theme;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.email.IEmailAddress;
import com.phloc.commons.gfx.ImageDataManager;
import com.phloc.commons.gfx.ScalableSize;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.commons.url.URLValidator;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.html.hc.html.AbstractHCTable;
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
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.EWebPageText;

public class WebPageStyleSimple implements IWebPageStyle
{
  @Nonnull
  public IHCNode createImageView (@Nullable final UserDataObject aUDO, @Nonnull final Locale aDisplayLocale)
  {
    return createImageView (aUDO, 200, aDisplayLocale);
  }

  @Nonnull
  public IHCNode createImageView (@Nullable final UserDataObject aUDO,
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
    return HCDiv.create (sText);
  }

  @Nonnull
  public IHCElement <?> createQuestionBox (@Nullable final String sText)
  {
    return HCDiv.create (sText);
  }

  @Nonnull
  public IHCElement <?> createSuccessBox (@Nullable final String sText)
  {
    return HCDiv.create (sText);
  }

  @Nonnull
  public AbstractHCTable <?> createTable (@Nullable final HCCol... aCols)
  {
    return new HCTable (aCols);
  }

  @Nonnull
  public DataTables createDefaultDataTables (@Nonnull final AbstractHCBaseTable <?> aTable,
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
}
