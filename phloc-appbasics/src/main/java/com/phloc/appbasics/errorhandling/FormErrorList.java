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
public class FormErrorList implements IFormErrorList
{
  private final List <IFormError> m_aItems = new ArrayList <IFormError> ();

  public FormErrorList ()
  {}

  public void addAll (@Nullable final IFormErrorList aErrorList)
  {
    if (aErrorList != null)
      for (final IFormError aFormError : aErrorList.getAllItems ())
        addItem (aFormError);
  }

  public void addAll (@Nullable final Collection <? extends IFormError> aErrorList)
  {
    if (aErrorList != null)
      for (final IFormError aFormError : aErrorList)
        addItem (aFormError);
  }

  public void addAll (@Nullable final IFormError... aErrorList)
  {
    if (aErrorList != null)
      for (final IFormError aFormError : aErrorList)
        addItem (aFormError);
  }

  /**
   * Add a new item.
   * 
   * @param aItem
   *        The item to be added. May not be <code>null</code>.
   */
  public final void addItem (@Nonnull final IFormError aItem)
  {
    if (aItem == null)
      throw new NullPointerException ("item");
    m_aItems.add (aItem);
  }

  /**
   * @return <code>true</code> if no item is contained, <code>false</code> if at
   *         least one item is contained.
   */
  public final boolean isEmpty ()
  {
    return m_aItems.isEmpty ();
  }

  /**
   * @return The number of contained items. Always &ge; 0.
   */
  @Nonnegative
  public final int getItemCount ()
  {
    return m_aItems.size ();
  }

  public final boolean hasErrorsOrWarnings ()
  {
    for (final IFormError aItem : m_aItems)
      if (aItem.getLevel ().isMoreOrEqualSevereThan (EFormErrorLevel.WARN))
        return true;
    return false;
  }

  @Nullable
  public final EFormErrorLevel getMostSevereErrorLevel ()
  {
    EFormErrorLevel ret = null;
    for (final IFormError aError : m_aItems)
      if (ret == null || aError.getLevel ().isMoreSevereThan (ret))
        ret = aError.getLevel ();
    return ret;
  }

  /**
   * @return A non-<code>null</code> list of all contained texts, independent of
   *         the level.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllItemTexts ()
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IFormError aError : m_aItems)
      ret.add (aError.getErrorText ());
    return ret;
  }

  /**
   * @return A copy of all contained items. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  public final List <IFormError> getAllItems ()
  {
    return ContainerHelper.newList (m_aItems);
  }

  /**
   * @return An {@link Iterator} over all contained items. Never
   *         <code>null</code>.
   */
  @Nonnull
  public final Iterator <IFormError> iterator ()
  {
    return m_aItems.iterator ();
  }

  // --- field specific elements ---

  /**
   * Check if any entry for the specified field is present
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @return <code>true</code> if an entry for the specified field is present
   */
  public boolean hasEntryForField (@Nullable final String sSearchFieldName)
  {
    if (StringHelper.hasText (sSearchFieldName))
      for (final IFormError aError : m_aItems)
        if (aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  /**
   * Check if any entry for the specified field and the specified error level is
   * present
   * 
   * @param sSearchFieldName
   *        The field name to search.
   * @param eFormErrorLevel
   *        The form error level to search. May not be <code>null</code>
   * @return <code>true</code> if an entry for the specified field is present
   *         that has exactly the specified form error level
   */
  public boolean hasEntryForField (@Nullable final String sSearchFieldName,
                                   @Nullable final EFormErrorLevel eFormErrorLevel)
  {
    if (StringHelper.hasText (sSearchFieldName) && eFormErrorLevel != null)
      for (final IFormError aError : m_aItems)
        if (aError.getLevel ().equals (eFormErrorLevel) && aError.getFieldName ().equals (sSearchFieldName))
          return true;
    return false;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormErrorList getListOfField (@Nullable final String sSearchFieldName)
  {
    final FormErrorList ret = new FormErrorList ();
    for (final IFormError aError : m_aItems)
      if (aError.getFieldName ().equals (sSearchFieldName))
        ret.addItem (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormErrorList getListOfFields (@Nullable final String... aSearchFieldNames)
  {
    final FormErrorList ret = new FormErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.addItem (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormErrorList getListOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final FormErrorList ret = new FormErrorList ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormError aError : m_aItems)
      {
        final String sErrorFieldName = aError.getFieldName ();
        for (final String sSearchField : aSearchFieldNames)
          if (sErrorFieldName.startsWith (sSearchField))
          {
            ret.addItem (aError);
            break;
          }
      }
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public FormErrorList getListOfFieldsRegExp (@Nonnull @Nonempty @RegEx final String sRegExp)
  {
    if (StringHelper.hasNoText (sRegExp))
      throw new IllegalArgumentException ("Empty RegExp");

    final FormErrorList ret = new FormErrorList ();
    for (final IFormError aError : m_aItems)
      if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
        ret.addItem (aError);
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfField (@Nullable final String sSearchFieldName)
  {
    final List <String> ret = new ArrayList <String> ();
    for (final IFormError aError : m_aItems)
      if (aError.getFieldName ().equals (sSearchFieldName))
        ret.add (aError.getErrorText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFields (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormError aError : m_aItems)
        if (ArrayHelper.contains (aSearchFieldNames, aError.getFieldName ()))
          ret.add (aError.getErrorText ());
    return ret;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllItemTextsOfFieldsStartingWith (@Nullable final String... aSearchFieldNames)
  {
    final List <String> ret = new ArrayList <String> ();
    if (ArrayHelper.isNotEmpty (aSearchFieldNames))
      for (final IFormError aError : m_aItems)
      {
        final String sErrorFieldName = aError.getFieldName ();
        for (final String sSearchField : aSearchFieldNames)
          if (sErrorFieldName.startsWith (sSearchField))
          {
            ret.add (aError.getErrorText ());
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
    for (final IFormError aError : m_aItems)
      if (RegExHelper.stringMatchesPattern (sRegExp, aError.getFieldName ()))
        ret.add (aError.getErrorText ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final FormErrorList rhs = (FormErrorList) o;
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
