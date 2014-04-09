package com.phloc.webpages;

import javax.annotation.Nonnull;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.hierarchy.DefaultHierarchyWalkerCallback;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.html.EHTMLElement;
import com.phloc.html.EHTMLVersion;

/**
 * Perform a standard cleansing on externally parsed HTML content. This
 * includes:
 * <ul>
 * <li>Setting the correct namespace as provided in the constructor</li>
 * <li>Removing any read <code>xml:space</code> attributes</li>
 * <li>Ensure that only valid elements are self-closed (
 * <code>&lt;.../&gt;</code>)</li>
 * </ul>
 *
 * @author Philip Helger
 */
public class PageViewExternalHTMLCleanser extends DefaultHierarchyWalkerCallback <IMicroNode>
{
  private final String m_sNamespaceURI;

  public PageViewExternalHTMLCleanser (@Nonnull final EHTMLVersion eHTMLVersion)
  {
    m_sNamespaceURI = ValueEnforcer.notNull (eHTMLVersion, "HTMLVersion").getNamespaceURI ();
  }

  @Override
  public void onItemBeforeChildren (final IMicroNode aItem)
  {
    if (aItem instanceof IMicroElement)
    {
      final IMicroElement e = (IMicroElement) aItem;

      // Ensure correct namespace
      e.setNamespaceURI (m_sNamespaceURI);

      // Remove this attribute
      e.removeAttribute ("xml:space");

      if (EHTMLElement.isTagThatMayNotBeSelfClosed (e.getTagName ()) && e.getChildCount () == 0)
      {
        // Avoid self-closed tag (e.g. <a> or <span>)
        e.appendText ("");
      }
    }
  }
}
