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
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.api.EHCTextDirection;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSInvocation;

/**
 * Wraps TinyMCE4 into an HC node. The only required settings is
 * {@link #setSelector(String)} but this does not need to be called, as the
 * default value {@link #DEFAULT_SELECTOR} is used automatically.
 * 
 * @author Philip Helger
 */
public class HCTinyMCE4 implements IHCNodeBuilder
{
  public static final boolean DEFAULT_BROWSER_SPELLCHECK = false;
  public static final boolean DEFAULT_NOWRAP = false;
  public static final boolean DEFAULT_OBJECT_RESIZING = true;
  public static final String DEFAULT_SELECTOR = "textarea";

  private String m_sAutoFocus;
  private ETriState m_eBrowserSpellcheck = ETriState.UNDEFINED;
  private EHCTextDirection m_eDirectionality;
  private ETinyMCE4Language m_eLanguage;
  private ISimpleURL m_aLanguageURL;
  private ETriState m_eNoWrap = ETriState.UNDEFINED;
  private ETriState m_eObjectResizing = ETriState.UNDEFINED;
  private String m_sSelector = DEFAULT_SELECTOR;

  @Nullable
  public String getAutoFocus ()
  {
    return m_sAutoFocus;
  }

  /**
   * This option enables you to auto focus an editor instance. The value of this
   * option should be an editor instance id. The editor instance id is the id
   * for the original textarea or div element that got replaced.
   * 
   * @param sAutoFocus
   *        Editor ID
   */
  public void setAutoFocus (@Nullable final String sAutoFocus)
  {
    m_sAutoFocus = sAutoFocus;
  }

  public boolean isBrowserSpellcheck ()
  {
    return m_eBrowserSpellcheck.getAsBooleanValue (DEFAULT_BROWSER_SPELLCHECK);
  }

  /**
   * This is a true/false value if the usage of the browsers internal
   * spellchecker should be used. Default value is false.
   * 
   * @param bBrowserSpellcheck
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setBrowserSpellcheck (final boolean bBrowserSpellcheck)
  {
    m_eBrowserSpellcheck = ETriState.valueOf (bBrowserSpellcheck);
  }

  /**
   * This is a true/false value if the usage of the browsers internal
   * spellchecker should be used. Default value is false.
   * 
   * @param aBrowserSpellcheck
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setBrowserSpellcheck (@Nullable final Boolean aBrowserSpellcheck)
  {
    m_eBrowserSpellcheck = ETriState.valueOf (aBrowserSpellcheck);
  }

  @Nullable
  public EHCTextDirection getDirectionality ()
  {
    return m_eDirectionality;
  }

  /**
   * <pre>
   * Set the default directionality of the editor.
   * Possible values are:
   * - ltr
   *  - rtl
   * </pre>
   * 
   * @param eDirectionality
   *        direction
   */
  public void setDirectionality (@Nullable final EHCTextDirection eDirectionality)
  {
    m_eDirectionality = eDirectionality;
  }

  @Nullable
  public ETinyMCE4Language getLanguage ()
  {
    return m_eLanguage;
  }

  /**
   * Set the language of the UI texts
   * 
   * @param eLanguage
   *        The language to use. <code>null</code> means English
   */
  public void setLanguage (@Nullable final ETinyMCE4Language eLanguage)
  {
    m_eLanguage = eLanguage;
  }

  @Nullable
  public ISimpleURL getLanguageURL ()
  {
    return m_aLanguageURL;
  }

  /**
   * A simple URL to where the language file to use. We recommend using a site
   * absolute URL.
   * 
   * @param aLanguageURL
   *        The language URL to use.
   */
  public void setLanguageURL (@Nullable final ISimpleURL aLanguageURL)
  {
    m_aLanguageURL = aLanguageURL;
  }

  public boolean isNoWrap ()
  {
    return m_eNoWrap.getAsBooleanValue (DEFAULT_NOWRAP);
  }

  /**
   * This option will make the editable are behave like very much like a
   * &lt;pre> tag, and add a scroll instead of wrapping text.
   * 
   * @param bNoWrap
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setNoWrap (final boolean bNoWrap)
  {
    m_eNoWrap = ETriState.valueOf (bNoWrap);
  }

  /**
   * This option will make the editable are behave like very much like a
   * &lt;pre> tag, and add a scroll instead of wrapping text.
   * 
   * @param aNoWrap
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setNoWrap (@Nullable final Boolean aNoWrap)
  {
    m_eNoWrap = ETriState.valueOf (aNoWrap);
  }

  public boolean isObjectResizing ()
  {
    return m_eObjectResizing.getAsBooleanValue (DEFAULT_OBJECT_RESIZING);
  }

  /**
   * This options allows you to turn on/off the resizing handles on images,
   * tables or media objects.
   * 
   * @param bObjectResizing
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setObjectResizing (final boolean bObjectResizing)
  {
    m_eObjectResizing = ETriState.valueOf (bObjectResizing);
  }

  /**
   * This options allows you to turn on/off the resizing handles on images,
   * tables or media objects.
   * 
   * @param aObjectResizing
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setObjectResizing (@Nullable final Boolean aObjectResizing)
  {
    m_eObjectResizing = ETriState.valueOf (aObjectResizing);
  }

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
   *        selector
   */
  public void setSelector (@Nonnull @Nonempty final String sSelector)
  {
    if (StringHelper.hasNoText (sSelector))
      throw new IllegalArgumentException ("selector");
    m_sSelector = sSelector;
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getJSInitOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();
    if (StringHelper.hasText (m_sAutoFocus))
      aOptions.add ("auto_focus", m_sAutoFocus);
    if (m_eBrowserSpellcheck.isDefined ())
      aOptions.add ("browser_spellcheck", isBrowserSpellcheck ());
    if (m_eDirectionality != null)
      aOptions.add ("directionality", m_eDirectionality.getAttrValue ());
    if (m_eLanguage != null)
      aOptions.add ("language", m_eLanguage.getID ());
    if (m_aLanguageURL != null)
      aOptions.add ("language_url", m_aLanguageURL.getAsString ());
    if (m_eNoWrap.isDefined ())
      aOptions.add ("nowrap", isNoWrap ());
    if (m_eObjectResizing.isDefined ())
      aOptions.add ("object_resizing", isObjectResizing ());
    aOptions.add ("selector", m_sSelector);
    return aOptions;
  }

  @Nonnull
  public JSInvocation getJSInvocation ()
  {
    return JSTinyMCE4.init (getJSInitOptions ());
  }

  @Nonnull
  public HCScript build ()
  {
    return new HCScript (getJSInvocation ());
  }
}
