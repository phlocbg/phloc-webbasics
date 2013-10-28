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
package com.phloc.tinymce4;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAssocArray;

public class HCTinyMCE4 implements IHCNodeBuilder
{
  public static final String DEFAULT_SELECTOR = "textarea";

  private String m_sSelector = DEFAULT_SELECTOR;

  @Nonnull
  @Nonempty
  public String getSelector ()
  {
    return m_sSelector;
  }

  /**
   * <pre>
   * Selector option, allows you to use CSS selector syntax for determining what areas should be editable, this is the recommended way of selecting what elements should be editable.
   * Some examples of usage.
   * This would select all textarea elements in the page.
   * selector: "textarea",
   * This would select all textareas with the class .editme in your page.
   * selector: "textarea.editme",
   * If you use the inline option, the selector can be used on any block element.
   * selector: "h1.editme",
   * selector: "div.editme",
   * </pre>
   * 
   * @param sSelector
   */
  public void setSelector (@Nonnull @Nonempty final String sSelector)
  {
    if (StringHelper.hasNoText (sSelector))
      throw new IllegalArgumentException ("selector");
    m_sSelector = sSelector;
  }

  @Nonnull
  public HCScript build ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    aOptions.add ("selector", m_sSelector);
    return new HCScript (JSTinyMCE4.init (aOptions));
  }
}
