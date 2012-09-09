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

import javax.annotation.Nonnull;

import com.phloc.commons.text.IPredefinedLocaleTextProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCLabel;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.impl.HCTextNode;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.impl.AbstractFormLabel;

public class BootstrapFormLabel extends AbstractFormLabel
{
  private final IHCElement <?> m_aNode;

  @Nonnull
  private static IHCElement <?> _modifyNode (@Nonnull final IHCNode aNode, @Nonnull final ELabelType eType)
  {
    IHCNode ret;
    if (eType.equals (ELabelType.MANDATORY))
      ret = new HCStrong ().addChild (aNode);
    else
      if (eType.equals (ELabelType.ALTERNATIVE))
        ret = new HCEM ().addChild (aNode);
      else
        ret = aNode;
    return new HCLabel ().addChild (ret).addClass (CSS_CLASS_FORM_LABEL);
  }

  protected BootstrapFormLabel (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    super (eType);
    if (sText == null)
      throw new NullPointerException ("text");

    m_aNode = _modifyNode (new HCTextNode (_getTextWithState (sText, eType)), eType);
  }

  protected BootstrapFormLabel (@Nonnull final IHCElementWithChildren <?> aNode, @Nonnull final ELabelType eType)
  {
    super (eType);
    if (aNode == null)
      throw new NullPointerException ("node");

    m_aNode = _modifyNode (_getNodeWithState (aNode, eType), eType);
  }

  @Nonnull
  @Override
  protected IHCNode getContainedHCNode ()
  {
    return m_aNode;
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
