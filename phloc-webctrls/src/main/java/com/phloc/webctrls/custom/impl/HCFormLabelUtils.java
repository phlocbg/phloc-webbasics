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

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.webctrls.custom.ELabelType;

public final class HCFormLabelUtils
{
  @Deprecated
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL = DefaultCSSClassProvider.create ("phloc-form-label");
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL2 = DefaultCSSClassProvider.create ("form-label");
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL_OPTIONAL = DefaultCSSClassProvider.create ("form-label-optional");
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL_MANDATORY = DefaultCSSClassProvider.create ("form-label-mandatory");
  public static final ICSSClassProvider CSS_CLASS_FORM_LABEL_ALTERNATIVE = DefaultCSSClassProvider.create ("form-label-alternative");

  public static final String SIGN_MANDATORY = "*";
  public static final String SIGN_ALTERNATIVE = "°";
  public static final String LABEL_END = ":";

  private HCFormLabelUtils ()
  {}

  @Nonnull
  public static String getSuffixString (@Nonnull final ELabelType eType)
  {
    if (eType.equals (ELabelType.MANDATORY))
      return SIGN_MANDATORY;
    if (eType.equals (ELabelType.ALTERNATIVE))
      return SIGN_ALTERNATIVE;
    return "";
  }

  @Nonnull
  public static String getSuffix (@Nonnull final ELabelType eType, final boolean bAppendColon)
  {
    final String sSuffix = getSuffixString (eType);
    return bAppendColon ? sSuffix + LABEL_END : sSuffix;
  }

  @Nonnull
  public static String getTextWithState (@Nonnull final String sText, @Nonnull final ELabelType eType)
  {

    ValueEnforcer.notNull (sText, "Text");
    ValueEnforcer.notNull (eType, "Type");

    // Trim optional trailing colon
    final String sPlainText = StringHelper.trimEnd (sText.trim (), LABEL_END);
    return getTextWithState (sText, eType, !StringHelper.endsWith (sPlainText, '?'));
  }

  @Nonnull
  public static String getTextWithState (@Nonnull final String sText,
                                         @Nonnull final ELabelType eType,
                                         final boolean bAppendColon)
  {
    ValueEnforcer.notNull (sText, "Text");
    ValueEnforcer.notNull (eType, "Type");

    // Trim optional trailing colon
    String sPlainText = StringHelper.trimEnd (sText.trim (), LABEL_END);
    // Append suffix only, if at least some text is present
    if (StringHelper.hasText (sPlainText))
      sPlainText += getSuffix (eType, bAppendColon);
    return sPlainText;
  }

  @Nonnull
  public static <T extends IHCNodeWithChildren <?>> T getNodeWithState (@Nonnull final T aNode,
                                                                        @Nonnull final ELabelType eType)
  {
    ValueEnforcer.notNull (aNode, "Node");
    ValueEnforcer.notNull (eType, "Type");

    // Only append the suffix, if at least one text child is present
    if (HCUtils.recursiveContainsAtLeastOneTextNode (aNode))
    {
      final String sPlainText = aNode.getPlainText ();
      if (sPlainText.length () > 0)
      {
        final String sSuffixString = getSuffixString (eType);
        if (StringHelper.hasText (sSuffixString) && StringHelper.endsWith (sPlainText, sSuffixString))
        {
          // Append only colon
          aNode.addChild (LABEL_END);
        }
        else
          if (!StringHelper.endsWith (sPlainText, LABEL_END))
            aNode.addChild (getSuffix (eType, true));
      }
    }
    return aNode;
  }
}
