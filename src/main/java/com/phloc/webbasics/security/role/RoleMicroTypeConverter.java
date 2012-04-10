package com.phloc.webbasics.security.role;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.convert.IMicroTypeConverter;
import com.phloc.commons.microdom.impl.MicroElement;

public final class RoleMicroTypeConverter implements IMicroTypeConverter
{
  private static final String ATTR_ID = "id";
  private static final String ATTR_NAME = "name";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull final String sTagName)
  {
    final IRole aRole = (IRole) aObject;
    final IMicroElement eRole = new MicroElement (sNamespaceURI, sTagName);
    eRole.setAttribute (ATTR_ID, aRole.getID ());
    eRole.setAttribute (ATTR_NAME, aRole.getName ());
    return eRole;
  }

  @Nonnull
  public IRole convertToNative (@Nonnull final IMicroElement eRole)
  {
    final String sID = eRole.getAttribute (ATTR_ID);
    final String sName = eRole.getAttribute (ATTR_NAME);
    return new Role (sID, sName);
  }
}
