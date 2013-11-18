package com.phloc.bootstrap3.table;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.bootstrap3.CBootstrapCSS;
import com.phloc.bootstrap3.form.BootstrapHelpBlock;
import com.phloc.commons.error.EErrorLevel;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
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

    final List <IHCControl <?>> aTarget = new ArrayList <IHCControl <?>> ();
    HCUtils.getAllHCControls (HCNodeList.create (aCtrls), aTarget);
    for (final IHCControl <?> aCtrl : aTarget)
      if (!(aCtrl instanceof HCCheckBox) && !(aCtrl instanceof HCRadioButton))
        aCtrl.addClass (CBootstrapCSS.FORM_CONTROL);
  }
}
