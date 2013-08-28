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

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.html.AbstractHCBaseTable;
import com.phloc.webbasics.app.init.IApplicationInitializer;
import com.phloc.webctrls.bootstrap3.ext.Bootstrap3DataTables;
import com.phloc.webctrls.bootstrap3.servlet.WebAppListenerMultiAppBootstrap3;
import com.phloc.webctrls.datatables.ajax.AjaxHandlerDataTables;
import com.phloc.webdemoapp.app.CDemoAppUI;
import com.phloc.webdemoapp.app.ajax.view.CDemoAppAjaxView;
import com.phloc.webdemoapp.app.init.DefaultSecurity;
import com.phloc.webdemoapp.app.init.InitializerConfig;
import com.phloc.webdemoapp.app.init.InitializerView;
import com.phloc.webpages.theme.WebPageStyleBootstrap3;
import com.phloc.webpages.theme.WebPageStyleManager;

/**
 * Callbacks for the application server
 * 
 * @author Philip Helger
 */
public final class DemoAppWebAppListener extends WebAppListenerMultiAppBootstrap3
{
  @Override
  @Nonnull
  @Nonempty
  protected Map <String, IApplicationInitializer> getAllInitializers ()
  {
    final Map <String, IApplicationInitializer> ret = new HashMap <String, IApplicationInitializer> ();
    ret.put (CDemoAppUI.APP_CONFIG_ID, new InitializerConfig ());
    ret.put (CDemoAppUI.APP_VIEW_ID, new InitializerView ());
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
    WebPageStyleManager.getInstance ().setStyle (new WebPageStyleBootstrap3 ()
    {
      @Override
      @Nonnull
      public Bootstrap3DataTables createDefaultDataTables (@Nonnull final AbstractHCBaseTable <?> aTable,
                                                           @Nonnull final Locale aDisplayLocale)
      {
        final Bootstrap3DataTables ret = super.createDefaultDataTables (aTable, aDisplayLocale);
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
