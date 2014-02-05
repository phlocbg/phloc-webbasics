/**
 * Copyright (C) 2012-2013 Philip Helger <ph@phloc.com>
 * All Rights Reserved
 *
 * This file is part of the Ecoware Online Shop.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.io.file.FilenameHelper;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.string.StringHelper;
import com.phloc.json2.IJsonObject;
import com.phloc.json2.impl.JsonObject;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webbasics.userdata.UserUploadManager;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractUnifiedResponseServlet;

public class UserUploadServlet extends AbstractUnifiedResponseServlet
{
  public static final String SERVLET_DEFAULT_NAME = "userUpload";
  public static final String SERVLET_DEFAULT_PATH = '/' + SERVLET_DEFAULT_NAME;
  public static final String PARAM_DIRECTORY = "dir";
  public static final String PARAM_ID = "id";
  public static final String PARAM_FILE = "file";

  private static final Logger s_aLogger = LoggerFactory.getLogger (UserUploadServlet.class);

  @Override
  @Nonnull
  protected EnumSet <EHTTPMethod> getAllowedHTTPMethods ()
  {
    return ALLOWED_METHDOS_POST;
  }

  @Nonnull
  private static IJsonObject _createSuccess ()
  {
    return new JsonObject ().add ("success", true);
  }

  @Nonnull
  private static IJsonObject _createError (@Nonnull final String sErrorMsg)
  {
    s_aLogger.error ("User upload error: " + sErrorMsg);
    return new JsonObject ().add ("success", false).add ("error", sErrorMsg).add ("preventRetry", true);
  }

  private void _post (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                      @Nonnull final UnifiedResponse aUnifiedResponse)
  {
    IJsonObject ret;

    final Object aFile = aRequestScope.getAttributeObject (PARAM_FILE);
    if (!(aFile instanceof IFileItem))
      ret = _createError ("No file passed. Maybe the request is not multipart, put 'multipart/form-data' enctype for your form.");
    else
    {
      final IFileItem aFileItem = (IFileItem) aFile;
      final String sDirectory = aRequestScope.getAttributeAsString (PARAM_DIRECTORY);
      final boolean bDirectoryPresent = StringHelper.hasText (sDirectory);
      if (bDirectoryPresent && !FilenameHelper.isValidFilenameWithPaths (sDirectory))
        ret = _createError ("The passed directory name '" + sDirectory + "' is invalid!");
      else
      {
        final String sID = aRequestScope.getAttributeAsString (PARAM_ID);
        if (StringHelper.hasNoText (sID))
          ret = _createError ("No file ID passed!");
        else
        {
          s_aLogger.info ("Uploading " + aFileItem + " as " + sID + " to " + sDirectory);
          // Directory
          String sPath = bDirectoryPresent ? FilenameHelper.ensurePathEndingWithSeparator (sDirectory) : "/";
          // Add basename
          sPath += GlobalIDFactory.getNewPersistentStringID ();
          // Add extension
          final String sExt = FilenameHelper.getExtension (aFileItem.getName ());
          if (StringHelper.hasText (sExt))
            sPath += "." + sExt;
          final UserDataObject aUDO = new UserDataObject (sPath);
          try
          {
            if (aFileItem.write (aUDO.getAsFile ()).isFailure ())
              ret = _createError ("Failed to store uploaded file " + aFileItem + " to " + aUDO);
            else
            {
              // Add to manager
              UserUploadManager.getInstance ().addUploadedFile (sID, aUDO);
              ret = _createSuccess ();
            }
          }
          catch (final Exception ex)
          {
            s_aLogger.error ("Writing " + aFileItem + " to " + aUDO + " failed", ex);
            ret = _createError ("Failed to store uploaded file " + aFileItem + " to " + aUDO);
          }
        }
      }
    }
    aUnifiedResponse.setMimeType (CMimeType.APPLICATION_JSON).setContentAndCharset (ret.getAsString (),
                                                                                    CCharset.CHARSET_UTF_8_OBJ);
  }

  @Override
  protected final void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                      @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException,
                                                                                      IOException
  {
    aUnifiedResponse.disableCaching ();
    _post (aRequestScope, aUnifiedResponse);
  }
}
