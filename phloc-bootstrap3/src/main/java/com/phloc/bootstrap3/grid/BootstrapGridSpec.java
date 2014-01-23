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
package com.phloc.bootstrap3.grid;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.hc.IHCElement;

@Immutable
public final class BootstrapGridSpec implements Serializable
{
  private final EBootstrapGridXS m_eXS;
  private final EBootstrapGridSM m_eSM;
  private final EBootstrapGridMD m_eMD;
  private final EBootstrapGridLG m_eLG;

  public BootstrapGridSpec (@Nullable final EBootstrapGridXS eXS,
                            @Nullable final EBootstrapGridSM eSM,
                            @Nullable final EBootstrapGridMD eMD,
                            @Nullable final EBootstrapGridLG eLG)
  {
    m_eXS = eXS;
    m_eSM = eSM;
    m_eMD = eMD;
    m_eLG = eLG;
  }

  @Nullable
  public EBootstrapGridXS getXS ()
  {
    return m_eXS;
  }

  @Nullable
  public EBootstrapGridSM getSM ()
  {
    return m_eSM;
  }

  @Nullable
  public EBootstrapGridMD getMD ()
  {
    return m_eMD;
  }

  @Nullable
  public EBootstrapGridLG getLG ()
  {
    return m_eLG;
  }

  public void applyTo (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    aElement.addClasses (m_eXS, m_eSM, m_eMD, m_eLG);
  }

  public void applyOffsetTo (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    aElement.addClasses (m_eXS == null ? null : m_eXS.getCSSClassOffset (),
                         m_eSM == null ? null : m_eSM.getCSSClassOffset (),
                         m_eMD == null ? null : m_eMD.getCSSClassOffset (),
                         m_eLG == null ? null : m_eLG.getCSSClassOffset ());
  }

  public void applyPullTo (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    aElement.addClasses (m_eXS == null ? null : m_eXS.getCSSClassPull (),
                         m_eSM == null ? null : m_eSM.getCSSClassPull (),
                         m_eMD == null ? null : m_eMD.getCSSClassPull (),
                         m_eLG == null ? null : m_eLG.getCSSClassPull ());
  }

  public void applyPushTo (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    aElement.addClasses (m_eXS == null ? null : m_eXS.getCSSClassPush (),
                         m_eSM == null ? null : m_eSM.getCSSClassPush (),
                         m_eMD == null ? null : m_eMD.getCSSClassPush (),
                         m_eLG == null ? null : m_eLG.getCSSClassPush ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("xs", m_eXS)
                                       .append ("sm", m_eSM)
                                       .append ("md", m_eMD)
                                       .append ("lg", m_eLG)
                                       .toString ();
  }

  @Nonnull
  public static BootstrapGridSpec create (final int nParts)
  {
    return create (nParts, nParts, nParts, nParts);
  }

  @Nonnull
  public static BootstrapGridSpec create (final int nPartsXS, final int nPartsSM, final int nPartsMD, final int nPartsLG)
  {
    return new BootstrapGridSpec (nPartsXS <= 0 ? null : EBootstrapGridXS.getFromParts (nPartsXS),
                                  nPartsSM <= 0 ? null : EBootstrapGridSM.getFromParts (nPartsSM),
                                  nPartsMD <= 0 ? null : EBootstrapGridMD.getFromParts (nPartsMD),
                                  nPartsLG <= 0 ? null : EBootstrapGridLG.getFromParts (nPartsLG));
  }
}
