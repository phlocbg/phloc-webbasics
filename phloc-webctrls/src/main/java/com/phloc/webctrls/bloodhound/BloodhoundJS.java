package com.phloc.webctrls.bloodhound;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.html.js.builder.JSExpr;
import com.phloc.html.js.builder.JSFieldRef;
import com.phloc.html.js.builder.JSRef;

@Immutable
public final class BloodhoundJS
{
  private BloodhoundJS ()
  {}

  @Nonnull
  public static JSRef bloodhound ()
  {
    return JSExpr.ref ("Bloodhound");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizers ()
  {
    return bloodhound ().ref ("tokenizers");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizersWhitespace ()
  {
    return bloodhoundTokenizers ().ref ("whitespace");
  }

  @Nonnull
  public static JSFieldRef bloodhoundTokenizersNonword ()
  {
    return bloodhoundTokenizers ().ref ("nonword");
  }
}
