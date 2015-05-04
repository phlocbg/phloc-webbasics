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
package com.phloc.webctrls.facebook.xfbml;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;

public class FBLike extends AbstractFBNodeFBXML
{
  private static final long serialVersionUID = 9145379725593321414L;

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
    super ("like"); //$NON-NLS-1$
    ValueEnforcer.notNull (aURL, "URL"); //$NON-NLS-1$
    this.m_aURL = aURL;
    this.m_bWithSendButton = bWithSendButton;
    this.m_aLayout = aLayout;
    this.m_bShowFaces = bShowFaces;
    this.m_nWidth = nWidth;
    this.m_aAction = aAction;
    this.m_aFont = aFont;
    this.m_aColorScheme = aColorScheme;
    this.m_sRefInfo = StringHelper.hasText (sRefInfo) ? createRefText (sRefInfo) : null;
  }

  @Override
  @OverrideOnDemand
  protected void applyProperties (@Nonnull final IMicroElement aElement,
                                  @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    aElement.setAttribute ("href", this.m_aURL.getAsString ()); //$NON-NLS-1$
    if (this.m_bWithSendButton)
      aElement.setAttribute ("send", Boolean.TRUE.toString ()); //$NON-NLS-1$
    if (this.m_aLayout != null)
      aElement.setAttribute ("layout", this.m_aLayout.getID ()); //$NON-NLS-1$
    if (this.m_bShowFaces)
      aElement.setAttribute ("show_faces", Boolean.TRUE.toString ()); //$NON-NLS-1$
    aElement.setAttribute ("width", this.m_nWidth); //$NON-NLS-1$
    if (this.m_aAction != null)
      aElement.setAttribute ("action", this.m_aAction.getID ()); //$NON-NLS-1$
    if (this.m_aFont != null)
      aElement.setAttribute ("font", this.m_aFont.getID ()); //$NON-NLS-1$
    if (this.m_aColorScheme != null)
      aElement.setAttribute ("colorscheme", this.m_aColorScheme.getID ()); //$NON-NLS-1$
    if (StringHelper.hasText (this.m_sRefInfo))
      aElement.setAttribute ("ref", this.m_sRefInfo); //$NON-NLS-1$
  }

  /**
   * Creates a label for tracking referrals from the passed String; Will be less
   * than 50 characters and can contain alphanumeric characters and some
   * punctuation (currently +/=-.:_).
   * 
   * @return a valid referrer text
   */
  @Nonnull
  protected static String createRefText (@Nullable final String sText)
  {
    if (StringHelper.hasNoText (sText))
      return ""; //$NON-NLS-1$
    final String sReplaced = RegExHelper.stringReplacePattern ("[^A-Za-z0-9\\+/=\\-\\.\\:_]", sText, ""); //$NON-NLS-1$ //$NON-NLS-2$
    return StringHelper.getCutAfterLength (sReplaced, 49);
  }
}
