package com.phloc.webbasics.smtp;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.appbasics.app.dao.impl.AbstractSimpleDAO;
import com.phloc.appbasics.app.dao.impl.DAOException;
import com.phloc.commons.IHasSize;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.web.smtp.impl.SMTPSettings;

/**
 * This class contains all globally available SMTP settings. The settings are
 * identified by name.
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

  public boolean containsSettings (@Nullable final String sID)
  {
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

  @Nullable
  public NamedSMTPSettings getSettings (@Nullable final String sID)
  {
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

  @Nullable
  public NamedSMTPSettings addSettings (@Nonnull @Nonempty final String sName, @Nonnull final SMTPSettings aSettings)
  {
    final NamedSMTPSettings aNamedSettings = new NamedSMTPSettings (sName, aSettings);

    m_aRWLock.writeLock ().lock ();
    try
    {
      _addItem (aNamedSettings);
      return aNamedSettings;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Nullable
  public EChange removeSettings (@Nullable final String sID)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      return EChange.valueOf (m_aMap.remove (sID) != null);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
