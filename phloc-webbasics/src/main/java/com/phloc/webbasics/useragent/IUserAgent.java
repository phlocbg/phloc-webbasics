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
package com.phloc.webbasics.useragent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.webbasics.browser.BrowserInfo;
import com.phloc.webbasics.browser.BrowserInfoIE;
import com.phloc.webbasics.browser.BrowserInfoMobile;
import com.phloc.webbasics.browser.BrowserInfoSpider;

/**
 * Represents a single parsed user agent.
 * 
 * @author philip
 */
public interface IUserAgent extends IHasStringRepresentation
{
  /**
   * @return The original user agent string as sent by the browser.
   */
  String getAsString ();

  /**
   * @return The information about the matching browser or <code>null</code> if
   *         no known browser was detected.
   */
  @Nullable
  BrowserInfo getBrowserInfo ();

  /**
   * @return Information about Mozilla Firefox in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoFirefox ();

  /**
   * @return Information about Microsoft Internet Explorer in this user agent.
   */
  @Nonnull
  BrowserInfoIE getInfoIE ();

  /**
   * @return Information about Opera in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoOpera ();

  /**
   * @return Information about Apple Safari in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoSafari ();

  /**
   * @return Information about Google Chrome in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoChrome ();

  /**
   * @return Information about Lynx in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoLynx ();

  /**
   * @return Information about Konqueror in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoKonqueror ();

  /**
   * @return Information about Gecko based browsers in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoGeckoBased ();

  /**
   * @return Information about WebKit based browsers in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoWebKitBased ();

  /**
   * @return Information about mobile browsers in this user agent.
   */
  @Nonnull
  BrowserInfoMobile getInfoMobile ();

  /**
   * @return Information about search engines/web spiders in this user agent.
   */
  @Nonnull
  BrowserInfoSpider getInfoWebSpider ();

  /**
   * @return Information about applications/APIs in this user agent.
   */
  @Nonnull
  BrowserInfo getInfoApplication ();
}
