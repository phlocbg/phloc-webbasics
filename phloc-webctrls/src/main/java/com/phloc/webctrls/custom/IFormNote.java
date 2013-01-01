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
package com.phloc.webctrls.custom;

import com.phloc.html.hc.IHCNode;

/**
 * Just a base interface for notes. A note is e.g. used in tables to describe
 * fields (the comment) or to show hints. There are several ways on how to
 * represents a note: plain text, icon with mouse-over, tiptip tooltip etc.
 * 
 * @author philip
 */
public interface IFormNote extends IHCNode
{
  /* empty */
}
