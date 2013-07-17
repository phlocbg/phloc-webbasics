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
package com.phloc.appbasics.errorhandling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.multimap.IMultiMapListBased;
import com.phloc.commons.collections.multimap.MultiLinkedHashMapArrayListBased;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Handles a list of form global errors.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public class ErrorList implements IErrorList
{
  private final List <IError> m_aItems = new ArrayList <IError> ();

  public ErrorList ()
  {}

  public ErrorList (@Nullable final IErrorList aErrorList)
  {
    addAll (aErrorList);
  }

  public ErrorList (@Nullable final Collection <? extends IError> aErrorList)
  {
    addAll (aErrorList);
  }

  public ErrorList (@Nullable final IError... aErrorList)
  {
    addAll (aErrorList);
  }

  public void addAll (@Nullable final IErrorList aErrorList)
  {
    if (aErrorList != null)
      for (final IError aFormError : aErrorList.getAllItems ())
        add (aFormError);
  }

  public void addAll (@Nullable final Collection <? extends IError> aErrorList)
  {
    if (aErrorList != null)
      for (final IError aFormError : aErrorList)
        add (aFormError);
  }

  public void addAll (@Nullable final IError... aErrorList)
  {
    if (aErrorList != null)
      for (final IError aFormError : aErrorList)
        add (aFormError);
  }

  /**
   * Add a new item.
   * 
   * @param aItem
   *        The item to be added. May not be <code>null</code>.
   */
  public void add (@Nonnull final IError aItem)
  {
    if (aItem == null)
      throw new NullPointerException ("item");
    m_aItems.add (aItem);
  }

  public void addSuccess (@Nonnull @Nonempty final String sText)
  {
    add (Error.createSuccess (sText));
  }

  public void addSuccess (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    add (Error.createSuccess (sFieldName, sText));
  }

  public void addSuccess (@Nullable final String sID,
                          @Nullable final String sFieldName,
                          @Nonnull @Nonempty final String sText)
  {
    add (Error.createSuccess (sID, sFieldName, sText));
  }

  public void addInfo (@Nonnull @Nonempty final String sText)
  {
    add (Error.createInfo (sText));
  }

  public void addInfo (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    add (Error.createInfo (sFieldName, sText));
  }

  public void addInfo (@Nullable final String sID,
                       @Nullable final String sFieldName,
                       @Nonnull @Nonempty final String sText)
  {
    add (Error.createInfo (sID, sFieldName, sText));
  }

  public void addWarning (@Nonnull @Nonempty final String sText)
  {
    add (Error.createWarning (sText));
  }

  public void addWarning (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    add (Error.createWarning (sFieldName, sText));
  }

  public void addWarning (@Nullable final String sID,
                          @Nullable final String sFieldName,
                          @Nonnull @Nonempty final String sText)
  {
    add (Error.createWarning (sID, sFieldName, sText));
  }

  public void addError (@Nonnull @Nonempty final String sText)
  {
    add (Error.createError (sText));
  }

  public void addError (@Nullable final String sFieldName, @Nonnull @Nonempty final String sText)
  {
    add (Error.createError (sFieldName, sText));
  }

  public void addError (@Nullable final String sID,
                        @Nullable final String sFieldName,
                        @Nonnull @Nonempty final String sText)
  {
    add (Error.createError (sID, sFieldName, sText));
  }

  public boolean isEmpty ()
  {
    return m_aItems.isEmpty ();
  }

  @Nonnegative
  public int getItemCount ()
  {
    return m_aItems.size ();
  }

  public boolean hasErrorsOrWarnings ()
  {
    for (final IError aItem : m_aItems)
      if (aItem.getLevel ().isMoreOrEqualSevereThan (EFormErrorLevel.WARN))
        return true;
    return false;
  }

  @Nullable
  public EFormErrorLevel getMostSevereErrorLevel ()
  {
    EFormErrorLevel ret = null;
    for (final IError aError : m_aItems)
      if (ret == null || aError.getLevel ().isMoreSevereThan (ret))
      {
        ret = aError.getLevel ();
        if (ret == EFormErrorLevel.HIGHEST)
          break;
      }
    return ret;
  }

  /**
   * @return A non-<code>null</code> list of all contained texts, independent of
   *         the level.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTexts ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IError aError : m_aItems)
      ret.add (aError.getText ());
    return ret;
  }

  /**
   * @return A copy of all contained items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <IError> getAllItems ()
  {
    return ContainerHelper.newList (m_aItems);
  }

  /**
   * @return An {@link Iterator} over all contained items. Never
   *         <code>null</code>.
   */
  @Nonnull
  public Iterator <IError> iterator ()
  {
    return m_aItems.iterator ();
  }

  // --- field specific elements ---

  @Nonnull
  @ReturnsMutableCopy
  public ErrorList getListWithoutField ()
  {
    final ErrorList ret = new ErrorList ();
    for (final IError aError : m_aItems)
      if (!aError.hasFieldName ())
        ret.add (aError);
    return ret;
  }

  public boolean hasEntryForField (@Nullable final String sSearchFieldName)
  {
    for (final IError aError : m_aItems)
      if (EqualsUtils.equals (sSearchFieldName, aError.getFieldName ()))
        return true;
    return false;
  }

  public boolean hasEntryForField (@Nullable final String sSearchFieldName,
                                   @Nullable final EFormErrorLevel eFormErrorLevel)
  {
    if (eFormErrorLevel != null)
      for (final IError aError : m_aItems)
        if (aError.getLevel ().equals (eFormErrorLevel) &&
            EqualsUtils.equals (sSearchFieldName, aError.getFieldName ()))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    final ErrorList ret = new ErrorList ();
    for (final IError aError : m_aItems)
      if (EqualsUtils.equals (sSearchFieldName, aError.getFieldName ()))
        ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    final ErrorList ret = new ErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ErrorList getListOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final ErrorList ret = new ErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IError aError : m_aItems)
        if (aError.hasFieldName ())
        {
          final String sErrorFieldName = aError.getFieldName ();
          for (final String sSearchField : aSearchFieldNames)
            if (sErrorFieldName.startsWith (sSearchField))
            {
              ret.add (aError);
              break;
            }
        }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public ErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    if (StringHelper.hasNoText (sRegExp))
      throw new IllegalArgumentException ("Empty RegExp");

    final ErrorList ret = new ErrorList ();
    for (final IError aError : m_aItems)
      if (aError.hasFieldName ())
        if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
          ret.add (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfField (@Nullable final String sSearchFieldName)
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IError aError : m_aItems)
      if (EqualsUtils.equals (aError.getFieldName (), sSearchFieldName))
        ret.add (aError.getText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFields (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError.getText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IError aError : m_aItems)
        if (aError.hasFieldName ())
        {
          final String sErrorFieldName = aError.getFieldName ();
          for (final String sSearchField : aSearchFieldNames)
            if (sErrorFieldName.startsWith (sSearchField))
            {
              ret.add (aError.getText ());
              break;
            }
        }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    if (StringHelper.hasNoText (sRegExp))
      throw new IllegalArgumentException ("Empty RegExp");

    final List <String> ret = new ArrayList <String> ();
    for (final IError aError : m_aItems)
      if (aError.hasFieldName ())
        if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
          ret.add (aError.getText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public IMultiMapListBased <String, IError> getStructuredByID ()
  {
    final IMultiMapListBased <String, IError> ret = new MultiLinkedHashMapArrayListBased <String, IError> ();
    for (final IError aFormError : m_aItems)
      ret.putSingle (aFormError.getID (), aFormError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public IMultiMapListBased <String, IError> getStructuredByFieldName ()
  {
    final IMultiMapListBased <String, IError> ret = new MultiLinkedHashMapArrayListBased <String, IError> ();
    for (final IError aFormError : m_aItems)
      ret.putSingle (aFormError.getFieldName (), aFormError);
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final ErrorList rhs = (ErrorList) o;
    return m_aItems.equals (rhs.m_aItems);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aItems).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("items", m_aItems).toString ();
  }
}
