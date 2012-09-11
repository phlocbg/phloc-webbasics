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

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.file.SimpleFileIO;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public class DAOWebFileIO implements IDAOIO
{
  public DAOWebFileIO ()
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
    return WebFileIO.getResource (sFilename);
  }

  public void renameFile (final String sSrcFileName, final String sDstFileName)
  {
    final File aSrcFile = WebFileIO.getFile (sSrcFileName);
    final File aDstFile = WebFileIO.getFile (sDstFileName);
    if (FileUtils.existsFile (aSrcFile))
    {
      // Delete destination file if present
      if (WebIO.getFileOpMgr ().deleteFileIfExisting (aDstFile).isSuccess ())
      {
        // and rename existing file to backup file
        WebIO.getFileOpMgr ().renameFile (aSrcFile, aDstFile);
      }
    }
  }

  @Nonnull
  public ESuccess saveFile (final String sFilename, final String sContent, final Charset aCharset)
  {
    return SimpleFileIO.writeFile (WebFileIO.getFile (sFilename), sContent, aCharset);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
