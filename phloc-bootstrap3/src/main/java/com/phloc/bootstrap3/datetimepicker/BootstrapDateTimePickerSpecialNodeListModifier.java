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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.hc.utils.IHCSpecialNodeListModifier;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.html.js.provider.CollectingJSCodeProvider;

public final class BootstrapDateTimePickerSpecialNodeListModifier implements IHCSpecialNodeListModifier
{
  public List <? extends IHCNode> modifySpecialNodes (@Nonnull final List <? extends IHCNode> aNodes)
  {
    final List <IHCNode> ret = new ArrayList <IHCNode> ();
    final List <BootstrapDateTimePickerJS> aDTPs = new ArrayList <BootstrapDateTimePickerJS> ();
    int nFirstIndex = -1;
    int nIndex = 0;
    for (final IHCNode aNode : aNodes)
    {
      if (aNode instanceof BootstrapDateTimePickerJS)
      {
        aDTPs.add ((BootstrapDateTimePickerJS) aNode);
        if (nFirstIndex < 0)
          nFirstIndex = nIndex;
      }
      else
        ret.add (aNode);
      nIndex++;
    }

    if (aDTPs.size () <= 1)
    {
      // Nothing to merge
      return aNodes;
    }

    final CollectingJSCodeProvider aMergedJS = new CollectingJSCodeProvider ();
    final List <BootstrapDateTimePickerJS> aRest = ContainerHelper.newList (aDTPs);
    while (!aRest.isEmpty ())
    {
      final BootstrapDateTimePickerJS aCurrent = aRest.remove (0);
      final JSAssocArray aCurrentJSOptions = aCurrent.getDateTimePicker ().getJSOptions ();

      // Find all other datetime pickers with the same options
      final List <BootstrapDateTimePickerJS> aSameOptions = new ArrayList <BootstrapDateTimePickerJS> ();
      final Iterator <BootstrapDateTimePickerJS> itRest = aRest.iterator ();
      while (itRest.hasNext ())
      {
        final BootstrapDateTimePickerJS aCurrentRest = itRest.next ();
        if (aCurrentRest.getDateTimePicker ().getJSOptions ().equals (aCurrentJSOptions))
        {
          aSameOptions.add (aCurrentRest);
          itRest.remove ();
        }
      }

      if (aSameOptions.isEmpty ())
      {
        // No other object has the same options
        aMergedJS.append (aCurrent.getJSCodeProvider ());
      }
      else
      {
        // We have multiple objects with the same options
        // Create a common selector
        IJQuerySelector aJQI = JQuerySelector.id (aCurrent.getDateTimePicker ().getContainerID ());
        for (final BootstrapDateTimePickerJS aSameOption : aSameOptions)
          aJQI = aJQI.multiple (JQuerySelector.id (aSameOption.getDateTimePicker ().getContainerID ()));
        // And apply once
        aMergedJS.append (BootstrapDateTimePicker.invoke (aJQI.invoke (), aCurrentJSOptions));
      }
    }

    // Add at the first index, where it was in the source list
    ret.add (nFirstIndex, new HCScript (aMergedJS));
    return ret;
  }
}
