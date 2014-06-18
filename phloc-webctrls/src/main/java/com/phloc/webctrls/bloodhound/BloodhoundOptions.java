package com.phloc.webctrls.bloodhound;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSFieldRef;
import com.phloc.html.js.builder.JSReturn;
import com.phloc.html.js.builder.JSVar;

@NotThreadSafe
public class BloodhoundOptions
{
  public static final String JSON_DATUM_TOKENIZER = "datumTokenizer";
  public static final String JSON_QUERY_TOKENIZER = "queryTokenizer";
  public static final String JSON_LIMIT = "limit";
  public static final String JSON_DUP_DETECTOR = "dupDetector";
  public static final String JSON_SORTER = "sorter";
  public static final String JSON_LOCAL = "local";
  public static final String JSON_PREFETCH = "prefetch";
  public static final String JSON_REMOTE = "remote";

  public static final int DEFAULT_LIMIT = 5;

  private static final Logger s_aLogger = LoggerFactory.getLogger (BloodhoundOptions.class);

  private IJSExpression m_aDatumTokenizer;
  private IJSExpression m_aQueryTokenizer;
  private int m_nLimit = DEFAULT_LIMIT;
  private IJSExpression m_aDupDetector;
  private IJSExpression m_aSorter;
  private List <? extends BloodhoundDatum> m_aLocal;
  private BloodhoundPrefetch m_aPrefetch;
  private BloodhoundRemote m_aRemote;

  public BloodhoundOptions ()
  {
    final JSVar aVarDatum = new JSVar ("datum");
    m_aDatumTokenizer = new JSAnonymousFunction (aVarDatum, BloodhoundJS.bloodhoundTokenizersWhitespace ()
                                                                        .invoke ()
                                                                        .arg (aVarDatum.ref ("val")));
    m_aQueryTokenizer = BloodhoundJS.bloodhoundTokenizersWhitespace ();
  }

  /**
   * @return A function with the signature (datum) that transforms a datum into
   *         an array of string tokens. Required.
   */
  @Nullable
  public IJSExpression getDatumTokenizer ()
  {
    return m_aDatumTokenizer;
  }

  @Nonnull
  public BloodhoundOptions setDatumTokenizer (@Nullable final IJSExpression aDatumTokenizer)
  {
    m_aDatumTokenizer = aDatumTokenizer;
    return this;
  }

  @Nonnull
  private BloodhoundOptions _setSpecialDatumTokenizer (@Nonnull final JSFieldRef aFieldRef,
                                                       @Nonnull @Nonempty final String sDatumValueFieldName)
  {
    ValueEnforcer.notEmpty (sDatumValueFieldName, "DatumValueFieldName");
    final JSVar aVarDatum = new JSVar ("d");
    return setDatumTokenizer (new JSAnonymousFunction (aVarDatum, aFieldRef.invoke ()
                                                                           .arg (aVarDatum.ref (sDatumValueFieldName))));
  }

  @Nonnull
  public BloodhoundOptions setDatumTokenizerNonword (@Nonnull @Nonempty final String sDatumValueFieldName)
  {
    return _setSpecialDatumTokenizer (BloodhoundJS.bloodhoundTokenizersNonword (), sDatumValueFieldName);
  }

  @Nonnull
  public BloodhoundOptions setDatumTokenizerWhitespace (@Nonnull @Nonempty final String sDatumValueFieldName)
  {
    return _setSpecialDatumTokenizer (BloodhoundJS.bloodhoundTokenizersWhitespace (), sDatumValueFieldName);
  }

  /**
   * Set a datum tokenizer that uses pre-tokenized tokens (e.g. from remote) as
   * contained in the datum. It therefore uses the field
   * {@link BloodhoundDatum#JSON_TOKENS} of each datum.
   * 
   * @return this
   */
  @Nonnull
  public BloodhoundOptions setDatumTokenizerPreTokenized ()
  {
    final JSVar aVarDatum = new JSVar ("d");
    return setDatumTokenizer (new JSAnonymousFunction (aVarDatum,
                                                       new JSReturn (aVarDatum.ref (BloodhoundDatum.JSON_TOKENS))));
  }

  /**
   * @return A function with the signature (query) that transforms a query into
   *         an array of string tokens. Required.
   */
  @Nullable
  public IJSExpression getQueryTokenizer ()
  {
    return m_aQueryTokenizer;
  }

  @Nonnull
  public BloodhoundOptions setQueryTokenizer (@Nullable final IJSExpression aQueryTokenizer)
  {
    m_aQueryTokenizer = aQueryTokenizer;
    return this;
  }

