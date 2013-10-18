package com.phloc.webbasics.smtp;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.impl.ReadonlySMTPSettings;

public class NamedSMTPSettings implements IHasID <String>
{
  private final String m_sID;
  private String m_sName;
  private ReadonlySMTPSettings m_aSMTPSettings;

  public NamedSMTPSettings (@Nonnull @Nonempty final String sName, @Nonnull final ISMTPSettings aSMTPSettings)
  {
    this (GlobalIDFactory.getNewPersistentStringID (), sName, aSMTPSettings);
  }

  NamedSMTPSettings (@Nonnull @Nonempty final String sID,
                     @Nonnull @Nonempty final String sName,
                     @Nonnull final ISMTPSettings aSMTPSettings)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
    setName (sName);
    setSMTPSettings (aSMTPSettings);
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public EChange setName (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");

    if (sName.equals (m_sName))
      return EChange.UNCHANGED;
    m_sName = sName;
    return EChange.CHANGED;
  }

  @Nonnull
  public ISMTPSettings getSMTPSettings ()
  {
    return m_aSMTPSettings;
  }

  @Nonnull
  public EChange setSMTPSettings (@Nonnull final ISMTPSettings aSMTPSettings)
  {
    if (aSMTPSettings == null)
      throw new NullPointerException ("SMTPSettings");

    // Ensure that the implementation type is the same!
    final ReadonlySMTPSettings aRealSMTPSettings = new ReadonlySMTPSettings (aSMTPSettings);
    if (aRealSMTPSettings.equals (m_aSMTPSettings))
      return EChange.UNCHANGED;
    m_aSMTPSettings = aRealSMTPSettings;
    return EChange.CHANGED;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final NamedSMTPSettings rhs = (NamedSMTPSettings) o;
    return m_sID.equals (rhs.m_sID);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sID).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("name", m_sName)
                                       .append ("SMTPsettings", m_aSMTPSettings)
                                       .toString ();
  }
}
