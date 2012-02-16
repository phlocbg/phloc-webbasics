package com.phloc.webbasics.servlet;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.FileSystemResource;

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
