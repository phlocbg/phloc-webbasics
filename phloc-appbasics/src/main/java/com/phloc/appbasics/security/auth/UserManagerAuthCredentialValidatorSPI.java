package com.phloc.appbasics.security.auth;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.auth.credentials.CredentialValidationResult;
import com.phloc.appbasics.auth.credentials.IAuthCredentialValidatorSPI;
import com.phloc.appbasics.auth.credentials.IAuthCredentials;
import com.phloc.appbasics.auth.credentials.userpw.UserNamePasswordCredentials;
import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.commons.annotations.IsSPIImplementation;

@IsSPIImplementation
public final class UserManagerAuthCredentialValidatorSPI implements IAuthCredentialValidatorSPI
{
  public boolean supportsCredentials (@Nonnull final IAuthCredentials aCredentials)
  {
    return aCredentials instanceof UserNamePasswordCredentials;
  }

  @Nonnull
  public CredentialValidationResult validateCredentials (@Nonnull final Locale aDisplayLocale,
                                                         @Nonnull final IAuthCredentials aCredentials)
  {
    final UserNamePasswordCredentials aUPC = (UserNamePasswordCredentials) aCredentials;
    final ELoginResult eLoginResult = LoggedInUserManager.getInstance ().loginUser (aUPC.getUserName (),
                                                                                    aUPC.getPassword ());
    if (eLoginResult.isSuccess ())
      return CredentialValidationResult.SUCCESS;

    // Credential validation failed
    return new CredentialValidationResult (eLoginResult.getDisplayText (aDisplayLocale));
  }
}
