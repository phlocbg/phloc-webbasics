/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.webscopes.domain;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.phloc.scopes.nonweb.domain.IRequestScope;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.webscopes.IWebScope;

/**
 * Interface for a single web request scope object that does not offer access to
 * the HTTP response.
 * 
 * @author philip
 */
public interface IRequestWebScopeWithoutResponse extends IRequestScope, IWebScope
{
  /**
   * @return A non-<code>null</code> but maybe empty map with all contained
   *         {@link IFileItem} objects from file uploads. The key of the map is
   *         the field name. Important: if the value is an array of
   *         {@link IFileItem} it is not considered in the returned map!
   */
  @Nonnull
  Map <String, IFileItem> getAllUploadedFileItems ();

  /**
   * @return A non-<code>null</code> but maybe empty list of all
   *         {@link IFileItem} objects in the request. In comparison to
   *         {@link #getAllUploadedFileItems()} this method also returns the
   *         content of {@link IFileItem} arrays.
   */
  @Nonnull
  List <IFileItem> getAllUploadedFileItemValues ();

  /**
   * Returns the name of the scheme used to make this request, for example,
   * <code>http</code>, <code>https</code>, or <code>ftp</code>. Different
   * schemes have different rules for constructing URLs, as noted in RFC 1738.
   * 
   * @return a <code>String</code> containing the name of the scheme used to
   *         make this request
   */
  String getScheme ();

  /**
   * Returns the host name of the server to which the request was sent. It is
   * the value of the part before ":" in the <code>Host</code> header value, if
   * any, or the resolved server name, or the server IP address.
   * 
   * @return a <code>String</code> containing the name of the server
   */

  String getServerName ();

  /**
   * Returns the name and version of the protocol the request uses in the form
   * <i>protocol/majorVersion.minorVersion</i>, for example, HTTP/1.1. For HTTP
   * servlets, the value returned is the same as the value of the CGI variable
   * <code>SERVER_PROTOCOL</code>.
   * 
   * @return a <code>String</code> containing the protocol name and version
   *         number
   */
  String getServerProtocolVersion ();

  /**
   * Returns the port number to which the request was sent. It is the value of
   * the part after ":" in the <code>Host</code> header value, if any, or the
   * server port where the client connection was accepted on.
   * 
   * @return an integer specifying the port number
   */
  int getServerPort ();

  /**
   * Returns the name of the HTTP method with which this request was made, for
   * example, GET, POST, or PUT. Same as the value of the CGI variable
   * REQUEST_METHOD.
   * 
   * @return a <code>String</code> specifying the name of the method with which
   *         this request was made
   */
  String getMethod ();

  /**
   * Returns any extra path information associated with the URL the client sent
   * when it made this request. The extra path information follows the servlet
   * path but precedes the query string and will start with a "/" character.
   * <p>
   * This method returns <code>null</code> if there was no extra path
   * information.
   * <p>
   * Same as the value of the CGI variable PATH_INFO.
   * 
   * @return a <code>String</code>, decoded by the web container, specifying
   *         extra path information that comes after the servlet path but before
   *         the query string in the request URL; or <code>null</code> if the
   *         URL does not have any extra path information
   */
  String getPathInfo ();

  /**
   * Returns any extra path information after the servlet name but before the
   * query string, and translates it to a real path. Same as the value of the
   * CGI variable PATH_TRANSLATED.
   * <p>
   * If the URL does not have any extra path information, this method returns
   * <code>null</code> or the servlet container cannot translate the virtual
   * path to a real path for any reason (such as when the web application is
   * executed from an archive). The web container does not decode this string.
   * 
   * @return a <code>String</code> specifying the real path, or
   *         <code>null</code> if the URL does not have any extra path
   *         information
   */
  String getPathTranslated ();

  /**
   * Returns the query string that is contained in the request URL after the
   * path. This method returns <code>null</code> if the URL does not have a
   * query string. Same as the value of the CGI variable QUERY_STRING.
   * 
   * @return a <code>String</code> containing the query string or
   *         <code>null</code> if the URL contains no query string. The value is
   *         not decoded by the container.
   */
  String getQueryString ();

  /**
   * Returns the fully qualified name of the client or the last proxy that sent
   * the request. If the engine cannot or chooses not to resolve the hostname
   * (to improve performance), this method returns the dotted-string form of the
   * IP address. For HTTP servlets, same as the value of the CGI variable
   * <code>REMOTE_HOST</code>.
   * 
   * @return a <code>String</code> containing the fully qualified name of the
   *         client
   */
  String getRemoteHost ();

