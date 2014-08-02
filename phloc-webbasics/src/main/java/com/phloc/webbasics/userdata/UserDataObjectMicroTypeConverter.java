/**
 * Copyright (C) 2012-2014 Philip Helger <ph@phloc.com>
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
package com.phloc.webbasics.userdata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;

public final class UserDataObjectMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_PATH = "path";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final UserDataObject aValue = (UserDataObject) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    aElement.setAttribute (ATTR_PATH, aValue.getPath ());
    return aElement;
  }

  @Nonnull
  public UserDataObject convertToNative (@Nonnull final IMicroElement aElement)
  {
    final String sPath = aElement.getAttribute (ATTR_PATH);
    return new UserDataObject (sPath);
  }
}
