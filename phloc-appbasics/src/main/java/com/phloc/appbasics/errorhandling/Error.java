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
package com.phloc.appbasics.errorhandling;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents an overall form error. Default implementation of {@link IError}.
 * 
 * @author Philip Helger
 */
@Immutable
public class Error implements IError
{
  private final String m_sID;
  private final EFormErrorLevel m_eLevel;
  private final String m_sFieldName;
  private final String m_sText;

  public Error (@Nonnull final EFormErrorLevel eLevel, @Nonnull @Nonempty final String sText)
  {
    this ((String) null, eLevel, (String) null, sText);
  }

  public Error (@Nullable final String sID,
                @Nonnull final EFormErrorLevel eLevel,
                @Nullable final String sFieldName,
                @Nonnull @Nonempty final String sText)
  {
    if (eLevel == null)
      throw new NullPointerException ("level");
    if (StringHelper.hasNoText (sText))
      throw new IllegalArgumentException ("Text");
    m_sID = sID;
    m_eLevel = eLevel;
    m_sFieldName = sFieldName;
    m_sText = sText;
  }

  @Nullable
  public String getID ()
  {
    return m_sID;
  }

  public boolean hasID ()
  {
    return StringHelper.hasText (m_sID);
  }

  @Nonnull
  public EFormErrorLevel getLevel ()
  {
    return m_eLevel;
  }

  @Nullable
  public String getFieldName ()
  {
    return m_sFieldName;
  }

  public boolean hasFieldName ()
  {
    return StringHelper.hasText (m_sFieldName);
  }

  @Nonnull
  public String getText ()
  {
    return m_sText;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof Error))
      return false;
    final Error rhs = (Error) o;
    return EqualsUtils.equals (m_sID, rhs.m_sID) &&
           m_eLevel.equals (rhs.m_eLevel) &&
           EqualsUtils.equals (m_sFieldName, rhs.m_sFieldName) &&
           m_sText.equals (rhs.m_sText);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID)
                                       .append (m_eLevel)
                                       .append (m_sFieldName)
                                       .append (m_sText)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("errorID", m_sID)
                                       .append ("level", m_eLevel)
                                       .appendIfNotNull ("fieldName", m_sFieldName)
                                       .append ("errorText", m_sText)
                                       .toString ();
  }

  @Nonnull
  public static Error createSuccess (@Nonnull @Nonempty final String sText)
  {
    return createSuccess (null, null, sText);
  }

  @Nonnull
  public static Error createSuccess (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    return createSuccess (null, sFieldName, sText);
  }

  @Nonnull
  public static Error createSuccess (@Nullable final String sID,
                                     @Nullable final String sFieldName,
                                     @Nonnull @Nonempty final String sText)
  {
    return new Error (sID, EFormErrorLevel.SUCCESS, sFieldName, sText);
  }

  @Nonnull
  public static Error createInfo (@Nonnull @Nonempty final String sText)
  {
    return createInfo (null, null, sText);
  }

  @Nonnull
  public static Error createInfo (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    return createInfo (null, sFieldName, sText);
  }

  @Nonnull
  public static Error createInfo (@Nullable final String sID,
                                  @Nullable final String sFieldName,
                                  @Nonnull @Nonempty final String sText)
  {
    return new Error (sID, EFormErrorLevel.INFO, sFieldName, sText);
  }

  @Nonnull
  public static Error createWarning (@Nonnull @Nonempty final String sText)
  {
    return createWarning (null, null, sText);
  }

  @Nonnull
  public static Error createWarning (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    return createWarning (null, sFieldName, sText);
  }

  @Nonnull
  public static Error createWarning (@Nullable final String sID,
                                     @Nullable final String sFieldName,
                                     @Nonnull @Nonempty final String sText)
  {
    return new Error (sID, EFormErrorLevel.WARN, sFieldName, sText);
  }

  @Nonnull
  public static Error createError (@Nonnull @Nonempty final String sText)
  {
    return createError (null, null, sText);
  }

  @Nonnull
  public static Error createError (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    return createError (null, sFieldName, sText);
  }

  @Nonnull
  public static Error createError (@Nullable final String sID,
                                   @Nullable final String sFieldName,
                                   @Nonnull @Nonempty final String sText)
  {
    return new Error (sID, EFormErrorLevel.ERROR, sFieldName, sText);
  }
}
