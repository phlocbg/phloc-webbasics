package com.phloc.bootstrap3.datetimepicker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.annotations.OutOfBandNode;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.IJSCodeProvider;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.IJQuerySelector;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.html.js.provider.CollectingJSCodeProvider;

/**
 * A special script that initializes the {@link BootstrapDateTimePicker}. It is
 * a separate class, so that potentially identical options can be merged to a
 * single invocation.
 * 
 * @author Philip Helger
 */
@OutOfBandNode
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

  @Nonnull
  public static IJSCodeProvider getMergedInitialization (@Nonnull final List <BootstrapDateTimePickerJS> aList)
  {
    if (ContainerHelper.isEmpty (aList))
      throw new IllegalArgumentException ("List may not be empty");

    if (aList.size () == 1)
    {
      // Nothing to merge
      return ContainerHelper.getFirstElement (aList).getJSCodeProvider ();
    }

    final CollectingJSCodeProvider ret = new CollectingJSCodeProvider ();
    final List <BootstrapDateTimePickerJS> aRest = ContainerHelper.newList (aList);
    while (!aRest.isEmpty ())
    {
      final BootstrapDateTimePickerJS aCurrent = aRest.remove (0);
      final JSAssocArray aCurrentJSOptions = aCurrent.getDateTimePicker ().getJSOptions ();

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
        ret.append (BootstrapDateTimePicker.invoke (aJQI.invoke ()).arg (aCurrentJSOptions));
      }
    }
    return ret;
  }
}
