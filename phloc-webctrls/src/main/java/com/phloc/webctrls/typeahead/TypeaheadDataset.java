package com.phloc.webctrls.typeahead;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.JSExpr;

/**
 * Represents a single typeahead dataset.
 * 
 * @author Philip Helger
 */
@Immutable
public class TypeaheadDataset
{
  public static final String JSON_NAME = "name";
  public static final String JSON_VALUE_KEY = "valueKey";
  public static final String JSON_LIMIT = "limit";
  public static final String JSON_TEMPLATE = "template";
  public static final String JSON_ENGINE = "engine";
  public static final String JSON_HEADER = "header";
  public static final String JSON_FOOTER = "footer";
  public static final String JSON_LOCAL = "local";
  public static final String JSON_PREFETCH = "prefetch";
  public static final String JSON_REMOTE = "remote";

  public static final String DEFAULT_VALUE_KEY = "value";
  public static final int DEFAULT_LIMIT = 5;

  private final String m_sName;
  private String m_sValueKey = DEFAULT_VALUE_KEY;
  private int m_nLimit = DEFAULT_LIMIT;
  private IJSExpression m_aTemplate;
  private String m_sEngine;

  public TypeaheadDataset (@Nonnull @Nonempty final String sName)
  {
    if (StringHelper.hasNoText (sName))
      throw new IllegalArgumentException ("name");
    m_sName = sName;
  }

  /**
   * @return The string used to identify the dataset. Used by typeahead.js to
   *         cache intelligently.
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public TypeaheadDataset setValueKey (@Nonnull @Nonempty final String sValueKey)
  {
    if (StringHelper.hasNoText (sValueKey))
      throw new IllegalArgumentException ("valueKey");
    m_sValueKey = sValueKey;
    return this;
  }

  /**
   * @return The key used to access the value of the datum in the datum object.
   *         Defaults to <code>value</code>.
   */
  @Nonnull
  @Nonempty
  public String getValueKey ()
  {
    return m_sValueKey;
  }

  @Nonnull
  public TypeaheadDataset setLimit (@Nonnegative final int nLimit)
  {
    if (nLimit < 1)
      throw new IllegalArgumentException ("Limit too small: " + nLimit);
    m_nLimit = nLimit;
    return this;
  }

  /**
   * @return The max number of suggestions from the dataset to display for a
   *         given query. Defaults to 5.
   */
  @Nonnegative
  public int getLimit ()
  {
    return m_nLimit;
  }

  @Nonnull
  public TypeaheadDataset setTemplate (@Nullable final String sTemplate)
  {
    return setTemplate (StringHelper.hasText (sTemplate) ? JSExpr.lit (sTemplate) : (IJSExpression) null);
  }

  @Nonnull
  public TypeaheadDataset setTemplate (@Nullable final IJSExpression aTemplate)
  {
    m_aTemplate = aTemplate;
    return this;
  }

  /**
   * @return The template used to render suggestions. Can be a string or a
   *         precompiled template. If not provided, suggestions will render as
   *         their value contained in a <code>&lt;p></code> element (i.e.
   *         <code>&lt;p>value&lt;/p></code>).
   */
  @Nullable
  public IJSExpression getTemplate ()
  {
    return m_aTemplate;
  }

  /**
   * The template engine used to compile/render <code>template</code> if it is a
   * string. Any engine can use used as long as it adheres to the expected API.
   * <strong>Required</strong> if <code>template</code> is a string.<br>
   * When you want to use Handlebars as the engine you must include Handlebars
   * and phloc-typeahead.js and may specify the name
   * <strong>TypeaheadHandlebars</strong>.
   * 
   * @param sEngine
   *        The name of the engine to use. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public TypeaheadDataset setEngine (@Nullable final String sEngine)
  {
    m_sEngine = sEngine;
    return this;
  }

  /**
   * @return The template engine used to compile/render <code>template</code> if
   *         it is a string. Any engine can use used as long as it adheres to
   *         the expected API. <strong>Required</strong> if
   *         <code>template</code> is a string.
   */
  @Nullable
  public String getEngine ()
  {
    return m_sEngine;
  }

  @Nonnull
  @ReturnsMutableCopy
  public JSAssocArray getAsJSCode ()
  {
    final JSAssocArray ret = new JSAssocArray ().add (JSON_NAME, m_sName);
    if (!m_sValueKey.equals (DEFAULT_VALUE_KEY))
      ret.add (JSON_VALUE_KEY, m_sValueKey);
    if (m_nLimit != DEFAULT_LIMIT)
      ret.add (JSON_LIMIT, m_nLimit);
    if (m_aTemplate != null)
      ret.add (JSON_TEMPLATE, m_aTemplate);
    if (StringHelper.hasText (m_sEngine))
      ret.add (JSON_ENGINE, m_sEngine);
    return ret;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("name", m_sName)
                                       .append ("valueKey", m_sValueKey)
                                       .append ("limit", m_nLimit)
                                       .appendIfNotNull ("template", m_aTemplate)
                                       .appendIfNotNull ("engine", m_sEngine)
                                       .toString ();
  }
}
