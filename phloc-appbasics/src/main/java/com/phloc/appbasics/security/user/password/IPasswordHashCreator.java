package com.phloc.appbasics.security.user.password;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;

/**
 * Interface for a password hash creator.
 * 
 * @author Philip Helger
 */
public interface IPasswordHashCreator
{
  /**
   * @return The name of the algorithm used in this creator. May neither be
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  String getAlgorithmName ();

  /**
   * The method to create a message digest hash from a password.
   * 
   * @param sPlainTextPassword
   *        Plain text password. May not be <code>null</code>.
   * @return The String representation of the password hash. Must be valid to
   *         encode in UTF-8.
   */
  @Nonnull
  String createPasswordHash (@Nonnull String sPlainTextPassword);
}
