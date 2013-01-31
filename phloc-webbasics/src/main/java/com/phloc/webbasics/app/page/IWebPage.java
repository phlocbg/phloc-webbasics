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
package com.phloc.webbasics.app.page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.page.IPage;

/**
 * The base interface for a single page of content.
 * 
 * @author philip
 */
public interface IWebPage extends IPage
{
  /**
   * Get the headline of the page. By default it is equal to the page name.
   * 
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   * @return The header/headline of the page. May be <code>null</code>.
   */
  @Nullable
  String getHeaderText (@Nonnull WebPageExecutionContext aWPEC);

  /**
   * @param aWPEC
   *        The web page execution context. Never <code>null</code>.
   */
  void getContent (@Nonnull WebPageExecutionContext aWPEC);
}
