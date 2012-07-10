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
package com.phloc.appbasics.app;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.EAppend;
import com.phloc.commons.io.IReadWriteResource;
import com.phloc.commons.io.file.FileOperationManager;
import com.phloc.commons.io.file.IFileOperationManager;
import com.phloc.commons.io.file.LoggingFileOperationCallback;
import com.phloc.commons.io.resource.FileSystemResource;
import com.phloc.commons.string.StringHelper;

/**
 * Abstract for accessing files inside the web application
 * 
 * @author philip
 */
@Immutable
public final class WebFileIO
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (WebFileIO.class);
  private static final IFileOperationManager s_aFOM = new FileOperationManager (new LoggingFileOperationCallback ());

  private static File s_aBasePath;

  private WebFileIO ()
  {}

  public static boolean isBasePathInited ()
  {
    return s_aBasePath != null;
  }

  public static void initBasePath (@Nonnull final File aBasePath)
  {
    if (aBasePath == null)
      throw new NullPointerException ("basePath");
    if (s_aBasePath != null)
      throw new IllegalStateException ("Another base path is already present: " + s_aBasePath);

    s_aLogger.info ("Using '" + aBasePath + "' as the storage base");
    s_aBasePath = aBasePath;

    // Ensure the base directory is present
    s_aFOM.createDirRecursiveIfNotExisting (s_aBasePath);
  }

  public static void initBasePath (@Nonnull @Nonempty final String sBasePath)
  {
    if (StringHelper.hasNoText (sBasePath))
      throw new IllegalArgumentException ("basePath");
    initBasePath (new File (sBasePath));
  }

  public static void resetBasePath ()
  {
    s_aBasePath = null;
  }

  @Nonnull
  @Nonempty
  public static String getBasePath ()
  {
    return getBasePathFile ().getAbsolutePath ();
  }

  @Nonnull
  public static File getBasePathFile ()
  {
    if (s_aBasePath == null)
      throw new IllegalStateException ("Base path was not initialized!");
    return s_aBasePath;
  }

  @Nonnull
  public static File getFile (@Nonnull final String sPath)
  {
    return new File (s_aBasePath, sPath);
  }

  @Nonnull
  public static IReadWriteResource getResource (@Nonnull final String sPath)
  {
    return new FileSystemResource (getFile (sPath));
  }

  @Nonnull
  public static InputStream getInputStream (@Nonnull final String sPath)
  {
    return getResource (sPath).getInputStream ();
  }

  @Nonnull
  public static OutputStream getOutputStream (@Nonnull final String sBasePathRelativePath,
                                              @Nonnull final EAppend eAppend)
  {
    return getResource (sBasePathRelativePath).getOutputStream (eAppend);
  }

  @Nonnull
  public static IFileOperationManager getFileOpMgr ()
  {
    return s_aFOM;
  }
}
