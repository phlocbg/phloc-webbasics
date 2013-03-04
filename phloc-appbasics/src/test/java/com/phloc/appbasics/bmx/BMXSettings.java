package com.phloc.appbasics.bmx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.state.EChange;

public class BMXSettings implements ICloneable <BMXSettings>
{
  private final Set <EBMXSetting> m_aSettings = EnumSet.noneOf (EBMXSetting.class);

  public BMXSettings ()
  {}

  public BMXSettings (@Nonnull final Collection <? extends EBMXSetting> aSettings)
  {
    if (aSettings == null)
      throw new NullPointerException ("settings");
    m_aSettings.addAll (EBMXSetting.getAllEnabledByDefault ());
  }

  public BMXSettings (@Nonnull final BMXSettings aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("other");
    m_aSettings.addAll (aOther.m_aSettings);
  }

  @Nonnull
  public EChange set (@Nonnull final EBMXSetting eSetting)
  {
    if (eSetting == null)
      throw new NullPointerException ("setting");
    return EChange.valueOf (m_aSettings.add (eSetting));
  }

  @Nonnull
  public EChange unset (@Nullable final EBMXSetting eSetting)
  {
    return EChange.valueOf (m_aSettings.remove (eSetting));
  }

  public boolean isSet (@Nullable final EBMXSetting eSetting)
  {
    return m_aSettings.contains (eSetting);
  }

  @Nonnull
  public int getStorageValue ()
  {
    int ret = 0;
    for (final EBMXSetting eSetting : m_aSettings)
      ret |= eSetting.getValue ();
    return ret;
  }

  @Nonnull
  public BMXSettings getClone ()
  {
    return new BMXSettings (this);
  }

  @Nonnull
  public static BMXSettings createDefault ()
  {
    return new BMXSettings (EBMXSetting.getAllEnabledByDefault ());
  }

  @Nonnull
  public static BMXSettings createFromStorageValue (final int nValue)
  {
    final List <EBMXSetting> aSettings = new ArrayList <EBMXSetting> ();
    for (final EBMXSetting eSetting : EBMXSetting.values ())
      if ((nValue & eSetting.getValue ()) != 0)
        aSettings.add (eSetting);
    return new BMXSettings (aSettings);
  }
}
