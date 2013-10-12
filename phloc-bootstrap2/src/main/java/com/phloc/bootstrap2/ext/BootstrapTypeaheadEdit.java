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
package com.phloc.bootstrap2.ext;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.bootstrap2.EBootstrapText;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCHiddenField;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.html.js.builder.jquery.JQuerySelector;
import com.phloc.webbasics.form.RequestField;

public class BootstrapTypeaheadEdit implements IHCNodeBuilder
{
  private final HCEdit m_aEdit;
  private final RequestField m_aRFHidden;
  private final String m_sHiddenFieldID;
  private final JSAnonymousFunction m_aSelectionCallback;
  private final BootstrapPhlocTypeaheadScript m_aScript;

  public BootstrapTypeaheadEdit (@Nonnull final RequestField aRFEdit,
                                 @Nonnull final RequestField aRFHidden,
                                 @Nonnull final ISimpleURL aAjaxInvocationURL,
                                 @Nonnull final Locale aDisplayLocale)
  {
    if (aRFEdit == null)
      throw new NullPointerException ("RequestFieldEdit");
    if (aRFHidden == null)
      throw new NullPointerException ("RequestFieldHidden");
    if (aAjaxInvocationURL == null)
      throw new NullPointerException ("AjaxInvocationURL");

    m_aEdit = new HCEdit (aRFEdit).setDisableAutoComplete (true)
                                  .setPlaceholder (EBootstrapText.ENTER_SEARCH_STRING.getDisplayText (aDisplayLocale));

    m_aRFHidden = aRFHidden;
    m_sHiddenFieldID = GlobalIDFactory.getNewStringID ();

    m_aSelectionCallback = new JSAnonymousFunction ();
    final JSVar aJSID = m_aSelectionCallback.param ("id");
    // Need to manually call the "change" handler, because otherwise onchange
    // event is not triggered for hidden fields!
    m_aSelectionCallback.body ().add (JQuery.idRef (m_sHiddenFieldID).val (aJSID).change ());
    m_aScript = new BootstrapPhlocTypeaheadScript (JQuerySelector.id (m_aEdit),
                                                   m_aSelectionCallback,
                                                   aAjaxInvocationURL);
  }

  /**
   * @return The contained edit field that will hold the selected name. Never
   *         <code>null</code>. Changes on the edit only have an effect if they
   *         are performed before this control is build!
   */
  @Nonnull
  public HCEdit getEdit ()
  {
    return m_aEdit;
  }

  /**
   * @return The ID of the hidden field that is generated for this edit. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getHiddenFieldID ()
  {
    return m_sHiddenFieldID;
  }

  /**
   * @return The JS callback function that is invoked, when an item is selected.
   *         Never <code>null</code>. Do not modify the original body content,
   *         as this is required to be present for the correct working of this
   *         control!
   */
  @Nonnull
  public JSAnonymousFunction getJSSelectionCallback ()
  {
    return m_aSelectionCallback;
  }

  /**
   * @return The type ahead script that handles the AJAX trigger based on the
   *         input. Never <code>null</code>. Changes on the script only have an
   *         effect if they are performed before this control is build!
   */
  @Nonnull
  public BootstrapPhlocTypeaheadScript getScript ()
  {
    return m_aScript;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCNodeList ret = new HCNodeList ();

    // Edit
    ret.addChild (m_aEdit);

    // HiddenField
    ret.addChild (new HCHiddenField (m_aRFHidden).setID (m_sHiddenFieldID));

    // JS code
    ret.addChild (m_aScript);

    return ret;
  }
}
