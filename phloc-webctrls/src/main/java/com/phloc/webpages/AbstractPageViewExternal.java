/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.webpages;

import java.nio.charset.Charset;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroContainer;
import com.phloc.commons.microdom.utils.MicroWalker;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.text.IReadonlyMultiLingualText;
import com.phloc.html.EHTMLVersion;
import com.phloc.html.parser.XHTMLParser;
import com.phloc.webbasics.app.html.WebHTMLCreator;
import com.phloc.webbasics.app.page.WebPageExecutionContext;

/**
 * Base class for pages consisting of external HTML code that is provided from
 * an external resource (e.g. for static pages).
 * 
 * @author Philip Helger
 */
@ThreadSafe
public abstract class AbstractPageViewExternal <WPECTYPE extends WebPageExecutionContext> extends AbstractWebPageExt <WPECTYPE>
{
  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_UTF_8_OBJ;

  protected final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();

  @GuardedBy ("m_aRWLock")
  private boolean m_bReadEveryTime = GlobalDebug.isDebugMode ();

  @Nonnull
  public static IMicroContainer readHTMLPageFragment (@Nonnull final IReadableResource aResource)
  {
    return readHTMLPageFragment (aResource, DEFAULT_CHARSET, WebHTMLCreator.getHTMLVersion ());
  }

  @Nonnull
  public static IMicroContainer readHTMLPageFragment (@Nonnull final IReadableResource aResource,
                                                      @Nonnull final Charset aCharset,
                                                      @Nonnull final EHTMLVersion eHTMLVersion)
  {
    // Read content once
    final String sContent = StreamUtils.getAllBytesAsString (aResource, aCharset);
    if (sContent == null)
      throw new IllegalStateException ("Failed to read resource " + aResource.toString ());

    // Parse content
    final IMicroContainer ret = XHTMLParser.unescapeXHTML (eHTMLVersion, sContent);
    if (ret == null)
      throw new IllegalStateException ("Failed to parse HTML code of resource " + aResource.toString ());

    // Do standard cleansing
    MicroWalker.walkNode (ret, new PageViewExternalHTMLCleanser (eHTMLVersion));

    return ret;
  }

  public AbstractPageViewExternal (@Nonnull @Nonempty final String sID, @Nonnull final String sName)
  {
    super (sID, sName);
  }

  public AbstractPageViewExternal (@Nonnull @Nonempty final String sID, @Nonnull final IReadonlyMultiLingualText aName)
  {
    super (sID, aName);
  }

  /**
   * @return <code>true</code> if the underlying resource should be read each
   *         time {@link #fillContent(WebPageExecutionContext)} is invoked or
   *         <code>false</code> if the resource is read once in the constructor
   *         and re-used over and over.
   */
  public final boolean isReadEveryTime ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return m_bReadEveryTime;
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * @param bReadEveryTime
   *        <code>true</code> if the underlying resource should be read each
   *        time {@link #fillContent(WebPageExecutionContext)} is invoked or
   *        <code>false</code> if the resource should be read once in the
   *        constructor and re-used over and over.
   * @return this
   */
  @Nonnull
  public final AbstractPageViewExternal <WPECTYPE> setReadEveryTime (final boolean bReadEveryTime)
  {
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_bReadEveryTime = bReadEveryTime;
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    return this;
  }

  /**
   * Re-read the content from the underlying resource. This only makes sense, if
   * {@link #isReadEveryTime()} is <code>false</code>.
   * 
   * @see #isReadEveryTime()
   * @see #setReadEveryTime(boolean)
   */
  public abstract void updateFromResource ();

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("readEveryTime", m_bReadEveryTime).toString ();
  }
}
