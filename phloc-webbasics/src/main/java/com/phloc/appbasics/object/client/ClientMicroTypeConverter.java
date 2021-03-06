/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.appbasics.object.client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.object.AbstractObjectMicroTypeConverter;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroElement;

public final class ClientMicroTypeConverter extends AbstractObjectMicroTypeConverter
{
  private static final String ATTR_DISPLAYNAME = "displayname";

  @Nonnull
  public IMicroElement convertToMicroElement (@Nonnull final Object aObject,
                                              @Nullable final String sNamespaceURI,
                                              @Nonnull @Nonempty final String sTagName)
  {
    final IClient aValue = (IClient) aObject;
    final IMicroElement aElement = new MicroElement (sNamespaceURI, sTagName);
    setObjectFields (aValue, aElement);
    aElement.setAttribute (ATTR_DISPLAYNAME, aValue.getDisplayName ());
    return aElement;
  }

  @Nonnull
  public Client convertToNative (@Nonnull final IMicroElement aElement)
  {
    final String sDisplayName = aElement.getAttribute (ATTR_DISPLAYNAME);
    return new Client (getStubObject (aElement), sDisplayName);
  }
}
