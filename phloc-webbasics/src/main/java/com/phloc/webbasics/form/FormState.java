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
package com.phloc.webbasics.form;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTime;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.datetime.PDTFactory;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;

@Immutable
public class FormState implements IHasID <String>, Serializable
{
  private final String m_sPageID;
  private final DateTime m_aDT;
  private final String m_sFlowID;
  private final MapBasedAttributeContainer m_aAttrs;

  public FormState (@Nonnull @Nonempty final String sPageID,
                    @Nonnull @Nonempty final String sFlowID,
                    @Nonnull final MapBasedAttributeContainer aAttrs)
  {
    if (StringHelper.hasNoText (sPageID))
      throw new IllegalArgumentException ("pageID");
    if (StringHelper.hasNoText (sFlowID))
      throw new IllegalArgumentException ("flowID");
    if (aAttrs == null)
      throw new NullPointerException ("attrs");
    m_sPageID = sPageID;
    m_aDT = PDTFactory.getCurrentDateTime ();
    m_sFlowID = sFlowID;
    m_aAttrs = aAttrs;
  }

  @Nonnull
  @Nonempty
  public String getPageID ()
  {
    return m_sPageID;
  }

  @Nonnull
  public DateTime getDateTime ()
  {
    return m_aDT;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return getFlowID ();
  }

  @Nonnull
  @Nonempty
  public String getFlowID ()
  {
    return m_sFlowID;
  }

  @Nonnull
  public MapBasedAttributeContainer getAttributes ()
  {
    return m_aAttrs;
  }

  @Nonnull
  public JSAssocArray getAsAssocArray ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    for (final Map.Entry <String, Object> aEntry : m_aAttrs.getAllAttributes ().entrySet ())
    {
      final String sKey = aEntry.getKey ();
      final Object aValue = aEntry.getValue ();
      if (aValue instanceof String)
        ret.add (sKey, (String) aValue);
      else
        if (aValue instanceof String [])
        {
          final JSArray aArray = new JSArray ();
          for (final String sElement : (String []) aValue)
            aArray.add (sElement);
          ret.add (sKey, aArray);
        }
      // else e.g. fileitem -> ignore
    }
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("pageID", m_sPageID)
                                       .append ("DT", m_aDT)
                                       .append ("flowID", m_sFlowID)
                                       .append ("attrs", m_aAttrs)
                                       .toString ();
  }
}
