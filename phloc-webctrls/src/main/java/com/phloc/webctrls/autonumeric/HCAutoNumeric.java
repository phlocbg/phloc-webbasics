package com.phloc.webctrls.autonumeric;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.DecimalFormatSymbolsFactory;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSPackage;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

/**
 * jQuery autoNumeric plugin from
 * 
 * <pre>
 * http://www.decorplanit.com/plugin/
 * </pre>
 * 
 * @author philip
 */
public class HCAutoNumeric implements IHCNodeBuilder
{
  public static final ICSSClassProvider CSS_CLASS_AUTO_NUMERIC_EDIT = DefaultCSSClassProvider.create ("auto-numeric-edit");

  private Double m_aInitialValue;
  private String m_sThousandSeparator;
  private String m_sDecimalSeparator;
  private Integer m_aDecimalPlaces;
  private Double m_aMin;
  private Double m_aMax;

  public HCAutoNumeric ()
  {}

  public HCAutoNumeric (@Nonnull final Locale aDisplayLocale)
  {
    final DecimalFormatSymbols aDFS = DecimalFormatSymbolsFactory.getInstance (aDisplayLocale);
    m_sThousandSeparator = Character.toString (aDFS.getGroupingSeparator ());
    m_sDecimalSeparator = Character.toString (aDFS.getDecimalSeparator ());
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
  public IHCNode build ()
  {
    if (m_aMin != null && m_aMax != null && m_aMin.doubleValue () > m_aMax.doubleValue ())
      throw new IllegalArgumentException ("Min must be <= max!");
    if (m_aMin != null && m_aInitialValue != null && m_aInitialValue.doubleValue () < m_aMin.doubleValue ())
      throw new IllegalArgumentException ("Initial value must be >= min!");
    if (m_aMax != null && m_aInitialValue != null && m_aInitialValue.doubleValue () > m_aMax.doubleValue ())
      throw new IllegalArgumentException ("Initial value must be <= max!");

    // build edit
    final String sID = GlobalIDFactory.getNewStringID ();
    final HCEdit aEdit = new HCEdit ().setID (sID).addClass (CSS_CLASS_AUTO_NUMERIC_EDIT);

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

    registerExternalResources ();
    final JSPackage aPkg = new JSPackage ();
    aPkg.add (JQuery.idRef (sID).invoke ("autoNumeric").arg (aArgs));
    if (m_aInitialValue != null)
      aPkg.add (JQuery.idRef (sID).invoke ("autoNumericSet").arg (m_aInitialValue.doubleValue ()));
    return HCNodeList.create (aEdit, new HCScriptOnDocumentReady (aPkg));
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutoNumericJSPathProvider.AUTONUMERIC_175);
  }
}
