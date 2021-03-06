/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.config;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.thirdparty.ELicense;
import com.phloc.commons.thirdparty.IThirdPartyModule;
import com.phloc.commons.thirdparty.IThirdPartyModuleProviderSPI;
import com.phloc.commons.thirdparty.ThirdPartyModule;
import com.phloc.commons.version.Version;
import com.phloc.web.config.ThirdPartyModuleProvider_phloc_web;

/**
 * Implement this SPI interface if your JAR file contains external third party
 * modules.
 * 
 * @author Philip Helger
 */
@IsSPIImplementation
public final class ThirdPartyModuleProvider_phloc_webbasics implements IThirdPartyModuleProviderSPI
{
  public static final IThirdPartyModule JAVAX_MAIL = ThirdPartyModuleProvider_phloc_web.JAVAX_MAIL.getAsNonOptionalCopy ();

  public static final IThirdPartyModule JSCH = ThirdPartyModuleProvider_phloc_web.JSCH.getAsOptionalCopy ();

  public static final IThirdPartyModule COMMONS_NET = new ThirdPartyModule ("Apache Commons Net",
                                                                            "Apache",
                                                                            ELicense.APACHE2,
                                                                            new Version (3, 3, 0),
                                                                            "http://commons.apache.org/net/",
                                                                            true);

  public static final IThirdPartyModule OPEN_CSV = new ThirdPartyModule ("OpenCSV",
                                                                         "Glen Smith",
                                                                         ELicense.APACHE2,
                                                                         new Version (2, 3),
                                                                         "http://opencsv.sourceforge.net/",
                                                                         true);

  @Nullable
  public IThirdPartyModule [] getAllThirdPartyModules ()
  {
    return new IThirdPartyModule [] { JAVAX_MAIL, JSCH, COMMONS_NET, OPEN_CSV };
  }
}
