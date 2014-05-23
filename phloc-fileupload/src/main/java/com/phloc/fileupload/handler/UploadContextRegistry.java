/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.fileupload.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.webscopes.singleton.SessionApplicationWebSingleton;

@ThreadSafe
public final class UploadContextRegistry extends SessionApplicationWebSingleton
{
  private static final long serialVersionUID = 5177051956289929072L;

  private final ReadWriteLock m_aLock = new ReentrantReadWriteLock ();
  private final Map <String, UploadContext> m_aContexts = new HashMap <String, UploadContext> ();

  // ESCA-JAVA0057: used for reflection
  @UsedViaReflection
  @Deprecated
  public UploadContextRegistry ()
  {
    // used for reflection
  }

  @Nonnull
  public static UploadContextRegistry getInstance ()
  {
    return getSessionApplicationSingleton (UploadContextRegistry.class);
  }

  @Nullable
  public UploadContext getContext (@Nullable final String sID)
  {
    m_aLock.readLock ().lock ();
    try
    {
      return m_aContexts.get (sID);
    }
    finally
    {
      m_aLock.readLock ().unlock ();
    }
  }

  public void setContext (@Nonnull @Nonempty final String sID, @Nonnull final UploadContext aContext)
  {
    ValueEnforcer.notEmpty (sID, "id"); //$NON-NLS-1$
    ValueEnforcer.notNull (aContext, "upload context"); //$NON-NLS-1$

    m_aLock.writeLock ().lock ();
    try
    {
      // we do not care for overwriting
      m_aContexts.put (sID, aContext);
    }
    finally
    {
      m_aLock.writeLock ().unlock ();
    }
  }

  @Override
  protected void onDestroy () throws Exception
  {
    m_aLock.writeLock ().lock ();
    try
    {
      m_aContexts.clear ();
    }
    finally
    {
      m_aLock.writeLock ().unlock ();
    }
  }
}
