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
package com.phloc.fileupload.handler;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;

public class UploadFilenameExtensionFilter implements IUploadFilenameFilter
{
  private static final long serialVersionUID = -685513897630446302L;
  private final Set <String> m_aValidExtensions;
  private final String m_sErrorCode;
  private final IHasDisplayText m_aErrorMessage;
  private final List <String> m_aErrorArguments;

  public UploadFilenameExtensionFilter (@Nonnull final Set <String> aValidExtensions,
                                        @Nonnull @Nonempty final String sErrorCode,
                                        @Nonnull final IHasDisplayText aErrorMessage,
                                        @Nullable final List <String> aErrorArguments)
  {
    if (aValidExtensions == null)
    {
      throw new NullPointerException ("aValidExtensions"); //$NON-NLS-1$
    }
    for (final String sExtension : aValidExtensions)
  {
    if (sExtension.indexOf ('.') >= 0)
      {
        throw new IllegalArgumentException ("Extension may not contain a dot: " + sExtension); //$NON-NLS-1$
      }
    }
    if (StringHelper.hasNoText (sErrorCode))
    {
      throw new IllegalArgumentException ("sErrorCode must not be null or empty!"); //$NON-NLS-1$
    }
    if (aErrorMessage == null)
    {
      throw new NullPointerException ("aErrorMessage"); //$NON-NLS-1$
    }
    this.m_aValidExtensions = ContainerHelper.newSet (aValidExtensions);
    this.m_sErrorCode = sErrorCode;
    this.m_aErrorMessage = aErrorMessage;
    this.m_aErrorArguments = ContainerHelper.newList (aErrorArguments);
  }

  @Override
  public boolean matchesFilter (final String sFilename)
  {
    // no limitation means everything is valid!
    if (this.m_aValidExtensions.isEmpty ())
    {
      return true;
    }
    for (final String sValidExtension : this.m_aValidExtensions)
    {
      if (sValidExtension.equalsIgnoreCase (FilenameHelper.getExtension (FilenameHelper.getSecureFilename (sFilename))))
      {
        return true;
      }
    }
    return false;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getErrorCode ()
  {
    return this.m_sErrorCode;
  }
  
  @Override
  @Nonnull
  @Nonempty
  public IHasDisplayText getErrorMessage ()
  {
    return this.m_aErrorMessage;
  }
  
  @Override
  @Nullable
  public List <String> getErrorArguments ()
  {
    return ContainerHelper.newList (this.m_aErrorArguments);
  }
}
