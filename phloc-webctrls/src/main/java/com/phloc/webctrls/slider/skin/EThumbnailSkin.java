package com.phloc.webctrls.slider.skin;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.property.ICSSProperty;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.webctrls.slider.skin.thumbnail.IThumbnailSkin;
import com.phloc.webctrls.slider.skin.thumbnail.ThumbnailSkin_IO;
import com.phloc.webctrls.slider.skin.thumbnail.ThumbnailSkin_OBIF;
import com.phloc.webctrls.slider.skin.thumbnail.ThumbnailSkin_T;
import com.phloc.webctrls.slider.skin.thumbnail.ThumbnailSkin_WC;

public enum EThumbnailSkin
{
  S01 ("jssort01", 800, 100, 72, 72, new ThumbnailSkin_WC (), null, null), //$NON-NLS-1$
  S02 ("jssort02", 240, 480, 99, 66, new ThumbnailSkin_WC (), null, null), //$NON-NLS-1$
  S03 ("jssort03", 600, 60, 62, 32, new ThumbnailSkin_WC (ArrayHelper.newArray (CCSSProperties.POSITION, //$NON-NLS-1$
                                                                                CCSSProperties.BACKGROUND_COLOR,
                                                                                CCSSProperties.TOP,
                                                                                CCSSProperties.LEFT),
                                                          ArrayHelper.newArray (CCSSValue.ABSOLUTE, "#000", //$NON-NLS-1$
                                                                                ECSSUnit.px (0),
                                                                                ECSSUnit.px (0))), ArrayHelper.newArray (CCSSProperties.BACKGROUND_COLOR,
                                                                                                                         CCSSProperties.FILTER,
                                                                                                                         CCSSProperties.OPACITY,
                                                                                                                         CCSSProperties.WIDTH,
                                                                                                                         CCSSProperties.HEIGHT), ArrayHelper.newArray ("#000", //$NON-NLS-1$
                                                                                                                                                                       "alpha(opacity=30)", //$NON-NLS-1$
                                                                                                                                                                       ".3", //$NON-NLS-1$
                                                                                                                                                                       ECSSUnit.perc (100),
                                                                                                                                                                       ECSSUnit.perc (100))),
  S04 ("jssort04", 600, 60, 62, 32, new ThumbnailSkin_WC (ArrayHelper.newArray (CCSSProperties.POSITION, //$NON-NLS-1$
                                                                                CCSSProperties.BACKGROUND_COLOR,
                                                                                CCSSProperties.TOP,
                                                                                CCSSProperties.LEFT),
                                                          ArrayHelper.newArray (CCSSValue.ABSOLUTE, "#000",//$NON-NLS-1$
                                                                                ECSSUnit.px (0),
                                                                                ECSSUnit.px (0))), null, null),
  S05 ("jssort05", 800, 100, 72, 72, new ThumbnailSkin_OBIF (72, 72, 1, 1), null, null), //$NON-NLS-1$
  S06 ("jssort06", 240, 480, 99, 66, new ThumbnailSkin_OBIF (99, 66, 0, 0), null, null), //$NON-NLS-1$
  S07 ("jssort07", 800, 100, 72, 72, new ThumbnailSkin_IO (), null, null), //$NON-NLS-1$
  S08 ("jssort08", 600, 100, 50, 50, new ThumbnailSkin_IO (), null, null), //$NON-NLS-1$
  S09 ("sliderb-T", 600, 45, 600, 45, new ThumbnailSkin_T (ECSSUnit.perc (100), ECSSUnit.perc (100), 45, 20, null, true), ArrayHelper.newArray (CCSSProperties.FILTER, //$NON-NLS-1$
                                                                                                                                                CCSSProperties.OPACITY,
                                                                                                                                                CCSSProperties.POSITION,
                                                                                                                                                CCSSProperties.DISPLAY,
                                                                                                                                                CCSSProperties.BACKGROUND_COLOR,
                                                                                                                                                CCSSProperties.TOP,
                                                                                                                                                CCSSProperties.LEFT,
                                                                                                                                                CCSSProperties.WIDTH,
                                                                                                                                                CCSSProperties.HEIGHT), ArrayHelper.newArray ("alpha(opacity=40)", //$NON-NLS-1$
                                                                                                                                                                                              ".4", //$NON-NLS-1$
                                                                                                                                                                                              CCSSValue.ABSOLUTE,
                                                                                                                                                                                              CCSSValue.BLOCK,
                                                                                                                                                                                              "#000000", //$NON-NLS-1$
                                                                                                                                                                                              ECSSUnit.px (0),
                                                                                                                                                                                              ECSSUnit.px (0),
                                                                                                                                                                                              ECSSUnit.perc (100),
                                                                                                                                                                                              ECSSUnit.perc (100))),
  S10 ("slider1-T", 600, 30, 600, 30, new ThumbnailSkin_T (ECSSUnit.perc (100), ECSSUnit.perc (100), 30, 16, null, true), ArrayHelper.newArray (CCSSProperties.FILTER, //$NON-NLS-1$
                                                                                                                                                CCSSProperties.OPACITY,
                                                                                                                                                CCSSProperties.POSITION,
                                                                                                                                                CCSSProperties.DISPLAY,
                                                                                                                                                CCSSProperties.BACKGROUND_COLOR,
                                                                                                                                                CCSSProperties.TOP,
                                                                                                                                                CCSSProperties.LEFT,
                                                                                                                                                CCSSProperties.WIDTH,
                                                                                                                                                CCSSProperties.HEIGHT), ArrayHelper.newArray ("alpha(opacity=50)", //$NON-NLS-1$
                                                                                                                                                                                              ".5", //$NON-NLS-1$
                                                                                                                                                                                              CCSSValue.ABSOLUTE,
                                                                                                                                                                                              CCSSValue.BLOCK,
                                                                                                                                                                                              "#000000", //$NON-NLS-1$
                                                                                                                                                                                              ECSSUnit.px (0),
                                                                                                                                                                                              ECSSUnit.px (0),
                                                                                                                                                                                              ECSSUnit.perc (100),
                                                                                                                                                                                              ECSSUnit.perc (100))),
  S11 ("jssort11", 600, 60, 60, 30, new ThumbnailSkin_T (ECSSUnit.px (60), ECSSUnit.px (30), 0, 0, "t", false), ArrayHelper.newArray (CCSSProperties.BACKGROUND_COLOR, //$NON-NLS-1$ //$NON-NLS-2$
                                                                                                                                      CCSSProperties.FILTER,
                                                                                                                                      CCSSProperties.OPACITY,
                                                                                                                                      CCSSProperties.WIDTH,
                                                                                                                                      CCSSProperties.HEIGHT), ArrayHelper.newArray ("#000", //$NON-NLS-1$
                                                                                                                                                                                    "alpha(opacity=30)", //$NON-NLS-1$
                                                                                                                                                                                    ".3", //$NON-NLS-1$
                                                                                                                                                                                    ECSSUnit.perc (100),
                                                                                                                                                                                    ECSSUnit.perc (100))),
  S12 ("jssort12", 600, 100, 200, 100, new ThumbnailSkin_T (ECSSUnit.px (200), ECSSUnit.px (100), 0, 0, null, false), null, null); //$NON-NLS-1$

