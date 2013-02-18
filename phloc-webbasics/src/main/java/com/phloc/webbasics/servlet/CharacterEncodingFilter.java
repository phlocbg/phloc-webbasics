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
package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.charset.CharsetManager;
import com.phloc.commons.string.StringParser;

/**
 * Special servlet filter that applies a certain encoding to a request and a
 * response.
 * 
 * @author philip
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

  private String m_sEncoding = DEFAULT_ENCODING;
  private boolean m_bForceEncoding = DEFAULT_FORCE_ENCODING;

  @OverrideOnDemand
  @Nonnull
  @Nonempty
  protected String getEncoding ()
  {
    return m_sEncoding;
  }

  @OverrideOnDemand
  protected boolean isForceEncoding ()
  {
    return m_bForceEncoding;
  }

  public void init (@Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    // encoding
    final String sEncoding = aFilterConfig.getInitParameter (FILTER_INITPARAM_ENCODING);
    if (sEncoding != null)
    {
      // Throws IllegalArgumentException in case it is unknown
      CharsetManager.getCharsetFromName (sEncoding);
      m_sEncoding = sEncoding;
    }

    // force encoding?
    final String sForceEncoding = aFilterConfig.getInitParameter (FILTER_INITPARAM_FORCE_ENCODING);
    if (sForceEncoding != null)
      m_bForceEncoding = StringParser.parseBool (sForceEncoding);
  }

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        @Nonnull final FilterChain aChain) throws IOException, ServletException
  {
    if (aRequest.getAttribute (REQUEST_ATTR) == null)
    {
      final String sEncoding = getEncoding ();
      // We need this for all form data etc.
      if (aRequest.getCharacterEncoding () == null || isForceEncoding ())
        aRequest.setCharacterEncoding (sEncoding);
      aResponse.setCharacterEncoding (sEncoding);
      aRequest.setAttribute (REQUEST_ATTR, Boolean.TRUE);
    }

    // Next filter in the chain
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
