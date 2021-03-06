/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.auth.identify;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.auth.credentials.AuthCredentialValidatorManager;
import com.phloc.appbasics.auth.credentials.CredentialValidationResult;
import com.phloc.appbasics.auth.credentials.IAuthCredentials;
import com.phloc.appbasics.auth.subject.AuthCredentialToSubjectResolverManager;
import com.phloc.appbasics.auth.subject.IAuthSubject;
import com.phloc.appbasics.auth.token.AuthTokenRegistry;
import com.phloc.appbasics.auth.token.IAuthToken;
import com.phloc.commons.ValueEnforcer;

/**
 * This is the main class for creating an {@link IAuthToken} from credentials.
 *
 * @author Philip Helger
 */
@Immutable
public final class AuthIdentificationManager
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AuthIdentificationManager.class);

  private AuthIdentificationManager ()
  {}

  /**
   * Validate the login credentials, try to resolve the subject and create a
   * token upon success.
   * 
   * @param aDisplayLocale
   *        The display locale
   * @param aCredentials
   *        The credentials to validate. If <code>null</code> it is treated as
   *        error.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static AuthIdentificationResult validateLoginCredentialsAndCreateToken (@Nonnull final Locale aDisplayLocale,
                                                                                 @Nonnull final IAuthCredentials aCredentials)
  {
    ValueEnforcer.notNull (aCredentials, "Credentials");

    // validate credentials
    final CredentialValidationResult aValidationResult = AuthCredentialValidatorManager.validateCredentials (aDisplayLocale,
                                                                                                             aCredentials);
    if (aValidationResult.isFailure ())
    {
      s_aLogger.warn ("Credentials have been rejected: " + aCredentials);
      return new AuthIdentificationResult (aValidationResult);
    }

    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Credentials have been accepted: " + aCredentials);

    // try to get AuthSubject from passed credentials
    final IAuthSubject aSubject = AuthCredentialToSubjectResolverManager.getSubjectFromCredentials (aCredentials);
    if (aSubject != null)
      s_aLogger.info ("Credentials " + aCredentials + " correspond to subject " + aSubject);

    // create the token (without expiration seconds)
    final IAuthToken aNewAuthToken = AuthTokenRegistry.createToken (new AuthIdentification (aSubject),
                                                                    AuthTokenRegistry.NEVER_EXPIRES);
    return new AuthIdentificationResult (aNewAuthToken);
  }
}
