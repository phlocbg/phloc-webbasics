package com.phloc.webbasics.app.security;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.microdom.reader.XMLMapHandler;
import com.phloc.webbasics.servlet.WebFileIO;

/**
 * Application user manager.
 * 
 * @author philip
 */
public final class SimpleWebUserManager {
  private static final SimpleWebUserManager s_aInstance = new SimpleWebUserManager ();
  private static final Logger s_aLogger = LoggerFactory.getLogger (SimpleWebUserManager.class);

  private final Map <String, String> m_aUsers = new HashMap <String, String> ();

  private SimpleWebUserManager () {
    final InputStream aIS = WebFileIO.getRegistryInputStream ("users.xml");
    if (aIS == null)
      throw new IllegalStateException ("No users.xml file found in registry!");
    if (XMLMapHandler.readMap (aIS, m_aUsers).isFailure ())
      s_aLogger.warn ("Failed to read users.xml");
  }

  @Nonnull
  public static SimpleWebUserManager getInstance () {
    return s_aInstance;
  }

  public boolean areLoginCredentialsValid (final String sUserName, final String sPassword) {
    final String sStoredPassword = m_aUsers.get (sUserName);
    return sStoredPassword != null && sStoredPassword.equals (sPassword);
  }
}
