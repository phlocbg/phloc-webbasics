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
package com.phloc.bootstrap3;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Since;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;

/**
 * CSS Constants for the Twitter Bootstrap3 framework
 * 
 * @author Philip Helger
 */
@Immutable
public final class CBootstrapCSS
{
  // Note: all CSS classes were created via MainExtractBootstrap3CSSClasses
  public static final ICSSClassProvider ACTIVE = DefaultCSSClassProvider.create ("active");
  public static final ICSSClassProvider AFFIX = DefaultCSSClassProvider.create ("affix");
  public static final ICSSClassProvider ALERT = DefaultCSSClassProvider.create ("alert");
  public static final ICSSClassProvider ALERT_DANGER = DefaultCSSClassProvider.create ("alert-danger");
  public static final ICSSClassProvider ALERT_DISMISSABLE = DefaultCSSClassProvider.create ("alert-dismissable");
  public static final ICSSClassProvider ALERT_INFO = DefaultCSSClassProvider.create ("alert-info");
  public static final ICSSClassProvider ALERT_LINK = DefaultCSSClassProvider.create ("alert-link");
  public static final ICSSClassProvider ALERT_SUCCESS = DefaultCSSClassProvider.create ("alert-success");
  public static final ICSSClassProvider ALERT_WARNING = DefaultCSSClassProvider.create ("alert-warning");
  public static final ICSSClassProvider ARROW = DefaultCSSClassProvider.create ("arrow");
  public static final ICSSClassProvider BADGE = DefaultCSSClassProvider.create ("badge");
  public static final ICSSClassProvider BOTTOM = DefaultCSSClassProvider.create ("bottom");
  public static final ICSSClassProvider BOTTOM_LEFT = DefaultCSSClassProvider.create ("bottom-left");
  public static final ICSSClassProvider BOTTOM_RIGHT = DefaultCSSClassProvider.create ("bottom-right");
  public static final ICSSClassProvider BREADCRUMB = DefaultCSSClassProvider.create ("breadcrumb");
  public static final ICSSClassProvider BTN = DefaultCSSClassProvider.create ("btn");
  public static final ICSSClassProvider BTN_BLOCK = DefaultCSSClassProvider.create ("btn-block");
  public static final ICSSClassProvider BTN_DANGER = DefaultCSSClassProvider.create ("btn-danger");
  public static final ICSSClassProvider BTN_DEFAULT = DefaultCSSClassProvider.create ("btn-default");
  public static final ICSSClassProvider BTN_GROUP = DefaultCSSClassProvider.create ("btn-group");
  public static final ICSSClassProvider BTN_GROUP_JUSTIFIED = DefaultCSSClassProvider.create ("btn-group-justified");
  public static final ICSSClassProvider BTN_GROUP_LG = DefaultCSSClassProvider.create ("btn-group-lg");
  public static final ICSSClassProvider BTN_GROUP_SM = DefaultCSSClassProvider.create ("btn-group-sm");
  public static final ICSSClassProvider BTN_GROUP_VERTICAL = DefaultCSSClassProvider.create ("btn-group-vertical");
  public static final ICSSClassProvider BTN_GROUP_XS = DefaultCSSClassProvider.create ("btn-group-xs");
  public static final ICSSClassProvider BTN_INFO = DefaultCSSClassProvider.create ("btn-info");
  public static final ICSSClassProvider BTN_LG = DefaultCSSClassProvider.create ("btn-lg");
  public static final ICSSClassProvider BTN_LINK = DefaultCSSClassProvider.create ("btn-link");
  public static final ICSSClassProvider BTN_PRIMARY = DefaultCSSClassProvider.create ("btn-primary");
  public static final ICSSClassProvider BTN_SM = DefaultCSSClassProvider.create ("btn-sm");
  public static final ICSSClassProvider BTN_SUCCESS = DefaultCSSClassProvider.create ("btn-success");
  public static final ICSSClassProvider BTN_TOOLBAR = DefaultCSSClassProvider.create ("btn-toolbar");
  public static final ICSSClassProvider BTN_WARNING = DefaultCSSClassProvider.create ("btn-warning");
  public static final ICSSClassProvider BTN_XS = DefaultCSSClassProvider.create ("btn-xs");
  public static final ICSSClassProvider CAPTION = DefaultCSSClassProvider.create ("caption");
  public static final ICSSClassProvider CARET = DefaultCSSClassProvider.create ("caret");
  public static final ICSSClassProvider CAROUSEL = DefaultCSSClassProvider.create ("carousel");
  public static final ICSSClassProvider CAROUSEL_CAPTION = DefaultCSSClassProvider.create ("carousel-caption");
  public static final ICSSClassProvider CAROUSEL_CONTROL = DefaultCSSClassProvider.create ("carousel-control");
  public static final ICSSClassProvider CAROUSEL_INDICATORS = DefaultCSSClassProvider.create ("carousel-indicators");
  public static final ICSSClassProvider CAROUSEL_INNER = DefaultCSSClassProvider.create ("carousel-inner");
  @Since ("3.0.1")
  public static final ICSSClassProvider CENTER_BLOCK = DefaultCSSClassProvider.create ("center-block");
  public static final ICSSClassProvider CHECKBOX = DefaultCSSClassProvider.create ("checkbox");
  public static final ICSSClassProvider CHECKBOX_INLINE = DefaultCSSClassProvider.create ("checkbox-inline");
  public static final ICSSClassProvider CLEARFIX = DefaultCSSClassProvider.create ("clearfix");
  public static final ICSSClassProvider CLOSE = DefaultCSSClassProvider.create ("close");
  public static final ICSSClassProvider COL = DefaultCSSClassProvider.create ("col");
  public static final ICSSClassProvider COL_LG_1 = DefaultCSSClassProvider.create ("col-lg-1");
  public static final ICSSClassProvider COL_LG_10 = DefaultCSSClassProvider.create ("col-lg-10");
  public static final ICSSClassProvider COL_LG_11 = DefaultCSSClassProvider.create ("col-lg-11");
  public static final ICSSClassProvider COL_LG_12 = DefaultCSSClassProvider.create ("col-lg-12");
  public static final ICSSClassProvider COL_LG_2 = DefaultCSSClassProvider.create ("col-lg-2");
  public static final ICSSClassProvider COL_LG_3 = DefaultCSSClassProvider.create ("col-lg-3");
  public static final ICSSClassProvider COL_LG_4 = DefaultCSSClassProvider.create ("col-lg-4");
  public static final ICSSClassProvider COL_LG_5 = DefaultCSSClassProvider.create ("col-lg-5");
  public static final ICSSClassProvider COL_LG_6 = DefaultCSSClassProvider.create ("col-lg-6");
  public static final ICSSClassProvider COL_LG_7 = DefaultCSSClassProvider.create ("col-lg-7");
  public static final ICSSClassProvider COL_LG_8 = DefaultCSSClassProvider.create ("col-lg-8");
  public static final ICSSClassProvider COL_LG_9 = DefaultCSSClassProvider.create ("col-lg-9");
  @Deprecated
  public static final ICSSClassProvider COL_LG_OFFSET_0 = DefaultCSSClassProvider.create ("col-lg-offset-0");
  public static final ICSSClassProvider COL_LG_OFFSET_1 = DefaultCSSClassProvider.create ("col-lg-offset-1");
  public static final ICSSClassProvider COL_LG_OFFSET_10 = DefaultCSSClassProvider.create ("col-lg-offset-10");
  public static final ICSSClassProvider COL_LG_OFFSET_11 = DefaultCSSClassProvider.create ("col-lg-offset-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_LG_OFFSET_12 = DefaultCSSClassProvider.create ("col-lg-offset-12");
  public static final ICSSClassProvider COL_LG_OFFSET_2 = DefaultCSSClassProvider.create ("col-lg-offset-2");
  public static final ICSSClassProvider COL_LG_OFFSET_3 = DefaultCSSClassProvider.create ("col-lg-offset-3");
  public static final ICSSClassProvider COL_LG_OFFSET_4 = DefaultCSSClassProvider.create ("col-lg-offset-4");
  public static final ICSSClassProvider COL_LG_OFFSET_5 = DefaultCSSClassProvider.create ("col-lg-offset-5");
  public static final ICSSClassProvider COL_LG_OFFSET_6 = DefaultCSSClassProvider.create ("col-lg-offset-6");
  public static final ICSSClassProvider COL_LG_OFFSET_7 = DefaultCSSClassProvider.create ("col-lg-offset-7");
  public static final ICSSClassProvider COL_LG_OFFSET_8 = DefaultCSSClassProvider.create ("col-lg-offset-8");
  public static final ICSSClassProvider COL_LG_OFFSET_9 = DefaultCSSClassProvider.create ("col-lg-offset-9");
  @Deprecated
  public static final ICSSClassProvider COL_LG_PULL_0 = DefaultCSSClassProvider.create ("col-lg-pull-0");
  public static final ICSSClassProvider COL_LG_PULL_1 = DefaultCSSClassProvider.create ("col-lg-pull-1");
  public static final ICSSClassProvider COL_LG_PULL_10 = DefaultCSSClassProvider.create ("col-lg-pull-10");
  public static final ICSSClassProvider COL_LG_PULL_11 = DefaultCSSClassProvider.create ("col-lg-pull-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_LG_PULL_12 = DefaultCSSClassProvider.create ("col-lg-pull-12");
  public static final ICSSClassProvider COL_LG_PULL_2 = DefaultCSSClassProvider.create ("col-lg-pull-2");
  public static final ICSSClassProvider COL_LG_PULL_3 = DefaultCSSClassProvider.create ("col-lg-pull-3");
  public static final ICSSClassProvider COL_LG_PULL_4 = DefaultCSSClassProvider.create ("col-lg-pull-4");
  public static final ICSSClassProvider COL_LG_PULL_5 = DefaultCSSClassProvider.create ("col-lg-pull-5");
  public static final ICSSClassProvider COL_LG_PULL_6 = DefaultCSSClassProvider.create ("col-lg-pull-6");
  public static final ICSSClassProvider COL_LG_PULL_7 = DefaultCSSClassProvider.create ("col-lg-pull-7");
  public static final ICSSClassProvider COL_LG_PULL_8 = DefaultCSSClassProvider.create ("col-lg-pull-8");
  public static final ICSSClassProvider COL_LG_PULL_9 = DefaultCSSClassProvider.create ("col-lg-pull-9");
  @Deprecated
  public static final ICSSClassProvider COL_LG_PUSH_0 = DefaultCSSClassProvider.create ("col-lg-push-0");
  public static final ICSSClassProvider COL_LG_PUSH_1 = DefaultCSSClassProvider.create ("col-lg-push-1");
  public static final ICSSClassProvider COL_LG_PUSH_10 = DefaultCSSClassProvider.create ("col-lg-push-10");
  public static final ICSSClassProvider COL_LG_PUSH_11 = DefaultCSSClassProvider.create ("col-lg-push-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_LG_PUSH_12 = DefaultCSSClassProvider.create ("col-lg-push-12");
  public static final ICSSClassProvider COL_LG_PUSH_2 = DefaultCSSClassProvider.create ("col-lg-push-2");
  public static final ICSSClassProvider COL_LG_PUSH_3 = DefaultCSSClassProvider.create ("col-lg-push-3");
  public static final ICSSClassProvider COL_LG_PUSH_4 = DefaultCSSClassProvider.create ("col-lg-push-4");
  public static final ICSSClassProvider COL_LG_PUSH_5 = DefaultCSSClassProvider.create ("col-lg-push-5");
  public static final ICSSClassProvider COL_LG_PUSH_6 = DefaultCSSClassProvider.create ("col-lg-push-6");
  public static final ICSSClassProvider COL_LG_PUSH_7 = DefaultCSSClassProvider.create ("col-lg-push-7");
  public static final ICSSClassProvider COL_LG_PUSH_8 = DefaultCSSClassProvider.create ("col-lg-push-8");
  public static final ICSSClassProvider COL_LG_PUSH_9 = DefaultCSSClassProvider.create ("col-lg-push-9");
  public static final ICSSClassProvider COL_MD_1 = DefaultCSSClassProvider.create ("col-md-1");
  public static final ICSSClassProvider COL_MD_10 = DefaultCSSClassProvider.create ("col-md-10");
  public static final ICSSClassProvider COL_MD_11 = DefaultCSSClassProvider.create ("col-md-11");
  public static final ICSSClassProvider COL_MD_12 = DefaultCSSClassProvider.create ("col-md-12");
  public static final ICSSClassProvider COL_MD_2 = DefaultCSSClassProvider.create ("col-md-2");
  public static final ICSSClassProvider COL_MD_3 = DefaultCSSClassProvider.create ("col-md-3");
  public static final ICSSClassProvider COL_MD_4 = DefaultCSSClassProvider.create ("col-md-4");
  public static final ICSSClassProvider COL_MD_5 = DefaultCSSClassProvider.create ("col-md-5");
  public static final ICSSClassProvider COL_MD_6 = DefaultCSSClassProvider.create ("col-md-6");
  public static final ICSSClassProvider COL_MD_7 = DefaultCSSClassProvider.create ("col-md-7");
  public static final ICSSClassProvider COL_MD_8 = DefaultCSSClassProvider.create ("col-md-8");
  public static final ICSSClassProvider COL_MD_9 = DefaultCSSClassProvider.create ("col-md-9");
  @Deprecated
  public static final ICSSClassProvider COL_MD_OFFSET_0 = DefaultCSSClassProvider.create ("col-md-offset-0");
  public static final ICSSClassProvider COL_MD_OFFSET_1 = DefaultCSSClassProvider.create ("col-md-offset-1");
  public static final ICSSClassProvider COL_MD_OFFSET_10 = DefaultCSSClassProvider.create ("col-md-offset-10");
  public static final ICSSClassProvider COL_MD_OFFSET_11 = DefaultCSSClassProvider.create ("col-md-offset-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_MD_OFFSET_12 = DefaultCSSClassProvider.create ("col-md-offset-12");
  public static final ICSSClassProvider COL_MD_OFFSET_2 = DefaultCSSClassProvider.create ("col-md-offset-2");
  public static final ICSSClassProvider COL_MD_OFFSET_3 = DefaultCSSClassProvider.create ("col-md-offset-3");
  public static final ICSSClassProvider COL_MD_OFFSET_4 = DefaultCSSClassProvider.create ("col-md-offset-4");
  public static final ICSSClassProvider COL_MD_OFFSET_5 = DefaultCSSClassProvider.create ("col-md-offset-5");
  public static final ICSSClassProvider COL_MD_OFFSET_6 = DefaultCSSClassProvider.create ("col-md-offset-6");
  public static final ICSSClassProvider COL_MD_OFFSET_7 = DefaultCSSClassProvider.create ("col-md-offset-7");
  public static final ICSSClassProvider COL_MD_OFFSET_8 = DefaultCSSClassProvider.create ("col-md-offset-8");
  public static final ICSSClassProvider COL_MD_OFFSET_9 = DefaultCSSClassProvider.create ("col-md-offset-9");
  @Deprecated
  public static final ICSSClassProvider COL_MD_PULL_0 = DefaultCSSClassProvider.create ("col-md-pull-0");
  public static final ICSSClassProvider COL_MD_PULL_1 = DefaultCSSClassProvider.create ("col-md-pull-1");
  public static final ICSSClassProvider COL_MD_PULL_10 = DefaultCSSClassProvider.create ("col-md-pull-10");
  public static final ICSSClassProvider COL_MD_PULL_11 = DefaultCSSClassProvider.create ("col-md-pull-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_MD_PULL_12 = DefaultCSSClassProvider.create ("col-md-pull-12");
  public static final ICSSClassProvider COL_MD_PULL_2 = DefaultCSSClassProvider.create ("col-md-pull-2");
  public static final ICSSClassProvider COL_MD_PULL_3 = DefaultCSSClassProvider.create ("col-md-pull-3");
  public static final ICSSClassProvider COL_MD_PULL_4 = DefaultCSSClassProvider.create ("col-md-pull-4");
  public static final ICSSClassProvider COL_MD_PULL_5 = DefaultCSSClassProvider.create ("col-md-pull-5");
  public static final ICSSClassProvider COL_MD_PULL_6 = DefaultCSSClassProvider.create ("col-md-pull-6");
  public static final ICSSClassProvider COL_MD_PULL_7 = DefaultCSSClassProvider.create ("col-md-pull-7");
  public static final ICSSClassProvider COL_MD_PULL_8 = DefaultCSSClassProvider.create ("col-md-pull-8");
  public static final ICSSClassProvider COL_MD_PULL_9 = DefaultCSSClassProvider.create ("col-md-pull-9");
  @Deprecated
  public static final ICSSClassProvider COL_MD_PUSH_0 = DefaultCSSClassProvider.create ("col-md-push-0");
  public static final ICSSClassProvider COL_MD_PUSH_1 = DefaultCSSClassProvider.create ("col-md-push-1");
  public static final ICSSClassProvider COL_MD_PUSH_10 = DefaultCSSClassProvider.create ("col-md-push-10");
  public static final ICSSClassProvider COL_MD_PUSH_11 = DefaultCSSClassProvider.create ("col-md-push-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_MD_PUSH_12 = DefaultCSSClassProvider.create ("col-md-push-12");
  public static final ICSSClassProvider COL_MD_PUSH_2 = DefaultCSSClassProvider.create ("col-md-push-2");
  public static final ICSSClassProvider COL_MD_PUSH_3 = DefaultCSSClassProvider.create ("col-md-push-3");
  public static final ICSSClassProvider COL_MD_PUSH_4 = DefaultCSSClassProvider.create ("col-md-push-4");
  public static final ICSSClassProvider COL_MD_PUSH_5 = DefaultCSSClassProvider.create ("col-md-push-5");
  public static final ICSSClassProvider COL_MD_PUSH_6 = DefaultCSSClassProvider.create ("col-md-push-6");
  public static final ICSSClassProvider COL_MD_PUSH_7 = DefaultCSSClassProvider.create ("col-md-push-7");
  public static final ICSSClassProvider COL_MD_PUSH_8 = DefaultCSSClassProvider.create ("col-md-push-8");
  public static final ICSSClassProvider COL_MD_PUSH_9 = DefaultCSSClassProvider.create ("col-md-push-9");
  public static final ICSSClassProvider COL_SM_1 = DefaultCSSClassProvider.create ("col-sm-1");
  public static final ICSSClassProvider COL_SM_10 = DefaultCSSClassProvider.create ("col-sm-10");
  public static final ICSSClassProvider COL_SM_11 = DefaultCSSClassProvider.create ("col-sm-11");
  public static final ICSSClassProvider COL_SM_12 = DefaultCSSClassProvider.create ("col-sm-12");
  public static final ICSSClassProvider COL_SM_2 = DefaultCSSClassProvider.create ("col-sm-2");
  public static final ICSSClassProvider COL_SM_3 = DefaultCSSClassProvider.create ("col-sm-3");
  public static final ICSSClassProvider COL_SM_4 = DefaultCSSClassProvider.create ("col-sm-4");
  public static final ICSSClassProvider COL_SM_5 = DefaultCSSClassProvider.create ("col-sm-5");
  public static final ICSSClassProvider COL_SM_6 = DefaultCSSClassProvider.create ("col-sm-6");
  public static final ICSSClassProvider COL_SM_7 = DefaultCSSClassProvider.create ("col-sm-7");
  public static final ICSSClassProvider COL_SM_8 = DefaultCSSClassProvider.create ("col-sm-8");
  public static final ICSSClassProvider COL_SM_9 = DefaultCSSClassProvider.create ("col-sm-9");
  public static final ICSSClassProvider COL_SM_OFFSET_1 = DefaultCSSClassProvider.create ("col-sm-offset-1");
  public static final ICSSClassProvider COL_SM_OFFSET_10 = DefaultCSSClassProvider.create ("col-sm-offset-10");
  public static final ICSSClassProvider COL_SM_OFFSET_11 = DefaultCSSClassProvider.create ("col-sm-offset-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_SM_OFFSET_12 = DefaultCSSClassProvider.create ("col-sm-offset-12");
  public static final ICSSClassProvider COL_SM_OFFSET_2 = DefaultCSSClassProvider.create ("col-sm-offset-2");
  public static final ICSSClassProvider COL_SM_OFFSET_3 = DefaultCSSClassProvider.create ("col-sm-offset-3");
  public static final ICSSClassProvider COL_SM_OFFSET_4 = DefaultCSSClassProvider.create ("col-sm-offset-4");
  public static final ICSSClassProvider COL_SM_OFFSET_5 = DefaultCSSClassProvider.create ("col-sm-offset-5");
  public static final ICSSClassProvider COL_SM_OFFSET_6 = DefaultCSSClassProvider.create ("col-sm-offset-6");
  public static final ICSSClassProvider COL_SM_OFFSET_7 = DefaultCSSClassProvider.create ("col-sm-offset-7");
  public static final ICSSClassProvider COL_SM_OFFSET_8 = DefaultCSSClassProvider.create ("col-sm-offset-8");
  public static final ICSSClassProvider COL_SM_OFFSET_9 = DefaultCSSClassProvider.create ("col-sm-offset-9");
  public static final ICSSClassProvider COL_SM_PULL_1 = DefaultCSSClassProvider.create ("col-sm-pull-1");
  public static final ICSSClassProvider COL_SM_PULL_10 = DefaultCSSClassProvider.create ("col-sm-pull-10");
  public static final ICSSClassProvider COL_SM_PULL_11 = DefaultCSSClassProvider.create ("col-sm-pull-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_SM_PULL_12 = DefaultCSSClassProvider.create ("col-sm-pull-12");
  public static final ICSSClassProvider COL_SM_PULL_2 = DefaultCSSClassProvider.create ("col-sm-pull-2");
  public static final ICSSClassProvider COL_SM_PULL_3 = DefaultCSSClassProvider.create ("col-sm-pull-3");
  public static final ICSSClassProvider COL_SM_PULL_4 = DefaultCSSClassProvider.create ("col-sm-pull-4");
  public static final ICSSClassProvider COL_SM_PULL_5 = DefaultCSSClassProvider.create ("col-sm-pull-5");
  public static final ICSSClassProvider COL_SM_PULL_6 = DefaultCSSClassProvider.create ("col-sm-pull-6");
  public static final ICSSClassProvider COL_SM_PULL_7 = DefaultCSSClassProvider.create ("col-sm-pull-7");
  public static final ICSSClassProvider COL_SM_PULL_8 = DefaultCSSClassProvider.create ("col-sm-pull-8");
  public static final ICSSClassProvider COL_SM_PULL_9 = DefaultCSSClassProvider.create ("col-sm-pull-9");
  public static final ICSSClassProvider COL_SM_PUSH_1 = DefaultCSSClassProvider.create ("col-sm-push-1");
  public static final ICSSClassProvider COL_SM_PUSH_10 = DefaultCSSClassProvider.create ("col-sm-push-10");
  public static final ICSSClassProvider COL_SM_PUSH_11 = DefaultCSSClassProvider.create ("col-sm-push-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_SM_PUSH_12 = DefaultCSSClassProvider.create ("col-sm-push-12");
  public static final ICSSClassProvider COL_SM_PUSH_2 = DefaultCSSClassProvider.create ("col-sm-push-2");
  public static final ICSSClassProvider COL_SM_PUSH_3 = DefaultCSSClassProvider.create ("col-sm-push-3");
  public static final ICSSClassProvider COL_SM_PUSH_4 = DefaultCSSClassProvider.create ("col-sm-push-4");
  public static final ICSSClassProvider COL_SM_PUSH_5 = DefaultCSSClassProvider.create ("col-sm-push-5");
  public static final ICSSClassProvider COL_SM_PUSH_6 = DefaultCSSClassProvider.create ("col-sm-push-6");
  public static final ICSSClassProvider COL_SM_PUSH_7 = DefaultCSSClassProvider.create ("col-sm-push-7");
  public static final ICSSClassProvider COL_SM_PUSH_8 = DefaultCSSClassProvider.create ("col-sm-push-8");
  public static final ICSSClassProvider COL_SM_PUSH_9 = DefaultCSSClassProvider.create ("col-sm-push-9");
  public static final ICSSClassProvider COL_XS_1 = DefaultCSSClassProvider.create ("col-xs-1");
  public static final ICSSClassProvider COL_XS_10 = DefaultCSSClassProvider.create ("col-xs-10");
  public static final ICSSClassProvider COL_XS_11 = DefaultCSSClassProvider.create ("col-xs-11");
  public static final ICSSClassProvider COL_XS_12 = DefaultCSSClassProvider.create ("col-xs-12");
  public static final ICSSClassProvider COL_XS_2 = DefaultCSSClassProvider.create ("col-xs-2");
  public static final ICSSClassProvider COL_XS_3 = DefaultCSSClassProvider.create ("col-xs-3");
  public static final ICSSClassProvider COL_XS_4 = DefaultCSSClassProvider.create ("col-xs-4");
  public static final ICSSClassProvider COL_XS_5 = DefaultCSSClassProvider.create ("col-xs-5");
  public static final ICSSClassProvider COL_XS_6 = DefaultCSSClassProvider.create ("col-xs-6");
  public static final ICSSClassProvider COL_XS_7 = DefaultCSSClassProvider.create ("col-xs-7");
  public static final ICSSClassProvider COL_XS_8 = DefaultCSSClassProvider.create ("col-xs-8");
  public static final ICSSClassProvider COL_XS_9 = DefaultCSSClassProvider.create ("col-xs-9");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_1 = DefaultCSSClassProvider.create ("col-xs-offset-1");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_10 = DefaultCSSClassProvider.create ("col-xs-offset-10");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_11 = DefaultCSSClassProvider.create ("col-xs-offset-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_12 = DefaultCSSClassProvider.create ("col-xs-offset-12");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_2 = DefaultCSSClassProvider.create ("col-xs-offset-2");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_3 = DefaultCSSClassProvider.create ("col-xs-offset-3");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_4 = DefaultCSSClassProvider.create ("col-xs-offset-4");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_5 = DefaultCSSClassProvider.create ("col-xs-offset-5");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_6 = DefaultCSSClassProvider.create ("col-xs-offset-6");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_7 = DefaultCSSClassProvider.create ("col-xs-offset-7");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_8 = DefaultCSSClassProvider.create ("col-xs-offset-8");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_OFFSET_9 = DefaultCSSClassProvider.create ("col-xs-offset-9");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_1 = DefaultCSSClassProvider.create ("col-xs-pull-1");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_10 = DefaultCSSClassProvider.create ("col-xs-pull-10");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_11 = DefaultCSSClassProvider.create ("col-xs-pull-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_12 = DefaultCSSClassProvider.create ("col-xs-pull-12");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_2 = DefaultCSSClassProvider.create ("col-xs-pull-2");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_3 = DefaultCSSClassProvider.create ("col-xs-pull-3");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_4 = DefaultCSSClassProvider.create ("col-xs-pull-4");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_5 = DefaultCSSClassProvider.create ("col-xs-pull-5");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_6 = DefaultCSSClassProvider.create ("col-xs-pull-6");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_7 = DefaultCSSClassProvider.create ("col-xs-pull-7");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_8 = DefaultCSSClassProvider.create ("col-xs-pull-8");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PULL_9 = DefaultCSSClassProvider.create ("col-xs-pull-9");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_1 = DefaultCSSClassProvider.create ("col-xs-push-1");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_10 = DefaultCSSClassProvider.create ("col-xs-push-10");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_11 = DefaultCSSClassProvider.create ("col-xs-push-11");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_12 = DefaultCSSClassProvider.create ("col-xs-push-12");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_2 = DefaultCSSClassProvider.create ("col-xs-push-2");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_3 = DefaultCSSClassProvider.create ("col-xs-push-3");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_4 = DefaultCSSClassProvider.create ("col-xs-push-4");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_5 = DefaultCSSClassProvider.create ("col-xs-push-5");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_6 = DefaultCSSClassProvider.create ("col-xs-push-6");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_7 = DefaultCSSClassProvider.create ("col-xs-push-7");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_8 = DefaultCSSClassProvider.create ("col-xs-push-8");
  @Since ("3.0.1")
  public static final ICSSClassProvider COL_XS_PUSH_9 = DefaultCSSClassProvider.create ("col-xs-push-9");
  public static final ICSSClassProvider COLLAPSE = DefaultCSSClassProvider.create ("collapse");
  public static final ICSSClassProvider COLLAPSING = DefaultCSSClassProvider.create ("collapsing");
  public static final ICSSClassProvider CONTAINER = DefaultCSSClassProvider.create ("container");
  public static final ICSSClassProvider CONTROL_LABEL = DefaultCSSClassProvider.create ("control-label");
  public static final ICSSClassProvider DANGER = DefaultCSSClassProvider.create ("danger");
  public static final ICSSClassProvider DISABLED = DefaultCSSClassProvider.create ("disabled");
  public static final ICSSClassProvider DIVIDER = DefaultCSSClassProvider.create ("divider");
  public static final ICSSClassProvider DL_HORIZONTAL = DefaultCSSClassProvider.create ("dl-horizontal");
  public static final ICSSClassProvider DROPDOWN = DefaultCSSClassProvider.create ("dropdown");
  public static final ICSSClassProvider DROPDOWN_BACKDROP = DefaultCSSClassProvider.create ("dropdown-backdrop");
  public static final ICSSClassProvider DROPDOWN_HEADER = DefaultCSSClassProvider.create ("dropdown-header");
  public static final ICSSClassProvider DROPDOWN_MENU = DefaultCSSClassProvider.create ("dropdown-menu");
  public static final ICSSClassProvider DROPDOWN_TOGGLE = DefaultCSSClassProvider.create ("dropdown-toggle");
  public static final ICSSClassProvider DROPUP = DefaultCSSClassProvider.create ("dropup");
  public static final ICSSClassProvider FADE = DefaultCSSClassProvider.create ("fade");
  public static final ICSSClassProvider FORM_CONTROL = DefaultCSSClassProvider.create ("form-control");
  public static final ICSSClassProvider FORM_CONTROL_STATIC = DefaultCSSClassProvider.create ("form-control-static");
  public static final ICSSClassProvider FORM_GROUP = DefaultCSSClassProvider.create ("form-group");
  public static final ICSSClassProvider FORM_HORIZONTAL = DefaultCSSClassProvider.create ("form-horizontal");
  public static final ICSSClassProvider FORM_INLINE = DefaultCSSClassProvider.create ("form-inline");
  public static final ICSSClassProvider GLYPHICON = DefaultCSSClassProvider.create ("glyphicon");
  public static final ICSSClassProvider GLYPHICON_ADJUST = DefaultCSSClassProvider.create ("glyphicon-adjust");
  public static final ICSSClassProvider GLYPHICON_ALIGN_CENTER = DefaultCSSClassProvider.create ("glyphicon-align-center");
  public static final ICSSClassProvider GLYPHICON_ALIGN_JUSTIFY = DefaultCSSClassProvider.create ("glyphicon-align-justify");
  public static final ICSSClassProvider GLYPHICON_ALIGN_LEFT = DefaultCSSClassProvider.create ("glyphicon-align-left");
  public static final ICSSClassProvider GLYPHICON_ALIGN_RIGHT = DefaultCSSClassProvider.create ("glyphicon-align-right");
  public static final ICSSClassProvider GLYPHICON_ARROW_DOWN = DefaultCSSClassProvider.create ("glyphicon-arrow-down");
  public static final ICSSClassProvider GLYPHICON_ARROW_LEFT = DefaultCSSClassProvider.create ("glyphicon-arrow-left");
  public static final ICSSClassProvider GLYPHICON_ARROW_RIGHT = DefaultCSSClassProvider.create ("glyphicon-arrow-right");
  public static final ICSSClassProvider GLYPHICON_ARROW_UP = DefaultCSSClassProvider.create ("glyphicon-arrow-up");
  public static final ICSSClassProvider GLYPHICON_ASTERISK = DefaultCSSClassProvider.create ("glyphicon-asterisk");
  public static final ICSSClassProvider GLYPHICON_BACKWARD = DefaultCSSClassProvider.create ("glyphicon-backward");
  public static final ICSSClassProvider GLYPHICON_BAN_CIRCLE = DefaultCSSClassProvider.create ("glyphicon-ban-circle");
  public static final ICSSClassProvider GLYPHICON_BARCODE = DefaultCSSClassProvider.create ("glyphicon-barcode");
  public static final ICSSClassProvider GLYPHICON_BELL = DefaultCSSClassProvider.create ("glyphicon-bell");
  public static final ICSSClassProvider GLYPHICON_BOLD = DefaultCSSClassProvider.create ("glyphicon-bold");
  public static final ICSSClassProvider GLYPHICON_BOOK = DefaultCSSClassProvider.create ("glyphicon-book");
  public static final ICSSClassProvider GLYPHICON_BOOKMARK = DefaultCSSClassProvider.create ("glyphicon-bookmark");
  public static final ICSSClassProvider GLYPHICON_BRIEFCASE = DefaultCSSClassProvider.create ("glyphicon-briefcase");
  public static final ICSSClassProvider GLYPHICON_BULLHORN = DefaultCSSClassProvider.create ("glyphicon-bullhorn");
  public static final ICSSClassProvider GLYPHICON_CALENDAR = DefaultCSSClassProvider.create ("glyphicon-calendar");
  public static final ICSSClassProvider GLYPHICON_CAMERA = DefaultCSSClassProvider.create ("glyphicon-camera");
  public static final ICSSClassProvider GLYPHICON_CERTIFICATE = DefaultCSSClassProvider.create ("glyphicon-certificate");
  public static final ICSSClassProvider GLYPHICON_CHECK = DefaultCSSClassProvider.create ("glyphicon-check");
  public static final ICSSClassProvider GLYPHICON_CHEVRON_DOWN = DefaultCSSClassProvider.create ("glyphicon-chevron-down");
  public static final ICSSClassProvider GLYPHICON_CHEVRON_LEFT = DefaultCSSClassProvider.create ("glyphicon-chevron-left");
  public static final ICSSClassProvider GLYPHICON_CHEVRON_RIGHT = DefaultCSSClassProvider.create ("glyphicon-chevron-right");
  public static final ICSSClassProvider GLYPHICON_CHEVRON_UP = DefaultCSSClassProvider.create ("glyphicon-chevron-up");
  public static final ICSSClassProvider GLYPHICON_CIRCLE_ARROW_DOWN = DefaultCSSClassProvider.create ("glyphicon-circle-arrow-down");
  public static final ICSSClassProvider GLYPHICON_CIRCLE_ARROW_LEFT = DefaultCSSClassProvider.create ("glyphicon-circle-arrow-left");
  public static final ICSSClassProvider GLYPHICON_CIRCLE_ARROW_RIGHT = DefaultCSSClassProvider.create ("glyphicon-circle-arrow-right");
  public static final ICSSClassProvider GLYPHICON_CIRCLE_ARROW_UP = DefaultCSSClassProvider.create ("glyphicon-circle-arrow-up");
  public static final ICSSClassProvider GLYPHICON_CLOUD = DefaultCSSClassProvider.create ("glyphicon-cloud");
  public static final ICSSClassProvider GLYPHICON_CLOUD_DOWNLOAD = DefaultCSSClassProvider.create ("glyphicon-cloud-download");
  public static final ICSSClassProvider GLYPHICON_CLOUD_UPLOAD = DefaultCSSClassProvider.create ("glyphicon-cloud-upload");
  public static final ICSSClassProvider GLYPHICON_COG = DefaultCSSClassProvider.create ("glyphicon-cog");
  public static final ICSSClassProvider GLYPHICON_COLLAPSE_DOWN = DefaultCSSClassProvider.create ("glyphicon-collapse-down");
  public static final ICSSClassProvider GLYPHICON_COLLAPSE_UP = DefaultCSSClassProvider.create ("glyphicon-collapse-up");
  public static final ICSSClassProvider GLYPHICON_COMMENT = DefaultCSSClassProvider.create ("glyphicon-comment");
  public static final ICSSClassProvider GLYPHICON_COMPRESSED = DefaultCSSClassProvider.create ("glyphicon-compressed");
  public static final ICSSClassProvider GLYPHICON_COPYRIGHT_MARK = DefaultCSSClassProvider.create ("glyphicon-copyright-mark");
  public static final ICSSClassProvider GLYPHICON_CREDIT_CARD = DefaultCSSClassProvider.create ("glyphicon-credit-card");
  public static final ICSSClassProvider GLYPHICON_CUTLERY = DefaultCSSClassProvider.create ("glyphicon-cutlery");
  public static final ICSSClassProvider GLYPHICON_DASHBOARD = DefaultCSSClassProvider.create ("glyphicon-dashboard");
  public static final ICSSClassProvider GLYPHICON_DOWNLOAD = DefaultCSSClassProvider.create ("glyphicon-download");
  public static final ICSSClassProvider GLYPHICON_DOWNLOAD_ALT = DefaultCSSClassProvider.create ("glyphicon-download-alt");
  public static final ICSSClassProvider GLYPHICON_EARPHONE = DefaultCSSClassProvider.create ("glyphicon-earphone");
  public static final ICSSClassProvider GLYPHICON_EDIT = DefaultCSSClassProvider.create ("glyphicon-edit");
  public static final ICSSClassProvider GLYPHICON_EJECT = DefaultCSSClassProvider.create ("glyphicon-eject");
  public static final ICSSClassProvider GLYPHICON_ENVELOPE = DefaultCSSClassProvider.create ("glyphicon-envelope");
  public static final ICSSClassProvider GLYPHICON_EURO = DefaultCSSClassProvider.create ("glyphicon-euro");
  public static final ICSSClassProvider GLYPHICON_EXCLAMATION_SIGN = DefaultCSSClassProvider.create ("glyphicon-exclamation-sign");
  public static final ICSSClassProvider GLYPHICON_EXPAND = DefaultCSSClassProvider.create ("glyphicon-expand");
  public static final ICSSClassProvider GLYPHICON_EXPORT = DefaultCSSClassProvider.create ("glyphicon-export");
  public static final ICSSClassProvider GLYPHICON_EYE_CLOSE = DefaultCSSClassProvider.create ("glyphicon-eye-close");
  public static final ICSSClassProvider GLYPHICON_EYE_OPEN = DefaultCSSClassProvider.create ("glyphicon-eye-open");
  public static final ICSSClassProvider GLYPHICON_FACETIME_VIDEO = DefaultCSSClassProvider.create ("glyphicon-facetime-video");
  public static final ICSSClassProvider GLYPHICON_FAST_BACKWARD = DefaultCSSClassProvider.create ("glyphicon-fast-backward");
  public static final ICSSClassProvider GLYPHICON_FAST_FORWARD = DefaultCSSClassProvider.create ("glyphicon-fast-forward");
  public static final ICSSClassProvider GLYPHICON_FILE = DefaultCSSClassProvider.create ("glyphicon-file");
  public static final ICSSClassProvider GLYPHICON_FILM = DefaultCSSClassProvider.create ("glyphicon-film");
  public static final ICSSClassProvider GLYPHICON_FILTER = DefaultCSSClassProvider.create ("glyphicon-filter");
  public static final ICSSClassProvider GLYPHICON_FIRE = DefaultCSSClassProvider.create ("glyphicon-fire");
  public static final ICSSClassProvider GLYPHICON_FLAG = DefaultCSSClassProvider.create ("glyphicon-flag");
  public static final ICSSClassProvider GLYPHICON_FLASH = DefaultCSSClassProvider.create ("glyphicon-flash");
  public static final ICSSClassProvider GLYPHICON_FLOPPY_DISK = DefaultCSSClassProvider.create ("glyphicon-floppy-disk");
  public static final ICSSClassProvider GLYPHICON_FLOPPY_OPEN = DefaultCSSClassProvider.create ("glyphicon-floppy-open");
  public static final ICSSClassProvider GLYPHICON_FLOPPY_REMOVE = DefaultCSSClassProvider.create ("glyphicon-floppy-remove");
  public static final ICSSClassProvider GLYPHICON_FLOPPY_SAVE = DefaultCSSClassProvider.create ("glyphicon-floppy-save");
  public static final ICSSClassProvider GLYPHICON_FLOPPY_SAVED = DefaultCSSClassProvider.create ("glyphicon-floppy-saved");
  public static final ICSSClassProvider GLYPHICON_FOLDER_CLOSE = DefaultCSSClassProvider.create ("glyphicon-folder-close");
  public static final ICSSClassProvider GLYPHICON_FOLDER_OPEN = DefaultCSSClassProvider.create ("glyphicon-folder-open");
  public static final ICSSClassProvider GLYPHICON_FONT = DefaultCSSClassProvider.create ("glyphicon-font");
  public static final ICSSClassProvider GLYPHICON_FORWARD = DefaultCSSClassProvider.create ("glyphicon-forward");
  public static final ICSSClassProvider GLYPHICON_FULLSCREEN = DefaultCSSClassProvider.create ("glyphicon-fullscreen");
  public static final ICSSClassProvider GLYPHICON_GBP = DefaultCSSClassProvider.create ("glyphicon-gbp");
  public static final ICSSClassProvider GLYPHICON_GIFT = DefaultCSSClassProvider.create ("glyphicon-gift");
  public static final ICSSClassProvider GLYPHICON_GLASS = DefaultCSSClassProvider.create ("glyphicon-glass");
  public static final ICSSClassProvider GLYPHICON_GLOBE = DefaultCSSClassProvider.create ("glyphicon-globe");
  public static final ICSSClassProvider GLYPHICON_HAND_DOWN = DefaultCSSClassProvider.create ("glyphicon-hand-down");
  public static final ICSSClassProvider GLYPHICON_HAND_LEFT = DefaultCSSClassProvider.create ("glyphicon-hand-left");
  public static final ICSSClassProvider GLYPHICON_HAND_RIGHT = DefaultCSSClassProvider.create ("glyphicon-hand-right");
  public static final ICSSClassProvider GLYPHICON_HAND_UP = DefaultCSSClassProvider.create ("glyphicon-hand-up");
  public static final ICSSClassProvider GLYPHICON_HD_VIDEO = DefaultCSSClassProvider.create ("glyphicon-hd-video");
  public static final ICSSClassProvider GLYPHICON_HDD = DefaultCSSClassProvider.create ("glyphicon-hdd");
  public static final ICSSClassProvider GLYPHICON_HEADER = DefaultCSSClassProvider.create ("glyphicon-header");
  public static final ICSSClassProvider GLYPHICON_HEADPHONES = DefaultCSSClassProvider.create ("glyphicon-headphones");
  public static final ICSSClassProvider GLYPHICON_HEART = DefaultCSSClassProvider.create ("glyphicon-heart");
  public static final ICSSClassProvider GLYPHICON_HEART_EMPTY = DefaultCSSClassProvider.create ("glyphicon-heart-empty");
  public static final ICSSClassProvider GLYPHICON_HOME = DefaultCSSClassProvider.create ("glyphicon-home");
  public static final ICSSClassProvider GLYPHICON_IMPORT = DefaultCSSClassProvider.create ("glyphicon-import");
  public static final ICSSClassProvider GLYPHICON_INBOX = DefaultCSSClassProvider.create ("glyphicon-inbox");
  public static final ICSSClassProvider GLYPHICON_INDENT_LEFT = DefaultCSSClassProvider.create ("glyphicon-indent-left");
  public static final ICSSClassProvider GLYPHICON_INDENT_RIGHT = DefaultCSSClassProvider.create ("glyphicon-indent-right");
  public static final ICSSClassProvider GLYPHICON_INFO_SIGN = DefaultCSSClassProvider.create ("glyphicon-info-sign");
  public static final ICSSClassProvider GLYPHICON_ITALIC = DefaultCSSClassProvider.create ("glyphicon-italic");
  public static final ICSSClassProvider GLYPHICON_LEAF = DefaultCSSClassProvider.create ("glyphicon-leaf");
  public static final ICSSClassProvider GLYPHICON_LINK = DefaultCSSClassProvider.create ("glyphicon-link");
  public static final ICSSClassProvider GLYPHICON_LIST = DefaultCSSClassProvider.create ("glyphicon-list");
  public static final ICSSClassProvider GLYPHICON_LIST_ALT = DefaultCSSClassProvider.create ("glyphicon-list-alt");
  public static final ICSSClassProvider GLYPHICON_LOCK = DefaultCSSClassProvider.create ("glyphicon-lock");
  public static final ICSSClassProvider GLYPHICON_LOG_IN = DefaultCSSClassProvider.create ("glyphicon-log-in");
  public static final ICSSClassProvider GLYPHICON_LOG_OUT = DefaultCSSClassProvider.create ("glyphicon-log-out");
  public static final ICSSClassProvider GLYPHICON_MAGNET = DefaultCSSClassProvider.create ("glyphicon-magnet");
  public static final ICSSClassProvider GLYPHICON_MAP_MARKER = DefaultCSSClassProvider.create ("glyphicon-map-marker");
  public static final ICSSClassProvider GLYPHICON_MINUS = DefaultCSSClassProvider.create ("glyphicon-minus");
  public static final ICSSClassProvider GLYPHICON_MINUS_SIGN = DefaultCSSClassProvider.create ("glyphicon-minus-sign");
  public static final ICSSClassProvider GLYPHICON_MOVE = DefaultCSSClassProvider.create ("glyphicon-move");
  public static final ICSSClassProvider GLYPHICON_MUSIC = DefaultCSSClassProvider.create ("glyphicon-music");
  public static final ICSSClassProvider GLYPHICON_NEW_WINDOW = DefaultCSSClassProvider.create ("glyphicon-new-window");
  public static final ICSSClassProvider GLYPHICON_OFF = DefaultCSSClassProvider.create ("glyphicon-off");
  public static final ICSSClassProvider GLYPHICON_OK = DefaultCSSClassProvider.create ("glyphicon-ok");
  public static final ICSSClassProvider GLYPHICON_OK_CIRCLE = DefaultCSSClassProvider.create ("glyphicon-ok-circle");
  public static final ICSSClassProvider GLYPHICON_OK_SIGN = DefaultCSSClassProvider.create ("glyphicon-ok-sign");
  public static final ICSSClassProvider GLYPHICON_OPEN = DefaultCSSClassProvider.create ("glyphicon-open");
  public static final ICSSClassProvider GLYPHICON_PAPERCLIP = DefaultCSSClassProvider.create ("glyphicon-paperclip");
  public static final ICSSClassProvider GLYPHICON_PAUSE = DefaultCSSClassProvider.create ("glyphicon-pause");
  public static final ICSSClassProvider GLYPHICON_PENCIL = DefaultCSSClassProvider.create ("glyphicon-pencil");
  public static final ICSSClassProvider GLYPHICON_PHONE = DefaultCSSClassProvider.create ("glyphicon-phone");
  public static final ICSSClassProvider GLYPHICON_PHONE_ALT = DefaultCSSClassProvider.create ("glyphicon-phone-alt");
  public static final ICSSClassProvider GLYPHICON_PICTURE = DefaultCSSClassProvider.create ("glyphicon-picture");
  public static final ICSSClassProvider GLYPHICON_PLANE = DefaultCSSClassProvider.create ("glyphicon-plane");
  public static final ICSSClassProvider GLYPHICON_PLAY = DefaultCSSClassProvider.create ("glyphicon-play");
  public static final ICSSClassProvider GLYPHICON_PLAY_CIRCLE = DefaultCSSClassProvider.create ("glyphicon-play-circle");
  public static final ICSSClassProvider GLYPHICON_PLUS = DefaultCSSClassProvider.create ("glyphicon-plus");
  public static final ICSSClassProvider GLYPHICON_PLUS_SIGN = DefaultCSSClassProvider.create ("glyphicon-plus-sign");
  public static final ICSSClassProvider GLYPHICON_PRINT = DefaultCSSClassProvider.create ("glyphicon-print");
  public static final ICSSClassProvider GLYPHICON_PUSHPIN = DefaultCSSClassProvider.create ("glyphicon-pushpin");
  public static final ICSSClassProvider GLYPHICON_QRCODE = DefaultCSSClassProvider.create ("glyphicon-qrcode");
  public static final ICSSClassProvider GLYPHICON_QUESTION_SIGN = DefaultCSSClassProvider.create ("glyphicon-question-sign");
  public static final ICSSClassProvider GLYPHICON_RANDOM = DefaultCSSClassProvider.create ("glyphicon-random");
  public static final ICSSClassProvider GLYPHICON_RECORD = DefaultCSSClassProvider.create ("glyphicon-record");
  public static final ICSSClassProvider GLYPHICON_REFRESH = DefaultCSSClassProvider.create ("glyphicon-refresh");
  public static final ICSSClassProvider GLYPHICON_REGISTRATION_MARK = DefaultCSSClassProvider.create ("glyphicon-registration-mark");
  public static final ICSSClassProvider GLYPHICON_REMOVE = DefaultCSSClassProvider.create ("glyphicon-remove");
  public static final ICSSClassProvider GLYPHICON_REMOVE_CIRCLE = DefaultCSSClassProvider.create ("glyphicon-remove-circle");
  public static final ICSSClassProvider GLYPHICON_REMOVE_SIGN = DefaultCSSClassProvider.create ("glyphicon-remove-sign");
  public static final ICSSClassProvider GLYPHICON_REPEAT = DefaultCSSClassProvider.create ("glyphicon-repeat");
  public static final ICSSClassProvider GLYPHICON_RESIZE_FULL = DefaultCSSClassProvider.create ("glyphicon-resize-full");
  public static final ICSSClassProvider GLYPHICON_RESIZE_HORIZONTAL = DefaultCSSClassProvider.create ("glyphicon-resize-horizontal");
  public static final ICSSClassProvider GLYPHICON_RESIZE_SMALL = DefaultCSSClassProvider.create ("glyphicon-resize-small");
  public static final ICSSClassProvider GLYPHICON_RESIZE_VERTICAL = DefaultCSSClassProvider.create ("glyphicon-resize-vertical");
  public static final ICSSClassProvider GLYPHICON_RETWEET = DefaultCSSClassProvider.create ("glyphicon-retweet");
  public static final ICSSClassProvider GLYPHICON_ROAD = DefaultCSSClassProvider.create ("glyphicon-road");
  public static final ICSSClassProvider GLYPHICON_SAVE = DefaultCSSClassProvider.create ("glyphicon-save");
  public static final ICSSClassProvider GLYPHICON_SAVED = DefaultCSSClassProvider.create ("glyphicon-saved");
  public static final ICSSClassProvider GLYPHICON_SCREENSHOT = DefaultCSSClassProvider.create ("glyphicon-screenshot");
  public static final ICSSClassProvider GLYPHICON_SD_VIDEO = DefaultCSSClassProvider.create ("glyphicon-sd-video");
  public static final ICSSClassProvider GLYPHICON_SEARCH = DefaultCSSClassProvider.create ("glyphicon-search");
  public static final ICSSClassProvider GLYPHICON_SEND = DefaultCSSClassProvider.create ("glyphicon-send");
  public static final ICSSClassProvider GLYPHICON_SHARE = DefaultCSSClassProvider.create ("glyphicon-share");
  public static final ICSSClassProvider GLYPHICON_SHARE_ALT = DefaultCSSClassProvider.create ("glyphicon-share-alt");
  public static final ICSSClassProvider GLYPHICON_SHOPPING_CART = DefaultCSSClassProvider.create ("glyphicon-shopping-cart");
  public static final ICSSClassProvider GLYPHICON_SIGNAL = DefaultCSSClassProvider.create ("glyphicon-signal");
  public static final ICSSClassProvider GLYPHICON_SORT = DefaultCSSClassProvider.create ("glyphicon-sort");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ALPHABET = DefaultCSSClassProvider.create ("glyphicon-sort-by-alphabet");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ALPHABET_ALT = DefaultCSSClassProvider.create ("glyphicon-sort-by-alphabet-alt");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ATTRIBUTES = DefaultCSSClassProvider.create ("glyphicon-sort-by-attributes");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ATTRIBUTES_ALT = DefaultCSSClassProvider.create ("glyphicon-sort-by-attributes-alt");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ORDER = DefaultCSSClassProvider.create ("glyphicon-sort-by-order");
  public static final ICSSClassProvider GLYPHICON_SORT_BY_ORDER_ALT = DefaultCSSClassProvider.create ("glyphicon-sort-by-order-alt");
  public static final ICSSClassProvider GLYPHICON_SOUND_5_1 = DefaultCSSClassProvider.create ("glyphicon-sound-5-1");
  public static final ICSSClassProvider GLYPHICON_SOUND_6_1 = DefaultCSSClassProvider.create ("glyphicon-sound-6-1");
  public static final ICSSClassProvider GLYPHICON_SOUND_7_1 = DefaultCSSClassProvider.create ("glyphicon-sound-7-1");
  public static final ICSSClassProvider GLYPHICON_SOUND_DOLBY = DefaultCSSClassProvider.create ("glyphicon-sound-dolby");
  public static final ICSSClassProvider GLYPHICON_SOUND_STEREO = DefaultCSSClassProvider.create ("glyphicon-sound-stereo");
  public static final ICSSClassProvider GLYPHICON_STAR = DefaultCSSClassProvider.create ("glyphicon-star");
  public static final ICSSClassProvider GLYPHICON_STAR_EMPTY = DefaultCSSClassProvider.create ("glyphicon-star-empty");
  public static final ICSSClassProvider GLYPHICON_STATS = DefaultCSSClassProvider.create ("glyphicon-stats");
  public static final ICSSClassProvider GLYPHICON_STEP_BACKWARD = DefaultCSSClassProvider.create ("glyphicon-step-backward");
  public static final ICSSClassProvider GLYPHICON_STEP_FORWARD = DefaultCSSClassProvider.create ("glyphicon-step-forward");
  public static final ICSSClassProvider GLYPHICON_STOP = DefaultCSSClassProvider.create ("glyphicon-stop");
  public static final ICSSClassProvider GLYPHICON_SUBTITLES = DefaultCSSClassProvider.create ("glyphicon-subtitles");
  public static final ICSSClassProvider GLYPHICON_TAG = DefaultCSSClassProvider.create ("glyphicon-tag");
  public static final ICSSClassProvider GLYPHICON_TAGS = DefaultCSSClassProvider.create ("glyphicon-tags");
  public static final ICSSClassProvider GLYPHICON_TASKS = DefaultCSSClassProvider.create ("glyphicon-tasks");
  public static final ICSSClassProvider GLYPHICON_TEXT_HEIGHT = DefaultCSSClassProvider.create ("glyphicon-text-height");
  public static final ICSSClassProvider GLYPHICON_TEXT_WIDTH = DefaultCSSClassProvider.create ("glyphicon-text-width");
  public static final ICSSClassProvider GLYPHICON_TH = DefaultCSSClassProvider.create ("glyphicon-th");
  public static final ICSSClassProvider GLYPHICON_TH_LARGE = DefaultCSSClassProvider.create ("glyphicon-th-large");
  public static final ICSSClassProvider GLYPHICON_TH_LIST = DefaultCSSClassProvider.create ("glyphicon-th-list");
  public static final ICSSClassProvider GLYPHICON_THUMBS_DOWN = DefaultCSSClassProvider.create ("glyphicon-thumbs-down");
  public static final ICSSClassProvider GLYPHICON_THUMBS_UP = DefaultCSSClassProvider.create ("glyphicon-thumbs-up");
  public static final ICSSClassProvider GLYPHICON_TIME = DefaultCSSClassProvider.create ("glyphicon-time");
  public static final ICSSClassProvider GLYPHICON_TINT = DefaultCSSClassProvider.create ("glyphicon-tint");
  public static final ICSSClassProvider GLYPHICON_TOWER = DefaultCSSClassProvider.create ("glyphicon-tower");
  public static final ICSSClassProvider GLYPHICON_TRANSFER = DefaultCSSClassProvider.create ("glyphicon-transfer");
  public static final ICSSClassProvider GLYPHICON_TRASH = DefaultCSSClassProvider.create ("glyphicon-trash");
  public static final ICSSClassProvider GLYPHICON_TREE_CONIFER = DefaultCSSClassProvider.create ("glyphicon-tree-conifer");
  public static final ICSSClassProvider GLYPHICON_TREE_DECIDUOUS = DefaultCSSClassProvider.create ("glyphicon-tree-deciduous");
  public static final ICSSClassProvider GLYPHICON_UNCHECKED = DefaultCSSClassProvider.create ("glyphicon-unchecked");
  public static final ICSSClassProvider GLYPHICON_UPLOAD = DefaultCSSClassProvider.create ("glyphicon-upload");
  public static final ICSSClassProvider GLYPHICON_USD = DefaultCSSClassProvider.create ("glyphicon-usd");
  public static final ICSSClassProvider GLYPHICON_USER = DefaultCSSClassProvider.create ("glyphicon-user");
  public static final ICSSClassProvider GLYPHICON_VOLUME_DOWN = DefaultCSSClassProvider.create ("glyphicon-volume-down");
  public static final ICSSClassProvider GLYPHICON_VOLUME_OFF = DefaultCSSClassProvider.create ("glyphicon-volume-off");
  public static final ICSSClassProvider GLYPHICON_VOLUME_UP = DefaultCSSClassProvider.create ("glyphicon-volume-up");
  public static final ICSSClassProvider GLYPHICON_WARNING_SIGN = DefaultCSSClassProvider.create ("glyphicon-warning-sign");
  public static final ICSSClassProvider GLYPHICON_WRENCH = DefaultCSSClassProvider.create ("glyphicon-wrench");
  public static final ICSSClassProvider GLYPHICON_ZOOM_IN = DefaultCSSClassProvider.create ("glyphicon-zoom-in");
  public static final ICSSClassProvider GLYPHICON_ZOOM_OUT = DefaultCSSClassProvider.create ("glyphicon-zoom-out");
  @Since ("3.0.1")
  public static final ICSSClassProvider GLYPHICONS_CHEVRON_LEFT = DefaultCSSClassProvider.create ("glyphicons-chevron-left");
  @Since ("3.0.1")
  public static final ICSSClassProvider GLYPHICONS_CHEVRON_RIGHT = DefaultCSSClassProvider.create ("glyphicons-chevron-right");
  public static final ICSSClassProvider H1 = DefaultCSSClassProvider.create ("h1");
  public static final ICSSClassProvider H2 = DefaultCSSClassProvider.create ("h2");
  public static final ICSSClassProvider H3 = DefaultCSSClassProvider.create ("h3");
  public static final ICSSClassProvider H4 = DefaultCSSClassProvider.create ("h4");
  public static final ICSSClassProvider H5 = DefaultCSSClassProvider.create ("h5");
  public static final ICSSClassProvider H6 = DefaultCSSClassProvider.create ("h6");
  public static final ICSSClassProvider HAS_ERROR = DefaultCSSClassProvider.create ("has-error");
  public static final ICSSClassProvider HAS_SUCCESS = DefaultCSSClassProvider.create ("has-success");
  public static final ICSSClassProvider HAS_WARNING = DefaultCSSClassProvider.create ("has-warning");
  public static final ICSSClassProvider HELP_BLOCK = DefaultCSSClassProvider.create ("help-block");
  public static final ICSSClassProvider HIDDEN = DefaultCSSClassProvider.create ("hidden");
  public static final ICSSClassProvider HIDDEN_LG = DefaultCSSClassProvider.create ("hidden-lg");
  public static final ICSSClassProvider HIDDEN_MD = DefaultCSSClassProvider.create ("hidden-md");
  public static final ICSSClassProvider HIDDEN_PRINT = DefaultCSSClassProvider.create ("hidden-print");
  public static final ICSSClassProvider HIDDEN_SM = DefaultCSSClassProvider.create ("hidden-sm");
  public static final ICSSClassProvider HIDDEN_XS = DefaultCSSClassProvider.create ("hidden-xs");
  public static final ICSSClassProvider HIDE = DefaultCSSClassProvider.create ("hide");
  public static final ICSSClassProvider ICON_BAR = DefaultCSSClassProvider.create ("icon-bar");
  public static final ICSSClassProvider ICON_NEXT = DefaultCSSClassProvider.create ("icon-next");
  public static final ICSSClassProvider ICON_PREV = DefaultCSSClassProvider.create ("icon-prev");
  public static final ICSSClassProvider IMG_CIRCLE = DefaultCSSClassProvider.create ("img-circle");
  public static final ICSSClassProvider IMG_RESPONSIVE = DefaultCSSClassProvider.create ("img-responsive");
  public static final ICSSClassProvider IMG_ROUNDED = DefaultCSSClassProvider.create ("img-rounded");
  public static final ICSSClassProvider IMG_THUMBNAIL = DefaultCSSClassProvider.create ("img-thumbnail");
  public static final ICSSClassProvider IN = DefaultCSSClassProvider.create ("in");
  public static final ICSSClassProvider INITIALISM = DefaultCSSClassProvider.create ("initialism");
  public static final ICSSClassProvider INPUT_GROUP = DefaultCSSClassProvider.create ("input-group");
  public static final ICSSClassProvider INPUT_GROUP_ADDON = DefaultCSSClassProvider.create ("input-group-addon");
  public static final ICSSClassProvider INPUT_GROUP_BTN = DefaultCSSClassProvider.create ("input-group-btn");
  public static final ICSSClassProvider INPUT_GROUP_LG = DefaultCSSClassProvider.create ("input-group-lg");
  public static final ICSSClassProvider INPUT_GROUP_SM = DefaultCSSClassProvider.create ("input-group-sm");
  public static final ICSSClassProvider INPUT_LG = DefaultCSSClassProvider.create ("input-lg");
  public static final ICSSClassProvider INPUT_SM = DefaultCSSClassProvider.create ("input-sm");
  public static final ICSSClassProvider INVISIBLE = DefaultCSSClassProvider.create ("invisible");
  @Deprecated
  public static final ICSSClassProvider IR = DefaultCSSClassProvider.create ("ir");
  public static final ICSSClassProvider ITEM = DefaultCSSClassProvider.create ("item");
  public static final ICSSClassProvider JUMBOTRON = DefaultCSSClassProvider.create ("jumbotron");
  public static final ICSSClassProvider LABEL = DefaultCSSClassProvider.create ("label");
  public static final ICSSClassProvider LABEL_DANGER = DefaultCSSClassProvider.create ("label-danger");
  public static final ICSSClassProvider LABEL_DEFAULT = DefaultCSSClassProvider.create ("label-default");
  public static final ICSSClassProvider LABEL_INFO = DefaultCSSClassProvider.create ("label-info");
  public static final ICSSClassProvider LABEL_PRIMARY = DefaultCSSClassProvider.create ("label-primary");
  public static final ICSSClassProvider LABEL_SUCCESS = DefaultCSSClassProvider.create ("label-success");
  public static final ICSSClassProvider LABEL_WARNING = DefaultCSSClassProvider.create ("label-warning");
  public static final ICSSClassProvider LEAD = DefaultCSSClassProvider.create ("lead");
  public static final ICSSClassProvider LEFT = DefaultCSSClassProvider.create ("left");
  public static final ICSSClassProvider LIST_GROUP = DefaultCSSClassProvider.create ("list-group");
  public static final ICSSClassProvider LIST_GROUP_ITEM = DefaultCSSClassProvider.create ("list-group-item");
  public static final ICSSClassProvider LIST_GROUP_ITEM_HEADING = DefaultCSSClassProvider.create ("list-group-item-heading");
  public static final ICSSClassProvider LIST_GROUP_ITEM_TEXT = DefaultCSSClassProvider.create ("list-group-item-text");
  public static final ICSSClassProvider LIST_INLINE = DefaultCSSClassProvider.create ("list-inline");
  public static final ICSSClassProvider LIST_UNSTYLED = DefaultCSSClassProvider.create ("list-unstyled");
  public static final ICSSClassProvider MEDIA = DefaultCSSClassProvider.create ("media");
  public static final ICSSClassProvider MEDIA_BODY = DefaultCSSClassProvider.create ("media-body");
  public static final ICSSClassProvider MEDIA_HEADING = DefaultCSSClassProvider.create ("media-heading");
  public static final ICSSClassProvider MEDIA_LIST = DefaultCSSClassProvider.create ("media-list");
  public static final ICSSClassProvider MEDIA_OBJECT = DefaultCSSClassProvider.create ("media-object");
  public static final ICSSClassProvider MODAL = DefaultCSSClassProvider.create ("modal");
  public static final ICSSClassProvider MODAL_BACKDROP = DefaultCSSClassProvider.create ("modal-backdrop");
  public static final ICSSClassProvider MODAL_BODY = DefaultCSSClassProvider.create ("modal-body");
  public static final ICSSClassProvider MODAL_CONTENT = DefaultCSSClassProvider.create ("modal-content");
  public static final ICSSClassProvider MODAL_DIALOG = DefaultCSSClassProvider.create ("modal-dialog");
  public static final ICSSClassProvider MODAL_FOOTER = DefaultCSSClassProvider.create ("modal-footer");
  public static final ICSSClassProvider MODAL_HEADER = DefaultCSSClassProvider.create ("modal-header");
  public static final ICSSClassProvider MODAL_OPEN = DefaultCSSClassProvider.create ("modal-open");
  public static final ICSSClassProvider MODAL_TITLE = DefaultCSSClassProvider.create ("modal-title");
  public static final ICSSClassProvider NAV = DefaultCSSClassProvider.create ("nav");
  public static final ICSSClassProvider NAV_DIVIDER = DefaultCSSClassProvider.create ("nav-divider");
  public static final ICSSClassProvider NAV_JUSTIFIED = DefaultCSSClassProvider.create ("nav-justified");
  public static final ICSSClassProvider NAV_PILLS = DefaultCSSClassProvider.create ("nav-pills");
  public static final ICSSClassProvider NAV_STACKED = DefaultCSSClassProvider.create ("nav-stacked");
  public static final ICSSClassProvider NAV_TABS = DefaultCSSClassProvider.create ("nav-tabs");
  public static final ICSSClassProvider NAV_TABS_JUSTIFIED = DefaultCSSClassProvider.create ("nav-tabs-justified");
  public static final ICSSClassProvider NAVBAR = DefaultCSSClassProvider.create ("navbar");
  public static final ICSSClassProvider NAVBAR_BRAND = DefaultCSSClassProvider.create ("navbar-brand");
  public static final ICSSClassProvider NAVBAR_BTN = DefaultCSSClassProvider.create ("navbar-btn");
  public static final ICSSClassProvider NAVBAR_COLLAPSE = DefaultCSSClassProvider.create ("navbar-collapse");
  public static final ICSSClassProvider NAVBAR_DEFAULT = DefaultCSSClassProvider.create ("navbar-default");
  public static final ICSSClassProvider NAVBAR_FIXED_BOTTOM = DefaultCSSClassProvider.create ("navbar-fixed-bottom");
  public static final ICSSClassProvider NAVBAR_FIXED_TOP = DefaultCSSClassProvider.create ("navbar-fixed-top");
  public static final ICSSClassProvider NAVBAR_FORM = DefaultCSSClassProvider.create ("navbar-form");
  public static final ICSSClassProvider NAVBAR_HEADER = DefaultCSSClassProvider.create ("navbar-header");
  public static final ICSSClassProvider NAVBAR_INVERSE = DefaultCSSClassProvider.create ("navbar-inverse");
  public static final ICSSClassProvider NAVBAR_LEFT = DefaultCSSClassProvider.create ("navbar-left");
  public static final ICSSClassProvider NAVBAR_LINK = DefaultCSSClassProvider.create ("navbar-link");
  public static final ICSSClassProvider NAVBAR_NAV = DefaultCSSClassProvider.create ("navbar-nav");
  public static final ICSSClassProvider NAVBAR_RIGHT = DefaultCSSClassProvider.create ("navbar-right");
  public static final ICSSClassProvider NAVBAR_STATIC_TOP = DefaultCSSClassProvider.create ("navbar-static-top");
  public static final ICSSClassProvider NAVBAR_TEXT = DefaultCSSClassProvider.create ("navbar-text");
  public static final ICSSClassProvider NAVBAR_TOGGLE = DefaultCSSClassProvider.create ("navbar-toggle");
  public static final ICSSClassProvider NEXT = DefaultCSSClassProvider.create ("next");
  public static final ICSSClassProvider OPEN = DefaultCSSClassProvider.create ("open");
  public static final ICSSClassProvider PAGE_HEADER = DefaultCSSClassProvider.create ("page-header");
  public static final ICSSClassProvider PAGER = DefaultCSSClassProvider.create ("pager");
  public static final ICSSClassProvider PAGINATION = DefaultCSSClassProvider.create ("pagination");
  public static final ICSSClassProvider PAGINATION_LG = DefaultCSSClassProvider.create ("pagination-lg");
  public static final ICSSClassProvider PAGINATION_SM = DefaultCSSClassProvider.create ("pagination-sm");
  public static final ICSSClassProvider PANEL = DefaultCSSClassProvider.create ("panel");
  public static final ICSSClassProvider PANEL_BODY = DefaultCSSClassProvider.create ("panel-body");
  public static final ICSSClassProvider PANEL_COLLAPSE = DefaultCSSClassProvider.create ("panel-collapse");
  public static final ICSSClassProvider PANEL_DANGER = DefaultCSSClassProvider.create ("panel-danger");
  public static final ICSSClassProvider PANEL_DEFAULT = DefaultCSSClassProvider.create ("panel-default");
  public static final ICSSClassProvider PANEL_FOOTER = DefaultCSSClassProvider.create ("panel-footer");
  public static final ICSSClassProvider PANEL_GROUP = DefaultCSSClassProvider.create ("panel-group");
  public static final ICSSClassProvider PANEL_HEADING = DefaultCSSClassProvider.create ("panel-heading");
  public static final ICSSClassProvider PANEL_INFO = DefaultCSSClassProvider.create ("panel-info");
  public static final ICSSClassProvider PANEL_PRIMARY = DefaultCSSClassProvider.create ("panel-primary");
  public static final ICSSClassProvider PANEL_SUCCESS = DefaultCSSClassProvider.create ("panel-success");
  public static final ICSSClassProvider PANEL_TITLE = DefaultCSSClassProvider.create ("panel-title");
  public static final ICSSClassProvider PANEL_WARNING = DefaultCSSClassProvider.create ("panel-warning");
  @Deprecated
  public static final ICSSClassProvider PILL_CONTENT = DefaultCSSClassProvider.create ("pill-content");
  @Deprecated
  public static final ICSSClassProvider PILL_PANE = DefaultCSSClassProvider.create ("pill-pane");
  public static final ICSSClassProvider POPOVER = DefaultCSSClassProvider.create ("popover");
  public static final ICSSClassProvider POPOVER_CONTENT = DefaultCSSClassProvider.create ("popover-content");
  public static final ICSSClassProvider POPOVER_TITLE = DefaultCSSClassProvider.create ("popover-title");
  public static final ICSSClassProvider PRE_SCROLLABLE = DefaultCSSClassProvider.create ("pre-scrollable");
  @Deprecated
  public static final ICSSClassProvider PRETTYPRINT = DefaultCSSClassProvider.create ("prettyprint");
  public static final ICSSClassProvider PREV = DefaultCSSClassProvider.create ("prev");
  public static final ICSSClassProvider PREVIOUS = DefaultCSSClassProvider.create ("previous");
  public static final ICSSClassProvider PROGRESS = DefaultCSSClassProvider.create ("progress");
  public static final ICSSClassProvider PROGRESS_BAR = DefaultCSSClassProvider.create ("progress-bar");
  public static final ICSSClassProvider PROGRESS_BAR_DANGER = DefaultCSSClassProvider.create ("progress-bar-danger");
  public static final ICSSClassProvider PROGRESS_BAR_INFO = DefaultCSSClassProvider.create ("progress-bar-info");
  public static final ICSSClassProvider PROGRESS_BAR_SUCCESS = DefaultCSSClassProvider.create ("progress-bar-success");
  public static final ICSSClassProvider PROGRESS_BAR_WARNING = DefaultCSSClassProvider.create ("progress-bar-warning");
  public static final ICSSClassProvider PROGRESS_STRIPED = DefaultCSSClassProvider.create ("progress-striped");
  public static final ICSSClassProvider PULL_LEFT = DefaultCSSClassProvider.create ("pull-left");
  public static final ICSSClassProvider PULL_RIGHT = DefaultCSSClassProvider.create ("pull-right");
  public static final ICSSClassProvider RADIO = DefaultCSSClassProvider.create ("radio");
  public static final ICSSClassProvider RADIO_INLINE = DefaultCSSClassProvider.create ("radio-inline");
  public static final ICSSClassProvider RIGHT = DefaultCSSClassProvider.create ("right");
  public static final ICSSClassProvider ROW = DefaultCSSClassProvider.create ("row");
  public static final ICSSClassProvider SHOW = DefaultCSSClassProvider.create ("show");
  @Since ("3.0.1")
  public static final ICSSClassProvider SMALL = DefaultCSSClassProvider.create ("small");
  public static final ICSSClassProvider SR_ONLY = DefaultCSSClassProvider.create ("sr-only");
  public static final ICSSClassProvider SUCCESS = DefaultCSSClassProvider.create ("success");
  public static final ICSSClassProvider TAB_CONTENT = DefaultCSSClassProvider.create ("tab-content");
  public static final ICSSClassProvider TAB_PANE = DefaultCSSClassProvider.create ("tab-pane");
  @Deprecated
  public static final ICSSClassProvider TABBABLE = DefaultCSSClassProvider.create ("tabbable");
  public static final ICSSClassProvider TABLE = DefaultCSSClassProvider.create ("table");
  public static final ICSSClassProvider TABLE_BORDERED = DefaultCSSClassProvider.create ("table-bordered");
  public static final ICSSClassProvider TABLE_CONDENSED = DefaultCSSClassProvider.create ("table-condensed");
  public static final ICSSClassProvider TABLE_HOVER = DefaultCSSClassProvider.create ("table-hover");
  public static final ICSSClassProvider TABLE_RESPONSIVE = DefaultCSSClassProvider.create ("table-responsive");
  public static final ICSSClassProvider TABLE_STRIPED = DefaultCSSClassProvider.create ("table-striped");
  public static final ICSSClassProvider TEXT_CENTER = DefaultCSSClassProvider.create ("text-center");
  public static final ICSSClassProvider TEXT_DANGER = DefaultCSSClassProvider.create ("text-danger");
  public static final ICSSClassProvider TEXT_HIDE = DefaultCSSClassProvider.create ("text-hide");
  public static final ICSSClassProvider TEXT_INFO = DefaultCSSClassProvider.create ("text-info");
  public static final ICSSClassProvider TEXT_LEFT = DefaultCSSClassProvider.create ("text-left");
  public static final ICSSClassProvider TEXT_MUTED = DefaultCSSClassProvider.create ("text-muted");
  public static final ICSSClassProvider TEXT_PRIMARY = DefaultCSSClassProvider.create ("text-primary");
  public static final ICSSClassProvider TEXT_RIGHT = DefaultCSSClassProvider.create ("text-right");
  public static final ICSSClassProvider TEXT_SUCCESS = DefaultCSSClassProvider.create ("text-success");
  public static final ICSSClassProvider TEXT_WARNING = DefaultCSSClassProvider.create ("text-warning");
  public static final ICSSClassProvider THUMBNAIL = DefaultCSSClassProvider.create ("thumbnail");
  public static final ICSSClassProvider TOOLTIP = DefaultCSSClassProvider.create ("tooltip");
  public static final ICSSClassProvider TOOLTIP_ARROW = DefaultCSSClassProvider.create ("tooltip-arrow");
  public static final ICSSClassProvider TOOLTIP_INNER = DefaultCSSClassProvider.create ("tooltip-inner");
  public static final ICSSClassProvider TOP = DefaultCSSClassProvider.create ("top");
  public static final ICSSClassProvider TOP_LEFT = DefaultCSSClassProvider.create ("top-left");
  public static final ICSSClassProvider TOP_RIGHT = DefaultCSSClassProvider.create ("top-right");
  public static final ICSSClassProvider VISIBLE_LG = DefaultCSSClassProvider.create ("visible-lg");
  public static final ICSSClassProvider VISIBLE_MD = DefaultCSSClassProvider.create ("visible-md");
  public static final ICSSClassProvider VISIBLE_PRINT = DefaultCSSClassProvider.create ("visible-print");
  public static final ICSSClassProvider VISIBLE_SM = DefaultCSSClassProvider.create ("visible-sm");
  public static final ICSSClassProvider VISIBLE_XS = DefaultCSSClassProvider.create ("visible-xs");
  public static final ICSSClassProvider WARNING = DefaultCSSClassProvider.create ("warning");
  public static final ICSSClassProvider WELL = DefaultCSSClassProvider.create ("well");
  public static final ICSSClassProvider WELL_LG = DefaultCSSClassProvider.create ("well-lg");
  public static final ICSSClassProvider WELL_SM = DefaultCSSClassProvider.create ("well-sm");

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
    return DANGER;
  }
}
