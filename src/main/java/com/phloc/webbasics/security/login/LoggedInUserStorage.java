package com.phloc.webbasics.security.login;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.web.WebFileIO;

public final class LoggedInUserStorage
{
  private LoggedInUserStorage ()
  {}

  @Nonnull
  private static String _getLoggedInUserID ()
  {
    final String ret = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (StringHelper.hasNoText (ret))
      throw new IllegalStateException ("No user is logged in!");
    return ret;
  }

  /**
   * @return The base directory for all user-related data
   */
  @Nonnull
  public static File getUserdataDirectory ()
  {
    final File aDir = WebFileIO.getFile ("userdata/" + _getLoggedInUserID ());
    WebFileIO.getFileOpMgr ().createDirRecursiveIfNotExisting (aDir);
    return aDir;
  }

  /**
   * @return The upload directory of the current user.
   */
  @Nonnull
  public static File getUploadDirectory ()
  {
    final File aDir = new File (getUserdataDirectory (), "upload");
    WebFileIO.getFileOpMgr ().createDirIfNotExisting (aDir);
    return aDir;
  }
}
