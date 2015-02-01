package com.phloc.webctrls.slider.skin;

import java.util.Map;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.property.ICSSProperty;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.hc.IHCHasCSSStyles;

public enum EBulletSkin
{
  S01 ("01", getDefaultStyles (12, 12), false), //$NON-NLS-1$
  S02 ("02", getDefaultStyles (21, //$NON-NLS-1$
                               21,
                               ArrayHelper.newArray (CCSSProperties.TEXT_ALIGN,
                                                     CCSSProperties.LINE_HEIGHT,
                                                     CCSSProperties.COLOR,
                                                     CCSSProperties.FONT_SIZE),
                               ArrayHelper.newArray (CCSSValue.CENTER, ECSSUnit.px (21), "White", //$NON-NLS-1$
                                                     ECSSUnit.px (12))), true),
  S03 ("03", getDefaultStyles (21, //$NON-NLS-1$
                               21,
                               ArrayHelper.newArray (CCSSProperties.TEXT_ALIGN,
                                                     CCSSProperties.LINE_HEIGHT,
                                                     CCSSProperties.COLOR,
                                                     CCSSProperties.FONT_SIZE),
                               ArrayHelper.newArray (CCSSValue.CENTER, ECSSUnit.px (21), "White", //$NON-NLS-1$
                                                     ECSSUnit.px (12))), true),
  S05 ("05", getDefaultStyles (16, 16), false), //$NON-NLS-1$
  S06 ("06", getDefaultStyles (18, 18), false), //$NON-NLS-1$
  S07 ("07", getDefaultStyles (20, 20), false), //$NON-NLS-1$
  S09 ("09", getDefaultStyles (12, 12), false), //$NON-NLS-1$
  S10 ("10", getDefaultStyles (11, 11), false), //$NON-NLS-1$
  S11 ("11", getDefaultStyles (11, 11), false), //$NON-NLS-1$
  S12 ("12", getDefaultStyles (16, 16), false), //$NON-NLS-1$
  S13 ("13", getDefaultStyles (21, 21), false), //$NON-NLS-1$
  S14 ("14", getDefaultStyles (12, 12), false), //$NON-NLS-1$
  S16 ("16", getDefaultStyles (21, 21), false), //$NON-NLS-1$
  S17 ("17", getDefaultStyles (16, 16), false), //$NON-NLS-1$
  S18 ("18", getDefaultStyles (24, //$NON-NLS-1$
                               24,
                               ArrayHelper.newArray (CCSSProperties.TEXT_ALIGN,
                                                     CCSSProperties.LINE_HEIGHT,
                                                     CCSSProperties.FONT_SIZE),
                               ArrayHelper.newArray (CCSSValue.CENTER, ECSSUnit.px (24), ECSSUnit.px (16))), true),
  S20 ("20", getDefaultStyles (19, //$NON-NLS-1$
                               19,
                               ArrayHelper.newArray (CCSSProperties.TEXT_ALIGN,
                                                     CCSSProperties.LINE_HEIGHT,
                                                     CCSSProperties.COLOR,
                                                     CCSSProperties.FONT_SIZE),
                               ArrayHelper.newArray (CCSSValue.CENTER, ECSSUnit.px (19), "White", //$NON-NLS-1$
                                                     ECSSUnit.px (12))), true),
  S21 ("21", getDefaultStyles (19, //$NON-NLS-1$
                               19,
                               ArrayHelper.newArray (CCSSProperties.TEXT_ALIGN,
                                                     CCSSProperties.LINE_HEIGHT,
                                                     CCSSProperties.COLOR,
                                                     CCSSProperties.FONT_SIZE),
                               ArrayHelper.newArray (CCSSValue.CENTER, ECSSUnit.px (19), "White", //$NON-NLS-1$
                                                     ECSSUnit.px (12))), true);

  private String m_sID;
  private Map <ICSSProperty, String> m_aStyles;
  private boolean m_bNumberTemplate;

  private EBulletSkin (@Nonnull @Nonempty final String sID,
                       @Nonnull final Map <ICSSProperty, String> aStyles,
                       final boolean bNumberTemplate)
  {
    if (StringHelper.hasNoText (sID))
    {
      throw new IllegalArgumentException ("sID must not be null or empty!"); //$NON-NLS-1$
    }
    if (aStyles == null)
    {
      throw new NullPointerException ("aStyles"); //$NON-NLS-1$
    }
    this.m_sID = sID;
    this.m_aStyles = aStyles;
    this.m_bNumberTemplate = bNumberTemplate;
  }

  // TODO not yet evaluated
  public boolean isNumberTemplate ()
  {
    return this.m_bNumberTemplate;
  }

  private static final Map <ICSSProperty, String> getDefaultStyles (final int nWidth, final int nHeight)
  {
    final Map <ICSSProperty, String> aStyles = ContainerHelper.newMap ();
    aStyles.put (CCSSProperties.POSITION, CCSSValue.ABSOLUTE);
    aStyles.put (CCSSProperties.WIDTH, ECSSUnit.px (nWidth));
    aStyles.put (CCSSProperties.HEIGHT, ECSSUnit.px (nHeight));
    return aStyles;
  }

  private static final Map <ICSSProperty, String> getDefaultStyles (final int nWidth,
                                                                    final int nHeight,
                                                                    final ICSSProperty [] aProperties,
                                                                    final String [] aValues)
  {
    final Map <ICSSProperty, String> aStyles = getDefaultStyles (nWidth, nHeight);
    aStyles.putAll (ContainerHelper.newMap (aProperties, aValues));
    return aStyles;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  public void applyStyles (final IHCHasCSSStyles <?> aElement)
  {
    for (final Map.Entry <ICSSProperty, String> aEntry : this.m_aStyles.entrySet ())
    {
      aElement.addStyle (aEntry.getKey ().newValue (aEntry.getValue ()));
    }
  }
}
