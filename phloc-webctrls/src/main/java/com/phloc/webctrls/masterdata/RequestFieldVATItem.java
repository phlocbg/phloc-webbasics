package com.phloc.webctrls.masterdata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.masterdata.vat.IVATItem;
import com.phloc.masterdata.vat.VATManager;
import com.phloc.webbasics.form.RequestField;

/**
 * Special {@link RequestField} child class, that handles {@link IVATItem}
 * objects, and falls back to the 0% item if nothing is selected.
 * 
 * @author philip
 */
public final class RequestFieldVATItem extends RequestField
{
  public RequestFieldVATItem (@Nonnull final String sFieldName, @Nullable final IVATItem aVATItem)
  {
    super (sFieldName, aVATItem == null ? VATManager.VATTYPE_NONE.getID () : aVATItem.getID ());
  }
}
