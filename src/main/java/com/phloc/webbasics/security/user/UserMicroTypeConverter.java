/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.security.user;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
  private static final String ELEMENT_LOGINNAME = "loginname";
  private static final String ELEMENT_EMAILADDRESS = "emailaddress";
  private static final String ELEMENT_PASSWORDHASH = "passwordhash";
  private static final String ELEMENT_FIRSTNAME = "firstname";
  private static final String ELEMENT_LASTNAME = "lastname";
  private static final String ELEMENT_CUSTOM = "custom";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IUser aUser = (IUser) aObject;
    final IMicroElement eUser = new MicroElement (sNamespaceURI, sTagName);
    eUser.setAttribute (ATTR_ID, aUser.getID ());
    eUser.appendElement (ELEMENT_LOGINNAME).appendText (aUser.getLoginName ());
    eUser.appendElement (ELEMENT_EMAILADDRESS).appendText (aUser.getEmailAddress ());
    eUser.appendElement (ELEMENT_PASSWORDHASH).appendText (aUser.getPasswordHash ());
    if (aUser.getFirstName () != null)
      eUser.appendElement (ELEMENT_FIRSTNAME).appendText (aUser.getFirstName ());
    if (aUser.getLastName () != null)
      eUser.appendElement (ELEMENT_LASTNAME).appendText (aUser.getLastName ());
    if (aUser.getDesiredLocale () != null)
      eUser.setAttribute (ATTR_DESIREDLOCALE, aUser.getDesiredLocale ().toString ());
    for (final Map.Entry <String, String> aEntry : aUser.getCustomAttrs ().entrySet ())
    {
      final IMicroElement eCustom = eUser.appendElement (ELEMENT_CUSTOM);
      eCustom.setAttribute (ATTR_ID, aEntry.getKey ());
      eCustom.appendText (aEntry.getValue ());
    }
    return eUser;
  }

  @Nonnull
  public User convertToNative (@Nonnull final IMicroElement eUser)
  {
    final String sID = eUser.getAttribute (ATTR_ID);
    final String sLoginName = MicroUtils.getChildTextContent (eUser, ELEMENT_LOGINNAME);
    final String sEmailAddress = MicroUtils.getChildTextContent (eUser, ELEMENT_EMAILADDRESS);
    final String sPasswordHash = MicroUtils.getChildTextContent (eUser, ELEMENT_PASSWORDHASH);
    final String sFirstName = MicroUtils.getChildTextContent (eUser, ELEMENT_FIRSTNAME);
    final String sLastName = MicroUtils.getChildTextContent (eUser, ELEMENT_LASTNAME);
    final String sDesiredLocale = eUser.getAttribute (ATTR_DESIREDLOCALE);
    final Locale aDesiredLocale = sDesiredLocale == null ? null : LocaleCache.get (sDesiredLocale);
    final Map <String, String> aCustomAttrs = new LinkedHashMap <String, String> ();
    for (final IMicroElement eCustom : eUser.getChildElements (ELEMENT_CUSTOM))
      aCustomAttrs.put (eCustom.getAttribute (ATTR_ID), eCustom.getTextContent ());
    return new User (sID, sLoginName, sEmailAddress, sPasswordHash, sFirstName, sLastName, aDesiredLocale, aCustomAttrs);
  }
}
