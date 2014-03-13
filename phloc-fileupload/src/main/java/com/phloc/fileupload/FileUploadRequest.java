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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiLinkedHashMapArrayListBased;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.CWebCharset;
import com.phloc.web.fileupload.IFileItem;

/**
 * This class handles the analyzed request data separated by form fields and
 * file uploads.
 * 
 * @author Boris Gregorcic
 */
@Immutable
public final class FileUploadRequest
{
  private final IMultiMapListBased <String, String> m_aFormFields = new MultiLinkedHashMapArrayListBased <String, String> ();
  private final IMultiMapListBased <String, IFileItem> m_aFormFiles = new MultiLinkedHashMapArrayListBased <String, IFileItem> ();

  FileUploadRequest (@Nonnull final List <IFileItem> aParseRequest)
  {
    for (final IFileItem aFileItem : aParseRequest)
    {
      if (aFileItem.isFormField ())
      {
        // We need to explicitly use the charset, as by default only the
        // charset from the content type is used!
        m_aFormFields.putSingle (aFileItem.getFieldName (), aFileItem.getString (CWebCharset.CHARSET_MULTIPART_OBJ));
      }
      else
      {
        m_aFormFiles.putSingle (aFileItem.getFieldName (), aFileItem);
      }
    }
  }

  /**
   * Retrieve an unmodifiable collection of all form fields.
   * 
   * @return A non-<code>null</code>, unmodifiable map of parameters.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, List <String>> getFormFields ()
  {
    return ContainerHelper.newMap (m_aFormFields);
  }

  /**
   * Retrieve an unmodifiable collection of all form file uploads.
   * 
   * @return A non-<code>null</code>, unmodifiable map of file uploads.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, List <IFileItem>> getFormFiles ()
  {
    return ContainerHelper.newMap (m_aFormFiles);
  }

  /**
   * Retrieve an unmodifiable collection of all file items.
   * 
   * @return A non-<code>null</code>, unmodifiable collection of
   *         {@link IFileItem} objects.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <IFileItem> getFormFileItems ()
  {
    final List <IFileItem> ret = new ArrayList <IFileItem> ();
    for (final List <IFileItem> aList : m_aFormFiles.values ())
      ret.addAll (aList);
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fields", m_aFormFields).append ("fileItems", m_aFormFiles).toString ();
  }
}
