package com.phloc.report.pdf;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;

@NotThreadSafe
public final class PLDebug
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (PLDebug.class);
  private static boolean s_bDebugSplit = GlobalDebug.isDebugMode ();

  private PLDebug ()
  {}

  public static boolean isDebugSplit ()
  {
    return s_bDebugSplit;
  }

  public static void setDebugSplit (final boolean bDebugSplit)
  {
    s_bDebugSplit = bDebugSplit;
  }

  public static void debugSplit (final String sMsg)
  {
    s_aLogger.info ("Splitting: " + sMsg);
  }
}
