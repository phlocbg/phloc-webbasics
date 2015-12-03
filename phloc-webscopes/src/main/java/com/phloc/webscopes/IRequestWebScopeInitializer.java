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
   */
  public void initRequestWebScope (final IRequestWebScope aScope);
}
