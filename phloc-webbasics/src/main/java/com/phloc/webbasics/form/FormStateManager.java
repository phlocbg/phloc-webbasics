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
package com.phloc.webbasics.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webscopes.singleton.SessionWebSingleton;

public class FormStateManager extends SessionWebSingleton
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (FormStateManager.class);

  private final Map <String, FormState> m_aMap = new HashMap <String, FormState> ();
  private boolean m_bAtLeastOnceAFormState = false;

  @Deprecated
  @UsedViaReflection
  public FormStateManager ()
  {}

  @Nonnull
  public static FormStateManager getInstance ()
  {
    return getSessionSingleton (FormStateManager.class);
  }

  public void saveFormState (@Nonnull final FormState aFormState)
  {
    if (aFormState == null)
      throw new NullPointerException ("formState");
    m_aMap.put (aFormState.getFlowID (), aFormState);
    m_bAtLeastOnceAFormState = true;
    if (GlobalDebug.isDebugMode ())
      s_aLogger.info ("Saved form state: " + aFormState.toString ());
    else
      s_aLogger.info ("Saved form state for page " + aFormState.getPageID ());
  }

  public boolean containedOnceAFormState ()
  {
    return m_bAtLeastOnceAFormState;
  }

  public boolean containsAnySavedFormState ()
  {
    return !m_aMap.isEmpty ();
  }

  @Nullable
  public FormState getFormStateOfID (@Nullable final String sFlowID)
  {
    return m_aMap.get (sFlowID);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <FormState> getAllFormStates ()
  {
    return ContainerHelper.newList (m_aMap.values ());
  }

  @Nonnull
  public EChange deleteFormState (@Nonnull final String sFlowID)
  {
    return EChange.valueOf (m_aMap.remove (sFlowID) != null);
  }

  @Nonnull
  public EChange deleteAllFormStates ()
  {
    if (m_aMap.isEmpty ())
      return EChange.UNCHANGED;
    m_aMap.clear ();
    return EChange.CHANGED;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
