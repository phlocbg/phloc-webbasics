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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.FileSystemResource;

/**
 * Abstract for accessing files inside the web application
 * 
 * @author philip
 */
@Immutable
public final class WebFileIO
{
  private static final String WEBINF_REGISTRY = "WEB-INF/registry/";
  private static String s_sRealPath;

  private WebFileIO ()
  {}

  public static void initBaseRealPath (final String sRealPath)
  {
    if (s_sRealPath != null)
      throw new IllegalStateException ("another real path is already present!");
    s_sRealPath = sRealPath;
  }

  @Nonnull
  public static File getFile (final String sPath)
  {
    return new File (s_sRealPath, sPath);
  }

  @Nonnull
  public static IReadableResource getRealPathRelativeInputStreamProvider (final String sPath)
  {
    return new FileSystemResource (getFile (sPath));
  }

  @Nonnull
  public static InputStream getRealPathRelativeInputStream (final String sPath)
  {
    return getRealPathRelativeInputStreamProvider (sPath).getInputStream ();
  }

  @Nonnull
  public static InputStream getRegistryInputStream (final String sPath)
  {
    return getRealPathRelativeInputStream (WEBINF_REGISTRY + sPath);
  }

  @Nonnull
  public static File getRegistryFile (final String sPath)
  {
    return getFile (WEBINF_REGISTRY + sPath);
  }
}
