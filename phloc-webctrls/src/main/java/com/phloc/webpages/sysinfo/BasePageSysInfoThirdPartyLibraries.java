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
package com.phloc.webpages.sysinfo;

import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.Translatable;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.name.ComparatorHasDisplayName;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.commons.name.IHasDisplayText;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.commons.text.ITextProvider;
import com.phloc.commons.text.impl.TextProvider;
import com.phloc.commons.text.resolve.DefaultTextResolver;
import com.phloc.commons.thirdparty.IThirdPartyModule;
import com.phloc.commons.thirdparty.ThirdPartyModuleRegistry;
import com.phloc.commons.url.ReadonlySimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCA;
import com.phloc.html.hc.html.HCA_Target;
import com.phloc.html.hc.html.HCH4;
import com.phloc.html.hc.html.HCUL;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.WebPageExecutionContext;
import com.phloc.webpages.AbstractWebPageExt;
import com.phloc.webpages.EWebPageText;

/**
 * Page with all linked third party libraries
 * 
 * @author Philip Helger
 */
public class BasePageSysInfoThirdPartyLibraries <WPECTYPE extends WebPageExecutionContext> extends AbstractWebPageExt <WPECTYPE>
{
  @Translatable
  protected static enum EText implements IHasDisplayText
  {
    MSG_TPM_HEADER ("Folgende externen Module werden verwendet", "The following external libraries are used"),
    MSG_LICENSED_UNDER (" lizensiert unter ", " licensed under ");

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

  public BasePageSysInfoThirdPartyLibraries (@Nonnull @Nonempty final String sID)
  {
    super (sID, EWebPageText.PAGE_NAME_SYSINFO_THIRDPARTYLIBS.getAsMLT ());
  }

  public BasePageSysInfoThirdPartyLibraries (@Nonnull @Nonempty final String sID, @Nonnull @Nonempty final String sName)
  {
    super (sID, sName);
  }

  public BasePageSysInfoThirdPartyLibraries (@Nonnull @Nonempty final String sID,
                                             @Nonnull final String sName,
                                             @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public BasePageSysInfoThirdPartyLibraries (@Nonnull @Nonempty final String sID,
                                             @Nonnull final IReadonlyMultiLingualText aName,
                                             @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  @Nonnull
  private static IHCNode _getModuleHCNode (@Nonnull final IThirdPartyModule aModule,
                                           @Nonnull final Locale aDisplayLocale)
  {
    final HCNodeList aNL = new HCNodeList ();

    // Module name
    String sModuleText = aModule.getDisplayName ();
    if (aModule.getVersion () != null)
      sModuleText += ' ' + aModule.getVersion ().getAsString ();

    // Link (if available)
    if (aModule.getWebSiteURL () == null)
      aNL.addChild (sModuleText);
    else
      aNL.addChild (new HCA (new ReadonlySimpleURL (aModule.getWebSiteURL ())).setTarget (HCA_Target.BLANK)
                                                                              .addChild (sModuleText));
    aNL.addChild (EText.MSG_LICENSED_UNDER.getDisplayText (aDisplayLocale));

    // License text
    String sLicenseText = aModule.getLicense ().getDisplayName ();
    if (aModule.getLicense ().getVersion () != null)
      sLicenseText += ' ' + aModule.getLicense ().getVersion ().getAsString ();

    // Link (if available)
    if (aModule.getLicense ().getURL () == null)
      aNL.addChild (sLicenseText);
    else
      aNL.addChild (new HCA (new ReadonlySimpleURL (aModule.getLicense ().getURL ())).setTarget (HCA_Target.BLANK)
                                                                                     .addChild (sLicenseText));
    return aNL;
  }

  @Override
  protected void fillContent (@Nonnull final WPECTYPE aWPEC)
  {
    final HCNodeList aNodeList = aWPEC.getNodeList ();
    final Locale aDisplayLocale = aWPEC.getDisplayLocale ();

    aNodeList.addChild (HCH4.create (EText.MSG_TPM_HEADER.getDisplayText (aDisplayLocale)));

    // Third party modules
    final Set <IThirdPartyModule> aModules = ThirdPartyModuleRegistry.getAllRegisteredThirdPartyModules ();
    final HCUL aUL = aNodeList.addAndReturnChild (new HCUL ());

    // Show all required modules, sorted by name
    for (final IThirdPartyModule aModule : ContainerHelper.getSorted (aModules,
                                                                      new ComparatorHasDisplayName <IHasDisplayName> (aDisplayLocale)))
      if (!aModule.isOptional ())
        aUL.addItem (_getModuleHCNode (aModule, aDisplayLocale));
  }
}
