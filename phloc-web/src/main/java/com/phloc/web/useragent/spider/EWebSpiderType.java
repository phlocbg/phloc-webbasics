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
package com.phloc.web.useragent.spider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

public enum EWebSpiderType implements IHasID <String>
{
  SEARCHSPIDER ("searchspider"),
  RSSSPIDER ("rssspider"),
  BLOGSPIDER ("blogspider"),
  FORUMSPIDER ("forumspider"),
  IMAGESPIDER ("imagespider"),
  MISC_SPIDER ("misc_spider"),
  LINKCHECKER ("linkchecker"),
  HTMLVALIDATOR ("htmlvalidator"),
  CSSVALIDATOR ("cssvalidator"),
  HTTP_MONITORING ("http_monitoring"),
  UNKNOWN ("unknown");

  private final String m_sID;

  private EWebSpiderType (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nullable
  public static EWebSpiderType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EWebSpiderType.class, sID);
  }
}
