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
package com.phloc.webdemoapp.app.ajax.config;

import javax.annotation.concurrent.Immutable;

import com.phloc.webbasics.ajax.IAjaxFunction;

/**
 * This class defines the available ajax functions
 * 
 * @author philip
 */
@Immutable
public final class CDemoAppAjaxConfig
{
  public static final IAjaxFunction CONFIG_SAVE_FORM_STATE = new DemoAppAjaxFunctionConfig ("saveFormState");
  public static final IAjaxFunction CONFIG_UPDATE_MENU_VIEW = new DemoAppAjaxFunctionConfig ("updateMenuView");

  private CDemoAppAjaxConfig ()
  {}
}
