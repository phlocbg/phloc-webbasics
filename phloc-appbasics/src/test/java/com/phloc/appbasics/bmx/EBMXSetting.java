package com.phloc.appbasics.bmx;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;

public enum EBMXSetting
{
  LZW_ENCODING (0x0001, true);

  private final int m_nValue;
  private final boolean m_bEnabledByDefault;

  private EBMXSetting (@Nonnegative final int nValue, final boolean bEnabledByDefault)
  {
    m_nValue = nValue;
    m_bEnabledByDefault = bEnabledByDefault;
  }

  public int getValue ()
  {
    return m_nValue;
  }

  public boolean isEnabledByDefault ()
  {
    return m_bEnabledByDefault;
  }

  @Nonnull
  @ReturnsMutableCopy
  public static List <EBMXSetting> getAllEnabledByDefault ()
  {
    final List <EBMXSetting> ret = new ArrayList <EBMXSetting> ();
    for (final EBMXSetting eSetting : values ())
      if (eSetting.isEnabledByDefault ())
        ret.add (eSetting);
    return ret;
  }
}
