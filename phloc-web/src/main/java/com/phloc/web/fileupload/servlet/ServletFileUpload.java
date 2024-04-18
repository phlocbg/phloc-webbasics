/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.fileupload.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.web.fileupload.FileUpload;
import com.phloc.web.fileupload.FileUploadException;
import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.fileupload.IFileItemFactory;
import com.phloc.web.fileupload.IFileItemIterator;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.servlet.request.RequestHelper;

/**
 * <p>
 * High level API for processing file uploads.
 * </p>
 * <p>
 * This class handles multiple files per single HTML widget, sent using
 * <code>multipart/mixed</code> encoding type, as specified by <a
 * href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Use
 * {@link #parseRequest(HttpServletRequest)} to acquire a list of
 * {@link com.phloc.web.fileupload.IFileItem}s associated with a given HTML
 * widget.
 * </p>
 * <p>
 * How the data for individual parts is stored is determined by the factory used
 * to create them; a given part may be in memory, on disk, or somewhere else.
 * </p>
 * 
 * @author <a href="mailto:Rafal.Krzewski@e-point.pl">Rafal Krzewski</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @author <a href="mailto:martinc@apache.org">Martin Cooper</a>
 * @author Sean C. Sullivan
 * @version $Id: ServletFileUpload.java 479484 2006-11-27 01:06:53Z jochen $
 */
public class ServletFileUpload extends FileUpload
{
  /**
   * Utility method that determines whether the request contains multipart
   * content.
   * 
   * @param request
   *        The servlet request to be evaluated. Must be non-null.
   * @return <code>true</code> if the request is multipart; <code>false</code>
   *         otherwise.
   */
  public static final boolean isMultipartContent (@Nonnull final HttpServletRequest request)
  {
    if (RequestHelper.getHttpMethod (request) != EHTTPMethod.POST)
      return false;

    final String sContentType = request.getContentType ();
    if (sContentType == null)
      return false;

    return sContentType.toLowerCase (Locale.US).startsWith (MULTIPART);
  }

  /**
   * Constructs an instance of this class which uses the supplied factory to
   * create <code>FileItem</code> instances.
   * 
   * @param fileItemFactory
   *        The factory to use for creating file items.
   */
  public ServletFileUpload (final IFileItemFactory fileItemFactory)
  {
    super (fileItemFactory);
  }

  /**
   * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
   * compliant <code>multipart/form-data</code> stream.
   * 
   * @param request
   *        The servlet request to be parsed.
   * @return A list of <code>FileItem</code> instances parsed from the request,
   *         in the order that they were transmitted.
   * @throws FileUploadException
   *         if there are problems reading/parsing the request or storing files.
   */
  @Nonnull
  @ReturnsMutableCopy
  public List <IFileItem> parseRequest (@Nonnull final HttpServletRequest request) throws FileUploadException
  {
    return parseRequest (new ServletRequestContext (request));
  }

  /**
   * Processes an <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>
   * compliant <code>multipart/form-data</code> stream.
   * 
   * @param request
   *        The servlet request to be parsed.
   * @return An iterator to instances of <code>FileItemStream</code> parsed from
   *         the request, in the order that they were transmitted.
   * @throws FileUploadException
   *         if there are problems reading/parsing the request or storing files.
   * @throws IOException
   *         An I/O error occurred. This may be a network error while
   *         communicating with the client or a problem while storing the
   *         uploaded content.
   */
  @Nonnull
  public IFileItemIterator getItemIterator (@Nonnull final HttpServletRequest request) throws FileUploadException,
                                                                                      IOException
  {
    return super.getItemIterator (new ServletRequestContext (request));
  }
}
