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
package com.phloc.web.smtp.attachment;

import java.util.List;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.ReturnsMutableCopy;

/**
 * This interface represents attachments to be added to a mail message.
 * 
 * @author philip
 */
public interface IReadonlyEmailAttachmentList extends IHasSize
{
  /**
   * Check if an attachment for the passed file name is contained.
   * 
   * @param sFilename
   *        The file name to be checked. The file name is case sensitive.
   * @return <code>true</code> if such an attachment is present,
   *         <code>false</code> otherwise.
   */
  boolean containsAttachment (@Nullable String sFilename);

  /**
   * @return A list with all registered file names or an empty list.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllAttachmentFilenames ();

  /**
   * @return A list with all registered attachments or an empty list.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <IEmailAttachment> getAllAttachments ();

  /**
   * @return A list of all attachments as Java Activation DataSource objects.
   */
  @Nonnull
  @ReturnsMutableCopy
  List <DataSource> getAsDataSourceList ();
}
