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

  /**
   * Whether or not the request body should be checked for JSON content and in
   * case of JSON, all properties will be provided as request scope attributes.
   * This is useful e.g. for sending big data in AJAX requests via the body. <br>
   * <br>
   * <b>ATTENTION: </b>If this is enabled, after the request initialization you
   * will no longer be able to read the request body!
   * 
   * @param aScope
   * @return whether or not the body should be interpreted as JSON.
   */
  public boolean isReadBodyAsJSON (final IRequestWebScope aScope);
}
