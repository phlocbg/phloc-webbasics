/**
 * Copyright (C) 2006-2015 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.web.mock;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.io.streams.NonBlockingByteArrayInputStream;

/**
 * A {@link ServletInputStream} for testing based on a predefined byte array or
 * an existing {@link InputStream}.
 * 
 * @author Boris Gregorcic
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
    this.m_aIS = ValueEnforcer.notNull (aBaseIS, "BaseInputStream");
  }

  @Override
  public int read () throws IOException
  {
    return this.m_aIS.read ();
  }

  @Override
  public void close () throws IOException
  {
    this.m_aIS.close ();
    super.close ();
  }

  @Override
  public boolean isFinished ()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isReady ()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void setReadListener (final ReadListener readListener)
  {
    // TODO Auto-generated method stub

  }
}
