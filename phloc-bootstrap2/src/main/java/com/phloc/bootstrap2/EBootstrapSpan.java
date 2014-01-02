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
package com.phloc.bootstrap2;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;

/**
 * Bootstrap spans
 * 
 * @author Philip Helger
 */
public enum EBootstrapSpan implements ICSSClassProvider
{
  SPAN1 (1, CBootstrapCSS.SPAN1),
  SPAN2 (2, CBootstrapCSS.SPAN2),
  SPAN3 (3, CBootstrapCSS.SPAN3),
  SPAN4 (4, CBootstrapCSS.SPAN4),
  SPAN5 (5, CBootstrapCSS.SPAN5),
  SPAN6 (6, CBootstrapCSS.SPAN6),
  SPAN7 (7, CBootstrapCSS.SPAN7),
  SPAN8 (8, CBootstrapCSS.SPAN8),
  SPAN9 (9, CBootstrapCSS.SPAN9),
  SPAN10 (10, CBootstrapCSS.SPAN10),
  SPAN11 (11, CBootstrapCSS.SPAN11),
  SPAN12 (12, CBootstrapCSS.SPAN12);

  private final int m_nParts;
  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapSpan (@Nonnegative final int nParts, @Nonnull final ICSSClassProvider aCSSClass)
  {
    m_nParts = nParts;
    m_aCSSClass = aCSSClass;
  }

  @Nonnegative
  public int getParts ()
  {
    return m_nParts;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }

  @Nonnull
  public HCDiv getAsNode (@Nullable final String sContent)
  {
    return new HCDiv ().addChild (sContent).addClass (this);
  }

  @Nonnull
  public HCDiv getAsNode (@Nullable final IHCNode aContent)
  {
    return new HCDiv ().addChild (aContent).addClass (this);
  }

  @Nonnull
  public HCDiv getAsNode (@Nullable final IHCNode... aContent)
  {
    return new HCDiv ().addChildren (aContent).addClass (this);
  }

  @Nonnull
  public HCDiv getAsNode (@Nullable final Iterable <? extends IHCNode> aContent)
  {
    return new HCDiv ().addChildren (aContent).addClass (this);
  }
}
