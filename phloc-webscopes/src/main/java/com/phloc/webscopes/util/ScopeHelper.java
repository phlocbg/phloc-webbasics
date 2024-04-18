package com.phloc.webscopes.util;

import jakarta.servlet.http.HttpSession;

import com.phloc.web.mock.MockHttpServletRequest;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Utility class for some general scope handling concerns
 * 
 * @author Boris Gregorcic
 */
public final class ScopeHelper
{
  private ScopeHelper ()
  {
    // private
  }

  /**
   * This method can be used to spawn a request when there is none existent.
   * This can be useful when executing actions without a request (e.g. triggered
   * by a scheduled job) where parts of the job may rely on a request (e.g. to
   * access the session)
   * 
   * @param sApplicationID
   *        The current application ID
   * @param aSession
   *        The HTTP session
   */
  public static void setupMockRequestOnDemand (final String sApplicationID, final HttpSession aSession)
  {
    if (sApplicationID == null)
    {
      throw new NullPointerException ("sApplicationID"); //$NON-NLS-1$
    }
    if (!WebScopeManager.isRequestScopePresent ())
    {
      final MockHttpServletRequest aRequest = new MockHttpServletRequest (WebScopeManager.getGlobalScope ()
                                                                                         .getServletContext ());
      aRequest.setSession (aSession);
      WebScopeManager.onRequestBegin (sApplicationID, aRequest, new MockHttpServletResponse ());
      WebScopeManager.getSessionScope (true);
    }
  }
}
