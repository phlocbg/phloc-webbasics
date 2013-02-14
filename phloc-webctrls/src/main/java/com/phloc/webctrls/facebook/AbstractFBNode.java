package com.phloc.webctrls.facebook;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.conversion.IHCConversionSettingsToNode;
import com.phloc.html.hc.impl.AbstractHCNode;

/**
 * Abstract base class for FB nodes
 * 
 * @author philip
 */
public abstract class AbstractFBNode extends AbstractHCNode
{
  public static final String NAMESPACE_URI = "http://ogp.me/ns/fb#";
  private final String m_sElementName;

  public AbstractFBNode (@Nonnull @Nonempty final String sElementName)
  {
    if (StringHelper.hasNoText (sElementName))
      throw new IllegalArgumentException ("elementName");
    m_sElementName = sElementName;
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
    return new MicroElement (NAMESPACE_URI, m_sElementName);
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
