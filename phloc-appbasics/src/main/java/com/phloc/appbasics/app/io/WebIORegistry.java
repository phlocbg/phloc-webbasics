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
package com.phloc.appbasics.app.io;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.file.FilenameHelper;

/**
 * Wrapper around the registry file handling.
 * 
 * @author philip
 */
@Immutable
public final class WebIORegistry
{
  /**
   * Registry directory name.
   */
  private static final String DIR_REGISTRY = "WEB-INF/registry/";

  private WebIORegistry ()
  {}

  @Nullable
  public static File getRegistryDirectoryFile ()
  {
    return WebIO.getDirectoryFile (DIR_REGISTRY);
  }

  /**
   * Get the registry path of the given plugin.
   * 
   * @param sDirectoryName
   *        The name of the directory. May not be <code>null</code>.
   * @return <code>WEB-INF/registry/<em>dirName</em>/</code>
   */
  @Nonnull
  public static String getRegistryDirectoryName (@Nonnull final String sDirectoryName)
  {
    return getRegistryDirectoryName (sDirectoryName, false);
  }

  @Nonnull
  public static String getRegistryDirectoryName (@Nonnull final String sDirectoryName, final boolean bCreateDirOnDemand)
  {
    if (!FilenameHelper.isValidFilenameWithPaths (sDirectoryName))
      throw new IllegalArgumentException ("Illegal file name passed: " + sDirectoryName);
    final String sDir = DIR_REGISTRY + sDirectoryName + '/';
    if (bCreateDirOnDemand)
      WebIO.mkDir (sDir, true);
    return sDir;
  }

  /**
   * @param sFilename
   *        The file name to use. May neither be <code>null</code> nor empty.
   * @return <code>WEB-INF/registry/<em>fileName</em></code>
   */
  @Nonnull
  public static String getRegistryFilename (@Nonnull final String sFilename)
  {
    if (!FilenameHelper.isValidFilename (sFilename))
      throw new IllegalArgumentException ("Illegal file name passed!");

    return DIR_REGISTRY + sFilename;
  }

  /**
   * @param sDirectoryName
   *        Directory name
   * @param sFilename
   *        File name
   * @return <code>WEB-INF/registry/<em>dirName</em>/<em>fileName</em></code>
   */
  @Nonnull
  public static String getRegistryFilename (@Nonnull final String sDirectoryName, @Nonnull final String sFilename)
  {
    return getRegistryFilename (sDirectoryName, sFilename, false);
  }

  @Nonnull
  public static String getRegistryFilename (@Nonnull final String sDirectoryName,
                                            @Nonnull final String sFilename,
                                            final boolean bCreateDirOnDemand)
  {
    if (!FilenameHelper.isValidFilename (sFilename))
      throw new IllegalArgumentException ("Illegal filename passed!");

    return getRegistryDirectoryName (sDirectoryName, bCreateDirOnDemand) + sFilename;
  }

  @Nonnull
  public static IReadableResource getReadableRegistryResource (@Nonnull final String sDirectoryName,
                                                               @Nonnull final String sFilename)
  {
    return getReadableRegistryResource (sDirectoryName, sFilename, false);
  }

  @Nonnull
  public static IReadableResource getReadableRegistryResource (@Nonnull final String sDirectoryName,
                                                               @Nonnull final String sFilename,
                                                               final boolean bCreateDirOnDemand)
  {
    return WebIO.getReadableResource (getRegistryFilename (sDirectoryName, sFilename, bCreateDirOnDemand));
  }

  @Nonnull
  public static IWritableResource getWritableRegistryResource (@Nonnull final String sDirectoryName,
                                                               @Nonnull final String sFilename)
  {
    return getWritableRegistryResource (sDirectoryName, sFilename, false);
  }

  @Nonnull
  public static IWritableResource getWritableRegistryResource (@Nonnull final String sDirectoryName,
                                                               @Nonnull final String sFilename,
                                                               final boolean bCreateDirOnDemand)
  {
    return WebIO.getWritableResource (getRegistryFilename (sDirectoryName, sFilename, bCreateDirOnDemand));
  }
}
