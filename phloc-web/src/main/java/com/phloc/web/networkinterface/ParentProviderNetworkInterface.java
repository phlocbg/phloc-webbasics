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
package com.phloc.web.networkinterface;

import java.net.NetworkInterface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.parent.IParentProvider;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Default implementation of {@link IParentProvider} for
 * {@link NetworkInterface}.
 * 
 * @author Philip Helger
 */
@Immutable
public class ParentProviderNetworkInterface implements IParentProvider <NetworkInterface>
{
  @Nullable
  public NetworkInterface getParent (@Nonnull final NetworkInterface aCurrent)
  {
    return aCurrent.getParent ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
