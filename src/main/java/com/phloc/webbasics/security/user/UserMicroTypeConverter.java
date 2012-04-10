package com.phloc.webbasics.security.user;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;

public final class UserMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_DESIREDLOCALE = "desiredlocale";
  private static final String ELEMENT_EMAILADDRESS = "emailaddress";
  private static final String ELEMENT_PASSWORDHASH = "passwordhash";
  private static final String ELEMENT_FIRSTNAME = "firstname";
  private static final String ELEMENT_LASTNAME = "lastname";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IUser aUser = (IUser) aObject;
    final IMicroElement eUser = new MicroElement (sNamespaceURI, sTagName);
    eUser.setAttribute (ATTR_ID, aUser.getID ());
    eUser.appendElement (ELEMENT_EMAILADDRESS).appendText (aUser.getEmailAddress ());
    eUser.appendElement (ELEMENT_PASSWORDHASH).appendText (aUser.getPasswordHash ());
    if (aUser.getFirstName () != null)
      eUser.appendElement (ELEMENT_FIRSTNAME).appendText (aUser.getFirstName ());
    if (aUser.getLastName () != null)
      eUser.appendElement (ELEMENT_LASTNAME).appendText (aUser.getLastName ());
    if (aUser.getDesiredLocale () != null)
      eUser.setAttribute (ATTR_DESIREDLOCALE, aUser.getDesiredLocale ().toString ());
    return eUser;
  }

  @Nonnull
  public IUser convertToNative (@Nonnull final IMicroElement eUser)
  {
    final String sID = eUser.getAttribute (ATTR_ID);
    final String sEmailAddress = MicroUtils.getChildTextContent (eUser, ELEMENT_EMAILADDRESS);
    final String sPasswordHash = MicroUtils.getChildTextContent (eUser, ELEMENT_PASSWORDHASH);
    final String sFirstName = MicroUtils.getChildTextContent (eUser, ELEMENT_FIRSTNAME);
    final String sLastName = MicroUtils.getChildTextContent (eUser, ELEMENT_LASTNAME);
    final String sDesiredLocale = eUser.getAttribute (ATTR_DESIREDLOCALE);
    final Locale aDesiredLocale = sDesiredLocale == null ? null : LocaleCache.get (sDesiredLocale);
    return new User (sID, sEmailAddress, sPasswordHash, sFirstName, sLastName, aDesiredLocale);
  }
}
