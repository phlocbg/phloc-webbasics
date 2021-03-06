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
package com.phloc.appbasics.object;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.type.ObjectType;

/**
 * Constants for {@link IObject} etc.
 * 
 * @author Philip Helger
 */
@Immutable
public final class CObject
{
  public static final String GLOBAL_CLIENT = "$";
  public static final String GLOBAL_CLIENT_NAME = "$system client$";

  public static final ObjectType OT_ACCOUNTINGAREA = new ObjectType ("accountingarea");
  public static final ObjectType OT_CLIENT = new ObjectType ("client");

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final CObject s_aInstance = new CObject ();

  private CObject ()
  {}
}
