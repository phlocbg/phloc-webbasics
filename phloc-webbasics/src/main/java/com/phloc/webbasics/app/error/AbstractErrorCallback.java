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
package com.phloc.webbasics.app.error;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.app.dao.IDAOReadExceptionHandler;
import com.phloc.appbasics.app.dao.IDAOWriteExceptionHandler;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.string.StringHelper;
import com.phloc.webbasics.action.IActionExceptionHandler;
import com.phloc.webbasics.ajax.IAjaxExceptionHandler;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * A base class for a central error callback that handles all kind of errors and
 * exceptions
 * 
 * @author Philip Helger
 */
public abstract class AbstractErrorCallback implements IAjaxExceptionHandler, IActionExceptionHandler, IDAOReadExceptionHandler, IDAOWriteExceptionHandler
{
  /**
   * Implement this method to handle all errors in a similar way.
   * 
   * @param t
   *        The exception that occurred.
   * @param aRequestScope
   *        The request scope in which the error occurred. May be
   *        <code>null</code>.
   * @param sErrorCode
   *        The unique error code for this error. Neither <code>null</code> not
   *        empty.
   */
  protected abstract void onError (@Nonnull Throwable t,
                                   @Nullable IRequestWebScopeWithoutResponse aRequestScope,
                                   @Nonnull @Nonempty String sErrorCode);

  public void onAjaxException (@Nonnull final Throwable t,
                               @Nullable final String sAjaxFunctionName,
                               @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final String sErrorCode = "ajax-error-" +
                              (StringHelper.hasText (sAjaxFunctionName) ? sAjaxFunctionName + "-" : "") +
                              InternalErrorHandler.createNewErrorID ();
    onError (t, aRequestScope, sErrorCode);
  }

  public void onActionExecutionException (@Nonnull final Throwable t,
                                          @Nonnull final String sActionName,
                                          @Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    final String sErrorCode = "action-error-" + sActionName + "-" + InternalErrorHandler.createNewErrorID ();
    onError (t, aRequestScope, sErrorCode);
  }

  public void onDAOReadException (@Nonnull final Throwable t,
                                  final boolean bInit,
                                  @Nullable final IReadableResource aRes)
  {
    onError (t, null, "DAO " + (bInit ? "init" : "read") + " error" + (aRes == null ? "" : " for " + aRes.getPath ()));
  }

  public void onDAOWriteException (@Nonnull final Throwable t,
                                   @Nonnull final IReadableResource aRes,
                                   @Nonnull final CharSequence aFileContent)
  {
    onError (t, null, "DAO write error for " + aRes.getPath () + " with " + aFileContent.length () + " chars");
  }
}
