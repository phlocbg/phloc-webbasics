package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.HCScriptFile;
import com.phloc.html.hc.impl.HCConditionalCommentNode;
import com.phloc.html.resource.js.JSFilenameHelper;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

/**
 * This class represents a single JS item to be included.
 * 
 * @author Philip Helger
 */
@NotThreadSafe
public final class JSItem
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

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("conditionalComment", m_sCondComment)
                                       .append ("path", m_sPath)
                                       .toString ();
  }
}