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
package com.phloc.webctrls.custom.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCStrong;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.AbstractWrappedHCNode;
import com.phloc.webctrls.custom.ELabelType;
import com.phloc.webctrls.custom.IFormLabel;

public abstract class AbstractFormLabel extends AbstractWrappedHCNode implements IFormLabel
{
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL = DefaultCSSClassProvider.create ("form-label");
  public static final String SIGN_MANDATORY = "*";
  public static final String SIGN_ALTERNATIVE = "°";
  public static final String LABEL_END = ":";

  private final ELabelType m_eType;

  @Nonnull
  protected static final String _getSuffix (@Nonnull final ELabelType eType)
  {
    if (eType.equals (ELabelType.MANDATORY))
      return SIGN_MANDATORY + LABEL_END;
    if (eType.equals (ELabelType.ALTERNATIVE))
      return SIGN_ALTERNATIVE + LABEL_END;
    return LABEL_END;
  }

  @Nonnull
  protected static final String _getTextWithState (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {
    // Trim optional trailing colon
    String sPlainText = StringHelper.trimEnd (sText.trim (), LABEL_END);
    // Append suffix only, if at least some text is present
    if (StringHelper.hasText (sPlainText))
      sPlainText += _getSuffix (eType);
    return sPlainText;
  }

  @Nullable
  protected static final IHCElementWithChildren <?> _getNodeWithState (@Nonnull final IHCElementWithChildren <?> aNode,
                                                                       @Nonnull final ELabelType eType)
  {
    // Only append the suffix, if at least one text child is present
    if (HCUtils.recursivelyContainsAtLeastOneTextNode (aNode))
      aNode.addChild (_getSuffix (eType));
    return aNode;
  }

  @Nonnull
  private IHCElement <?> _modifyNode (@Nonnull final IHCElement <?> aNode)
  {
    if (m_eType.equals (ELabelType.MANDATORY))
      return new HCStrong (aNode);
    if (m_eType.equals (ELabelType.ALTERNATIVE))
      return new HCEM (aNode);
    return aNode;
  }

  protected AbstractFormLabel (@Nonnull final ELabelType eType)
  {
    if (eType == null)
      throw new NullPointerException ("state");

    m_eType = eType;
  }

  @Nonnull
  public final ELabelType getType ()
  {
    return m_eType;
  }
}
