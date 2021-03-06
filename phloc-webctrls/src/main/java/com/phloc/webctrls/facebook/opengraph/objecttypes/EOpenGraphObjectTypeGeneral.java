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
package com.phloc.webctrls.facebook.opengraph.objecttypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.lang.EnumHelper;

/**
 * The various open graph object types taken from <a href=
 * "https://developers.facebook.com/docs/reference/opengraph/object-type"
 * >developers.facebook.com</a>
 * 
 * @author Boris Gregorcic
 */
public enum EOpenGraphObjectTypeGeneral implements IOpenGraphObjectType
{
 PLACE ("place"),
 WEBSITE ("website"),
 BOOK ("book"),
 PROFILE ("profile"),
 OBJECT ("object"),
 ARTICLE ("article"),
 PRODUCT ("product"),
 EVENT ("event");

  private final String m_sID;

  private EOpenGraphObjectTypeGeneral (@Nonnull @Nonempty final String sID)
  {
    this.m_sID = sID;
  }

  @Override
  @Nonnull
  @Nonempty
  public String getID ()
  {
    return this.m_sID;
  }

  @Nullable
  public static EOpenGraphObjectTypeGeneral getFromID (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EOpenGraphObjectTypeGeneral.class, sID);
  }
}
