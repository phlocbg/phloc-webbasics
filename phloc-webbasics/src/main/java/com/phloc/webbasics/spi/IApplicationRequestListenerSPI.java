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
package com.phloc.webbasics.spi;

import java.io.Serializable;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.IsSPIInterface;
import com.phloc.webbasics.action.servlet.AbstractApplicationActionServlet;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * SPI interface that is invoked in {@link AbstractApplicationActionServlet}.
 * 
 * @author Philip Helger
 */
@IsSPIInterface
public interface IApplicationRequestListenerSPI extends Serializable
{
  /**
   * Called at the very beginning of a request.
   * 
   * @param aRequestScope
   *        The scope of the current request.
   */
  void onRequestBegin (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  /**
   * Called at the end of a request.
   * 
   * @param bExceptionOccurred
   *        <code>true</code> if an exception occurred, <code>false</code> if
   *        not.
   */
  void onRequestEnd (boolean bExceptionOccurred);
}
