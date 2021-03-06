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
package com.phloc.webbasics.ssh.ftp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;

public final class FTPFileFilterFileEndsWith implements FTPFileFilter
{
  private final String m_sEnd;

  public FTPFileFilterFileEndsWith (@Nonnull @Nonempty final String sEnd)
  {
    m_sEnd = ValueEnforcer.notEmpty (sEnd, "end");
  }

  public boolean accept (@Nullable final FTPFile aFile)
  {
    return aFile != null && aFile.isFile () && aFile.getName ().endsWith (m_sEnd);
  }
}
