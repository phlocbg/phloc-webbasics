package com.phloc.webbasics.spi;

import com.phloc.commons.annotations.IsSPIInterface;

@IsSPIInterface
public interface IApplicationStartupListenerSPI
{
  /**
   * Called when application is started up (when the servlet context is
   * initialized).
   */
  void onStartup ();
}
