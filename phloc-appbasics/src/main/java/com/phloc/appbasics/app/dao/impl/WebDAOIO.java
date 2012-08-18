/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.app.dao.impl;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.ESuccess;

public class WebDAOIO implements IDAOIO
{
  public WebDAOIO ()
  {}

  /**
   * @return Overwrite this only if you know what you're doing.
   */
  @Nullable
  public InputStream openInputStream (@Nullable final String sFilename)
  {
    // we're not operating on a file!
    if (sFilename == null)
      return null;

    return getReadableResource (sFilename).getInputStream ();
  }

  @Nonnull
  public IReadableResource getReadableResource (final String sFilename)
  {
    return WebIO.getReadableResource (sFilename);
  }

  public void renameFile (final String sSrcFileName, final String sDstFileName)
  {
    if (WebIO.resourceExists (sSrcFileName))
    {
      // Delete destination file if present
      WebIO.deleteFileIfExisting (sDstFileName);

      // and rename existing file to backup file
      WebIO.renameFile (sSrcFileName, sDstFileName);
    }
  }

  @Nonnull
  public ESuccess saveFile (final String sFilename, final String sContent, final Charset aCharset)
  {
    return WebIO.saveFile (sFilename, sContent, aCharset);
  }
}
