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
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.url.SMap;

/**
 * This is an interface for upload post processors which perform additional
 * tasks on an uploaded file.
 * 
 * @author Boris Gregorcic
 */
public interface IUploadPostProcessor extends Serializable
{
  /**
   * This method is called after successful upload. The post processor can
   * perform its tasks and finally return whether or not the file should be
   * deleted.
   * 
   * @param aUploadedFile
   *        the uploaded file to be post processed
   * @param sSourceFilename
   *        The original file name
   * @param aProperties
   *        Properties stored in the upload context (may contain context
   *        specific information)
   * @return the result of the post processing (
   *         {@link UploadPostProcessingResult})
   */
  @Nullable
  UploadPostProcessingResult performPostProcessing (@Nonnull File aUploadedFile,
                                                    @Nonnull String sSourceFilename,
                                                    @Nonnull SMap aProperties);

  /**
   * @return A specific message to be shown while this post processing task is
   *         running. To indicate going on, an alternating "..." string will be
   *         appended to the message automatically.
   */
  @Nonnull
  IHasDisplayText getProgressMessage ();
}
