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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCTypeahead implements IHCNodeBuilder
{
  private final IJSExpression m_aSelector;
  private final List <TypeaheadDataset> m_aDatasets = new ArrayList <TypeaheadDataset> ();
  private JSAnonymousFunction m_aOnInitialized;
  private JSAnonymousFunction m_aOnOpened;
  private JSAnonymousFunction m_aOnClosed;
  private JSAnonymousFunction m_aOnSelected;
  private JSAnonymousFunction m_aOnAutoCompleted;

  public HCTypeahead (@Nonnull final IJQuerySelector aSelector)
  {
    this (aSelector.invoke ());
  }

  public HCTypeahead (@Nonnull final IJSExpression aSelector)
  {
    if (aSelector == null)
      throw new NullPointerException ("selector");
    m_aSelector = aSelector;
  }

  @Nonnull
  public IJSExpression getSelector ()
  {
    return m_aSelector;
  }

  @Nonnull
  public HCTypeahead addDataset (@Nonnull final TypeaheadDataset aDataset)
  {
    if (aDataset == null)
      throw new NullPointerException ("dataset");
    m_aDatasets.add (aDataset);
    return this;
  }

  @Nonnegative
  public int getDatasetCount ()
  {
    return m_aDatasets.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <TypeaheadDataset> getAllDatasets ()
  {
    return ContainerHelper.newList (m_aDatasets);
  }

  /**
   * Triggered after initialization. If data needs to be prefetched, this event
   * will not be triggered until after the prefetched data is processed.
   * 
   * @param aOnInitialized
   *        Function to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public HCTypeahead setOnInitialized (@Nullable final JSAnonymousFunction aOnInitialized)
  {
    m_aOnInitialized = aOnInitialized;
    return this;
  }

  /**
   * @return Triggered after initialization. If data needs to be prefetched,
   *         this event will not be triggered until after the prefetched data is
   *         processed.
   */
  @Nullable
  public JSAnonymousFunction getOnInitialized ()
  {
    return m_aOnInitialized;
  }

  /**
   * Triggered when the dropdown menu of a typeahead is opened.
   * 
   * @param aOnOpened
   *        Function to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public HCTypeahead setOnOpened (@Nullable final JSAnonymousFunction aOnOpened)
  {
    m_aOnOpened = aOnOpened;
    return this;
  }

  /**
   * @return Triggered when the dropdown menu of a typeahead is opened.
   */
  @Nullable
  public JSAnonymousFunction getOnOpened ()
  {
    return m_aOnOpened;
  }

  /**
   * Triggered when the dropdown menu of a typeahead is closed.
   * 
   * @param aOnClosed
   *        Function to call. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public HCTypeahead setOnClosed (@Nullable final JSAnonymousFunction aOnClosed)
  {
    m_aOnClosed = aOnClosed;
    return this;
  }

  /**
   * @return Triggered when the dropdown menu of a typeahead is closed.
   */
  @Nullable
  public JSAnonymousFunction getOnClosed ()
  {
    return m_aOnClosed;
  }

  /**
   * Triggered when a suggestion from the dropdown menu is explicitly selected.
   * The datum for the selected suggestion is passed to the event handler as an
   * argument in addition to the name of the dataset it originated from.
   * 
   * @param aOnSelected
   *        Function to call. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public HCTypeahead setOnSelected (@Nullable final JSAnonymousFunction aOnSelected)
  {
    m_aOnSelected = aOnSelected;
    return this;
  }

  /**
   * @return Triggered when a suggestion from the dropdown menu is explicitly
   *         selected. The datum for the selected suggestion is passed to the
   *         event handler as an argument in addition to the name of the dataset
   *         it originated from.
   */
  @Nullable
  public JSAnonymousFunction getOnSelected ()
  {
    return m_aOnSelected;
  }

  /**
   * Triggered when the query is autocompleted. The datum used for
   * autocompletion is passed to the event handler as an argument in addition to
   * the name of the dataset it originated from.
   * 
   * @param aOnAutoCompleted
   *        Function to call. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public HCTypeahead setOnAutoCompleted (@Nullable final JSAnonymousFunction aOnAutoCompleted)
  {
    m_aOnAutoCompleted = aOnAutoCompleted;
    return this;
  }

  /**
   * @return Triggered when the query is autocompleted. The datum used for
   *         autocompletion is passed to the event handler as an argument in
   *         addition to the name of the dataset it originated from.
   */
  @Nullable
  public JSAnonymousFunction getOnAutoCompleted ()
  {
    return m_aOnAutoCompleted;
  }

  @Nullable
  public JSInvocation getAsJSObject ()
  {
    if (m_aDatasets.isEmpty ())
      throw new IllegalStateException ("At least one dataset must be provided!");

    JSInvocation ret = invoke (m_aSelector);
    if (m_aDatasets.size () == 1)
    {
      // Exactly one dataset
      ret.arg (ContainerHelper.getFirstElement (m_aDatasets).getAsJSObject ());
    }
    else
    {
      // More than one dataset
      final JSArray aJSDatasets = new JSArray ();
      for (final TypeaheadDataset aDataset : m_aDatasets)
        aJSDatasets.add (aDataset.getAsJSObject ());
      ret.arg (aJSDatasets);
    }
    if (m_aOnInitialized != null)
      ret = ret.invoke ("on").arg ("typeahead:initialized").arg (m_aOnInitialized);
    if (m_aOnOpened != null)
      ret = ret.invoke ("on").arg ("typeahead:opened").arg (m_aOnOpened);
    if (m_aOnClosed != null)
      ret = ret.invoke ("on").arg ("typeahead:closed").arg (m_aOnClosed);
    if (m_aOnSelected != null)
      ret = ret.invoke ("on").arg ("typeahead:selected").arg (m_aOnSelected);
    if (m_aOnAutoCompleted != null)
      ret = ret.invoke ("on").arg ("typeahead:autocompleted").arg (m_aOnAutoCompleted);
    return ret;
  }

  @Nullable
  public IHCNode build ()
  {
    return new HCScript (getAsJSObject ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("selector", m_aSelector)
                                       .append ("datasets", m_aDatasets)
                                       .appendIfNotNull ("onInitialized", m_aOnInitialized)
                                       .appendIfNotNull ("onOpened", m_aOnOpened)
                                       .appendIfNotNull ("onClosed", m_aOnClosed)
                                       .appendIfNotNull ("onSelected", m_aOnSelected)
                                       .appendIfNotNull ("onAutoCompleted", m_aOnAutoCompleted)
                                       .toString ();
  }

  @Nonnull
  public static JSInvocation invoke (@Nonnull final IJSExpression aExpr)
  {
    return aExpr.invoke ("typeahead");
  }

  @Nonnull
  public static JSInvocation typeaheadDestroy (@Nonnull final IJSExpression aTypeahead)
  {
    return invoke (aTypeahead).arg ("destroy");
  }

  @Nonnull
  public static JSInvocation typeaheadSetQuery (@Nonnull final IJSExpression aTypeahead, @Nonnull final String sQuery)
  {
    return invoke (aTypeahead).arg ("setQuery").arg (sQuery);
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETypeaheadJSPathProvider.TYPEAHEAD_0_9_3);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETypeaheadJSPathProvider.PHLOC_TYPEAHEAD);
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (ETypeaheadCSSPathProvider.TYPEAHEAD_BOOTSTRAP);
  }
}
