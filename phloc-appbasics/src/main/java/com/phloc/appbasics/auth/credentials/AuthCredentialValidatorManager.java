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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.string.StringHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Immutable
public final class AuthCredentialValidatorManager
{
  private static List <IAuthCredentialValidatorSPI> s_aHdlList;

  static
  {
    s_aHdlList = ServiceLoaderUtils.getAllSPIImplementations (IAuthCredentialValidatorSPI.class);
    if (s_aHdlList.isEmpty ())
      throw new InitializationException ("No class implementing " + IAuthCredentialValidatorSPI.class + " was found!");
  }

  private AuthCredentialValidatorManager ()
  {}

  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public static CredentialValidationResult validateCredentials (@Nonnull final Locale aDisplayLocale,
                                                                @Nonnull final IAuthCredentials aCredentials)
  {
    // Collect all strings of all supporting credential validators
    final List <String> aFailedMessages = new ArrayList <String> ();

    // Check all credential handlers if the can handle the passed credentials
    for (final IAuthCredentialValidatorSPI aHdl : s_aHdlList)
      if (aHdl.supportsCredentials (aCredentials))
      {
        final CredentialValidationResult aResult = aHdl.validateCredentials (aDisplayLocale, aCredentials);
        if (aResult == null)
          throw new IllegalStateException ("validateCredentials returned a null object from " + aHdl);
        if (aResult.isSuccess ())
          return aResult;
        aFailedMessages.add (aResult.getErrorMessage ());
      }

    return new CredentialValidationResult (StringHelper.getImplodedNonEmpty ('\n', aFailedMessages));
  }
}
