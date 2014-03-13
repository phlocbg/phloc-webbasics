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
package com.phloc.fileupload;

import java.io.File;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.phloc.web.fileupload.io.DiskFileItem;
import com.phloc.web.fileupload.io.DiskFileItemFactory;

/**
 * A special {@link DiskFileItemFactory} that updates the {@link FileUploadProgressListener} with
 * the correct file name.
 * 
 * @author Boris Gregorcic
 */
public final class NotifyingDiskFileItemFactory extends DiskFileItemFactory
{
  public NotifyingDiskFileItemFactory (@Nonnegative final int nSizeThreshold,
                                       @Nullable final File aRepository)
  {
    super (nSizeThreshold, aRepository);
  }
  
  @Override
  @Nullable
  public DiskFileItem createItem (final String sFieldName,
                                  final String sContentType,
                                  final boolean bIsFormField,
                                  final String sFileName)
  {
    // Main creation
    final DiskFileItem aFileItem = super.createItem (sFieldName,
                                                     sContentType,
                                                     bIsFormField,
                                                     sFileName);
    
    // Inform update listener (only cares about files!)
    if (!aFileItem.isFormField ())
    {
      FileUploadProgressListener.getInstance ().setFileItem (aFileItem);
    }
    return aFileItem;
  }
}
