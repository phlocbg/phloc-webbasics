package com.phloc.webbasics.state;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.type.IHasType;
import com.phloc.commons.type.ObjectType;

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
