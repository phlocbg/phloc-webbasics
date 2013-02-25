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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistrarSPI;
import com.phloc.commons.microdom.convert.IMicroTypeConverterRegistry;
import com.phloc.web.smtp.attachment.EmailAttachment;
import com.phloc.web.smtp.attachment.EmailAttachmentMicroTypeConverter;
import com.phloc.web.smtp.attachment.EmailAttachmentList;
import com.phloc.web.smtp.attachment.EmailAttachmentListMicroTypeConverter;
import com.phloc.web.smtp.settings.SMTPSettings;
import com.phloc.web.smtp.settings.SMTPSettingsMicroTypeConverter;

/**
 * Register all MicroTypeConverter implementations of this project.
 * 
 * @author philip
 */
@Immutable
@IsSPIImplementation
public final class MicroTypeConverterRegistrar_phloc_web implements IMicroTypeConverterRegistrarSPI
{
  public void registerMicroTypeConverter (@Nonnull final IMicroTypeConverterRegistry aRegistry)
  {
    aRegistry.registerMicroElementTypeConverter (EmailAttachment.class, new EmailAttachmentMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (EmailAttachmentList.class, new EmailAttachmentListMicroTypeConverter ());
    aRegistry.registerMicroElementTypeConverter (SMTPSettings.class, new SMTPSettingsMicroTypeConverter ());
  }
}
