package com.phloc.web.http.digestauth;

import java.nio.charset.Charset;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;

/**
 * Handling for HTTP Digest Authentication
 * 
 * @author philip
 */
@Immutable
public final class HTTPDigestAuth
{
  public static final String HEADER_VALUE_PREFIX_DIGEST = "Digest";
  private static final char USERNAME_PASSWORD_SEPARATOR = ':';
  private static final Charset CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
  private static final HTTPDigestAuth s_aInstance = new HTTPDigestAuth ();

  private HTTPDigestAuth ()
  {}
}
