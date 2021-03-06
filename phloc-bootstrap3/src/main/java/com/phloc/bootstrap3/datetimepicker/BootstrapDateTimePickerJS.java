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
package com.phloc.bootstrap3.datetimepicker;

import javax.annotation.Nonnull;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.annotations.OutOfBandNode;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.utils.SpecialNodeListModifier;

/**
 * A special script that initializes the {@link BootstrapDateTimePicker}. It is
 * a separate class, so that potentially identical options can be merged to a
 * single invocation.
 * 
 * @author Philip Helger
 */
@OutOfBandNode
@SpecialNodeListModifier (BootstrapDateTimePickerSpecialNodeListModifier.class)
public class BootstrapDateTimePickerJS extends HCScript
{
  private final BootstrapDateTimePicker m_aDTP;

  public BootstrapDateTimePickerJS (@Nonnull final BootstrapDateTimePicker aDTP)
  {
    super (aDTP.invoke (aDTP.getJSOptions ()));
    m_aDTP = aDTP;
  }

  @Nonnull
  public BootstrapDateTimePicker getDateTimePicker ()
  {
    return m_aDTP;
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("DTP", m_aDTP).toString ();
  }
}