  /**
   * Returns the Internet Protocol (IP) address of the client or last proxy that
   * sent the request. For HTTP servlets, same as the value of the CGI variable
   * <code>REMOTE_ADDR</code>.
   * 
   * @return a <code>String</code> containing the IP address of the client that
   *         sent the request
   */
  String getRemoteAddr ();

  /**
   * Returns the name of the authentication scheme used to protect the servlet.
   * All servlet containers support basic, form and client certificate
   * authentication, and may additionally support digest authentication. If the
   * servlet is not authenticated <code>null</code> is returned.
   * <p>
   * Same as the value of the CGI variable AUTH_TYPE.
   * 
   * @return one of the static members BASIC_AUTH, FORM_AUTH, CLIENT_CERT_AUTH,
   *         DIGEST_AUTH (suitable for == comparison) or the container-specific
   *         string indicating the authentication scheme, or <code>null</code>
   *         if the request was not authenticated.
   */
  String getAuthType ();

  /**
   * Returns the login of the user making this request, if the user has been
   * authenticated, or <code>null</code> if the user has not been authenticated.
   * Whether the user name is sent with each subsequent request depends on the
   * browser and type of authentication. Same as the value of the CGI variable
   * REMOTE_USER.
   * 
   * @return a <code>String</code> specifying the login of the user making this
   *         request, or <code>null</code> if the user login is not known
   */
  String getRemoteUser ();

  /**
   * Returns the MIME type of the body of the request, or <code>null</code> if
   * the type is not known. For HTTP servlets, same as the value of the CGI
   * variable CONTENT_TYPE.
   * 
   * @return a <code>String</code> containing the name of the MIME type of the
   *         request, or null if the type is not known
   */
  String getContentType ();

  /**
   * Returns the length, in bytes, of the request body and made available by the
   * input stream, or -1 if the length is not known. For HTTP servlets, same as
   * the value of the CGI variable CONTENT_LENGTH.
   * 
   * @return an integer containing the length of the request body or -1 if the
   *         length is not known
   */
  long getContentLength ();

  /**
   * Returns the part of this request's URL that calls the servlet. This path
   * starts with a "/" character and includes either the servlet name or a path
   * to the servlet, but does not include any extra path information or a query
   * string. Same as the value of the CGI variable SCRIPT_NAME.
   * <p>
   * This method will return an empty string ("") if the servlet used to process
   * this request was matched using the "/*" pattern.
   * 
   * @return a <code>String</code> containing the name or path of the servlet
   *         being called, as specified in the request URL, decoded, or an empty
   *         string if the servlet used to process the request is matched using
   *         the "/*" pattern.
   */
  String getServletPath ();

  /**
   * Returns the current <code>HttpSession</code> associated with this request
   * or, if there is no current session and <code>create</code> is true, returns
   * a new session.
   * <p>
   * If <code>bCreateIfNotExisting</code> is <code>false</code> and the request
   * has no valid <code>HttpSession</code>, this method returns
   * <code>null</code>.
   * <p>
   * To make sure the session is properly maintained, you must call this method
   * before the response is committed. If the container is using cookies to
   * maintain session integrity and is asked to create a new session when the
   * response is committed, an IllegalStateException is thrown.
   * 
   * @param bCreateIfNotExisting
   *        <code>true</code> to create a new session for this request if
   *        necessary; <code>false</code> to return <code>null</code> if there's
   *        no current session
   * @return the <code>HttpSession</code> associated with this request or
   *         <code>null</code> if <code>bCreateIfNotExisting</code> is
   *         <code>false</code> and the request has no valid session
   */
  @Nullable
  HttpSession getSession (boolean bCreateIfNotExisting);

  /**
   * @return Return the absolute server path. E.g. "http://localhost:8080"
   */
  String getFullServerPath ();

  /**
   * @return Return the relative context path. E.g. <code>/context</code> or an
   *         empty string for the root context. Never with a trailing slash.
   */
  String getContextPath ();

  /**
   * @return Return the absolute context path. E.g.
   *         <code>http://localhost:8080/context</code>. Never with a trailing
   *         slash.
   */
  String getFullContextPath ();

