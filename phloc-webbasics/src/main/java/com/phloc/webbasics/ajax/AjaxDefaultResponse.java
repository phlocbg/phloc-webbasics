/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.ajax;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCHasChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.utils.AbstractHCSpecialNodes;
import com.phloc.html.hc.utils.HCSpecialNodeHandler;
import com.phloc.json.IJSON;
import com.phloc.json.IJSONObject;
import com.phloc.json.impl.JSONObject;
import com.phloc.webbasics.IWebURIToURLConverter;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.app.html.ResourceInclusionSettings;
import com.phloc.webbasics.app.html.StreamURIToURLConverter;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

@Immutable
public class AjaxDefaultResponse extends AbstractHCSpecialNodes <AjaxDefaultResponse> implements IAjaxResponse
{
  private static final long serialVersionUID = 6050693354636525867L;
  /** Success property */
  public static final String PROPERTY_SUCCESS = "success"; //$NON-NLS-1$
  /**
   * Response value property - only in case of success - contains the response
   * data as object
   */
  public static final String PROPERTY_VALUE = "value"; //$NON-NLS-1$
  /**
   * Additional CSS files - only in case of success - contains a list of strings
   */
  public static final String PROPERTY_EXTERNAL_CSS = "externalcss"; //$NON-NLS-1$
  /** Additional inline CSS - only in case of success - contains a string */
  public static final String PROPERTY_INLINE_CSS = "inlinecss"; //$NON-NLS-1$
  /**
   * Additional JS files - only in case of success - contains a list of strings
   */
  public static final String PROPERTY_EXTERNAL_JS = "externaljs"; //$NON-NLS-1$
  /** Additional inline JS - only in case of success - contains a string */
  public static final String PROPERTY_INLINE_JS = "inlinejs"; //$NON-NLS-1$
  /** Error message property - only in case of error */
  public static final String PROPERTY_ERRORMESSAGE = "errormessage"; //$NON-NLS-1$
  /** Default property for HTML content */
  public static final String PROPERTY_HTML = "html"; //$NON-NLS-1$

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  /**
   * The converter to be used from relative URIs (for CSS and JS) to absolute
   * URLs.
   */
  @GuardedBy ("s_aRWLock")
  private static IWebURIToURLConverter s_aConverter = StreamURIToURLConverter.getInstance ();

  private final boolean m_bSuccess;
  private final String m_sErrorMessage;
  private final IJSONObject m_aSuccessValue;

  private void _addCSSAndJS (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                             @Nonnull final IWebURIToURLConverter aConverter)
  {
    ValueEnforcer.notNull (aRequestScope, "RequestScope"); //$NON-NLS-1$
    ValueEnforcer.notNull (aConverter, "URIToURLConverter"); //$NON-NLS-1$

    // Grab per-request CSS/JS only in success case!
    for (final ISimpleURL aCSSPath : PerRequestCSSIncludes.getAllRegisteredCSSIncludeURLsForThisRequest (aRequestScope,
                                                                                                         aConverter,
                                                                                                         ResourceInclusionSettings.getInstance ()
                                                                                                                                  .isUseRegularCSS ()))
      addExternalCSS (aCSSPath.getAsString ());
    for (final ISimpleURL aJSPath : PerRequestJSIncludes.getAllRegisteredJSIncludeURLsForThisRequest (aRequestScope,
                                                                                                      aConverter,
                                                                                                      ResourceInclusionSettings.getInstance ()
                                                                                                                               .isUseRegularJS ()))
      addExternalJS (aJSPath.getAsString ());
  }

  /**
   * Success constructor for HC nodes
   * 
   * @param aRequestScope
   *        Request scope
   * @param aNode
   *        The response HTML node. May be <code>null</code>.
   * @param aConverter
   *        the converter to use
   */
  protected AjaxDefaultResponse (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                 @Nullable final IHCNode aNode,
                                 @Nonnull final IWebURIToURLConverter aConverter)
  {
    // Do it first
    _addCSSAndJS (aRequestScope, aConverter);

    // Now decompose the HCNode itself
    final JSONObject aObj = new JSONObject ();
    if (aNode != null)
    {
      // Customize before extracting special content
      if (aNode instanceof IHCNodeWithChildren <?>)
        HCUtils.customizeNodes ((IHCNodeWithChildren <?>) aNode, HCSettings.getConversionSettings ());

      IHCNode aRealNode;
      if (aNode instanceof IHCHasChildren)
      {
        // no need to keepOnDocumentReady stuff as the document is already
        // loaded
        final boolean bKeepOnDocumentReady = false;
        aRealNode = HCSpecialNodeHandler.extractSpecialContent ((IHCHasChildren) aNode, this, bKeepOnDocumentReady);
      }
      else
        aRealNode = aNode;

      // Serialize remaining node to HTML
      aObj.setStringProperty (PROPERTY_HTML, HCSettings.getAsHTMLStringWithoutNamespaces (aRealNode));
    }
    this.m_bSuccess = true;
    this.m_sErrorMessage = null;
    this.m_aSuccessValue = aObj;
  }

