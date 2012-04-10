package com.phloc.webbasics;

import java.io.File;

import com.phloc.scopes.web.mock.AbstractWebScopeAwareTestCase;
import com.phloc.webbasics.web.WebFileIO;

public abstract class AbstractStorageAwareTestCase extends AbstractWebScopeAwareTestCase
{
  static
  {
    // Init the base path once
    WebFileIO.initBasePath (new File ("target/junit").getAbsolutePath ());
  }
}
