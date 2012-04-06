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
package com.phloc.webbasics.servlet;

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

  private static final String WEBINF_REGISTRY = "WEB-INF/registry/";
  private static String s_sBasePath;

  private WebFileIO ()
  {}

  public static boolean isBasePathInited ()
  {
    return s_sBasePath != null;
  }

  public static void initBasePath (@Nonnull @Nonempty final String sBasePath)
  {
    if (StringHelper.hasNoText (sBasePath))
      throw new IllegalArgumentException ("basePath");
    if (s_sBasePath != null)
      throw new IllegalStateException ("Another base path is already present: " + s_sBasePath);

    s_aLogger.info ("Using '" + sBasePath + "' as the storage base");
    s_sBasePath = sBasePath;

    // Ensure the base directory is present
    s_aFOM.createDirRecursiveIfNotExisting (new File (s_sBasePath));
  }

  @Nonnull
  @Nonempty
  public static String getBasePath ()
  {
    if (s_sBasePath == null)
      throw new IllegalStateException ("Base path was not initialized!");
    return s_sBasePath;
  }

  @Nonnull
  public static File getFile (final String sPath)
  {
    return new File (s_sBasePath, sPath);
  }

  @Nonnull
  public static IReadWriteResource getResource (final String sPath)
  {
    return new FileSystemResource (getFile (sPath));
  }

  @Nonnull
  public static InputStream getInputStream (final String sPath)
  {
    return getResource (sPath).getInputStream ();
  }

  @Nonnull
  public static OutputStream getOutputStream (final String sBasePathRelativePath, @Nonnull final EAppend eAppend)
  {
    return getResource (sBasePathRelativePath).getOutputStream (eAppend);
  }

  @Nonnull
  public static IFileOperationManager getFileOpMgr ()
  {
    return s_aFOM;
  }

  @Nonnull
  public static InputStream getRegistryInputStream (final String sPath)
  {
    return getInputStream (WEBINF_REGISTRY + sPath);
  }

  @Nonnull
  public static File getRegistryFile (final String sPath)
  {
    return getFile (WEBINF_REGISTRY + sPath);
  }
}
