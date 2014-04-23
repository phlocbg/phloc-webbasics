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
package com.phloc.web.useragent.spider;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Contains information about a single web spider.
 * 
 * @author Philip Helger
 */
public final class WebSpiderInfo implements IHasID <String>, Serializable
{
  private final String m_sID;
  private String m_sName;
  private EWebSpiderType m_eType;
  private String m_sInfo;

  public WebSpiderInfo (@Nonnull @Nonempty final String sID)
  {
    m_sID = ValueEnforcer.notEmpty (sID, "ID");
  }

  @Nonnull
  public String getID ()
  {
    return m_sID;
  }

  void setName (@Nullable final String sName)
  {
    m_sName = sName;
  }

  @Nullable
  public String getName ()
  {
    return m_sName;
  }

  void setType (@Nullable final EWebSpiderType eType)
  {
    m_eType = eType;
  }

  @Nullable
  public EWebSpiderType getType ()
  {
    return m_eType;
  }

  void setInfo (@Nullable final String sInfo)
  {
    m_sInfo = sInfo;
  }

  @Nullable
  public String getInfo ()
  {
    return m_sInfo;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof WebSpiderInfo))
      return false;
    final WebSpiderInfo rhs = (WebSpiderInfo) o;
    return m_sID.equals (rhs.m_sID) &&
           EqualsUtils.equals (m_sName, rhs.m_sName) &&
           EqualsUtils.equals (m_eType, rhs.m_eType) &&
           EqualsUtils.equals (m_sInfo, rhs.m_sInfo);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID)
                                       .append (m_sName)
                                       .append (m_eType)
                                       .append (m_sInfo)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("id", m_sID)
                                       .appendIfNotNull ("name", m_sName)
                                       .appendIfNotNull ("type", m_eType)
                                       .appendIfNotNull ("info", m_sInfo)
                                       .toString ();
  }
}
