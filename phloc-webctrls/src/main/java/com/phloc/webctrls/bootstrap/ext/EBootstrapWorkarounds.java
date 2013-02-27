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
package com.phloc.webctrls.bootstrap.ext;

import javax.annotation.Nonnull;

import com.phloc.commons.version.Version;
import com.phloc.commons.version.VersionRange;
import com.phloc.html.EHTMLElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeWithChildren;
import com.phloc.html.hc.html.HCScript;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSVar;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webctrls.bootstrap.CBootstrapCSS;
import com.phloc.webscopes.mgr.WebScopeManager;

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
    public boolean isApplicable (@Nonnull final Version aBootstrapVersion)
    {
      return new VersionRange (new Version (2, 1, 0), true, new Version (2, 2, 1), true).versionMatches (aBootstrapVersion);
    }

    @Override
    @Nonnull
    public IHCNode createFixCode ()
    {
      final JSAnonymousFunction aAF = new JSAnonymousFunction ();
      final JSVar e = aAF.param ("e");
      aAF.body ().invoke (e, "stopPropagation");
      return new HCScript (JQuery.elementNameRef (EHTMLElement.BODY)
                                 .on ("touchstart.dropdown")
                                 .arg (JQuery.classRef (CBootstrapCSS.DROPDOWN_MENU))
                                 .arg (aAF));
    }
  };

  private EBootstrapWorkarounds ()
  {}

  /**
   * @param aBootstrapVersion
   *        The bootstrap version to be used. May not be <code>null</code>.
   * @return <code>true</code> if this fix needs to be applied to the current
   *         version, <code>false</code> otherwise.
   */
  public abstract boolean isApplicable (@Nonnull Version aBootstrapVersion);

  /**
   * @return The HC node for fixing the issue. May not be <code>null</code> and
   *         is only called, if isApplicable returns <code>true</code>.
   */
  @Nonnull
  public abstract IHCNode createFixCode ();

  /**
   * Append this specific fix, if the fix is applicable for the passed bootstrap
   * version.
   * 
   * @param aBootstrapVersion
   *        The bootstrap version to be used. May not be <code>null</code>.
   * @param aParentElement
   *        The parent element to append the fix code to. May not be
   *        <code>null</code>.
   */
  public void appendIfApplicable (@Nonnull final Version aBootstrapVersion,
                                  @Nonnull final IHCNodeWithChildren <?> aParentElement)
  {
    if (isApplicable (aBootstrapVersion))
      if (!WebScopeManager.getRequestScope ().getAndSetAttributeFlag ("bootstrap-workaround-" + name ()))
        aParentElement.addChild (createFixCode ());
  }
}
