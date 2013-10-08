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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import org.slf4j.bridge.SLF4JBridgeHandler;

import com.phloc.bootstrap3.ext.BootstrapDataTables;
import com.phloc.bootstrap3.servlet.WebAppListenerMultiAppBootstrap;
import com.phloc.bootstrap3.styler.BootstrapWebPageStyler;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.webbasics.app.init.IApplicationInitializer;
import com.phloc.webctrls.datatables.ajax.AjaxHandlerDataTables;
import com.phloc.webctrls.styler.WebPageStylerManager;
import com.phloc.webdemoapp.app.CDemoApp;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;
import com.phloc.webdemoapp.app.init.DefaultSecurity;
import com.phloc.webdemoapp.app.init.InitializerConfig;
import com.phloc.webdemoapp.app.init.InitializerView;

/**
 * Callbacks for the application server
 * 
 * @author Philip Helger
 */
public final class DemoAppWebAppListener extends WebAppListenerMultiAppBootstrap
{
  @Override
  @Nonnull
  @Nonempty
  protected Map <String, IApplicationInitializer> getAllInitializers ()
  {
    final Map <String, IApplicationInitializer> ret = new HashMap <String, IApplicationInitializer> ();
    ret.put (CDemoApp.APP_CONFIG_ID, new InitializerConfig ());
    ret.put (CDemoApp.APP_VIEW_ID, new InitializerView ());
    return ret;
  }

  @Override
  protected void initGlobals ()
  {
    // Internal stuff:

    // JUL to SLF4J
    SLF4JBridgeHandler.removeHandlersForRootLogger ();
    SLF4JBridgeHandler.install ();

    super.initGlobals ();

    // UI stuff
    WebPageStylerManager.getInstance ().setStyler (new BootstrapWebPageStyler ()
    {
      @Override
      @Nonnull
      public BootstrapDataTables createDefaultDataTables (@Nonnull final AbstractHCBaseTable <?> aTable,
                                                           @Nonnull final Locale aDisplayLocale)
      {
        final BootstrapDataTables ret = super.createDefaultDataTables (aTable, aDisplayLocale);
        ret.setAutoWidth (false)
           .setUseJQueryAjax (true)
           .setAjaxSource (CDemoAppAjaxView.VIEW_DATATABLES.getInvocationURL ())
           .setServerParams (ContainerHelper.newMap (AjaxHandlerDataTables.OBJECT_ID, aTable.getID ()));
        return ret;
      }
    });

    // Set all security related stuff
    DefaultSecurity.init ();
  }
}
