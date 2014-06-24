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
package com.phloc.webpages;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.appbasics.security.AccessManager;
import com.phloc.appbasics.security.lock.LockResult;
import com.phloc.appbasics.security.lock.ObjectLockManager;
import com.phloc.appbasics.security.user.IUser;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.state.EContinue;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.app.page.IWebPageExecutionContext;
import com.phloc.webbasics.mgr.MetaSystemManager;

/**
 * Abstract base class for a web page that supports locking etc.
 * 
 * @author Philip Helger
 * @param <DATATYPE>
 *        The data type of the object to be handled.
 */
public abstract class AbstractWebPageFormExt <DATATYPE extends IHasID <String>, WPECTYPE extends IWebPageExecutionContext> extends AbstractWebPageForm <DATATYPE, WPECTYPE>
{
  public AbstractWebPageFormExt (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public AbstractWebPageFormExt (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  public AbstractWebPageFormExt (@Nonnull @Nonempty final String sID,
                                 @Nonnull final String sName,
                                 @Nullable final String sDescription)
  {
    super (sID, sName, sDescription);
  }

  public AbstractWebPageFormExt (@Nonnull @Nonempty final String sID,
                                 @Nonnull final IReadonlyMultiLingualText aName,
                                 @Nullable final IReadonlyMultiLingualText aDescription)
  {
    super (sID, aName, aDescription);
  }

  /**
   * Get the display name of the passed object.
   * 
   * @param aSelectedObject
   *        The object to get the display name from. Never <code>null</code>.
   * @return <code>null</code> to indicate that no display name is available
   */
  @Nullable
  @OverrideOnDemand
  protected String getObjectDisplayName (@Nonnull final DATATYPE aSelectedObject)
  {
    return null;
  }

  /**
   * Try to lock the specified object. When overriding the method make sure to
   * emit all error messages on your own, when e.g. an object is locked.
   * 
   * @param aWPEC
   *        The current web page execution context. Never <code>null</code>.
   * @param aSelectedObject
   *        The currently selected object. May be <code>null</code> if no object
   *        is selected.
   * @param eFormAction
   *        The current form action. May be <code>null</code> if a non-standard
   *        action is handled.
   * @return {@link EContinue#CONTINUE} if normal execution can continue or
   *         {@link EContinue#BREAK} if execution cannot continue (e.g. because
   *         object is already locked).
   */
  @Override
  @Nonnull
  @OverrideOnDemand
  protected EContinue beforeProcessing (@Nonnull final WPECTYPE aWPEC,
                                        @Nullable final DATATYPE aSelectedObject,
                                        @Nullable final EWebPageFormAction eFormAction)
  {
    final ObjectLockManager aOLM = MetaSystemManager.getLockManager ();
    // Lock EDIT and DELETE if an object is present
    if (eFormAction != null && eFormAction.isModifying () && aSelectedObject != null)
    {
      // Try to lock object
      final String sObjectID = aSelectedObject.getID ();
      final LockResult <String> aLockResult = aOLM.lockObjectAndUnlockAllOthers (sObjectID);
      if (aLockResult.isNotLocked ())
      {
        // Failed to lock object
        final Locale aDisplayLocale = aWPEC.getDisplayLocale ();
        final HCNodeList aNodeList = aWPEC.getNodeList ();

        final String sLockUserID = aOLM.getLockUserID (sObjectID);
        final IUser aLockUser = AccessManager.getInstance ().getUserOfID (sLockUserID);
        final String sObjectName = getObjectDisplayName (aSelectedObject);
        final String sDisplayObjectName = StringHelper.hasText (sObjectName) ? " '" + sObjectName + "'" : "";
        final String sDisplayUserName = aLockUser != null ? "'" + aLockUser.getDisplayName () + "'"
                                                         : EWebPageText.LOCKING_OTHER_USER.getDisplayText (aDisplayLocale);
        aNodeList.addChild (getStyler ().createErrorBox (EWebPageText.LOCKING_FAILED.getDisplayTextWithArgs (aDisplayLocale,
                                                                                                             sDisplayObjectName,
                                                                                                             sDisplayUserName)));
        return EContinue.BREAK;
      }
    }
    else
    {
      // No lock action required - unlock all
      aOLM.unlockAllObjectsOfCurrentUser ();
    }

    return EContinue.CONTINUE;
  }
}
