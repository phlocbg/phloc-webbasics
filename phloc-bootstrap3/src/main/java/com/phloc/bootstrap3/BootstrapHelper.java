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
package com.phloc.bootstrap3;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.htmlext.HCUtils;

@Immutable
public final class BootstrapHelper
{
  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final BootstrapHelper s_aInstance = new BootstrapHelper ();

  private BootstrapHelper ()
  {}

  public static void markAsFormControls (@Nullable final Iterable <? extends IHCControl <?>> aCtrls)
  {
    if (aCtrls != null)
      for (final IHCControl <?> aCurCtrl : aCtrls)
        if (!(aCurCtrl instanceof HCCheckBox) &&
            !(aCurCtrl instanceof HCRadioButton) &&
            !(aCurCtrl instanceof HCHiddenField))
          aCurCtrl.addClass (CBootstrapCSS.FORM_CONTROL);
  }

  public static void markChildrenAsFormControls (@Nullable final IHCNodeWithChildren <?> aNode)
  {
    if (aNode != null)
      for (final IHCNode aCell : aNode.getChildren ())
        markAsFormControls (HCUtils.getAllHCControls (aCell));
  }
}
