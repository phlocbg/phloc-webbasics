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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.name.IHasDisplayText;

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
    ValueEnforcer.notNull (aValidExtensions, "ValidExtensions"); //$NON-NLS-1$
    for (final String sExtension : aValidExtensions)
      if (sExtension.indexOf ('.') >= 0)
        throw new IllegalArgumentException ("Extension may not contain a dot: " + sExtension); //$NON-NLS-1$
    ValueEnforcer.notEmpty (sErrorCode, "ErrorCode must not be null or empty!"); //$NON-NLS-1$
    ValueEnforcer.notNull (aErrorMessage, "ErrorMessage"); //$NON-NLS-1$
    m_aValidExtensions = ContainerHelper.newSet (aValidExtensions);
    m_sErrorCode = sErrorCode;
    m_aErrorMessage = aErrorMessage;
    m_aErrorArguments = ContainerHelper.newList (aErrorArguments);
  }

  public boolean matchesFilter (final String sFilename)
  {
    // no limitation means everything is valid!
    if (m_aValidExtensions.isEmpty ())
      return true;

    final String sExt = FilenameHelper.getExtension (FilenameHelper.getSecureFilename (sFilename));
    for (final String sValidExtension : m_aValidExtensions)
      if (sValidExtension.equalsIgnoreCase (sExt))
        return true;
    return false;
  }

  @Nonnull
  @Nonempty
  public String getErrorCode ()
  {
    return m_sErrorCode;
  }

  @Nonnull
  public IHasDisplayText getErrorMessage ()
  {
    return m_aErrorMessage;
  }

  @ReturnsMutableCopy
  public List <String> getErrorArguments ()
  {
    return ContainerHelper.newList (m_aErrorArguments);
  }
}
