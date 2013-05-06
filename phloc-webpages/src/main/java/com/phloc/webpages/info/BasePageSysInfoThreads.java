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
package com.phloc.webpages.info;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.concurrent.ComparatorThreadID;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.datetime.PDTFactory;
import com.phloc.datetime.format.PDTToString;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.htmlext.HCUtils;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.bootstrap.BootstrapTable;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webctrls.datatables.comparator.ComparatorTableInteger;
import com.phloc.webctrls.datatables.comparator.ComparatorTableLong;
import com.phloc.webpages.AbstractWebPageExt;

public class BasePageSysInfoThreads extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_ID ("ID", "ID"),
    MSG_GROUP ("Gruppe", "Group"),
    MSG_NAME ("Name", "Name"),
    MSG_PRIORITY ("Prio", "Prio"),
    MSG_STATE ("Status", "State"),
    MSG_STACKTRACE ("Stacktrace", "Stacktrace");

    private final ITextProvider m_aTP;

    private EText (final String sDE, final String sEN)
    {
      m_aTP = TextProvider.create_DE_EN (sDE, sEN);
    }

    @Nullable
    public String getDisplayText (@Nonnull final Locale aContentLocale)
    {
      return DefaultTextResolver.getText (this, m_aTP, aContentLocale);
    }
  }

  public BasePageSysInfoThreads (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoThreads (@Nonnull @Nonempty final String sID,
                              @Nonnull final String sName,
                              @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoThreads (@Nonnull @Nonempty final String sID,
                              @Nonnull final IReadonlyMultiLingualText aName,
                              @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  private static String _getThreadGroupName (@Nullable final ThreadGroup aParamTG)
  {
    ThreadGroup aThreadGroup = aParamTG;
    final StringBuilder ret = new StringBuilder ();
    while (aThreadGroup != null)
    {
      if (ret.length () > 0)
        ret.insert (0, '/');
      ret.insert (0, aThreadGroup.getName ());

      // Descend
      aThreadGroup = aThreadGroup.getParent ();
    }
    return ret.toString ();
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final BootstrapTable aTable = new BootstrapTable (new HCCol (50),
                                                      new HCCol (100),
                                                      new HCCol (150),
                                                      new HCCol (55),
                                                      new HCCol (100),
                                                      HCCol.star ()).setID (getID ());

    // get all threads and sort them by thread ID
    final Map <Thread, StackTraceElement []> aThreads = ContainerHelper.getSortedByKey (Thread.getAllStackTraces (),
                                                                                        new ComparatorThreadID ());

    aTable.setSpanningHeaderContent ("Total count=" +
                                     aThreads.size () +
                                     "; Prios (min/norm/max): " +
                                     Thread.MIN_PRIORITY +
                                     "/" +
                                     Thread.NORM_PRIORITY +
                                     "/" +
                                     Thread.MAX_PRIORITY +
                                     "; Zeitpunkt: " +
                                     PDTToString.getAsString (PDTFactory.getCurrentDateTime (), aDisplayLocale));
    aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                     EText.MSG_GROUP.getDisplayText (aDisplayLocale),
                                     EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                     EText.MSG_PRIORITY.getDisplayText (aDisplayLocale),
                                     EText.MSG_STATE.getDisplayText (aDisplayLocale),
                                     EText.MSG_STACKTRACE.getDisplayText (aDisplayLocale));

    // For all system properties
    for (final Map.Entry <Thread, StackTraceElement []> aEntry : aThreads.entrySet ())
    {
      final HCRow aRow = aTable.addBodyRow ();

      // Thread ID (long)
      final Thread aThread = aEntry.getKey ();
      aRow.addCell (Long.toString (aThread.getId ()));

      // Thread group
      final ThreadGroup aThreadGroup = aThread.getThreadGroup ();
      aRow.addCell (_getThreadGroupName (aThreadGroup));

      // Thread name
      aRow.addCell (aThread.getName ());

      // Priority (int)
      aRow.addCell (Integer.toString (aThread.getPriority ()));

      // State
      aRow.addCell (String.valueOf (aThread.getState ()));

      // Stack trace
      aRow.addCell (HCUtils.nl2brList (StackTraceHelper.getStackAsString (aEntry.getValue ())));
    }
    aNodeList.addChild (aTable);

    final DataTables aDataTables = createDefaultDataTables (aTable, aDisplayLocale);
    aDataTables.getColumn (0).addClass (CSS_CLASS_RIGHT).setComparator (new ComparatorTableLong (aDisplayLocale));
    aDataTables.getColumn (3).addClass (CSS_CLASS_RIGHT).setComparator (new ComparatorTableInteger (aDisplayLocale));
    aDataTables.getColumn (5).setSortable (false);
    aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);
    aNodeList.addChild (aDataTables);
  }
}
