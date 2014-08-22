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
   * @param sFunctionName
   *        The name of the invoked function
   * @param aRequestWebScope
   *        The request scope of the current invocation
   * @param aHandler
   *        The handler that was used
   * @param nExecutionMillis
   *        The execution time in milliseconds
   */
  void onLongRunningExecution (@Nonnull String sFunctionName,
                               @Nonnull final IRequestWebScopeWithoutResponse aRequestWebScope,
                               @Nonnull IAjaxHandler aHandler,
                               @Nonnegative long nExecutionMillis);

}
