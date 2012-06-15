/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.ui.bootstrap;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCI;

/**
 * Bootstrap icons
 * 
 * @author philip
 */
public enum EBootstrapIcon implements ICSSClassProvider
{
  ICON_WHITE ("icon-white"),
  ICON_GLASS ("icon-glass"),
  ICON_MUSIC ("icon-music"),
  ICON_SEARCH ("icon-search"),
  ICON_ENVELOPE ("icon-envelope"),
  ICON_HEART ("icon-heart"),
  ICON_STAR ("icon-star"),
  ICON_STAR_EMPTY ("icon-star-empty"),
  ICON_USER ("icon-user"),
  ICON_FILM ("icon-film"),
  ICON_TH_LARGE ("icon-th-large"),
  ICON_TH ("icon-th"),
  ICON_TH_LIST ("icon-th-list"),
  ICON_OK ("icon-ok"),
  ICON_REMOVE ("icon-remove"),
  ICON_ZOOM_IN ("icon-zoom-in"),
  ICON_ZOOM_OUT ("icon-zoom-out"),
  ICON_OFF ("icon-off"),
  ICON_SIGNAL ("icon-signal"),
  ICON_COG ("icon-cog"),
  ICON_TRASH ("icon-trash"),
  ICON_HOME ("icon-home"),
  ICON_FILE ("icon-file"),
  ICON_TIME ("icon-time"),
  ICON_ROAD ("icon-road"),
  ICON_DOWNLOAD_ALT ("icon-download-alt"),
  ICON_DOWNLOAD ("icon-download"),
  ICON_UPLOAD ("icon-upload"),
  ICON_INBOX ("icon-inbox"),
  ICON_PLAY_CIRCLE ("icon-play-circle"),
  ICON_REPEAT ("icon-repeat"),
  ICON_REFRESH ("icon-refresh"),
  ICON_LIST_ALT ("icon-list-alt"),
  ICON_LOCK ("icon-lock"),
  ICON_FLAG ("icon-flag"),
  ICON_HEADPHONES ("icon-headphones"),
  ICON_VOLUME_OFF ("icon-volume-off"),
  ICON_VOLUME_DOWN ("icon-volume-down"),
  ICON_VOLUME_UP ("icon-volume-up"),
  ICON_QRCODE ("icon-qrcode"),
  ICON_BARCODE ("icon-barcode"),
  ICON_TAG ("icon-tag"),
  ICON_TAGS ("icon-tags"),
  ICON_BOOK ("icon-book"),
  ICON_BOOKMARK ("icon-bookmark"),
  ICON_PRINT ("icon-print"),
  ICON_CAMERA ("icon-camera"),
  ICON_FONT ("icon-font"),
  ICON_BOLD ("icon-bold"),
  ICON_ITALIC ("icon-italic"),
  ICON_TEXT_HEIGHT ("icon-text-height"),
  ICON_TEXT_WIDTH ("icon-text-width"),
  ICON_ALIGN_LEFT ("icon-align-left"),
  ICON_ALIGN_CENTER ("icon-align-center"),
  ICON_ALIGN_RIGHT ("icon-align-right"),
  ICON_ALIGN_JUSTIFY ("icon-align-justify"),
  ICON_LIST ("icon-list"),
  ICON_INDENT_LEFT ("icon-indent-left"),
  ICON_INDENT_RIGHT ("icon-indent-right"),
  ICON_FACETIME_VIDEO ("icon-facetime-video"),
  ICON_PICTURE ("icon-picture"),
  ICON_PENCIL ("icon-pencil"),
  ICON_MAP_MARKER ("icon-map-marker"),
  ICON_ADJUST ("icon-adjust"),
  ICON_TINT ("icon-tint"),
  ICON_EDIT ("icon-edit"),
  ICON_SHARE ("icon-share"),
  ICON_CHECK ("icon-check"),
  ICON_MOVE ("icon-move"),
  ICON_STEP_BACKWARD ("icon-step-backward"),
  ICON_FAST_BACKWARD ("icon-fast-backward"),
  ICON_BACKWARD ("icon-backward"),
  ICON_PLAY ("icon-play"),
  ICON_PAUSE ("icon-pause"),
  ICON_STOP ("icon-stop"),
  ICON_FORWARD ("icon-forward"),
  ICON_FAST_FORWARD ("icon-fast-forward"),
  ICON_STEP_FORWARD ("icon-step-forward"),
  ICON_EJECT ("icon-eject"),
  ICON_CHEVRON_LEFT ("icon-chevron-left"),
  ICON_CHEVRON_RIGHT ("icon-chevron-right"),
  ICON_PLUS_SIGN ("icon-plus-sign"),
  ICON_MINUS_SIGN ("icon-minus-sign"),
  ICON_REMOVE_SIGN ("icon-remove-sign"),
  ICON_OK_SIGN ("icon-ok-sign"),
  ICON_QUESTION_SIGN ("icon-question-sign"),
  ICON_INFO_SIGN ("icon-info-sign"),
  ICON_SCREENSHOT ("icon-screenshot"),
  ICON_REMOVE_CIRCLE ("icon-remove-circle"),
  ICON_OK_CIRCLE ("icon-ok-circle"),
  ICON_BAN_CIRCLE ("icon-ban-circle"),
  ICON_ARROW_LEFT ("icon-arrow-left"),
  ICON_ARROW_RIGHT ("icon-arrow-right"),
  ICON_ARROW_UP ("icon-arrow-up"),
  ICON_ARROW_DOWN ("icon-arrow-down"),
  ICON_SHARE_ALT ("icon-share-alt"),
  ICON_RESIZE_FULL ("icon-resize-full"),
  ICON_RESIZE_SMALL ("icon-resize-small"),
  ICON_PLUS ("icon-plus"),
  ICON_MINUS ("icon-minus"),
  ICON_ASTERISK ("icon-asterisk"),
  ICON_EXCLAMATION_SIGN ("icon-exclamation-sign"),
  ICON_GIFT ("icon-gift"),
  ICON_LEAF ("icon-leaf"),
  ICON_FIRE ("icon-fire"),
  ICON_EYE_OPEN ("icon-eye-open"),
  ICON_EYE_CLOSE ("icon-eye-close"),
  ICON_WARNING_SIGN ("icon-warning-sign"),
  ICON_PLANE ("icon-plane"),
  ICON_CALENDAR ("icon-calendar"),
  ICON_RANDOM ("icon-random"),
  ICON_COMMENT ("icon-comment"),
  ICON_MAGNET ("icon-magnet"),
  ICON_CHEVRON_UP ("icon-chevron-up"),
  ICON_CHEVRON_DOWN ("icon-chevron-down"),
  ICON_RETWEET ("icon-retweet"),
  ICON_SHOPPING_CART ("icon-shopping-cart"),
  ICON_FOLDER_CLOSE ("icon-folder-close"),
  ICON_FOLDER_OPEN ("icon-folder-open"),
  ICON_RESIZE_VERTICAL ("icon-resize-vertical"),
  ICON_RESIZE_HORIZONTAL ("icon-resize-horizontal");

  private final String m_sCSSClass;

  private EBootstrapIcon (@Nonnull @Nonempty final String sCSSClass)
  {
    m_sCSSClass = sCSSClass;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }

  @Nonnull
  public IHCNode getAsNode ()
  {
    return new HCI ().addClass (this);
  }
}
