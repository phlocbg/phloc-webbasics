package com.phloc.webctrls.autonumeric;

import javax.annotation.Nullable;

import com.phloc.webbasics.form.RequestField;

/**
 * Special numeric edit for years from {@value #DEFAULT_MIN} to
 * {@value #DEFAULT_MAX}.
 * 
 * @author philip
 */
public class HCEditYear extends HCAutoNumeric
{
  public static final int DEFAULT_MIN = 0;
  public static final int DEFAULT_MAX = 9999;

  public HCEditYear ()
  {
    this (null);
  }

  public HCEditYear (@Nullable final RequestField aRF)
  {
    super (aRF);
    setDecimalPlaces (0);
    setThousandSeparator ("");
    setMin (DEFAULT_MIN);
    setMax (DEFAULT_MAX);
  }
}
