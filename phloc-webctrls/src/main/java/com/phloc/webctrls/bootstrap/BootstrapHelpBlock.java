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
package com.phloc.webctrls.bootstrap;

import com.phloc.html.hc.html.AbstractHCSpan;

/**
 * Bootstrap block help.
 * 
 * @author philip
 */
public class BootstrapHelpBlock extends AbstractHCSpan <BootstrapHelpBlock> implements
                                                                           IBootstrapHelpItem <BootstrapHelpBlock>
{
  private void _init ()
  {
    addClass (CBootstrapCSS.HELP_BLOCK);
  }

  public BootstrapHelpBlock ()
  {
    super ();
    _init ();
  }
}
