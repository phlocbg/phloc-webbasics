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
package com.phloc.webctrls.facebook.opengraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.webctrls.facebook.CFacebook;

public enum EOpenGraphMetaTag implements IHasID <String>
{
  TITLE ("title"),
  TYPE ("type"),
  URL ("url"),
  IMAGE ("image"),
  SITE_NAME ("site_name"),
  DESCRIPTION ("description");

  private final String m_sID;

  private EOpenGraphMetaTag (@Nonnull @Nonempty final String sID)
  {
    m_sID = CFacebook.OPENGRAPH_PREFIX + ':' + sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EOpenGraphMetaTag getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EOpenGraphMetaTag.class, sID);
  }
}
