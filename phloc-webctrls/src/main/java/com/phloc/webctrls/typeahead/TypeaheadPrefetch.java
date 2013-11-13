package com.phloc.webctrls.typeahead;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.ICloneable;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSAssocArray;

/**
 * Represent a prefetch object used in a dataset
 * 
 * @author Philip Helger
 */
@Immutable
public class TypeaheadPrefetch implements ICloneable <TypeaheadPrefetch>
{
  public static final String JSON_URL = "url";
  public static final String JSON_TTL = "ttl";
  public static final String JSON_FILTER = "filter";

  public static final int DEFAULT_TTL = 86400000;

  private final SimpleURL m_aURL;
  private int m_nTTL = DEFAULT_TTL;
  private JSAnonymousFunction m_aFilter;

  /**
   * Constructor
   * 
   * @param aURL
   *        A URL to a JSON file containing an array of datums.
   */
  public TypeaheadPrefetch (@Nonnull final ISimpleURL aURL)
  {
    if (aURL == null)
      throw new NullPointerException ("URL");
    m_aURL = new SimpleURL (aURL);
  }

  public TypeaheadPrefetch (@Nonnull final TypeaheadPrefetch aOther)
  {
    if (aOther == null)
      throw new NullPointerException ("Other");
    m_aURL = aOther.m_aURL.getClone ();
    m_nTTL = aOther.m_nTTL;
    m_aFilter = aOther.m_aFilter;
  }

  /**
   * @return A URL to a JSON file containing an array of datums.
   */
  @Nonnull
  public ISimpleURL getURL ()
  {
    return m_aURL;
  }

  /**
   * The time (in milliseconds) the prefetched data should be cached in
   * localStorage. Defaults to <code>86400000</code> (1 day).
   * 
   * @param nTTL
   *        Time to live in milliseconds. Must be &ge; 1.
   * @return this
   */
  @Nonnull
  public TypeaheadPrefetch setTTL (@Nonnegative final int nTTL)
  {
    if (nTTL < 1)
      throw new IllegalArgumentException ("TTL is too small: " + nTTL);
    m_nTTL = nTTL;
    return this;
  }

  /**
   * @return The time (in milliseconds) the prefetched data should be cached in
   *         localStorage. Defaults to <code>86400000</code> (1 day).
   */
  @Nonnegative
  public int getTTL ()
  {
    return m_nTTL;
  }

  /**
   * A function with the signature <code>filter(parsedResponse)</code> that
   * transforms the response body into an array of datums. Expected to return an
   * array of datums.
   * 
   * @param aFilter
   *        Function to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public TypeaheadPrefetch setFilter (@Nullable final JSAnonymousFunction aFilter)
  {
    m_aFilter = aFilter;
    return this;
  }

  /**
   * @return A function with the signature <code>filter(parsedResponse)</code>
   *         that transforms the response body into an array of datums. Expected
   *         to return an array of datums.
   */
  @Nullable
  public JSAnonymousFunction getFilter ()
  {
    return m_aFilter;
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getAsJSObject ()
  {
    final JSAssocArray ret = new JSAssocArray ();
    ret.add (JSON_URL, m_aURL.getAsString ());
    if (m_nTTL != DEFAULT_TTL)
      ret.add (JSON_TTL, m_nTTL);
    if (m_aFilter != null)
      ret.add (JSON_FILTER, m_aFilter);
    return ret;
  }

  @Nonnull
  public TypeaheadPrefetch getClone ()
  {
    return new TypeaheadPrefetch (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("URL", m_aURL)
                                       .append ("ttl", m_nTTL)
                                       .appendIfNotNull ("filter", m_aFilter)
                                       .toString ();
  }
}
