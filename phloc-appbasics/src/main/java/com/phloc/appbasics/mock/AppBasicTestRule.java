package com.phloc.appbasics.mock;

import com.phloc.appbasics.app.ApplicationInitializer;
import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;
import com.phloc.scopes.nonweb.mock.ScopeTestRule;

public class AppBasicTestRule extends ScopeTestRule
{
  @Override
  protected void before () throws Throwable
  {
    super.before ();
    initAppBasic ();
  }

  public static void initAppBasic ()
  {
    // Init the base path once
    WebFileIO.resetBasePath ();
    ApplicationInitializer.initIO (ScopeTestRule.STORAGE_PATH);

    // Init the IDs
    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
      GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }
}
