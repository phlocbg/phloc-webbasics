package com.phloc.web.smtp;

import java.io.UnsupportedEncodingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.phloc.commons.email.IEmailAddress;

/**
 * This class handles a single email address. It is split into an address part
 * and an optional name. The personal name is optional and may be
 * <code>null</code>.
 * 
 * @author philip
 */
@Immutable
public final class InternetAddressUtils
{
  private InternetAddressUtils ()
  {}

  @Nonnull
  public static InternetAddress getAsInternetAddress (@Nonnull final IEmailAddress aAddress,
                                                      @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                                      AddressException
  {
    return getAsInternetAddress (aAddress.getAddress (), aAddress.getPersonal (), sCharset);
  }

  @Nonnull
  public static InternetAddress getAsInternetAddress (@Nonnull final String sAddress,
                                                      @Nullable final String sPersonal,
                                                      @Nullable final String sCharset) throws UnsupportedEncodingException,
                                                                                      AddressException
  {
    final InternetAddress ret = new InternetAddress (sAddress, sPersonal, sCharset);
    ret.validate ();
    return ret;
  }
}
