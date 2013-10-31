/**
 * Copyright (C) 2006-2013 phloc systems
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.misc.IHasUIText;
import com.phloc.appbasics.object.client.IClientObject;
import com.phloc.commons.name.IHasDisplayName;
import com.phloc.masterdata.address.IReadonlyAddress;
import com.phloc.masterdata.currency.ECurrency;

/**
 * This interface represents a single accounting area.
 * 
 * @author Philip Helger
 */
public interface IAccountingArea extends IClientObject, IHasDisplayName, IHasUIText
{
  /**
   * @return The company type. E.g. "KEG"
   */
  @Nonnull
  String getCompanyType ();

  /**
   * @return The company UID.
   */
  @Nonnull
  String getCompanyVATIN ();

  /**
   * @return The company number (Interne Betriebsnummer). May not be
   *         <code>null</code>.
   */
  @Nonnull
  String getCompanyNumber ();

  /**
   * @return The address of the owner.
   */
  @Nonnull
  IReadonlyAddress getAddress ();

  /**
   * @return The telephone number. May be <code>null</code>.
   */
  @Nonnull
  String getTelephone ();

  /**
   * @return The telefax number. May be <code>null</code>.
   */
  @Nullable
  String getFax ();

  /**
   * @return The email address. May be <code>null</code>.
   */
  @Nullable
  String getEmailAddress ();

  /**
   * @return The web site. May be <code>null</code>.
   */
  @Nullable
  String getWebSite ();

  /**
   * @return The default currency in this client. May not be <code>null</code>.
   */
  @Nonnull
  ECurrency getDefaultCurrency ();
}
