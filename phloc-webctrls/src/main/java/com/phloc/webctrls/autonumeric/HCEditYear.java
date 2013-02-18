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
package com.phloc.webctrls.autonumeric;

import javax.annotation.Nullable;

import com.phloc.webbasics.form.RequestField;

/**
 * Special numeric edit for years from {@value #DEFAULT_MIN} to
 * {@value #DEFAULT_MAX}.
 * 
 * @author philip
 */
public class HCEditYear extends HCAutoNumeric
{
  /** Default minimum value is 0 */
  public static final int DEFAULT_MIN = 0;
  /** Default maximum value is 0 */
  public static final int DEFAULT_MAX = 9999;

  public HCEditYear ()
  {
    this (null);
  }

  public HCEditYear (@Nullable final RequestField aRF)
  {
    super (aRF);
    setDecimalPlaces (0);
    setThousandSeparator ("");
    setMin (DEFAULT_MIN);
    setMax (DEFAULT_MAX);
  }
}
