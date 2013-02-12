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
package com.phloc.appbasics.app.dao;

import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.state.ESuccess;

/**
 * The DAO file IO API
 * 
 * @author philip
 */
public interface IDAOIO
{
  @Nullable
  InputStream openInputStream (@Nullable String sFilename);

  @Nonnull
  IReadableResource getReadableResource (@Nonnull String sFilename);

  void renameFile (@Nonnull String sSrcFileName, @Nonnull String sDstFileName);

  @Nonnull
  ESuccess saveFile (@Nonnull String sFilename, @Nonnull String sContent, @Nonnull Charset aCharset);
}
