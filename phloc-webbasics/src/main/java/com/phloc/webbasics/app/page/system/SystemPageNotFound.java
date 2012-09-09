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
package com.phloc.webbasics.app.page.system;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCH1;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webbasics.app.page.AbstractWebPage;

/**
 * Default page showing a very rudimentary "page not found"
 * 
 * @author philip
 */
public class SystemPageNotFound extends AbstractWebPage
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (SystemPageNotFound.class);

  @Translatable
  protected static enum ETextBase implements IHasDisplayText
  {
    PAGENAME ("Seite nicht gefunden", "Page not found"),
    TITLE ("Seite nicht gefunden", "Page not found"),
    MESSAGE ("Die von Ihnen gesuchte Seite existiert leider nicht!", "The page you are looking for does not exist!");

    private ITextProvider m_aTP;

    private ETextBase (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    public String getDisplayText (final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  private static final SystemPageNotFound s_aInstance = new SystemPageNotFound ();

  protected SystemPageNotFound ()
  {
    super ("system.notfound", ETextBase.PAGENAME);
  }

  @Nonnull
  public static SystemPageNotFound getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  @OverrideOnDemand
  public IHCNode getContent (@Nonnull final Locale aDisplayLocale)
  {
    s_aLogger.info ("PAGE NOT FOUND " + WebScopeManager.getRequestScope ().getRequest ().getRequestURL ().toString ());
    return new HCH1 ().addChild (ETextBase.MESSAGE.getDisplayText (aDisplayLocale));
  }
}
