package com.phloc.webctrls.autosize;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCRequestField;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCTextAreaAutosize extends HCTextArea
{
  public HCTextAreaAutosize (@Nullable final String sName)
  {
    super (sName);
    registerExternalResources ();
  }

  public HCTextAreaAutosize (@Nullable final String sName, @Nullable final String sValue)
  {
    super (sName, sValue);
    registerExternalResources ();
  }

  public HCTextAreaAutosize (@Nonnull final IHCRequestField aRF)
  {
    super (aRF);
    registerExternalResources ();
  }

  public static void registerExternalResources ()
  {
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutosizeJSPathProvider.AUTOSIZE_115);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EAutosizeJSPathProvider.AUTOSIZE_ALL);
  }
}
