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
package com.phloc.webdemoapp.app.ajax.view;

import javax.annotation.concurrent.Immutable;

import com.phloc.webbasics.ajax.IAjaxFunction;

/**
 * This class defines the available ajax functions
 * 
 * @author Philip Helger
 */
@Immutable
public final class CDemoAppAjaxView
{
  public static final IAjaxFunction DATATABLES = new DemoAppAjaxFunctionView ("dataTables");
  public static final IAjaxFunction LOGIN = new DemoAppAjaxFunctionView ("login");
  public static final IAjaxFunction UPDATE_MENU_VIEW = new DemoAppAjaxFunctionView ("updateMenuView");

  private CDemoAppAjaxView ()
  {}
}
