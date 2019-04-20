package com.phloc.web.smtp;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.LockedContainerHelper;
import com.phloc.commons.email.EmailAddressUtils;
import com.phloc.commons.state.EValidity;

public class MXChecker
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final MXChecker INSTANCE = new MXChecker ();
  }

  /**
   * Ctor for singleton creation
   */
  protected MXChecker ()
  {
    // protected
  }

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static MXChecker getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  private static final Logger LOG = LoggerFactory.getLogger (MXChecker.class);
  private final ReentrantReadWriteLock m_aLock = new ReentrantReadWriteLock ();
  private final Map <String, EValidity> m_aCheckedRecords = ContainerHelper.newMap ();

  public boolean isValidMXEntry (@Nullable final String sEmail)
  {
    final String sUnifiedEmail = EmailAddressUtils.getUnifiedEmailAddress (sEmail);

    // MX record checking
    final int i = sUnifiedEmail.indexOf ('@');
    final String sHostName = sUnifiedEmail.substring (i + 1);

    EValidity eValidity = null;
    eValidity = LockedContainerHelper.getByKey (sHostName, this.m_aCheckedRecords, this.m_aLock);
    if (eValidity != null)
    {
      return eValidity.isValid ();
    }
    final EValidity eCheckedValidity = _hasMXRecord (sHostName);
    this.m_aLock.writeLock ().lock ();
    try
    {
      eValidity = this.m_aCheckedRecords.get (sHostName);
      if (eValidity == null)
      {
        eValidity = eCheckedValidity;
        this.m_aCheckedRecords.put (sHostName, eValidity);
      }
      return eValidity.isValid ();
    }
    finally
    {
      this.m_aLock.writeLock ().unlock ();
    }
  }

  private static EValidity _hasMXRecord (@Nonnull final String sHostName)
  {
    try
    {
      final Record [] aRecords = new Lookup (sHostName, Type.MX).run ();
      return EValidity.valueOf (aRecords != null && aRecords.length > 0);
    }
    catch (final Exception ex)
    {
      // Do not log this message, as this method is potentially called very
      // often!
      LOG.warn ("Failed to check for MX record on host '" +
                sHostName +
                "': " +
                ex.getClass ().getName () +
                " - " +
                ex.getMessage ());
      return EValidity.INVALID;
    }
  }

  public void clear ()
  {
    LockedContainerHelper.clear (this.m_aCheckedRecords, this.m_aLock);
  }
}
