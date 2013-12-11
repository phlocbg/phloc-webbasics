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
package com.phloc.bootstrap3.grid;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCDiv;

/**
 * Defines a Bootstrap row that contains columns.
 * 
 * @author Philip Helger
 */
public class BootstrapRow extends AbstractHCDiv <BootstrapRow>
{
  public BootstrapRow ()
  {
    addClass (CBootstrapCSS.ROW);
  }

  @Nonnull
  public HCDiv createColumn (final int nParts)
  {
    return createColumn (nParts, nParts, nParts, nParts);
  }

  @Nonnull
  public HCDiv createColumn (final int nPartsXS, final int nPartsSM, final int nPartsMD, final int nPartsLG)
  {
    return createColumn (nPartsXS <= 0 ? null : EBootstrapGridXS.getFromParts (nPartsXS),
                         nPartsSM <= 0 ? null : EBootstrapGridSM.getFromParts (nPartsSM),
                         nPartsMD <= 0 ? null : EBootstrapGridMD.getFromParts (nPartsMD),
                         nPartsLG <= 0 ? null : EBootstrapGridLG.getFromParts (nPartsLG));
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrapGridXS eSpan)
  {
    return createColumn (eSpan, null, null, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrapGridSM eSpan)
  {
    return createColumn (null, eSpan, null, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrapGridMD eSpan)
  {
    return createColumn (null, null, eSpan, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrapGridLG eSpan)
  {
    return createColumn (null, null, null, eSpan);
  }

  @Nonnull
  public HCDiv createColumn (@Nullable final EBootstrapGridXS eXS,
                             @Nullable final EBootstrapGridSM eSM,
                             @Nullable final EBootstrapGridMD eMD,
                             @Nullable final EBootstrapGridLG eLG)
  {
    if (eXS == null && eSM == null && eMD == null && eLG == null)
      throw new NullPointerException ("At least one type must be set!");

    final HCDiv aDiv = addAndReturnChild (new HCDiv ());
    aDiv.addClasses (eXS, eSM, eMD, eLG);
    return aDiv;
  }

  @Nonnull
  @Deprecated
  public BootstrapRow addColumns (@Nonnegative final int nParts0,
                                  @Nullable final IHCNode aPart0,
                                  @Nonnegative final int nParts1,
                                  @Nullable final IHCNode aPart1)
  {
    createColumn (nParts0).addChild (aPart0);
    createColumn (nParts1).addChild (aPart1);
    return this;
  }

  @Nonnull
  @Deprecated
  public BootstrapRow addColumns (@Nonnegative final int nParts0,
                                  @Nullable final IHCNode aPart0,
                                  @Nonnegative final int nParts1,
                                  @Nullable final IHCNode aPart1,
                                  @Nonnegative final int nParts2,
                                  @Nullable final IHCNode aPart2)
  {
    addColumns (nParts0, aPart0, nParts1, aPart1);
    createColumn (nParts2).addChild (aPart2);
    return this;
  }

  @Nonnull
  @Deprecated
  public BootstrapRow addColumns (@Nonnegative final int nParts0,
                                  @Nullable final IHCNode aPart0,
                                  @Nonnegative final int nParts1,
                                  @Nullable final IHCNode aPart1,
                                  @Nonnegative final int nParts2,
                                  @Nullable final IHCNode aPart2,
                                  @Nonnegative final int nParts3,
                                  @Nullable final IHCNode aPart3)
  {
    addColumns (nParts0, aPart0, nParts1, aPart1, nParts2, aPart2);
    createColumn (nParts3).addChild (aPart3);
    return this;
  }
}
