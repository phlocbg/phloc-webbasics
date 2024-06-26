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
package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.string.StringParser;

/**
 * Special servlet filter that applies a certain encoding to a request and a
 * response.
 * 
 * @author Philip Helger
 */
public class CharacterEncodingFilter implements Filter
{
  /** Name of the init parameter for the encoding */
  public static final String FILTER_INITPARAM_ENCODING = "encoding";
  /** Name of the init parameter to force setting the encoding */
  public static final String FILTER_INITPARAM_FORCE_ENCODING = "forceEncoding";
  /** The default encoding is UTF-8 */
  public static final String DEFAULT_ENCODING = CCharset.CHARSET_UTF_8;
  /** By default the encoding is not enforced. */
  public static final boolean DEFAULT_FORCE_ENCODING = false;
  private static final String REQUEST_ATTR = CharacterEncodingFilter.class.getName ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (CharacterEncodingFilter.class);

  private String m_sEncoding = DEFAULT_ENCODING;
  private boolean m_bForceEncoding = DEFAULT_FORCE_ENCODING;

  /**
   * @return The encoding to be used by this filter. Neither <code>null</code>
   *         nor empty.
   */
  @OverrideOnDemand
  @Nonnull
  @Nonempty
  protected String getEncoding ()
  {
    return this.m_sEncoding;
  }

  @OverrideOnDemand
  protected boolean isForceEncoding ()
  {
    return this.m_bForceEncoding;
  }

  @Override
  public void init (@Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    // encoding
    final String sEncoding = aFilterConfig.getInitParameter (FILTER_INITPARAM_ENCODING);
    if (sEncoding != null)
    {
      // Throws IllegalArgumentException in case it is unknown
      CharsetManager.getCharsetFromName (sEncoding);
      this.m_sEncoding = sEncoding;
    }

    // force encoding?
    final String sForceEncoding = aFilterConfig.getInitParameter (FILTER_INITPARAM_FORCE_ENCODING);
    if (sForceEncoding != null)
      this.m_bForceEncoding = StringParser.parseBool (sForceEncoding);
  }

  @Override
  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        @Nonnull final FilterChain aChain) throws IOException, ServletException
  {
    // Avoid double filtering
    if (aRequest.getAttribute (REQUEST_ATTR) == null)
    {
      final String sEncoding = getEncoding ();
      final String sOldRequestEncoding = aRequest.getCharacterEncoding ();
      // We need this for all form data etc.
      if (sOldRequestEncoding == null || isForceEncoding ())
      {
        aRequest.setCharacterEncoding (sEncoding);
        if (sOldRequestEncoding != null && !sEncoding.equalsIgnoreCase (sOldRequestEncoding))
        {
          s_aLogger.info ("Changed request encoding from '{}' to '{}'", sOldRequestEncoding, sEncoding);
        }
      }
      aResponse.setCharacterEncoding (sEncoding);
      aRequest.setAttribute (REQUEST_ATTR, Boolean.TRUE);
    }

    // Next filter in the chain
    aChain.doFilter (aRequest, aResponse);
  }

  @Override
  public void destroy ()
  {}
}
