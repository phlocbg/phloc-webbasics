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
package com.phloc.webctrls.bootstrap;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.html.HCI;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Bootstrap icons
 * 
 * @author philip
 */
public enum EBootstrapIcon implements IIcon
{
  GLASS ("icon-glass"),
  MUSIC ("icon-music"),
  SEARCH ("icon-search"),
  ENVELOPE ("icon-envelope"),
  HEART ("icon-heart"),
  STAR ("icon-star"),
  STAR_EMPTY ("icon-star-empty"),
  USER ("icon-user"),
  FILM ("icon-film"),
  TH_LARGE ("icon-th-large"),
  TH ("icon-th"),
  TH_LIST ("icon-th-list"),
  OK ("icon-ok"),
  REMOVE ("icon-remove"),
  ZOOM_IN ("icon-zoom-in"),
  ZOOM_OUT ("icon-zoom-out"),
  OFF ("icon-off"),
  SIGNAL ("icon-signal"),
  COG ("icon-cog"),
  TRASH ("icon-trash"),
  HOME ("icon-home"),
  FILE ("icon-file"),
  TIME ("icon-time"),
  ROAD ("icon-road"),
  DOWNLOAD_ALT ("icon-download-alt"),
  DOWNLOAD ("icon-download"),
  UPLOAD ("icon-upload"),
  INBOX ("icon-inbox"),
  PLAY_CIRCLE ("icon-play-circle"),
  REPEAT ("icon-repeat"),
  REFRESH ("icon-refresh"),
  LIST_ALT ("icon-list-alt"),
  LOCK ("icon-lock"),
  FLAG ("icon-flag"),
  HEADPHONES ("icon-headphones"),
  VOLUME_OFF ("icon-volume-off"),
  VOLUME_DOWN ("icon-volume-down"),
  VOLUME_UP ("icon-volume-up"),
  QRCODE ("icon-qrcode"),
  BARCODE ("icon-barcode"),
  TAG ("icon-tag"),
  TAGS ("icon-tags"),
  BOOK ("icon-book"),
  BOOKMARK ("icon-bookmark"),
  PRINT ("icon-print"),
  CAMERA ("icon-camera"),
  FONT ("icon-font"),
  BOLD ("icon-bold"),
  ITALIC ("icon-italic"),
  TEXT_HEIGHT ("icon-text-height"),
  TEXT_WIDTH ("icon-text-width"),
  ALIGN_LEFT ("icon-align-left"),
  ALIGN_CENTER ("icon-align-center"),
  ALIGN_RIGHT ("icon-align-right"),
  ALIGN_JUSTIFY ("icon-align-justify"),
  LIST ("icon-list"),
  INDENT_LEFT ("icon-indent-left"),
  INDENT_RIGHT ("icon-indent-right"),
  FACETIME_VIDEO ("icon-facetime-video"),
  PICTURE ("icon-picture"),
  PENCIL ("icon-pencil"),
  MAP_MARKER ("icon-map-marker"),
  ADJUST ("icon-adjust"),
  TINT ("icon-tint"),
  EDIT ("icon-edit"),
  SHARE ("icon-share"),
  CHECK ("icon-check"),
  MOVE ("icon-move"),
  STEP_BACKWARD ("icon-step-backward"),
  FAST_BACKWARD ("icon-fast-backward"),
  BACKWARD ("icon-backward"),
  PLAY ("icon-play"),
  PAUSE ("icon-pause"),
  STOP ("icon-stop"),
  FORWARD ("icon-forward"),
  FAST_FORWARD ("icon-fast-forward"),
  STEP_FORWARD ("icon-step-forward"),
  EJECT ("icon-eject"),
  CHEVRON_LEFT ("icon-chevron-left"),
  CHEVRON_RIGHT ("icon-chevron-right"),
  PLUS_SIGN ("icon-plus-sign"),
  MINUS_SIGN ("icon-minus-sign"),
  REMOVE_SIGN ("icon-remove-sign"),
  OK_SIGN ("icon-ok-sign"),
  QUESTION_SIGN ("icon-question-sign"),
  INFO_SIGN ("icon-info-sign"),
  SCREENSHOT ("icon-screenshot"),
  REMOVE_CIRCLE ("icon-remove-circle"),
  OK_CIRCLE ("icon-ok-circle"),
  BAN_CIRCLE ("icon-ban-circle"),
  ARROW_LEFT ("icon-arrow-left"),
  ARROW_RIGHT ("icon-arrow-right"),
  ARROW_UP ("icon-arrow-up"),
  ARROW_DOWN ("icon-arrow-down"),
  SHARE_ALT ("icon-share-alt"),
  RESIZE_FULL ("icon-resize-full"),
  RESIZE_SMALL ("icon-resize-small"),
  PLUS ("icon-plus"),
  MINUS ("icon-minus"),
  ASTERISK ("icon-asterisk"),
  EXCLAMATION_SIGN ("icon-exclamation-sign"),
  GIFT ("icon-gift"),
  LEAF ("icon-leaf"),
  FIRE ("icon-fire"),
  EYE_OPEN ("icon-eye-open"),
  EYE_CLOSE ("icon-eye-close"),
  WARNING_SIGN ("icon-warning-sign"),
  PLANE ("icon-plane"),
  CALENDAR ("icon-calendar"),
  RANDOM ("icon-random"),
  COMMENT ("icon-comment"),
  MAGNET ("icon-magnet"),
  CHEVRON_UP ("icon-chevron-up"),
  CHEVRON_DOWN ("icon-chevron-down"),
  RETWEET ("icon-retweet"),
  SHOPPING_CART ("icon-shopping-cart"),
  FOLDER_CLOSE ("icon-folder-close"),
  FOLDER_OPEN ("icon-folder-open"),
  RESIZE_VERTICAL ("icon-resize-vertical"),
  RESIZE_HORIZONTAL ("icon-resize-horizontal"),
  HDD ("icon-hdd"),
  BULLHORN ("icon-bullhorn"),
  BELL ("icon-bell"),
  CERTIFICATE ("icon-certificate"),
  THUMBS_UP ("icon-thumbs-up"),
  THUMBS_DOWN ("icon-thumbs-down"),
  HAND_RIGHT ("icon-hand-right"),
  HAND_LEFT ("icon-hand-left"),
  HAND_UP ("icon-hand-up"),
  HAND_DOWN ("icon-hand-down"),
  CIRCLE_ARROW_RIGHT ("icon-circle-arrow-right"),
  CIRCLE_ARROW_LEFT ("icon-circle-arrow-left"),
  CIRCLE_ARROW_UP ("icon-circle-arrow-up"),
  CIRCLE_ARROW_DOWN ("icon-circle-arrow-down"),
  GLOBE ("icon-globe"),
  WRENCH ("icon-wrench"),
  TASKS ("icon-tasks"),
  FILTER ("icon-filter"),
  BRIEFCASE ("icon-briefcase"),
  FULLSCREEN ("icon-fullscreen");

