/**
 * Copyright (C) 2012-2013 Philip Helger <ph@phloc.com>
 * All Rights Reserved
 *
 * This file is part of the Ecoware Online Shop.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.phloc.webbasics.servlet;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.url.URLUtils;
import com.phloc.webbasics.userdata.UserDataManager;
import com.phloc.webbasics.userdata.UserDataObject;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.AbstractStreamServlet;

public abstract class AbstractUserStreamServlet extends AbstractStreamServlet
{
  public static final String SERVLET_DEFAULT_NAME = "user";
  public static final String SERVLET_DEFAULT_PATH = '/' + SERVLET_DEFAULT_NAME;

  /**
   * Get the user data object mathcing the passed request and filename
   * 
   * @param aRequestScope
   *        HTTP request
   * @param sFilename
   *        Filename as extracted from the URL
   * @return Never <code>null</code>.
   */
  @Nonnull
  @OverrideOnDemand
  protected UserDataObject getUserDataObject (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                              @Nonnull final String sFilename)
  {
    // URL decode is required because requests contain e.g. "%20"
    final String sFilename1 = URLUtils.urlDecode (sFilename);

    return new UserDataObject (sFilename1);
  }

  @Override
  @Nonnull
  protected IReadableResource getResource (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                           @Nonnull final String sFilename)
  {
    return UserDataManager.getResource (getUserDataObject (aRequestScope, sFilename));
  }
}
