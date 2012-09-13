package com.phloc.webbasics.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CheckResponseFilter implements Filter
{
  public void init (final FilterConfig filterConfig) throws ServletException
  {}

  public void doFilter (final ServletRequest aRequest, final ServletResponse aResponse, final FilterChain aChain) throws IOException,
                                                                                                                 ServletException
  {
    aChain.doFilter (aRequest, aResponse);
  }

  public void destroy ()
  {}
}
