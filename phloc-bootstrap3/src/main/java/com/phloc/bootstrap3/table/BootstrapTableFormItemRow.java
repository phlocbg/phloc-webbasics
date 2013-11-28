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
package com.phloc.bootstrap3.table;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.BootstrapHelper;
import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.form.BootstrapHelpBlock;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.validation.error.IError;
import com.phloc.webctrls.custom.table.HCTableFormItemRow;

public class BootstrapTableFormItemRow extends HCTableFormItemRow
{
  public BootstrapTableFormItemRow (final boolean bHeader, final boolean bHasNoteColumn)
  {
    super (bHeader, bHasNoteColumn);
  }

  @Override
  protected IHCNode createErrorNode (@Nonnull final IError aError)
  {
    return new BootstrapHelpBlock ().addChild (aError.getErrorText ());
  }

  @Override
  protected void modifyControls (@Nonnull final Iterable <? extends IHCNode> aCtrls, final boolean bHasErrors)
  {
    if (bHasErrors)
      addClass (CBootstrapCSS.getCSSClass (EErrorLevel.ERROR));
    else
      removeClass (CBootstrapCSS.getCSSClass (EErrorLevel.ERROR));

    BootstrapHelper.markAsFormControls (HCUtils.getAllHCControls (aCtrls));
  }
}
