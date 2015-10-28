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
package com.phloc.web.smtp.transport;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.concurrent.ThreadUtils;
import com.phloc.commons.email.EmailAddress;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.idfactory.MemoryIntIDFactory;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.web.smtp.EEmailType;
import com.phloc.web.smtp.IEmailData;
import com.phloc.web.smtp.EmailGlobalSettings;
import com.phloc.web.smtp.impl.EmailData;
import com.phloc.web.smtp.impl.SMTPSettings;
import com.phloc.web.smtp.transport.MailAPI;
import com.phloc.web.smtp.transport.listener.LoggingConnectionListener;
import com.phloc.web.smtp.transport.listener.LoggingTransportListener;

/**
 * Test class for class {@link MailAPI}.
 * 
 * @author Philip Helger
 */
public final class MailAPITest
{
  static
  {
    GlobalIDFactory.setPersistentIntIDFactory (new MemoryIntIDFactory ());
  }

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
      GlobalDebug.setDebugModeDirect (true);
      EmailGlobalSettings.enableJavaxMailDebugging (GlobalDebug.isDebugMode ());

      // Setup debug listeners
      EmailGlobalSettings.setConnectionListener (new LoggingConnectionListener ());
      EmailGlobalSettings.setTransportListener (new LoggingTransportListener ());

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

      // try to queue again after MailAPI was stopped - should end up in failed
      // mail queue
      assertEquals (0, MailAPI.getFailedMailQueue ().size ());
      MailAPI.queueMail (aSMTPSettings, aMailData);
      assertEquals (1, MailAPI.getFailedMailQueue ().size ());

      GlobalDebug.setDebugModeDirect (false);
    }
  }

  @Ignore ("to avoid spamming my mailbox")
  @Test
  public void testStopImmediately ()
  {
    // This file might not be present, as it contains the real-life SMTP
    // settings. It should reside in src/test/resource and is SVN ignored by
    // name
    final IReadableResource aRes = new ClassPathResource ("smtp-settings.xml");
    if (aRes.exists ())
    {
      final SMTPSettings aSMTPSettings = MicroTypeConverter.convertToNative (MicroReader.readMicroXML (aRes)
                                                                                        .getDocumentElement (),
                                                                             SMTPSettings.class);
      final IEmailData aMailData = new EmailData (EEmailType.TEXT);
      aMailData.setTo (new EmailAddress ("ph@phloc.com"));
      aMailData.setFrom (new EmailAddress ("auto@phloc.com"));
      aMailData.setSubject ("JÜnit test with späcial käräktärs");
      aMailData.setBody ("Hi there\nLine 2\n4 special chars: äöüß\n123456789\nBest regards: phloc-web");

      final List <IEmailData> aMails = new ArrayList <IEmailData> ();
      for (int i = 0; i < 10; ++i)
        aMails.add (aMailData);

      MailAPI.queueMails (aSMTPSettings, aMails);
      ThreadUtils.sleep (20);

      // Stop immediately
      MailAPI.stop (true);
    }
  }
}
