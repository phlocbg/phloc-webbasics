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
import javax.annotation.Nullable;

import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.html.HCI;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Bootstrap icons
 * 
 * @author Philip Helger
 */
public enum EBootstrapIcon implements IIcon
{
  ADJUST (CBootstrapCSS.ICON_ADJUST),
  ALIGN_CENTER (CBootstrapCSS.ICON_ALIGN_CENTER),
  ALIGN_JUSTIFY (CBootstrapCSS.ICON_ALIGN_JUSTIFY),
  ALIGN_LEFT (CBootstrapCSS.ICON_ALIGN_LEFT),
  ALIGN_RIGHT (CBootstrapCSS.ICON_ALIGN_RIGHT),
  ARROW_DOWN (CBootstrapCSS.ICON_ARROW_DOWN),
  ARROW_LEFT (CBootstrapCSS.ICON_ARROW_LEFT),
  ARROW_RIGHT (CBootstrapCSS.ICON_ARROW_RIGHT),
  ARROW_UP (CBootstrapCSS.ICON_ARROW_UP),
  ASTERISK (CBootstrapCSS.ICON_ASTERISK),
  BACKWARD (CBootstrapCSS.ICON_BACKWARD),
  BAN_CIRCLE (CBootstrapCSS.ICON_BAN_CIRCLE),
  BAR (CBootstrapCSS.ICON_BAR),
  BARCODE (CBootstrapCSS.ICON_BARCODE),
  BELL (CBootstrapCSS.ICON_BELL),
  BOLD (CBootstrapCSS.ICON_BOLD),
  BOOK (CBootstrapCSS.ICON_BOOK),
  BOOKMARK (CBootstrapCSS.ICON_BOOKMARK),
  BRIEFCASE (CBootstrapCSS.ICON_BRIEFCASE),
  BULLHORN (CBootstrapCSS.ICON_BULLHORN),
  CALENDAR (CBootstrapCSS.ICON_CALENDAR),
  CAMERA (CBootstrapCSS.ICON_CAMERA),
  CERTIFICATE (CBootstrapCSS.ICON_CERTIFICATE),
  CHECK (CBootstrapCSS.ICON_CHECK),
  CHEVRON_DOWN (CBootstrapCSS.ICON_CHEVRON_DOWN),
  CHEVRON_LEFT (CBootstrapCSS.ICON_CHEVRON_LEFT),
  CHEVRON_RIGHT (CBootstrapCSS.ICON_CHEVRON_RIGHT),
  CHEVRON_UP (CBootstrapCSS.ICON_CHEVRON_UP),
  CIRCLE_ARROW_DOWN (CBootstrapCSS.ICON_CIRCLE_ARROW_DOWN),
  CIRCLE_ARROW_LEFT (CBootstrapCSS.ICON_CIRCLE_ARROW_LEFT),
  CIRCLE_ARROW_RIGHT (CBootstrapCSS.ICON_CIRCLE_ARROW_RIGHT),
  CIRCLE_ARROW_UP (CBootstrapCSS.ICON_CIRCLE_ARROW_UP),
  COG (CBootstrapCSS.ICON_COG),
  COMMENT (CBootstrapCSS.ICON_COMMENT),
  DOWNLOAD (CBootstrapCSS.ICON_DOWNLOAD),
  DOWNLOAD_ALT (CBootstrapCSS.ICON_DOWNLOAD_ALT),
  EDIT (CBootstrapCSS.ICON_EDIT),
  EJECT (CBootstrapCSS.ICON_EJECT),
  ENVELOPE (CBootstrapCSS.ICON_ENVELOPE),
  EXCLAMATION_SIGN (CBootstrapCSS.ICON_EXCLAMATION_SIGN),
  EYE_CLOSE (CBootstrapCSS.ICON_EYE_CLOSE),
  EYE_OPEN (CBootstrapCSS.ICON_EYE_OPEN),
  FACETIME_VIDEO (CBootstrapCSS.ICON_FACETIME_VIDEO),
  FAST_BACKWARD (CBootstrapCSS.ICON_FAST_BACKWARD),
  FAST_FORWARD (CBootstrapCSS.ICON_FAST_FORWARD),
  FILE (CBootstrapCSS.ICON_FILE),
  FILM (CBootstrapCSS.ICON_FILM),
  FILTER (CBootstrapCSS.ICON_FILTER),
  FIRE (CBootstrapCSS.ICON_FIRE),
  FLAG (CBootstrapCSS.ICON_FLAG),
  FOLDER_CLOSE (CBootstrapCSS.ICON_FOLDER_CLOSE),
  FOLDER_OPEN (CBootstrapCSS.ICON_FOLDER_OPEN),
  FONT (CBootstrapCSS.ICON_FONT),
  FORWARD (CBootstrapCSS.ICON_FORWARD),
  FULLSCREEN (CBootstrapCSS.ICON_FULLSCREEN),
  GIFT (CBootstrapCSS.ICON_GIFT),
  GLASS (CBootstrapCSS.ICON_GLASS),
  GLOBE (CBootstrapCSS.ICON_GLOBE),
  HAND_DOWN (CBootstrapCSS.ICON_HAND_DOWN),
  HAND_LEFT (CBootstrapCSS.ICON_HAND_LEFT),
  HAND_RIGHT (CBootstrapCSS.ICON_HAND_RIGHT),
  HAND_UP (CBootstrapCSS.ICON_HAND_UP),
  HDD (CBootstrapCSS.ICON_HDD),
  HEADPHONES (CBootstrapCSS.ICON_HEADPHONES),
  HEART (CBootstrapCSS.ICON_HEART),
  HOME (CBootstrapCSS.ICON_HOME),
  INBOX (CBootstrapCSS.ICON_INBOX),
  INDENT_LEFT (CBootstrapCSS.ICON_INDENT_LEFT),
  INDENT_RIGHT (CBootstrapCSS.ICON_INDENT_RIGHT),
  INFO_SIGN (CBootstrapCSS.ICON_INFO_SIGN),
  ITALIC (CBootstrapCSS.ICON_ITALIC),
  LEAF (CBootstrapCSS.ICON_LEAF),
  LIST (CBootstrapCSS.ICON_LIST),
  LIST_ALT (CBootstrapCSS.ICON_LIST_ALT),
  LOCK (CBootstrapCSS.ICON_LOCK),
  MAGNET (CBootstrapCSS.ICON_MAGNET),
  MAP_MARKER (CBootstrapCSS.ICON_MAP_MARKER),
  MINUS (CBootstrapCSS.ICON_MINUS),
  MINUS_SIGN (CBootstrapCSS.ICON_MINUS_SIGN),
  MOVE (CBootstrapCSS.ICON_MOVE),
  MUSIC (CBootstrapCSS.ICON_MUSIC),
  OFF (CBootstrapCSS.ICON_OFF),
  OK (CBootstrapCSS.ICON_OK),
  OK_CIRCLE (CBootstrapCSS.ICON_OK_CIRCLE),
  OK_SIGN (CBootstrapCSS.ICON_OK_SIGN),
  PAUSE (CBootstrapCSS.ICON_PAUSE),
  PENCIL (CBootstrapCSS.ICON_PENCIL),
  PICTURE (CBootstrapCSS.ICON_PICTURE),
  PLANE (CBootstrapCSS.ICON_PLANE),
  PLAY (CBootstrapCSS.ICON_PLAY),
  PLAY_CIRCLE (CBootstrapCSS.ICON_PLAY_CIRCLE),
  PLUS (CBootstrapCSS.ICON_PLUS),
  PLUS_SIGN (CBootstrapCSS.ICON_PLUS_SIGN),
  PRINT (CBootstrapCSS.ICON_PRINT),
  QRCODE (CBootstrapCSS.ICON_QRCODE),
  QUESTION_SIGN (CBootstrapCSS.ICON_QUESTION_SIGN),
  RANDOM (CBootstrapCSS.ICON_RANDOM),
  REFRESH (CBootstrapCSS.ICON_REFRESH),
  REMOVE (CBootstrapCSS.ICON_REMOVE),
  REMOVE_CIRCLE (CBootstrapCSS.ICON_REMOVE_CIRCLE),
  REMOVE_SIGN (CBootstrapCSS.ICON_REMOVE_SIGN),
  REPEAT (CBootstrapCSS.ICON_REPEAT),
  RESIZE_FULL (CBootstrapCSS.ICON_RESIZE_FULL),
  RESIZE_HORIZONTAL (CBootstrapCSS.ICON_RESIZE_HORIZONTAL),
  RESIZE_SMALL (CBootstrapCSS.ICON_RESIZE_SMALL),
  RESIZE_VERTICAL (CBootstrapCSS.ICON_RESIZE_VERTICAL),
  RETWEET (CBootstrapCSS.ICON_RETWEET),
  ROAD (CBootstrapCSS.ICON_ROAD),
  SCREENSHOT (CBootstrapCSS.ICON_SCREENSHOT),
  SEARCH (CBootstrapCSS.ICON_SEARCH),
  SHARE (CBootstrapCSS.ICON_SHARE),
  SHARE_ALT (CBootstrapCSS.ICON_SHARE_ALT),
  SHOPPING_CART (CBootstrapCSS.ICON_SHOPPING_CART),
  SIGNAL (CBootstrapCSS.ICON_SIGNAL),
  STAR (CBootstrapCSS.ICON_STAR),
  STAR_EMPTY (CBootstrapCSS.ICON_STAR_EMPTY),
  STEP_BACKWARD (CBootstrapCSS.ICON_STEP_BACKWARD),
  STEP_FORWARD (CBootstrapCSS.ICON_STEP_FORWARD),
  STOP (CBootstrapCSS.ICON_STOP),
  TAG (CBootstrapCSS.ICON_TAG),
  TAGS (CBootstrapCSS.ICON_TAGS),
  TASKS (CBootstrapCSS.ICON_TASKS),
  TEXT_HEIGHT (CBootstrapCSS.ICON_TEXT_HEIGHT),
  TEXT_WIDTH (CBootstrapCSS.ICON_TEXT_WIDTH),
  TH (CBootstrapCSS.ICON_TH),
  TH_LARGE (CBootstrapCSS.ICON_TH_LARGE),
  TH_LIST (CBootstrapCSS.ICON_TH_LIST),
  THUMBS_DOWN (CBootstrapCSS.ICON_THUMBS_DOWN),
  THUMBS_UP (CBootstrapCSS.ICON_THUMBS_UP),
  TIME (CBootstrapCSS.ICON_TIME),
  TINT (CBootstrapCSS.ICON_TINT),
  TRASH (CBootstrapCSS.ICON_TRASH),
  UPLOAD (CBootstrapCSS.ICON_UPLOAD),
  USER (CBootstrapCSS.ICON_USER),
  VOLUME_DOWN (CBootstrapCSS.ICON_VOLUME_DOWN),
  VOLUME_OFF (CBootstrapCSS.ICON_VOLUME_OFF),
  VOLUME_UP (CBootstrapCSS.ICON_VOLUME_UP),
  WARNING_SIGN (CBootstrapCSS.ICON_WARNING_SIGN),
  WRENCH (CBootstrapCSS.ICON_WRENCH),
  ZOOM_IN (CBootstrapCSS.ICON_ZOOM_IN),
  ZOOM_OUT (CBootstrapCSS.ICON_ZOOM_OUT);

  /** The CSS class to be applied to a Bootstrap icon to make it white */
  public static final ICSSClassProvider CSS_CLASS_ICON_WHITE = CBootstrapCSS.ICON_WHITE;

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapIcon (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nullable
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }

  @Nonnull
  public IHCElement <?> getAsNode ()
  {
    return new HCI ().addClass (m_aCSSClass);
  }

  @Nonnull
  public IHCElement <?> getAsNodeInverted ()
  {
    return new HCI ().addClasses (this, CSS_CLASS_ICON_WHITE);
  }

  @Nonnull
  public BootstrapIconWhite getAsWhiteIcon ()
  {
    return new BootstrapIconWhite (this);
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
    DefaultIcons.set (EDefaultIcon.COPY, RETWEET);
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
