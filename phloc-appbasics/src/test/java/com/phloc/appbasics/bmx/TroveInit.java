package com.phloc.appbasics.bmx;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.SystemProperties;

@Immutable
public final class TroveInit
{
  private TroveInit ()
  {}

  public static void init ()
  {
    if (GlobalDebug.isDebugMode ())
      SystemProperties.setPropertyValue ("gnu.trove.verbose", "true");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.byte", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.short", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.char", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.int", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.long", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.float", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.double", "MIN_VALUE");
  }
}