  private String m_sID;
  private IThumbnailSkin m_aSkinContent;
  private int m_nWidth;
  private int m_nHeight;
  private int m_nPrototypeWidth;
  private int m_nPrototypeHeight;
  private final ICSSProperty [] m_aMaskProperties;
  private final String [] m_aMaskValues;

  private EThumbnailSkin (@Nonnull @Nonempty final String sID,
                          final int nWidth,
                          final int nHeight,
                          final int nPrototypeWidth,
                          final int nPrototypeHeight,
                          @Nonnull final IThumbnailSkin aSkinContent,
                          @Nullable final ICSSProperty [] aMaskProperties,
                          @Nullable final String [] aMaskValues)
  {
    if (StringHelper.hasNoText (sID))
    {
      throw new IllegalArgumentException ("sID must not be null or empty!"); //$NON-NLS-1$
    }
    if (aSkinContent == null)
    {
      throw new NullPointerException ("aSkinContent"); //$NON-NLS-1$
    }
    this.m_sID = sID;
    this.m_nWidth = nWidth;
    this.m_nHeight = nHeight;
    this.m_nPrototypeWidth = nPrototypeWidth;
    this.m_nPrototypeHeight = nPrototypeHeight;
    this.m_aSkinContent = aSkinContent;
    this.m_aMaskProperties = aMaskProperties;
    this.m_aMaskValues = aMaskValues;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  public int getWidth ()
  {
    return this.m_nWidth;
  }

  public int getHeight ()
  {
    return this.m_nHeight;
  }

  public int getPrototypeWidth ()
  {
    return this.m_nPrototypeWidth;
  }

  public int getPrototypeHeight ()
  {
    return this.m_nPrototypeHeight;
  }

  public void addPrototypeContent (final HCDiv aPrototype)
  {
    for (final IHCNode aContent : this.m_aSkinContent.getPrototypeContent ())
    {
      aPrototype.addChild (aContent);
    }
  }

  public void addOptionalMask (final HCDiv aNavigator)
  {
    final Map <ICSSProperty, String> aMaskStyles = ContainerHelper.newMap (this.m_aMaskProperties, this.m_aMaskValues);
    if (ContainerHelper.isNotEmpty (aMaskStyles))
    {
      final HCDiv aMask = new HCDiv ();
      for (final Map.Entry <ICSSProperty, String> aEntry : aMaskStyles.entrySet ())
      {
        aMask.addStyle (aEntry.getKey ().newValue (aEntry.getValue ()));
      }
      aNavigator.addChild (aMask);
    }
  }
}
