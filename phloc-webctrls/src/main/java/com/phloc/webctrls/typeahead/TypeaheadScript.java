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
package com.phloc.webctrls.typeahead;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import com.phloc.commons.state.EChange;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

@Deprecated
public class TypeaheadScript implements IHCNodeBuilder
{
  public static final int DEFAULT_MIN_LENGTH = 2;
  public static final int DEFAULT_MAX_SHOW_ITEMS = 8;

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static int s_nDefaultMinLength = DEFAULT_MIN_LENGTH;
  @GuardedBy ("s_aRWLock")
  private static int s_nDefaultMaxShowItems = DEFAULT_MAX_SHOW_ITEMS;

  private final IJQuerySelector m_aEditFieldSelector;
  private final JSAnonymousFunction m_aIDCallback;
  private final ISimpleURL m_aAjaxInvocationURL;
  private int m_nMinLength = s_nDefaultMinLength;
  private int m_nMaxShowItems = s_nDefaultMaxShowItems;

  public TypeaheadScript (@Nonnull final IJQuerySelector aEditFieldSelector,
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

  public static void setDefaultMinLength (@Nonnegative final int nDefaultMinLength)
  {
    if (nDefaultMinLength < 0)
      throw new IllegalArgumentException ("DefaultMinLength must be >= 0: " + nDefaultMinLength);

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_nDefaultMinLength = nDefaultMinLength;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public static int getDefaultMinLength ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nDefaultMinLength;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setDefaultMaxShowItems (@Nonnegative final int nDefaultMaxShowItems)
  {
    if (nDefaultMaxShowItems <= 0)
      throw new IllegalArgumentException ("DefaultMaxShowItems must be > 0: " + nDefaultMaxShowItems);

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_nDefaultMaxShowItems = nDefaultMaxShowItems;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnegative
  public static int getDefaultMaxShowItems ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_nDefaultMaxShowItems;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
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
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETypeaheadJSPathProvider.TYPEAHEAD_0_9_3);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETypeaheadJSPathProvider.PHLOC_TYPEAHEAD);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETypeaheadCSSPathProvider.TYPEAHEAD_BOOTSTRAP);
  }
}
