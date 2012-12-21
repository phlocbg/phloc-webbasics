package com.phloc.webctrls.fineupload;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.json.impl.JSONObject;

public class FineUploader extends FineUploaderBasic
{
  public FineUploader (@Nonnull final Locale aDisplayLocale)
  {
    super (aDisplayLocale);
  }

  @Override
  protected void extendJSONMessage (@Nonnull final JSONObject aMessages, @Nonnull final Locale aDisplayLocale)
  {
    aMessages.setStringProperty ("tooManyFilesError",
                                 EFineUploaderText.TOO_MANY_FILE_ERROR.getDisplayText (aDisplayLocale));
  }
}
