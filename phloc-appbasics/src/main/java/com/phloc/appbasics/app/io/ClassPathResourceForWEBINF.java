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
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Specialized internal class path input stream provider, that looks both in the
 * passed directory and optionally in a sub-folder of "WEB-INF".
 * 
 * @author Philip Helger
 */
public class ClassPathResourceForWEBINF implements IReadableResource
{
  private final List <String> m_aPaths = new ArrayList <String> (2);

  public ClassPathResourceForWEBINF (@Nonnull @Nonempty final String sOriginalPath)
  {
    if (StringHelper.hasNoText (sOriginalPath))
      throw new IllegalArgumentException ("No path specified");

    m_aPaths.add (sOriginalPath);
    // Required in case the passed path already contains "/WEB-INF"!
    if (sOriginalPath.startsWith ("/WEB-INF/"))
      m_aPaths.add (0, sOriginalPath.substring (8));
    else
      m_aPaths.add ((StringHelper.startsWith (sOriginalPath, '/') ? "/WEB-INF" : "WEB-INF/") + sOriginalPath);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getPaths ()
  {
    return ContainerHelper.newList (m_aPaths);
  }

  @Nonnull
  public String getResourceID ()
  {
    final URL aURL = getAsURL ();
    return aURL == null ? getPath () : aURL.toExternalForm ();
  }

  @Nonnull
  @Nonempty
  public String getPath ()
  {
    return m_aPaths.get (0);
  }

  @Nullable
  public InputStream getInputStream ()
  {
    // returns null if not found
    InputStream ret = null;
    for (final String sPath : m_aPaths)
    {
      ret = ClassPathResource.getInputStream (sPath);
      if (ret != null)
        break;
    }
    return ret;
  }

  @Nullable
  @Deprecated
  public Reader getReader (@Nonnull final String sCharset)
  {
    return StreamUtils.createReader (getInputStream (), sCharset);
  }

  @Nullable
  public Reader getReader (@Nonnull final Charset aCharset)
  {
    return StreamUtils.createReader (getInputStream (), aCharset);
  }

  public boolean exists ()
  {
    return getAsURL () != null;
  }

  @Nullable
  public URL getAsURL ()
  {
    // returns null if not found
    URL ret = null;
    for (final String sPath : m_aPaths)
    {
      ret = ClassPathResource.getAsURL (sPath);
      if (ret != null)
        break;
    }
    return ret;
  }

  @Nullable
  public File getAsFile ()
  {
    File ret = null;
    // First try from classpath
    for (final String sPath : m_aPaths)
    {
      ret = ClassPathResource.getAsFile (sPath);
      if (ret != null)
        break;
    }
    if (ret == null)
    {
      // Not in classpath - use directly
      for (final String sPath : m_aPaths)
      {
        final File aFile = new File (sPath);
        if (aFile.exists ())
          return aFile;
      }
    }
    return ret;
  }

  @Nonnull
  public IReadableResource getReadableCloneForPath (@Nonnull final String sPath)
  {
    return new ClassPathResourceForWEBINF (sPath);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof ClassPathResourceForWEBINF))
      return false;
    final ClassPathResourceForWEBINF rhs = (ClassPathResourceForWEBINF) o;
    return m_aPaths.equals (rhs.m_aPaths);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aPaths).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("paths", m_aPaths).toString ();
  }
}
