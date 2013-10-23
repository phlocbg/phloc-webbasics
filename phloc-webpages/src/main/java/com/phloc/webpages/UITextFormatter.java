package com.phloc.webpages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.hc.impl.HCTextNode;

public final class UITextFormatter
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UITextFormatter.class);

  private UITextFormatter ()
  {}

  @Nonnull
  public static IHCNode getToStringContent (final Object aValue)
  {
    final String sOrigValue = String.valueOf (aValue);
    if (StringHelper.startsWith (sOrigValue, '[') && StringHelper.endsWith (sOrigValue, ']'))
    {
      try
      {
        final List <String> aParts = new ArrayList <String> ();
        String sValue = sOrigValue.substring (1, sOrigValue.length () - 1);

        final String [] aObjStart = RegExHelper.getAllMatchingGroupValues ("([\\[]*)([A-Za-z0-9_$]+@0x[0-9a-fA-F]{8})(?:: (.+))?",
                                                                           sValue);
        aParts.add (aObjStart[1]);
        if (aObjStart[2] != null)
        {
          sValue = StringHelper.getConcatenatedOnDemand (aObjStart[0], aObjStart[2]).trim ();
          if (sValue.length () > 0)
          {
            sValue = StringHelper.replaceAll (sValue, "; ", ";\n");
            aParts.addAll (StringHelper.getExploded ('\n', sValue));
          }
        }

        final HCNodeList ret = new HCNodeList ();
        for (final String s : aParts)
          ret.addChild (HCDiv.create (s));
        return ret;
      }
      catch (final Exception ex)
      {
        s_aLogger.error ("Failed to format", ex);
      }
    }
    return new HCTextNode (sOrigValue);
  }
}
