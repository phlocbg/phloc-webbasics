package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.state.EChange;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webctrls.bootstrap.EBootstrapJSPathProvider;

public class BootstrapPhlocTypeahead implements IHCNodeBuilder
{
  private final IJQuerySelector m_aEditFieldSelector;
  private final JSAnonymousFunction m_aIDCallback;
  private final ISimpleURL m_aAjaxInvocationURL;
  private int m_nMinLength = 2;
  private int m_nMaxShowItems = 8;

  public BootstrapPhlocTypeahead (@Nonnull final IJQuerySelector aEditFieldSelector,
                                  @Nonnull final JSAnonymousFunction aIDCallback,
                                  @Nonnull final ISimpleURL aAjaxInvocationURL)
  {
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
  public IHCNode build ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EBootstrapJSPathProvider.BOOTSTRAP_PHLOC_TYPEAHEAD);
    return new HCScript (new JSInvocation ("registerTypeaheadKeyValuePair").arg (m_aEditFieldSelector.getExpression ())
                                                                           .arg (m_aIDCallback)
                                                                           .arg (m_aAjaxInvocationURL.getAsString ())
                                                                           .arg (m_nMinLength)
                                                                           .arg (m_nMaxShowItems));
  }
}
