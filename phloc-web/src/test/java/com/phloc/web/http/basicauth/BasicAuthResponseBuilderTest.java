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
package com.phloc.web.http.basicauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test class for class {@link BasicAuthServerBuilder}.
 * 
 * @author philip
 */
public final class BasicAuthResponseBuilderTest
{
  @Test
  public void testBasic ()
  {
    final BasicAuthServerBuilder b = new BasicAuthServerBuilder ();
    try
    {
      // Mandatory realm not present
      b.build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    b.setRealm ("xyz");
    assertEquals ("Basic realm=\"xyz\"", b.build ());
    b.setRealm ("anything");
    assertEquals ("Basic realm=\"anything\"", b.build ());
  }
}
