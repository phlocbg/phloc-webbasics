package com.phloc.webctrls.typeahead;

import javax.annotation.Nonnull;

import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSInvocation;

public class HCTypeahead
{

  @Nonnull
  public static JSInvocation invoke (@Nonnull final IJSExpression aExpr)
  {
    return aExpr.invoke ("typeahead");
  }

  @Nonnull
  public static JSInvocation typeaheadDestroy (@Nonnull final IJSExpression aTypeahead)
  {
    return invoke (aTypeahead).arg ("destroy");
  }

  @Nonnull
  public static JSInvocation typeaheadSetQuery (@Nonnull final IJSExpression aTypeahead, @Nonnull final String sQuery)
  {
    return invoke (aTypeahead).arg ("setQuery").arg (sQuery);
  }
}
