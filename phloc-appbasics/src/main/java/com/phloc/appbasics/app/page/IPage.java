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
package com.phloc.appbasics.app.page;

import java.io.Serializable;

import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayText;

/**
 * The base interface for a single page of content.
 * 
 * @author Philip Helger
 */
public interface IPage extends IHasID <String>, IHasDisplayText, Serializable
{
  /**
   * Determine whether help is available for this page. The default
   * implementation returns always <code>true</code>.
   * 
   * @return <code>true</code> if help is available for this page,
   *         <code>false</code> otherwise.
   */
  boolean isHelpAvailable ();
}
