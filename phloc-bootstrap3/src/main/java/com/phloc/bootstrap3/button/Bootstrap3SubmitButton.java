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
package com.phloc.bootstrap3.button;

import javax.annotation.Nonnull;

import com.phloc.html.hc.api.EHCButtonType;

/**
 * Bootstrap3 submit button.
 * 
 * @author Philip Helger
 */
public class Bootstrap3SubmitButton extends Bootstrap3Button
{
  private void _init ()
  {
    setType (EHCButtonType.SUBMIT);
  }

  public Bootstrap3SubmitButton ()
  {
    this (EBootstrap3ButtonType.PRIMARY);
  }

  public Bootstrap3SubmitButton (@Nonnull final EBootstrap3ButtonType eButtonType)
  {
    super (eButtonType);
    _init ();
  }

  public Bootstrap3SubmitButton (@Nonnull final EBootstrap3ButtonSize eButtonSize)
  {
    this (EBootstrap3ButtonType.PRIMARY, eButtonSize);
  }

  public Bootstrap3SubmitButton (@Nonnull final EBootstrap3ButtonType eButtonType,
                                 @Nonnull final EBootstrap3ButtonSize eButtonSize)
  {
    super (eButtonType, eButtonSize);
    _init ();
  }
}