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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.text.impl.TextFormatter;
import com.phloc.commons.typeconvert.TypeConverter;

@Immutable
public final class UploadPostProcessingResult implements ISuccessIndicator, IHasDisplayText, Serializable
{
  private static final long serialVersionUID = -6921324804647089488L;
  private final ESuccess m_eSuccess;
  private final IHasDisplayText m_aMessage;
  private final String m_sResultCode;
  private final Object [] m_aMessageArgs;
  private final boolean m_bReplaceMainSuccessMessage;

  public UploadPostProcessingResult (@Nonnull final ESuccess eSuccess,
                                     @Nonnull final IHasDisplayText aMessage,
                                     @Nonnull final String sResultCode,
                                     @Nullable final Object [] aMessageArgs,
                                     final boolean bReplaceMainSuccessMessage)
  {
    if (eSuccess == null)
    {
      throw new NullPointerException ("success");
    }
    if (aMessage == null)
    {
      throw new NullPointerException ("message");
    }
    if (sResultCode == null)
    {
      throw new NullPointerException ("sResultCode");
    }
    m_eSuccess = eSuccess;
    m_aMessage = aMessage;
    m_sResultCode = sResultCode;
    m_aMessageArgs = ArrayHelper.getCopy (aMessageArgs);
    m_bReplaceMainSuccessMessage = bReplaceMainSuccessMessage;
  }

  public UploadPostProcessingResult (@Nonnull final ESuccess eSuccess,
                                     @Nonnull final IHasDisplayText aMessage,
                                     @Nonnull final String sResultCode,
                                     @Nullable final Object [] aMessageArgs)
  {
    this (eSuccess, aMessage, sResultCode, aMessageArgs, false);
  }

  @Override
  public boolean isFailure ()
  {
    return m_eSuccess.isFailure ();
  }

  @Override
  public boolean isSuccess ()
  {
    return m_eSuccess.isSuccess ();
  }

  @Override
  public String getDisplayText (final Locale aLocale)
  {
    final String ret = m_aMessage.getDisplayText (aLocale);
    return ArrayHelper.isEmpty (m_aMessageArgs) ? ret : TextFormatter.getFormattedText (ret, m_aMessageArgs);
  }

  /**
   * @return A language independent result code that allows providing custom UI
   *         texts based on it.
   */
  public String getResultCode ()
  {
    return m_sResultCode;
  }

  /**
   * @return Any possible message arguments that can be used in constructing a
   *         custom UI message (see also {@link #getResultCode()})
   */
  public List <String> getResultArgs ()
  {
    final List <String> aArgs = new ArrayList <String> ();
    if (!ArrayHelper.isEmpty (m_aMessageArgs))
    {
      for (final Object aArg : m_aMessageArgs)
      {
        aArgs.add (TypeConverter.convertIfNecessary (aArg, String.class));
      }
    }
    return aArgs;
  }

  public boolean isReplaceMainSuccessMessage ()
  {
    return m_bReplaceMainSuccessMessage;
  }
}
