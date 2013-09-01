package com.phloc.webctrls.bootstrap3.form;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCControl;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.AbstractHCEdit;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCRadioButton;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;

public class Bootstrap3Form extends HCForm
{
  private final EBootstrap3FormType m_eFormType;

  public Bootstrap3Form (@Nonnull final ISimpleURL aAction)
  {
    this (aAction, EBootstrap3FormType.DEFAULT);
  }

  public Bootstrap3Form (@Nonnull final ISimpleURL aAction, @Nonnull final EBootstrap3FormType eFormType)
  {
    super (aAction);
    if (eFormType == null)
      throw new NullPointerException ("FormType");
    m_eFormType = eFormType;
    setRole (EHTMLRole.FORM);
    addClass (eFormType);
  }

  @Nullable
  private static IHCControl <?> _getFirstControl (@Nonnull final IHCNode aCtrl)
  {
    if (aCtrl instanceof IHCControl <?>)
      return (IHCControl <?>) aCtrl;

    if (aCtrl instanceof IHCNodeWithChildren <?>)
    {
      // E.g. HCNodeList
      final IHCNodeWithChildren <?> aParent = (IHCNodeWithChildren <?>) aCtrl;
      if (aParent.hasChildren ())
        for (final IHCNode aChild : aParent.getChildren ())
        {
          final IHCControl <?> aNestedCtrl = _getFirstControl (aChild);
          if (aNestedCtrl != null)
            return aNestedCtrl;
        }
    }

    return null;
  }

  @Nonnull
  public Bootstrap3Form addFormGroup (@Nullable final String sLabel, @Nonnull final IHCNode aCtrl)
  {
    if (aCtrl == null)
      throw new NullPointerException ("ctrl");

    final IHCControl <?> aFirstControl = _getFirstControl (aCtrl);
    if (aFirstControl instanceof HCCheckBox)
    {
      // Check box
      final HCDiv aDiv = addAndReturnChild (new HCDiv ().addClass (CBootstrap3CSS.CHECKBOX));
      final HCLabel aLabel = aDiv.addAndReturnChild (new HCLabel ());
      aLabel.addChild (aCtrl);
      if (StringHelper.hasText (sLabel))
        aLabel.addChild (new HCTextNode (" " + sLabel));
    }
    else
      if (aFirstControl instanceof HCRadioButton)
      {
        // Radio button
        final HCDiv aDiv = addAndReturnChild (new HCDiv ().addClass (CBootstrap3CSS.RADIO));
        final HCLabel aLabel = aDiv.addAndReturnChild (new HCLabel ());
        aLabel.addChild (aCtrl);
        if (StringHelper.hasText (sLabel))
          aLabel.addChild (new HCTextNode (" " + sLabel));
      }
      else
      {
        // Other control
        if (StringHelper.hasText (sLabel))
        {
          // We have a label
          final HCLabel aLabel = HCLabel.create (sLabel);

          // Screen reader only....
          if (m_eFormType == EBootstrap3FormType.INLINE)
            aLabel.addClass (CBootstrap3CSS.SR_ONLY);

          if (aFirstControl != null)
          {
            // We have a label for a control
            String sControlID = aFirstControl.getID ();
            if (StringHelper.hasNoText (sControlID))
            {
              sControlID = GlobalIDFactory.getNewStringID ();
              aFirstControl.setID (sControlID);
            }
            aLabel.setFor (sControlID);

            // Ensure the form-control class is present
            aFirstControl.addClass (CBootstrap3CSS.FORM_CONTROL);

            // Set the default placeholder (if none is present)
            if (aFirstControl instanceof AbstractHCEdit <?>)
            {
              final AbstractHCEdit <?> aEdit = (AbstractHCEdit <?>) aFirstControl;
              if (StringHelper.hasNoText (aEdit.getPlaceholder ()))
                aEdit.setPlaceholder (sLabel);
            }
          }

          // Add in form group
          addChild (new HCDiv ().addClass (CBootstrap3CSS.FORM_GROUP).addChildren (aLabel, aCtrl));
        }
        else
        {
          // No label
          addChild (aCtrl);
        }
      }
    return this;
  }
}
