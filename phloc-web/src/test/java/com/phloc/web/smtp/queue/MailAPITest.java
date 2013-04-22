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
package com.phloc.web.smtp.queue;

import org.junit.Ignore;
import org.junit.Test;

import com.phloc.commons.email.EmailAddress;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.EmailData;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.listener.LoggingConnectionListener;
import com.phloc.web.smtp.listener.LoggingTransportListener;
import com.phloc.web.smtp.settings.SMTPSettings;

/**
 * Test class for class {@link MailAPI}.
 * 
 * @author philip
 */
public final class MailAPITest
{
  @Ignore ("to avoid spamming my mailbox")
  @Test
  public void testBasic ()
  {
    // This file might not be present, as it contains the real-life SMTP
    // settings. It should reside in src/test/resource and is SVN ignored by
    // name
    final IReadableResource aRes = new ClassPathResource ("smtp-settings.xml");
    if (aRes.exists ())
    {
      // Setup listeners
      MailTransportSettings.setConnectionListener (new LoggingConnectionListener ());
      MailTransportSettings.setTransportListener (new LoggingTransportListener ());

      final SMTPSettings aSMTPSettings = MicroTypeConverter.convertToNative (MicroReader.readMicroXML (aRes)
                                                                                        .getDocumentElement (),
                                                                             SMTPSettings.class);
      final IEmailData aMailData = new EmailData (EEmailType.TEXT);
      aMailData.setTo (new EmailAddress ("ph@phloc.com"));
      aMailData.setFrom (new EmailAddress ("auto@phloc.com"));
      aMailData.setSubject ("JÜnit test with späcial käräktärs");
      aMailData.setBody ("Hi there\nLine 2\n4 special chars: äöüß\n123456789\nBest regards: phloc-web");
      MailAPI.queueMail (aSMTPSettings, aMailData);
      MailAPI.stop ();
    }
  }
}
