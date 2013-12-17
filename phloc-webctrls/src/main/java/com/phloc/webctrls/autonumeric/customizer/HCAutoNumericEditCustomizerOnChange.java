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
package com.phloc.webctrls.autonumeric.customizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.js.EJSEvent;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.webctrls.autonumeric.IHCAutoNumericEditCustomizer;

/**
 * A special {@link IHCAutoNumericEditCustomizer} that sets an event handler
 * onchange.
 * 
 * @author Philip Helger
 */
public class HCAutoNumericEditCustomizerOnChange implements IHCAutoNumericEditCustomizer
{
  private final IJSCodeProvider m_aOnChange;

  public HCAutoNumericEditCustomizerOnChange (@Nullable final IJSCodeProvider aOnChange)
  {
    m_aOnChange = aOnChange;
  }

  public void customize (@Nonnull final HCEdit aEdit)
  {
    if (m_aOnChange != null)
      aEdit.setEventHandler (EJSEvent.ONCHANGE, m_aOnChange);
  }
}
