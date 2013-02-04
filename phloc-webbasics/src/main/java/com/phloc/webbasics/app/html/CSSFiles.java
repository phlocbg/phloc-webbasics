package com.phloc.webbasics.app.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
import com.phloc.commons.regex.RegExHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.css.media.ECSSMedium;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLink;
import com.phloc.html.hc.impl.HCConditionalCommentNode;
import com.phloc.webbasics.app.LinkUtils;

public class CSSFiles
{
  public static final class Item
  {
    private final String m_sCondComment;
    private final String m_sPath;
    private final SimpleURL m_aURL;
    private final List <ECSSMedium> m_aMedia;

    public Item (@Nonnull @Nonempty final String sPath)
    {
      this (null, sPath, null);
    }

    public Item (@Nullable final String sCondComment,
                 @Nonnull @Nonempty final String sPath,
                 @Nullable final Collection <ECSSMedium> aMedia)
    {
      if (StringHelper.hasNoText (sPath))
        throw new IllegalArgumentException ("path");
      m_sCondComment = sCondComment;
      m_sPath = GlobalDebug.isDebugMode () ? sPath : CSSFilenameHelper.getMinifiedCSSFilename (sPath);
      m_aURL = LinkUtils.getURLWithContext (m_sPath);
      m_aMedia = ContainerHelper.newList (aMedia);
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

    @Nonnull
    public List <ECSSMedium> getMedia ()
    {
      return ContainerHelper.newList (m_aMedia);
    }

    @Nullable
    public IHCNode getAsNode ()
    {
      final HCLink aLink = HCLink.createCSSLink (m_aURL);
      if (m_aMedia != null)
        for (final ECSSMedium eMedium : m_aMedia)
          aLink.addMedium (eMedium);
      if (StringHelper.hasText (m_sCondComment))
        return new HCConditionalCommentNode (m_sCondComment, aLink);
      return aLink;
    }
  }

  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSFiles.class);

  private final List <Item> m_aItems = new ArrayList <Item> ();

  public CSSFiles (@Nonnull final IReadableResource aFile)
  {
    final IMicroDocument aDoc = MicroReader.readMicroXML (aFile);
    if (aDoc != null)
    {
      final IMicroElement eRoot = aDoc.getDocumentElement ();
      if (eRoot.getTagName ().equals ("list"))
      {
        // Old style
        s_aLogger.warn ("CSS file " + aFile.getPath () + " is in old syntax");
        final List <String> aAllCSSFiles = new ArrayList <String> ();
        if (XMLListHandler.readList (eRoot, aAllCSSFiles).isFailure ())
          s_aLogger.error ("Failed to read " + aFile.getPath ());
        for (final String sCSS : aAllCSSFiles)
          m_aItems.add (new Item (sCSS));
      }
      else
      {
        // New style
        for (final IMicroElement eChild : eRoot.getChildElements ("css"))
        {
          final String sCondComment = eChild.getAttribute ("condcomment");
          final String sPath = eChild.getAttribute ("path");
          if (StringHelper.hasNoText (sPath))
          {
            s_aLogger.error ("Found CSS item without a path in " + aFile.getPath ());
            continue;
          }
          final String sMedia = eChild.getAttribute ("media");
          final Set <ECSSMedium> aMediaList = new LinkedHashSet <ECSSMedium> ();
          if (sMedia != null)
            for (final String sMedium : RegExHelper.getSplitToArray (sMedia, ",\\s*"))
            {
              final ECSSMedium eMedium = ECSSMedium.getFromNameOrNull (sMedium);
              if (eMedium == null)
              {
                s_aLogger.warn ("CSS item '" +
                                sPath +
                                "' in " +
                                aFile.getPath () +
                                " has an invalid medium '" +
                                sMedium +
                                "'");
                continue;
              }
              aMediaList.add (eMedium);
            }
          m_aItems.add (new Item (sCondComment, sPath, aMediaList));
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
