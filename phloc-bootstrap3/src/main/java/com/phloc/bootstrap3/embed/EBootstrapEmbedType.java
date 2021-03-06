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
package com.phloc.bootstrap3.embed;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.css.ICSSClassProvider;

/**
 * Bootstrap3 responsive embed aspect ratio
 *
 * @author Philip Helger
 */
public enum EBootstrapEmbedType implements ICSSClassProvider
{
  ASPECT_RATIO_16_9 (CBootstrapCSS.EMBED_RESPONSIVE_16BY9),
  ASPECT_RATIO_4_3 (CBootstrapCSS.EMBED_RESPONSIVE_4BY3);

  private final ICSSClassProvider m_aCSSClass;

  private EBootstrapEmbedType (@Nonnull final ICSSClassProvider aCSSClass)
  {
    m_aCSSClass = aCSSClass;
  }

  @Nonnull
  public String getCSSClass ()
  {
    return m_aCSSClass.getCSSClass ();
  }
}
