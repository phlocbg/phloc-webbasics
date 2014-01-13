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
package com.phloc.web.sitemap;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;

import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.xml.EXMLCharMode;
import com.phloc.commons.xml.serialize.XMLMaskHelper;
import com.phloc.datetime.IHasLastModificationDateTime;
import com.phloc.datetime.PDTUtils;
import com.phloc.web.datetime.PDTWebDateUtils;

/**
 * Represents a single URL within an XML URL set.
 * 
 * @author Philip Helger
 */
@Immutable
public final class XMLSitemapURL implements IHasLastModificationDateTime, Serializable
{
  /** Maximum length of a single URL */
  public static final int LOCATION_MAX_LENGTH = 2048;
  public static final double MIN_PRIORITY = 0;
  public static final double MAX_PRIORITY = 1;
  public static final double DEFAULT_PRIORITY = 0.5;

  private static final String ELEMENT_URL = "url";
  private static final String ELEMENT_LOC = "loc";
  private static final String ELEMENT_LASTMOD = "lastmod";
  private static final String ELEMENT_CHANGEFREQ = "changefreq";
  private static final String ELEMENT_PRIORITY = "priority";

  private final String m_sLocation;
  private final DateTime m_aLastModification;
  private final EXMLSitemapChangeFequency m_eChangeFreq;
  private final double m_dPriority;
  private final String m_sPriority;
  private final int m_nOutputLength;

  public XMLSitemapURL (@Nonnull final ISimpleURL aLocation)
  {
    this (aLocation, null);
  }

  public XMLSitemapURL (@Nonnull final ISimpleURL aLocation, @Nullable final DateTime aLastModification)
  {
    this (aLocation, aLastModification, null, null);
  }

  public XMLSitemapURL (@Nonnull final ISimpleURL aLocation,
                        @Nullable final DateTime aLastModification,
                        @Nullable final EXMLSitemapChangeFequency eChangeFreq,
                        @Nullable final Double aPriority)
  {
    if (aLocation == null)
      throw new NullPointerException ("location");
    if (aPriority != null)
      if (aPriority.doubleValue () < MIN_PRIORITY || aPriority.doubleValue () > MAX_PRIORITY)
        throw new IllegalArgumentException ("Invalid priority passed: " + aPriority);

    m_sLocation = aLocation.getAsString ();
    if (m_sLocation.length () > LOCATION_MAX_LENGTH)
      throw new IllegalArgumentException ("URL location is too long!");
    m_aLastModification = PDTUtils.isNullValue (aLastModification) ? null : aLastModification;
    m_eChangeFreq = eChangeFreq;
    m_dPriority = aPriority == null ? DEFAULT_PRIORITY : aPriority.doubleValue ();
    m_sPriority = aPriority == null ? null : aPriority.toString ();
    m_nOutputLength = _buildEstimatedOutputLength ();
  }

  /**
   * Get the length of a single tag name in XML representation for open AND
   * close together.
   * 
   * @param s
   *        The tag name without leading and trailing angle brackets
   * @return The length in chars in XML representation
   */
  @Nonnegative
  private static int _getTagOutputLength (@Nonnull final String s)
  {
    // length + "<" + ">" + "<" + "/>"
    return s.length () * 2 + 5;
  }

  @Nonnegative
  private int _buildEstimatedOutputLength ()
  {
    // <url> element
    int ret = _getTagOutputLength (ELEMENT_URL);

    // <loc>
    ret += _getTagOutputLength (ELEMENT_LOC) +
           XMLMaskHelper.getMaskedXMLTextLength (CXMLSitemap.XML_WRITER_SETTINGS.getXMLVersion (),
                                                 EXMLCharMode.TEXT,
                                                 CXMLSitemap.XML_WRITER_SETTINGS.getIncorrectCharacterHandling (),
                                                 m_sLocation);

    if (m_aLastModification != null)
      ret += _getTagOutputLength (ELEMENT_LASTMOD) + 24;

    if (m_eChangeFreq != null)
      ret += _getTagOutputLength (ELEMENT_CHANGEFREQ) + m_eChangeFreq.getText ().length ();

    if (m_sPriority != null)
      ret += _getTagOutputLength (ELEMENT_PRIORITY) + m_sPriority.length ();

    return ret;
  }

  @Nonnull
  public String getLocation ()
  {
    return m_sLocation;
  }

  @Nullable
  public DateTime getLastModificationDateTime ()
  {
    return m_aLastModification;
  }

  @Nullable
  public EXMLSitemapChangeFequency getChangeFrequency ()
  {
    return m_eChangeFreq;
  }

  @Nonnegative
  public double getPriority ()
  {
    return m_dPriority;
  }

  @Nullable
  public String getPriorityString ()
  {
    return m_sPriority;
  }

  @Nonnegative
  public int getOutputLength ()
  {
    return m_nOutputLength;
  }

  @Nonnull
  public IMicroElement getAsElement ()
  {
    final IMicroElement ret = new MicroElement (CXMLSitemap.XML_NAMESPACE_0_9, ELEMENT_URL);
    ret.appendElement (CXMLSitemap.XML_NAMESPACE_0_9, ELEMENT_LOC).appendText (m_sLocation);
    if (m_aLastModification != null)
      ret.appendElement (CXMLSitemap.XML_NAMESPACE_0_9, ELEMENT_LASTMOD)
         .appendText (PDTWebDateUtils.getAsStringXSD (m_aLastModification));
    if (m_eChangeFreq != null)
      ret.appendElement (CXMLSitemap.XML_NAMESPACE_0_9, ELEMENT_CHANGEFREQ).appendText (m_eChangeFreq.getText ());
    if (m_sPriority != null)
      ret.appendElement (CXMLSitemap.XML_NAMESPACE_0_9, ELEMENT_PRIORITY).appendText (m_sPriority);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof XMLSitemapURL))
      return false;
    final XMLSitemapURL rhs = (XMLSitemapURL) o;
    // Don't compare output length and double priority as they are calculated
    return m_sLocation.equals (rhs.m_sLocation) &&
           EqualsUtils.equals (m_aLastModification, rhs.m_aLastModification) &&
           EqualsUtils.equals (m_eChangeFreq, rhs.m_eChangeFreq) &&
           EqualsUtils.equals (m_sPriority, rhs.m_sPriority);
  }

  @Override
  public int hashCode ()
  {
    // Don't compare output length and double priority as they are calculated
    return new HashCodeGenerator (this).append (m_sLocation)
                                       .append (m_aLastModification)
                                       .append (m_eChangeFreq)
                                       .append (m_sPriority)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("location", m_sLocation)
                                       .appendIfNotNull ("lastModification", m_aLastModification)
                                       .appendIfNotNull ("changeFrequency", m_eChangeFreq)
                                       .appendIfNotNull ("priority", m_sPriority)
                                       .append ("outputLength", m_nOutputLength)
                                       .toString ();
  }
}
