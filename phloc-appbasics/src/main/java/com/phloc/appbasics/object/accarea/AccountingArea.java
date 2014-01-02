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
package com.phloc.appbasics.object.accarea;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.appbasics.object.CObject;
import com.phloc.appbasics.object.StubObject;
import com.phloc.appbasics.object.client.AbstractClientObject;
import com.phloc.appbasics.object.client.IClient;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.ObjectType;
import com.phloc.masterdata.address.Address;
import com.phloc.masterdata.address.IReadonlyAddress;
import com.phloc.masterdata.currency.ECurrency;

/**
 * Default implementation of {@link IAccountingArea}
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class AccountingArea extends AbstractClientObject implements IAccountingArea
{
  private String m_sDisplayName;
  private String m_sCompanyType;
  private String m_sCompanyVATIN;
  private String m_sCompanyNumber;
  private Address m_aAddress;
  private String m_sTelephone;
  private String m_sFax;
  private String m_sEmailAddress;
  private String m_sWebSite;
  private ECurrency m_eDefaultCurrency;

  /**
   * Constructor for new accounting area
   * 
   * @param aClient
   *        Client
   * @param sDisplayName
   *        display name
   * @param sCompanyType
   *        company type
   * @param sCompanyVATIN
   *        company VATIN
   * @param sCompanyNumber
   *        company number
   * @param aAddress
   *        address
   * @param sTelephone
   *        Telephone number
   * @param sFax
   *        Fax number
   * @param sEmailAddress
   *        Email address
   * @param sWebSite
   *        Web site
   * @param eDefaultCurrency
   *        default currency
   * @param aDisplayLocale
   *        The display locale to use. May not be <code>null</code>.
   */
  public AccountingArea (@Nonnull final IClient aClient,
                         @Nonnull @Nonempty final String sDisplayName,
                         @Nonnull @Nonempty final String sCompanyType,
                         @Nonnull @Nonempty final String sCompanyVATIN,
                         @Nonnull @Nonempty final String sCompanyNumber,
                         @Nonnull final IReadonlyAddress aAddress,
                         @Nonnull final String sTelephone,
                         @Nullable final String sFax,
                         @Nullable final String sEmailAddress,
                         @Nullable final String sWebSite,
                         @Nonnull final ECurrency eDefaultCurrency,
                         @Nonnull final Locale aDisplayLocale)
  {
    this (aClient,
          StubObject.createForCurrentUser (),
          sDisplayName,
          sCompanyType,
          sCompanyVATIN,
          sCompanyNumber,
          aAddress,
          sTelephone,
          sFax,
          sEmailAddress,
          sWebSite,
          eDefaultCurrency,
          aDisplayLocale);
  }

  AccountingArea (@Nonnull final IClient aClient,
                  @Nonnull final StubObject aStubObject,
                  @Nonnull @Nonempty final String sDisplayName,
                  @Nonnull @Nonempty final String sCompanyType,
                  @Nonnull @Nonempty final String sCompanyVATIN,
                  @Nonnull @Nonempty final String sCompanyNumber,
                  @Nonnull final IReadonlyAddress aAddress,
                  @Nonnull final String sTelephone,
                  @Nullable final String sFax,
                  @Nullable final String sEmailAddress,
                  @Nullable final String sWebSite,
                  @Nonnull final ECurrency eDefaultCurrency,
                  @Nonnull final Locale aDisplayLocale)
  {
    super (aClient, aStubObject);
    setDisplayName (sDisplayName);
    setCompanyType (sCompanyType);
    setCompanyVATIN (sCompanyVATIN);
    setCompanyNumber (sCompanyNumber);
    setAddress (aAddress, aDisplayLocale);
    setTelephone (sTelephone);
    setFax (sFax);
    setEmailAddress (sEmailAddress);
    setWebSite (sWebSite);
    setDefaultCurrency (eDefaultCurrency);
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return CObject.OT_ACCOUNTINGAREA;
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nonnull
  public EChange setDisplayName (@Nonnull @Nonempty final String sDisplayName)
  {
    if (StringHelper.hasNoText (sDisplayName))
      throw new IllegalArgumentException ("DisplayName");
    if (sDisplayName.equals (m_sDisplayName))
      return EChange.UNCHANGED;
    m_sDisplayName = sDisplayName;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getCompanyType ()
  {
    return m_sCompanyType;
  }

  @Nonnull
  public EChange setCompanyType (@Nonnull @Nonempty final String sCompanyType)
  {
    if (StringHelper.hasNoText (sCompanyType))
      throw new IllegalArgumentException ("CompanyType");
    if (sCompanyType.equals (m_sCompanyType))
      return EChange.UNCHANGED;
    m_sCompanyType = sCompanyType;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getCompanyVATIN ()
  {
    return m_sCompanyVATIN;
  }

  @Nonnull
  public EChange setCompanyVATIN (@Nonnull @Nonempty final String sCompanyVATIN)
  {
    if (StringHelper.hasNoText (sCompanyVATIN))
      throw new IllegalArgumentException ("CompanyVATIN");
    if (sCompanyVATIN.equals (m_sCompanyVATIN))
      return EChange.UNCHANGED;
    m_sCompanyVATIN = sCompanyVATIN;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getCompanyNumber ()
  {
    return m_sCompanyNumber;
  }

  @Nonnull
  public EChange setCompanyNumber (@Nonnull @Nonempty final String sCompanyNumber)
  {
    if (StringHelper.hasNoText (sCompanyNumber))
      throw new IllegalArgumentException ("CompanyNumber");
    if (sCompanyNumber.equals (m_sCompanyNumber))
      return EChange.UNCHANGED;
    m_sCompanyNumber = sCompanyNumber;
    return EChange.CHANGED;
  }

  @Nonnull
  public IReadonlyAddress getAddress ()
  {
    return m_aAddress;
  }

  @Nonnull
  public EChange setAddress (@Nonnull final IReadonlyAddress aAddress, @Nonnull final Locale aDisplayLocale)
  {
    if (aAddress == null)
      throw new NullPointerException ("address");

    final Address aNewAddress = new Address (aAddress, aDisplayLocale);
    if (aNewAddress.equals (m_aAddress))
      return EChange.UNCHANGED;
    m_aAddress = aNewAddress;
    return EChange.CHANGED;
  }

  @Nonnull
  public String getTelephone ()
  {
    return m_sTelephone;
  }

  @Nonnull
  public EChange setTelephone (@Nonnull @Nonempty final String sTelephone)
  {
    if (StringHelper.hasNoText (sTelephone))
      throw new IllegalArgumentException ("Telephone");
    if (sTelephone.equals (m_sTelephone))
      return EChange.UNCHANGED;
    m_sTelephone = sTelephone;
    return EChange.CHANGED;
  }

  @Nullable
  public String getFax ()
  {
    return m_sFax;
  }

  @Nonnull
  public EChange setFax (@Nullable final String sFax)
  {
    if (EqualsUtils.equals (sFax, m_sFax))
      return EChange.UNCHANGED;
    m_sFax = sFax;
    return EChange.CHANGED;
  }

  @Nullable
  public String getEmailAddress ()
  {
    return m_sEmailAddress;
  }

  @Nonnull
  public EChange setEmailAddress (@Nullable final String sEmailAddress)
  {
    if (EqualsUtils.equals (sEmailAddress, m_sEmailAddress))
      return EChange.UNCHANGED;
    m_sEmailAddress = sEmailAddress;
    return EChange.CHANGED;
  }

  @Nullable
  public String getWebSite ()
  {
    return m_sWebSite;
  }

  @Nonnull
  public EChange setWebSite (@Nullable final String sWebSite)
  {
    if (EqualsUtils.equals (sWebSite, m_sWebSite))
      return EChange.UNCHANGED;
    m_sWebSite = sWebSite;
    return EChange.CHANGED;
  }

  @Nonnull
  public ECurrency getDefaultCurrency ()
  {
    return m_eDefaultCurrency;
  }

  @Nonnull
  @Nonempty
  public String getDefaultCurrencyID ()
  {
    return m_eDefaultCurrency.getID ();
  }

  @Nonnull
  public EChange setDefaultCurrency (@Nonnull final ECurrency eDefaultCurrency)
  {
    if (eDefaultCurrency == null)
      throw new NullPointerException ("defaultCurrency");

    if (eDefaultCurrency.equals (m_eDefaultCurrency))
      return EChange.UNCHANGED;
    m_eDefaultCurrency = eDefaultCurrency;
    return EChange.CHANGED;
  }

  @Nonnull
  @Nonempty
  public String getAsUIText ()
  {
    return getClient ().getAsUIText () + " - " + getDisplayName ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("displayName", m_sDisplayName)
                            .append ("companyType", m_sCompanyType)
                            .append ("companyVATIN", m_sCompanyVATIN)
                            .append ("companyNumber", m_sCompanyNumber)
                            .append ("address", m_aAddress)
                            .appendIfNotNull ("telephone", m_sTelephone)
                            .appendIfNotNull ("fax", m_sFax)
                            .appendIfNotNull ("emailAddress", m_sEmailAddress)
                            .appendIfNotNull ("website", m_sWebSite)
                            .appendIfNotNull ("defaultCurrency", m_eDefaultCurrency)
                            .toString ();
  }
}
