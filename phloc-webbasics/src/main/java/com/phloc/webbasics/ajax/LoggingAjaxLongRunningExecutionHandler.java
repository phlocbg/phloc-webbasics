package com.phloc.webbasics.ajax;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingAjaxLongRunningExecutionHandler implements IAjaxLongRunningExecutionHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingAjaxLongRunningExecutionHandler.class);

  public void onLongRunningExecution (@Nonnull final String sFunctionName,
                                      @Nonnull final IAjaxHandler aHandler,
                                      @Nonnegative final long nExecutionMillis)
  {
    s_aLogger.warn ("Finished invoking AJAX function '" +
                    sFunctionName +
                    "' which took " +
                    nExecutionMillis +
                    " milliseconds (which is too long)");
  }
}
