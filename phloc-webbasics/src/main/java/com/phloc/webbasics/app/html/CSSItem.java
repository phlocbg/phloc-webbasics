package com.phloc.webbasics.app.html;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.SimpleURL;
import com.phloc.css.CSSFilenameHelper;
import com.phloc.css.media.ECSSMedium;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCLink;
import com.phloc.html.hc.impl.HCConditionalCommentNode;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This class represents a single CSS item to be included.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class CSSItem
{
  private final String m_sCondComment;
  private final String m_sPath;
  private final List <ECSSMedium> m_aMedia;

  public CSSItem (@Nullable final String sCondComment,
                  @Nonnull @Nonempty final String sPath,
                  @Nullable final Collection <ECSSMedium> aMedia)
  {
    ValueEnforcer.notEmpty (sPath, "Path");
    m_sCondComment = sCondComment;
    m_sPath = GlobalDebug.isDebugMode () ? sPath : CSSFilenameHelper.getMinifiedCSSFilename (sPath);
    m_aMedia = ContainerHelper.newList (aMedia);
  }

  @Nullable
  public String getConditionalComment ()
  {
    return m_sCondComment;
  }

  /**
   * @return The path to the CSS item. In debug mode, the full path is used,
   *         otherwise the minified CSS path is used. Neither
   *         <code>null</code> nor empty.
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
  @ReturnsMutableCopy
  public List <ECSSMedium> getMedia ()
  {
    return ContainerHelper.newList (m_aMedia);
  }

  @Nonnull
  public IHCNode getAsNode (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope)
  {
    // Ensure that it works without cookies
    final HCLink aLink = HCLink.createCSSLink (getAsURL (aRequestScope));
    if (m_aMedia != null)
      for (final ECSSMedium eMedium : m_aMedia)
        aLink.addMedium (eMedium);

    if (StringHelper.hasText (m_sCondComment))
      return new HCConditionalCommentNode (m_sCondComment, aLink);
    return aLink;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("conditionalComment", m_sCondComment)
                                       .append ("path", m_sPath)
                                       .append ("media", m_aMedia)
                                       .toString ();
  }
}