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
package com.phloc.web.http;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.commons.mock.PhlocAssert;

public final class AcceptEncodingHandlerTest
{
  @Test
  public void testReadFromSpecs ()
  {
    assertNotNull (AcceptEncodingHandler.getAcceptEncodings ("compress, gzip"));
    assertNotNull (AcceptEncodingHandler.getAcceptEncodings (""));
    assertNotNull (AcceptEncodingHandler.getAcceptEncodings ("*"));
    assertNotNull (AcceptEncodingHandler.getAcceptEncodings ("compress;q=0.5, gzip;q=1.0"));
    assertNotNull (AcceptEncodingHandler.getAcceptEncodings ("gzip;q=1.0, identity; q=0.5, *;q=0"));
  }

  @Test
  public void testReadFromSpecs2 ()
  {
    AcceptEncodingList aAE = AcceptEncodingHandler.getAcceptEncodings ("compress, gzip");
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("identity"));
    aAE = AcceptEncodingHandler.getAcceptEncodings ("");
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("identity"));
    aAE = AcceptEncodingHandler.getAcceptEncodings ("*");
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("identity"));
    aAE = AcceptEncodingHandler.getAcceptEncodings ("compress;q=0.5, gzip;q=1.0");
    PhlocAssert.assertEquals (0.5, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("identity"));
    aAE = AcceptEncodingHandler.getAcceptEncodings ("gzip;q=1.0, identity; q=0.5, *;q=0");
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (0, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (0.5, aAE.getQualityOfEncoding ("identity"));
    aAE = AcceptEncodingHandler.getAcceptEncodings ("gzip;q=1.0, identity; q=0.5, *;q=0.7");
    PhlocAssert.assertEquals (0.7, aAE.getQualityOfEncoding ("compress"));
    PhlocAssert.assertEquals (1, aAE.getQualityOfEncoding ("gzip"));
    PhlocAssert.assertEquals (0.7, aAE.getQualityOfEncoding ("other"));
    PhlocAssert.assertEquals (0.5, aAE.getQualityOfEncoding ("identity"));
  }
}
