package com.phloc.webctrls.autonumeric;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.webbasics.form.RequestField;

/**
 * Special auto numeric that only takes integers (and not decimal places)
 * 
 * @author Philip Helger
 */
public class HCAutoNumericInt extends HCAutoNumeric
{
  public HCAutoNumericInt (@Nonnull final RequestField aRF)
  {
    this (aRF, Locale.US);
  }

  public HCAutoNumericInt (@Nonnull final RequestField aRF, @Nonnull final Locale aDisplayLocale)
  {
    super (aRF, aDisplayLocale);
    setDecimalPlaces (0);
  }
}
