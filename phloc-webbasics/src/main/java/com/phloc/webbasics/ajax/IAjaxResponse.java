package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;

public interface IAjaxResponse
{
  /**
   * Get the Ajax response as JSON
   * 
   * @param bIndentAndAlign
   *        <code>true</code> if the JSON code should be indented and aligned
   * @return Never <code>null</code>.
   */
  @Nonnull
  String getSerializedAsJSON (boolean bIndentAndAlign);
}