  public static final ICSSClassProvider CSS_CLASS_ICON_WHITE = DefaultCSSClassProvider.create ("icon-white");

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
  public IHCElement <?> getAsNode ()
  {
    return new HCI ().addClass (this);
  }

  @Nonnull
  public IHCElement <?> getAsNodeInverted ()
  {
    return new HCI ().addClasses (this, CSS_CLASS_ICON_WHITE);
  }

  @Nonnull
  public BootstrapIconWhite getAsWhiteIcon ()
  {
    return new BootstrapIconWhite (m_sCSSClass);
  }

  @Nonnull
  public <T extends IHCElement <?>> T applyToNode (@Nonnull final T aElement)
  {
    aElement.addClass (this);
    return aElement;
  }

  public static void setAsDefault ()
  {
    DefaultIcons.set (EDefaultIcon.ADD, PLUS_SIGN);
    DefaultIcons.set (EDefaultIcon.BACK, CIRCLE_ARROW_LEFT);
    DefaultIcons.set (EDefaultIcon.BACK_TO_LIST, CIRCLE_ARROW_LEFT);
    DefaultIcons.set (EDefaultIcon.CANCEL, REMOVE_CIRCLE);
    DefaultIcons.set (EDefaultIcon.DELETE, REMOVE);
    DefaultIcons.set (EDefaultIcon.DOWN, ARROW_DOWN);
    DefaultIcons.set (EDefaultIcon.EDIT, PENCIL);
    DefaultIcons.set (EDefaultIcon.HELP, QUESTION_SIGN);
    DefaultIcons.set (EDefaultIcon.INFO, INFO_SIGN);
    DefaultIcons.set (EDefaultIcon.MAGNIFIER, ZOOM_IN);
    DefaultIcons.set (EDefaultIcon.MINUS, MINUS);
    DefaultIcons.set (EDefaultIcon.NEW, FILE);
    DefaultIcons.set (EDefaultIcon.NEXT, CIRCLE_ARROW_RIGHT);
    DefaultIcons.set (EDefaultIcon.NO, REMOVE);
    DefaultIcons.set (EDefaultIcon.PLUS, PLUS);
    DefaultIcons.set (EDefaultIcon.SAVE, HDD);
    DefaultIcons.set (EDefaultIcon.SAVE_ALL, HDD);
    DefaultIcons.set (EDefaultIcon.SAVE_AS, HDD);
    DefaultIcons.set (EDefaultIcon.SAVE_CLOSE, HDD);
    DefaultIcons.set (EDefaultIcon.UP, ARROW_UP);
    DefaultIcons.set (EDefaultIcon.YES, OK);
  }

  public static void setAsDefaultWhite ()
  {
    setAsDefault ();
    for (final EDefaultIcon eDefault : EDefaultIcon.values ())
      DefaultIcons.set (eDefault, ((EBootstrapIcon) DefaultIcons.get (eDefault)).getAsWhiteIcon ());
  }
}
