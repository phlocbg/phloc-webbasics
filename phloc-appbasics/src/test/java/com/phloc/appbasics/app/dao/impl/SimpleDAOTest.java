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
package com.phloc.appbasics.app.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.annotation.Nonnull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.phloc.appbasics.mock.AppBasicTestRule;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;

/**
 * Test class for class {@link AbstractSimpleDAO}.
 * 
 * @author Philip Helger
 */
public class SimpleDAOTest
{
  private static final class MyDAO extends AbstractSimpleDAO
  {
    public MyDAO (final String sFilename) throws DAOException
    {
      super (sFilename);
      initialRead ();
    }

    @Override
    @Nonnull
    protected EChange onRead (@Nonnull final IMicroDocument aDoc)
    {
      return EChange.UNCHANGED;
    }

    @Override
    @Nonnull
    protected IMicroDocument createWriteData ()
    {
      final MicroDocument aDoc = new MicroDocument ();
      return aDoc;
    }
  }

  @Rule
  public final TestRule m_aRule = new AppBasicTestRule ();

  @Test
  public void testBasic () throws DAOException
  {
    final MyDAO aDAO = new MyDAO ("notexisting.xml");
    assertNotNull (aDAO.getLastInitDateTime ());
    assertEquals (1, aDAO.getInitCount ());
    assertNull (aDAO.getLastReadDateTime ());
    assertEquals (0, aDAO.getReadCount ());
    assertNull (aDAO.getLastWriteDateTime ());
    assertEquals (0, aDAO.getWriteCount ());
  }
}
