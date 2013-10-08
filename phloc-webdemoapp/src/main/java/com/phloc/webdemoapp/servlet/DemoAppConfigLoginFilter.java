/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.webdemoapp.servlet;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.appbasics.security.login.LoggedInUserManager;
import com.phloc.bootstrap3.ext.BootstrapLoginHTMLProvider;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsImmutableObject;
import com.phloc.commons.state.EContinue;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.app.html.IHTMLProvider;
import com.phloc.webbasics.login.LoginManager;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webdemoapp.app.CDemoAppSecurity;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webscopes.servlets.filter.AbstractUnifiedResponseFilter;

public final class DemoAppConfigLoginFilter extends AbstractUnifiedResponseFilter
{
  private LoginManager m_aLogin;

  @Override
  @Nonnull
  @Nonempty
  protected String getApplicationID (@Nonnull final FilterConfig aFilterConfig)
  {
    return CDemoApp.APP_CONFIG_ID;
  }

  @Override
  protected void onInit (@Nonnull final FilterConfig aFilterConfig) throws ServletException
  {
    // Make the application login configurable if you like
    m_aLogin = new LoginManager ()
    {
      @Override
      protected IHTMLProvider createLoginScreen (final boolean bLoginError, @Nonnull final ELoginResult eLoginResult)
      {
        return new BootstrapLoginHTMLProvider (bLoginError, eLoginResult, "DemoApp Administration - Login");
      }

      @Override
      @Nonnull
      @ReturnsImmutableObject
      protected Collection <String> getAllRequiredRoleIDs ()
      {
        return CDemoAppSecurity.REQUIRED_ROLE_IDS_CONFIG;
      }
    };
  }

  @Override
  @Nonnull
  protected EContinue handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                     @Nonnull final UnifiedResponse aUnifiedResponse) throws ServletException
  {
    if (m_aLogin.checkUserAndShowLogin (aRequestScope, aUnifiedResponse).isBreak ())
    {
      // Show login screen
      return EContinue.BREAK;
    }

    // Check if the currently logged in user has the required roles
    final String sCurrentUserID = LoggedInUserManager.getInstance ().getCurrentUserID ();
    if (!AccessManager.getInstance ().hasUserAllRoles (sCurrentUserID, CDemoAppSecurity.REQUIRED_ROLE_IDS_CONFIG))
    {
      aUnifiedResponse.setStatus (HttpServletResponse.SC_FORBIDDEN);
      return EContinue.BREAK;
    }

    return EContinue.CONTINUE;
  }
}
