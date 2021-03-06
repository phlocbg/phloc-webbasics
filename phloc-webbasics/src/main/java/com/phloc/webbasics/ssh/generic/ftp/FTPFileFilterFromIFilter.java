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
package com.phloc.webbasics.ssh.generic.ftp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPFileFilters;

import com.phloc.commons.filter.IFilter;

public final class FTPFileFilterFromIFilter implements FTPFileFilter
{
  private final IFilter <FTPFile> m_aFilter;

  public FTPFileFilterFromIFilter (@Nonnull final IFilter <FTPFile> aFilter)
  {
    if (aFilter == null)
      throw new NullPointerException ("filter");
    m_aFilter = aFilter;
  }

  public boolean accept (final FTPFile aFile)
  {
    return m_aFilter.matchesFilter (aFile);
  }

  @Nonnull
  public static FTPFileFilter create (@Nullable final IFilter <FTPFile> aFilter)
  {
    return aFilter == null ? FTPFileFilters.NON_NULL : new FTPFileFilterFromIFilter (aFilter);
  }
}
