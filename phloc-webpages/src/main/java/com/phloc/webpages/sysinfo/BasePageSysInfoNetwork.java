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
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.hc.IHCCell;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.web.networkinterface.ComparatorNetworkInterfaceName;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webctrls.custom.tabbox.ITabBox;
import com.phloc.webctrls.custom.table.IHCTableFormView;
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
    MSG_PROXY ("Proxy", "Proxy"),
    MSG_ID ("ID", "id"),
    MSG_NAME ("Name", "Name"),
    MSG_MAC ("MAC Adresse", "MAC address"),
    MSG_IS_UP ("Up?", "Up?"),
    MSG_IS_LOOPBACK ("Lb?", "Lb?"),
    MSG_IS_POINT_TO_POINT ("P2P?", "P2P?"),
    MSG_IS_MULTICAST ("MC?", "MC?"),
    MSG_MTU ("MTU", "MTU"),
    MSG_IS_VIRTUAL ("Virt?", "Virt?");

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
      aTable.setID (getID () + "$ni");
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
        final List <NetworkInterface> aNIs = ContainerHelper.newList (NetworkInterface.getNetworkInterfaces ());
        ContainerHelper.getSortedInline (aNIs, new ComparatorNetworkInterfaceName ());
        for (final NetworkInterface aNI : aNIs)
        {
          int nDepth = 0;
          NetworkInterface aCur = aNI;
          while (aCur.getParent () != null)
          {
            nDepth++;
            aCur = aCur.getParent ();
          }

          final HCRow aRow = aTable.addBodyRow ();
          aRow.addCell (aNI.getName ());
          final IHCCell <?> aCell = aRow.addAndReturnCell (aNI.getDisplayName ());
          if (nDepth > 0)
            aCell.addStyle (CCSSProperties.PADDING_LEFT.newValue (ECSSUnit.em (nDepth)));

          final byte [] aMAC = aNI.getHardwareAddress ();
          aRow.addCell (aMAC == null ? "" : StringHelper.getHexEncoded (aMAC));

          aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isUp (), aDisplayLocale));

          aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isLoopback (), aDisplayLocale));

          aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isPointToPoint (), aDisplayLocale));

          aRow.addCell (EWebBasicsText.getYesOrNo (aNI.supportsMulticast (), aDisplayLocale));

          final int nMTU = aNI.getMTU ();
          if (nMTU > 0)
            aRow.addCell (Integer.toString (nMTU));
          else
            aRow.addCell ();

          aRow.addCell (EWebBasicsText.getYesOrNo (aNI.isVirtual (), aDisplayLocale));
        }
      }
      catch (final Exception ex)
      {
        aTable.addSpanningBodyContent (EText.MSG_ERROR_FINDING.getDisplayText (aDisplayLocale) +
                                       (GlobalDebug.isDebugMode () ? ": " + ex.getMessage () : ""));
      }
      aTabBox.addTab (EText.MSG_NETWORK_INTERFACES.getDisplayText (aDisplayLocale), aTable);
    }
    aNodeList.addChild (aTabBox);
  }
}
