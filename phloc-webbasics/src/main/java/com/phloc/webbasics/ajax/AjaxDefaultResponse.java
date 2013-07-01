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
package com.phloc.webbasics.ajax;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCHasChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.utils.AbstractHCSpecialNodes;
import com.phloc.html.hc.utils.HCSpecialNodeHandler;
import com.phloc.json.IJSON;
import com.phloc.json.impl.JSONObject;
import com.phloc.webbasics.app.html.IURIToURLConverter;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.app.html.StreamURIToURLConverter;

@Immutable
public class AjaxDefaultResponse extends AbstractHCSpecialNodes <AjaxDefaultResponse> implements
                                                                                     ISuccessIndicator,
                                                                                     IAjaxResponse
{
  /** Success property */
  public static final String PROPERTY_SUCCESS = "success";
  /**
   * Response value property - only in case of success - contains the response
   * data as object
   */
  public static final String PROPERTY_VALUE = "value";
  /**
   * Additional CSS files - only in case of success - contains a list of strings
   */
  public static final String PROPERTY_EXTERNAL_CSS = "externalcss";
  /** Additional JS files - only in case of success - contains a list of strings */
  public static final String PROPERTY_EXTERNAL_JS = "externaljs";
  /** Additional inline JS - only in case of success - contains a string */
  public static final String PROPERTY_INLINE_JS = "inlinejs";
  /** Error message property - only in case of error */
  public static final String PROPERTY_ERRORMESSAGE = "errormessage";
  /** Default property for HTML content */
  public static final String PROPERTY_HTML = "html";

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  /**
   * The converter to be used from relative URIs (for CSS and JS) to absolute
   * URLs.
   */
  @GuardedBy ("s_aRWLock")
  private static IURIToURLConverter s_aConverter = StreamURIToURLConverter.getInstance ();

  private final boolean m_bSuccess;
  private final String m_sErrorMessage;
  private final IJSON m_aSuccessValue;

  private void _addCSSAndJS (@Nonnull final IURIToURLConverter aConverter)
  {
    if (aConverter == null)
      throw new NullPointerException ("URIToURLConverter");

    // Grab per-request CSS/JS only in success case!
    final boolean bRegularFiles = GlobalDebug.isDebugMode ();
    for (final ISimpleURL aCSSPath : PerRequestCSSIncludes.getAllRegisteredCSSIncludeURLsForThisRequest (aConverter,
                                                                                                         bRegularFiles))
      addExternalCSS (aCSSPath.getAsString ());
    for (final ISimpleURL aJSPath : PerRequestJSIncludes.getAllRegisteredJSIncludeURLsForThisRequest (aConverter,
                                                                                                      bRegularFiles))
      addExternalJS (aJSPath.getAsString ());
  }

  /**
   * Success constructor for HC nodes
   * 
   * @param aNode
   *        The response HTML node. May be <code>null</code>.
   */
  protected AjaxDefaultResponse (@Nullable final IHCNode aNode, @Nonnull final IURIToURLConverter aConverter)
  {
    // Do it first
    _addCSSAndJS (aConverter);

    // Now decompose the HCNode itself
    final JSONObject aObj = new JSONObject ();
    if (aNode != null)
    {
      IHCNode aRealNode;
      if (aNode instanceof IHCHasChildren)
      {
        // no need to keepOnDocumentReady stuff as the document is already
        // loaded
        aRealNode = HCSpecialNodeHandler.extractSpecialContent ((IHCHasChildren) aNode, this, false);
      }
      else
        aRealNode = aNode;

      // Serialize remaining node to HTML
      aObj.setStringProperty (PROPERTY_HTML, HCSettings.getAsHTMLStringWithoutNamespaces (aRealNode));
    }
    m_bSuccess = true;
    m_sErrorMessage = null;
    m_aSuccessValue = aObj;
  }

  protected AjaxDefaultResponse (final boolean bSuccess,
                                 @Nullable final String sErrorMessage,
                                 @Nullable final IJSON aSuccessValue,
                                 @Nullable final IURIToURLConverter aConverter)
  {
    m_bSuccess = bSuccess;
    m_sErrorMessage = sErrorMessage;
    m_aSuccessValue = aSuccessValue;
    if (bSuccess)
      _addCSSAndJS (aConverter);
  }

  public static void setDefaultURIToURLConverter (@Nonnull final IURIToURLConverter aConverter)
  {
    if (aConverter == null)
      throw new NullPointerException ("converter");

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
  public static IURIToURLConverter getDefaultURIToURLConverter ()
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

  public boolean isSuccess ()
  {
    return m_bSuccess;
  }

  public boolean isFailure ()
  {
    return !m_bSuccess;
  }

  /**
   * @return In case this is a failure, this field contains the error message.
   *         May be <code>null</code>.
   */
  @Nullable
  public String getErrorMessage ()
  {
    return m_sErrorMessage;
  }

  /**
   * @return In case this is a success, this field contains the success object.
   *         May be <code>null</code>.
   */
  @Nullable
  public IJSON getSuccessValue ()
  {
    return m_aSuccessValue;
  }

  @Nonnull
  public String getSerializedAsJSON (final boolean bIndentAndAlign)
  {
    final JSONObject aAssocArray = new JSONObject ();
    aAssocArray.setBooleanProperty (PROPERTY_SUCCESS, m_bSuccess);
    if (m_bSuccess)
    {
      if (m_aSuccessValue != null)
        aAssocArray.setProperty (PROPERTY_VALUE, m_aSuccessValue);
      if (hasExternalCSSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_CSS, getAllExternalCSSs ());
      if (hasExternalJSs ())
        aAssocArray.setStringListProperty (PROPERTY_EXTERNAL_JS, getAllExternalJSs ());
      if (hasInlineJS ())
        aAssocArray.setStringProperty (PROPERTY_INLINE_JS, getInlineJS ().getJSCode ());
    }
    else
    {
      aAssocArray.setStringProperty (PROPERTY_ERRORMESSAGE, m_sErrorMessage != null ? m_sErrorMessage : "");
    }
    return aAssocArray.getJSONString (bIndentAndAlign);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final AjaxDefaultResponse rhs = (AjaxDefaultResponse) o;
    return m_bSuccess == rhs.m_bSuccess &&
           EqualsUtils.equals (m_sErrorMessage, rhs.m_sErrorMessage) &&
           EqualsUtils.equals (m_aSuccessValue, rhs.m_aSuccessValue);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ())
                            .append (m_bSuccess)
                            .append (m_sErrorMessage)
                            .append (m_aSuccessValue)
                            .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("success", m_bSuccess)
                            .append ("errorMsg", m_sErrorMessage)
                            .append ("successValue", m_aSuccessValue)
                            .toString ();
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess ()
  {
    return createSuccess (getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nonnull final IURIToURLConverter aConverter)
  {
    return createSuccess ((IJSON) null, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nullable final IJSON aSuccessValue)
  {
    return createSuccess (aSuccessValue, getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nullable final IJSON aSuccessValue,
                                                   @Nonnull final IURIToURLConverter aConverter)
  {
    return new AjaxDefaultResponse (true, null, aSuccessValue, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nullable final IHCNode aNode)
  {
    // Use the default converter here
    return createSuccess (aNode, getDefaultURIToURLConverter ());
  }

  @Nonnull
  public static AjaxDefaultResponse createSuccess (@Nullable final IHCNode aNode,
                                                   @Nonnull final IURIToURLConverter aConverter)
  {
    // Special case required
    return new AjaxDefaultResponse (aNode, aConverter);
  }

  @Nonnull
  public static AjaxDefaultResponse createError (@Nullable final String sErrorMessage)
  {
    // No converter needed in case of error!
    return new AjaxDefaultResponse (false, sErrorMessage, null, null);
  }
}
