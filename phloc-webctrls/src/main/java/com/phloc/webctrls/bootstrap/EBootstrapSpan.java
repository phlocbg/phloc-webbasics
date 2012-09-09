/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webctrls.bootstrap;

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
 * @author philip
 */
public enum EBootstrapSpan implements ICSSClassProvider
{
  SPAN1 (1, "span1"),
  SPAN2 (2, "span2"),
  SPAN3 (3, "span3"),
  SPAN4 (4, "span4"),
  SPAN5 (5, "span5"),
  SPAN6 (6, "span6"),
  SPAN7 (7, "span7"),
  SPAN8 (8, "span8"),
  SPAN9 (9, "span9"),
  SPAN10 (10, "span10"),
  SPAN11 (11, "span11"),
  SPAN12 (12, "span12");

  private final int m_nParts;
  private final String m_sCSSClass;

  private EBootstrapSpan (@Nonnegative final int nParts, @Nonnull @Nonempty final String sCSSClass)
  {
    m_nParts = nParts;
    m_sCSSClass = sCSSClass;
  }

  public int getParts ()
  {
    return m_nParts;
  }

  @Nonnull
  @Nonempty
  public String getCSSClass ()
  {
    return m_sCSSClass;
  }

  @Nonnull
  public HCDiv getAsNode (@Nullable final IHCNode aContent)
  {
    return new HCDiv ().addChild (aContent).addClass (this);
  }
}
