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
package com.phloc.webctrls.bootstrap.derived;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.url.ISimpleURL;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webctrls.bootstrap.BootstrapButton;
import com.phloc.webctrls.bootstrap.BootstrapButtonToolbar;
import com.phloc.webctrls.custom.EDefaultIcon;

/**
 * Bootstrap block help.
 * 
 * @author philip
 */
public class BootstrapButtonToolbarAdvanced extends BootstrapButtonToolbar
{
  private final ISimpleURL m_aSelfHref;

  public BootstrapButtonToolbarAdvanced ()
  {
    this (LinkUtils.getSelfHref ());
  }

  public BootstrapButtonToolbarAdvanced (@Nonnull final ISimpleURL aSelfHref)
  {
    super ();
    m_aSelfHref = aSelfHref;
  }

  @Nonnull
  public BootstrapButtonToolbarAdvanced addButtonBack (@Nonnull final Locale aDisplayLocale)
  {
    addChild (BootstrapButton.create (EWebBasicsText.MSG_BUTTON_BACK.getDisplayText (aDisplayLocale),
                                      m_aSelfHref,
                                      EDefaultIcon.BACK));
    return this;
  }

  @Nonnull
  public BootstrapButtonToolbarAdvanced addButtonCancel (@Nonnull final Locale aDisplayLocale)
  {
    addChild (BootstrapButton.create (EWebBasicsText.MSG_BUTTON_CANCEL.getDisplayText (aDisplayLocale),
                                      m_aSelfHref,
                                      EDefaultIcon.CANCEL));
    return this;
  }

  @Nonnull
  public BootstrapButtonToolbarAdvanced addButtonNo (@Nonnull final Locale aDisplayLocale)
  {
    addChild (BootstrapButton.create (EWebBasicsText.MSG_BUTTON_NO.getDisplayText (aDisplayLocale),
                                      m_aSelfHref,
                                      EDefaultIcon.NO));
    return this;
  }

  @Nonnull
  public BootstrapButtonToolbarAdvanced addButtonEdit (@Nonnull final Locale aDisplayLocale,
                                                       @Nonnull final ISimpleURL aURL)
  {
    addChild (BootstrapButton.create (EWebBasicsText.MSG_BUTTON_EDIT.getDisplayText (aDisplayLocale),
                                      aURL,
                                      EDefaultIcon.EDIT));
    return this;
  }

  @Nonnull
  public BootstrapButtonToolbarAdvanced addButtonNew (@Nonnull final ISimpleURL aURL, final String sCaption)
  {
    addChild (BootstrapButton.create (sCaption, aURL, EDefaultIcon.NEW));
    return this;
  }
}
