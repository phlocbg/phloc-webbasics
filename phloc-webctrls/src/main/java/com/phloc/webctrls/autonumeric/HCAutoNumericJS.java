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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.html.annotations.OutOfBandNode;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSInvocation;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQueryInvocation;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.html.js.provider.CollectingJSCodeProvider;

/**
 * A special script that initializes the auto numeric. It is a separate class,
 * so that potentially identical options can be merged to a single invocation.
 *
 * @author Philip Helger
 */
@OutOfBandNode
public class HCAutoNumericJS extends HCScriptOnDocumentReady
{
  private final HCAutoNumeric m_aAutoNumeric;

  @Nonnull
  private static IJSCodeProvider _createInitCode (@Nullable final JQueryInvocation aExplicitAutoNumeric,
                                                  @Nonnull final HCAutoNumeric aAutoNumeric)
  {
    final JQueryInvocation aInvocation = aExplicitAutoNumeric != null ? aExplicitAutoNumeric
                                                                     : JQuery.idRef (aAutoNumeric.getID ());

    JSInvocation ret = HCAutoNumeric.autoNumericInit (aInvocation, aAutoNumeric.getJSOptions ());
    if (aAutoNumeric.getInitialValue () != null)
    {
      // Never locale specific!
      ret = HCAutoNumeric.autoNumericSet (ret, aAutoNumeric.getInitialValue ());
    }
    return ret;
  }

  public HCAutoNumericJS (@Nonnull final HCAutoNumeric aAutoNumeric)
  {
    super (_createInitCode (null, aAutoNumeric));
    m_aAutoNumeric = aAutoNumeric;
  }

  @Nonnull
  public HCAutoNumeric getAutoNumeric ()
  {
    return m_aAutoNumeric;
  }

  @Nonnull
  public static IJSCodeProvider getMergedInitialization (@Nonnull final List <HCAutoNumericJS> aList)
  {
    if (ContainerHelper.isEmpty (aList))
      throw new IllegalArgumentException ("List may not be empty");

    if (aList.size () == 1)
    {
      // Nothing to merge
      return ContainerHelper.getFirstElement (aList).getJSCodeProvider ();
    }

    final CollectingJSCodeProvider ret = new CollectingJSCodeProvider ();
    final List <HCAutoNumericJS> aRest = ContainerHelper.newList (aList);
    while (!aRest.isEmpty ())
    {
      final HCAutoNumericJS aCurrent = aRest.remove (0);
      final JSAssocArray aCurrentJSOptions = aCurrent.getAutoNumeric ().getJSOptions ();
      final BigDecimal aCurrentInitValue = aCurrent.getAutoNumeric ().getInitialValue ();

      // Find all other datetime pickers with the same options
      final List <HCAutoNumericJS> aSameOptions = new ArrayList <HCAutoNumericJS> ();
      final Iterator <HCAutoNumericJS> itRest = aRest.iterator ();
      while (itRest.hasNext ())
      {
        final HCAutoNumericJS aCurrentRest = itRest.next ();
        if (aCurrentRest.getAutoNumeric ().getJSOptions ().equals (aCurrentJSOptions) &&
            EqualsUtils.equals (aCurrentRest.getAutoNumeric ().getInitialValue (), aCurrentInitValue))
        {
          aSameOptions.add (aCurrentRest);
          itRest.remove ();
        }
      }

      if (aSameOptions.isEmpty ())
      {
        // No other object has the same options
        ret.append (aCurrent.getJSCodeProvider ());
      }
      else
      {
        // We have multiple objects with the same options
        // Create a common selector
        IJQuerySelector aJQI = JQuerySelector.id (aCurrent.getAutoNumeric ().getID ());
        for (final HCAutoNumericJS aSameOption : aSameOptions)
          aJQI = aJQI.multiple (JQuerySelector.id (aSameOption.getAutoNumeric ().getID ()));
        // And apply once
        ret.append (_createInitCode (aJQI.invoke (), aCurrent.getAutoNumeric ()));
      }
    }
    return ret;
  }
}
