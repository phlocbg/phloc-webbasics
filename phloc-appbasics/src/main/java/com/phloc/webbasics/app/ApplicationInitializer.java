/**
 * Copyright (C) 2006-2012 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.webbasics.app;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.idfactory.FileIntIDFactory;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.system.EJVMVendor;
import com.phloc.scopes.nonweb.mgr.ScopeManager;
import com.phloc.webbasics.web.WebFileIO;

/**
 * This class is intended to handle the initial application startup and the
 * final shutdown.
 * 
 * @author philip
 */
public final class ApplicationInitializer
{
  /** The logger to use. */
  private static final Logger s_aLogger = LoggerFactory.getLogger (ApplicationInitializer.class);

  private ApplicationInitializer ()
  {}

  public static void init (@Nullable final String sTraceMode,
                           @Nullable final String sDebugMode,
                           @Nullable final String sProductionMode,
                           @Nonnull final String sBasePath)
  {
    // Tell them to use the server VM if possible:
    final EJVMVendor eJVMVendor = EJVMVendor.getCurrentVendor ();
    if (eJVMVendor.isSun () && eJVMVendor != EJVMVendor.SUN_SERVER)
      s_aLogger.warn ("Consider using the Sun Server Runtime by specifiying '-server' on the commandline!");

    // set global debug/trace mode
    final boolean bTraceMode = Boolean.parseBoolean (sTraceMode);
    final boolean bDebugMode = Boolean.parseBoolean (sDebugMode);
    final boolean bProductionMode = Boolean.parseBoolean (sProductionMode);
    GlobalDebug.setTraceModeDirect (bTraceMode);
    GlobalDebug.setDebugModeDirect (bDebugMode);
    GlobalDebug.setProductionModeDirect (bProductionMode);

    // begin global context
    ScopeManager.onGlobalBegin ("app");

    // Get base path!
    WebFileIO.initBasePath (sBasePath);

    // Set persistent ID provider: file based
    GlobalIDFactory.setPersistentIntIDFactory (new FileIntIDFactory (WebFileIO.getFile ("persistent_id.dat")));
  }

  public static void shutdown ()
  {
    WebFileIO.resetBasePath ();

    ScopeManager.onGlobalEnd ();
  }
}
