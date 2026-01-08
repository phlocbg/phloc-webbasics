package com.phloc.web.servlet.response;

/**
 * This class can be used to steer the handling of headers when it comes to
 * logging in case of warnings or errors. For security reasons, some sensitive
 * headers may need to be excluded from logging. This class allows to
 * <ul>
 * <li>exclude request headers all together from logging (either globally or in
 * the scope of the current request)</li>
 * <li>exclude response headers all together from logging (either globally or in
 * the scope of the current request)</li>
 * <li>mark specific headers as sensitive which will log a placeholder value
 * instead of the actual value ((either globally or in the scope of the current
 * request))</li>
 * </ul>
 * 
 * @author Boris Gregorcic
 */
public final class HeaderSecurity
{
  private static final String HEADER_VALUE_PLACEHOLDER = "***"; //$NON-NLS-1$
  public static final HeaderSecuritySettings GLOBAL_HEADER_SECURITY_SETTINGS = new HeaderSecuritySettings ();
  public static final ThreadLocal <HeaderSecuritySettings> REQUEST_HEADER_SECURITY_SETTINGS = new ThreadLocal <> ();

  private HeaderSecurity ()
  {
    // Private constructor to prevent instantiation
  }

  private static HeaderSecuritySettings getSettings (final boolean bGlobal)
  {
    return bGlobal ? GLOBAL_HEADER_SECURITY_SETTINGS : REQUEST_HEADER_SECURITY_SETTINGS.get ();
  }

  /**
   * Mark a header as sensitive. if logged, the value will be replaced with an
   * obfuscating placeholder.
   * 
   * @param sHeaderName
   *        The name of the header
   * @param bGlobal
   *        Whether this header should be treated sensitive in general
   *        (globally) or only for this request
   */
  public static void addSensitiveHeader (final String sHeaderName, final boolean bGlobal)
  {
    getSettings (bGlobal).addSensitiveHeader (sHeaderName);
  }

  /**
   * Disable logging of request headers when an error or warning occurs.
   * 
   * @param bGlobal
   *        Whether this should be disabled in general (globally) or only for
   *        this request
   */
  public static void disableLogRequestHeadersOnError (final boolean bGlobal)
  {
    getSettings (bGlobal).setLogRequestHeadersOnError (false);
  }

  /**
   * Disable logging of response headers when an error or warning occurs.
   * 
   * @param bGlobal
   *        Whether this should be disabled in general (globally) or only for
   *        this request
   */
  public static void disableLogResponseHeadersOnError (final boolean bGlobal)
  {
    getSettings (bGlobal).setLogResponseHeadersOnError (false);
  }

  /**
   * @return Whether or not request headers are logged when an error or warning
   *         occurs (not disabled). This will return <code>true</code> if
   *         logging of request headers was neither disabled specifically for
   *         this request, nor globally.
   */
  public static boolean isLogRequestHeadersOnError ()
  {
    return getSettings (false).isLogRequestHeadersOnError () && getSettings (true).isLogRequestHeadersOnError ();
  }

  /**
   * @return Whether or not response headers are logged when an error or warning
   *         occurs (not disabled). This will return <code>true</code> if
   *         logging of response headers was neither disabled specifically for
   *         this request, nor globally.
   */
  public static boolean isLogResponseHeadersOnError ()
  {
    return getSettings (false).isLogResponseHeadersOnError () && getSettings (true).isLogResponseHeadersOnError ();
  }

  /**
   * Checks whether a header is marked as sensitive. This will return
   * <code>true</code> if the header is marked as sensitive either specifically
   * for this request, or globally.
   * 
   * @param sHeaderName
   *        The name of the header
   * @return <code>true</code> if it was marked as sensitive, <code>false</code>
   *         otherwise.
   */
  public static boolean isSensitiveHeader (final String sHeaderName)
  {
    return getSettings (false).isSensitiveHeader (sHeaderName) || getSettings (true).isSensitiveHeader (sHeaderName);
  }

  /**
   * Checks if the passed header is marked as sensitive and returns either the
   * passed value or an obfuscating placeholder in case it is considered
   * sensitive.
   * 
   * @param sHeaderName
   *        The name of the header
   * @param sHeaderValue
   *        The actual value of the header
   * @return The obfuscated value if the header is marked as sensitive, or the
   *         actual value otherwise.
   */
  public static String getObfuscatedHeaderValueOnDemand (final String sHeaderName, final String sHeaderValue)
  {
    return HeaderSecurity.isSensitiveHeader (sHeaderName) ? HEADER_VALUE_PLACEHOLDER : sHeaderValue;
  }

  public static void clear ()
  {
    GLOBAL_HEADER_SECURITY_SETTINGS.clear ();
  }
}
