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
package com.phloc.webctrls.custom.impl;

import javax.annotation.Nonnull;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.IFormLabel;

public class HCFormLabel extends HCLabel implements IFormLabel
{
  private final ELabelType m_eType;

  @SuppressWarnings ("deprecation")
  private void _assignClasses (@Nonnull final ELabelType eType)
  {
    addClasses (HCFormLabelUtils.CSS_CLASS_FORM_LABEL, HCFormLabelUtils.CSS_CLASS_FORM_LABEL2);
    switch (eType)
    {
      case OPTIONAL:
        addClass (HCFormLabelUtils.CSS_CLASS_FORM_LABEL_OPTIONAL);
        break;
      case MANDATORY:
        addClass (HCFormLabelUtils.CSS_CLASS_FORM_LABEL_MANDATORY);
        break;
      case ALTERNATIVE:
        addClass (HCFormLabelUtils.CSS_CLASS_FORM_LABEL_ALTERNATIVE);
        break;
      default:
        break;
    }
  }

  public HCFormLabel (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    _assignClasses (eType);
    addChild (new HCTextNode (HCFormLabelUtils.getTextWithState (sText, eType)));
    m_eType = eType;
  }

  public HCFormLabel (@Nonnull final IHCNodeWithChildren <?> aNode, @Nonnull final ELabelType eType)
  {
    _assignClasses (eType);
    addChild (HCFormLabelUtils.getNodeWithState (aNode, eType));
    m_eType = eType;
  }

  @Nonnull
  public ELabelType getType ()
  {
    return m_eType;
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final String sText)
  {
    return new HCFormLabel (sText, ELabelType.DEFAULT);
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return create (aTextProvider.getText ());
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final IPredefinedLocaleTextProvider aTextProvider,
                                    @Nonnull final ELabelType eType)
  {
    return create (aTextProvider.getText (), eType);
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    return new HCFormLabel (sText, eType);
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final IHCElementWithChildren <?> aNode)
  {
    return new HCFormLabel (aNode, ELabelType.DEFAULT);
  }

  @Nonnull
  public static HCFormLabel create (@Nonnull final IHCElementWithChildren <?> aNode, @Nonnull final ELabelType eType)
  {
    return new HCFormLabel (aNode, eType);
  }

  @Nonnull
  public static HCFormLabel createMandatory (@Nonnull final String sText)
  {
    return new HCFormLabel (sText, ELabelType.MANDATORY);
  }

  @Nonnull
  public static HCFormLabel createMandatory (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return createMandatory (aTextProvider.getText ());
  }

  @Nonnull
  public static HCFormLabel createMandatory (@Nonnull final IHCElementWithChildren <?> aNode)
  {
    return new HCFormLabel (aNode, ELabelType.MANDATORY);
  }

  @Nonnull
  public static HCFormLabel createAlternative (@Nonnull final String sText)
  {
    return new HCFormLabel (sText, ELabelType.ALTERNATIVE);
  }

  @Nonnull
  public static HCFormLabel createAlternative (@Nonnull final IPredefinedLocaleTextProvider aTextProvider)
  {
    return createAlternative (aTextProvider.getText ());
  }

  @Nonnull
  public static HCFormLabel createAlternative (@Nonnull final IHCElementWithChildren <?> aNode)
  {
    return new HCFormLabel (aNode, ELabelType.ALTERNATIVE);
  }
}
