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
package com.phloc.webbasics.form.validation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.phloc.commons.string.StringHelper;

/**
 * Test class for class {@link EFormErrorLevel}.
 * 
 * @author philip
 */
public final class EFormErrorLevelTest
{
  @Test
  public void testDefault ()
  {
    for (final EFormErrorLevel e : EFormErrorLevel.values ())
    {
      assertNotNull (e.getErrorLevel ());
      assertTrue (e.isSuccess () == e.getErrorLevel ().isSuccess ());
      assertTrue (e.isFailure () != e.getErrorLevel ().isSuccess ());
      assertTrue (StringHelper.hasText (e.getID ()));
      assertSame (e, EFormErrorLevel.getFromIDOrNull (e.getID ()));
      assertSame (e, EFormErrorLevel.valueOf (e.name ()));
    }
  }
}
