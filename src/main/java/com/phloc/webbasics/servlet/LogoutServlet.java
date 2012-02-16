package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.app.security.SimpleWebLogin;

public final class LogoutServlet extends AbstractScopeAwareHttpServlet {
  private static final Logger s_aLogger = LoggerFactory.getLogger (LogoutServlet.class);

  public LogoutServlet () {}

  private void _run (final HttpServletResponse aHttpResponse) throws IOException {
    // Remember the current user ID
    final String sUserID = SimpleWebLogin.getCurrentUserID ();

    // Perform the main logout
    if (SimpleWebLogin.logout ().isChanged ())
      s_aLogger.info ("User " + sUserID + " logged out!");

    // Go home
    aHttpResponse.sendRedirect (LinkUtils.getServletURL ("/").getAsString ());
  }

  @Override
  protected void onGet (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws IOException {
    _run (aResponse);
  }

  @Override
  protected void onPost (final HttpServletRequest aRequest, final HttpServletResponse aResponse) throws IOException {
    _run (aResponse);
  }
}
