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

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.IInputStreamProvider;

/**
 * This interface represents attachments to be added to a mail message. Messages
 * with attachments are always send as MIME messages.
 * 
 * @author philip
 */
public interface IEmailAttachment extends IInputStreamProvider
{
  /**
   * @return The filename of the attachment
   */
  @Nonnull
  @Nonempty
  String getFilename ();

  /**
   * @return The object holding the input stream to the data.
   */
  @Nonnull
  IInputStreamProvider getInputStreamProvider ();

  /**
   * @return The content type (MIME type) of the attachment
   */
  @Nullable
  String getContentType ();

  /**
   * @return The attachment as a {@link DataSource}.
   */
  @Nonnull
  DataSource getAsDataSource ();
}
