package com.phloc.appbasics.mock;

import java.io.File;

import javax.annotation.Nonnull;

import com.phloc.appbasics.app.io.WebFileIO;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.app.io.WebIOResourceProviderChain;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;
import com.phloc.scopes.nonweb.mock.ScopeTestRule;

public class AppBasicTestRule extends ScopeTestRule
{
  private final File m_aStoragePath;

  public AppBasicTestRule ()
  {
    this (ScopeTestRule.STORAGE_PATH);
  }

  public AppBasicTestRule (@Nonnull final File aStoragePath)
  {
    if (aStoragePath == null)
      throw new NullPointerException ("storagePath");
    m_aStoragePath = aStoragePath;
  }

  @Nonnull
  public File getStoragePath ()
  {
    return m_aStoragePath;
  }

  @Override
  public void before ()
  {
    super.before ();
    initAppBasics (m_aStoragePath);
  }

  public static void initAppBasics (@Nonnull final File aStoragePath)
  {
    // Init the base path once
    WebFileIO.resetBasePath ();
    WebFileIO.initBasePath (aStoragePath);
    WebIO.init (new WebIOResourceProviderChain (aStoragePath));

    // Init the IDs
    if (!GlobalIDFactory.hasPersistentIntIDFactory ())
      GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }
}
