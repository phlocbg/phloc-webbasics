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

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.phloc.commons.charset.CCharset;

/**
 * Special servlet filter that applies a certain encoding to a request and a
 * response.
 * 
 * @author philip
 */
public class CharacterEncodingFilter implements Filter
{
  private static final String s_sEncoding = CCharset.CHARSET_UTF_8;

  public void init (final FilterConfig aFilterConfig) throws ServletException
  {}

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        @Nonnull final FilterChain aChain) throws IOException, ServletException
  {
    // We need this for all form data etc.
    if (aRequest.getCharacterEncoding () == null)
      aRequest.setCharacterEncoding (s_sEncoding);
    aResponse.setCharacterEncoding (s_sEncoding);
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
