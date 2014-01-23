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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.DecimalFormatSymbolsFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSGlobal;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.JSVar;
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
public class HCAutoNumeric implements IHCNodeBuilder, IHasID <String>
{
  /** The special CSS class to use for numeric inputs */
  public static final ICSSClassProvider CSS_CLASS_AUTO_NUMERIC_EDIT = DefaultCSSClassProvider.create ("auto-numeric-edit");
  private static final Logger s_aLogger = LoggerFactory.getLogger (HCAutoNumeric.class);

  public static enum ELeadingZero
  {
    /**
     * allows leading zero to be entered. They are removed on focusout event
     * (default)
     */
    ALLOW ("allow"),
    /** leading zeros not allowed. */
    DENY ("deny"),
    /** leading zeros allowed and will be retained on the focusout event */
    KEEP ("keep");

    private final String m_sID;

    private ELeadingZero (@Nonnull @Nonempty final String sID)
    {
      m_sID = sID;
    }

    @Nonnull
    @Nonempty
    public String getID ()
    {
      return m_sID;
    }
  }

  private final RequestField m_aRF;
  private String m_sID;
  private BigDecimal m_aInitialValue;
  private String m_sThousandSeparator;
  private String m_sDecimalSeparator;
  private Integer m_aDecimalPlaces;
  private BigDecimal m_aMin;
  private BigDecimal m_aMax;
  private ELeadingZero m_eLeadingZero;
  private IHCAutoNumericEditCustomizer m_aEditCustomizer;

  public HCAutoNumeric ()
  {
    this (null, null);
  }

  public HCAutoNumeric (@Nullable final Locale aDisplayLocale)
  {
    this (aDisplayLocale, null);
  }

  public HCAutoNumeric (@Nullable final RequestField aRF)
  {
    this (null, aRF);
  }

  public HCAutoNumeric (@Nullable final Locale aDisplayLocale, @Nullable final RequestField aRF)
  {
    // Because the default min value is 0 (in v1.8.2) and we want negative
    // values by default!
    m_aMin = BigDecimal.valueOf (-999999999);

    m_aRF = aRF;
    m_sID = GlobalIDFactory.getNewStringID ();
    if (aDisplayLocale != null)
    {
      final DecimalFormatSymbols m_aDFS = DecimalFormatSymbolsFactory.getInstance (aDisplayLocale);
      m_sThousandSeparator = Character.toString (m_aDFS.getGroupingSeparator ());
      m_sDecimalSeparator = Character.toString (m_aDFS.getDecimalSeparator ());
    }
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public HCAutoNumeric setID (@Nonnull @Nonempty final String sID)
  {
    if (StringHelper.hasNoText (sID))
      throw new IllegalArgumentException ("ID");
    m_sID = sID;
    return this;
  }

  @Nullable
  public BigDecimal getInitialValue ()
  {
    return m_aInitialValue;
  }

  @Nonnull
  public HCAutoNumeric setInitialValue (final long nInitialValue)
  {
    return setInitialValue (BigDecimal.valueOf (nInitialValue));
  }

  @Nonnull
  public HCAutoNumeric setInitialValue (final double dInitialValue)
  {
    return setInitialValue (BigDecimal.valueOf (dInitialValue));
  }

  @Nonnull
  public HCAutoNumeric setInitialValue (@Nullable final BigDecimal aInitialValue)
  {
    m_aInitialValue = aInitialValue;
    return this;
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
    return this;
  }

  @Nullable
  public ELeadingZero getLeadingZero ()
  {
    return m_eLeadingZero;
  }

  @Nonnull
  public HCAutoNumeric setLeadingZero (@Nullable final ELeadingZero eLeadingZero)
  {
    m_eLeadingZero = eLeadingZero;
    return this;
  }

  @Nullable
  public final IHCAutoNumericEditCustomizer getEditCustomizer ()
  {
    return m_aEditCustomizer;
  }

  @Nonnull
  public HCAutoNumeric setEditCustomizer (@Nullable final IHCAutoNumericEditCustomizer aEditCustomizer)
  {
    m_aEditCustomizer = aEditCustomizer;
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
    return invoke (JQuery.idRef (m_sID));
  }

  @Nonnull
  public JSInvocation autoNumericInit ()
  {
    return invoke ().arg ("init");
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
  public JSInvocation autoNumericSet (@Nonnull final IJSExpression aExpr)
  {
    return autoNumericSet ().arg (aExpr);
  }

  @Nonnull
  public static JSInvocation autoNumericSet (@Nonnull final IJSExpression aAutoNumeric,
                                             @Nonnull final IJSExpression aValueToSet)
  {
    return invoke (aAutoNumeric).arg ("set").arg (aValueToSet);
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

  /**
   * Customize the edit. By default the edit customizer from
   * {@link #getEditCustomizer()} is used.
   * 
   * @param aEdit
   *        The edit to be customized
   */
  @OverrideOnDemand
  protected void customizeEdit (@Nonnull final HCEdit aEdit)
  {
    final IHCAutoNumericEditCustomizer aEditCustomizer = getEditCustomizer ();
    if (aEditCustomizer != null)
      aEditCustomizer.customize (aEdit);
  }

  @Nonnull
  public IHCNode build ()
  {
    if (m_aMin != null && m_aMax != null && m_aMin.doubleValue () > m_aMax.doubleValue ())
      throw new IllegalArgumentException ("Min must be <= max!");
    if (m_aMin != null && m_aInitialValue != null && m_aInitialValue.compareTo (m_aMin) < 0)
      throw new IllegalArgumentException ("Initial value must be >= min!");
    if (m_aMax != null && m_aInitialValue != null && m_aInitialValue.compareTo (m_aMax) > 0)
      throw new IllegalArgumentException ("Initial value must be <= max!");

    // build edit
    if (m_aRF != null && m_aInitialValue != null)
      s_aLogger.error ("InitialValue and RequestField cannot be used together - ignoring RequestField default value");
    final HCEdit aEdit = m_aRF != null ? m_aInitialValue != null ? new HCEdit (m_aRF.getFieldName ())
                                                                : new HCEdit (m_aRF) : new HCEdit ();
    aEdit.setID (m_sID).addClass (CSS_CLASS_AUTO_NUMERIC_EDIT);
    customizeEdit (aEdit);

    // build arguments
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

    registerExternalResources ();
    final JSPackage aPkg = new JSPackage ();
    final JSVar e = aPkg.var ("e" + m_sID, JQuery.idRef (m_sID));
    aPkg.add (invoke (e).arg ("init").arg (aArgs));
    if (m_aInitialValue != null)
    {
      // Never locale specific!
      aPkg.add (invoke (e).arg ("set").arg (m_aInitialValue.toString ()));
    }
    return HCNodeList.create (aEdit, new HCScriptOnDocumentReady (aPkg));
  }

  public static void registerExternalResources ()
  {
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EAutoNumericCSSPathProvider.AUTONUMERIC);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutoNumericJSPathProvider.AUTONUMERIC_1_9_18);
  }
}
