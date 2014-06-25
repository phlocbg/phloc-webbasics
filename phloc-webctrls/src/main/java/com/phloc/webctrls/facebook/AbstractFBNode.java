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
package com.phloc.webctrls.facebook;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.impl.AbstractHCNode;

/**
 * Abstract base class for FB nodes
 * 
 * @author Philip Helger
 */
public abstract class AbstractFBNode extends AbstractHCNode
{
  private final String m_sElementName;

  public AbstractFBNode (@Nonnull @Nonempty final String sElementName)
  {
    m_sElementName = ValueEnforcer.notEmpty (sElementName, "ElementName");
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
    return new MicroElement (CFacebook.FACEBOOK_NAMESPACE_URI, m_sElementName);
  }

  /**
   * Apply custom properties to the passed {@link IMicroElement}
   * 
   * @param aElement
   *        Micro element. Never <code>null</code>.
   * @param aConversionSettings
   *        The conversion settings to be used. Never <code>null</code>.
   */
  @OverrideOnDemand
  protected void applyProperties (@Nonnull final IMicroElement aElement,
                                  @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {}

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
  @OverridingMethodsMustInvokeSuper
  protected void finishAfterApplyProperties (@Nonnull final IMicroElement eElement,
                                             @Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    // FB tags may never be self-closed!
    if (!eElement.hasChildren ())
      eElement.appendText ("");
  }

  @Override
  @Nonnull
  protected IMicroNode internalConvertToNode (@Nonnull final IHCConversionSettingsToNode aConversionSettings)
  {
    // Create the element itself
    final IMicroElement ret = createElement (aConversionSettings);

    // Customize element
    applyProperties (ret, aConversionSettings);

    // Optional callback after everything was done (implementation dependent)
    finishAfterApplyProperties (ret, aConversionSettings);
    return ret;
  }
}
