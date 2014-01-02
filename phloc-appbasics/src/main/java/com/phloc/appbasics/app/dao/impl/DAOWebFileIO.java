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
package com.phloc.appbasics.app.dao.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.appbasics.app.dao.IDAOIO;
import com.phloc.appbasics.app.io.PathRelativeFileIO;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.file.FileOperationManager;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.string.ToStringGenerator;

@Immutable
public final class DAOWebFileIO implements IDAOIO
{
  private static final DAOWebFileIO s_aInstance = new DAOWebFileIO ();

  private DAOWebFileIO ()
  {}

  @Nonnull
  public static DAOWebFileIO getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  private static PathRelativeFileIO _getIO ()
  {
    return WebFileIO.getDataIO ();
  }

  @Nonnull
  private static FileOperationManager _getFOM ()
  {
    return WebFileIO.getFileOpMgr ();
  }

  /**
   * @return Overwrite this only if you know what you're doing.
   */
  @Nullable
  public InputStream openInputStream (@Nullable final String sFilename)
  {
    // we're not operating on a file!
    if (sFilename == null)
      return null;

    return _getIO ().getInputStream (sFilename);
  }

  @Nonnull
  public IReadableResource getReadableResource (@Nonnull final String sFilename)
  {
    return _getIO ().getResource (sFilename);
  }

  public void renameFile (@Nonnull final String sSrcFileName, @Nonnull final String sDstFileName)
  {
    final PathRelativeFileIO aIO = _getIO ();
    final File aSrcFile = aIO.getFile (sSrcFileName);
    final File aDstFile = aIO.getFile (sDstFileName);
    if (FileUtils.existsFile (aSrcFile))
    {
      // Delete destination file if present
      if (_getFOM ().deleteFileIfExisting (aDstFile).isSuccess ())
      {
        // and rename existing file to backup file
        _getFOM ().renameFile (aSrcFile, aDstFile);
      }
    }
  }

  @Nonnull
  public ESuccess saveFile (@Nonnull final String sFilename,
                            @Nonnull final String sContent,
                            @Nonnull final Charset aCharset)
  {
    return _getIO ().saveFile (sFilename, sContent, aCharset);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
