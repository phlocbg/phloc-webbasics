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
package com.phloc.bootstrap2;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.DevelopersNote;
import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.IFormLabel;
import com.phloc.webctrls.custom.impl.HCFormLabelUtils;

@Deprecated
@DevelopersNote ("Use HCFormLabel instead")
public class BootstrapFormLabel extends HCLabel implements IFormLabel
{
  private final ELabelType m_eType;

  @Nonnull
  private static IHCNode _modifyNode (@Nonnull final IHCNode aNode, @Nonnull final ELabelType eType)
  {
    IHCNode ret;
    if (eType.equals (ELabelType.MANDATORY))
      ret = new HCStrong ().addChild (aNode);
    else
      if (eType.equals (ELabelType.ALTERNATIVE))
        ret = new HCEM ().addChild (aNode);
      else
        ret = aNode;
    return ret;
  }

  protected BootstrapFormLabel (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    addClass (HCFormLabelUtils.CSS_CLASS_FORM_LABEL);
    addChild (_modifyNode (new HCTextNode (HCFormLabelUtils.getTextWithState (sText, eType)), eType));
    m_eType = eType;
  }

  protected BootstrapFormLabel (@Nonnull final IHCElementWithChildren <?> aNode, @Nonnull final ELabelType eType)
  {
    addClass (HCFormLabelUtils.CSS_CLASS_FORM_LABEL);
    addChild (_modifyNode (HCFormLabelUtils.getNodeWithState (aNode, eType), eType));
    m_eType = eType;
  }

  @Nonnull
  public ELabelType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final String sText)
  {
    return new BootstrapFormLabel (sText, ELabelType.DEFAULT);
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return create (aTextProvider.getText ());
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final IPredefinedLocaleTextProvider aTextProvider,
                                           @Nonnull final ELabelType eType)
  {
    return create (aTextProvider.getText (), eType);
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    return new BootstrapFormLabel (sText, eType);
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final IHCElementWithChildren <?> aNode)
  {
    return new BootstrapFormLabel (aNode, ELabelType.DEFAULT);
  }

  @Nonnull
  public static BootstrapFormLabel create (@Nonnull final IHCElementWithChildren <?> aNode,
                                           @Nonnull final ELabelType eType)
  {
    return new BootstrapFormLabel (aNode, eType);
  }

  @Nonnull
  public static BootstrapFormLabel createMandatory (@Nonnull final String sText)
  {
    return new BootstrapFormLabel (sText, ELabelType.MANDATORY);
  }

  @Nonnull
  public static BootstrapFormLabel createMandatory (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return createMandatory (aTextProvider.getText ());
  }

  @Nonnull
  public static BootstrapFormLabel createMandatory (@Nonnull final IHCElementWithChildren <?> aNode)
  {
    return new BootstrapFormLabel (aNode, ELabelType.MANDATORY);
  }
}
