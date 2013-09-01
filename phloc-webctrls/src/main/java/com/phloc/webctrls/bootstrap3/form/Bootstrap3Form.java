package com.phloc.webctrls.bootstrap3.form;

import javax.annotation.Nonnull;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.html.HCForm;

public class Bootstrap3Form extends HCForm
{
  public Bootstrap3Form (@Nonnull final ISimpleURL aAction)
  {
    this (EBootstrap3FormType.DEFAULT, aAction);
  }

  public Bootstrap3Form (@Nonnull final EBootstrap3FormType eFormType, @Nonnull final ISimpleURL aAction)
  {
    super (aAction);
    if (eFormType == null)
      throw new NullPointerException ("FormType");
    setRole (EHTMLRole.FORM);
    addClass (eFormType);
  }
}
