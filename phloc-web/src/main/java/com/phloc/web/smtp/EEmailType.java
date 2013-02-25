package com.phloc.web.smtp;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

/**
 * Determines the type of an email.
 * 
 * @author philip
 */
public enum EEmailType implements IHasID <String>
{
  TEXT ("text"),
  HTML ("html");

  private final String m_sID;

  private EEmailType (@Nonnull @Nonempty final String sID)
  {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  public boolean isText ()
  {
    return this == TEXT;
  }

  public boolean isHTML ()
  {
    return this == HTML;
  }

  @Nullable
  public static EEmailType getFromIDOrNull (@Nullable final String sID)
  {
    return EnumHelper.getFromIDOrNull (EEmailType.class, sID);
  }
}
