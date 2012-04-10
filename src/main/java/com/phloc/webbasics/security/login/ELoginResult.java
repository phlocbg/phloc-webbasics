package com.phloc.webbasics.security.login;

import com.phloc.commons.state.ISuccessIndicator;

/**
 * Represents the different login results.
 * 
 * @author philip
 */
public enum ELoginResult implements ISuccessIndicator
{
  /** Login was successfully */
  SUCCESS,
  /** No such user */
  USER_NOT_EXISTING,
  /** The provided password is invalid */
  INVALID_PASSWORD,
  /** The user was already logged in */
  USER_ALREADY_LOGGED_IN,
  /** Another user is already logged in this session */
  SESSION_ALREADY_HAS_USER;

  public boolean isSuccess ()
  {
    return this == SUCCESS;
  }

  public boolean isFailure ()
  {
    return this != SUCCESS;
  }
}
