/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webctrls.fineupload;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.json.impl.JSONObject;

public class FineUploader extends FineUploaderBasic
{
  public FineUploader (@Nonnull final Locale aDisplayLocale)
  {
    super (aDisplayLocale);
  }

  @Override
  protected void extendJSONMessages (@Nonnull final JSONObject aMessages, @Nonnull final Locale aDisplayLocale)
  {
    aMessages.setStringProperty ("tooManyFilesError",
                                 EFineUploaderText.TOO_MANY_FILE_ERROR.getDisplayText (aDisplayLocale));
  }

  @Override
  protected void extendJSON (@Nonnull final JSONObject aRoot, @Nullable final Locale aDisplayLocale)
  {
    if (aDisplayLocale != null)
    {
      final JSONObject aText = new JSONObject ();
      aText.setStringProperty ("uploadButton", EFineUploaderText.UPLOAD_BUTTON.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("cancelButton", EFineUploaderText.CANCEL_BUTTON.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("retryButton", EFineUploaderText.RETRY_BUTTON.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("failUpload", EFineUploaderText.FAIL_UPLOAD.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("dragZone", EFineUploaderText.DRAG_ZONE.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("dropProcessing", EFineUploaderText.DROP_PROCESSING.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("formatProgress", EFineUploaderText.FORMAT_PROGRESS.getDisplayText (aDisplayLocale));
      aText.setStringProperty ("waitingForResponse",
                               EFineUploaderText.WAITING_FOR_RESPONSE.getDisplayText (aDisplayLocale));
      aRoot.setObjectProperty ("text", aText);
    }
  }
}
