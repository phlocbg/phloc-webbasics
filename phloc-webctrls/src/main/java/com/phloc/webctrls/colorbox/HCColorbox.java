package com.phloc.webctrls.colorbox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.html.hc.IHCElement;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.IHCNodeBuilder;
import com.phloc.html.hc.html.HCScriptOnDocumentReady;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.html.js.builder.JSAssocArray;
import com.phloc.html.js.builder.jquery.JQuery;
import com.phloc.webbasics.app.html.PerRequestCSSIncludes;
import com.phloc.webbasics.app.html.PerRequestJSIncludes;

public class HCColorbox implements IHCNodeBuilder
{
  public static final boolean DEFAULT_PHOTO = false;

  private final IHCElement <?> m_aElement;
  private boolean m_bPhoto = false;
  private String m_sMaxWidth;
  private String m_sMaxHeight;

  public HCColorbox (@Nonnull final IHCElement <?> aElement)
  {
    if (aElement == null)
      throw new NullPointerException ("element");
    m_aElement = aElement;
  }

  public boolean isPhoto ()
  {
    return m_bPhoto;
  }

  @Nonnull
  public HCColorbox setPhoto (final boolean bPhoto)
  {
    m_bPhoto = bPhoto;
    return this;
  }

  @Nullable
  public String getMaxWidth ()
  {
    return m_sMaxWidth;
  }

  @Nonnull
  public HCColorbox setMaxWidth (@Nullable final String sMaxWidth)
  {
    m_sMaxWidth = sMaxWidth;
    return this;
  }

  @Nullable
  public String getMaxHeight ()
  {
    return m_sMaxHeight;
  }

  @Nonnull
  public HCColorbox setMaxHeight (@Nullable final String sMaxHeight)
  {
    m_sMaxHeight = sMaxHeight;
    return this;
  }

  @Nullable
  public IHCNode build ()
  {
    // Ensure element has an ID
    String sID = m_aElement.getID ();
    if (StringHelper.hasNoText (sID))
    {
      sID = GlobalIDFactory.getNewStringID ();
      m_aElement.setID (sID);
    }
    registerExternalResources ();

    // Build parameters
    final JSAssocArray aArgs = new JSAssocArray ();
    if (m_bPhoto != DEFAULT_PHOTO)
      aArgs.add ("photo", m_bPhoto);
    if (StringHelper.hasText (m_sMaxWidth))
      aArgs.add ("maxWidth", m_sMaxWidth);
    if (StringHelper.hasText (m_sMaxHeight))
      aArgs.add ("maxHeight", m_sMaxHeight);
    return HCNodeList.create (m_aElement,
                              new HCScriptOnDocumentReady (JQuery.idRef (sID).invoke ("colorbox").arg (aArgs)));
  }

  public static void registerExternalResources ()
  {
    PerRequestCSSIncludes.registerCSSIncludeForThisRequest (EColorboxCSSPathProvider.COLORBOX_1320);
    PerRequestJSIncludes.registerJSIncludeForThisRequest (EColorboxJSPathProvider.COLORBOX_1320);
  }

}
