package com.phloc.webbasics.servlet;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ServiceLoaderBackport;
import com.phloc.webbasics.app.html.HTMLCreationManager;
import com.phloc.webbasics.app.html.SimpleWebHTMLManager;
import com.phloc.webbasics.spi.IApplicationRequestListenerSPI;

public final class ApplicationServlet extends AbstractScopeAwareHttpServlet {
  private static final Logger s_aLogger = LoggerFactory.getLogger (ApplicationServlet.class);

  private final List <IApplicationRequestListenerSPI> m_aListeners;

  public ApplicationServlet () {
    m_aListeners = ContainerHelper.newList (ServiceLoaderBackport.load (IApplicationRequestListenerSPI.class));
  }

  private void _run (final HttpServletRequest aHttpRequest, final HttpServletResponse aHttpResponse) throws ServletException {
    // Invoke all "request begin" listener
    for (final IApplicationRequestListenerSPI aListener : m_aListeners)
      try {
        aListener.onRequestBegin ();
      }
      catch (final Throwable t) {
        s_aLogger.error ("Failed to invoke onRequestBegin on " + aListener, t);
      }

    try {
      SimpleWebHTMLManager.createHTMLResponse (aHttpRequest, aHttpResponse, new HTMLCreationManager ());
    }
    finally {
      // Invoke all "request end" listener
      for (final IApplicationRequestListenerSPI aListener : m_aListeners)
        try {
          aListener.onRequestEnd ();
        }
        catch (final Throwable t) {
          s_aLogger.error ("Failed to invoke onRequestEnd on " + aListener, t);
        }
    }
  }

  @Override
  protected void onGet (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws ServletException {
    _run (aRequest, aResponse);
  }

  @Override
  protected void onPost (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws ServletException {
    _run (aRequest, aResponse);
  }
}
