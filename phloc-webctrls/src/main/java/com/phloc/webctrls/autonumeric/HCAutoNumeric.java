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
package com.phloc.webctrls.autonumeric;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.phloc.html.js.builder.JSAssocArray;
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
 * @author philip
 */
public class HCAutoNumeric implements IHCNodeBuilder, IHasID <String>
{
  public static final ICSSClassProvider CSS_CLASS_AUTO_NUMERIC_EDIT = DefaultCSSClassProvider.create ("auto-numeric-edit");

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

  private final String m_sFieldName;
  private final String m_sID;
  private Double m_aInitialValue;
  private String m_sThousandSeparator;
  private String m_sDecimalSeparator;
  private Integer m_aDecimalPlaces;
  private Double m_aMin;
  private Double m_aMax;
  private ELeadingZero m_eLeadingZero;

  public HCAutoNumeric (@Nonnull final Locale aDisplayLocale)
  {
    this (aDisplayLocale, null);
  }

  public HCAutoNumeric (@Nonnull final Locale aDisplayLocale, @Nullable final String sFieldName)
  {
    if (aDisplayLocale == null)
      throw new NullPointerException ("displayLocale");

    m_sFieldName = sFieldName;
    m_sID = GlobalIDFactory.getNewStringID ();
    final DecimalFormatSymbols aDFS = DecimalFormatSymbolsFactory.getInstance (aDisplayLocale);
    m_sThousandSeparator = Character.toString (aDFS.getGroupingSeparator ());
    m_sDecimalSeparator = Character.toString (aDFS.getDecimalSeparator ());
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  @Nonnull
  public HCAutoNumeric setInitialValue (final double dInitialValue)
  {
    m_aInitialValue = Double.valueOf (dInitialValue);
    return this;
  }

  @Nonnull
  public HCAutoNumeric setThousandSeparator (@Nullable final String sThousandSeparator)
  {
    m_sThousandSeparator = sThousandSeparator;
    return this;
  }

  @Nonnull
  public HCAutoNumeric setDecimalSeparator (@Nullable final String sDecimalSeparator)
  {
    m_sDecimalSeparator = sDecimalSeparator;
    return this;
  }

  @Nonnull
  public HCAutoNumeric setDecimalPlaces (final int nDecimalPlaces)
  {
    m_aDecimalPlaces = Integer.valueOf (nDecimalPlaces);
    return this;
  }

  @Nonnull
  public HCAutoNumeric setMin (final double dMin)
  {
    m_aMin = Double.valueOf (dMin);
    return this;
  }

  @Nonnull
  public HCAutoNumeric setMax (final double dMax)
  {
    m_aMax = Double.valueOf (dMax);
    return this;
  }

  @Nonnull
  public HCAutoNumeric setLeadingZero (@Nullable final ELeadingZero eLeadingZero)
  {
    m_eLeadingZero = eLeadingZero;
    return this;
  }

  @Nonnull
  public JSInvocation autoNumericGet ()
  {
    return JQuery.idRef (m_sID).invoke ("get");
  }

  @Nonnull
  public JSInvocation autoNumericSet ()
  {
    return JQuery.idRef (m_sID).invoke ("set");
  }

  /**
   * Customize the edit
   * 
   * @param aEdit
   *        The edit to be customized
   */
  @OverrideOnDemand
  protected void customizeEdit (@Nonnull final HCEdit aEdit)
  {}

  @Nonnull
  public IHCNode build ()
  {
    if (m_aMin != null && m_aMax != null && m_aMin.doubleValue () > m_aMax.doubleValue ())
      throw new IllegalArgumentException ("Min must be <= max!");
    if (m_aMin != null && m_aInitialValue != null && m_aInitialValue.doubleValue () < m_aMin.doubleValue ())
      throw new IllegalArgumentException ("Initial value must be >= min!");
    if (m_aMax != null && m_aInitialValue != null && m_aInitialValue.doubleValue () > m_aMax.doubleValue ())
      throw new IllegalArgumentException ("Initial value must be <= max!");

    // build edit
    final HCEdit aEdit = StringHelper.hasText (m_sFieldName) ? new HCEdit (new RequestField (m_sFieldName))
                                                            : new HCEdit ();
    aEdit.setID (m_sID).addClass (CSS_CLASS_AUTO_NUMERIC_EDIT);
    customizeEdit (aEdit);

    // build arguments
    final JSAssocArray aArgs = new JSAssocArray ();

    if (m_sThousandSeparator != null)
      aArgs.add ("aSep", m_sThousandSeparator);
    if (m_sDecimalSeparator != null)
      aArgs.add ("aDec", m_sDecimalSeparator);
    if (m_aDecimalPlaces != null)
      aArgs.add ("mDec", m_aDecimalPlaces.toString ());
    if (m_aMin != null)
      aArgs.add ("vMin", m_aMin.toString ());
    if (m_aMax != null)
      aArgs.add ("vMax", m_aMax.toString ());
    if (m_eLeadingZero != null)
      aArgs.add ("lZero", m_eLeadingZero.getID ());

    registerExternalResources ();
    final JSPackage aPkg = new JSPackage ();
    final JSVar e = aPkg.var ("e" + m_sID, JQuery.idRef (m_sID));
    aPkg.add (e.invoke ("autoNumeric").arg ("init").arg (aArgs));
    if (m_aInitialValue != null)
      aPkg.add (e.invoke ("set").arg (m_aInitialValue.doubleValue ()));
    return HCNodeList.create (aEdit, new HCScriptOnDocumentReady (aPkg));
  }

  public static void registerExternalResources ()
  {
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EAutoNumericCSSPathProvider.AUTONUMERIC);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutoNumericJSPathProvider.AUTONUMERIC_182);
  }
}
