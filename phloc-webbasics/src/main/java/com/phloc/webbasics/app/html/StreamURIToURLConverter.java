package com.phloc.webbasics.app.html;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.webbasics.app.LinkUtils;

/**
 * The default implementation of {@link IURIToURLConverter} that uses
 * {@link LinkUtils#getStreamURL(String)} to convert URIs to URLs. If you are
 * using a different stream servlet path, you may need to provide your own
 * implementation and use it in
 * {@link com.phloc.webbasics.ajax.AjaxDefaultResponse}!
 * 
 * @author Philip Helger
 */
@Immutable
public final class StreamURIToURLConverter implements IURIToURLConverter
{
  private static final StreamURIToURLConverter s_aInstance = new StreamURIToURLConverter ();

  private StreamURIToURLConverter ()
  {}

  /**
   * @return The default instance of this class. Never <code>null</code>.
   */
  @Nonnull
  public static StreamURIToURLConverter getInstance ()
  {
    return s_aInstance;
  }

  @Nonnull
  public ISimpleURL getAsURL (@Nonnull @Nonempty final String sURI)
  {
    return LinkUtils.getStreamURL (sURI);
  }
}
