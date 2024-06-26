/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.smtp.transport.listener;

import javax.annotation.Nonnull;
import jakarta.mail.event.TransportEvent;
import jakarta.mail.event.TransportListener;

import com.phloc.commons.string.ToStringGenerator;

/**
 * An implementation of {@link TransportListener} that does nothing.
 * 
 * @author Philip Helger
 */
public class DoNothingTransportListener implements TransportListener
{
  @Override
  public void messageDelivered (@Nonnull final TransportEvent aEvent)
  {}

  @Override
  public void messageNotDelivered (@Nonnull final TransportEvent aEvent)
  {}

  @Override
  public void messagePartiallyDelivered (@Nonnull final TransportEvent aEvent)
  {}

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
