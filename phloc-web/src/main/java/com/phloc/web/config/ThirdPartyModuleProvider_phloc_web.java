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
package com.phloc.web.config;

import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.thirdparty.ELicense;
import com.phloc.commons.thirdparty.IThirdPartyModule;
import com.phloc.commons.thirdparty.IThirdPartyModuleProviderSPI;
import com.phloc.commons.thirdparty.ThirdPartyModule;
import com.phloc.commons.version.Version;

/**
 * Implement this SPI interface if your JAR file contains external third party
 * modules.
 * 
 * @author philip
 */
@IsSPIImplementation
public final class ThirdPartyModuleProvider_phloc_web implements IThirdPartyModuleProviderSPI
{
  private static final IThirdPartyModule DNSJAVA = new ThirdPartyModule ("dnsjava",
                                                                         "Brian Wellington",
                                                                         ELicense.BSD,
                                                                         new Version (2, 1, 1),
                                                                         "http://www.xbill.org/dnsjava/",
                                                                         true);

  private static final IThirdPartyModule RHINO = new ThirdPartyModule ("Rhino",
                                                                       "Mozilla",
                                                                       ELicense.MPL20,
                                                                       new Version ("1.7R4"),
                                                                       "https://developer.mozilla.org/de/docs/Rhino",
                                                                       true);

  private static final IThirdPartyModule JAVAX_MAIL = new ThirdPartyModule ("JavaMail",
                                                                            "Oracle",
                                                                            ELicense.BSD,
                                                                            new Version (1, 5, 0),
                                                                            "http://javamail.java.net/",
                                                                            true);

  private static final IThirdPartyModule JSCH = new ThirdPartyModule ("JSch",
                                                                      "JCraft Inc.",
                                                                      ELicense.BSD,
                                                                      new Version (0, 1, 49),
                                                                      "http://www.jcraft.com/jsch/",
                                                                      true);

  @Nullable
  public IThirdPartyModule [] getAllThirdPartyModules ()
  {
    return new IThirdPartyModule [] { DNSJAVA, RHINO, JAVAX_MAIL, JSCH };
  }
}
