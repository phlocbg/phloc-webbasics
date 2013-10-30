package com.phloc.appbasics.security.password.hash;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class combines password hash and the used algorithm.
 * 
 * @author Philip Helger
 */
@Immutable
public final class PasswordHash
{
  private final String m_sAlgorithmName;
  private final String m_sPasswordHash;

  public PasswordHash (@Nonnull @Nonempty final String sAlgorithmName, @Nonnull @Nonempty final String sPasswordHash)
  {
    if (StringHelper.hasNoText (sAlgorithmName))
      throw new IllegalArgumentException ("algorithmName");
    if (StringHelper.hasNoText (sPasswordHash))
      throw new IllegalArgumentException ("passwordHash");
    m_sAlgorithmName = sAlgorithmName;
    m_sPasswordHash = sPasswordHash;
  }

  @Nonnull
  @Nonempty
  public String getAlgorithmName ()
  {
    return m_sAlgorithmName;
  }

  @Nonnull
  @Nonempty
  public String getPasswordHash ()
  {
    return m_sPasswordHash;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof PasswordHash))
      return false;
    final PasswordHash rhs = (PasswordHash) o;
    return m_sAlgorithmName.equals (rhs.m_sAlgorithmName) && m_sPasswordHash.equals (rhs.m_sPasswordHash);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sAlgorithmName).append (m_sPasswordHash).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("algorithmName", m_sAlgorithmName)
                                       .append ("passwordHash", m_sPasswordHash)
                                       .toString ();
  }
}
