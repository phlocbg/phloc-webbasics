package com.phloc.appbasics.bmx;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.SystemProperties;

/**
 * Trove4J utilities
 * 
 * @author philip
 */
@Immutable
public final class TroveUtils
{
  private TroveUtils ()
  {}

  /**
   * Initialize all system properties for {@link gnu.trove.impl.Constants}
   */
  public static void init (final boolean bTroveVerbose)
  {
    if (bTroveVerbose)
      SystemProperties.setPropertyValue ("gnu.trove.verbose", "true");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.byte", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.char", "MAX_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.float", "NEGATIVE_INFINITY");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.double", "NEGATIVE_INFINITY");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.int", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.long", "MIN_VALUE");
    SystemProperties.setPropertyValue ("gnu.trove.no_entry.short", "MIN_VALUE");
  }
}
