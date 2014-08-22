package com.phloc.webbasics.ajax;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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
   * @param aHandler
   *        The handler that was used
   * @param nExecutionMillis
   *        The execution time in milliseconds
   */
  void onLongRunningExecution (@Nonnull String sFunctionName,
                               @Nonnull IAjaxHandler aHandler,
                               @Nonnegative long nExecutionMillis);

}
