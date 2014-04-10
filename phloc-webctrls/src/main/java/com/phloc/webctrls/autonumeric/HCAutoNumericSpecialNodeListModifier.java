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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.utils.IHCSpecialNodeListModifier;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.html.js.provider.CollectingJSCodeProvider;

public final class HCAutoNumericSpecialNodeListModifier implements IHCSpecialNodeListModifier
{
  public List <? extends IHCNode> modifySpecialNodes (@Nonnull final List <? extends IHCNode> aNodes)
  {
    final List <IHCNode> ret = new ArrayList <IHCNode> ();
    final List <HCAutoNumericJS> aDTPs = new ArrayList <HCAutoNumericJS> ();
    int nFirstIndex = -1;
    int nIndex = 0;
    for (final IHCNode aNode : aNodes)
    {
      if (aNode instanceof HCAutoNumericJS)
      {
        aDTPs.add ((HCAutoNumericJS) aNode);
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
    final List <HCAutoNumericJS> aRest = ContainerHelper.newList (aDTPs);
    while (!aRest.isEmpty ())
    {
      final HCAutoNumericJS aCurrent = aRest.remove (0);
      final JSAssocArray aCurrentJSOptions = aCurrent.getAutoNumeric ().getJSOptions ();

      // Find all other auto numerics with the same options
      final List <HCAutoNumericJS> aSameOptions = new ArrayList <HCAutoNumericJS> ();
      final Iterator <HCAutoNumericJS> itRest = aRest.iterator ();
      while (itRest.hasNext ())
      {
        final HCAutoNumericJS aCurrentRest = itRest.next ();
        if (aCurrentRest.getAutoNumeric ().getJSOptions ().equals (aCurrentJSOptions))
        {
          aSameOptions.add (aCurrentRest);
          itRest.remove ();
        }
      }

      if (aSameOptions.isEmpty ())
      {
        // No other object has the same options
        aMergedJS.append (aCurrent.getOnDocumentReadyCode ());
      }
      else
      {
        // We have multiple objects with the same options
        // Create a common selector
        IJQuerySelector aJQI = JQuerySelector.id (aCurrent.getAutoNumeric ().getID ());
        for (final HCAutoNumericJS aSameOption : aSameOptions)
          aJQI = aJQI.multiple (JQuerySelector.id (aSameOption.getAutoNumeric ().getID ()));
        // And apply once
        aMergedJS.append (HCAutoNumericJS.createInitCode (aJQI.invoke (), aCurrent.getAutoNumeric ()));
      }
    }

    // Add at the first index, where it was in the source list
    ret.add (nFirstIndex, new HCScriptOnDocumentReady (aMergedJS));
    return ret;
  }
}
