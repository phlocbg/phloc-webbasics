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
package com.phloc.webbasics.app.html;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCScriptFile;
import com.phloc.html.hc.impl.HCConditionalCommentNode;
import com.phloc.html.resource.js.JSFilenameHelper;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This class keeps all the global JS files that are read from configuration.
 * 
 * @author Philip Helger
 */
@ThreadSafe
public class JSFiles
{
  /**
   * This class represents a single JS item to be included.
   * 
   * @author Philip Helger
   */
  @NotThreadSafe
  public static final class JSItem
  {
    private final String m_sCondComment;
    private final String m_sPath;

    public JSItem (@Nullable final String sCondComment, @Nonnull @Nonempty final String sPath)
    {
      ValueEnforcer.notEmpty (sPath, "path");
      m_sCondComment = sCondComment;
      m_sPath = GlobalDebug.isDebugMode () ? sPath : JSFilenameHelper.getMinifiedJSPath (sPath);
    }

    @Nullable
    public String getConditionalComment ()
    {
      return m_sCondComment;
    }

    /**
     * @return The path to the JS item. In debug mode, the full path is used,
     *         otherwise the minified JS path is used. Neither <code>null</code>
     *         nor empty.
     */
    @Nonnull
    @Nonempty
    public String getPath ()
    {
      return m_sPath;
    }

    @Nonnull
    public SimpleURL getAsURL (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
    {
      return LinkUtils.getURLWithContext (aRequestScope, m_sPath);
    }

    @Nonnull
    public IHCNode getAsNode (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
    {
      // Ensure it works cookie-less
      final HCScriptFile aScript = HCScriptFile.create (getAsURL (aRequestScope));
      if (StringHelper.hasText (m_sCondComment))
        return new HCConditionalCommentNode (m_sCondComment, aScript);
      return aScript;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (JSFiles.class);

  private final ReadWriteLock m_aRWLock = new ReentrantReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private final List <JSItem> m_aItems = new ArrayList <JSItem> ();

  public JSFiles (@Nonnull final IReadableResource aFile)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aFile);
    if (aDoc != null)
    {
      final IMicroElement eRoot = aDoc.getDocumentElement ();
      if (eRoot.getTagName ().equals ("list"))
      {
        // Old style
        s_aLogger.warn ("JS file " + aFile.getPath () + " is in old syntax");
        final List <String> aAllJSFiles = new ArrayList <String> ();
        if (XMLListHandler.readList (eRoot, aAllJSFiles).isFailure ())
          s_aLogger.error ("Failed to read " + aFile.getPath ());
        for (final String sJS : aAllJSFiles)
          addGlobalItem (null, sJS);
      }
      else
      {
        // New style
        for (final IMicroElement eChild : eRoot.getAllChildElements ("js"))
        {
          final String sCondComment = eChild.getAttribute ("condcomment");
          final String sPath = eChild.getAttribute ("path");
          if (StringHelper.hasNoText (sPath))
          {
            s_aLogger.error ("Found JS item without a path in " + aFile.getPath ());
            continue;
          }
          addGlobalItem (sCondComment, sPath);
        }
      }
    }
  }

  @Nonnull
  public JSFiles addGlobalItem (@Nullable final String sCondComment, @Nonnull @Nonempty final String sPath)
  {
    final JSItem aItem = new JSItem (sCondComment, sPath);
    m_aRWLock.writeLock ().lock ();
    try
    {
      m_aItems.add (aItem);
    }
    finally
    {
      m_aRWLock.writeLock ().unlock ();
    }
    return this;
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <JSItem> getAllItems ()
  {
    m_aRWLock.readLock ().lock ();
    try
    {
      return ContainerHelper.newList (m_aItems);
    }
    finally
    {
      m_aRWLock.readLock ().unlock ();
    }
  }
}
