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
package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.URLUtils;
import com.phloc.scopes.web.domain.IRequestWebScope;
import com.phloc.scopes.web.servlet.AbstractScopeAwareHttpServlet;
import com.phloc.webbasics.web.RequestHelper;

/**
 * Base class for stream and download servlet.
 * 
 * @author philip
 */
public abstract class AbstractObjectDeliveryServlet extends AbstractScopeAwareHttpServlet
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractObjectDeliveryServlet.class);

  private Set <String> m_aAllowedExtensions;
  private Set <String> m_aDeniedExtensions;

  @Nonnull
  private static String _unifyExtension (@Nonnull final String sExt)
  {
    return sExt.toLowerCase (Locale.US);
  }

  /**
   * Helper function to convert the configuration string to a collection.
   * 
   * @param sExtensionList
   *        The string to be separated to a list. Each item is separated by a
   *        ",".
   */
  @Nonnull
  private static Set <String> _asSet (@Nullable final String sExtensionList)
  {
    final Set <String> ret = new HashSet <String> ();
    if (StringHelper.hasText (sExtensionList))
      for (final String sExtension : StringHelper.getExploded (',', sExtensionList))
        ret.add (_unifyExtension (sExtension.trim ()));
    return ret;
  }

  @Override
  protected final void onInit ()
  {
    m_aAllowedExtensions = _asSet (getInitParameter ("allowedExtensions"));
    m_aDeniedExtensions = _asSet (getInitParameter ("deniedExtensions"));
  }

  private boolean _hasValidExtension (@Nullable final String sFilename)
  {
    final String sExt = _unifyExtension (FilenameHelper.getExtension (sFilename));
    if (m_aDeniedExtensions.contains (sExt) || m_aDeniedExtensions.contains ("*"))
      return false;
    return m_aAllowedExtensions.contains (sExt) || m_aAllowedExtensions.contains ("*");
  }

  private static boolean _isPossibleDirectoryTraversalRequest (@Nonnull final String sFilename)
  {
    return sFilename.indexOf ("/..") >= 0 ||
           sFilename.indexOf ("../") >= 0 ||
           sFilename.indexOf ("\\..") >= 0 ||
           sFilename.indexOf ("..\\") >= 0;
  }

  protected abstract void onDeliverResource (@Nonnull HttpServletRequest aHttpRequest,
                                             @Nonnull HttpServletResponse aHttpResponse,
                                             @Nonnull String sFilename) throws IOException;

  private void _handle (@Nonnull final HttpServletRequest aHttpRequest, @Nonnull final HttpServletResponse aHttpResponse) throws IOException
  {
    // cut the leading "/"
    final String sFilename = URLUtils.urlDecode (RequestHelper.getPathWithinServlet (aHttpRequest));

    if (StringHelper.hasText (sFilename) &&
        _hasValidExtension (sFilename) &&
        !_isPossibleDirectoryTraversalRequest (sFilename))
    {
      onDeliverResource (aHttpRequest, aHttpResponse, sFilename);
    }
    else
    {
      // Send the same error code as if it is simply not found to confuse
      // attackers :)
      s_aLogger.warn ("Illegal delivery request '" + sFilename + "'");
      aHttpResponse.sendError (HttpServletResponse.SC_NOT_FOUND, sFilename);
    }
  }

  @Override
  protected final void onGet (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final HttpServletResponse aHttpResponse,
                              @Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    _handle (aHttpRequest, aHttpResponse);
  }

  @Override
  protected final void onPost (@Nonnull final HttpServletRequest aHttpRequest,
                               @Nonnull final HttpServletResponse aHttpResponse,
                               @Nonnull final IRequestWebScope aRequestScope) throws IOException
  {
    _handle (aHttpRequest, aHttpResponse);
  }
}
