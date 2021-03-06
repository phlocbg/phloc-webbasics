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
package com.phloc.appbasics.security.user;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.phloc.appbasics.security.password.hash.PasswordHash;
import com.phloc.appbasics.security.password.hash.PasswordHashCreatorDefault;
import com.phloc.commons.annotations.ContainsSoftMigration;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;
import com.phloc.commons.microdom.utils.MicroUtils;
import com.phloc.commons.string.StringParser;
import com.phloc.datetime.PDTFactory;

public final class UserMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_CREATIONDT = "creationdt";
  private static final String ATTR_LASTMODDT = "lastmoddt";
  private static final String ATTR_DELETIONDT = "deletiondt";
  private static final String ATTR_DESIREDLOCALE = "desiredlocale";
  private static final String ATTR_LASTLOGINDT = "lastlogindt";
  private static final String ATTR_LOGINCOUNT = "logincount";
  private static final String ATTR_CONSECUTIVEFAILEDLOGINCOUNT = "consecutivefailedlogincount";
  private static final String ELEMENT_LOGINNAME = "loginname";
  private static final String ELEMENT_EMAILADDRESS = "emailaddress";
  private static final String ELEMENT_PASSWORDHASH = "passwordhash";
  private static final String ATTR_ALGORITHM = "algorithm";
  private static final String ELEMENT_FIRSTNAME = "firstname";
  private static final String ELEMENT_LASTNAME = "lastname";
  private static final String ELEMENT_CUSTOM = "custom";
  private static final String ATTR_DELETED = "deleted";
  private static final String ATTR_DISABLED = "disabled";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IUser aUser = (IUser) aObject;
    final IMicroElement eUser = new MicroElement (sNamespaceURI, sTagName);
    eUser.setAttribute (ATTR_ID, aUser.getID ());
    eUser.setAttributeWithConversion (ATTR_CREATIONDT, aUser.getCreationDateTime ());
    eUser.setAttributeWithConversion (ATTR_LASTMODDT, aUser.getLastModificationDateTime ());
    eUser.setAttributeWithConversion (ATTR_DELETIONDT, aUser.getDeletionDateTime ());
    eUser.appendElement (ELEMENT_LOGINNAME).appendText (aUser.getLoginName ());
    if (aUser.getEmailAddress () != null)
      eUser.appendElement (ELEMENT_EMAILADDRESS).appendText (aUser.getEmailAddress ());
    final IMicroElement ePasswordHash = eUser.appendElement (ELEMENT_PASSWORDHASH);
    ePasswordHash.setAttribute (ATTR_ALGORITHM, aUser.getPasswordHash ().getAlgorithmName ());
    ePasswordHash.appendText (aUser.getPasswordHash ().getPasswordHashValue ());
    if (aUser.getFirstName () != null)
      eUser.appendElement (ELEMENT_FIRSTNAME).appendText (aUser.getFirstName ());
    if (aUser.getLastName () != null)
      eUser.appendElement (ELEMENT_LASTNAME).appendText (aUser.getLastName ());
    if (aUser.getDesiredLocale () != null)
      eUser.setAttribute (ATTR_DESIREDLOCALE, aUser.getDesiredLocale ().toString ());
    if (aUser.getLastLoginDateTime () != null)
      eUser.setAttributeWithConversion (ATTR_LASTLOGINDT, aUser.getLastLoginDateTime ());
    eUser.setAttribute (ATTR_LOGINCOUNT, aUser.getLoginCount ());
    eUser.setAttribute (ATTR_CONSECUTIVEFAILEDLOGINCOUNT, aUser.getConsecutiveFailedLoginCount ());
    for (final Map.Entry <String, Object> aEntry : ContainerHelper.getSortedByKey (aUser.getAllAttributes ())
                                                                  .entrySet ())
    {
      final IMicroElement eCustom = eUser.appendElement (ELEMENT_CUSTOM);
      eCustom.setAttribute (ATTR_ID, aEntry.getKey ());
      eCustom.appendText (String.valueOf (aEntry.getValue ()));
    }
    eUser.setAttribute (ATTR_DELETED, Boolean.toString (aUser.isDeleted ()));
    eUser.setAttribute (ATTR_DISABLED, Boolean.toString (aUser.isDisabled ()));
    return eUser;
  }

  @ContainsSoftMigration
  @Nonnull
  public User convertToNative (@Nonnull final IMicroElement eUser)
  {
    final String sID = eUser.getAttribute (ATTR_ID);
    DateTime aCreationDT = eUser.getAttributeWithConversion (ATTR_CREATIONDT, DateTime.class);
    if (aCreationDT == null)
    {
      // Migration for old data
      aCreationDT = PDTFactory.getCurrentDateTime ();
    }
    final DateTime aLastModificationDT = eUser.getAttributeWithConversion (ATTR_LASTMODDT, DateTime.class);
    final DateTime aDeletionDT = eUser.getAttributeWithConversion (ATTR_DELETIONDT, DateTime.class);
    final String sLoginName = MicroUtils.getChildTextContent (eUser, ELEMENT_LOGINNAME);
    final String sEmailAddress = MicroUtils.getChildTextContent (eUser, ELEMENT_EMAILADDRESS);
    final IMicroElement ePasswordHash = eUser.getFirstChildElement (ELEMENT_PASSWORDHASH);
    String sPasswordHashAlgorithm = ePasswordHash.getAttribute (ATTR_ALGORITHM);
    if (sPasswordHashAlgorithm == null)
    {
      // migration
      sPasswordHashAlgorithm = PasswordHashCreatorDefault.ALGORITHM;
    }
    final String sPasswordHashValue = ePasswordHash.getTextContent ();
    final PasswordHash aPasswordHash = new PasswordHash (sPasswordHashAlgorithm, sPasswordHashValue);
    final String sFirstName = MicroUtils.getChildTextContent (eUser, ELEMENT_FIRSTNAME);
    final String sLastName = MicroUtils.getChildTextContent (eUser, ELEMENT_LASTNAME);
    final String sDesiredLocale = eUser.getAttribute (ATTR_DESIREDLOCALE);
    final Locale aDesiredLocale = sDesiredLocale == null ? null : LocaleCache.getLocale (sDesiredLocale);
    final DateTime aLastLoginDT = eUser.getAttributeWithConversion (ATTR_LASTLOGINDT, DateTime.class);
    final int nLoginCount = StringParser.parseInt (eUser.getAttribute (ATTR_LOGINCOUNT), 0);
    final int nConsecutiveFailedLoginCount = StringParser.parseInt (eUser.getAttribute (ATTR_CONSECUTIVEFAILEDLOGINCOUNT),
                                                                    0);
    final Map <String, String> aCustomAttrs = new LinkedHashMap <String, String> ();
    for (final IMicroElement eCustom : eUser.getAllChildElements (ELEMENT_CUSTOM))
      aCustomAttrs.put (eCustom.getAttribute (ATTR_ID), eCustom.getTextContent ());
    final String sDeleted = eUser.getAttribute (ATTR_DELETED);
    final boolean bDeleted = StringParser.parseBool (sDeleted);
    final String sDisabled = eUser.getAttribute (ATTR_DISABLED);
    final boolean bDisabled = StringParser.parseBool (sDisabled);

    return new User (sID,
                     aCreationDT,
                     aLastModificationDT,
                     aDeletionDT,
                     sLoginName,
                     sEmailAddress,
                     aPasswordHash,
                     sFirstName,
                     sLastName,
                     aDesiredLocale,
                     aLastLoginDT,
                     nLoginCount,
                     nConsecutiveFailedLoginCount,
                     aCustomAttrs,
                     bDeleted,
                     bDisabled);
  }
}
