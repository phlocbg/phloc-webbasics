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
package com.phloc.webctrls.fineupload;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;

@Translatable
public enum EFineUploaderBasicText implements IHasDisplayText
{
  TYPE_ERROR ("{file} hat eine ungültige Erweiterung.\nErlaubt sind nur: {extensions}.", "{file} has an invalid extension.\nValid extension(s): {extensions}."),
  SIZE_ERROR ("{file} ist zu groß. Die maximale Dateigröße ist {sizeLimit}.", "{file} is too large, maximum file size is {sizeLimit}."),
  MIN_SIZE_ERROR ("{file} ist zu klein. Die minimale Dateigröße ist {minSizeLimit}.", "{file} is too small, minimum file size is {minSizeLimit}."),
  EMPTY_ERROR ("{file} ist leer.", "{file} is empty, please select files again without it."),
  NO_FILES_ERROR ("Es wurde keine Dateien zum Hochladen ausgewählt.", "No files to upload."),
  ON_LEAVE ("Derzeit werden Dateien hochgeladen. Wenn Sie diese seiten nun verlassen, wird das Hochladen abgebrochen!", "The files are being uploaded, if you leave now the upload will be cancelled.");

  private final ITextProvider m_aTP;

  private EFineUploaderBasicText (@Nonnull final String sDE, @Nonnull final String sEN)
  {
    m_aTP = TextProvider.create_DE_EN (sDE, sEN);
  }

  public String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
  }
}
