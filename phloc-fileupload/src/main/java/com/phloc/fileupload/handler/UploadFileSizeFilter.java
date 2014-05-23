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

public class UploadFileSizeFilter implements IUploadFileSizeFilter
{
  private static final long serialVersionUID = 609798685438707770L;
  private final long m_nMaxBytes;

  public UploadFileSizeFilter (final long nMaxBytes)
  {
    m_nMaxBytes = nMaxBytes;
  }

  public boolean matchesFilter (final Long aSizeInBytes)
  {
    return m_nMaxBytes < 0 || aSizeInBytes == null || aSizeInBytes.longValue () < this.m_nMaxBytes;
  }

  public long getMaxBytes ()
  {
    return m_nMaxBytes;
  }
}
