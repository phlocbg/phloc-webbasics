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
package com.phloc.webctrls.autonumeric;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.lang.DecimalFormatSymbolsFactory;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCHasChildrenMutable;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.ext.IHCNodeWithJSOptions;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSGlobal;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;
import com.phloc.webbasics.form.RequestField;

/**
 * jQuery autoNumeric plugin from
 * 
 * <pre>
 * http://www.decorplanit.com/plugin/
 * </pre>
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class HCAutoNumeric extends HCEdit implements IHCNodeWithJSOptions
{
  /** The special CSS class to use for numeric inputs */
  public static final ICSSClassProvider CSS_CLASS_AUTO_NUMERIC_EDIT = DefaultCSSClassProvider.create ("auto-numeric-edit");

  public static final int DEFAULT_MIN_VALUE = -999999999;

  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static String s_sDefaultThousandSeparator = null;

  private String m_sThousandSeparator;
  private String m_sDecimalSeparator;
  private Integer m_aDecimalPlaces;
  private BigDecimal m_aMin;
  private BigDecimal m_aMax;
  private EAutoNumericLeadingZero m_eLeadingZero;
  private EAutoNumericRoundingMode m_eRoundingMode;

  public HCAutoNumeric (@Nonnull final RequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    super (aRF);
    ValueEnforcer.notNull (aDisplayLocale, "DisplayLocale");

    ensureID ();
    addClass (CSS_CLASS_AUTO_NUMERIC_EDIT);

    // Because the default min value is 0 (in v1.8.2) and we want negative
    // values by default!
    m_aMin = BigDecimal.valueOf (DEFAULT_MIN_VALUE);

    final DecimalFormatSymbols m_aDFS = DecimalFormatSymbolsFactory.getInstance (aDisplayLocale);
    m_sThousandSeparator = Character.toString (m_aDFS.getGroupingSeparator ());
    m_sDecimalSeparator = Character.toString (m_aDFS.getDecimalSeparator ());

    // Assign default
    final String sDefaultThousandSeparator = getDefaultThousandSeparator ();
    if (sDefaultThousandSeparator != null)
      m_sThousandSeparator = sDefaultThousandSeparator;
  }

  @Nullable
  public static String getDefaultThousandSeparator ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_sDefaultThousandSeparator;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  public static void setDefaultThousandSeparator (@Nullable final String sDefaultThousandSeparator)
  {
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_sDefaultThousandSeparator = sDefaultThousandSeparator;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public String getThousandSeparator ()
  {
    return m_sThousandSeparator;
  }

  @Nonnull
  public HCAutoNumeric setThousandSeparator (@Nullable final String sThousandSeparator)
  {
    m_sThousandSeparator = sThousandSeparator;
    return this;
  }

  @Nullable
  public String getDecimalSeparator ()
  {
    return m_sDecimalSeparator;
  }

  @Nonnull
  public HCAutoNumeric setDecimalSeparator (@Nullable final String sDecimalSeparator)
  {
    m_sDecimalSeparator = sDecimalSeparator;
    return this;
  }

  @Nullable
  public Integer getDecimalPlaces ()
  {
    return m_aDecimalPlaces;
  }

  @Nonnull
  public HCAutoNumeric setDecimalPlaces (final int nDecimalPlaces)
  {
    m_aDecimalPlaces = Integer.valueOf (nDecimalPlaces);
    return this;
  }

  private void _checkMinMax ()
  {
    if (m_aMin != null && m_aMax != null && m_aMin.compareTo (m_aMax) > 0)
      throw new IllegalArgumentException ("Min (" + m_aMin + ") must be <= max (" + m_aMax + ")!");
  }

  @Nullable
  public BigDecimal getMin ()
  {
    return m_aMin;
  }

  @Nonnull
  public HCAutoNumeric setMin (final long nMin)
  {
    return setMin (BigDecimal.valueOf (nMin));
  }

  @Nonnull
  public HCAutoNumeric setMin (final double dMin)
  {
    return setMin (BigDecimal.valueOf (dMin));
  }

  @Nonnull
  public HCAutoNumeric setMin (@Nullable final BigDecimal aMin)
  {
    m_aMin = aMin;
    _checkMinMax ();
    return this;
  }

  @Nullable
  public BigDecimal getMax ()
  {
    return m_aMax;
  }

  @Nonnull
  public HCAutoNumeric setMax (final long nMax)
  {
    return setMax (BigDecimal.valueOf (nMax));
  }

  @Nonnull
  public HCAutoNumeric setMax (final double dMax)
  {
    return setMax (BigDecimal.valueOf (dMax));
  }

  @Nonnull
  public HCAutoNumeric setMax (@Nullable final BigDecimal aMax)
  {
    m_aMax = aMax;
    _checkMinMax ();
    return this;
  }

  @Nullable
  public EAutoNumericLeadingZero getLeadingZero ()
  {
    return m_eLeadingZero;
  }

  @Nonnull
  public HCAutoNumeric setLeadingZero (@Nullable final EAutoNumericLeadingZero eLeadingZero)
  {
    m_eLeadingZero = eLeadingZero;
    return this;
  }

  @Nullable
  public EAutoNumericRoundingMode getRoundingMode ()
  {
    return m_eRoundingMode;
  }

  @Nonnull
  public HCAutoNumeric setRoundingMode (@Nullable final EAutoNumericRoundingMode eRoundingMode)
  {
    m_eRoundingMode = eRoundingMode;
    return this;
  }

  @Nonnull
  public static JSInvocation invoke (@Nonnull final IJSExpression aExpr)
  {
    return aExpr.invoke ("autoNumeric");
  }

  @Nonnull
  public JSInvocation invoke ()
  {
    return invoke (JQuery.idRef (this));
  }

  @Nonnull
  public static JSInvocation autoNumericInit (@Nonnull final IJSExpression aAutoNumeric)
  {
    return invoke (aAutoNumeric).arg ("init");
  }

  @Nonnull
  public static JSInvocation autoNumericInit (@Nonnull final IJSExpression aAutoNumeric,
                                              @Nonnull final JSAssocArray aOptions)
  {
    return autoNumericInit (aAutoNumeric).arg (aOptions);
  }

  @Nonnull
  public JSInvocation autoNumericInit ()
  {
    return invoke ().arg ("init");
  }

  @Nonnull
  public JSInvocation autoNumericInit (@Nonnull final JSAssocArray aOptions)
  {
    return autoNumericInit ().arg (aOptions);
  }

  @Nonnull
  public JSInvocation autoNumericDestroy ()
  {
    return invoke ().arg ("destroy");
  }

  @Nonnull
  public JSInvocation autoNumericUpdate ()
  {
    return invoke ().arg ("update");
  }

  @Nonnull
  public static JSInvocation autoNumericSet (@Nonnull final IJSExpression aAutoNumeric,
                                             @Nonnull final IJSExpression aValueToSet)
  {
    return invoke (aAutoNumeric).arg ("set").arg (aValueToSet);
  }

  @Nonnull
  public static JSInvocation autoNumericSet (@Nonnull final IJSExpression aAutoNumeric, final int nValueToSet)
  {
    return autoNumericSet (aAutoNumeric, JSExpr.lit (nValueToSet));
  }

  @Nonnull
  public static JSInvocation autoNumericSet (@Nonnull final IJSExpression aAutoNumeric,
                                             @Nonnull final BigDecimal aValueToSet)
  {
    return autoNumericSet (aAutoNumeric, JSExpr.lit (aValueToSet));
  }

  @Nonnull
  public JSInvocation autoNumericSet ()
  {
    return invoke ().arg ("set");
  }

  @Nonnull
  public JSInvocation autoNumericSet (final int nValue)
  {
    return autoNumericSet ().arg (nValue);
  }

  @Nonnull
  public JSInvocation autoNumericSet (@Nonnull final BigDecimal aValueToSet)
  {
    return autoNumericSet ().arg (aValueToSet);
  }

  @Nonnull
  public JSInvocation autoNumericSet (@Nonnull final IJSExpression aExpr)
  {
    return autoNumericSet ().arg (aExpr);
  }

  @Nonnull
  public static IJSExpression autoNumericGet (@Nonnull final IJSExpression aAutoNumeric)
  {
    // Remember: the result is a String!!
    return invoke (aAutoNumeric).arg ("get");
  }

  @Nonnull
  public static IJSExpression autoNumericGetAsFloat (@Nonnull final IJSExpression aAutoNumeric)
  {
    return JSGlobal.parseFloat (autoNumericGet (aAutoNumeric));
  }

  @Nonnull
  public JSInvocation autoNumericGet ()
  {
    // Remember: the result is a String!!
    return invoke ().arg ("get");
  }

  @Nonnull
  public JSInvocation autoNumericGetAsFloat ()
  {
    return JSGlobal.parseFloat (autoNumericGet ());
  }

  @Nonnull
  public JSInvocation autoNumericGetString ()
  {
    return invoke ().arg ("getString");
  }

  @Nonnull
  public JSInvocation autoNumericGetArray ()
  {
    return invoke ().arg ("getArray");
  }

  @Nonnull
  public JSInvocation autoNumericGetSettings ()
  {
    return invoke ().arg ("getSettings");
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getJSOptions ()
  {
    final JSAssocArray aArgs = new JSAssocArray ();

    if (m_sThousandSeparator != null)
      aArgs.add ("aSep", m_sThousandSeparator);
    if (m_sDecimalSeparator != null)
      aArgs.add ("aDec", m_sDecimalSeparator);
    if (m_aDecimalPlaces != null)
      aArgs.add ("mDec", m_aDecimalPlaces.intValue ());
    if (m_aMin != null)
      aArgs.add ("vMin", m_aMin.toString ());
    if (m_aMax != null)
      aArgs.add ("vMax", m_aMax.toString ());
    if (m_eLeadingZero != null)
      aArgs.add ("lZero", m_eLeadingZero.getID ());
    if (m_eRoundingMode != null)
      aArgs.add ("mRound", m_eRoundingMode.getID ());

    return aArgs;
  }

  @Override
  public void onAdded (@Nonnegative final int nIndex, @Nonnull final IHCHasChildrenMutable <?, ?> aParent)
  {
    // Register resources
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EAutoNumericCSSPathProvider.AUTONUMERIC);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutoNumericJSPathProvider.AUTONUMERIC);

    // Add special JS code
    ((IHCNodeWithChildren <?>) aParent).addChild (new HCAutoNumericJS (this));
  }

  @Override
  public void onRemoved (@Nonnegative final int nIndex, @Nonnull final IHCHasChildrenMutable <?, ?> aParent)
  {
    // Remove the JS that is now on that index
    aParent.removeChild (nIndex);
  }
}
