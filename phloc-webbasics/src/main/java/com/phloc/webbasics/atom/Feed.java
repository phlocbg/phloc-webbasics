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
package com.phloc.webbasics.atom;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;

/**
 * Main ATOM 1.0 Feed
 * 
 * @author Philip Helger
 */
public final class Feed extends FeedSource
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (Feed.class);

  private final List <FeedEntry> m_aEntries = new ArrayList <FeedEntry> ();

  public Feed ()
  {}

  @Nonnull
  public Feed addEntry (@Nonnull final FeedEntry aEntry)
  {
    if (aEntry == null)
      throw new NullPointerException ("entry");
    m_aEntries.add (aEntry);
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <FeedEntry> getAllEntries ()
  {
    return ContainerHelper.newList (m_aEntries);
  }

  @Nonnegative
  public int getEntryCount ()
  {
    return m_aEntries.size ();
  }

  @Nonnull
  public IMicroDocument getAsDocument ()
  {
    final IMicroDocument aDocument = new MicroDocument ();
    final IMicroElement aElement = aDocument.appendElement (CFeed.XMLNS_ATOM, "feed");
    super.fillElement (aElement);
    for (final IFeedElement aEntry : m_aEntries)
      aElement.appendChild (aEntry.getAsElement ("entry"));
    return aDocument;
  }

  @Override
  public IMicroElement getAsElement (final String sElementName)
  {
    final IMicroElement aElement = super.getAsElement (sElementName);
    for (final IFeedElement aEntry : m_aEntries)
      aElement.appendChild (aEntry.getAsElement ("entry"));
    return aElement;
  }

  @Override
  public boolean isValid ()
  {
    if (!super.isValid ())
      return false;

    // check nested fields
    for (final FeedEntry aValue : m_aEntries)
      if (!aValue.isValid ())
      {
        s_aLogger.warn ("At least one entry is invalid");
        return false;
      }

    // atom:feed elements MUST contain one or more atom:author elements, unless
    // all of the atom:feed element's child atom:entry elements contain at least
    // one atom:author element.
    if (getAuthors ().isEmpty ())
      for (final FeedEntry aEntry : m_aEntries)
        if (aEntry.getAuthors ().isEmpty ())
        {
          s_aLogger.warn ("Both the feed author field and at least one entry author field is empty");
          return false;
        }

    return true;
  }
}
