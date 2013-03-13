package com.phloc.webctrls.serverlog;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSInvocation;

@Immutable
public final class JSServerLog
{
  private JSServerLog ()
  {}

  @Nonnull
  public static JSInvocation serverLogInit (@Nonnull final ISimpleURL aURL,
                                            @Nonnull final String sKey,
                                            final boolean bDebugMode)
  {
    return new JSInvocation ("serverLogInit").arg (aURL.getAsString ()).arg (sKey).arg (bDebugMode);
  }

  @Nonnull
  public static JSInvocation serverLog (@Nonnull final String sSeverity, @Nonnull final String sMessage)
  {
    return new JSInvocation ("serverLog").arg (sSeverity).arg (sMessage);
  }

  @Nonnull
  public static JSInvocation setupServerLogForWindow ()
  {
    return new JSInvocation ("setupServerLogForWindow");
  }

  @Nonnull
  public static JSInvocation addExceptionHandlers (@Nonnull final IJSExpression aExpr)
  {
    return new JSInvocation ("addExceptionHandlers").arg (aExpr);
  }
}
