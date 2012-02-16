package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.state.EContinue;
import com.phloc.webbasics.app.scope.RequestScope;
import com.phloc.webbasics.app.scope.ScopeManager;

/**
 * Abstract HTTP servlet filter implementation using the correct scope handling.
 * 
 * @author philip
 */
public abstract class AbstractScopeAwareFilter implements Filter
{
  @OverrideOnDemand
  public void init (final FilterConfig aFilterConfig) throws ServletException
  {}

  /**
   * Implement this main filtering method in subclasses.
   * 
   * @param aHttpRequest
   *        The HTTP request. Never <code>null</code>.
   * @param aHttpResponse
   *        The HTTP response. Never <code>null</code>.
   * @return {@link EContinue#CONTINUE} to indicate that the next filter is to
   *         be called or {@link EContinue#BREAK} to indicate that the next
   *         filter does not need to be called! Never return <code>null</code>!
   */
  @Nonnull
  protected abstract EContinue doFilter (@Nonnull HttpServletRequest aHttpRequest,
                                         @Nonnull HttpServletResponse aHttpResponse) throws IOException,
                                                                                    ServletException;

  public final void doFilter (final ServletRequest aRequest, final ServletResponse aResponse, final FilterChain aChain) throws IOException,
                                                                                                                       ServletException
  {
    final HttpServletRequest aHttpRequest = (HttpServletRequest) aRequest;
    final HttpServletResponse aHttpResponse = (HttpServletResponse) aResponse;
    ScopeManager.onRequestBegin (new RequestScope (aHttpRequest, aHttpResponse));
    try
    {
      if (doFilter (aHttpRequest, aHttpResponse).isContinue ())
      {
        // Continue as usual
        aChain.doFilter (aRequest, aResponse);
      }
    }
    finally
    {
      // End the scope after the complete filtering process!!!!
      ScopeManager.onRequestEnd ();
    }
  }

  @OverrideOnDemand
  public void destroy ()
  {}
}
