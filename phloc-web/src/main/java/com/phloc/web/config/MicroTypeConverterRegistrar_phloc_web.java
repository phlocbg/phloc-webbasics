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
package com.phloc.web.config;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.web.smtp.failed.FailedMailData;
import com.phloc.web.smtp.failed.FailedMailDataMicroTypeConverter;
import com.phloc.web.smtp.impl.EmailAttachment;
import com.phloc.web.smtp.impl.EmailAttachmentList;
import com.phloc.web.smtp.impl.EmailAttachmentListMicroTypeConverter;
import com.phloc.web.smtp.impl.EmailAttachmentMicroTypeConverter;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.web.smtp.impl.EmailDataMicroTypeConverter;
import com.phloc.web.smtp.impl.ReadonlySMTPSettings;
import com.phloc.web.smtp.impl.ReadonlySMTPSettingsMicroTypeConverter;
import com.phloc.web.smtp.impl.SMTPSettings;
import com.phloc.web.smtp.impl.SMTPSettingsMicroTypeConverter;

/**
 * Register all MicroTypeConverter implementations of this project.
 * 
 * @author Philip Helger
 */
@Immutable
@IsSPIImplementation
public final class MicroTypeConverterRegistrar_phloc_web implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    aRegistry.registerMicroElementTypeConverter (EmailAttachment.class, new EmailAttachmentMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (EmailAttachmentList.class,
                                                 new EmailAttachmentListMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (EmailData.class, new EmailDataMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (FailedMailData.class, new FailedMailDataMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (ReadonlySMTPSettings.class,
                                                 new ReadonlySMTPSettingsMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (SMTPSettings.class, new SMTPSettingsMicroTypeConverter ());
  }
}
