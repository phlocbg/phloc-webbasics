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
package com.phloc.appbasics.app.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.appbasics.app.dao.IDAODataProvider;
import com.phloc.appbasics.app.io.ConstantHasFilename;
import com.phloc.appbasics.app.io.IHasFilename;
import com.phloc.appbasics.app.io.WebIO;
import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.mock.PhlocTestUtils;
import com.phloc.commons.mutable.MutableInt;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringParser;

/**
 * Test class for class {@link DefaultDAO}.
 * 
 * @author philip
 */
public final class DefaultDAOTest
{
  @Rule
  public final TestRule m_aRule = new AppBasicTestRule ();

  @Test
  public void testBasic () throws DAOException
  {
    final MutableInt aInt = new MutableInt (-1);
    final IDAODataProvider aDP = new IDAODataProvider ()
    {
      @Nonnull
      public Charset getCharset ()
      {
        return CCharset.CHARSET_ISO_8859_1_OBJ;
      }

      @Nonnull
      public EChange initForFirstTimeUsage () throws Exception
      {
        aInt.set (0);
        return EChange.UNCHANGED;
      }

      @Nonnull
      public EChange readFromStream (final InputStream aIS) throws Exception
      {
        aInt.set (StringParser.parseInt (StreamUtils.getAllBytesAsString (aIS, getCharset ()), -1));
        return EChange.UNCHANGED;
      }

      public void fillStringBuilderForSaving (final StringBuilder aSB) throws Exception
      {
        aSB.append (aInt.intValue ());
      }

      public boolean isContentValidForSaving (@Nullable final String sContent)
      {
        return StringParser.isInt (sContent);
      }
    };
    final IHasFilename aHF = new ConstantHasFilename ("abc.txt");
    // Ensure that no such file exists
    WebIO.deleteFile (aHF.getFilename ());
    final DefaultDAO aDAO1 = new DefaultDAO (aHF, aDP);
    PhlocTestUtils.testDefaultImplementationWithEqualContentObject (aDAO1, new DefaultDAO (aHF, aDP));
    PhlocTestUtils.testDefaultImplementationWithDifferentContentObject (aDAO1,
                                                                        new DefaultDAO (new ConstantHasFilename ("abc2.txt"),
                                                                                        aDP));

    // Initial value
    assertEquals (-1, aInt.intValue ());
    // Read or init
    aDAO1.readFromFile ();
    assertEquals (0, aInt.intValue ());
    // Explicitly set a new value
    aInt.set (5);
    assertFalse (aDAO1.hasPendingChanges ());
    // Auto save is enabled
    aDAO1.markAsChanged ();
    assertFalse (aDAO1.hasPendingChanges ());
    // Nothing to do
    aDAO1.writeToFileOnPendingChanges ();
    assertFalse (aDAO1.hasPendingChanges ());
    // Read again
    aInt.set (-2);
    aDAO1.readFromFile ();
    assertEquals (5, aInt.intValue ());
    assertFalse (aDAO1.hasPendingChanges ());
  }
}
