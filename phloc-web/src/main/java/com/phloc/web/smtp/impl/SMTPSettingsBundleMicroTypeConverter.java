package com.phloc.web.smtp.impl;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.convert.MicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.web.smtp.ISMTPSettings;
import com.phloc.web.smtp.ISMTPSettingsBundle;

public class SMTPSettingsBundleMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ELEMENT_SMTP = "smtp"; //$NON-NLS-1$

  @Override
  public IMicroElement convertToMicroElement (final Object aObject, final String sNamespaceURI, final String sTagName)
  {
    final ISMTPSettingsBundle aSMTPSettingsBundle = (ISMTPSettingsBundle) aObject;
    final IMicroElement eSMTPSettingsBundle = new MicroElement (sNamespaceURI, sTagName);
    for (final ISMTPSettings aSMTP : aSMTPSettingsBundle.getAll ())
    {
      final IMicroElement eSMTP = MicroTypeConverter.convertToMicroElement (aSMTP, ELEMENT_SMTP);
      eSMTPSettingsBundle.appendChild (eSMTP);
    }
    return eSMTPSettingsBundle;
  }

  @Override
  public SMTPSettingsBundle convertToNative (final IMicroElement eElement)
  {
    final SMTPSettingsBundle aBundle = new SMTPSettingsBundle ();
    for (final IMicroElement eSMTP : eElement.getAllChildElements (ELEMENT_SMTP))
    {
      final SMTPSettings aSMTP = MicroTypeConverter.convertToNative (eSMTP, SMTPSettings.class);
      aBundle.add (aSMTP);
    }
    return aBundle;
  }
}
