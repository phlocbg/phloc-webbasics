package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
  private final ISimpleURL m_aAjaxInvocationURL;

  public BootstrapTypeaheadEdit (@Nonnull final RequestField aRFEdit,
                                 @Nonnull final RequestField aRFHidden,
                                 @Nonnull final ISimpleURL aAjaxInvocationURL)
  {
    if (aRFEdit == null)
      throw new NullPointerException ("RequestFieldEdit");
    if (aRFHidden == null)
      throw new NullPointerException ("RequestFieldHidden");
    if (aAjaxInvocationURL == null)
      throw new NullPointerException ("AjaxInvocationURL");

    m_aEdit = new HCEdit (aRFEdit).setDisableAutoComplete (true).setID (GlobalIDFactory.getNewStringID ());
    m_aRFHidden = aRFHidden;
    m_aAjaxInvocationURL = aAjaxInvocationURL;
  }

  @Nullable
  public IHCNode build ()
  {
    final HCNodeList ret = new HCNodeList ();

    // Edit
    ret.addChild (m_aEdit);

    // HiddenField
    final String sHiddenFieldID = GlobalIDFactory.getNewStringID ();
    ret.addChild (new HCHiddenField (m_aRFHidden).setID (sHiddenFieldID));

    // JS code
    final JSAnonymousFunction aIDCallback = new JSAnonymousFunction ();
    final JSVar aID = aIDCallback.param ("id");
    aIDCallback.body ().add (JQuery.idRef (sHiddenFieldID).val ().arg (aID));
    ret.addChild (new BootstrapPhlocTypeahead (JQuerySelector.id (m_aEdit.getID ()), aIDCallback, m_aAjaxInvocationURL));

    return ret;
  }
}
