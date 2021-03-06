/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webctrls.typeaheadten;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.js.builder.IJSExpression;

/**
 * Represents a single typeahead dataset.
 * 
 * @author Philip Helger
 */
@Immutable
public class TypeaheadDataset
{
  public static final String JSON_SOURCE = "source";
  public static final String JSON_NAME = "name";
  public static final String JSON_DISPLAY_KEY = "displayKey";
  public static final String JSON_TEMPLATES = "templates";
  public static final String JSON_TEMPLATES_EMPTY = "empty";
  public static final String JSON_TEMPLATES_FOOTER = "footer";
  public static final String JSON_TEMPLATES_HEADER = "header";
  public static final String JSON_TEMPLATES_SUGGESTION = "suggestion";

  private IJSExpression m_aSource;
  private String m_sName;
  private String m_sDisplayKey;
  private IJSExpression m_aEmpty;
  private IJSExpression m_aFooter;
  private IJSExpression m_aHeader;
  private IJSExpression m_aSuggestion;

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("source", m_aSource)
                                       .appendIfNotNull ("name", m_sName)
                                       .appendIfNotNull ("displayKey", m_sDisplayKey)
                                       .appendIfNotNull ("empty", m_aEmpty)
                                       .appendIfNotNull ("footer", m_aFooter)
                                       .appendIfNotNull ("header", m_aHeader)
                                       .appendIfNotNull ("suggestion", m_aSuggestion)
                                       .toString ();
  }
}
