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
package com.phloc.webbasics.app.error;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IHasMicroNodeRepresentation;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;

public final class InternalErrorData
{
  private static final class Entry implements IHasStringRepresentation, IHasMicroNodeRepresentation
  {
    private final String m_sKey;
    private final String m_sValue;

    public Entry (@Nonnull @Nonempty final String sKey, @Nullable final String sValue)
    {
      if (StringHelper.hasNoText (sKey))
        throw new IllegalArgumentException ("key");
      m_sKey = sKey;
      m_sValue = sValue;
    }

    @Nonnull
    @Nonempty
    public String getAsString ()
    {
      return m_sKey + ": " + m_sValue;
    }

    @Nonnull
    public IMicroElement getAsMicroNode ()
    {
      final IMicroElement eEntry = new MicroElement ("entry");
      eEntry.setAttribute ("key", m_sKey);
      eEntry.appendText (m_sValue);
      return eEntry;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (InternalErrorData.class);

  private final List <Entry> m_aFields = new ArrayList <Entry> ();
  private final List <Entry> m_aRequestFields = new ArrayList <Entry> ();
  private final List <Entry> m_aRequestHeaders = new ArrayList <Entry> ();
  private final List <Entry> m_aRequestParameters = new ArrayList <Entry> ();

  public InternalErrorData ()
  {}

  public void addField (@Nonnull final String sKey, @Nullable final String sValue)
  {
    m_aFields.add (new Entry (sKey, sValue));
  }

  public void addField (@Nonnull final String sKey, @Nonnull final Throwable t)
  {
    final String sValue = "Failed to get " + sKey + ": " + t.getMessage () + " -- " + t.getClass ().getName ();
    s_aLogger.warn (sValue);
    addField (sKey, sValue);
  }

  public void addRequestField (@Nonnull final String sKey, @Nullable final String sValue)
  {
    m_aRequestFields.add (new Entry (sKey, sValue));
  }

  public void addRequestHeader (@Nonnull final String sKey, @Nullable final String sValue)
  {
    m_aRequestHeaders.add (new Entry (sKey, sValue));
  }

  public void addRequestParameter (@Nonnull final String sKey, @Nullable final String sValue)
  {
    m_aRequestParameters.add (new Entry (sKey, sValue));
  }

  @Nonnull
  public String getAsString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    for (final Entry aEntry : m_aFields)
      aSB.append (aEntry.getAsString ()).append ('\n');

    if (!m_aRequestFields.isEmpty ())
    {
      aSB.append ("Request:\n");
      for (final Entry aEntry : m_aRequestFields)
        aSB.append ("  ").append (aEntry.getAsString ()).append ('\n');
    }

    if (!m_aRequestHeaders.isEmpty ())
    {
      aSB.append ("Request header:\n");
      for (final Entry aEntry : m_aRequestHeaders)
        aSB.append ("  ").append (aEntry.getAsString ()).append ('\n');
    }

    if (!m_aRequestParameters.isEmpty ())
    {
      aSB.append ("Request parameters:\n");
      for (final Entry aEntry : m_aRequestParameters)
        aSB.append ("  ").append (aEntry.getAsString ()).append ('\n');
    }
    return aSB.toString ();
  }

  @Nonnull
  public IMicroElement getAsMicroNode ()
  {
    final IMicroElement eMetaData = new MicroElement ("metadata");

    {
      final IMicroElement eFields = eMetaData.appendElement ("fields");
      for (final Entry aEntry : m_aFields)
        eFields.appendChild (aEntry.getAsMicroNode ());
    }

    if (!m_aRequestFields.isEmpty ())
    {
      final IMicroElement eRequestFields = eMetaData.appendElement ("requestfields");
      for (final Entry aEntry : m_aRequestFields)
        eRequestFields.appendChild (aEntry.getAsMicroNode ());
    }

    if (!m_aRequestHeaders.isEmpty ())
    {
      final IMicroElement eRequestHeaders = eMetaData.appendElement ("requestheaders");
      for (final Entry aEntry : m_aRequestHeaders)
        eRequestHeaders.appendChild (aEntry.getAsMicroNode ());
    }

    if (!m_aRequestParameters.isEmpty ())
    {
      final IMicroElement eRequestParameters = eMetaData.appendElement ("requestparameters");
      for (final Entry aEntry : m_aRequestParameters)
        eRequestParameters.appendChild (aEntry.getAsMicroNode ());
    }
    return eMetaData;
  }
}
