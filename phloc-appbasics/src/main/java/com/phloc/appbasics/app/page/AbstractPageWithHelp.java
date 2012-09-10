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
package com.phloc.appbasics.app.page;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.text.IReadonlyMultiLingualText;

/**
 * Base class for a page that supports help.
 * 
 * @author philip
 */
public abstract class AbstractPageWithHelp extends AbstractPage
{
  /** The name of the window where the help opens up */
  public static final String HELP_WINDOW_NAME = "simplehelpwindow";

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   */
  public AbstractPageWithHelp (@Nonnull @Nonempty final String sID)
  {
    super (sID);
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param sName
   *        The constant (non-translatable) name of the page. May not be
   *        <code>null</code>.
   */
  public AbstractPageWithHelp (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  /**
   * Constructor
   * 
   * @param sID
   *        The unique page ID. May not be <code>null</code>.
   * @param aName
   *        The name of the page. May not be <code>null</code>.
   */
  public AbstractPageWithHelp (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  /**
   * Determine whether help is available for this page. The default
   * implementation returns always <code>true</code>.
   * 
   * @return <code>true</code> if help is available for this page,
   *         <code>false</code> otherwise.
   */
  @OverrideOnDemand
  protected boolean isHelpAvailable ()
  {
    return true;
  }
}
