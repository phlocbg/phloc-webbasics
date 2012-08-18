/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.appbasics.app.dao;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.state.EChange;

public interface IXMLDAODataProvider
{
  /**
   * Prepare the data structure for an initial save. This is only called if no
   * file was found to be read from.
   * 
   * @return {@link EChange#CHANGED} if reading the document changed the content
   *         and so a call to the save action should be performed. Never
   *         <code>null</code>.
   * @throws Exception
   *         Any error that may occur on initialization.
   */
  @Nonnull
  EChange initForFirstTimeUsage () throws Exception;

  /**
   * This interface needs to be overridden by all classes implementing this
   * class. This method should convert an XML DOM Document into internal data
   * structures.
   * 
   * @param aDoc
   *        The XML document to scan. May be <code>null</code> if parsing
   *        failed.
   * @return {@link EChange#CHANGED} if reading the document changed the content
   *         and so a call to the save action should be performed. Never
   *         <code>null</code>.
   */
  @Nonnull
  EChange readXML (@Nullable IMicroDocument aDoc);

  /**
   * This is the method to be overloaded by sub classes.
   * 
   * @param aDoc
   *        the document object to be filled. Never <code>null</code>.
   */
  void fillXMLDocument (@Nonnull IMicroDocument aDoc);
}
