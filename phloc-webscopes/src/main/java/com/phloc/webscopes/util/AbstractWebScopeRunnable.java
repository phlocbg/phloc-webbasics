package com.phloc.webscopes.util;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.callback.INonThrowingRunnable;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.mock.MockHttpServletResponse;
import com.phloc.web.mock.OfflineHttpServletRequest;
import com.phloc.webscopes.mgr.WebScopeManager;

/**
 * Abstract implementation of {@link Runnable} that handles WebScopes correctly.
 * 
 * @author philip
 */
public abstract class AbstractWebScopeRunnable implements INonThrowingRunnable
{
  private final ServletContext m_aSC;
  private final String m_sApplicationID;

  public AbstractWebScopeRunnable ()
  {
    this (WebScopeManager.getGlobalScope ().getServletContext (), WebScopeManager.getApplicationScope ().getID ());
  }

  public AbstractWebScopeRunnable (@Nonnull final ServletContext aSC, @Nonnull @Nonempty final String sApplicationID)
  {
    if (aSC == null)
      throw new NullPointerException ("servletContext");
    if (StringHelper.hasNoText (sApplicationID))
      throw new IllegalArgumentException ("applicationID");

    m_aSC = aSC;
    m_sApplicationID = sApplicationID;
  }

  /**
   * Implement your code in here.
   */
  protected abstract void scopedRun ();

  public final void run ()
  {
    WebScopeManager.onRequestBegin (m_sApplicationID,
                                    new OfflineHttpServletRequest (m_aSC, false),
                                    new MockHttpServletResponse ());
    try
    {
      scopedRun ();
    }
    finally
    {
      WebScopeManager.onRequestEnd ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("servletContext", m_aSC)
                                       .append ("applicationID", m_sApplicationID)
                                       .toString ();
  }
}
