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
package com.phloc.webscopes.servlets;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.random.VerySecureRandom;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.URLUtils;
import com.phloc.web.servlet.request.RequestHelper;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Base class for stream and download servlet.
 * 
 * @author Philip Helger
 */
public abstract class AbstractObjectDeliveryServlet extends AbstractUnifiedResponseServlet
{
  protected static final String REQUEST_ATTR_OBJECT_DELIVERY_FILENAME = "$object-delivery.filename";
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractObjectDeliveryServlet.class);

  /**
   * Create a unique value per server startup. This is the ETag value for all
   * resources streamed from this servlet, since it uses only ClassPath
   * resources that may only change upon new initialisation of this class.
   * Therefore the ETag value is calculated only once and used to stream all
   * classpath resources.
   */
  protected static final String ETAG_VALUE_OBJECT_DELIVERY_SERVLET = '"' + Long.toString (VerySecureRandom.getInstance ()
                                                                                                          .nextLong ()) + '"';

  private static final Set <String> s_aDeniedFilenames = new LinkedHashSet <String> ();
  private static final Set <String> s_aDeniedExtensions = new LinkedHashSet <String> ();
  private static final Set <String> s_aAllowedFilenames = new LinkedHashSet <String> ();
  private static final Set <String> s_aAllowedExtensions = new LinkedHashSet <String> ();
  private static boolean s_bDeniedAllExtensions = false;
  private static boolean s_bAllowedAllExtensions = false;

  @Nonnull
  private static String _unifyItem (@Nonnull final String sItem)
  {
    return sItem.toLowerCase (Locale.US);
  }

  /**
   * Helper function to convert the configuration string to a collection.
   * 
   * @param aSet
   *        The set to be filled. May not be <code>null</code>.
   * @param sItemList
   *        The string to be separated to a list. Each item is separated by a
   *        ",".
   * @param bUnify
   *        To unify the found item by converting them all to lowercase. This
   *        makes only sense for file extensions but not for file names. This
   *        unification is only relevant because of the incasesensitive file
   *        system on Windows machines.
   */
  private static void _asSet (@Nonnull final Set <String> aSet, @Nullable final String sItemList, final boolean bUnify)
  {
    if (StringHelper.hasText (sItemList))
      for (final String sItem : StringHelper.getExploded (',', sItemList))
      {
        String sRealItem = sItem.trim ();
        if (bUnify)
          sRealItem = _unifyItem (sRealItem);

        // Add only non-empty items
        if (StringHelper.hasText (sRealItem))
          aSet.add (sRealItem);
      }
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected final void onInit ()
  {
    _asSet (s_aDeniedFilenames, getInitParameter ("deniedFilenames"), false);
    _asSet (s_aDeniedExtensions, getInitParameter ("deniedExtensions"), true);
    _asSet (s_aAllowedFilenames, getInitParameter ("allowedFilenames"), false);
    _asSet (s_aAllowedExtensions, getInitParameter ("allowedExtensions"), true);
    s_bDeniedAllExtensions = s_aDeniedExtensions.contains ("*");
    s_bAllowedAllExtensions = s_aAllowedExtensions.contains ("*");

    if (s_aLogger.isDebugEnabled ())
    {
      s_aLogger.debug ("Settings: deniedFilenames=" +
                       s_aDeniedFilenames +
                       "; deniedExtensions=" +
                       s_aDeniedExtensions +
                       "; allowedFilenames=" +
                       s_aAllowedFilenames +
                       "; allowedExtension=" +
                       s_aAllowedExtensions);
    }

    // Short hint, as this may render the whole servlet senseless...
    if (s_bDeniedAllExtensions)
      s_aLogger.warn ("All extension have been denied. This means that this servlet will deny to delivery any resource!");
  }

  private static boolean _isValidFilename (@Nullable final String sRelativeFilename)
  {
    final String sFilename = FilenameHelper.getWithoutPath (sRelativeFilename);
    final String sUnifiedExt = _unifyItem (FilenameHelper.getExtension (sFilename));

    // Denial has precedence
    if (s_aDeniedFilenames.contains (sFilename))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Denied object with name '" + sFilename + "' because it is in the denied filenames list");
      return false;
    }

    if (s_bDeniedAllExtensions || s_aDeniedExtensions.contains (sUnifiedExt))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Denied object with name '" + sFilename + "' because it is in the denied extension list");
      return false;
    }

    // Allowance comes next
    if (s_aAllowedFilenames.contains (sFilename))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Allowed object with name '" + sFilename + "' because it is in the allowed filenames list");
      return true;
    }

    if (s_bAllowedAllExtensions || s_aAllowedExtensions.contains (sUnifiedExt))
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Allowed object with name '" + sFilename + "' because it is in the allowed extension list");
      return true;
    }

    // Neither denied nor allowed -> deny for the sake of security
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Denied object with name '" + sFilename + "' because it is neither denied nor allowed");
    return false;
  }

  private static boolean _isPossibleDirectoryTraversalRequest (@Nonnull final String sFilename)
  {
    return sFilename.indexOf ("/..") >= 0 ||
           sFilename.indexOf ("../") >= 0 ||
           sFilename.indexOf ("\\..") >= 0 ||
           sFilename.indexOf ("..\\") >= 0;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  protected EContinue initRequestState (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                        @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    // cut the leading "/"
    final String sFilename = URLUtils.urlDecode (RequestHelper.getPathWithinServlet (aRequestScope.getRequest ()));

    if (StringHelper.hasNoText (sFilename) ||
        !_isValidFilename (sFilename) ||
        _isPossibleDirectoryTraversalRequest (sFilename))
    {
      // Send the same error code as if it is simply not found to confuse
      // attackers :)
      s_aLogger.warn ("Illegal delivery request '" + sFilename + "'");
      aUnifiedResponse.setStatus (HttpServletResponse.SC_NOT_FOUND);
      return EContinue.BREAK;
    }

    // Filename seems to be safe
    aRequestScope.setAttribute (REQUEST_ATTR_OBJECT_DELIVERY_FILENAME, sFilename);
    return EContinue.CONTINUE;
  }

  @Override
  @Nullable
  protected final String getSupportedETag (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    return ETAG_VALUE_OBJECT_DELIVERY_SERVLET;
  }

  protected abstract void onDeliverResource (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                                             @Nonnull UnifiedResponse aUnifiedResponse,
                                             @Nonnull String sFilename) throws IOException;

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException, IOException
  {
    // The request has been checked and the filename is valid for delivery
    final String sFilename = aRequestScope.getAttributeAsString (REQUEST_ATTR_OBJECT_DELIVERY_FILENAME);
    onDeliverResource (aRequestScope, aUnifiedResponse, sFilename);

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Delivered object with name '" + sFilename + "'");
  }
}
