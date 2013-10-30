package com.phloc.appbasics.security.password.hash;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Abstract base class of {@link IPasswordHashCreator}.
 * 
 * @author Philip Helger
 */
public abstract class AbstractPasswordHashCreator implements IPasswordHashCreator
{
  private final String m_sAlgorithm;

  public AbstractPasswordHashCreator (@Nonnull @Nonempty final String sAlgorithm)
  {
    if (StringHelper.hasNoText (sAlgorithm))
      throw new IllegalArgumentException ("algorithm");
    m_sAlgorithm = sAlgorithm;
  }

  @Nonnull
  @Nonempty
  public final String getAlgorithmName ()
  {
    return m_sAlgorithm;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("algorithm", m_sAlgorithm).toString ();
  }
}
