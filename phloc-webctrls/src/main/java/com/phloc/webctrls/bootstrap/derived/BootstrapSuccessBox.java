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
package com.phloc.webctrls.bootstrap.derived;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.webctrls.bootstrap.BootstrapAlert;
import com.phloc.webctrls.bootstrap.EBootstrapAlertType;

/**
 * Bootstrap error box
 * 
 * @author philip
 */
public class BootstrapSuccessBox extends BootstrapAlert
{
  public BootstrapSuccessBox ()
  {
    super ();
    setType (EBootstrapAlertType.SUCCESS);
    setBlock (true);
    setShowClose (true);
  }

  @Nonnull
  public static BootstrapAlert create (@Nullable final String sChild)
  {
    return new BootstrapSuccessBox ().addChild (sChild);
  }
}
