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
package com.phloc.webbasics;

import java.io.File;
import java.io.IOException;

import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.idfactory.FileIntIDFactory;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.scopes.web.mock.AbstractWebScopeAwareTestCase;
import com.phloc.webbasics.web.WebFileIO;

public abstract class AbstractStorageAwareTestCase extends AbstractWebScopeAwareTestCase
{
  static
  {
    // Init the base path once
    WebFileIO.initBasePath (new File ("target/junit"));

    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
    {
      try
      {
        final File aIDFile = File.createTempFile ("junit_persistent_ids", ".dat", null);
        GlobalIDFactory.setPersistentIntIDFactory (new FileIntIDFactory (aIDFile));

        // Ensure the file is deleted at the end of the tests....
        aIDFile.deleteOnExit ();
      }
      catch (final IOException e)
      {
        throw new InitializationException (e);
      }
    }
  }
}
