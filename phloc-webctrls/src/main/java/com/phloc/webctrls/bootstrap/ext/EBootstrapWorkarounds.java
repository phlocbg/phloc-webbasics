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
package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;

import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.version.Version;
import com.phloc.commons.version.VersionRange;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.conversion.IHCConversionSettings;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.scopes.web.mgr.WebScopeManager;
import com.phloc.webctrls.bootstrap.CBootstrap;

/**
 * This class provides some workarounds for known Bootstrap bugs.
 * 
 * @author philip
 */
public enum EBootstrapWorkarounds
{
  /**
   * Fix the problem with the drop-down menu on IPad with Bootstrap 2.1.0 and
   * 2.1.1.<br>
   * Source:
   * https://github.com/twitter/bootstrap/issues/2975#issuecomment-6659992
   */
  IPAD_DROPDOWN_FIX
  {
    @Override
    public boolean isApplicable ()
    {
      return new VersionRange (new Version (2, 1, 0), true, new Version (2, 1, 1), true).versionMatches (CBootstrap.BOOTSTRAP_VERSION);
    }

    @Override
    @Nonnull
    public IHCNode createFixCode ()
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar e = aAF.param ("e");
      aAF.body ().invoke (e, "stopPropagation");
      return new HCScript (JQuery.elementNameRef (EHTMLElement.BODY)
                                 .on ()
                                 .arg ("touchstart.dropdown")
                                 .arg (".dropdown-menu")
                                 .arg (aAF));
    }
  };

  private EBootstrapWorkarounds ()
  {}

  /**
   * @return <code>true</code> if this fix needs to be applied to the current
   *         version, <code>false</code> otherwise.
   */
  public abstract boolean isApplicable ();

  /**
   * @return The HC node for fixing the issue. May not be <code>null</code> and
   *         is only called, if isApplicable returns <code>true</code>.
   */
  @Nonnull
  public abstract IHCNode createFixCode ();

  public void appendIfApplicable (@Nonnull final IMicroNode aParentNode,
                                  @Nonnull final IHCConversionSettings aConversionSettings)
  {
    if (isApplicable ())
      if (!WebScopeManager.getRequestScope ().getAndSetAttributeFlag ("bootstrap-workaround-" + name ()))
        aParentNode.appendChild (createFixCode ().getAsNode (aConversionSettings));
  }
}
