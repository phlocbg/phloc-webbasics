package com.phloc.webbasics.security.lock;

import javax.annotation.Nonnull;

import org.joda.time.DateTime;

/**
 * A single lock information object.
 * 
 * @author philip
 */
public interface ILockInfo
{
  /**
   * @return The ID of the user who locked the object.
   */
  @Nonnull
  String getLockUserID ();

  /**
   * @return The date and time when the lock was created.
   */
  @Nonnull
  DateTime getLockDateTime ();
}
