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
package com.phloc.webbasics.atom;

import java.util.Locale;

import javax.annotation.Nonnull;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.mime.CMimeType;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.stats.IStatisticsHandlerKeyedCounter;
import com.phloc.commons.stats.StatisticsManager;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.timing.StopWatch;
import com.phloc.commons.xml.serialize.XMLWriterSettings;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webbasics.action.AbstractActionExecutor;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * Abstract news feed action.
 * 
 * @author philip
 */
public abstract class AbstractNewsfeedActionExecutor extends AbstractActionExecutor implements
                                                                                   IHasID <String>,
                                                                                   IHasDisplayText
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractNewsfeedActionExecutor.class);
  private static final IStatisticsHandlerKeyedCounter s_aStatsHdlExecute = StatisticsManager.getKeyedCounterHandler (AbstractNewsfeedActionExecutor.class.getName () +
                                                                                                                     "$EXECUTE");
  private static final IStatisticsHandlerKeyedCounter s_aStatsHdlError = StatisticsManager.getKeyedCounterHandler (AbstractNewsfeedActionExecutor.class.getName () +
                                                                                                                   "$ERROR");

  private final IHasDisplayText m_aDisplayText;
  private final String m_sFeedID;

  public AbstractNewsfeedActionExecutor (@Nonnull final IHasDisplayText aDisplayText,
                                         @Nonnull @Nonempty final String sFeedID)
  {
    if (aDisplayText == null)
      throw new NullPointerException ("displayText");
    if (StringHelper.hasNoText (sFeedID))
      throw new IllegalArgumentException ("No feed ID passed!");
    m_aDisplayText = aDisplayText;
    m_sFeedID = sFeedID;
  }

  @Nonnull
  @Nonempty
  public final String getID ()
  {
    return m_sFeedID;
  }

  public final String getDisplayText (@Nonnull final Locale aContentLocale)
  {
    return m_aDisplayText.getDisplayText (aContentLocale);
  }

  @Nonnull
  @OverrideOnDemand
  protected String getFeedDescription ()
  {
    return "phloc-webbasics";
  }

  protected abstract void fillNewsfeed (@Nonnull Feed aFeed);

  public final void execute (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                             @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    // Increment statistics counter
    final StopWatch aSW = new StopWatch (true);
    s_aStatsHdlExecute.increment (m_sFeedID);

    final Feed aFeed = new Feed ();
    aFeed.setID ("urn:phloc-webbasics:newsfeed:" + m_sFeedID);

    final FeedGenerator aGenerator = new FeedGenerator ("urn:phloc-webbasics");
    aGenerator.setDescription (getFeedDescription ());
    aFeed.setGenerator (aGenerator);
    aFeed.addLink (new FeedLink (aRequestScope.getFullContextAndServletPath () + m_sFeedID, FeedLink.REL_SELF));

    // Abstract filling
    fillNewsfeed (aFeed);

    if (!aFeed.isValid ())
    {
      s_aLogger.error ("Created newsfeed with ID '" + m_sFeedID + "' is invalid!");
      s_aStatsHdlError.increment (m_sFeedID);
    }

    // Performance improvement: set the Last-Modified HTTP header if available
    if (aFeed.getUpdated () != null)
    {
      final LocalDateTime aLDT = aFeed.getUpdated ().getDateTime ();
      if (aLDT != null)
        aUnifiedResponse.setLastModified (aLDT.toDateTime (DateTimeZone.UTC));
    }

    final String sXML = MicroWriter.getXMLString (aFeed.getAsDocument ());
    aUnifiedResponse.setContentAndCharset (sXML, XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ)
                    .setMimeType (CMimeType.APPLICATION_ATOM_XML);
    StatisticsManager.getTimerHandler (AbstractNewsfeedActionExecutor.class.getName () + "$TIMER." + m_sFeedID)
                     .addTime (aSW.stopAndGetMillis ());
  }
}
