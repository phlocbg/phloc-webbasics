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

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Since;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;

// ESCA-JAVA0116:
/**
 * CSS Constants for the Twitter Bootstrap framework
 * 
 * @author Philip Helger
 */
@Immutable
public final class CBootstrapCSS
{
  // Note: all CSS classes were created via MainExtractBootstrapCSSClasses
  public static final ICSSClassProvider ACCORDION = DefaultCSSClassProvider.create ("accordion");
  public static final ICSSClassProvider ACCORDION_GROUP = DefaultCSSClassProvider.create ("accordion-group");
  public static final ICSSClassProvider ACCORDION_HEADING = DefaultCSSClassProvider.create ("accordion-heading");
  public static final ICSSClassProvider ACCORDION_INNER = DefaultCSSClassProvider.create ("accordion-inner");
  public static final ICSSClassProvider ACCORDION_TOGGLE = DefaultCSSClassProvider.create ("accordion-toggle");
  public static final ICSSClassProvider ACTIVE = DefaultCSSClassProvider.create ("active");
  public static final ICSSClassProvider ADD_ON = DefaultCSSClassProvider.create ("add-on");
  public static final ICSSClassProvider AFFIX = DefaultCSSClassProvider.create ("affix");
  public static final ICSSClassProvider ALERT = DefaultCSSClassProvider.create ("alert");
  public static final ICSSClassProvider ALERT_BLOCK = DefaultCSSClassProvider.create ("alert-block");
  public static final ICSSClassProvider ALERT_DANGER = DefaultCSSClassProvider.create ("alert-danger");
  public static final ICSSClassProvider ALERT_ERROR = DefaultCSSClassProvider.create ("alert-error");
  public static final ICSSClassProvider ALERT_INFO = DefaultCSSClassProvider.create ("alert-info");
  public static final ICSSClassProvider ALERT_SUCCESS = DefaultCSSClassProvider.create ("alert-success");
  public static final ICSSClassProvider ARROW = DefaultCSSClassProvider.create ("arrow");
  public static final ICSSClassProvider BADGE = DefaultCSSClassProvider.create ("badge");
  public static final ICSSClassProvider BADGE_IMPORTANT = DefaultCSSClassProvider.create ("badge-important");
  public static final ICSSClassProvider BADGE_INFO = DefaultCSSClassProvider.create ("badge-info");
  public static final ICSSClassProvider BADGE_INVERSE = DefaultCSSClassProvider.create ("badge-inverse");
  public static final ICSSClassProvider BADGE_SUCCESS = DefaultCSSClassProvider.create ("badge-success");
  public static final ICSSClassProvider BADGE_WARNING = DefaultCSSClassProvider.create ("badge-warning");
  public static final ICSSClassProvider BAR = DefaultCSSClassProvider.create ("bar");
  public static final ICSSClassProvider BAR_DANGER = DefaultCSSClassProvider.create ("bar-danger");
  public static final ICSSClassProvider BAR_INFO = DefaultCSSClassProvider.create ("bar-info");
  public static final ICSSClassProvider BAR_SUCCESS = DefaultCSSClassProvider.create ("bar-success");
  public static final ICSSClassProvider BAR_WARNING = DefaultCSSClassProvider.create ("bar-warning");
  public static final ICSSClassProvider BOTTOM = DefaultCSSClassProvider.create ("bottom");
  public static final ICSSClassProvider BRAND = DefaultCSSClassProvider.create ("brand");
  public static final ICSSClassProvider BREADCRUMB = DefaultCSSClassProvider.create ("breadcrumb");
  public static final ICSSClassProvider BTN = DefaultCSSClassProvider.create ("btn");
  public static final ICSSClassProvider BTN_BLOCK = DefaultCSSClassProvider.create ("btn-block");
  public static final ICSSClassProvider BTN_DANGER = DefaultCSSClassProvider.create ("btn-danger");
  public static final ICSSClassProvider BTN_GROUP = DefaultCSSClassProvider.create ("btn-group");
  public static final ICSSClassProvider BTN_GROUP_VERTICAL = DefaultCSSClassProvider.create ("btn-group-vertical");
  public static final ICSSClassProvider BTN_INFO = DefaultCSSClassProvider.create ("btn-info");
  public static final ICSSClassProvider BTN_INVERSE = DefaultCSSClassProvider.create ("btn-inverse");
  public static final ICSSClassProvider BTN_LARGE = DefaultCSSClassProvider.create ("btn-large");
  public static final ICSSClassProvider BTN_LINK = DefaultCSSClassProvider.create ("btn-link");
  public static final ICSSClassProvider BTN_MINI = DefaultCSSClassProvider.create ("btn-mini");
  public static final ICSSClassProvider BTN_NAVBAR = DefaultCSSClassProvider.create ("btn-navbar");
  public static final ICSSClassProvider BTN_PRIMARY = DefaultCSSClassProvider.create ("btn-primary");
  public static final ICSSClassProvider BTN_SMALL = DefaultCSSClassProvider.create ("btn-small");
  public static final ICSSClassProvider BTN_SUCCESS = DefaultCSSClassProvider.create ("btn-success");
  public static final ICSSClassProvider BTN_TOOLBAR = DefaultCSSClassProvider.create ("btn-toolbar");
  public static final ICSSClassProvider BTN_WARNING = DefaultCSSClassProvider.create ("btn-warning");
  public static final ICSSClassProvider CAPTION = DefaultCSSClassProvider.create ("caption");
  public static final ICSSClassProvider CARET = DefaultCSSClassProvider.create ("caret");
  public static final ICSSClassProvider CAROUSEL = DefaultCSSClassProvider.create ("carousel");
  public static final ICSSClassProvider CAROUSEL_CAPTION = DefaultCSSClassProvider.create ("carousel-caption");
  public static final ICSSClassProvider CAROUSEL_CONTROL = DefaultCSSClassProvider.create ("carousel-control");
  @Since ("2.3.1")
  public static final ICSSClassProvider CAROUSEL_INDICATORS = DefaultCSSClassProvider.create ("carousel-indicators");
  public static final ICSSClassProvider CAROUSEL_INNER = DefaultCSSClassProvider.create ("carousel-inner");
  public static final ICSSClassProvider CHECKBOX = DefaultCSSClassProvider.create ("checkbox");
  public static final ICSSClassProvider CLEARFIX = DefaultCSSClassProvider.create ("clearfix");
  public static final ICSSClassProvider CLOSE = DefaultCSSClassProvider.create ("close");
  public static final ICSSClassProvider COLLAPSE = DefaultCSSClassProvider.create ("collapse");
  public static final ICSSClassProvider CONTAINER = DefaultCSSClassProvider.create ("container");
  public static final ICSSClassProvider CONTAINER_FLUID = DefaultCSSClassProvider.create ("container-fluid");
  public static final ICSSClassProvider CONTROL_GROUP = DefaultCSSClassProvider.create ("control-group");
  public static final ICSSClassProvider CONTROL_LABEL = DefaultCSSClassProvider.create ("control-label");
  public static final ICSSClassProvider CONTROLS = DefaultCSSClassProvider.create ("controls");
  public static final ICSSClassProvider CONTROLS_ROW = DefaultCSSClassProvider.create ("controls-row");
  public static final ICSSClassProvider DISABLED = DefaultCSSClassProvider.create ("disabled");
  public static final ICSSClassProvider DIVIDER = DefaultCSSClassProvider.create ("divider");
  public static final ICSSClassProvider DIVIDER_VERTICAL = DefaultCSSClassProvider.create ("divider-vertical");
  public static final ICSSClassProvider DL_HORIZONTAL = DefaultCSSClassProvider.create ("dl-horizontal");
  public static final ICSSClassProvider DROPDOWN = DefaultCSSClassProvider.create ("dropdown");
  public static final ICSSClassProvider DROPDOWN_MENU = DefaultCSSClassProvider.create ("dropdown-menu");
  public static final ICSSClassProvider DROPDOWN_SUBMENU = DefaultCSSClassProvider.create ("dropdown-submenu");
  public static final ICSSClassProvider DROPDOWN_TOGGLE = DefaultCSSClassProvider.create ("dropdown-toggle");
  public static final ICSSClassProvider DROPUP = DefaultCSSClassProvider.create ("dropup");
  public static final ICSSClassProvider ERROR = DefaultCSSClassProvider.create ("error");
  public static final ICSSClassProvider FADE = DefaultCSSClassProvider.create ("fade");
  public static final ICSSClassProvider FOCUSED = DefaultCSSClassProvider.create ("focused");
  public static final ICSSClassProvider FORM_ACTIONS = DefaultCSSClassProvider.create ("form-actions");
  public static final ICSSClassProvider FORM_HORIZONTAL = DefaultCSSClassProvider.create ("form-horizontal");
  public static final ICSSClassProvider FORM_INLINE = DefaultCSSClassProvider.create ("form-inline");
  public static final ICSSClassProvider FORM_SEARCH = DefaultCSSClassProvider.create ("form-search");
  public static final ICSSClassProvider GOOGLE_MAPS = DefaultCSSClassProvider.create ("google-maps");
  public static final ICSSClassProvider HELP_BLOCK = DefaultCSSClassProvider.create ("help-block");
  public static final ICSSClassProvider HELP_INLINE = DefaultCSSClassProvider.create ("help-inline");
  public static final ICSSClassProvider HERO_UNIT = DefaultCSSClassProvider.create ("hero-unit");
  public static final ICSSClassProvider HIDE = DefaultCSSClassProvider.create ("hide");
  public static final ICSSClassProvider HIDE_TEXT = DefaultCSSClassProvider.create ("hide-text");
  public static final ICSSClassProvider ICON_ADJUST = DefaultCSSClassProvider.create ("icon-adjust");
  public static final ICSSClassProvider ICON_ALIGN_CENTER = DefaultCSSClassProvider.create ("icon-align-center");
  public static final ICSSClassProvider ICON_ALIGN_JUSTIFY = DefaultCSSClassProvider.create ("icon-align-justify");
  public static final ICSSClassProvider ICON_ALIGN_LEFT = DefaultCSSClassProvider.create ("icon-align-left");
  public static final ICSSClassProvider ICON_ALIGN_RIGHT = DefaultCSSClassProvider.create ("icon-align-right");
  public static final ICSSClassProvider ICON_ARROW_DOWN = DefaultCSSClassProvider.create ("icon-arrow-down");
  public static final ICSSClassProvider ICON_ARROW_LEFT = DefaultCSSClassProvider.create ("icon-arrow-left");
  public static final ICSSClassProvider ICON_ARROW_RIGHT = DefaultCSSClassProvider.create ("icon-arrow-right");
  public static final ICSSClassProvider ICON_ARROW_UP = DefaultCSSClassProvider.create ("icon-arrow-up");
  public static final ICSSClassProvider ICON_ASTERISK = DefaultCSSClassProvider.create ("icon-asterisk");
  public static final ICSSClassProvider ICON_BACKWARD = DefaultCSSClassProvider.create ("icon-backward");
  public static final ICSSClassProvider ICON_BAN_CIRCLE = DefaultCSSClassProvider.create ("icon-ban-circle");
  public static final ICSSClassProvider ICON_BAR = DefaultCSSClassProvider.create ("icon-bar");
  public static final ICSSClassProvider ICON_BARCODE = DefaultCSSClassProvider.create ("icon-barcode");
  public static final ICSSClassProvider ICON_BELL = DefaultCSSClassProvider.create ("icon-bell");
  public static final ICSSClassProvider ICON_BOLD = DefaultCSSClassProvider.create ("icon-bold");
  public static final ICSSClassProvider ICON_BOOK = DefaultCSSClassProvider.create ("icon-book");
  public static final ICSSClassProvider ICON_BOOKMARK = DefaultCSSClassProvider.create ("icon-bookmark");
  public static final ICSSClassProvider ICON_BRIEFCASE = DefaultCSSClassProvider.create ("icon-briefcase");
  public static final ICSSClassProvider ICON_BULLHORN = DefaultCSSClassProvider.create ("icon-bullhorn");
  public static final ICSSClassProvider ICON_CALENDAR = DefaultCSSClassProvider.create ("icon-calendar");
  public static final ICSSClassProvider ICON_CAMERA = DefaultCSSClassProvider.create ("icon-camera");
  public static final ICSSClassProvider ICON_CERTIFICATE = DefaultCSSClassProvider.create ("icon-certificate");
  public static final ICSSClassProvider ICON_CHECK = DefaultCSSClassProvider.create ("icon-check");
  public static final ICSSClassProvider ICON_CHEVRON_DOWN = DefaultCSSClassProvider.create ("icon-chevron-down");
  public static final ICSSClassProvider ICON_CHEVRON_LEFT = DefaultCSSClassProvider.create ("icon-chevron-left");
  public static final ICSSClassProvider ICON_CHEVRON_RIGHT = DefaultCSSClassProvider.create ("icon-chevron-right");
  public static final ICSSClassProvider ICON_CHEVRON_UP = DefaultCSSClassProvider.create ("icon-chevron-up");
  public static final ICSSClassProvider ICON_CIRCLE_ARROW_DOWN = DefaultCSSClassProvider.create ("icon-circle-arrow-down");
  public static final ICSSClassProvider ICON_CIRCLE_ARROW_LEFT = DefaultCSSClassProvider.create ("icon-circle-arrow-left");
  public static final ICSSClassProvider ICON_CIRCLE_ARROW_RIGHT = DefaultCSSClassProvider.create ("icon-circle-arrow-right");
  public static final ICSSClassProvider ICON_CIRCLE_ARROW_UP = DefaultCSSClassProvider.create ("icon-circle-arrow-up");
  public static final ICSSClassProvider ICON_COG = DefaultCSSClassProvider.create ("icon-cog");
  public static final ICSSClassProvider ICON_COMMENT = DefaultCSSClassProvider.create ("icon-comment");
  public static final ICSSClassProvider ICON_DOWNLOAD = DefaultCSSClassProvider.create ("icon-download");
  public static final ICSSClassProvider ICON_DOWNLOAD_ALT = DefaultCSSClassProvider.create ("icon-download-alt");
  public static final ICSSClassProvider ICON_EDIT = DefaultCSSClassProvider.create ("icon-edit");
  public static final ICSSClassProvider ICON_EJECT = DefaultCSSClassProvider.create ("icon-eject");
  public static final ICSSClassProvider ICON_ENVELOPE = DefaultCSSClassProvider.create ("icon-envelope");
  public static final ICSSClassProvider ICON_EXCLAMATION_SIGN = DefaultCSSClassProvider.create ("icon-exclamation-sign");
  public static final ICSSClassProvider ICON_EYE_CLOSE = DefaultCSSClassProvider.create ("icon-eye-close");
  public static final ICSSClassProvider ICON_EYE_OPEN = DefaultCSSClassProvider.create ("icon-eye-open");
  public static final ICSSClassProvider ICON_FACETIME_VIDEO = DefaultCSSClassProvider.create ("icon-facetime-video");
  public static final ICSSClassProvider ICON_FAST_BACKWARD = DefaultCSSClassProvider.create ("icon-fast-backward");
  public static final ICSSClassProvider ICON_FAST_FORWARD = DefaultCSSClassProvider.create ("icon-fast-forward");
  public static final ICSSClassProvider ICON_FILE = DefaultCSSClassProvider.create ("icon-file");
  public static final ICSSClassProvider ICON_FILM = DefaultCSSClassProvider.create ("icon-film");
  public static final ICSSClassProvider ICON_FILTER = DefaultCSSClassProvider.create ("icon-filter");
  public static final ICSSClassProvider ICON_FIRE = DefaultCSSClassProvider.create ("icon-fire");
  public static final ICSSClassProvider ICON_FLAG = DefaultCSSClassProvider.create ("icon-flag");
  public static final ICSSClassProvider ICON_FOLDER_CLOSE = DefaultCSSClassProvider.create ("icon-folder-close");
  public static final ICSSClassProvider ICON_FOLDER_OPEN = DefaultCSSClassProvider.create ("icon-folder-open");
  public static final ICSSClassProvider ICON_FONT = DefaultCSSClassProvider.create ("icon-font");
  public static final ICSSClassProvider ICON_FORWARD = DefaultCSSClassProvider.create ("icon-forward");
  public static final ICSSClassProvider ICON_FULLSCREEN = DefaultCSSClassProvider.create ("icon-fullscreen");
  public static final ICSSClassProvider ICON_GIFT = DefaultCSSClassProvider.create ("icon-gift");
  public static final ICSSClassProvider ICON_GLASS = DefaultCSSClassProvider.create ("icon-glass");
  public static final ICSSClassProvider ICON_GLOBE = DefaultCSSClassProvider.create ("icon-globe");
  public static final ICSSClassProvider ICON_HAND_DOWN = DefaultCSSClassProvider.create ("icon-hand-down");
  public static final ICSSClassProvider ICON_HAND_LEFT = DefaultCSSClassProvider.create ("icon-hand-left");
  public static final ICSSClassProvider ICON_HAND_RIGHT = DefaultCSSClassProvider.create ("icon-hand-right");
  public static final ICSSClassProvider ICON_HAND_UP = DefaultCSSClassProvider.create ("icon-hand-up");
  public static final ICSSClassProvider ICON_HDD = DefaultCSSClassProvider.create ("icon-hdd");
  public static final ICSSClassProvider ICON_HEADPHONES = DefaultCSSClassProvider.create ("icon-headphones");
  public static final ICSSClassProvider ICON_HEART = DefaultCSSClassProvider.create ("icon-heart");
  public static final ICSSClassProvider ICON_HOME = DefaultCSSClassProvider.create ("icon-home");
  public static final ICSSClassProvider ICON_INBOX = DefaultCSSClassProvider.create ("icon-inbox");
  public static final ICSSClassProvider ICON_INDENT_LEFT = DefaultCSSClassProvider.create ("icon-indent-left");
  public static final ICSSClassProvider ICON_INDENT_RIGHT = DefaultCSSClassProvider.create ("icon-indent-right");
  public static final ICSSClassProvider ICON_INFO_SIGN = DefaultCSSClassProvider.create ("icon-info-sign");
  public static final ICSSClassProvider ICON_ITALIC = DefaultCSSClassProvider.create ("icon-italic");
  public static final ICSSClassProvider ICON_LEAF = DefaultCSSClassProvider.create ("icon-leaf");
  public static final ICSSClassProvider ICON_LIST = DefaultCSSClassProvider.create ("icon-list");
  public static final ICSSClassProvider ICON_LIST_ALT = DefaultCSSClassProvider.create ("icon-list-alt");
  public static final ICSSClassProvider ICON_LOCK = DefaultCSSClassProvider.create ("icon-lock");
  public static final ICSSClassProvider ICON_MAGNET = DefaultCSSClassProvider.create ("icon-magnet");
  public static final ICSSClassProvider ICON_MAP_MARKER = DefaultCSSClassProvider.create ("icon-map-marker");
  public static final ICSSClassProvider ICON_MINUS = DefaultCSSClassProvider.create ("icon-minus");
  public static final ICSSClassProvider ICON_MINUS_SIGN = DefaultCSSClassProvider.create ("icon-minus-sign");
  public static final ICSSClassProvider ICON_MOVE = DefaultCSSClassProvider.create ("icon-move");
  public static final ICSSClassProvider ICON_MUSIC = DefaultCSSClassProvider.create ("icon-music");
  public static final ICSSClassProvider ICON_OFF = DefaultCSSClassProvider.create ("icon-off");
  public static final ICSSClassProvider ICON_OK = DefaultCSSClassProvider.create ("icon-ok");
  public static final ICSSClassProvider ICON_OK_CIRCLE = DefaultCSSClassProvider.create ("icon-ok-circle");
  public static final ICSSClassProvider ICON_OK_SIGN = DefaultCSSClassProvider.create ("icon-ok-sign");
  public static final ICSSClassProvider ICON_PAUSE = DefaultCSSClassProvider.create ("icon-pause");
  public static final ICSSClassProvider ICON_PENCIL = DefaultCSSClassProvider.create ("icon-pencil");
  public static final ICSSClassProvider ICON_PICTURE = DefaultCSSClassProvider.create ("icon-picture");
  public static final ICSSClassProvider ICON_PLANE = DefaultCSSClassProvider.create ("icon-plane");
  public static final ICSSClassProvider ICON_PLAY = DefaultCSSClassProvider.create ("icon-play");
  public static final ICSSClassProvider ICON_PLAY_CIRCLE = DefaultCSSClassProvider.create ("icon-play-circle");
  public static final ICSSClassProvider ICON_PLUS = DefaultCSSClassProvider.create ("icon-plus");
  public static final ICSSClassProvider ICON_PLUS_SIGN = DefaultCSSClassProvider.create ("icon-plus-sign");
  public static final ICSSClassProvider ICON_PRINT = DefaultCSSClassProvider.create ("icon-print");
  public static final ICSSClassProvider ICON_QRCODE = DefaultCSSClassProvider.create ("icon-qrcode");
  public static final ICSSClassProvider ICON_QUESTION_SIGN = DefaultCSSClassProvider.create ("icon-question-sign");
  public static final ICSSClassProvider ICON_RANDOM = DefaultCSSClassProvider.create ("icon-random");
  public static final ICSSClassProvider ICON_REFRESH = DefaultCSSClassProvider.create ("icon-refresh");
  public static final ICSSClassProvider ICON_REMOVE = DefaultCSSClassProvider.create ("icon-remove");
  public static final ICSSClassProvider ICON_REMOVE_CIRCLE = DefaultCSSClassProvider.create ("icon-remove-circle");
  public static final ICSSClassProvider ICON_REMOVE_SIGN = DefaultCSSClassProvider.create ("icon-remove-sign");
  public static final ICSSClassProvider ICON_REPEAT = DefaultCSSClassProvider.create ("icon-repeat");
  public static final ICSSClassProvider ICON_RESIZE_FULL = DefaultCSSClassProvider.create ("icon-resize-full");
  public static final ICSSClassProvider ICON_RESIZE_HORIZONTAL = DefaultCSSClassProvider.create ("icon-resize-horizontal");
  public static final ICSSClassProvider ICON_RESIZE_SMALL = DefaultCSSClassProvider.create ("icon-resize-small");
  public static final ICSSClassProvider ICON_RESIZE_VERTICAL = DefaultCSSClassProvider.create ("icon-resize-vertical");
  public static final ICSSClassProvider ICON_RETWEET = DefaultCSSClassProvider.create ("icon-retweet");
  public static final ICSSClassProvider ICON_ROAD = DefaultCSSClassProvider.create ("icon-road");
  public static final ICSSClassProvider ICON_SCREENSHOT = DefaultCSSClassProvider.create ("icon-screenshot");
  public static final ICSSClassProvider ICON_SEARCH = DefaultCSSClassProvider.create ("icon-search");
  public static final ICSSClassProvider ICON_SHARE = DefaultCSSClassProvider.create ("icon-share");
  public static final ICSSClassProvider ICON_SHARE_ALT = DefaultCSSClassProvider.create ("icon-share-alt");
  public static final ICSSClassProvider ICON_SHOPPING_CART = DefaultCSSClassProvider.create ("icon-shopping-cart");
  public static final ICSSClassProvider ICON_SIGNAL = DefaultCSSClassProvider.create ("icon-signal");
  public static final ICSSClassProvider ICON_STAR = DefaultCSSClassProvider.create ("icon-star");
  public static final ICSSClassProvider ICON_STAR_EMPTY = DefaultCSSClassProvider.create ("icon-star-empty");
  public static final ICSSClassProvider ICON_STEP_BACKWARD = DefaultCSSClassProvider.create ("icon-step-backward");
  public static final ICSSClassProvider ICON_STEP_FORWARD = DefaultCSSClassProvider.create ("icon-step-forward");
  public static final ICSSClassProvider ICON_STOP = DefaultCSSClassProvider.create ("icon-stop");
  public static final ICSSClassProvider ICON_TAG = DefaultCSSClassProvider.create ("icon-tag");
  public static final ICSSClassProvider ICON_TAGS = DefaultCSSClassProvider.create ("icon-tags");
  public static final ICSSClassProvider ICON_TASKS = DefaultCSSClassProvider.create ("icon-tasks");
  public static final ICSSClassProvider ICON_TEXT_HEIGHT = DefaultCSSClassProvider.create ("icon-text-height");
  public static final ICSSClassProvider ICON_TEXT_WIDTH = DefaultCSSClassProvider.create ("icon-text-width");
  public static final ICSSClassProvider ICON_TH = DefaultCSSClassProvider.create ("icon-th");
  public static final ICSSClassProvider ICON_TH_LARGE = DefaultCSSClassProvider.create ("icon-th-large");
  public static final ICSSClassProvider ICON_TH_LIST = DefaultCSSClassProvider.create ("icon-th-list");
  public static final ICSSClassProvider ICON_THUMBS_DOWN = DefaultCSSClassProvider.create ("icon-thumbs-down");
  public static final ICSSClassProvider ICON_THUMBS_UP = DefaultCSSClassProvider.create ("icon-thumbs-up");
  public static final ICSSClassProvider ICON_TIME = DefaultCSSClassProvider.create ("icon-time");
  public static final ICSSClassProvider ICON_TINT = DefaultCSSClassProvider.create ("icon-tint");
  public static final ICSSClassProvider ICON_TRASH = DefaultCSSClassProvider.create ("icon-trash");
  public static final ICSSClassProvider ICON_UPLOAD = DefaultCSSClassProvider.create ("icon-upload");
  public static final ICSSClassProvider ICON_USER = DefaultCSSClassProvider.create ("icon-user");
  public static final ICSSClassProvider ICON_VOLUME_DOWN = DefaultCSSClassProvider.create ("icon-volume-down");
  public static final ICSSClassProvider ICON_VOLUME_OFF = DefaultCSSClassProvider.create ("icon-volume-off");
  public static final ICSSClassProvider ICON_VOLUME_UP = DefaultCSSClassProvider.create ("icon-volume-up");
  public static final ICSSClassProvider ICON_WARNING_SIGN = DefaultCSSClassProvider.create ("icon-warning-sign");
  public static final ICSSClassProvider ICON_WHITE = DefaultCSSClassProvider.create ("icon-white");
  public static final ICSSClassProvider ICON_WRENCH = DefaultCSSClassProvider.create ("icon-wrench");
  public static final ICSSClassProvider ICON_ZOOM_IN = DefaultCSSClassProvider.create ("icon-zoom-in");
  public static final ICSSClassProvider ICON_ZOOM_OUT = DefaultCSSClassProvider.create ("icon-zoom-out");
  public static final ICSSClassProvider IMG_CIRCLE = DefaultCSSClassProvider.create ("img-circle");
  public static final ICSSClassProvider IMG_POLAROID = DefaultCSSClassProvider.create ("img-polaroid");
  public static final ICSSClassProvider IMG_ROUNDED = DefaultCSSClassProvider.create ("img-rounded");
  public static final ICSSClassProvider IN = DefaultCSSClassProvider.create ("in");
  public static final ICSSClassProvider INFO = DefaultCSSClassProvider.create ("info");
  public static final ICSSClassProvider INITIALISM = DefaultCSSClassProvider.create ("initialism");
  public static final ICSSClassProvider INLINE = DefaultCSSClassProvider.create ("inline");
  public static final ICSSClassProvider INPUT_APPEND = DefaultCSSClassProvider.create ("input-append");
  public static final ICSSClassProvider INPUT_BLOCK_LEVEL = DefaultCSSClassProvider.create ("input-block-level");
  public static final ICSSClassProvider INPUT_LARGE = DefaultCSSClassProvider.create ("input-large");
  public static final ICSSClassProvider INPUT_MEDIUM = DefaultCSSClassProvider.create ("input-medium");
  public static final ICSSClassProvider INPUT_MINI = DefaultCSSClassProvider.create ("input-mini");
  public static final ICSSClassProvider INPUT_PREPEND = DefaultCSSClassProvider.create ("input-prepend");
  public static final ICSSClassProvider INPUT_SMALL = DefaultCSSClassProvider.create ("input-small");
  public static final ICSSClassProvider INPUT_XLARGE = DefaultCSSClassProvider.create ("input-xlarge");
  public static final ICSSClassProvider INPUT_XXLARGE = DefaultCSSClassProvider.create ("input-xxlarge");
  public static final ICSSClassProvider INVISIBLE = DefaultCSSClassProvider.create ("invisible");
  public static final ICSSClassProvider IR = DefaultCSSClassProvider.create ("ir");
  public static final ICSSClassProvider ITEM = DefaultCSSClassProvider.create ("item");
  public static final ICSSClassProvider LABEL = DefaultCSSClassProvider.create ("label");
  public static final ICSSClassProvider LABEL_IMPORTANT = DefaultCSSClassProvider.create ("label-important");
  public static final ICSSClassProvider LABEL_INFO = DefaultCSSClassProvider.create ("label-info");
  public static final ICSSClassProvider LABEL_INVERSE = DefaultCSSClassProvider.create ("label-inverse");
  public static final ICSSClassProvider LABEL_SUCCESS = DefaultCSSClassProvider.create ("label-success");
  public static final ICSSClassProvider LABEL_WARNING = DefaultCSSClassProvider.create ("label-warning");
  public static final ICSSClassProvider LARGE = DefaultCSSClassProvider.create ("large");
  public static final ICSSClassProvider LEAD = DefaultCSSClassProvider.create ("lead");
  public static final ICSSClassProvider LEFT = DefaultCSSClassProvider.create ("left");
  public static final ICSSClassProvider MEDIA = DefaultCSSClassProvider.create ("media");
  public static final ICSSClassProvider MEDIA_BODY = DefaultCSSClassProvider.create ("media-body");
  public static final ICSSClassProvider MEDIA_HEADING = DefaultCSSClassProvider.create ("media-heading");
  public static final ICSSClassProvider MEDIA_LIST = DefaultCSSClassProvider.create ("media-list");
  public static final ICSSClassProvider MEDIA_OBJECT = DefaultCSSClassProvider.create ("media-object");
  public static final ICSSClassProvider MODAL = DefaultCSSClassProvider.create ("modal");
  public static final ICSSClassProvider MODAL_BACKDROP = DefaultCSSClassProvider.create ("modal-backdrop");
  public static final ICSSClassProvider MODAL_BODY = DefaultCSSClassProvider.create ("modal-body");
  public static final ICSSClassProvider MODAL_FOOTER = DefaultCSSClassProvider.create ("modal-footer");
  public static final ICSSClassProvider MODAL_FORM = DefaultCSSClassProvider.create ("modal-form");
  public static final ICSSClassProvider MODAL_HEADER = DefaultCSSClassProvider.create ("modal-header");
  public static final ICSSClassProvider MUTED = DefaultCSSClassProvider.create ("muted");
  public static final ICSSClassProvider NAV = DefaultCSSClassProvider.create ("nav");
  public static final ICSSClassProvider NAV_COLLAPSE = DefaultCSSClassProvider.create ("nav-collapse");
  public static final ICSSClassProvider NAV_HEADER = DefaultCSSClassProvider.create ("nav-header");
  public static final ICSSClassProvider NAV_LIST = DefaultCSSClassProvider.create ("nav-list");
  public static final ICSSClassProvider NAV_PILLS = DefaultCSSClassProvider.create ("nav-pills");
  public static final ICSSClassProvider NAV_STACKED = DefaultCSSClassProvider.create ("nav-stacked");
  public static final ICSSClassProvider NAV_TABS = DefaultCSSClassProvider.create ("nav-tabs");
  public static final ICSSClassProvider NAVBAR = DefaultCSSClassProvider.create ("navbar");
  public static final ICSSClassProvider NAVBAR_FIXED_BOTTOM = DefaultCSSClassProvider.create ("navbar-fixed-bottom");
  public static final ICSSClassProvider NAVBAR_FIXED_TOP = DefaultCSSClassProvider.create ("navbar-fixed-top");
  public static final ICSSClassProvider NAVBAR_FORM = DefaultCSSClassProvider.create ("navbar-form");
  public static final ICSSClassProvider NAVBAR_INNER = DefaultCSSClassProvider.create ("navbar-inner");
  public static final ICSSClassProvider NAVBAR_INVERSE = DefaultCSSClassProvider.create ("navbar-inverse");
  public static final ICSSClassProvider NAVBAR_LINK = DefaultCSSClassProvider.create ("navbar-link");
  public static final ICSSClassProvider NAVBAR_SEARCH = DefaultCSSClassProvider.create ("navbar-search");
  public static final ICSSClassProvider NAVBAR_STATIC_TOP = DefaultCSSClassProvider.create ("navbar-static-top");
  public static final ICSSClassProvider NAVBAR_TEXT = DefaultCSSClassProvider.create ("navbar-text");
  public static final ICSSClassProvider NEXT = DefaultCSSClassProvider.create ("next");
  public static final ICSSClassProvider OFFSET1 = DefaultCSSClassProvider.create ("offset1");
  public static final ICSSClassProvider OFFSET10 = DefaultCSSClassProvider.create ("offset10");
  public static final ICSSClassProvider OFFSET11 = DefaultCSSClassProvider.create ("offset11");
  public static final ICSSClassProvider OFFSET12 = DefaultCSSClassProvider.create ("offset12");
  public static final ICSSClassProvider OFFSET2 = DefaultCSSClassProvider.create ("offset2");
  public static final ICSSClassProvider OFFSET3 = DefaultCSSClassProvider.create ("offset3");
  public static final ICSSClassProvider OFFSET4 = DefaultCSSClassProvider.create ("offset4");
  public static final ICSSClassProvider OFFSET5 = DefaultCSSClassProvider.create ("offset5");
  public static final ICSSClassProvider OFFSET6 = DefaultCSSClassProvider.create ("offset6");
  public static final ICSSClassProvider OFFSET7 = DefaultCSSClassProvider.create ("offset7");
  public static final ICSSClassProvider OFFSET8 = DefaultCSSClassProvider.create ("offset8");
  public static final ICSSClassProvider OFFSET9 = DefaultCSSClassProvider.create ("offset9");
  public static final ICSSClassProvider OPEN = DefaultCSSClassProvider.create ("open");
  public static final ICSSClassProvider PAGE_HEADER = DefaultCSSClassProvider.create ("page-header");
  public static final ICSSClassProvider PAGER = DefaultCSSClassProvider.create ("pager");
  public static final ICSSClassProvider PAGINATION = DefaultCSSClassProvider.create ("pagination");
  public static final ICSSClassProvider PAGINATION_CENTERED = DefaultCSSClassProvider.create ("pagination-centered");
  public static final ICSSClassProvider PAGINATION_LARGE = DefaultCSSClassProvider.create ("pagination-large");
  public static final ICSSClassProvider PAGINATION_MINI = DefaultCSSClassProvider.create ("pagination-mini");
  public static final ICSSClassProvider PAGINATION_RIGHT = DefaultCSSClassProvider.create ("pagination-right");
  public static final ICSSClassProvider PAGINATION_SMALL = DefaultCSSClassProvider.create ("pagination-small");
  public static final ICSSClassProvider PILL_CONTENT = DefaultCSSClassProvider.create ("pill-content");
  public static final ICSSClassProvider PILL_PANE = DefaultCSSClassProvider.create ("pill-pane");
  public static final ICSSClassProvider POPOVER = DefaultCSSClassProvider.create ("popover");
  public static final ICSSClassProvider POPOVER_CONTENT = DefaultCSSClassProvider.create ("popover-content");
  public static final ICSSClassProvider POPOVER_TITLE = DefaultCSSClassProvider.create ("popover-title");
  public static final ICSSClassProvider PRE_SCROLLABLE = DefaultCSSClassProvider.create ("pre-scrollable");
  public static final ICSSClassProvider PRETTYPRINT = DefaultCSSClassProvider.create ("prettyprint");
  public static final ICSSClassProvider PREV = DefaultCSSClassProvider.create ("prev");
  public static final ICSSClassProvider PREVIOUS = DefaultCSSClassProvider.create ("previous");
  public static final ICSSClassProvider PROGRESS = DefaultCSSClassProvider.create ("progress");
  public static final ICSSClassProvider PROGRESS_DANGER = DefaultCSSClassProvider.create ("progress-danger");
  public static final ICSSClassProvider PROGRESS_INFO = DefaultCSSClassProvider.create ("progress-info");
  public static final ICSSClassProvider PROGRESS_STRIPED = DefaultCSSClassProvider.create ("progress-striped");
  public static final ICSSClassProvider PROGRESS_SUCCESS = DefaultCSSClassProvider.create ("progress-success");
  public static final ICSSClassProvider PROGRESS_WARNING = DefaultCSSClassProvider.create ("progress-warning");
  public static final ICSSClassProvider PULL_LEFT = DefaultCSSClassProvider.create ("pull-left");
  public static final ICSSClassProvider PULL_RIGHT = DefaultCSSClassProvider.create ("pull-right");
  public static final ICSSClassProvider RADIO = DefaultCSSClassProvider.create ("radio");
  public static final ICSSClassProvider RIGHT = DefaultCSSClassProvider.create ("right");
  public static final ICSSClassProvider ROW = DefaultCSSClassProvider.create ("row");
  public static final ICSSClassProvider ROW_FLUID = DefaultCSSClassProvider.create ("row-fluid");
  public static final ICSSClassProvider SEARCH_QUERY = DefaultCSSClassProvider.create ("search-query");
  public static final ICSSClassProvider SHOW = DefaultCSSClassProvider.create ("show");
  public static final ICSSClassProvider SPAN1 = DefaultCSSClassProvider.create ("span1");
  public static final ICSSClassProvider SPAN10 = DefaultCSSClassProvider.create ("span10");
  public static final ICSSClassProvider SPAN11 = DefaultCSSClassProvider.create ("span11");
  public static final ICSSClassProvider SPAN12 = DefaultCSSClassProvider.create ("span12");
  public static final ICSSClassProvider SPAN2 = DefaultCSSClassProvider.create ("span2");
  public static final ICSSClassProvider SPAN3 = DefaultCSSClassProvider.create ("span3");
  public static final ICSSClassProvider SPAN4 = DefaultCSSClassProvider.create ("span4");
  public static final ICSSClassProvider SPAN5 = DefaultCSSClassProvider.create ("span5");
  public static final ICSSClassProvider SPAN6 = DefaultCSSClassProvider.create ("span6");
  public static final ICSSClassProvider SPAN7 = DefaultCSSClassProvider.create ("span7");
  public static final ICSSClassProvider SPAN8 = DefaultCSSClassProvider.create ("span8");
  public static final ICSSClassProvider SPAN9 = DefaultCSSClassProvider.create ("span9");
  public static final ICSSClassProvider SUCCESS = DefaultCSSClassProvider.create ("success");
  public static final ICSSClassProvider TAB_CONTENT = DefaultCSSClassProvider.create ("tab-content");
  public static final ICSSClassProvider TAB_PANE = DefaultCSSClassProvider.create ("tab-pane");
  public static final ICSSClassProvider TABBABLE = DefaultCSSClassProvider.create ("tabbable");
  public static final ICSSClassProvider TABLE = DefaultCSSClassProvider.create ("table");
  public static final ICSSClassProvider TABLE_BORDERED = DefaultCSSClassProvider.create ("table-bordered");
  public static final ICSSClassProvider TABLE_CONDENSED = DefaultCSSClassProvider.create ("table-condensed");
  public static final ICSSClassProvider TABLE_HOVER = DefaultCSSClassProvider.create ("table-hover");
  public static final ICSSClassProvider TABLE_STRIPED = DefaultCSSClassProvider.create ("table-striped");
  public static final ICSSClassProvider TABS_BELOW = DefaultCSSClassProvider.create ("tabs-below");
  public static final ICSSClassProvider TABS_LEFT = DefaultCSSClassProvider.create ("tabs-left");
  public static final ICSSClassProvider TABS_RIGHT = DefaultCSSClassProvider.create ("tabs-right");
  public static final ICSSClassProvider TABS_STACKED = DefaultCSSClassProvider.create ("tabs-stacked");
  @Since ("2.3.0")
  public static final ICSSClassProvider TEXT_CENTER = DefaultCSSClassProvider.create ("text-center");
  public static final ICSSClassProvider TEXT_ERROR = DefaultCSSClassProvider.create ("text-error");
  public static final ICSSClassProvider TEXT_INFO = DefaultCSSClassProvider.create ("text-info");
  @Since ("2.3.0")
  public static final ICSSClassProvider TEXT_LEFT = DefaultCSSClassProvider.create ("text-left");
  @Since ("2.3.0")
  public static final ICSSClassProvider TEXT_RIGHT = DefaultCSSClassProvider.create ("text-right");
  public static final ICSSClassProvider TEXT_SUCCESS = DefaultCSSClassProvider.create ("text-success");
  public static final ICSSClassProvider TEXT_WARNING = DefaultCSSClassProvider.create ("text-warning");
  public static final ICSSClassProvider THUMBNAIL = DefaultCSSClassProvider.create ("thumbnail");
  public static final ICSSClassProvider THUMBNAILS = DefaultCSSClassProvider.create ("thumbnails");
  public static final ICSSClassProvider TOOLTIP = DefaultCSSClassProvider.create ("tooltip");
  public static final ICSSClassProvider TOOLTIP_ARROW = DefaultCSSClassProvider.create ("tooltip-arrow");
  public static final ICSSClassProvider TOOLTIP_INNER = DefaultCSSClassProvider.create ("tooltip-inner");
  public static final ICSSClassProvider TOP = DefaultCSSClassProvider.create ("top");
  public static final ICSSClassProvider TYPEAHEAD = DefaultCSSClassProvider.create ("typeahead");
  public static final ICSSClassProvider UNEDITABLE_INPUT = DefaultCSSClassProvider.create ("uneditable-input");
  public static final ICSSClassProvider UNEDITABLE_TEXTAREA = DefaultCSSClassProvider.create ("uneditable-textarea");
  public static final ICSSClassProvider UNSTYLED = DefaultCSSClassProvider.create ("unstyled");
  public static final ICSSClassProvider WARNING = DefaultCSSClassProvider.create ("warning");
  public static final ICSSClassProvider WELL = DefaultCSSClassProvider.create ("well");
  public static final ICSSClassProvider WELL_LARGE = DefaultCSSClassProvider.create ("well-large");
  public static final ICSSClassProvider WELL_SMALL = DefaultCSSClassProvider.create ("well-small");

  private CBootstrapCSS ()
  {}

  @Nullable
  public static ICSSClassProvider getCSSClass (@Nullable final EErrorLevel eErrorLevel)
  {
    if (eErrorLevel == null)
      return null;

    if (eErrorLevel.isLessOrEqualSevereThan (EErrorLevel.SUCCESS))
      return SUCCESS;
    if (eErrorLevel.isLessOrEqualSevereThan (EErrorLevel.WARN))
      return WARNING;
    return ERROR;
  }
}
