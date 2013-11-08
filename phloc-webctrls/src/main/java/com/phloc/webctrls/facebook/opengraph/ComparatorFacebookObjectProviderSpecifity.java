package com.phloc.webctrls.facebook.opengraph;

import com.phloc.commons.compare.AbstractComparator;
import com.phloc.commons.compare.CompareUtils;

public class ComparatorFacebookObjectProviderSpecifity extends AbstractComparator <IFacebookObjectProvider>
{
  @Override
  protected int mainCompare (final IFacebookObjectProvider aProvider1, final IFacebookObjectProvider aProvider2)
  {
    return CompareUtils.compare (aProvider1.getSpecifity (), aProvider2.getSpecifity ());
  }
}
