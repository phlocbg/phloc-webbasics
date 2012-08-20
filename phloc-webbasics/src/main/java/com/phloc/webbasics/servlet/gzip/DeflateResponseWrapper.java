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
package com.phloc.webbasics.servlet.gzip;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * A {@link HttpServletResponseWrapper} implementation that does deflating
 * 
 * @author philip
 */
public class DeflateResponseWrapper extends AbstractResponseWrapper
{
  private final String m_sContentEncoding;

  public DeflateResponseWrapper (@Nonnull final HttpServletResponse aHttpResponse,
                                 @Nonnull final String sContentEncoding)
  {
    super (aHttpResponse);
    m_sContentEncoding = sContentEncoding;
  }

  @Override
  @Nonnull
  protected DeflateServletOutputStream createOutputStream () throws IOException
  {
    return new DeflateServletOutputStream ((HttpServletResponse) getResponse (), m_sContentEncoding);
  }
}
