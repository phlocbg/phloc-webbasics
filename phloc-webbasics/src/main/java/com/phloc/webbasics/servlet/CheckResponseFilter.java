package com.phloc.webbasics.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import com.phloc.webbasics.web.ResponseHelper;

public class CheckResponseFilter implements Filter
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CheckResponseFilter.class);

  public void init (@Nonnull final FilterConfig filterConfig) throws ServletException
  {}

  /**
   * @param sRequestURL
   * @param nStatusCode
   */
  protected void checkStatusCode (@Nonnull final String sRequestURL, final int nStatusCode)
  {
    if (nStatusCode != HttpServletResponse.SC_OK &&
        nStatusCode < HttpServletResponse.SC_MULTIPLE_CHOICES &&
        nStatusCode >= HttpServletResponse.SC_BAD_REQUEST)
      s_aLogger.warn ("Status code " + nStatusCode + " in response to '" + sRequestURL + "'");
  }

  /**
   * @param sRequestURL
   * @param sCharacterEncoding
   * @param nStatusCode
   */
  @OverrideOnDemand
  protected void checkCharacterEncoding (@Nonnull final String sRequestURL,
                                         @Nullable final String sCharacterEncoding,
                                         final int nStatusCode)
  {
    if (StringHelper.hasNoText (sCharacterEncoding) && !ResponseHelper.isRedirectStatusCode (nStatusCode))
      s_aLogger.warn ("No character encoding on " + nStatusCode + " response to '" + sRequestURL + "'");
  }

  /**
   * @param sRequestURL
   * @param sContentType
   * @param nStatusCode
   */
  @OverrideOnDemand
  protected void checkContentType (@Nonnull final String sRequestURL,
                                   @Nullable final String sContentType,
                                   final int nStatusCode)
  {
    if (StringHelper.hasNoText (sContentType) && !ResponseHelper.isRedirectStatusCode (nStatusCode))
      s_aLogger.warn ("No content type on " + nStatusCode + " response to '" + sRequestURL + "'");
  }

  @OverrideOnDemand
  protected void checkHeaders (@Nonnull final String sRequestURL,
                               @Nonnull final Map <String, List <String>> aHeaders,
                               final int nStatusCode)
  {
    if (nStatusCode != HttpServletResponse.SC_OK && !aHeaders.isEmpty ())
      s_aLogger.warn ("Headers on " + nStatusCode + " response to '" + sRequestURL + "': " + aHeaders);
  }

  private void _checkResults (@Nonnull final HttpServletRequest aHttpRequest,
                              @Nonnull final StatusAwareHttpResponseWrapper aHttpResponse)
  {
    final String sRequestURL = aHttpRequest.getRequestURL ().toString ();
    final int nStatusCode = aHttpResponse.getStatusCode ();
    final Map <String, List <String>> aHeaders = aHttpResponse.getHeaderMap ().getAllHeaders ();
    final String sCharacterEncoding = aHttpResponse.getCharacterEncoding ();
    final String sContentType = aHttpResponse.getContentType ();

    checkStatusCode (sRequestURL, nStatusCode);
    checkCharacterEncoding (sRequestURL, sCharacterEncoding, nStatusCode);
    checkContentType (sRequestURL, sContentType, nStatusCode);
    checkHeaders (sRequestURL, aHeaders, nStatusCode);
  }

  public void doFilter (@Nonnull final ServletRequest aRequest,
                        @Nonnull final ServletResponse aResponse,
                        final FilterChain aChain) throws IOException, ServletException
  {
    StatusAwareHttpResponseWrapper aWrapper = null;
    if (aRequest instanceof HttpServletRequest && aResponse instanceof HttpServletResponse)
      aWrapper = new StatusAwareHttpResponseWrapper ((HttpServletResponse) aResponse);
    aChain.doFilter (aRequest, aWrapper != null ? aWrapper : aResponse);
    if (aWrapper != null)
      _checkResults ((HttpServletRequest) aRequest, aWrapper);
  }

  public void destroy ()
  {}
}
