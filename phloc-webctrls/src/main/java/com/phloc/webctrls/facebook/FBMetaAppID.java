package com.phloc.webctrls.facebook;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.html.meta.MetaElement;

public class FBMetaAppID extends MetaElement
{
  private static final long serialVersionUID = 7422677747549691858L;

  public FBMetaAppID (final String sAppID)
  {
    super (CFacebook.FACEBOOK_PREFIX + ':' + "app_id", false, sAppID);
  }

  @Override
  @Nonnull
  @Nonempty
  @OverrideOnDemand
  protected String getNodeNameAttribute ()
  {
    return "property";
  }
}
