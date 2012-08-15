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
package com.phloc.appbasics.mock;

import com.phloc.appbasics.app.ApplicationInitializer;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;
import com.phloc.scopes.web.mock.AbstractWebScopeAwareTestCase;

/**
 * Abstract test base class initializing the {@link WebFileIO} base path and
 * setting a persistent int ID provider
 * 
 * @author philip
 */
public abstract class AbstractAppBasicWebTestCase extends AbstractWebScopeAwareTestCase
{
  static
  {
    initTestEnvironment ();
  }

  public static final void initTestEnvironment ()
  {
    // Init the base path once
    ApplicationInitializer.initIO (AbstractAppBasicTestCase.STORAGE_PATH);

    // Init the IDs
    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
      GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }
}
