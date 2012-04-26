package com.phloc.webbasics.form;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.html.hc.CHCParam;
import com.phloc.html.hc.html.HCCheckBox;
import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.scopes.web.util.RequestField;

/**
 * Special request field specially for check boxes with a fixed value.
 * 
 * @author philip
 */
public final class RequestFieldCheckbox extends RequestField
{
  private final boolean m_bDefaultValue;

  public RequestFieldCheckbox (@Nonnull final RequestField aRF)
  {
    this (aRF.getFieldName (), CHCParam.VALUE_CHECKED.equals (aRF.getDefaultValue ()));
  }

  public RequestFieldCheckbox (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    super (sFieldName, getStringValue (bDefaultValue));

    // Save default value to parsing from string once we don't need it anymore
    // The default value is immutable
    m_bDefaultValue = bDefaultValue;
  }

  /**
   * @param bValue
   *        the boolean value
   * @return The string parameter value to be used for the passed parameter
   */
  @Nonnull
  public static String getStringValue (final boolean bValue)
  {
    return bValue ? CHCParam.VALUE_CHECKED : CHCParam.VALUE_UNCHECKED;
  }

  /**
   * @return <code>true</code> if the checkbox is checked or if no such request
   *         parameter is present and the fallback is <code>true</code>,
   *         <code>false</code> otherwise.
   */
  public boolean getCheckBoxValue ()
  {
    return getCheckBoxValue (getFieldName (), m_bDefaultValue);
  }

  public static boolean getCheckBoxValue (@Nonnull @Nonempty final String sFieldName, final boolean bDefaultValue)
  {
    final IRequestScope aScope = getScope ();

    // Is the checked value present?
    final String sRequestValue = aScope.getAttributeAsString (sFieldName);
    if (sRequestValue != null)
      return Boolean.parseBoolean (sRequestValue);

    // Check if the hidden parameter for "checkbox is contained in the request"
    // is present?
    if (aScope.containsAttribute (HCCheckBox.getHiddenFieldName (sFieldName)))
      return false;

    // Neither nor - default!
    return bDefaultValue;
  }
}
