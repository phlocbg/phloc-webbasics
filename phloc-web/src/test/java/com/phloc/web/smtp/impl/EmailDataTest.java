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
package com.phloc.web.smtp.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.email.EmailAddress;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.web.smtp.EEmailType;

public final class EmailDataTest
{
  @Test
  public void testBasic ()
  {
    final EmailData aEmailData = new EmailData (EEmailType.TEXT);
    final EmailAddress aMA = new EmailAddress ("ph@phloc.com", "Philip");

    aEmailData.setFrom (aMA);
    assertEquals (aMA, aEmailData.getFrom ());

    aEmailData.setReplyTo (aMA);
    assertEquals (aMA, aEmailData.getReplyTo ().get (0));

    aEmailData.setTo (aMA);
    assertEquals (aMA, aEmailData.getTo ().get (0));

    aEmailData.setCc (aMA);
    assertEquals (aMA, aEmailData.getCc ().get (0));

    aEmailData.setBcc (aMA);
    assertEquals (aMA, aEmailData.getBcc ().get (0));

    PhlocTestUtils.testMicroTypeConversion (aEmailData);

    assertEquals (0, aEmailData.getAttributeCount ());
    aEmailData.setAttribute ("test", "foo");
    assertEquals (1, aEmailData.getAttributeCount ());
    aEmailData.setAttribute ("test2", "bar");
    assertEquals (2, aEmailData.getAttributeCount ());

    PhlocTestUtils.testMicroTypeConversion (aEmailData);
  }
}