  /**
   * @return Return the absolute servlet path. E.g.
   *         <code>/context/config.jsp</code> or <code>/context/action/</code>
   */
  String getContextAndServletPath ();

  /**
   * @return Return the absolute servlet path. E.g.
   *         <code>http://localhost:8080/context/config.jsp</code> or
   *         <code>http://localhost:8080/context/action/</code>
   */
  String getFullContextAndServletPath ();

  /**
   * Get the full URL (incl. protocol) and parameters of the current request.<br>
   * <code>http://hostname.com:81/context/servlet/path/a/b?c=123&amp;d=789</code>
   * 
   * @return The full URL of the current request.
   */
  String getURL ();

  /**
   * Encodes the specified URL by including the session ID in it, or, if
   * encoding is not needed, returns the URL unchanged. The implementation of
   * this method includes the logic to determine whether the session ID needs to
   * be encoded in the URL. For example, if the browser supports cookies, or
   * session tracking is turned off, URL encoding is unnecessary.
   * <p>
   * For robust session tracking, all URLs emitted by a servlet should be run
   * through this method. Otherwise, URL rewriting cannot be used with browsers
   * which do not support cookies.
   * 
   * @param sURL
   *        the url to be encoded.
   * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
   */
  @Nonnull
  String encodeURL (@Nonnull String sURL);

  /**
   * Encodes the specified URL for use in the <code>sendRedirect</code> method
   * or, if encoding is not needed, returns the URL unchanged. The
   * implementation of this method includes the logic to determine whether the
   * session ID needs to be encoded in the URL. Because the rules for making
   * this determination can differ from those used to decide whether to encode a
   * normal link, this method is separated from the <code>encodeURL</code>
   * method.
   * <p>
   * All URLs sent to the <code>HttpServletResponse.sendRedirect</code> method
   * should be run through this method. Otherwise, URL rewriting cannot be used
   * with browsers which do not support cookies.
   * 
   * @param sURL
   *        the url to be encoded.
   * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
   * @see #encodeURL(String)
   */
  @Nonnull
  String encodeRedirectURL (@Nonnull String sURL);

  /**
   * Check if this request uses a Cookie based session handling (meaning cookies
   * are enabled) or whether the session ID needs to be appended to a URL.
   * 
   * @return <code>true</code> if the session ID is passed via cookies.
   */
  boolean areCookiesEnabled ();

  /**
   * Returns the value of the specified request header as a <code>String</code>.
   * If the request did not include a header of the specified name, this method
   * returns <code>null</code>. If there are multiple headers with the same
   * name, this method returns the first head in the request. The header name is
   * case insensitive. You can use this method with any request header.
   * 
   * @param sName
   *        a <code>String</code> specifying the header name
   * @return a <code>String</code> containing the value of the requested header,
   *         or <code>null</code> if the request does not have a header of that
   *         name
   */
  @Nullable
  String getRequestHeader (@Nullable String sName);

  /**
   * Returns all the values of the specified request header as an
   * <code>Enumeration</code> of <code>String</code> objects.
   * <p>
   * Some headers, such as <code>Accept-Language</code> can be sent by clients
   * as several headers each with a different value rather than sending the
   * header as a comma separated list.
   * <p>
   * If the request did not include any headers of the specified name, this
   * method returns an empty <code>Enumeration</code>. The header name is case
   * insensitive. You can use this method with any request header.
   * 
   * @param sName
   *        a <code>String</code> specifying the header name
   * @return an <code>Enumeration</code> containing the values of the requested
   *         header. If the request does not have any headers of that name
   *         return an empty enumeration. If the container does not allow access
   *         to header information, return <code>null</code>
   */
  @Nullable
  Enumeration <String> getRequestHeaders (@Nullable String sName);

  /**
   * Returns an enumeration of all the header names this request contains. If
   * the request has no headers, this method returns an empty enumeration.
   * <p>
   * Some servlet containers do not allow servlets to access headers using this
   * method, in which case this method returns <code>null</code>
   * 
   * @return an enumeration of all the header names sent with this request; if
   *         the request has no headers, an empty enumeration; if the servlet
   *         container does not allow servlets to use this method,
   *         <code>null</code>
   */
  @Nullable
  Enumeration <String> getRequestHeaderNames ();

  /**
   * @return The underlying HTTP servlet request object
   */
  @Nonnull
  HttpServletRequest getRequest ();
}
