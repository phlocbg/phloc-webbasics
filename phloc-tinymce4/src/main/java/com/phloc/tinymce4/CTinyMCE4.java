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
package com.phloc.tinymce4;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.version.Version;

/**
 * Constants for usage with TinyMCE
 * 
 * @author Philip Helger
 */
@Immutable
public final class CTinyMCE4
{
  /** Edit version */
  public static final Version TINYMCE_VERSION_4_0_14 = new Version (4, 0, 14);

  private CTinyMCE4 ()
  {}
}
