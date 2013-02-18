package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.webctrls.facebook.AbstractFBNode;

public class FBLike extends AbstractFBNode
{
  /**
   * The default width of this plugin
   */
  public static final int DEFAULT_WIDTH = 450;

  private final ISimpleURL m_aURL;
  private final boolean m_bWithSendButton;
  private final EFBLikeLayout m_aLayout;
  private final boolean m_bShowFaces;
  private final int m_nWidth;
  private final EFBLikeAction m_aAction;
  private final EFBFont m_aFont;
  private final EFBColorScheme m_aColorScheme;
  private final String m_sRefInfo;

  /**
   * @param aURL
   *        the URL to like. The XFBML version defaults to the current page
   * @param bWithSendButton
   *        specifies whether to include a Send button with the Like button.
   *        This only works with the XFBML version.
   * @param aLayout
   *        General layout of the plugin (<b>standard</b>, box_count, button
   *        count). May be <code>null</code> to use default
   * @param bShowFaces
   *        specifies whether to display profile photos below the button
   *        (standard layout only)
   */
  public FBLike (@Nonnull final ISimpleURL aURL,
                 final boolean bWithSendButton,
                 @Nullable final EFBLikeLayout aLayout,
                 final boolean bShowFaces)
  {
    this (aURL, bWithSendButton, aLayout, bShowFaces, DEFAULT_WIDTH, null, null, null, null);
  }

  /**
   * @param aURL
   *        the URL to like. The XFBML version defaults to the current page
   * @param bWithSendButton
   *        specifies whether to include a Send button with the Like button.
   *        This only works with the XFBML version.
   * @param aLayout
   *        General layout of the plugin (<b>standard</b>, box_count, button
   *        count). May be <code>null</code> to use default
   * @param bShowFaces
   *        specifies whether to display profile photos below the button
   *        (standard layout only)
   * @param nWidth
   *        the width of the Like button
   * @param aAction
   *        the verb to display on the button (<b>like</b> | recommend). May be
   *        <code>null</code> to use default
   * @param aFont
   *        the font to display in the button. May be <code>null</code> to use
   *        default
   * @param aColorScheme
   *        the color scheme for the like button (<b>light</b> | dark). May be
   *        <code>null</code> to use default
   * @param sRefInfo
   *        a label for tracking referrals; must be less than 50 characters and
   *        can contain alphanumeric characters and some punctuation (currently
   *        +/=-.:_). The passed text will be shortened and invalid characters
   *        will be automatically replaced. The ref attribute causes two
   *        parameters to be added to the referrer URL when a user clicks a link
   *        from a stream story about a Like action:
   *        <ul>
   *        <li><b>fb_ref:</b> the ref parameter</li>
   *        <li><b>fb_source:</b> the stream type ('home', 'profile', 'search',
   *        'ticker', 'tickerdialog' or 'other') in which the click occurred and
   *        the story type ('oneline' or 'multiline'), concatenated with an
   *        underscore.</li>
   *        </ul>
   */
  public FBLike (@Nonnull final ISimpleURL aURL,
                 final boolean bWithSendButton,
                 @Nullable final EFBLikeLayout aLayout,
                 final boolean bShowFaces,
                 final int nWidth,
                 @Nullable final EFBLikeAction aAction,
                 @Nullable final EFBFont aFont,
                 @Nullable final EFBColorScheme aColorScheme,
                 @Nullable final String sRefInfo)
  {
    super ("like");
    if (aURL == null)
      throw new NullPointerException ("aURL must not be null!");
    m_aURL = aURL;
    m_bWithSendButton = bWithSendButton;
    m_aLayout = aLayout;
    m_bShowFaces = bShowFaces;
    m_nWidth = nWidth;
    m_aAction = aAction;
    m_aFont = aFont;
    m_aColorScheme = aColorScheme;
    m_sRefInfo = StringHelper.hasText (sRefInfo) ? createRefText (sRefInfo) : null;
  }

  @Override
  @OverrideOnDemand
  protected void applyProperties (@Nonnull final IMicroElement aElement,
                                  @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    aElement.setAttribute ("href", this.m_aURL.getAsString ());
    if (m_bWithSendButton)
    {
      aElement.setAttribute ("send", String.valueOf (true));
    }
    if (m_aLayout != null)
    {
      aElement.setAttribute ("layout", this.m_aLayout.getID ());
    }
    if (m_bShowFaces)
    {
      aElement.setAttribute ("show_faces", String.valueOf (true));
    }
    aElement.setAttribute ("width", this.m_nWidth);
    if (m_aAction != null)
    {
      aElement.setAttribute ("action", this.m_aAction.getID ());
    }
    if (m_aFont != null)
    {
      aElement.setAttribute ("font", this.m_aFont.getID ());
    }
    if (m_aColorScheme != null)
    {
      aElement.setAttribute ("colorscheme", this.m_aColorScheme.getID ());
    }
    if (StringHelper.hasText (this.m_sRefInfo))
    {
      aElement.setAttribute ("ref", this.m_sRefInfo);
    }
  }

  /**
   * Creates a label for tracking referrals from the passed String; Will be less
   * than 50 characters and can contain alphanumeric characters and some
   * punctuation (currently +/=-.:_).
   * 
   * @return a valid referrer text
   */
  @Nonnull
  protected static String createRefText (final String sText)
  {
    if (StringHelper.hasNoText (sText))
      return "";
    final String sReplaced = RegExHelper.stringReplacePattern ("[^A-Za-z0-9\\+/=\\-\\.\\:_]", sText, "");
    return StringHelper.getCutAfterLength (sReplaced, 49, null);
  }
}
