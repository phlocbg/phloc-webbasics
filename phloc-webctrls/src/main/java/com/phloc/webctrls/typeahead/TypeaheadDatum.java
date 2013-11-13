package com.phloc.webctrls.typeahead;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.json2.IJsonProvider;
import com.phloc.json2.impl.JsonArray;
import com.phloc.json2.impl.JsonObject;

/**
 * Represents a single typeahead datum (= data record) with the minimum data
 * elements {@link #JSON_VALUE} and {@link #JSON_TOKENS}.
 * 
 * @author Philip Helger
 */
@Immutable
public class TypeaheadDatum implements IJsonProvider
{
  public static final String JSON_VALUE = "value";
  public static final String JSON_TOKENS = "tokens";

  private final String m_sValue;
  private final List <String> m_aTokens;

  public TypeaheadDatum (@Nonnull final String sValue, @Nonnull final String... aTokens)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    if (ArrayHelper.isEmpty (aTokens))
      throw new IllegalArgumentException ("tokens");
    m_sValue = sValue;
    m_aTokens = ContainerHelper.newList (aTokens);
  }

  public TypeaheadDatum (@Nonnull final String sValue, @Nonnull final Collection <String> aTokens)
  {
    if (sValue == null)
      throw new NullPointerException ("value");
    if (ContainerHelper.isEmpty (aTokens))
      throw new IllegalArgumentException ("tokens");
    m_sValue = sValue;
    m_aTokens = ContainerHelper.newList (aTokens);
  }

  @Nonnull
  public final String getValue ()
  {
    return m_sValue;
  }

  @Nonnull
  @ReturnsMutableCopy
  public final List <String> getAllTokens ()
  {
    return ContainerHelper.newList (m_aTokens);
  }

  @Nonnull
  @ReturnsMutableCopy
  public JsonObject getAsJson ()
  {
    return new JsonObject ().add (JSON_VALUE, m_sValue).add (JSON_TOKENS, new JsonArray ().addAll (m_aTokens));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("value", m_sValue).append ("tokens", m_aTokens).toString ();
  }
}
