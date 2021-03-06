package com.phloc.webbasics.ajax;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Callback interface to be used with the {@link IAjaxInvoker} to get notified
 * on long running executions.
 *
 * @author Philip Helger
 */
public interface IAjaxLongRunningExecutionHandler
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
   *        The handler that was used
   * @param nExecutionMillis
   *        The execution time in milliseconds
   */
  void onLongRunningExecution (@Nonnull IAjaxInvoker aInvoker,
                               @Nonnull String sFunctionName,
                               @Nonnull IRequestWebScopeWithoutResponse aRequestWebScope,
                               @Nonnull IAjaxHandler aAjaxHandler,
                               @Nonnegative long nExecutionMillis);
}