  @Nonnull
  public BloodhoundOptions setQueryTokenizerNonword ()
  {
    return setQueryTokenizer (BloodhoundJS.bloodhoundTokenizersNonword ());
  }

  @Nonnull
  public BloodhoundOptions setQueryTokenizerWhitespace ()
  {
    return setQueryTokenizer (BloodhoundJS.bloodhoundTokenizersWhitespace ());
  }

  /**
   * @return The max number of suggestions to return from Bloodhound#get. If not
   *         reached, the data source will attempt to backfill the suggestions
   *         from remote.
   */
  @Nonnegative
  public int getLimit ()
  {
    return m_nLimit;
  }

  @Nonnull
  public BloodhoundOptions setLimit (@Nonnegative final int nLimit)
  {
    m_nLimit = ValueEnforcer.isGT0 (nLimit, "Limit");
    return this;
  }

  /**
   * @return If set, this is expected to be a function with the signature
   *         (remoteMatch, localMatch) that returns <code>true</code> if the
   *         datums are duplicates or <code>false</code> otherwise. If not set,
   *         duplicate detection will not be performed.
   */
  @Nullable
  public IJSExpression getDupDetector ()
  {
    return m_aDupDetector;
  }

  @Nonnull
  public BloodhoundOptions setDupDetector (@Nullable final IJSExpression aDupDetector)
  {
    m_aDupDetector = aDupDetector;
    return this;
  }

  /**
   * @return A compare function used to sort matched datums for a given query.
   * @see "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/sort"
   */
  @Nullable
  public IJSExpression getSorter ()
  {
    return m_aSorter;
  }

  @Nonnull
  public BloodhoundOptions setSorter (@Nullable final IJSExpression aSorter)
  {
    m_aSorter = aSorter;
    return this;
  }

  @Nullable
  @ReturnsMutableCopy
  public List <? extends BloodhoundDatum> getLocal ()
  {
    return m_aLocal == null ? null : ContainerHelper.newList (m_aLocal);
  }

  @Nonnull
  public BloodhoundOptions setLocal (@Nullable final BloodhoundDatum... aLocal)
  {
    return setLocal (aLocal == null ? null : ContainerHelper.newList (aLocal));
  }

  @Nonnull
  public BloodhoundOptions setLocal (@Nullable final List <? extends BloodhoundDatum> aLocal)
  {
    m_aLocal = aLocal == null ? null : ContainerHelper.newList (aLocal);
    return this;
  }

  @Nullable
  public BloodhoundPrefetch getPrefetch ()
  {
    return m_aPrefetch;
  }

  @Nonnull
  public BloodhoundOptions setPrefetch (@Nullable final BloodhoundPrefetch aPrefetch)
  {
    m_aPrefetch = aPrefetch;
    return this;
  }

  @Nullable
  public BloodhoundRemote getRemote ()
  {
    return m_aRemote;
  }

  @Nonnull
  public BloodhoundOptions setRemote (@Nullable final BloodhoundRemote aRemote)
  {
    m_aRemote = aRemote;
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getAsJSObject ()
  {
    if (m_aDatumTokenizer == null)
      s_aLogger.warn ("No datumTokenizer present!");
    if (m_aQueryTokenizer == null)
      s_aLogger.warn ("No queryTokenizer present!");
    if (m_aLocal == null && m_aPrefetch == null && m_aRemote == null)
      s_aLogger.warn ("Either local, prefetch or remote must be set!");

    final JSAssocArray ret = new JSAssocArray ();
    if (m_aDatumTokenizer != null)
      ret.add (JSON_DATUM_TOKENIZER, m_aDatumTokenizer);
    if (m_aQueryTokenizer != null)
      ret.add (JSON_QUERY_TOKENIZER, m_aQueryTokenizer);
    if (m_nLimit != DEFAULT_LIMIT)
      ret.add (JSON_LIMIT, m_nLimit);
    if (m_aDupDetector != null)
      ret.add (JSON_DUP_DETECTOR, m_aDupDetector);
    if (m_aSorter != null)
      ret.add (JSON_SORTER, m_aSorter);
    if (m_aLocal != null)
    {
      final JSArray aLocal = new JSArray ();
      for (final BloodhoundDatum aDatum : m_aLocal)
        aLocal.add (aDatum.getAsJSObject ());
      ret.add (JSON_LOCAL, aLocal);
    }
    if (m_aPrefetch != null)
      ret.add (JSON_PREFETCH, m_aPrefetch.getAsJSObject ());
    if (m_aRemote != null)
      ret.add (JSON_REMOTE, m_aRemote.getAsJSObject ());
    return ret;
  }
}
