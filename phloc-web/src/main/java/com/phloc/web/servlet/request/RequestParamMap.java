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
package com.phloc.web.servlet.request;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.attrs.AbstractReadonlyAttributeContainer;
import com.phloc.commons.collections.attrs.IReadonlyAttributeContainer;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.lang.CGStringHelper;
import com.phloc.commons.lang.GenericReflection;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This class represents a nested map that is build from request parameters.
 * E.g. the parameter <code>struct[key]=value</code> results in a
 * <code>map{struct=map{key=value}}</code>.<br>
 * If another parameter <code>struct[key2]=value2</code> is added the resulting
 * map looks like this: <code>map{struct=map{key=value, key2=value2}}</code>.
 * Theses maps can indefinitely be nested.<br>
 * Having only <code>struct[key1][key2][key3]=value</code> results in
 * <code>map{struct=map{key1=map{key2=map{key3=value}}}}</code><br>
 * By default the separator chars ar "[" and "]" but since this may be a problem
 * with JS expressions, {@link #setSeparatorChars(char, char)} offers the
 * possibility to set different separator chars that are not special.
 * 
 * @author Philip Helger
 */
@Immutable
public final class RequestParamMap implements IRequestParamMap
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (RequestParamMap.class);

  private static String s_sOpen = "[";
  private static String s_sClose = "]";

  private final Map <String, Object> m_aMap;

  public RequestParamMap ()
  {
    m_aMap = new HashMap <String, Object> ();
  }

  /**
   * This constructor is private, because it does not call the
   * {@link #put(String, Object)} method which does the main string parsing!
   * 
   * @param aMap
   *        The map to use. May not be <code>null</code>.
   */
  private RequestParamMap (@Nonnull final Map <String, Object> aMap)
  {
    if (aMap == null)
      throw new NullPointerException ("map");
    m_aMap = aMap;
  }

  private void _recursiveAddItem (@Nonnull final Map <String, Object> aMap,
                                  @Nonnull final String sName,
                                  @Nullable final Object aValue)
  {
    final int nIndex = sName.indexOf (s_sOpen);
    if (nIndex == -1)
    {
      // Value level
      aMap.put (sName, aValue);
    }
    else
    {
      // Get the name until the first "["
      final String sPrefix = sName.substring (0, nIndex);

      // Ensure that the respective map is present
      final Object aPrefixValue = aMap.get (sPrefix);
      Map <String, Object> aChildMap = GenericReflection.<Object, Map <String, Object>> uncheckedCast (aPrefixValue);
      if (aChildMap == null)
      {
        aChildMap = new HashMap <String, Object> ();
        aMap.put (sPrefix, aChildMap);
      }

      // Recursively scan child items (starting at the first character after the
      // '[')
      _recursiveAddItem (aChildMap, sName.substring (nIndex + 1), aValue);
    }
  }

  public void put (@Nonnull final String sName, @Nullable final Object aValue)
  {
    // replace everything just to have opening "[" left and one closing "]" at
    // the end that is filtered out manually
    String sRealName = StringHelper.replaceAll (sName, s_sClose + s_sOpen, s_sOpen);
    sRealName = StringHelper.trimEnd (sRealName, s_sClose);
    _recursiveAddItem (m_aMap, sRealName, aValue);
  }

  @Nullable
  private static Map <String, Object> _getAsMap (@Nonnull final Map <String, Object> aMap, @Nullable final String sPath)
  {
    final Object aPathObj = aMap.get (sPath);
    if (aPathObj != null && !(aPathObj instanceof Map <?, ?>))
    {
      s_aLogger.warn ("You're trying to access the path element '" +
                      sPath +
                      "' as map, but it is a " +
                      CGStringHelper.getClassLocalName (aPathObj) +
                      "!");
      return null;
    }
    return GenericReflection.<Object, Map <String, Object>> uncheckedCast (aPathObj);
  }

  @Nullable
  private Map <String, Object> _getResolvedParentMap (@Nonnull @Nonempty final String... aPath)
  {
    if (ArrayHelper.isEmpty (aPath))
      throw new IllegalArgumentException ("Path path array may not be empty!");

    Map <String, Object> aMap = m_aMap;
    // Until the second last object
    for (int i = 0; i < aPath.length - 1; ++i)
    {
      aMap = _getAsMap (aMap, aPath[i]);
      if (aMap == null)
        return null;
    }
    return aMap;
  }

  public boolean contains (@Nonnull @Nonempty final String... aPath)
  {
    final Map <String, Object> aMap = _getResolvedParentMap (aPath);
    return aMap != null && aMap.containsKey (ArrayHelper.getLast (aPath));
  }

  @Nullable
  public Object getObject (@Nonnull @Nonempty final String... aPath)
  {
    final Map <String, Object> aMap = _getResolvedParentMap (aPath);
    return aMap == null ? null : aMap.get (ArrayHelper.getLast (aPath));
  }

  @Nullable
  public String getString (@Nonnull @Nonempty final String... aPath)
  {
    final Object aValue = getObject (aPath);
    return AbstractReadonlyAttributeContainer.getAsString (ArrayHelper.getLast (aPath), aValue, null);
  }

  @Nullable
  @ReturnsMutableCopy
  public IRequestParamMap getMap (@Nonnull @Nonempty final String... aPath)
  {
    if (ArrayHelper.isEmpty (aPath))
      throw new IllegalArgumentException ("Path path array may not be empty!");

    Map <String, Object> aMap = m_aMap;
    for (final String sPath : aPath)
    {
      aMap = _getAsMap (aMap, sPath);
      if (aMap == null)
        return null;
    }
    return new RequestParamMap (aMap);
  }

  public boolean containsKey (@Nullable final String sKey)
  {
    return m_aMap.containsKey (sKey);
  }

  public boolean isEmpty ()
  {
    return m_aMap.isEmpty ();
  }

  @Nonnegative
  public int size ()
  {
    return m_aMap.size ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public Set <String> keySet ()
  {
    return ContainerHelper.newSet (m_aMap.keySet ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Collection <Object> values ()
  {
    return ContainerHelper.newList (m_aMap.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, Object> getAsObjectMap ()
  {
    return ContainerHelper.newMap (m_aMap);
  }

  @Nonnull
  @ReturnsMutableCopy
  public Map <String, String> getAsValueMap ()
  {
    final Map <String, String> ret = new HashMap <String, String> (m_aMap.size ());
    for (final Map.Entry <String, Object> aEntry : m_aMap.entrySet ())
      ret.put (aEntry.getKey (), (String) aEntry.getValue ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof RequestParamMap))
      return false;
    final RequestParamMap rhs = (RequestParamMap) o;
    return m_aMap.equals (rhs.m_aMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }

  @Nonnull
  public static IRequestParamMap create (@Nonnull final Map <String, Object> aAttrCont)
  {
    final RequestParamMap ret = new RequestParamMap ();
    for (final Map.Entry <String, Object> aEntry : aAttrCont.entrySet ())
      ret.put (aEntry.getKey (), aEntry.getValue ());
    return ret;
  }

  @Nonnull
  public static IRequestParamMap create (@Nonnull final IReadonlyAttributeContainer aAttrCont)
  {
    final RequestParamMap ret = new RequestParamMap ();
    for (final String sName : aAttrCont.getAllAttributeNames ())
      ret.put (sName, aAttrCont.getAttributeObject (sName));
    return ret;
  }

  @Nonnull
  @Nonempty
  public static String getFieldName (@Nonnull @Nonempty final String sBaseName, @Nullable final String... aSuffixes)
  {
    if (StringHelper.hasNoText (sBaseName))
      throw new IllegalArgumentException ("baseName");

    final StringBuilder aSB = new StringBuilder (sBaseName);
    if (aSuffixes != null)
      for (final String sSuffix : aSuffixes)
        aSB.append (s_sOpen).append (StringHelper.getNotNull (sSuffix)).append (s_sClose);
    return aSB.toString ();
  }

  /**
   * Set the separator chars to use.
   * 
   * @param cOpen
   *        Open char
   * @param cClose
   *        Close char - must be different from open char!
   */
  public static void setSeparatorChars (final char cOpen, final char cClose)
  {
    if (cOpen == cClose)
      throw new IllegalArgumentException ("Open and closing element may not be identical!");
    s_sOpen = Character.toString (cOpen);
    s_sClose = Character.toString (cClose);
  }

  /**
   * @return The open char. By default this is "["
   */
  public static char getOpenChar ()
  {
    return s_sOpen.charAt (0);
  }

  /**
   * @return The close char. By default this is "]"
   */
  public static char getCloseChar ()
  {
    return s_sClose.charAt (0);
  }
}
