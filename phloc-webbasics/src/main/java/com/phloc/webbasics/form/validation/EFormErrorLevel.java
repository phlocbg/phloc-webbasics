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
package com.phloc.webbasics.form.validation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.error.IHasErrorLevel;
import com.phloc.commons.error.ISeverityComparable;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.state.IErrorIndicator;
import com.phloc.commons.state.ISuccessIndicator;

/**
 * A special error level enum for form errors.
 * 
 * @author Philip Helger
 */
public enum EFormErrorLevel implements IHasID <String>, ISuccessIndicator, IErrorIndicator, IHasErrorLevel, ISeverityComparable <EFormErrorLevel>
{
  SUCCESS (EErrorLevel.SUCCESS),
  INFO (EErrorLevel.INFO),
  WARN (EErrorLevel.WARN),
  ERROR (EErrorLevel.ERROR);

  /** Lowest error level */
  @Nonnull
  public static final EFormErrorLevel LOWEST = SUCCESS;
  /** Highest error level */
  @Nonnull
  public static final EFormErrorLevel HIGHEST = ERROR;

  private final EErrorLevel m_eLevel;

  private EFormErrorLevel (@Nonnull final EErrorLevel eLevel)
  {
    m_eLevel = eLevel;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_eLevel.getID ();
  }

  @Nonnull
  public EErrorLevel getErrorLevel ()
  {
    return m_eLevel;
  }

  public boolean isSuccess ()
  {
    return m_eLevel.isSuccess ();
  }

  public boolean isFailure ()
  {
    return m_eLevel.isFailure ();
  }

  public boolean isError ()
  {
    return m_eLevel.isError ();
  }

  public boolean isNoError ()
  {
    return m_eLevel.isNoError ();
  }

  public boolean isEqualSevereThan (@Nonnull final EFormErrorLevel eLevel)
  {
    return m_eLevel.isEqualSevereThan (eLevel.m_eLevel);
  }

  public boolean isLessSevereThan (@Nonnull final EFormErrorLevel eLevel)
  {
    return m_eLevel.isLessSevereThan (eLevel.m_eLevel);
  }

  public boolean isLessOrEqualSevereThan (@Nonnull final EFormErrorLevel eLevel)
  {
    return m_eLevel.isLessOrEqualSevereThan (eLevel.m_eLevel);
  }

  public boolean isMoreSevereThan (@Nonnull final EFormErrorLevel eLevel)
  {
    return m_eLevel.isMoreSevereThan (eLevel.m_eLevel);
  }

  public boolean isMoreOrEqualSevereThan (@Nonnull final EFormErrorLevel eLevel)
  {
    return m_eLevel.isMoreOrEqualSevereThan (eLevel.m_eLevel);
  }

  @Nullable
  public static EFormErrorLevel getMostSevere (@Nullable final EFormErrorLevel eLevel1,
                                               @Nullable final EFormErrorLevel eLevel2)
  {
    if (eLevel1 == eLevel2)
      return eLevel1;
    if (eLevel1 == null)
      return eLevel2;
    if (eLevel2 == null)
      return eLevel1;
    return eLevel1.isMoreSevereThan (eLevel2) ? eLevel1 : eLevel2;
  }

  @Nullable
  public static EFormErrorLevel getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EFormErrorLevel.class, sID);
  }

  @Nullable
  public static EFormErrorLevel getFromErrorLevelOrNull (@Nullable final EErrorLevel eLevel)
  {
    return eLevel == null ? null : EnumHelper.getFromIDOrNull (EFormErrorLevel.class, eLevel.getID ());
  }
}
