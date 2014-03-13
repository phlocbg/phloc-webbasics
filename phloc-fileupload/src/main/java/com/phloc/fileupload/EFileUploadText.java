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
package com.phloc.fileupload;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.name.IHasDisplayTextWithArgs;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EFileUploadText implements IHasDisplayText, IHasDisplayTextWithArgs
{
  ERROR_NO_FILE_OR_EMPTY ("Es wurde keine Datei augewählt oder die Datei ist leer.", "No file was selected or the file is empty."),
  ERROR_NO_MULTIPART_REQUEST ("Es wurde keine Datei an den Server geschickt. Abbruch nach {0} Versuchen.", "No file was sent to the server. Aborting after {0} tries."),
  ERROR_WRONG_TYPE ("Die Datei ist nicht vom Typ {0}!", "The file is not of type {0}!"),
  ERROR_REQUESTSIZE_EXCEEDED ("Fehler beim Hochladen der Datei. Die maximale Dateigröße von {0} wurde überschritten!", "Error uploading the file. The maximum request size {0} was exceeded!"),
  ERROR_FILESIZE_EXCEEDED ("Fehler beim Hochladen der Datei ''{0}''. Die maximale Dateigröße von {1} wurde überschritten!", "Error uploading the file ''{0}''. The maximum file size {1} was exceeded!"),
  ERROR_UPLOAD_GENERIC ("Beim Hochladen der Datei ist ein Fehler aufgetreten: {0}", "Error uploading the file to the server: {0}"),
  ERROR_UPLOAD_CONTEXT ("Fehler beim Hochladen der Datei ''{0}''. Kein Zielverzeichnis bekannt!", "Error uploading the file ''{0}''. No target directory known!"),
  ERROR_POST_PROCESSING ("Interner Fehler in der Nachbearbeitung des Uploads!", "Internal error in upload post processing!"),
  PROGRESS_DESC_INIT ("Initialisiere Upload...", "Initializing upload..."),
  PROGRESS_DESC_PROGRESS ("{0} von {1} gelesen...", "{0} of {1} read..."),
  PROGRESS_DESC_PROGRESS_UNKNOWN ("{0} gelesen...", "{0} read..."),
  PROGRESS_DESC_DONE ("Fertig!", "Done!"),
  PROGRESS_MSG_SUCCESS ("Die Datei ''{0}'' wurde erfolgreich hochgeladen.", "The file ''{0}'' was successfully uploaded.");

  private final ITextProvider m_aTP;

  private EFileUploadText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  @Override
  @Nullable
  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, this.m_aTP, aContentLocale);
  }

  @Override
  @Nullable
  public String getDisplayTextWithArgs (@Nonnull final Locale aContentLocale, @Nullable final Object... aArgs)
  {
    return DefaultTextResolver.getTextWithArgs (this, this.m_aTP, aContentLocale, aArgs);
  }

  /**
   * @return The name of the string property
   */
  public String getStringPropertyName ()
  {
    return name ();
  }
}
