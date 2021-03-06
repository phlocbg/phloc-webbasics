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
package com.phloc.webctrls.autonumeric;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.MustImplementEqualsAndHashcode;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.annotations.OutOfBandNode;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.utils.SpecialNodeListModifier;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQueryInvocation;

/**
 * A special script that initializes the auto numeric. It is a separate class,
 * so that potentially identical options can be merged to a single invocation.<br>
 * Must implement equals and hashcode to be able to get removed "stateless" from
 * a node
 * 
 * @author Philip Helger
 */
@OutOfBandNode
@MustImplementEqualsAndHashcode
@SpecialNodeListModifier (HCAutoNumericSpecialNodeListModifier.class)
public class HCAutoNumericJS extends HCScriptOnDocumentReady
{
  private final HCAutoNumeric m_aAutoNumeric;

  @Nonnull
  public static IJSCodeProvider createInitCode (@Nullable final JQueryInvocation aExplicitAutoNumeric,
                                                @Nonnull final HCAutoNumeric aAutoNumeric)
  {
    final JQueryInvocation aInvocation = aExplicitAutoNumeric != null ? aExplicitAutoNumeric
                                                                     : JQuery.idRef (aAutoNumeric);

    final JSInvocation ret = HCAutoNumeric.autoNumericInit (aInvocation, aAutoNumeric.getJSOptions ());
    return ret;
  }

  public HCAutoNumericJS (@Nonnull final HCAutoNumeric aAutoNumeric)
  {
    super (createInitCode (null, aAutoNumeric));
    m_aAutoNumeric = aAutoNumeric;
  }

  @Nonnull
  public HCAutoNumeric getAutoNumeric ()
  {
    return m_aAutoNumeric;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final HCAutoNumericJS rhs = (HCAutoNumericJS) o;
    return m_aAutoNumeric.equals (rhs.m_aAutoNumeric);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aAutoNumeric).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("autoNumeric", m_aAutoNumeric).toString ();
  }
}
