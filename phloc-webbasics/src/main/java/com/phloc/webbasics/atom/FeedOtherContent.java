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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.xml.CXML;

public final class FeedOtherContent extends AbstractFeedElement implements IFeedContent
{
  private final List <Object> m_aChildren = new ArrayList <Object> ();
  private final String m_sType;

  public FeedOtherContent (@Nonnull @Nonempty final String sType)
  {
    if (StringHelper.hasNoText (sType))
      throw new IllegalArgumentException ("type is empty");
    m_sType = sType;
  }

  @Nonnull
  @Nonempty
  public String getType ()
  {
    return m_sType;
  }

  @Nonnull
  public FeedOtherContent addChild (@Nonnull final String sText)
  {
    if (sText == null)
      throw new NullPointerException ("text");
    m_aChildren.add (sText);
    return this;
  }

  @Nonnull
  public FeedOtherContent addChild (@Nonnull final IMicroNode aNode)
  {
    if (aNode == null)
      throw new NullPointerException ("node");
    m_aChildren.add (aNode);
    return this;
  }

  public IMicroElement getAsElement (final String sElementName)
  {
    final IMicroElement aElement = new MicroElement (CFeed.XMLNS_ATOM, sElementName);
    aElement.setAttribute ("type", m_sType);
    for (final Object aChild : m_aChildren)
      if (aChild instanceof String)
        aElement.appendText ((String) aChild);
      else
        aElement.appendChild ((IMicroNode) aChild);
    if (StringHelper.hasText (getLanguage ()))
      aElement.setAttribute (CXML.XML_ATTR_LANG, getLanguage ());
    return aElement;
  }

  public boolean isValid ()
  {
    return true;
  }
}
