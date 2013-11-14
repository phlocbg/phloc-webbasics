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
package com.phloc.webctrls.typeahead;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.json2.IJsonProvider;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;

/**
 * Represents a single typeahead datum (= data record) with the minimum data
 * elements {@link #JSON_VALUE} and {@link #JSON_TOKENS}.
 * 
 * @author Philip Helger
 */
@Immutable
public class TypeaheadDatum implements IJsonProvider
{
  public static final String JSON_VALUE = "value";
  public static final String JSON_TOKENS = "tokens";

  private final String m_sValue;
  private final List <String> m_aTokens;

  public TypeaheadDatum (@Nonnull final String sValue, @Nonnull final String... aTokens)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    if (ArrayHelper.isEmpty (aTokens))
      throw new IllegalArgumentException ("tokens");
    m_sValue = sValue;
    m_aTokens = ContainerHelper.newList (aTokens);
  }

  public TypeaheadDatum (@Nonnull final String sValue, @Nonnull final Collection <String> aTokens)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    if (ContainerHelper.isEmpty (aTokens))
      throw new IllegalArgumentException ("tokens");
    m_sValue = sValue;
    m_aTokens = ContainerHelper.newList (aTokens);
  }

  @Nonnull
  public final String getValue ()
  {
    return m_sValue;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllTokens ()
  {
    return ContainerHelper.newList (m_aTokens);
  }

  @Nonnull
  @ReturnsMutableCopy
  public JsonObject getAsJson ()
  {
    return new JsonObject ().add (JSON_VALUE, m_sValue).add (JSON_TOKENS, new JsonArray ().addAll (m_aTokens));
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getAsJSObject ()
  {
    return new JSAssocArray ().add (JSON_VALUE, m_sValue).add (JSON_TOKENS, new JSArray ().addAll (m_aTokens));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_sValue).append ("tokens", m_aTokens).toString ();
  }
}
