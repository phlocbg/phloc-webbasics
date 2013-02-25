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
package com.phloc.webbasics.servlet.gzip;

import javax.annotation.Nonnull;
import javax.servlet.FilterConfig;

import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This is a generic filter that first tries to find whether "GZip" is
 * supported, and if this fails, whether "Deflate" is supported. If none is
 * supported, no compression will happen in this filter.
 * 
 * @author philip
 */
@Deprecated
@SuppressFBWarnings ("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public final class CompressFilter extends com.phloc.web.servlets.filter.CompressFilter
{
  @Override
  public void init (@Nonnull final FilterConfig aFilterConfig)
  {
    LoggerFactory.getLogger (CompressFilter.class)
                 .error ("This implementation of CompressFilter is deprecated! Use com.phloc.web.servlets.filter.CompressFilter instead!");
    super.init (aFilterConfig);
  }
}
