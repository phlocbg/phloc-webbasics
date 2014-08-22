package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;

import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Callback interface to be used with the {@link IAjaxInvoker} to get notified
 * after an {@link IAjaxHandler} was invoked.
 *
 * @author Philip Helger
 */
public interface IAjaxAfterExecutionHandler
{
  /**
   * Callback method
   *
   * @param aInvoker
   *        The {@link IAjaxInvoker} object that invoked the AJAX function
   * @param sFunctionName
   *        The name of the invoked function
   * @param aRequestWebScope
   *        The request scope of the current invocation
   * @param aAjaxHandler
   *        The handler that will be used
   * @param aAjaxResponse
   *        The created Ajax response
   */
  void onAfterExecution (@Nonnull IAjaxInvoker aInvoker,
                         @Nonnull String sFunctionName,
                         @Nonnull IRequestWebScopeWithoutResponse aRequestWebScope,
                         @Nonnull IAjaxHandler aAjaxHandler,
                         @Nonnull IAjaxResponse aAjaxResponse);
}
