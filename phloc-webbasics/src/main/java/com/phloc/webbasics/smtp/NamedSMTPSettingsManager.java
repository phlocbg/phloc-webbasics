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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.appbasics.security.audit.AuditUtils;
import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.smtp.ISMTPSettings;

/**
 * This class manages {@link NamedSMTPSettings} objects.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class NamedSMTPSettingsManager extends AbstractSimpleDAO implements IHasSize
{
  private static final String ELEMENT_NAMEDSMTPSETTINGSLIST = "namedsmtpsettingslist";
  private static final String ELEMENT_NAMEDSMTPSETTINGS = "namedsmtpsettings";

  private final Map <String, NamedSMTPSettings> m_aMap = new HashMap <String, NamedSMTPSettings> ();

  public NamedSMTPSettingsManager (@Nonnull @Nonempty final String sFilename) throws DAOException
  {
    super (sFilename);
    initialRead ();
  }

  @Override
  @Nonnull
  protected EChange onRead (@Nonnull final IMicroDocument aDoc)
  {
    for (final IMicroElement eNamedSMTPSettings : aDoc.getDocumentElement ()
                                                      .getAllChildElements (ELEMENT_NAMEDSMTPSETTINGS))
      _addItem (MicroTypeConverter.convertToNative (eNamedSMTPSettings, NamedSMTPSettings.class));
    return EChange.UNCHANGED;
  }

  @Override
  @Nonnull
  protected IMicroDocument createWriteData ()
  {
    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_NAMEDSMTPSETTINGSLIST);
    for (final NamedSMTPSettings aNamedSMTPSettings : ContainerHelper.getSortedByKey (m_aMap).values ())
      eRoot.appendChild (MicroTypeConverter.convertToMicroElement (aNamedSMTPSettings, ELEMENT_NAMEDSMTPSETTINGS));
    return aDoc;
  }

  private void _addItem (@Nonnull final NamedSMTPSettings aNamedSMTPSettings)
  {
    final String sUserID = aNamedSMTPSettings.getID ();
    if (m_aMap.containsKey (sUserID))
      throw new IllegalArgumentException ("NamedSMTPSettings ID " + sUserID + " is already in use!");
    m_aMap.put (sUserID, aNamedSMTPSettings);
  }

  @Nonnegative
  public int size ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.size ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  public boolean isEmpty ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.isEmpty ();
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @return A copy of all contained SMTP settings as map from ID to object.
   *         Never <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public Map <String, NamedSMTPSettings> getAllSettings ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newMap (m_aMap);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Check if the specified settings ID is contained or not.
   * 
   * @param sID
   *        The ID to check. May be <code>null</code>.
   * @return <code>true</code> if the passed ID is contained
   */
  public boolean containsSettings (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return false;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.containsKey (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Get the settings of the specified ID
   * 
   * @param sID
   *        The ID to search. May be <code>null</code>.
   * @return <code>null</code> if no such settings are contained.
   */
  @Nullable
  public NamedSMTPSettings getSettings (@Nullable final String sID)
  {
    if (StringHelper.hasNoText (sID))
      return null;

    m_aRWLock.readLock ().lock ();
    try
    {
      return m_aMap.get (sID);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Create a new settings object.
   * 
   * @param sName
   *        The name of the settings. May neither be <code>null</code> nor
   *        empty.
   * @param aSettings
   *        The main SMTP settings. May not be <code>null</code>.
   * @return The created {@link NamedSMTPSettings} object and never
   *         <code>null</code>.
   */
  @Nonnull
  public NamedSMTPSettings addSettings (@Nonnull @Nonempty final String sName, @Nonnull final ISMTPSettings aSettings)
  {
    final NamedSMTPSettings aNamedSettings = new NamedSMTPSettings (sName, aSettings);

    m_aRWLock.writeLock ().lock ();
    try
    {
      _addItem (aNamedSettings);
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }

    AuditUtils.onAuditCreateSuccess (NamedSMTPSettings.OT_NAMED_SMTP_SETTINGS,
                                     aNamedSettings.getID (),
                                     aNamedSettings.getName (),
                                     aSettings.getHostName (),
                                     Integer.toString (aSettings.getPort ()),
                                     aSettings.getCharset (),
                                     Boolean.toString (aSettings.isSSLEnabled ()),
                                     Boolean.toString (aSettings.isSTARTTLSEnabled ()),
                                     Long.toString (aSettings.getConnectionTimeoutMilliSecs ()),
                                     Long.toString (aSettings.getTimeoutMilliSecs ()));
    return aNamedSettings;
  }

  /**
   * Update an existing settings object.
   * 
   * @param sID
   *        The ID of the settings object to be updated. May be
   *        <code>null</code>.
   * @param sName
   *        The new name of the settings. May neither be <code>null</code> nor
   *        empty.
   * @param aSettings
   *        The new SMTP settings. May not be <code>null</code>.
   * @return {@link EChange#CHANGED} if something was changed.
   */
  @Nullable
  public EChange updateSettings (@Nullable final String sID,
                                 @Nonnull @Nonempty final String sName,
                                 @Nonnull final ISMTPSettings aSettings)
  {
    final NamedSMTPSettings aNamedSettings = getSettings (sID);
    if (aNamedSettings == null)
    {
      AuditUtils.onAuditModifyFailure (NamedSMTPSettings.OT_NAMED_SMTP_SETTINGS, sID, "no-such-id");
      return EChange.UNCHANGED;
    }

    m_aRWLock.writeLock ().lock ();
    try
    {
      EChange eChange = EChange.UNCHANGED;
      eChange = eChange.or (aNamedSettings.setName (sName));
      eChange = eChange.or (aNamedSettings.setSMTPSettings (aSettings));
      if (eChange.isUnchanged ())
        return EChange.UNCHANGED;
      markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    AuditUtils.onAuditModifySuccess (NamedSMTPSettings.OT_NAMED_SMTP_SETTINGS,
                                     aNamedSettings.getID (),
                                     aNamedSettings.getName (),
                                     aSettings.getHostName (),
                                     Integer.toString (aSettings.getPort ()),
                                     aSettings.getCharset (),
                                     Boolean.toString (aSettings.isSSLEnabled ()),
                                     Boolean.toString (aSettings.isSTARTTLSEnabled ()),
                                     Long.toString (aSettings.getConnectionTimeoutMilliSecs ()),
                                     Long.toString (aSettings.getTimeoutMilliSecs ()));
    return EChange.CHANGED;
  }

  /**
   * Remove the SMTP settings with the specified ID.
   * 
   * @param sID
   *        The ID to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if a removal was performed.
   */
  @Nullable
  public EChange removeSettings (@Nullable final String sID)
  {
    EChange eChange;
    m_aRWLock.writeLock ().lock ();
    try
    {
      eChange = EChange.valueOf (m_aMap.remove (sID) != null);
      if (eChange.isChanged ())
        markAsChanged ();
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    if (eChange.isChanged ())
      AuditUtils.onAuditDeleteSuccess (NamedSMTPSettings.OT_NAMED_SMTP_SETTINGS, sID);
    else
      AuditUtils.onAuditDeleteFailure (NamedSMTPSettings.OT_NAMED_SMTP_SETTINGS, sID, "no-such-id");
    return eChange;
  }

  /**
   * Remove all contained SMTP settings
   * 
   * @return {@link EChange#CHANGED} if a removal was performed.
   */
  @Nullable
  public EChange removeAllSettings ()
  {
    // Get all available settings IDs
    Set <String> aAllIDs;
    m_aRWLock.readLock ().lock ();
    try
    {
      aAllIDs = ContainerHelper.newSet (m_aMap.keySet ());
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }

    // Batch remove all settings
    EChange eChange = EChange.UNCHANGED;
    beginWithoutAutoSave ();
    try
    {
      for (final String sID : aAllIDs)
        eChange = eChange.or (removeSettings (sID));
    }
    finally
    {
      endWithoutAutoSave ();
    }
    return eChange;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
