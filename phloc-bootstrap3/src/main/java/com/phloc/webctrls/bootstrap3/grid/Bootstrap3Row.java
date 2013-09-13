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
package com.phloc.webctrls.bootstrap3.grid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.html.AbstractHCDiv;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

public class Bootstrap3Row extends AbstractHCDiv <Bootstrap3Row>
{
  public Bootstrap3Row ()
  {
    addClass (CBootstrap3CSS.ROW);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrap3GridXS eSpan)
  {
    return createColumn (eSpan, null, null, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrap3GridSM eSpan)
  {
    return createColumn (null, eSpan, null, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrap3GridMD eSpan)
  {
    return createColumn (null, null, eSpan, null);
  }

  @Nonnull
  public HCDiv createColumn (@Nonnull final EBootstrap3GridLG eSpan)
  {
    return createColumn (null, null, null, eSpan);
  }

  @Nonnull
  public HCDiv createColumn (@Nullable final EBootstrap3GridXS eXS,
                             @Nullable final EBootstrap3GridSM eSM,
                             @Nullable final EBootstrap3GridMD eMD,
                             @Nullable final EBootstrap3GridLG eLG)
  {
    if (eXS == null && eSM == null && eMD == null && eLG == null)
      throw new NullPointerException ("At least one type must be set!");

    final HCDiv aDiv = addAndReturnChild (new HCDiv ());
    aDiv.addClasses (eXS, eSM, eMD, eLG);
    return aDiv;
  }
}
