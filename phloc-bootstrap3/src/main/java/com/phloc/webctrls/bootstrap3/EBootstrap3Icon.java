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
package com.phloc.webctrls.bootstrap3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.webctrls.custom.DefaultIcons;
import com.phloc.webctrls.custom.EDefaultIcon;
import com.phloc.webctrls.custom.IIcon;

/**
 * Bootstrap3 icons
 * 
 * @author Philip Helger
 */
public enum EBootstrap3Icon implements IIcon
{
  ADJUST (CBootstrap3CSS.GLYPHICON_ADJUST),
  ALIGN_CENTER (CBootstrap3CSS.GLYPHICON_ALIGN_CENTER),
  ALIGN_JUSTIFY (CBootstrap3CSS.GLYPHICON_ALIGN_JUSTIFY),
  ALIGN_LEFT (CBootstrap3CSS.GLYPHICON_ALIGN_LEFT),
  ALIGN_RIGHT (CBootstrap3CSS.GLYPHICON_ALIGN_RIGHT),
  ARROW_DOWN (CBootstrap3CSS.GLYPHICON_ARROW_DOWN),
  ARROW_LEFT (CBootstrap3CSS.GLYPHICON_ARROW_LEFT),
  ARROW_RIGHT (CBootstrap3CSS.GLYPHICON_ARROW_RIGHT),
  ARROW_UP (CBootstrap3CSS.GLYPHICON_ARROW_UP),
  ASTERISK (CBootstrap3CSS.GLYPHICON_ASTERISK),
  BACKWARD (CBootstrap3CSS.GLYPHICON_BACKWARD),
  BAN_CIRCLE (CBootstrap3CSS.GLYPHICON_BAN_CIRCLE),
  BARCODE (CBootstrap3CSS.GLYPHICON_BARCODE),
  BELL (CBootstrap3CSS.GLYPHICON_BELL),
  BOLD (CBootstrap3CSS.GLYPHICON_BOLD),
  BOOK (CBootstrap3CSS.GLYPHICON_BOOK),
  BOOKMARK (CBootstrap3CSS.GLYPHICON_BOOKMARK),
  BRIEFCASE (CBootstrap3CSS.GLYPHICON_BRIEFCASE),
  BULLHORN (CBootstrap3CSS.GLYPHICON_BULLHORN),
  CALENDAR (CBootstrap3CSS.GLYPHICON_CALENDAR),
  CAMERA (CBootstrap3CSS.GLYPHICON_CAMERA),
  CERTIFICATE (CBootstrap3CSS.GLYPHICON_CERTIFICATE),
  CHECK (CBootstrap3CSS.GLYPHICON_CHECK),
  CHEVRON_DOWN (CBootstrap3CSS.GLYPHICON_CHEVRON_DOWN),
  CHEVRON_LEFT (CBootstrap3CSS.GLYPHICON_CHEVRON_LEFT),
  CHEVRON_RIGHT (CBootstrap3CSS.GLYPHICON_CHEVRON_RIGHT),
  CHEVRON_UP (CBootstrap3CSS.GLYPHICON_CHEVRON_UP),
  CIRCLE_ARROW_DOWN (CBootstrap3CSS.GLYPHICON_CIRCLE_ARROW_DOWN),
  CIRCLE_ARROW_LEFT (CBootstrap3CSS.GLYPHICON_CIRCLE_ARROW_LEFT),
  CIRCLE_ARROW_RIGHT (CBootstrap3CSS.GLYPHICON_CIRCLE_ARROW_RIGHT),
  CIRCLE_ARROW_UP (CBootstrap3CSS.GLYPHICON_CIRCLE_ARROW_UP),
  CLOUD (CBootstrap3CSS.GLYPHICON_CLOUD),
  CLOUD_DOWNLOAD (CBootstrap3CSS.GLYPHICON_CLOUD_DOWNLOAD),
  CLOUD_UPLOAD (CBootstrap3CSS.GLYPHICON_CLOUD_UPLOAD),
  COG (CBootstrap3CSS.GLYPHICON_COG),
  COLLAPSE_DOWN (CBootstrap3CSS.GLYPHICON_COLLAPSE_DOWN),
  COLLAPSE_UP (CBootstrap3CSS.GLYPHICON_COLLAPSE_UP),
  COMMENT (CBootstrap3CSS.GLYPHICON_COMMENT),
  COMPRESSED (CBootstrap3CSS.GLYPHICON_COMPRESSED),
  COPYRIGHT_MARK (CBootstrap3CSS.GLYPHICON_COPYRIGHT_MARK),
  CREDIT_CARD (CBootstrap3CSS.GLYPHICON_CREDIT_CARD),
  CUTLERY (CBootstrap3CSS.GLYPHICON_CUTLERY),
  DASHBOARD (CBootstrap3CSS.GLYPHICON_DASHBOARD),
  DOWNLOAD (CBootstrap3CSS.GLYPHICON_DOWNLOAD),
  DOWNLOAD_ALT (CBootstrap3CSS.GLYPHICON_DOWNLOAD_ALT),
  EARPHONE (CBootstrap3CSS.GLYPHICON_EARPHONE),
  EDIT (CBootstrap3CSS.GLYPHICON_EDIT),
  EJECT (CBootstrap3CSS.GLYPHICON_EJECT),
  ENVELOPE (CBootstrap3CSS.GLYPHICON_ENVELOPE),
  EURO (CBootstrap3CSS.GLYPHICON_EURO),
  EXCLAMATION_SIGN (CBootstrap3CSS.GLYPHICON_EXCLAMATION_SIGN),
  EXPAND (CBootstrap3CSS.GLYPHICON_EXPAND),
  EXPORT (CBootstrap3CSS.GLYPHICON_EXPORT),
  EYE_CLOSE (CBootstrap3CSS.GLYPHICON_EYE_CLOSE),
  EYE_OPEN (CBootstrap3CSS.GLYPHICON_EYE_OPEN),
  FACETIME_VIDEO (CBootstrap3CSS.GLYPHICON_FACETIME_VIDEO),
  FAST_BACKWARD (CBootstrap3CSS.GLYPHICON_FAST_BACKWARD),
  FAST_FORWARD (CBootstrap3CSS.GLYPHICON_FAST_FORWARD),
  FILE (CBootstrap3CSS.GLYPHICON_FILE),
  FILM (CBootstrap3CSS.GLYPHICON_FILM),
  FILTER (CBootstrap3CSS.GLYPHICON_FILTER),
  FIRE (CBootstrap3CSS.GLYPHICON_FIRE),
  FLAG (CBootstrap3CSS.GLYPHICON_FLAG),
  FLASH (CBootstrap3CSS.GLYPHICON_FLASH),
  FLOPPY_DISK (CBootstrap3CSS.GLYPHICON_FLOPPY_DISK),
  FLOPPY_OPEN (CBootstrap3CSS.GLYPHICON_FLOPPY_OPEN),
  FLOPPY_REMOVE (CBootstrap3CSS.GLYPHICON_FLOPPY_REMOVE),
  FLOPPY_SAVE (CBootstrap3CSS.GLYPHICON_FLOPPY_SAVE),
  FLOPPY_SAVED (CBootstrap3CSS.GLYPHICON_FLOPPY_SAVED),
  FOLDER_CLOSE (CBootstrap3CSS.GLYPHICON_FOLDER_CLOSE),
  FOLDER_OPEN (CBootstrap3CSS.GLYPHICON_FOLDER_OPEN),
  FONT (CBootstrap3CSS.GLYPHICON_FONT),
  FORWARD (CBootstrap3CSS.GLYPHICON_FORWARD),
  FULLSCREEN (CBootstrap3CSS.GLYPHICON_FULLSCREEN),
  GBP (CBootstrap3CSS.GLYPHICON_GBP),
  GIFT (CBootstrap3CSS.GLYPHICON_GIFT),
  GLASS (CBootstrap3CSS.GLYPHICON_GLASS),
  GLOBE (CBootstrap3CSS.GLYPHICON_GLOBE),
  HAND_DOWN (CBootstrap3CSS.GLYPHICON_HAND_DOWN),
  HAND_LEFT (CBootstrap3CSS.GLYPHICON_HAND_LEFT),
  HAND_RIGHT (CBootstrap3CSS.GLYPHICON_HAND_RIGHT),
  HAND_UP (CBootstrap3CSS.GLYPHICON_HAND_UP),
  HD_VIDEO (CBootstrap3CSS.GLYPHICON_HD_VIDEO),
  HDD (CBootstrap3CSS.GLYPHICON_HDD),
  HEADER (CBootstrap3CSS.GLYPHICON_HEADER),
  HEADPHONES (CBootstrap3CSS.GLYPHICON_HEADPHONES),
  HEART (CBootstrap3CSS.GLYPHICON_HEART),
  HEART_EMPTY (CBootstrap3CSS.GLYPHICON_HEART_EMPTY),
  HOME (CBootstrap3CSS.GLYPHICON_HOME),
  IMPORT (CBootstrap3CSS.GLYPHICON_IMPORT),
  INBOX (CBootstrap3CSS.GLYPHICON_INBOX),
  INDENT_LEFT (CBootstrap3CSS.GLYPHICON_INDENT_LEFT),
  INDENT_RIGHT (CBootstrap3CSS.GLYPHICON_INDENT_RIGHT),
  INFO_SIGN (CBootstrap3CSS.GLYPHICON_INFO_SIGN),
  ITALIC (CBootstrap3CSS.GLYPHICON_ITALIC),
  LEAF (CBootstrap3CSS.GLYPHICON_LEAF),
  LINK (CBootstrap3CSS.GLYPHICON_LINK),
  LIST (CBootstrap3CSS.GLYPHICON_LIST),
  LIST_ALT (CBootstrap3CSS.GLYPHICON_LIST_ALT),
  LOCK (CBootstrap3CSS.GLYPHICON_LOCK),
  LOG_IN (CBootstrap3CSS.GLYPHICON_LOG_IN),
  LOG_OUT (CBootstrap3CSS.GLYPHICON_LOG_OUT),
  MAGNET (CBootstrap3CSS.GLYPHICON_MAGNET),
  MAP_MARKER (CBootstrap3CSS.GLYPHICON_MAP_MARKER),
  MINUS (CBootstrap3CSS.GLYPHICON_MINUS),
  MINUS_SIGN (CBootstrap3CSS.GLYPHICON_MINUS_SIGN),
  MOVE (CBootstrap3CSS.GLYPHICON_MOVE),
  MUSIC (CBootstrap3CSS.GLYPHICON_MUSIC),
  NEW_WINDOW (CBootstrap3CSS.GLYPHICON_NEW_WINDOW),
  OFF (CBootstrap3CSS.GLYPHICON_OFF),
  OK (CBootstrap3CSS.GLYPHICON_OK),
  OK_CIRCLE (CBootstrap3CSS.GLYPHICON_OK_CIRCLE),
  OK_SIGN (CBootstrap3CSS.GLYPHICON_OK_SIGN),
  OPEN (CBootstrap3CSS.GLYPHICON_OPEN),
  PAPERCLIP (CBootstrap3CSS.GLYPHICON_PAPERCLIP),
  PAUSE (CBootstrap3CSS.GLYPHICON_PAUSE),
  PENCIL (CBootstrap3CSS.GLYPHICON_PENCIL),
  PHONE (CBootstrap3CSS.GLYPHICON_PHONE),
  PHONE_ALT (CBootstrap3CSS.GLYPHICON_PHONE_ALT),
  PICTURE (CBootstrap3CSS.GLYPHICON_PICTURE),
  PLANE (CBootstrap3CSS.GLYPHICON_PLANE),
  PLAY (CBootstrap3CSS.GLYPHICON_PLAY),
  PLAY_CIRCLE (CBootstrap3CSS.GLYPHICON_PLAY_CIRCLE),
  PLUS (CBootstrap3CSS.GLYPHICON_PLUS),
  PLUS_SIGN (CBootstrap3CSS.GLYPHICON_PLUS_SIGN),
  PRINT (CBootstrap3CSS.GLYPHICON_PRINT),
  PUSHPIN (CBootstrap3CSS.GLYPHICON_PUSHPIN),
  QRCODE (CBootstrap3CSS.GLYPHICON_QRCODE),
  QUESTION_SIGN (CBootstrap3CSS.GLYPHICON_QUESTION_SIGN),
  RANDOM (CBootstrap3CSS.GLYPHICON_RANDOM),
  RECORD (CBootstrap3CSS.GLYPHICON_RECORD),
  REFRESH (CBootstrap3CSS.GLYPHICON_REFRESH),
  REGISTRATION_MARK (CBootstrap3CSS.GLYPHICON_REGISTRATION_MARK),
  REMOVE (CBootstrap3CSS.GLYPHICON_REMOVE),
  REMOVE_CIRCLE (CBootstrap3CSS.GLYPHICON_REMOVE_CIRCLE),
  REMOVE_SIGN (CBootstrap3CSS.GLYPHICON_REMOVE_SIGN),
  REPEAT (CBootstrap3CSS.GLYPHICON_REPEAT),
  RESIZE_FULL (CBootstrap3CSS.GLYPHICON_RESIZE_FULL),
  RESIZE_HORIZONTAL (CBootstrap3CSS.GLYPHICON_RESIZE_HORIZONTAL),
  RESIZE_SMALL (CBootstrap3CSS.GLYPHICON_RESIZE_SMALL),
  RESIZE_VERTICAL (CBootstrap3CSS.GLYPHICON_RESIZE_VERTICAL),
  RETWEET (CBootstrap3CSS.GLYPHICON_RETWEET),
  ROAD (CBootstrap3CSS.GLYPHICON_ROAD),
  SAVE (CBootstrap3CSS.GLYPHICON_SAVE),
  SAVED (CBootstrap3CSS.GLYPHICON_SAVED),
  SCREENSHOT (CBootstrap3CSS.GLYPHICON_SCREENSHOT),
  SD_VIDEO (CBootstrap3CSS.GLYPHICON_SD_VIDEO),
  SEARCH (CBootstrap3CSS.GLYPHICON_SEARCH),
  SEND (CBootstrap3CSS.GLYPHICON_SEND),
  SHARE (CBootstrap3CSS.GLYPHICON_SHARE),
  SHARE_ALT (CBootstrap3CSS.GLYPHICON_SHARE_ALT),
  SHOPPING_CART (CBootstrap3CSS.GLYPHICON_SHOPPING_CART),
  SIGNAL (CBootstrap3CSS.GLYPHICON_SIGNAL),
  SORT (CBootstrap3CSS.GLYPHICON_SORT),
  SORT_BY_ALPHABET (CBootstrap3CSS.GLYPHICON_SORT_BY_ALPHABET),
  SORT_BY_ALPHABET_ALT (CBootstrap3CSS.GLYPHICON_SORT_BY_ALPHABET_ALT),
  SORT_BY_ATTRIBUTES (CBootstrap3CSS.GLYPHICON_SORT_BY_ATTRIBUTES),
  SORT_BY_ATTRIBUTES_ALT (CBootstrap3CSS.GLYPHICON_SORT_BY_ATTRIBUTES_ALT),
  SORT_BY_ORDER (CBootstrap3CSS.GLYPHICON_SORT_BY_ORDER),
  SORT_BY_ORDER_ALT (CBootstrap3CSS.GLYPHICON_SORT_BY_ORDER_ALT),
  SOUND_5_1 (CBootstrap3CSS.GLYPHICON_SOUND_5_1),
  SOUND_6_1 (CBootstrap3CSS.GLYPHICON_SOUND_6_1),
  SOUND_7_1 (CBootstrap3CSS.GLYPHICON_SOUND_7_1),
  SOUND_DOLBY (CBootstrap3CSS.GLYPHICON_SOUND_DOLBY),
  SOUND_STEREO (CBootstrap3CSS.GLYPHICON_SOUND_STEREO),
  STAR (CBootstrap3CSS.GLYPHICON_STAR),
  STAR_EMPTY (CBootstrap3CSS.GLYPHICON_STAR_EMPTY),
  STATS (CBootstrap3CSS.GLYPHICON_STATS),
  STEP_BACKWARD (CBootstrap3CSS.GLYPHICON_STEP_BACKWARD),
  STEP_FORWARD (CBootstrap3CSS.GLYPHICON_STEP_FORWARD),
  STOP (CBootstrap3CSS.GLYPHICON_STOP),
  SUBTITLES (CBootstrap3CSS.GLYPHICON_SUBTITLES),
  TAG (CBootstrap3CSS.GLYPHICON_TAG),
  TAGS (CBootstrap3CSS.GLYPHICON_TAGS),
  TASKS (CBootstrap3CSS.GLYPHICON_TASKS),
  TEXT_HEIGHT (CBootstrap3CSS.GLYPHICON_TEXT_HEIGHT),
  TEXT_WIDTH (CBootstrap3CSS.GLYPHICON_TEXT_WIDTH),
  TH (CBootstrap3CSS.GLYPHICON_TH),
  TH_LARGE (CBootstrap3CSS.GLYPHICON_TH_LARGE),
  TH_LIST (CBootstrap3CSS.GLYPHICON_TH_LIST),
  THUMBS_DOWN (CBootstrap3CSS.GLYPHICON_THUMBS_DOWN),
  THUMBS_UP (CBootstrap3CSS.GLYPHICON_THUMBS_UP),
  TIME (CBootstrap3CSS.GLYPHICON_TIME),
  TINT (CBootstrap3CSS.GLYPHICON_TINT),
  TOWER (CBootstrap3CSS.GLYPHICON_TOWER),
  TRANSFER (CBootstrap3CSS.GLYPHICON_TRANSFER),
  TRASH (CBootstrap3CSS.GLYPHICON_TRASH),
  TREE_CONIFER (CBootstrap3CSS.GLYPHICON_TREE_CONIFER),
  TREE_DECIDUOUS (CBootstrap3CSS.GLYPHICON_TREE_DECIDUOUS),
  UNCHECKED (CBootstrap3CSS.GLYPHICON_UNCHECKED),
  UPLOAD (CBootstrap3CSS.GLYPHICON_UPLOAD),
  USD (CBootstrap3CSS.GLYPHICON_USD),
  USER (CBootstrap3CSS.GLYPHICON_USER),
  VOLUME_DOWN (CBootstrap3CSS.GLYPHICON_VOLUME_DOWN),
  VOLUME_OFF (CBootstrap3CSS.GLYPHICON_VOLUME_OFF),
  VOLUME_UP (CBootstrap3CSS.GLYPHICON_VOLUME_UP),
  WARNING_SIGN (CBootstrap3CSS.GLYPHICON_WARNING_SIGN),
  WRENCH (CBootstrap3CSS.GLYPHICON_WRENCH),
  ZOOM_IN (CBootstrap3CSS.GLYPHICON_ZOOM_IN),
  ZOOM_OUT (CBootstrap3CSS.GLYPHICON_ZOOM_OUT);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrap3Icon (@Nonnull final ICSSClassProvider aCSSClass)
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
    return new HCSpan ().addClasses (CBootstrap3CSS.GLYPHICON, m_aCSSClass);
  }

  @Nonnull
  public <T extends IHCElement <?>> T applyToNode (@Nonnull final T aElement)
  {
    aElement.addClasses (CBootstrap3CSS.GLYPHICON, this);
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
    DefaultIcons.set (EDefaultIcon.KEY, LOCK);
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
}
