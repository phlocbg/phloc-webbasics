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
package com.phloc.appbasics.auth.credentials;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.state.ISuccessIndicator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents the result of a credential validation.
 * 
 * @author Philip Helger
 */
public class CredentialValidationResult implements ISuccessIndicator
{
  public static final CredentialValidationResult SUCCESS = new CredentialValidationResult ();

  private final String m_sErrorMsg;

  /**
   * Success only constructor.
   */
  private CredentialValidationResult ()
  {
    m_sErrorMsg = null;
  }

  /**
   * Constructor with an error message
   * 
   * @param sErrorMsg
   *        The error message. May neither be <code>null</code> nor empty.
   */
  public CredentialValidationResult (@Nonnull @Nonempty final String sErrorMsg)
  {
    if (StringHelper.hasNoText (sErrorMsg))
      throw new IllegalArgumentException ("errorMsg");
    m_sErrorMsg = sErrorMsg;
  }

  public boolean isSuccess ()
  {
    return m_sErrorMsg == null;
  }

  public boolean isFailure ()
  {
    return m_sErrorMsg != null;
  }

  /**
   * @return The error message or <code>null</code> if this is a success.
   */
  @Nullable
  public String getErrorMessage ()
  {
    return m_sErrorMsg;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("errorMsg", m_sErrorMsg).toString ();
  }
}
