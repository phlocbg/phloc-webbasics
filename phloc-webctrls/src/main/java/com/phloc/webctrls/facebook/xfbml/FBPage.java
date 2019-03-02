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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;

/**
 * This is an integration of the Facebook Page Plugin
 * (https://developers.facebook.com/docs/plugins/page-plugin) which lets you
 * embed a stream of a Facebook page inside website
 * 
 * @author Boris Gregorcic
 */
public class FBPage extends AbstractFBNodeFBXML
{
  /**
   * The default width of this plugin
   */
  public static final int DEFAULT_WIDTH = 340;
  public static final int MIN_WIDTH = 280;
  public static final int MAX_WIDTH = 500;

  public static final int MIN_HEIGHT = 130;
  public static final int DEFAULT_HEIGHT = 500;

  public static final boolean DEFAULT_HIDE_COVER = false;
  public static final boolean DEFAULT_SHOW_FACEPILE = true;
  public static final boolean DEFAULT_SHOW_POSTS = false;

  private static final long serialVersionUID = -3993366259061319709L;

  private final ISimpleURL m_aURL;
  private final int m_nWidth;
  private final int m_nHeight;
  private final boolean m_bHideCover;
  private final boolean m_bShowFacePile;
  private final boolean m_bShowPosts;

  /**
   * @param aURL
   *        The URL of the Facebook Page
   */
  public FBPage (@Nonnull final ISimpleURL aURL)
  {
    this (aURL, DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_HIDE_COVER, DEFAULT_SHOW_FACEPILE, DEFAULT_SHOW_POSTS);
  }

  /**
   * @param aURL
   *        The URL of the Facebook Page
   * @param nWidth
   *        The pixel width of the plugin. (min 280 - max 500), default:340
   * @param nHeight
   *        The maximum pixel height of the plugin. (min 130), default:500
   * @param bHideCover
   *        Hide cover photo in the header
   * @param bShowFacePile
   *        Whether or not to show face pile
   * @param bShowPosts
   *        Whether or not to show posts
   */
  public FBPage (@Nonnull final ISimpleURL aURL,
                 final int nWidth,
                 final int nHeight,
                 final boolean bHideCover,
                 final boolean bShowFacePile,
                 final boolean bShowPosts)
  {
    super ("page"); //$NON-NLS-1$
    ValueEnforcer.notNull (aURL, "URL"); //$NON-NLS-1$
    this.m_aURL = aURL;
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
    this.m_bHideCover = bHideCover;
    this.m_bShowFacePile = bShowFacePile;
    this.m_bShowPosts = bShowPosts;
  }

  @Override
  @OverrideOnDemand
  protected void applyProperties (@Nonnull final IMicroElement aElement,
                                  @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    aElement.setAttribute ("href", this.m_aURL.getAsString ()); //$NON-NLS-1$
    aElement.setAttribute ("width", this.m_nWidth); //$NON-NLS-1$
    aElement.setAttribute ("height", this.m_nHeight); //$NON-NLS-1$
    if (this.m_bHideCover != DEFAULT_HIDE_COVER)
    {
      aElement.setAttribute ("hide_cover", String.valueOf (this.m_bHideCover)); //$NON-NLS-1$
    }
    if (this.m_bShowFacePile != DEFAULT_SHOW_FACEPILE)
    {
      aElement.setAttribute ("show_facepile", String.valueOf (this.m_bShowFacePile)); //$NON-NLS-1$
    }
    if (this.m_bShowPosts != DEFAULT_SHOW_POSTS)
    {
      aElement.setAttribute ("show_posts", String.valueOf (this.m_bShowPosts)); //$NON-NLS-1$
    }
  }
}
