package com.phloc.webbasics.app.html;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.reader.XMLListHandler;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCScriptFile;
import com.phloc.html.hc.impl.HCConditionalCommentNode;
import com.phloc.html.resource.js.JSFilenameHelper;
import com.phloc.webbasics.app.LinkUtils;

public class JSFiles
{
  public static final class Item
  {
    private final String m_sCondComment;
    private final String m_sPath;
    private final SimpleURL m_aURL;

    public Item (@Nullable final String sCondComment, @Nonnull @Nonempty final String sPath)
    {
      if (StringHelper.hasNoText (sPath))
        throw new IllegalArgumentException ("path");
      m_sCondComment = sCondComment;
      m_sPath = GlobalDebug.isDebugMode () ? sPath : JSFilenameHelper.getMinifiedJSPath (sPath);
      m_aURL = LinkUtils.getURLWithContext (m_sPath);
    }

    @Nullable
    public String getConditionalComment ()
    {
      return m_sCondComment;
    }

    @Nonnull
    @Nonempty
    public String getPath ()
    {
      return m_sPath;
    }

    @Nonnull
    public ISimpleURL getAsURL ()
    {
      return m_aURL;
    }

    @Nullable
    public IHCNode getAsNode ()
    {
      final HCScriptFile aScript = HCScriptFile.create (m_aURL);
      if (StringHelper.hasText (m_sCondComment))
        return new HCConditionalCommentNode (m_sCondComment, aScript);
      return aScript;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (JSFiles.class);

  private final List <Item> m_aItems = new ArrayList <Item> ();

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
          m_aItems.add (new Item (null, sJS));
      }
      else
      {
        // New style
        for (final IMicroElement eChild : eRoot.getChildElements ("js"))
        {
          final String sCondComment = eChild.getAttribute ("condcomment");
          final String sPath = eChild.getAttribute ("path");
          if (StringHelper.hasNoText (sPath))
          {
            s_aLogger.error ("Found JS item without a path in " + aFile.getPath ());
            continue;
          }
          m_aItems.add (new Item (sCondComment, sPath));
        }
      }
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <Item> getAllItems ()
  {
    return ContainerHelper.newList (m_aItems);
  }
}
