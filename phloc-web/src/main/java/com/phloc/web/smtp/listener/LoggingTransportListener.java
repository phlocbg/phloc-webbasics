package com.phloc.web.smtp.listener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.error.EErrorLevel;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.log.LogUtils;

/**
 * An implementation of {@link TransportListener} that logs stuff to a logger.
 * 
 * @author Philip Helger
 */
public class LoggingTransportListener implements TransportListener
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingConnectionListener.class);

  private final EErrorLevel m_eErrorLevel;

  public LoggingTransportListener ()
  {
    this (EErrorLevel.INFO);
  }

  public LoggingTransportListener (@Nonnull final EErrorLevel eErrorLevel)
  {
    if (eErrorLevel == null)
      throw new NullPointerException ("ErrorLevel");
    m_eErrorLevel = eErrorLevel;
  }

  @Nonnull
  public static String getAddressesString (@Nullable final Address [] aAddresses)
  {
    if (aAddresses == null || aAddresses.length == 0)
      return "[]";
    final StringBuilder aSB = new StringBuilder ().append ('[');
    for (final Address aAddress : aAddresses)
    {
      if (aSB.length () > 1)
        aSB.append (", ");
      aSB.append (aAddress == null ? "null" : aAddress.toString ());
    }
    return aSB.append (']').toString ();
  }

  @Nonnull
  public static String getMessageString (@Nullable final Message aMsg)
  {
    if (aMsg == null)
      return "null";
    if (aMsg instanceof MimeMessage)
      try
      {
        return "MIME-Msg " + ((MimeMessage) aMsg).getMessageID ();
      }
      catch (final MessagingException ex)
      {
        return "MIME-Msg " + ex.getClass ().getName () + " - " + ex.getMessage ();
      }
    return CGStringHelper.getClassLocalName (aMsg.getClass ());
  }

  @Nonnull
  public static String getLogString (@Nonnull final TransportEvent aEvent)
  {
    return "validSent=" +
           getAddressesString (aEvent.getValidSentAddresses ()) +
           "; validUnsent=" +
           getAddressesString (aEvent.getValidUnsentAddresses ()) +
           "; invalid=" +
           getAddressesString (aEvent.getInvalidAddresses ()) +
           "; msg=" +
           getMessageString (aEvent.getMessage ());
  }

  public void messageDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message delivered: " + getLogString (aEvent));
  }

  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message not delivered: " + getLogString (aEvent));
  }

  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
  {
    LogUtils.log (s_aLogger, m_eErrorLevel, "Message partially delivered: " + getLogString (aEvent));
  }
}
