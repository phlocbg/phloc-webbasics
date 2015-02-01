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
package com.phloc.webctrls.slider.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.css.ICSSWriterSettings;
import com.phloc.css.property.CSSPropertyFree;
import com.phloc.css.property.ECSSProperty;
import com.phloc.css.propertyvalue.ICSSValue;
import com.phloc.html.CHTMLAttributes;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.impl.AbstractHCNode;

// TODO: move to phloc HTML

@NotThreadSafe
public class AbstractCustomHCElement <THISTYPE extends AbstractCustomHCElement <THISTYPE>> extends AbstractHCNode
{
  private static final long serialVersionUID = 7681182043706217442L;

  /** The cached element name */
  private final String m_sElementName;

  // Must be a LinkedHashSet:
  private Set <ICSSClassProvider> m_aCSSClassProviders;
  // Must be a LinkedHashMap:
  private Map <ECSSProperty, ICSSValue> m_aStyles;

  // Must be a LinkedHashMap_
  private Map <String, String> m_aCustomAttrs;

  protected AbstractCustomHCElement (@Nonnull final String sElementName)
  {
    this.m_sElementName = sElementName;
  }

  @Nonnull
  @Nonempty
  public final String getTagName ()
  {
    return this.m_sElementName;
  }

  @Nonnull
  protected final THISTYPE thisAsT ()
  {
    // Avoid the unchecked cast warning in all places
    return GenericReflection.<AbstractCustomHCElement <THISTYPE>, THISTYPE> uncheckedCast (this);
  }

