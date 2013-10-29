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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.ETriState;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.api.EHCTextDirection;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.json2.impl.JsonObject;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * Wraps TinyMCE4 into an HC node. The only required settings is
 * {@link #setSelector(String)} but this does not need to be called, as the
 * default value {@link #DEFAULT_SELECTOR} is used automatically.
 * 
 * @author Philip Helger
 */
public class HCTinyMCE4 implements IHCNodeBuilder
{
  // General options
  public static final boolean DEFAULT_BROWSER_SPELLCHECK = false;
  public static final boolean DEFAULT_NOWRAP = false;
  public static final boolean DEFAULT_OBJECT_RESIZING = true;
  public static final String DEFAULT_SELECTOR = "textarea";
  public static final boolean DEFAULT_INLINE = false;
  public static final boolean DEFAULT_HIDDEN_INPUT = true;

  // Cleanup/output
  public static final boolean DEFAULT_CONVERT_FONTS_TO_SPANS = true;

  // General options
  private String m_sAutoFocus;
  private EHCTextDirection m_eDirectionality;
  private ETriState m_eBrowserSpellcheck = ETriState.UNDEFINED;
  private ETinyMCE4Language m_eLanguage;
  private ISimpleURL m_aLanguageURL;
  private ETriState m_eNoWrap = ETriState.UNDEFINED;
  private ETriState m_eObjectResizing = ETriState.UNDEFINED;
  private final Set <ETinyMCE4Plugin> m_aPlugins = new LinkedHashSet <ETinyMCE4Plugin> ();
  private final Set <TinyMCE4ExternalPlugin> m_aExternalPlugins = new LinkedHashSet <TinyMCE4ExternalPlugin> ();
  private String m_sSelector = DEFAULT_SELECTOR;
  private ETinyMCE4Skin m_eSkin;
  private ISimpleURL m_aSkinURL;
  private ETinyMCE4Theme m_eTheme;
  private ISimpleURL m_aThemeURL;
  private ETriState m_eInline = ETriState.UNDEFINED;
  private ETriState m_eHiddenInput = ETriState.UNDEFINED;

  // Cleanup/output
  private ETriState m_eConvertFontsToSpans = ETriState.UNDEFINED;

  // custom_elements
  // doctype
  // element_format
  // entities
  // entity_encoding
  // extended_valid_elements
  // fix_list_elements
  // font_formats
  // fontsize_formats
  // force_p_newlines
  // force_hex_style_colors
  // forced_root_block
  // forced_root_block_attrs
  // formats
  // indentation
  // invalid_elements
  // keep_styles
  // protect
  // schema
  // style_formats
  // block_formats
  // valid_children
  // valid_elements
  // valid_styles

  // Content style
  // body_id
  // body_class
  // content_css

  // Visual aids
  // visual

  // Undo/Redo
  // custom_undo_redo_levels

  // User interface
  // toolbar
  // toolbar<N>
  // menubar
  // menu
  // statusbar
  // resize
  // width
  // height
  // preview_styles
  // fixed_toolbar_container

  // URL
  // convert_urls
  // relative_urls
  // remove_script_host
  // document_base_url

  // Callbacks
  // file_browser_callback

  // Custom
  private final Map <String, IJSExpression> m_aCustom = new LinkedHashMap <String, IJSExpression> ();

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
  @ReturnsMutableCopy
  public Set <ETinyMCE4Plugin> getAllPlugins ()
  {
    return ContainerHelper.newOrderedSet (m_aPlugins);
  }

  public void addPlugin (@Nonnull final ETinyMCE4Plugin ePlugin)
  {
    if (ePlugin == null)
      throw new NullPointerException ("plugin");
    m_aPlugins.add (ePlugin);
  }

  public void addAllPlugins ()
  {
    for (final ETinyMCE4Plugin ePlugin : ETinyMCE4Plugin.values ())
      m_aPlugins.add (ePlugin);
  }

  public void removePlugin (@Nullable final ETinyMCE4Plugin ePlugin)
  {
    if (ePlugin != null)
      m_aPlugins.remove (ePlugin);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <TinyMCE4ExternalPlugin> getAllExternalPlugins ()
  {
    return ContainerHelper.newOrderedSet (m_aExternalPlugins);
  }

  public void addExternalPlugin (@Nonnull final TinyMCE4ExternalPlugin eExternalPlugin)
  {
    if (eExternalPlugin == null)
      throw new NullPointerException ("plugin");
    m_aExternalPlugins.add (eExternalPlugin);
  }

  public void removeExternalPlugin (@Nullable final TinyMCE4ExternalPlugin eExternalPlugin)
  {
    if (eExternalPlugin != null)
      m_aExternalPlugins.remove (eExternalPlugin);
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

  @Nullable
  public ETinyMCE4Skin getSkin ()
  {
    return m_eSkin;
  }

  /**
   * Select what skin to use, this should match the foldername of the skin.
   * 
   * @param eSkin
   *        Skin to use.
   */
  public void setSkin (@Nullable final ETinyMCE4Skin eSkin)
  {
    m_eSkin = eSkin;
  }

  @Nullable
  public ISimpleURL getSkinURL ()
  {
    return m_aSkinURL;
  }

  /**
   * This option enables you to specify location of the current skin. Enables
   * you to load TinyMCE from one URL for example a CDN then load a local skin
   * on the current server.
   * 
   * @param aSkinURL
   *        The skin URL to use.
   */
  public void setSkinURL (@Nullable final ISimpleURL aSkinURL)
  {
    m_aSkinURL = aSkinURL;
  }

  @Nullable
  public ETinyMCE4Theme getTheme ()
  {
    return m_eTheme;
  }

  /**
   * Set the theme of TinyMCE.
   * 
   * @param eTheme
   *        Theme to use.
   */
  public void setTheme (@Nullable final ETinyMCE4Theme eTheme)
  {
    m_eTheme = eTheme;
  }

  @Nullable
  public ISimpleURL getThemeURL ()
  {
    return m_aThemeURL;
  }

  /**
   * This option enables you to specify location of the current theme. Enables
   * you to load TinyMCE from one URL for example a CDN then load a local theme
   * on the current server.
   * 
   * @param aThemeURL
   *        The theme URL to use.
   */
  public void setThemeURL (@Nullable final ISimpleURL aThemeURL)
  {
    m_aThemeURL = aThemeURL;
  }

  public boolean isInline ()
  {
    return m_eInline.getAsBooleanValue (DEFAULT_INLINE);
  }

  /**
   * This option changes the behaviour of the editor to allow the usage of
   * inline elements instead of a textarea.
   * 
   * @param bInline
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setInline (final boolean bInline)
  {
    m_eInline = ETriState.valueOf (bInline);
  }

  /**
   * This option changes the behaviour of the editor to allow the usage of
   * inline elements instead of a textarea.
   * 
   * @param aInline
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setInline (@Nullable final Boolean aInline)
  {
    m_eInline = ETriState.valueOf (aInline);
  }

  public boolean isHiddenInput ()
  {
    return m_eHiddenInput.getAsBooleanValue (DEFAULT_HIDDEN_INPUT);
  }

  /**
   * This option gives you the ability to disable the auto generation of hidden
   * input fields for inline editing elements. By default all inline editors
   * gets an hidden input element that contents gets saved to when you do
   * editor.save() or tinymce.triggerSave(); this can be disabled if you don't
   * need these controls.
   * 
   * @param bHiddenInput
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setHiddenInput (final boolean bHiddenInput)
  {
    m_eHiddenInput = ETriState.valueOf (bHiddenInput);
  }

  /**
   * This option gives you the ability to disable the auto generation of hidden
   * input fields for inline editing elements. By default all inline editors
   * gets an hidden input element that contents gets saved to when you do
   * editor.save() or tinymce.triggerSave(); this can be disabled if you don't
   * need these controls.
   * 
   * @param aHiddenInput
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setHiddenInput (@Nullable final Boolean aHiddenInput)
  {
    m_eHiddenInput = ETriState.valueOf (aHiddenInput);
  }

  public boolean isConvertFontsToSpans ()
  {
    return m_eConvertFontsToSpans.getAsBooleanValue (DEFAULT_CONVERT_FONTS_TO_SPANS);
  }

  /**
   * If you set this option to true, TinyMCE will convert all font elements to
   * span elements and generate span elements instead of font elements. This
   * option should be used in order to get more W3C compatible code, since font
   * elements are deprecated.
   * 
   * @param bConvertFontsToSpans
   *        <code>true</code> to enabled, <code>false</code> to disable
   */
  public void setConvertFontsToSpans (final boolean bConvertFontsToSpans)
  {
    m_eConvertFontsToSpans = ETriState.valueOf (bConvertFontsToSpans);
  }

  /**
   * If you set this option to true, TinyMCE will convert all font elements to
   * span elements and generate span elements instead of font elements. This
   * option should be used in order to get more W3C compatible code, since font
   * elements are deprecated.
   * 
   * @param aConvertFontsToSpans
   *        <code>true</code> to enabled, <code>false</code> to disable and
   *        <code>null</code> for default value.
   */
  public void setConvertFontsToSpans (@Nullable final Boolean aConvertFontsToSpans)
  {
    m_eConvertFontsToSpans = ETriState.valueOf (aConvertFontsToSpans);
  }

  public void addCustomOption (@Nonnull @Nonempty final String sName, @Nonnull final String sValue)
  {
    addCustomOption (sName, JSExpr.lit (sValue));
  }

  public void addCustomOption (@Nonnull @Nonempty final String sName, @Nonnull final IJSExpression aValue)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    if (aValue == null)
      throw new NullPointerException ("value");
    m_aCustom.put (sName, aValue);
  }

  @Nullable
  public IJSExpression removeCustomOption (@Nullable final String sName)
  {
    return m_aCustom.remove (sName);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, IJSExpression> getAllCustomOptions ()
  {
    return ContainerHelper.newOrderedMap (m_aCustom);
  }

  public boolean containsCustomOption (@Nullable final String sName)
  {
    return m_aCustom.containsKey (sName);
  }

  @Nullable
  public IJSExpression getCustomOptionValue (@Nullable final String sName)
  {
    return m_aCustom.get (sName);
  }

  @Nonnegative
  public int getCustomOptionCount ()
  {
    return m_aCustom.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getJSInitOptions ()
  {
    final JSAssocArray aOptions = new JSAssocArray ();

    // General options
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
    if (!m_aPlugins.isEmpty ())
    {
      final StringBuilder aSB = new StringBuilder ();
      for (final ETinyMCE4Plugin ePlugin : m_aPlugins)
      {
        if (aSB.length () > 0)
          aSB.append (' ');
        aSB.append (ePlugin.getID ());
      }
      aOptions.add ("plugins", aSB.toString ());
    }
    if (!m_aExternalPlugins.isEmpty ())
    {
      final JsonObject aJsonObject = new JsonObject ();
      for (final TinyMCE4ExternalPlugin aExternalPlugin : m_aExternalPlugins)
        aJsonObject.add (aExternalPlugin.getPluginName (), aExternalPlugin.getPluginURL ().getAsString ());
      aOptions.add ("external_plugins", aJsonObject);
    }
    aOptions.add ("selector", m_sSelector);
    if (m_eSkin != null)
      aOptions.add ("skin", m_eSkin.getID ());
    if (m_aSkinURL != null)
      aOptions.add ("skin_url", m_aSkinURL.getAsString ());
    if (m_eTheme != null)
      aOptions.add ("theme", m_eTheme.getID ());
    if (m_aThemeURL != null)
      aOptions.add ("theme_url", m_aThemeURL.getAsString ());
    if (m_eInline.isDefined ())
      aOptions.add ("inline", isInline ());
    if (m_eHiddenInput.isDefined ())
      aOptions.add ("hidden_input", isHiddenInput ());

    // Cleanup/output
    if (m_eConvertFontsToSpans.isDefined ())
      aOptions.add ("convert_fonts_to_spans", isConvertFontsToSpans ());

    // Custom
    for (final Map.Entry <String, IJSExpression> aEntry : m_aCustom.entrySet ())
      aOptions.add (aEntry.getKey (), aEntry.getValue ());

    return aOptions;
  }

  /**
   * @return The JSInvocation with the tinymce.init code and all options
   */
  @Nonnull
  public JSInvocation getJSInvocation ()
  {
    return JSTinyMCE4.init (getJSInitOptions ());
  }

  @Nonnull
  public HCScript build ()
  {
    registerExternalResources ();
    return new HCScript (getJSInvocation ());
  }

  public static final void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (ETinyMCE4JSPathProvider.TINYMCE);
  }
}