  protected AjaxDefaultResponse (final boolean bSuccess,
                                 @Nullable final String sErrorMessage,
                                 @Nullable final IJSONObject aSuccessValue,
                                 @Nullable final IRequestWebScopeWithoutResponse aRequestScope,
                                 @Nullable final IWebURIToURLConverter aConverter)
  {
    this.m_bSuccess = bSuccess;
    this.m_sErrorMessage = sErrorMessage;
    this.m_aSuccessValue = aSuccessValue;
    if (bSuccess)
      _addCSSAndJS (aRequestScope, aConverter);
  }

  public static void setDefaultURIToURLConverter (@Nonnull final IWebURIToURLConverter aConverter)
  {
    ValueEnforcer.notNull (aConverter, "Converter"); //$NON-NLS-1$

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aConverter = aConverter;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nonnull
  public static IWebURIToURLConverter getDefaultURIToURLConverter ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aConverter;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  @Override
  public boolean isSuccess ()
  {
    return this.m_bSuccess;
  }

  @Override
  public boolean isFailure ()
  {
    return !this.m_bSuccess;
  }

  /**
   * @return In case this is a failure, this field contains the error message.
   *         May be <code>null</code>.
   */
  @Nullable
  public String getErrorMessage ()
  {
    return this.m_sErrorMessage;
  }

  /**
   * @return In case this is a success, this field contains the success object.
   *         May be <code>null</code>.
   */
  @Nullable
  public IJSON getSuccessValue ()
  {
    return this.m_aSuccessValue;
  }

  @Override
  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    final JSONObject aAssocArray = new JSONObject ();
    aAssocArray.setBooleanProperty (PROPERTY_SUCCESS, this.m_bSuccess);
    if (this.m_bSuccess)
    {
      if (this.m_aSuccessValue != null)
        aAssocArray.setObjectProperty (PROPERTY_VALUE, this.m_aSuccessValue);
      if (hasExternalCSSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_CSS, getAllExternalCSSs ());
      if (hasInlineCSS ())
        aAssocArray.setStringProperty (PROPERTY_INLINE_CSS, getInlineCSS ().toString ());
      if (hasExternalJSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_JS, getAllExternalJSs ());
      if (hasInlineJS ())
        aAssocArray.setStringProperty (PROPERTY_INLINE_JS, getInlineJS ().getJSCode ());
    }
    else
    {
      aAssocArray.setStringProperty (PROPERTY_ERRORMESSAGE, this.m_sErrorMessage != null ? this.m_sErrorMessage : ""); //$NON-NLS-1$
    }
    return aAssocArray.getJSONString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AjaxDefaultResponse rhs = (AjaxDefaultResponse) o;
    return this.m_bSuccess == rhs.m_bSuccess &&
           EqualsUtils.equals (this.m_sErrorMessage, rhs.m_sErrorMessage) &&
           EqualsUtils.equals (this.m_aSuccessValue, rhs.m_aSuccessValue);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (this.m_bSuccess)
                            .append (this.m_sErrorMessage)
                            .append (this.m_aSuccessValue)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("success", this.m_bSuccess) //$NON-NLS-1$
                            .appendIfNotNull ("errorMsg", this.m_sErrorMessage) //$NON-NLS-1$
                            .appendIfNotNull ("successValue", this.m_aSuccessValue) //$NON-NLS-1$
                            .toString ();
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return createSuccess (aRequestScope, getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nonnull final IWebURIToURLConverter aConverter)
  {
    return createSuccess (aRequestScope, (IJSONObject) null, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nullable final IJSONObject aSuccessValue)
  {
    return createSuccess (aRequestScope, aSuccessValue, getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nullable final IJSONObject aSuccessValue,
                                                   @Nonnull final IWebURIToURLConverter aConverter)
  {
    return new AjaxDefaultResponse (true, null, aSuccessValue, aRequestScope, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nullable final IHCNode aNode)
  {
    // Use the default converter here
    return createSuccess (aRequestScope, aNode, getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nullable final IHCNode aNode,
                                                   @Nonnull final IWebURIToURLConverter aConverter)
  {
    // Special case required
    return new AjaxDefaultResponse (aRequestScope, aNode, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createError ()
  {
    return createError ((String) null);
  }

  @Nonnull
  public static AjaxDefaultResponse createError (@Nullable final String sErrorMessage)
  {
    // No request scope needed in case of error!
    // No converter needed in case of error!
    return new AjaxDefaultResponse (false,
                                    sErrorMessage,
                                    (IJSONObject) null,
                                    (IRequestWebScopeWithoutResponse) null,
                                    (IWebURIToURLConverter) null);
  }
}
