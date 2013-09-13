package com.phloc.web.proxy;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.annotation.Nonnull;

import com.phloc.commons.string.StringParser;

/**
 * A special authenticator for HTTP/HTTPS/FTPS proxy connections.
 * 
 * @author Philip Helger
 */
public class HttpProxyAuthenticator extends Authenticator
{
  private final EHttpProxyType m_eProxyType;

  public HttpProxyAuthenticator (@Nonnull final EHttpProxyType eProxyType)
  {
    if (eProxyType == null)
      throw new NullPointerException ("ProxyType");
    m_eProxyType = eProxyType;
  }

  @Override
  public PasswordAuthentication getPasswordAuthentication ()
  {
    if (getRequestorType () == RequestorType.PROXY)
    {
      // Get current proxy host
      final String sProxyHost = m_eProxyType.getProxyHost ();
      if (getRequestingHost ().equalsIgnoreCase (sProxyHost))
      {
        // Get current proxy port
        final String sProxyPort = m_eProxyType.getProxyPort ();
        if (StringParser.parseInt (sProxyPort, -1) == getRequestingPort ())
        {
          // Seems to be OK.
          final String sProxyUser = m_eProxyType.getProxyUser ();
          final String sProxyPassword = m_eProxyType.getProxyPassword ();
          return new PasswordAuthentication (sProxyUser, sProxyPassword == null ? new char [0]
                                                                               : sProxyPassword.toCharArray ());
        }
      }
    }
    return null;
  }
}
