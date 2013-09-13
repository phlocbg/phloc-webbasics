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
package com.phloc.web.sitemap.provider;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.commons.state.ESuccess;
import com.phloc.web.sitemap.XMLSitemapIndex;
import com.phloc.web.sitemap.XMLSitemapURLSet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class manages the implementations of {@link IXMLSitemapProviderSPI}.
 * 
 * @author Philip Helger
 */
@Immutable
public final class XMLSitemapProvider
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (XMLSitemapProvider.class);
  private static final List <IXMLSitemapProviderSPI> s_aProviders;

  static
  {
    s_aProviders = ServiceLoaderUtils.getAllSPIImplementations (IXMLSitemapProviderSPI.class);
  }

  private XMLSitemapProvider ()
  {}

  @Nonnegative
  public static int getProviderCount ()
  {
    return s_aProviders.size ();
  }

  @Nonnull
  public static ESuccess createSitemapFiles (@Nonnull final File aTargetDirectory)
  {
    return createSitemapFiles (aTargetDirectory, XMLSitemapIndex.DEFAULT_USE_GZIP);
  }

  @Nonnull
  @SuppressFBWarnings ("RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE")
  public static ESuccess createSitemapFiles (@Nonnull final File aTargetDirectory, final boolean bUseGZip)
  {
    if (aTargetDirectory == null)
      throw new NullPointerException ("targetDirectory");
    if (!FileUtils.existsDir (aTargetDirectory))
      throw new IllegalArgumentException ("The passed file is not an existing directory: " + aTargetDirectory);

    // Any provider present?
    if (s_aProviders.isEmpty ())
      return ESuccess.SUCCESS;

    s_aLogger.info ("Writing XML sitemap files for " + s_aProviders.size () + " providers");

    // Start creating the index
    final XMLSitemapIndex aIndex = new XMLSitemapIndex (bUseGZip);
    for (final IXMLSitemapProviderSPI aSPI : s_aProviders)
    {
      final XMLSitemapURLSet aURLSet = aSPI.createURLSet ();
      if (aURLSet == null)
      {
        s_aLogger.warn ("SPI implementation " + aSPI + " returned a null sitemap URL set!");
        continue;
      }
      if (aURLSet.getURLCount () > 0)
        aIndex.addURLSet (aURLSet);
      else
        s_aLogger.info ("SPI implementation " + aSPI + " returned an empty URL set!");
    }

    // Did we get any URL set back?
    if (aIndex.getURLSetCount () == 0)
    {
      s_aLogger.error ("No SPI implementation did deliver a valid URL set -> not doing anything!");
      return ESuccess.FAILURE;
    }

    // Main write to disk action
    return aIndex.writeToDisk (aTargetDirectory);
  }
}
