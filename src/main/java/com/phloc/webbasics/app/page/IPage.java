package com.phloc.webbasics.app.page;

import java.util.Locale;

import javax.annotation.Nullable;

import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.html.hc.IHCNode;

public interface IPage extends IHasID <String>, IHasDisplayText
{
  /**
   * @return The content of the area based on the current state.
   */
  @Nullable
  IHCNode getContent (Locale aDisplayLocale);
}
