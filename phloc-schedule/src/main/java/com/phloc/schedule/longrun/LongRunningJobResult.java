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
package com.phloc.schedule.longrun;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasStringRepresentation;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.url.ISimpleURL;

public final class LongRunningJobResult implements IHasStringRepresentation
{
  public static enum EType implements IHasID <String>
  {
    FILE ("file"),
    XML ("html"),
    TEXT ("text"),
    LINK ("link");

    private final String m_sID;

    private EType (@Nonnull @Nonempty final String sID)
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
    public static EType getFromIDOrNull (@Nullable final String sID)
    {
      return EnumHelper.getFromIDOrNull (EType.class, sID);
    }
  }

  private final EType m_eType;
  private final Object m_aResult;

  private LongRunningJobResult (@Nonnull final EType eType, @Nonnull final Object aResult)
  {
    if (eType == null)
      throw new NullPointerException ("type");
    if (aResult == null)
      throw new NullPointerException ("result");
    m_eType = eType;
    m_aResult = aResult;
  }

  @Nonnull
  public EType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public Object getResultObject ()
  {
    return m_aResult;
  }

  @Nullable
  public File getResultFile ()
  {
    return getType ().equals (EType.FILE) ? (File) m_aResult : null;
  }

  @Nullable
  public IMicroNode getResultXML ()
  {
    return getType ().equals (EType.XML) ? (IMicroNode) m_aResult : null;
  }

  @Nullable
  public String getResultText ()
  {
    return getType ().equals (EType.TEXT) ? (String) m_aResult : null;
  }

  @Nullable
  public ISimpleURL getResultLink ()
  {
    return getType ().equals (EType.LINK) ? (ISimpleURL) m_aResult : null;
  }

  @Nonnull
  public String getAsString ()
  {
    switch (m_eType)
    {
      case FILE:
        return getResultFile ().getAbsolutePath ();
      case XML:
        return MicroWriter.getXMLString (getResultXML ());
      case TEXT:
        return getResultText ();
      case LINK:
        return getResultLink ().getAsString ();
      default:
        throw new IllegalStateException ("Unhandled type!");
    }
  }

  @Nonnull
  public static LongRunningJobResult createFile (@Nonnull final File aResult)
  {
    return new LongRunningJobResult (EType.FILE, aResult);
  }

  @Nonnull
  public static LongRunningJobResult createXML (@Nonnull final IMicroNode aResult)
  {
    return new LongRunningJobResult (EType.XML, aResult);
  }

  @Nonnull
  public static LongRunningJobResult createText (@Nonnull final String sResult)
  {
    return new LongRunningJobResult (EType.TEXT, sResult);
  }

  @Nonnull
  public static LongRunningJobResult createLink (@Nonnull final ISimpleURL aResult)
  {
    return new LongRunningJobResult (EType.LINK, aResult);
  }
}
