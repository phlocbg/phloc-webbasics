/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.webbasics.state;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.IHasType;
import com.phloc.commons.type.ObjectType;

/**
 * A special wrapper that wraps an arbitrary object into an {@link IHasUIState}
 * object.
 * 
 * @author Philip Helger
 * @param <T>
 *        Wrapped oject data tapye
 */
public class UIStateWrapper <T extends Serializable> implements IHasUIState
{
  private final ObjectType m_aObjectType;
  private final T m_aObject;

  public UIStateWrapper (@Nonnull final ObjectType aObjectType, @Nonnull final T aObject)
  {
    if (aObjectType == null)
      throw new NullPointerException ("objectType");
    if (aObject == null)
      throw new NullPointerException ("object");
    m_aObjectType = aObjectType;
    m_aObject = aObject;
  }

  @Nonnull
  public ObjectType getTypeID ()
  {
    return m_aObjectType;
  }

  /**
   * @return The wrapped object. Never <code>null</code>.
   */
  @Nonnull
  public T getObject ()
  {
    return m_aObject;
  }

  @Nullable
  public <U> U getCastedObject ()
  {
    // Regular cast
    return GenericReflection.<T, U> uncheckedCast (m_aObject);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("objectType", m_aObjectType).append ("object", m_aObject).toString ();
  }

  @Nonnull
  public static <T extends Serializable> UIStateWrapper <T> create (@Nonnull final ObjectType aObjectType,
                                                                    @Nonnull final T aObject)
  {
    return new UIStateWrapper <T> (aObjectType, aObject);
  }

  @Nonnull
  public static <T extends Serializable & IHasType> UIStateWrapper <T> create (@Nonnull final T aObject)
  {
    return new UIStateWrapper <T> (aObject.getTypeID (), aObject);
  }
}
