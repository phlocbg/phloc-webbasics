/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.appbasics.app.io;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.IWritableResource;
import com.phloc.commons.io.IWritableResourceProvider;
import com.phloc.commons.io.resourceprovider.ClassPathResourceProvider;
import com.phloc.commons.io.resourceprovider.FileSystemResourceProvider;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Specialized resource provider for testing, that only delivers a resource when
 * it exists!
 * 
 * @author Philip Helger
 */
public final class WebIOResourceProviderChain implements IWritableResourceProvider
{
  private final FileSystemResourceProvider m_aFSProvider;
  private final ClassPathResourceProvider m_aCPProvider;

  public WebIOResourceProviderChain (@Nonnull final File aFileBasePath)
  {
    if (aFileBasePath == null)
      throw new NullPointerException ("fileBasePath");
    m_aFSProvider = new FileSystemResourceProvider (aFileBasePath);
    m_aCPProvider = new ClassPathResourceProvider ();
  }

  public boolean supportsReading (@Nullable final String sName)
  {
    return m_aFSProvider.supportsReading (sName) || m_aCPProvider.supportsReading (sName);
  }

  @Nonnull
  public IReadableResource getReadableResource (@Nonnull final String sName)
  {
    // Does the FileSystem support the resource?
    if (m_aFSProvider.supportsReading (sName))
    {
      // Use the resource only if if exists!
      final IReadableResource aRes = m_aFSProvider.getReadableResource (sName);
      if (aRes.exists ())
        return aRes;
    }

    // Fallback to classpath
    return m_aCPProvider.getReadableResource (sName);
  }

  public boolean supportsWriting (@Nullable final String sName)
  {
    // Only the FileSystem supports writing resources
    return m_aFSProvider.supportsWriting (sName);
  }

  @Nonnull
  public IWritableResource getWritableResource (@Nonnull final String sName)
  {
    // Only the FileSystem supports writing resources
    return m_aFSProvider.getWritableResource (sName);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof WebIOResourceProviderChain))
      return false;
    final WebIOResourceProviderChain rhs = (WebIOResourceProviderChain) o;
    return m_aFSProvider.equals (rhs.m_aFSProvider) && m_aCPProvider.equals (rhs.m_aCPProvider);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aFSProvider).append (m_aCPProvider).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("fsProvider", m_aFSProvider)
                                       .append ("cpProvider", m_aCPProvider)
                                       .toString ();
  }
}
