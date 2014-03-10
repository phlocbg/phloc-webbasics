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
    final List <IHCNode> aNonDTPs = new ArrayList <IHCNode> ();
    final List <BootstrapDateTimePickerJS> aDTPs = new ArrayList <BootstrapDateTimePickerJS> ();
    for (final IHCNode aNode : aNodes)
      if (aNode instanceof BootstrapDateTimePickerJS)
        aDTPs.add ((BootstrapDateTimePickerJS) aNode);
      else
        aNonDTPs.add (aNode);

    if (aDTPs.size () <= 1)
    {
      // Nothing to merge
      return aNodes;
    }

    final CollectingJSCodeProvider ret = new CollectingJSCodeProvider ();
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
        ret.append (aCurrent.getJSCodeProvider ());
      }
      else
      {
        // We have multiple objects with the same options
        // Create a common selector
        IJQuerySelector aJQI = JQuerySelector.id (aCurrent.getDateTimePicker ().getContainerID ());
        for (final BootstrapDateTimePickerJS aSameOption : aSameOptions)
          aJQI = aJQI.multiple (JQuerySelector.id (aSameOption.getDateTimePicker ().getContainerID ()));
        // And apply once
        ret.append (BootstrapDateTimePicker.invoke (aJQI.invoke (), aCurrentJSOptions));
      }
    }
    aNonDTPs.add (new HCScript (ret));
    return aNonDTPs;
  }
}
