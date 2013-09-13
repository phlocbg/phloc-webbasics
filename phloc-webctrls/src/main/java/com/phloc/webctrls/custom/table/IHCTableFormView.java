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
package com.phloc.webctrls.custom.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.html.hc.IHCBaseTable;
import com.phloc.html.hc.IHCNode;
import com.phloc.webctrls.custom.IFormLabel;

public interface IHCTableFormView <THISTYPE extends IHCTableFormView <THISTYPE>> extends IHCBaseTable <THISTYPE>
{
  void addItemRow (@Nullable IFormLabel aLabel, @Nullable String sValue);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable String... aValues);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode aValue);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable IHCNode... aValues);

  void addItemRow (@Nullable IFormLabel aLabel, @Nullable Iterable <? extends IHCNode> aValues);

  void addItemRow (@Nonnull String sLabel, @Nullable String sValue);

  void addItemRow (@Nonnull String sLabel, @Nullable String... aValue);

  void addItemRow (@Nonnull String sLabel, @Nullable IHCNode aValue);

  void addItemRow (@Nonnull String sLabel, @Nullable IHCNode... aValues);

  void addItemRow (@Nonnull String sLabel, @Nullable Iterable <? extends IHCNode> aValues);
}
