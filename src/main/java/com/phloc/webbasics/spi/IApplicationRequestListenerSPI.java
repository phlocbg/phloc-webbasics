package com.phloc.webbasics.spi;

import com.phloc.commons.annotations.IsSPIInterface;

@IsSPIInterface
public interface IApplicationRequestListenerSPI
{
  /**
   * Called at the very beginning of a request.
   */
  void onRequestBegin ();

  /**
   * Called at the end of a request.
   */
  void onRequestEnd ();
}
