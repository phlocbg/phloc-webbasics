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
package com.phloc.bootstrap3.config;

import javax.annotation.Nullable;

import com.phloc.bootstrap3.CBootstrap;
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
 * @author Philip Helger
 */
@IsSPIImplementation
public final class ThirdPartyModuleProvider_phloc_bootstrap3 implements IThirdPartyModuleProviderSPI
{
  public static final IThirdPartyModule BOOTSTRAP3 = new ThirdPartyModule ("Bootstrap",
                                                                           "Twitter",
                                                                           ELicense.MIT,
                                                                           CBootstrap.BOOTSTRAP_VERSION_320,
                                                                           "http://getbootstrap.com/");

  public static final IThirdPartyModule BOOTSTRAP_DATETIME_PICKER = new ThirdPartyModule ("Bootstrap Datetime Picker",
                                                                                          "Sebastian Malot",
                                                                                          ELicense.APACHE2,
                                                                                          new Version (2013, 12, 17),
                                                                                          "http://www.malot.fr/bootstrap-datetimepicker/index.php");

  @Nullable
  public IThirdPartyModule [] getAllThirdPartyModules ()
  {
    return new IThirdPartyModule [] { BOOTSTRAP3, BOOTSTRAP_DATETIME_PICKER };
  }
}
