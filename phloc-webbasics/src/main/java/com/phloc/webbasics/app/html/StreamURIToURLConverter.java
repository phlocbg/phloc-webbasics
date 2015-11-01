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
package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.IURIToURLConverter;
import com.phloc.webbasics.IWebURIToURLConverter;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * The default implementation of {@link IURIToURLConverter} that uses
 * {@link LinkUtils#getStreamURL(IRequestWebScopeWithoutResponse, String)} to
 * convert URIs to URLs. If you are using a different stream servlet path, you
 * may need to provide your own implementation and use it in
 * {@link com.phloc.webbasics.ajax.AjaxDefaultResponse}!
 * 
 * @author Philip Helger
 */
@Immutable
public final class StreamURIToURLConverter implements IWebURIToURLConverter
{
  private static final StreamURIToURLConverter s_aInstance = new StreamURIToURLConverter ();

  private StreamURIToURLConverter ()
  {}

  /**
   * @return The default instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static StreamURIToURLConverter getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  public ISimpleURL getAsURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                              @Nonnull @Nonempty final String sURI)
  {
    return LinkUtils.getStreamURL (aRequestScope, sURI);
  }
}
