package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.phloc.commons.charset.CCharset;

/**
 * Special servlet filter that applies a certain encoding to a request and a
 * response.
 * 
 * @author philip
 */
public class CharacterEncodingFilter implements Filter
{
  private static final String s_sEncoding = CCharset.CHARSET_UTF_8;

  public void init (final FilterConfig aFilterConfig) throws ServletException
  {}

  public void doFilter (final ServletRequest aRequest, final ServletResponse aResponse, final FilterChain aChain) throws IOException,
                                                                                                                 ServletException
  {
    // We need this for all form data etc.
    if (aRequest.getCharacterEncoding () == null)
      aRequest.setCharacterEncoding (s_sEncoding);
    aResponse.setCharacterEncoding (s_sEncoding);
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
