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
package com.phloc.appbasics.app.dao.impl;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public class DAOWebIO implements IDAOIO
{
  public DAOWebIO ()
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
  public IReadableResource getReadableResource (@Nonnull final String sFilename)
  {
    return WebIO.getReadableResource (sFilename);
  }

  public void renameFile (@Nonnull final String sSrcFilename, @Nonnull final String sDstFilename)
  {
    if (WebIO.resourceExists (sSrcFilename))
    {
      // Delete destination file if present
      WebIO.deleteFileIfExisting (sDstFilename);

      // and rename existing file to backup file
      WebIO.renameFile (sSrcFilename, sDstFilename);
    }
  }

  @Nonnull
  public ESuccess saveFile (@Nonnull final String sFilename,
                            @Nonnull final String sContent,
                            @Nonnull final Charset aCharset)
  {
    return WebIO.saveFile (sFilename, sContent, aCharset);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
