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
package com.phloc.webdemoapp.ui;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webbasics.userdata.UserUploadManager;
import com.phloc.webctrls.fineupload.FineUploader;
import com.phloc.webctrls.fineupload.HCFineUploaderBasic;
import com.phloc.webctrls.styler.WebPageStylerManager;
import com.phloc.webdemoapp.servlet.DemoAppUserUploadServlet;

@Immutable
public final class DemoAppFormUI
{
  private DemoAppFormUI ()
  {}

  @Nonnull
  public static FineUploader createFineUploader (@Nonnull final Locale aDisplayLocale,
                                                 @Nullable final String sDirectory,
                                                 @Nonnull @Nonempty final String sID)
  {
    final FineUploader aFU = new FineUploader (aDisplayLocale);
    aFU.setDebug (GlobalDebug.isDebugMode ())
       .setEndpoint (LinkUtils.getURLWithContext (DemoAppUserUploadServlet.SERVLET_DEFAULT_PATH))
       .setMultiple (false)
       .setAutoUpload (false)
       .setForceMultipart (true)
       .setMaxConnections (1)
       .setInputName (DemoAppUserUploadServlet.PARAM_FILE);

    // Additional parameters
    if (StringHelper.hasText (sDirectory))
      aFU.addParam (DemoAppUserUploadServlet.PARAM_DIRECTORY, sDirectory);
    aFU.addParam (DemoAppUserUploadServlet.PARAM_ID, sID);
    return aFU;
  }

  @Nonnull
  public static IHCNode createImageUploadCtrl (@Nonnull final Locale aDisplayLocale,
                                               @Nullable final String sDirectory,
                                               @Nonnull @Nonempty final String sID)
  {
    // Is something in progress?
    final UserDataObject aUDO = UserUploadManager.getInstance ().getUploadedFile (sID);
    if (aUDO == null)
      return new HCFineUploaderBasic (createFineUploader (aDisplayLocale, sDirectory, sID)).setButtonToUse (WebPageStylerManager.getStyler ()
                                                                                                                               .createUploadButton (aDisplayLocale))
                                                                                           .build ();
    // add preview
    return WebPageStylerManager.getStyler ().createImageView (aUDO, aDisplayLocale);
  }

}
