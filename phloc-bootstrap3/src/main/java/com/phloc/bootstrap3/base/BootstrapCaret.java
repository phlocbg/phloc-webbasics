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
package com.phloc.bootstrap3.base;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;

/**
 * Represents a caret symbol (e.g. for dropdowns) as the combination of a space
 * (" ") and the span with class=caret
 * 
 * @author Philip Helger
 */
public class BootstrapCaret extends HCNodeList
{
  public BootstrapCaret ()
  {
    addChildren (new HCTextNode (" "), new HCSpan ().addClass (CBootstrapCSS.CARET));
  }
}
