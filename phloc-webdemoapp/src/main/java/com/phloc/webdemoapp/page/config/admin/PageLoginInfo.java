/**
 * Copyright (C) 2012-2013 Philip Helger <ph@phloc.com>
 * All Rights Reserved
 *
 * This file is part of the Ecoware Online Shop.
 *
 * Proprietary and confidential.
 *
 * It can not be copied and/or distributed without the
 * express permission of Philip Helger.
 *
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 */
package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.security.BasePageLoginInfo;

public class PageLoginInfo extends BasePageLoginInfo
{
  public PageLoginInfo (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Logged in users");
  }
}
