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
package com.phloc.appbasics.app;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.app.io.WebIOIntIDFactory;
import com.phloc.appbasics.app.io.WebIOResourceProviderChain;
import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.StringParser;
import com.phloc.commons.system.EJVMVendor;
import com.phloc.scopes.nonweb.mgr.ScopeManager;

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

  public static void initIO (@Nonnull @Nonempty final String sBasePath)
  {
    if (StringHelper.hasNoText (sBasePath))
      throw new IllegalArgumentException ("basePath");
    initIO (new File (sBasePath));
  }

  public static void initIO (@Nullable final File aBasePath)
  {
    // Init base path
    if (aBasePath != null)
    {
      WebFileIO.initBasePath (aBasePath);
      WebIO.init (new WebIOResourceProviderChain (aBasePath));
    }
  }

  public static void init (@Nullable final String sTraceMode,
                           @Nullable final String sDebugMode,
                           @Nullable final String sProductionMode,
                           @Nullable final String sGlobalScopeID,
                           @Nullable final File aBasePath)
  {
    final boolean bTraceMode = StringParser.parseBool (sTraceMode);
    final boolean bDebugMode = StringParser.parseBool (sDebugMode);
    final boolean bProductionMode = StringParser.parseBool (sProductionMode);
    init (bTraceMode, bDebugMode, bProductionMode, sGlobalScopeID, aBasePath);
  }

  public static void init (final boolean bTraceMode,
                           final boolean bDebugMode,
                           final boolean bProductionMode,
                           @Nullable final String sGlobalScopeID,
                           @Nullable final File aBasePath)
  {
    // Tell them to use the server VM if possible:
    final EJVMVendor eJVMVendor = EJVMVendor.getCurrentVendor ();
    if (eJVMVendor.isSun () && eJVMVendor != EJVMVendor.SUN_SERVER)
      s_aLogger.warn ("Consider using the Sun Server Runtime by specifiying '-server' on the commandline!");

    // set global debug/trace mode
    GlobalDebug.setTraceModeDirect (bTraceMode);
    GlobalDebug.setDebugModeDirect (bDebugMode);
    GlobalDebug.setProductionModeDirect (bProductionMode);

    // Init global scope?
    if (sGlobalScopeID != null)
      ScopeManager.onGlobalBegin (sGlobalScopeID);

    // Init base path
    initIO (aBasePath);

    // Set persistent ID provider: file based
    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
      GlobalIDFactory.setPersistentIntIDFactory (new WebIOIntIDFactory ("persistent_id.dat"));
  }

  public static void shutdown (final boolean bResetBasePath, final boolean bShutdownGlobalScope)
  {
    if (bResetBasePath)
      WebFileIO.resetBasePath ();

    if (bShutdownGlobalScope)
      ScopeManager.onGlobalEnd ();
  }
}
