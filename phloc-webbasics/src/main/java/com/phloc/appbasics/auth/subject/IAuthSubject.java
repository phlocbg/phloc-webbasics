/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.auth.subject;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.name.IHasName;

/**
 * Represents a user or any other subject that uses an application. A subject
 * has at least a name and some additional properties.
 * 
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface IAuthSubject extends IHasName
{
  /* empty */
}
