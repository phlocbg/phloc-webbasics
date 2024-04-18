package com.phloc.webscopes.impl;

import javax.annotation.Nullable;

/**
 * Enumeration for the error codes in {@link jakarta.servlet.RequestDispatcher} as
 * these are not yet available in servlet-api versions &lt; 3.0
 * 
 * @author Boris Gregorcic
 */
public enum ERequestDispatcherErrors
{
 ERROR_EXCEPTION ("jakarta.servlet.error.exception"), //$NON-NLS-1$
 ERROR_EXCEPTION_TYPE ("jakarta.servlet.error.exception_type"), //$NON-NLS-1$
 ERROR_MESSAGE ("jakarta.servlet.error.message"), //$NON-NLS-1$
 ERROR_REQUEST_URI ("jakarta.servlet.error.request_uri"), //$NON-NLS-1$
 ERROR_SERVLET_NAME ("jakarta.servlet.error.servlet_name"), //$NON-NLS-1$
 ERROR_STATUS_CODE ("jakarta.servlet.error.status_code"), //$NON-NLS-1$
 FORWARD_SERVLET_PATH ("jakarta.servlet.forward.servlet_path"), //$NON-NLS-1$
 FORWARD_REQUEST_URI ("jakarta.servlet.forward.request_uri"); //$NON-NLS-1$

  private String m_sID;

  private ERequestDispatcherErrors (final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  /**
   * Tries to resolve the enum entry corresponding to the passed ID
   * 
   * @param sID
   *        The ID of the error
   * @return The resolved enum entry, or <code>null</code>
   */
  @Nullable
  public static ERequestDispatcherErrors getFromID (final String sID)
  {
    for (final ERequestDispatcherErrors eValue : values ())
    {
      if (eValue.getID ().equals (sID))
      {
        return eValue;
      }
    }
    return null;
  }

}
