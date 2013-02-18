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
package com.phloc.webbasics.browser;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.version.Version;
import com.phloc.webbasics.spider.WebSpiderInfo;

/**
 * Special browser info for Web Spider user agents.
 * 
 * @author philip
 */
public final class BrowserInfoSpider extends BrowserInfo
{
  /** Is it not */
  @SuppressWarnings ("hiding")
  public static final BrowserInfoSpider IS_IT_NOT = new BrowserInfoSpider ();

  private final WebSpiderInfo m_aWebSpiderInfo;

  private BrowserInfoSpider ()
  {
    m_aWebSpiderInfo = null;
  }

  public BrowserInfoSpider (final WebSpiderInfo aWebSpiderInfo)
  {
    super (EBrowserType.SPIDER, new Version (0));
    m_aWebSpiderInfo = aWebSpiderInfo;
  }

  public String getSearchEngineName ()
  {
    return m_aWebSpiderInfo == null ? null : m_aWebSpiderInfo.getName ();
  }

  @Override
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return getSearchEngineName ();
  }

  @Override
  public String toString ()
  {
    if (isItNot ())
      return new ToStringGenerator (null).append ("isIt", "not").toString ();
    return ToStringGenerator.getDerived (super.toString ()).appendIfNotNull ("info", m_aWebSpiderInfo).toString ();
  }
}
