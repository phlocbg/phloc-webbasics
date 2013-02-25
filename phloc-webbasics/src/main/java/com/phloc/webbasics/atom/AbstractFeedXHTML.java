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
package com.phloc.webbasics.atom;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.CXML;
import com.phloc.commons.xml.serialize.EXMLSerializeIndent;
import com.phloc.html.CHTMLDocTypes;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.hc.conversion.HCConversionSettings;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.html.HCDiv;

public abstract class AbstractFeedXHTML extends AbstractFeedElement
{
  private static final HCConversionSettings HCCONV_SETTINGS;
  static
  {
    // Feed XML always as XHTML 1.1
    HCCONV_SETTINGS = new HCConversionSettings (HCSettings.getConversionSettings (false), EHTMLVersion.XHTML11);
    HCCONV_SETTINGS.getXMLWriterSettings ().setIndent (EXMLSerializeIndent.NONE);
    HCCONV_SETTINGS.getCSSWriterSettings ().setOptimizedOutput (true);
  }
  private final HCDiv m_aDiv;

  public AbstractFeedXHTML (@Nonnull final HCDiv aDiv)
  {
    if (aDiv == null)
      throw new NullPointerException ("div");
    m_aDiv = aDiv;
  }

  @Nonnull
  @Nonempty
  public final String getType ()
  {
    return EFeedTextType.XHTML.getType ();
  }

  @Nonnull
  public final HCDiv getDIV ()
  {
    return m_aDiv;
  }

  public final IMicroElement getAsElement (final String sElementName)
  {
    final IMicroElement aElement = new MicroElement (CFeed.XMLNS_ATOM, sElementName);
    aElement.setAttribute ("type", getType ());
    {
      final IMicroNode aDivNode = m_aDiv.convertToNode (HCCONV_SETTINGS);
      ((IMicroElement) aDivNode).setNamespaceURI (CHTMLDocTypes.DOCTYPE_XHTML_URI);
      aElement.appendChild (aDivNode);
    }
    if (StringHelper.hasText (getLanguage ()))
      aElement.setAttribute (CXML.XML_ATTR_LANG, getLanguage ());
    return aElement;
  }

  public final boolean isValid ()
  {
    return true;
  }
}
