package com.phloc.webscopes;

import com.phloc.commons.annotations.IsSPIInterface;
import com.phloc.webscopes.domain.IRequestWebScope;

@IsSPIInterface
public interface IRequestWebScopeInitializer
{
  /**
   * A custom request initialization
   * 
   * @param aScope
   * @return true if the default init should still be performed afterwards
   */
  public boolean initRequestWebScope (final IRequestWebScope aScope);
}
