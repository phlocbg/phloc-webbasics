/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.servlet.gzip;

import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.servlet.ServletOutputStream;

/**
 * Special {@link ServletOutputStream} that knows whether it is closed or not
 * 
 * @author philip
 */
public abstract class AbstractServletOutputStream extends ServletOutputStream
{
  private OutputStream m_aOS;
  private boolean m_bClosed = false;

  protected final void setWrappedOutputStream (@Nonnull final OutputStream aOS)
  {
    if (aOS == null)
      throw new NullPointerException ("OS");
    if (m_aOS != null)
      throw new IllegalStateException ("Already a wrapped OS present");
    m_aOS = aOS;
  }

  public final boolean isClosed ()
  {
    return m_bClosed;
  }

  protected abstract void onClose () throws IOException;

  @Override
  @OverridingMethodsMustInvokeSuper
  public final void close () throws IOException
  {
    if (!m_bClosed)
    {
      onClose ();
      m_bClosed = true;
    }
  }

  @Override
  public final void flush () throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot flush a closed output stream");
    m_aOS.flush ();
  }

  @Override
  public final void write (final int b) throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot write to a closed output stream");
    m_aOS.write ((byte) b);
  }

  @Override
  public final void write (final byte b[]) throws IOException
  {
    write (b, 0, b.length);
  }

  @Override
  public final void write (final byte b[], final int off, final int len) throws IOException
  {
    if (m_bClosed)
      throw new IOException ("Cannot write to a closed output stream");
    m_aOS.write (b, off, len);
  }
}
