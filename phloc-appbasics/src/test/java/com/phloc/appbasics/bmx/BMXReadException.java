package com.phloc.appbasics.bmx;

import javax.annotation.Nonnull;

/**
 * Exceptions that can occur when reading BMX files.
 * 
 * @author philip
 */
public class BMXReadException extends RuntimeException
{
  public BMXReadException (@Nonnull final String sMessage)
  {
    super (sMessage);
  }

  public BMXReadException (@Nonnull final String sMessage, @Nonnull final Throwable t)
  {
    super (sMessage, t);
  }
}
