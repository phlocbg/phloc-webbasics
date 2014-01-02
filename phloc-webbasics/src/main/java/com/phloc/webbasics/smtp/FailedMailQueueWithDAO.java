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
package com.phloc.webbasics.smtp;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.commons.annotations.MustBeLocked;
import com.phloc.commons.annotations.MustBeLocked.ELockType;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.exceptions.InitializationException;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;
import com.phloc.web.smtp.failed.FailedMailData;
import com.phloc.web.smtp.failed.FailedMailQueue;

/**
 * A special {@link FailedMailQueue} that supports persistent storage.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class FailedMailQueueWithDAO extends FailedMailQueue
{
  private final class FMQDAO extends AbstractSimpleDAO
  {
    private static final String ELEMENT_FAILEDMAILS = "failedmails";
    private static final String ELEMENT_FAILEDMAILDATA = "failedmaildata";

    public FMQDAO (@Nullable final String sFilename)
    {
      super (sFilename);
    }

    /**
     * Initial read - just to make initialRead accessible
     * 
     * @throws DAOException
     *         In case of error
     */
    void internalInitialRead () throws DAOException
    {
      initialRead ();
    }

    @Override
    @Nonnull
    protected EChange onRead (@Nonnull final IMicroDocument aDoc)
    {
      for (final IMicroElement eFMD : aDoc.getDocumentElement ().getAllChildElements (ELEMENT_FAILEDMAILDATA))
      {
        final FailedMailData aFMD = MicroTypeConverter.convertToNative (eFMD, FailedMailData.class);
        if (aFMD != null)
          FailedMailQueueWithDAO.this.add (aFMD);
      }
      return EChange.UNCHANGED;
    }

    @Override
    @Nonnull
    protected IMicroDocument createWriteData ()
    {
      final IMicroDocument aDoc = new MicroDocument ();
      final IMicroElement eRoot = aDoc.appendElement (ELEMENT_FAILEDMAILS);
      for (final FailedMailData aFMD : FailedMailQueueWithDAO.this.getAllFailedMails ())
        eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aFMD, ELEMENT_FAILEDMAILDATA));
      return aDoc;
    }

    // Change visibility to default
    @MustBeLocked (ELockType.WRITE)
    void markChanged ()
    {
      super.markAsChanged ();
    }
  }

  private final FMQDAO m_aDAO;

  public FailedMailQueueWithDAO (@Nullable final String sFilename)
  {
    try
    {
      m_aDAO = new FMQDAO (sFilename);
      // Read here, so that the DAO can be used
      m_aDAO.internalInitialRead ();
    }
    catch (final DAOException ex)
    {
      throw new InitializationException ("Failed to init FailedMailQueueWithDAO with filename '" + sFilename + "'", ex);
    }
  }

  private void _markAsChanged ()
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      // Must be called in a lock
      m_aDAO.markChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public void add (@Nonnull final FailedMailData aFailedMailData)
  {
    super.add (aFailedMailData);
    _markAsChanged ();
  }

  @Override
  @Nullable
  public FailedMailData remove (@Nullable final String sID)
  {
    final FailedMailData ret = super.remove (sID);
    if (ret != null)
      _markAsChanged ();
    return ret;
  }

  @Override
  @Nonnull
  @ReturnsMutableCopy
  public List <FailedMailData> removeAll ()
  {
    final List <FailedMailData> ret = super.removeAll ();
    if (!ret.isEmpty ())
      _markAsChanged ();
    return ret;
  }
}
