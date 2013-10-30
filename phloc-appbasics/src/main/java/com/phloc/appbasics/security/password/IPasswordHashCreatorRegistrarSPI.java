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
package com.phloc.appbasics.security.password;

import javax.annotation.Nonnull;

import com.phloc.appbasics.security.password.hash.IPasswordHashCreator;
import com.phloc.appbasics.security.password.hash.PasswordHashCreatorManager;
import com.phloc.commons.annotations.IsSPIInterface;

/**
 * SPI interface to be implemented by other modules wishing to register their
 * own micro-type converters.
 * 
 * @author Philip Helger
 */
@IsSPIInterface
public interface IPasswordHashCreatorRegistrarSPI
{
  /**
   * Register all {@link IPasswordHashCreator} implementations.
   * 
   * @param aRegistry
   *        The registry to register your creators. Never <code>null</code>.
   */
  void registerPasswordHashCreator (@Nonnull PasswordHashCreatorManager aRegistry);
}
