/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.mock;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.cleanup.CommonsCleanup;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;

/**
 * Static appbasics test init and shutdown code
 * 
 * @author Philip Helger
 */
@SuppressWarnings ("deprecation")
public final class AppBasicTestInit
{
  private AppBasicTestInit ()
  {}

  public static void initAppBasics (@Nonnull final File aDataPath, @Nonnull final File aServletContextPath)
  {
    // Init the base path once - don't check access rights in test, for
    // performance reasons
    WebFileIO.initPaths (aDataPath, aServletContextPath, false);

    // Init the IDs
    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
      GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }

  public static void shutdownAppBasics ()
  {
    // Init the base path once
    WebFileIO.resetPaths ();

    // Clean commons
    CommonsCleanup.cleanup ();
  }
}
