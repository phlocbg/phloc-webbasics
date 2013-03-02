package com.phloc.web.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.servlet.ServletInputStream;

import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;

/**
 * A {@link ServletInputStream} for testing based on a predefined byte array or
 * an existing {@link InputStream}.
 * 
 * @author philip
 */
public class MockServletInputStream extends ServletInputStream
{
  private final InputStream m_aIS;

  public MockServletInputStream (@Nonnull final byte [] aContent)
  {
    this (new NonBlockingByteArrayInputStream (aContent));
  }

  public MockServletInputStream (@Nonnull final InputStream aBaseIS)
  {
    if (aBaseIS == null)
      throw new NullPointerException ("baseIS");
    m_aIS = aBaseIS;
  }

  @Override
  public int read () throws IOException
  {
    return m_aIS.read ();
  }

  @Override
  public void close () throws IOException
  {
    m_aIS.close ();
    super.close ();
  }
}