  public final boolean containsClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    return this.m_aCSSClassProviders != null &&
           aCSSClassProvider != null &&
           this.m_aCSSClassProviders.contains (aCSSClassProvider);
  }

  @Nonnull
  public final THISTYPE addClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    if (aCSSClassProvider != null)
    {
      if (this.m_aCSSClassProviders == null)
        this.m_aCSSClassProviders = new LinkedHashSet <ICSSClassProvider> ();
      this.m_aCSSClassProviders.add (aCSSClassProvider);
    }
    return thisAsT ();
  }

  @Deprecated
  @Nonnull
  public final THISTYPE addClasses (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    return addClass (aCSSClassProvider);
  }

  @Nonnull
  public final THISTYPE addClasses (@Nullable final ICSSClassProvider... aCSSClassProviders)
  {
    if (aCSSClassProviders != null)
      for (final ICSSClassProvider aProvider : aCSSClassProviders)
        addClass (aProvider);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE addClasses (@Nullable final Iterable <? extends ICSSClassProvider> aCSSClassProviders)
  {
    if (aCSSClassProviders != null)
      for (final ICSSClassProvider aProvider : aCSSClassProviders)
        addClass (aProvider);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE removeClass (@Nullable final ICSSClassProvider aCSSClassProvider)
  {
    if (this.m_aCSSClassProviders != null && aCSSClassProvider != null)
      this.m_aCSSClassProviders.remove (aCSSClassProvider);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE removeAllClasses ()
  {
    if (this.m_aCSSClassProviders != null)
      this.m_aCSSClassProviders.clear ();
    return thisAsT ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <ICSSClassProvider> getAllClasses ()
  {
    return ContainerHelper.newOrderedSet (this.m_aCSSClassProviders);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Set <String> getAllClassNames ()
  {
    final Set <String> ret = new LinkedHashSet <String> ();
    if (this.m_aCSSClassProviders != null)
      for (final ICSSClassProvider aCSSClassProvider : this.m_aCSSClassProviders)
      {
        final String sCSSClass = aCSSClassProvider.getCSSClass ();
        if (StringHelper.hasText (sCSSClass))
          ret.add (sCSSClass);
      }
    return ret;
  }

  public final boolean hasAnyClass ()
  {
    return this.m_aCSSClassProviders != null && !this.m_aCSSClassProviders.isEmpty ();
  }

  @Nullable
  public final String getAllClassesAsString ()
  {
    if (this.m_aCSSClassProviders == null || this.m_aCSSClassProviders.isEmpty ())
      return null;

    final StringBuilder aSB = new StringBuilder ();
    for (final ICSSClassProvider aCSSClassProvider : this.m_aCSSClassProviders)
    {
      final String sCSSClass = aCSSClassProvider.getCSSClass ();
      if (StringHelper.hasText (sCSSClass))
      {
        if (aSB.length () > 0)
          aSB.append (' ');
        aSB.append (sCSSClass);
      }
    }
    return aSB.toString ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <ECSSProperty, ICSSValue> getAllStyles ()
  {
    return ContainerHelper.newMap (this.m_aStyles);
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Collection <ICSSValue> getAllStyleValues ()
  {
    return this.m_aStyles == null ? new ArrayList <ICSSValue> () : ContainerHelper.newList (this.m_aStyles.values ());
  }

  @Nullable
  public final ICSSValue getStyleValue (@Nullable final ECSSProperty eProperty)
  {
    return eProperty == null || this.m_aStyles == null ? null : this.m_aStyles.get (eProperty);
  }

  public final boolean containsStyle (@Nullable final ECSSProperty eProperty)
  {
    return this.m_aStyles != null && this.m_aStyles.containsKey (eProperty);
  }

  public final boolean hasStyle (@Nullable final ICSSValue aValue)
  {
    if (aValue == null || this.m_aStyles == null)
      return false;

    // Contained styles can never have a null value!
    final ECSSProperty eProp = aValue.getProp ();
    return EqualsUtils.equals (this.m_aStyles.get (eProp), aValue);
  }

  public final boolean hasAnyStyle ()
  {
    return this.m_aStyles != null && !this.m_aStyles.isEmpty ();
  }

  @Nonnull
  public final THISTYPE addStyle (@Nonnull final ECSSProperty eProperty, @Nonnull @Nonempty final String sPropertyValue)
  {
    return addStyle (new CSSPropertyFree (eProperty).newValue (sPropertyValue));
  }

  @Nonnull
  public final THISTYPE addStyle (@Nullable final ICSSValue aValue)
  {
    if (aValue != null)
    {
      if (this.m_aStyles == null)
        this.m_aStyles = new LinkedHashMap <ECSSProperty, ICSSValue> ();
      this.m_aStyles.put (aValue.getProp (), aValue);
    }
    return thisAsT ();
  }

  @Nonnull
  @Deprecated
  public final THISTYPE addStyles (@Nullable final ICSSValue aValue)
  {
    return addStyle (aValue);
  }

  @Nonnull
  public final THISTYPE addStyles (@Nullable final ICSSValue... aValues)
  {
    if (aValues != null)
      for (final ICSSValue aValue : aValues)
        addStyle (aValue);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE addStyles (@Nullable final Iterable <? extends ICSSValue> aValues)
  {
    if (aValues != null)
      for (final ICSSValue aValue : aValues)
        addStyle (aValue);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE removeStyle (@Nonnull final ECSSProperty eProperty)
  {
    if (this.m_aStyles != null)
      this.m_aStyles.remove (eProperty);
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE removeAllStyles ()
  {
    this.m_aStyles.clear ();
    return thisAsT ();
  }

  @Nullable
  public final String getAllStylesAsString (@Nonnull final ICSSWriterSettings aCSSSettings)
  {
    if (this.m_aStyles == null || this.m_aStyles.isEmpty ())
      return null;
    final StringBuilder aSB = new StringBuilder ();
    for (final ICSSValue aValue : this.m_aStyles.values ())
      aSB.append (aValue.getAsCSSString (aCSSSettings, 0));
    return aSB.toString ();
  }

  public boolean hasCustomAttrs ()
  {
    return ContainerHelper.isNotEmpty (this.m_aCustomAttrs);
  }

  @Nonnegative
  public int getCustomAttrCount ()
  {
    return ContainerHelper.getSize (this.m_aCustomAttrs);
  }

  public boolean containsCustomAttr (@Nullable final String sName)
  {
    return this.m_aCustomAttrs != null && StringHelper.hasText (sName) ? this.m_aCustomAttrs.containsKey (sName)
                                                                      : false;
  }

  @Nullable
  public final String getCustomAttrValue (@Nullable final String sName)
  {
    return this.m_aCustomAttrs != null && StringHelper.hasText (sName) ? this.m_aCustomAttrs.get (sName) : null;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final Map <String, String> getAllCustomAttrs ()
  {
    return ContainerHelper.newOrderedMap (this.m_aCustomAttrs);
  }

  @Nonnull
  public final THISTYPE setCustomAttr (@Nullable final String sName, @Nullable final String sValue)
  {
    if (StringHelper.hasText (sName) && sValue != null)
    {
      if (this.m_aCustomAttrs == null)
        this.m_aCustomAttrs = new LinkedHashMap <String, String> ();
      this.m_aCustomAttrs.put (sName, sValue);
    }
    return thisAsT ();
  }

  @Nonnull
  public final THISTYPE setCustomAttr (@Nullable final String sName, final int nValue)
  {
    return setCustomAttr (sName, Integer.toString (nValue));
  }

  @Nonnull
  public final THISTYPE setCustomAttr (@Nullable final String sName, final long nValue)
  {
    return setCustomAttr (sName, Long.toString (nValue));
  }

  @Nonnull
  public final THISTYPE removeCustomAttr (@Nullable final String sName)
  {
    if (this.m_aCustomAttrs != null && sName != null)
      this.m_aCustomAttrs.remove (sName);
    return thisAsT ();
  }

  public static boolean isDataAttrName (@Nullable final String sName)
  {
    return StringHelper.startsWith (sName, CHTMLAttributes.HTML5_PREFIX_DATA);
  }

  @Nullable
  public static String makeDataAttrName (@Nullable final String sName)
  {
    return StringHelper.hasNoText (sName) ? sName : CHTMLAttributes.HTML5_PREFIX_DATA + sName;
  }

  public boolean hasDataAttrs ()
  {
    if (this.m_aCustomAttrs != null)
      for (final String sName : this.m_aCustomAttrs.keySet ())
        if (isDataAttrName (sName))
          return true;
    return false;
  }

  public boolean containsDataAttr (@Nullable final String sName)
  {
    return containsCustomAttr (makeDataAttrName (sName));
  }

  @Nullable
  public String getDataAttrValue (@Nullable final String sName)
  {
    return getCustomAttrValue (makeDataAttrName (sName));
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAllDataAttrs ()
  {
    final Map <String, String> ret = new LinkedHashMap <String, String> ();
    if (this.m_aCustomAttrs != null)
      for (final Map.Entry <String, String> aEntry : this.m_aCustomAttrs.entrySet ())
        if (isDataAttrName (aEntry.getKey ()))
          ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  public THISTYPE setDataAttr (@Nullable final String sName, final int nValue)
  {
    return setCustomAttr (makeDataAttrName (sName), nValue);
  }

  @Nonnull
  public THISTYPE setDataAttr (@Nullable final String sName, final long nValue)
  {
    return setCustomAttr (makeDataAttrName (sName), nValue);
  }

  @Nonnull
  public THISTYPE setDataAttr (@Nullable final String sName, @Nullable final String sValue)
  {
    return setCustomAttr (makeDataAttrName (sName), sValue);
  }

  @Nonnull
  public THISTYPE removeDataAttr (@Nullable final String sName)
  {
    return removeCustomAttr (makeDataAttrName (sName));
  }

  /**
   * @param aConversionSettings
   *        The conversion settings to be used
   * @return The created micro element for this HC element. May not be
   *         <code>null</code>.
   */
  @OverrideOnDemand
  @Nonnull
  protected IMicroElement createElement (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    return new MicroElement (aConversionSettings.getHTMLNamespaceURI (), this.m_sElementName);
  }

  /**
   * Set all attributes and child elements of this object
   * 
   * @param aElement
   *        The current micro element to be filled
   * @param aConversionSettings
   *        The conversion settings to be used
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void applyProperties (@Nonnull final IMicroElement aElement,
                                  @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    aElement.setAttribute (CHTMLAttributes.CLASS, getAllClassesAsString ());

    aElement.setAttribute (CHTMLAttributes.STYLE, getAllStylesAsString (aConversionSettings.getCSSWriterSettings ()));

    if (ContainerHelper.isNotEmpty (this.m_aCustomAttrs))
    {
      for (final Map.Entry <String, String> aEntry : this.m_aCustomAttrs.entrySet ())
      {
        aElement.setAttribute (aEntry.getKey (), aEntry.getValue ());
      }
    }
  }

  /**
   * This method is called after the element itself was created and filled.
   * Overwrite this method to perform actions that can only be done after the
   * element was build finally.
   * 
   * @param eElement
   *        The created micro element
   * @param aConversionSettings
   *        The conversion settings to be used
   */
  @OverrideOnDemand
  protected void finishAfterApplyProperties (@Nonnull final IMicroElement eElement,
                                             @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {}

  /*
   * Note: return type cannot by IMicroElement since the checkbox object
   * delivers an IMicroNodeList!
   */
  @Override
  @Nonnull
  @OverridingMethodsMustInvokeSuper
  protected IMicroNode internalConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {

    // Create the element
    final IMicroElement ret = createElement (aConversionSettings);
    if (ret == null)
      throw new IllegalStateException ("Created a null element!"); //$NON-NLS-1$

    // Set all HTML attributes etc.
    applyProperties (ret, aConversionSettings);

    // Optional callback after everything was done (implementation dependent)
    finishAfterApplyProperties (ret, aConversionSettings);
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("element", this.m_sElementName) //$NON-NLS-1$
                                       .appendIfNotNull ("classes", this.m_aCSSClassProviders) //$NON-NLS-1$
                                       .appendIfNotNull ("styles", this.m_aStyles) //$NON-NLS-1$
                                       .appendIfNotNull ("customAttrs", this.m_aCustomAttrs) //$NON-NLS-1$
                                       .toString ();
  }

}
