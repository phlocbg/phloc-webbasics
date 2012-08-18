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
package com.phloc.appbasics.security.usergroup;

import java.io.Serializable;

import com.phloc.appbasics.security.role.IRoleContainer;
import com.phloc.appbasics.security.user.IUserContainer;
import com.phloc.commons.name.IHasName;
import com.phloc.commons.type.ITypedObject;

/**
 * Represents a single user group encapsulating 0-n users.
 * 
 * @author philip
 */
public interface IUserGroup extends ITypedObject <String>, IHasName, IUserContainer, IRoleContainer, Serializable
{
  /* empty */
}
