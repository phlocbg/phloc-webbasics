package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.string.StringHelper;

public class CheckResponseFilter implements Filter
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CheckResponseFilter.class);

  public void init (@Nonnull final FilterConfig filterConfig) throws ServletException
  {}

  /**
   * @param sRequestURL
   * @param nStatusCode
   */
  protected void checkStatusCode (final String sRequestURL, final int nStatusCode)
  {
    if (nStatusCode != HttpServletResponse.SC_OK &&
        nStatusCode < HttpServletResponse.SC_MULTIPLE_CHOICES &&
        nStatusCode >= HttpServletResponse.SC_BAD_REQUEST)
      s_aLogger.warn ("Status code " + nStatusCode + " in response to '" + sRequestURL + '"');
  }

  /**
   * @param sRequestURL
   * @param sCharacterEncoding
   * @param nStatusCode
   */
  @OverrideOnDemand
  protected void checkCharacterEncoding (final String sRequestURL,
                                         final String sCharacterEncoding,
                                         final int nStatusCode)
  {
    if (StringHelper.hasNoText (sCharacterEncoding))
      s_aLogger.warn ("No character encoding in response to '" + sRequestURL + '"');
  }

  /**
   * @param sRequestURL
   * @param sContentType
   * @param nStatusCode
   */
  @OverrideOnDemand
  protected void checkContentType (final String sRequestURL, final String sContentType, final int nStatusCode)
  {
    if (StringHelper.hasNoText (sContentType))
      s_aLogger.warn ("No character encoding in response to '" + sRequestURL + '"');
  }

  private void _checkResults (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final StatusAwareHttpResponseWrapper aHttpResponse)
  {
    final String sRequestURL = aHttpRequest.getRequestURL ().toString ();
    final int nStatusCode = aHttpResponse.getStatusCode ();
    final String sCharacterEncoding = aHttpResponse.getCharacterEncoding ();
    final String sContentType = aHttpResponse.getContentType ();

    checkStatusCode (sRequestURL, nStatusCode);
    checkCharacterEncoding (sRequestURL, sCharacterEncoding, nStatusCode);
    checkContentType (sRequestURL, sContentType, nStatusCode);
  }

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        final FilterChain aChain) throws IOException, ServletException
  {
    StatusAwareHttpResponseWrapper aWrapper = null;
    if (aRequest instanceof HttpServletRequest && aResponse instanceof HttpServletResponse)
    {
      aWrapper = new StatusAwareHttpResponseWrapper ((HttpServletResponse) aResponse);
      ((HttpServletResponse) aResponse).addDateHeader ("foo", new Date ().getTime ());
    }
    aChain.doFilter (aRequest, aWrapper != null ? aWrapper : aResponse);
    if (aWrapper != null)
      _checkResults ((HttpServletRequest) aRequest, aWrapper);
  }

  public void destroy ()
  {}
}
