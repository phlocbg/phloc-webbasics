package com.phloc.webctrls.page;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.html.entities.HTMLEntityResolver;
import com.phloc.html.hc.conversion.HCSettings;
import com.phloc.html.hc.impl.HCDOMWrapper;
import com.phloc.html.hc.impl.HCNodeList;

/**
 * Renders a page with HTML code that is provided from an external resource
 * (e.g. for static pages)
 * 
 * @author philip
 */
public class PageViewExternal extends AbstractWebPageExt
{
  private final IMicroElement m_aDocElem;

  public PageViewExternal (@Nonnull @Nonempty final String sID,
                           @Nonnull final String sName,
                           @Nonnull final IReadableResource aRes)
  {
    super (sID, sName);

    // Read content once
    final String sContent = StreamUtils.getAllBytesAsString (aRes, CCharset.CHARSET_UTF_8_OBJ);
    if (sContent == null)
      throw new IllegalStateException ("Failed to read " + aRes.toString ());

    // Parse content once
    final String sParsable = "<x xmlns='" +
                             HCSettings.getConversionSettings (false).getHTMLVersion ().getNamespaceURI () +
                             "'>" +
                             sContent +
                             "</x>";
    final IMicroDocument aDoc = MicroReader.readMicroXML (sParsable, HTMLEntityResolver.getInstance ());
    if (aDoc == null || aDoc.getDocumentElement () == null)
      throw new IllegalStateException ("Failed to parse code for page " + aRes.toString ());
    m_aDocElem = aDoc.getDocumentElement ();
  }

  @Override
  protected void fillContent (@Nonnull final Locale aDisplayLocale, @Nonnull final HCNodeList aNodeList)
  {
    if (m_aDocElem.hasChildren ())
      for (final IMicroNode aChild : m_aDocElem.getChildren ())
        aNodeList.addChild (new HCDOMWrapper (aChild.getClone ()));
  }
}
