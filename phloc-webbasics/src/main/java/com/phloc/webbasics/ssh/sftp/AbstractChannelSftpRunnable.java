/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webbasics.ssh.sftp;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;

/**
 * Abstract implementation of the {@link IChannelSftpRunnable} interface
 * encapsulating the display name.
 *
 * @author philip
 */
public abstract class AbstractChannelSftpRunnable implements IChannelSftpRunnable
{
  private final String m_sDisplayName;

  public AbstractChannelSftpRunnable (@Nonnull @Nonempty final String sDisplayName)
  {
    if (StringHelper.hasNoText (sDisplayName))
      throw new IllegalArgumentException ("name may not be empty!");
    m_sDisplayName = sDisplayName;
  }

  @Nonnull
  @Nonempty
  public final String getDisplayName ()
  {
    return m_sDisplayName;
  }
}
