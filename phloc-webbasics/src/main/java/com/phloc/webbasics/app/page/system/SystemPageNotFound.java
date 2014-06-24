/**
 * Copyright (C) 2006-2014 phloc systems
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
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.AbstractWebPage;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;

/**
 * Default page showing a very rudimentary "page not found"
 * 
 * @author Philip Helger
 */
public class SystemPageNotFound <WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPage <WPECTYPE>
{
  public static final String PAGEID_SYSTEM_NOTFOUND = "system.notfound";
  private static final Logger s_aLogger = LoggerFactory.getLogger (SystemPageNotFound.class);

  @Translatable
  protected static enum ETextBase implements IHasDisplayText
  {
    PAGENAME ("Seite nicht gefunden", "Page not found"),
    TITLE ("Seite nicht gefunden", "Page not found"),
    MESSAGE ("Die von Ihnen gesuchte Seite existiert leider nicht!", "The page you are looking for does not exist!");

    private final TextProvider m_aTP;

    private ETextBase (@Nonnull final String sDE, @Nonnull final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  public SystemPageNotFound ()
  {
    super (PAGEID_SYSTEM_NOTFOUND, ETextBase.PAGENAME.m_aTP);
  }

  @Override
  protected void fillContent (@Nonnull final WPECTYPE aWPEC)
  {
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
    final HCNodeList aNodeList = aWPEC.getNodeList ();

    s_aLogger.info ("PAGE NOT FOUND " + aWPEC.getRequestScope ().getURL ());
    aNodeList.addChild (HCH1.create (ETextBase.MESSAGE.getDisplayText (aDisplayLocale)));
  }
}
