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
package com.phloc.bootstrap3.pageheader;

import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap3CSS;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCDiv;

/**
 * Bootstrap3 page header.
 * 
 * @author Philip Helger
 */
public class Bootstrap3PageHeader extends AbstractHCDiv <Bootstrap3PageHeader>
{
  public Bootstrap3PageHeader ()
  {
    super ();
    addClass (CBootstrap3CSS.PAGE_HEADER);
  }

  @Nullable
  public static Bootstrap3PageHeader createOnDemand (@Nullable final IHCNode aNode)
  {
    return aNode == null ? null : new Bootstrap3PageHeader ().addChild (aNode);
  }
}