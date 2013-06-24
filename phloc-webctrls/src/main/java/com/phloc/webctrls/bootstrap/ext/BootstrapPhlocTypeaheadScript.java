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
package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.bootstrap.EBootstrapJSPathProvider;

public class BootstrapPhlocTypeaheadScript implements IHCNodeBuilder
{
  private final IJQuerySelector m_aEditFieldSelector;
  private final JSAnonymousFunction m_aIDCallback;
  private final ISimpleURL m_aAjaxInvocationURL;
  private int m_nMinLength = 2;
  private int m_nMaxShowItems = 8;

  public BootstrapPhlocTypeaheadScript (@Nonnull final IJQuerySelector aEditFieldSelector,
                                        @Nonnull final JSAnonymousFunction aIDCallback,
                                        @Nonnull final ISimpleURL aAjaxInvocationURL)
  {
    if (aEditFieldSelector == null)
      throw new NullPointerException ("EditFieldSelector");
    if (aIDCallback == null)
      throw new NullPointerException ("IDCallback");
    if (aAjaxInvocationURL == null)
      throw new NullPointerException ("AjaxInvocationURL");

    m_aEditFieldSelector = aEditFieldSelector;
    m_aIDCallback = aIDCallback;
    m_aAjaxInvocationURL = aAjaxInvocationURL;
  }

  @Nonnegative
  public int getMinLength ()
  {
    return m_nMinLength;
  }

  @Nonnull
  public EChange setMinLength (@Nonnull final int nMinLength)
  {
    if (nMinLength < 1)
      throw new IllegalArgumentException ("MinLength is too small: " + nMinLength);
    if (nMinLength == m_nMinLength)
      return EChange.UNCHANGED;
    m_nMinLength = nMinLength;
    return EChange.CHANGED;
  }

  @Nonnegative
  public int getMaxShowItems ()
  {
    return m_nMaxShowItems;
  }

  @Nonnull
  public EChange setMaxShowItems (@Nonnegative final int nMaxShowItems)
  {
    if (nMaxShowItems < 1)
      throw new IllegalArgumentException ("MaxShowItems is too small: " + nMaxShowItems);
    if (nMaxShowItems == m_nMaxShowItems)
      return EChange.UNCHANGED;
    m_nMaxShowItems = nMaxShowItems;
    return EChange.CHANGED;
  }

  @Nullable
  public HCScript build ()
  {
    registerExternalResources ();
    return new HCScript (new JSInvocation ("registerTypeaheadKeyValuePair").arg (m_aEditFieldSelector.getExpression ())
                                                                           .arg (m_aIDCallback)
                                                                           .arg (m_aAjaxInvocationURL.getAsString ())
                                                                           .arg (m_nMinLength)
                                                                           .arg (m_nMaxShowItems));
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrapJSPathProvider.BOOTSTRAP_PHLOC_TYPEAHEAD);
  }
}
