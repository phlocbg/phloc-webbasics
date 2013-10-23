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
package com.phloc.webpages.sysinfo;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.compare.ESortOrder;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.tree.utils.sort.TreeWithIDSorter;
import com.phloc.commons.tree.utils.walk.TreeWalker;
import com.phloc.commons.tree.withid.DefaultTreeItemWithID;
import com.phloc.commons.tree.withid.unique.DefaultTreeWithGlobalUniqueID;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEM;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.web.networkinterface.ComparatorNetworkInterfaceName;
import com.phloc.web.networkinterface.NetworkInterfaceUtils;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableFormView;
import com.phloc.webctrls.datatables.DataTables;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with information on the current network settings
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoNetwork extends AbstractWebPageExt
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_NETWORK_INTERFACES ("Netzwerkkarten", "Network interfaces"),
    MSG_ERROR_FINDING ("Fehler beim Ermitteln der Netzwerkkarten", "Error determining network interfaces"),
    MSG_ID ("ID", "id"),
    MSG_NAME ("Name", "Name"),
    MSG_MAC ("MAC Adresse", "MAC address"),
    MSG_IS_UP ("Up?", "Up?"),
    MSG_IS_LOOPBACK ("Lb?", "Lb?"),
    MSG_IS_POINT_TO_POINT ("P2P?", "P2P?"),
    MSG_IS_MULTICAST ("MC?", "MC?"),
    MSG_MTU ("MTU", "MTU"),
    MSG_IS_VIRTUAL ("Virt?", "Virt?"),
    MSG_ERROR ("Fehler!", "Error!");

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

  public BasePageSysInfoNetwork (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_NETWORK.getAsMLT ());
  }

  public BasePageSysInfoNetwork (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoNetwork (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoNetwork (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Override
  protected void fillContent (@Nonnull final WebPageExecutionContext aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    final ITabBox <?> aTabBox = getStyler ().createTabBox ();

    // HTTP headers
    {
      final IHCTableFormView <?> aTable = getStyler ().createTableFormView (new HCCol (80),
                                                                            HCCol.star (),
                                                                            HCCol.star (),
                                                                            new HCCol (40),
                                                                            new HCCol (40),
                                                                            new HCCol (40),
                                                                            new HCCol (40),
                                                                            new HCCol (40),
                                                                            new HCCol (40));
      aTable.setID (getID () + "-ni");
      aTable.addHeaderRow ().addCells (EText.MSG_ID.getDisplayText (aDisplayLocale),
                                       EText.MSG_NAME.getDisplayText (aDisplayLocale),
                                       EText.MSG_MAC.getDisplayText (aDisplayLocale),
                                       EText.MSG_IS_UP.getDisplayText (aDisplayLocale),
                                       EText.MSG_IS_LOOPBACK.getDisplayText (aDisplayLocale),
                                       EText.MSG_IS_POINT_TO_POINT.getDisplayText (aDisplayLocale),
                                       EText.MSG_IS_MULTICAST.getDisplayText (aDisplayLocale),
                                       EText.MSG_MTU.getDisplayText (aDisplayLocale),
                                       EText.MSG_IS_VIRTUAL.getDisplayText (aDisplayLocale));
      try
      {
        final DefaultTreeWithGlobalUniqueID <String, NetworkInterface> aNITree = NetworkInterfaceUtils.createNetworkInterfaceTree ();
        // Sort on each level
        TreeWithIDSorter.sortByValue (aNITree, new ComparatorNetworkInterfaceName ());
        TreeWalker.walkTree (aNITree,
                             new DefaultHierarchyWalkerCallback <DefaultTreeItemWithID <String, NetworkInterface>> ()
                             {
                               @Override
                               public void onItemBeforeChildren (@Nonnull final DefaultTreeItemWithID <String, NetworkInterface> aItem)
                               {
                                 final NetworkInterface aNI = aItem.getData ();
                                 final int nDepth = getLevel ();
                                 final HCRow aRow = aTable.addBodyRow ();
                                 aRow.addCell (aNI.getName ());
                                 final IHCCell <?> aCell = aRow.addAndReturnCell (aNI.getDisplayName ());
                                 if (nDepth > 0)
                                   aCell.addStyle (CCSSProperties.PADDING_LEFT.newValue (ECSSUnit.em (nDepth)));

                                 // hardware address (usually MAC) of the
                                 // interface if it has one and if it can be
                                 // accessed given the current privileges.
                                 try
                                 {
                                   final byte [] aMAC = aNI.getHardwareAddress ();
                                   aRow.addCell (aMAC == null ? "" : StringHelper.getHexEncoded (aMAC));
                                 }
                                 catch (final SocketException ex)
                                 {
                                   aRow.addCell (HCEM.create (EText.MSG_ERROR.getDisplayText (aDisplayLocale)));
                                 }

                                 // network interface is up and running.
                                 try
                                 {
                                   aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isUp (), aDisplayLocale));
                                 }
                                 catch (final SocketException ex)
                                 {
                                   aRow.addCell (HCEM.create (EText.MSG_ERROR.getDisplayText (aDisplayLocale)));
                                 }

                                 // network interface is a loopback interface.
                                 try
                                 {
                                   aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isLoopback (), aDisplayLocale));
                                 }
                                 catch (final SocketException ex)
                                 {
                                   aRow.addCell (HCEM.create (EText.MSG_ERROR.getDisplayText (aDisplayLocale)));
                                 }

                                 // network interface is a point to point
                                 // interface. A typical point to point
                                 // interface would be a PPP connection through
                                 // a modem.
                                 try
                                 {
                                   aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isPointToPoint (), aDisplayLocale));
                                 }
                                 catch (final SocketException ex)
                                 {
                                   aRow.addCell (HCEM.create (EText.MSG_ERROR.getDisplayText (aDisplayLocale)));
                                 }

                                 // network interface supports multicasting or
                                 // not.
                                 try
                                 {
                                   aRow.addCell (EWebBasicsText.getYesOrNo (aNI.supportsMulticast (), aDisplayLocale));
                                 }
                                 catch (final SocketException ex)
                                 {
                                   aRow.addCell (HCEM.create (EText.MSG_ERROR.getDisplayText (aDisplayLocale)));
                                 }

                                 // Maximum Transmission Unit (MTU) of this
                                 // interface.
                                 int nMTU = -1;
                                 try
                                 {
                                   nMTU = aNI.getMTU ();
                                 }
                                 catch (final SocketException ex)
                                 {}
                                 if (nMTU > 0)
                                   aRow.addCell (Integer.toString (nMTU));
                                 else
                                   aRow.addCell ();

                                 // this interface is a virtual interface (also
                                 // called subinterface). Virtual interfaces
                                 // are, on some systems, interfaces created as
                                 // a child of a physical interface and given
                                 // different settings (like address or MTU).
                                 // Usually the name of the interface will the
                                 // name of the parent followed by a colon (:)
                                 // and a number identifying the child since
                                 // there can be several virtual interfaces
                                 // attached to a single physical interface
                                 aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isVirtual (), aDisplayLocale));
                               }
                             });
      }
      catch (final Throwable t)
      {
        aTable.addSpanningBodyContent (EText.MSG_ERROR_FINDING.getDisplayText (aDisplayLocale) +
                                       (GlobalDebug.isDebugMode () ? ": " + t.getMessage () : ""));
      }

      final DataTables aDataTables = getStyler ().createDefaultDataTables (aTable, aDisplayLocale);
      aDataTables.setDisplayLength (-1);
      aDataTables.setInitialSorting (0, ESortOrder.ASCENDING);

      aTabBox.addTab (EText.MSG_NETWORK_INTERFACES.getDisplayText (aDisplayLocale),
                      new HCNodeList ().addChild (aTable).addChild (aDataTables));
    }
    aNodeList.addChild (aTabBox);
  }
}
