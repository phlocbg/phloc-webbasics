/**
 * Copyright (C) 2006-2014 phloc systems
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
package com.phloc.fileupload.handler;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.ValueEnforcer;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SMap;
import com.phloc.fileupload.IUploadPostProcessor;
import com.phloc.html.js.builder.JSAnonymousFunction;

/**
 * This class encapsulates context information for a certain type of upload. It
 * contains information about the target upload directory as well as an optional
 * upload post processor for performing tasks upon the uploaded file.
 * 
 * @author Boris Gregorcic
 */
public class UploadContext implements Serializable
{
  private static final long serialVersionUID = -3815678526665140941L;

  private File m_aUploadDirectory;
  private final IUploadPostProcessor m_aPostProcessor;
  private final ISimpleURL m_aUploadServletURL;
  private final ISimpleURL m_aSelfURL;
  private JSAnonymousFunction m_aUploadCompleteCallback;
  private String m_sTargetFileName;
  private IUploadFilenameFilter m_aFilenameFilter;
  private IUploadFileSizeFilter m_aFileSizeFilter;
  private IUploadMimeTypeFilter m_aMimeTypeFilter;
  private final SMap m_aProperties = new SMap ();
  private final ReentrantReadWriteLock m_aLock = new ReentrantReadWriteLock ();

  public UploadContext (@Nonnull final File aUploadDirectory,
                        @Nonnull final ISimpleURL aUploadServletURL,
                        @Nonnull final ISimpleURL aSelfURL)
  {
    this (aUploadDirectory, null, aUploadServletURL, aSelfURL);
  }

  public UploadContext (@Nonnull final File aUploadDirectory,
                        @Nullable final IUploadPostProcessor aPostProcessor,
                        @Nonnull final ISimpleURL aUploadServletURL,
                        @Nonnull final ISimpleURL aSelfURL)
  {
    ValueEnforcer.notNull (aUploadServletURL, "UploadServletURL"); //$NON-NLS-1$
    ValueEnforcer.notNull (aSelfURL, "SelfURL"); //$NON-NLS-1$

    setUploadDirectory (aUploadDirectory);
    m_aPostProcessor = aPostProcessor;
    m_aUploadServletURL = aUploadServletURL;
    m_aSelfURL = aSelfURL;
  }

  public void setProperty (final String sKey, final String sValue)
  {
    m_aLock.writeLock ().lock ();
    try
    {
      m_aProperties.add (sKey, sValue);
    }
    finally
    {
      m_aLock.writeLock ().unlock ();
    }
  }

  public String getProperty (final String sKey)
  {
    m_aLock.readLock ().lock ();
    try
    {
      return m_aProperties.get (sKey);
    }
    finally
    {
      m_aLock.readLock ().unlock ();
    }
  }

  @Nonnull
  @ReturnsMutableCopy
  public SMap getProperties ()
  {
    m_aLock.readLock ().lock ();
    try
    {
      return m_aProperties.getClone ();
    }
    finally
    {
      m_aLock.readLock ().unlock ();
    }
  }

  /**
   * @return The upload directory to be used for this upload.
   */
  @Nonnull
  public File getUploadDirectory ()
  {
    return m_aUploadDirectory;
  }

  public void setUploadDirectory (@Nonnull final File aUploadDirectory)
  {
    ValueEnforcer.notNull (aUploadDirectory, "UploadDirectory"); //$NON-NLS-1$
    m_aUploadDirectory = aUploadDirectory;
  }

  @Nullable
  public String getTargetFileName ()
  {
    return m_sTargetFileName;
  }

  public void setTargetFileName (final String sTargetFileName)
  {
    m_sTargetFileName = sTargetFileName;
  }

  @Nullable
  public IUploadFilenameFilter getFilenameFilter ()
  {
    return m_aFilenameFilter;
  }

  @Nullable
  public IUploadFileSizeFilter getFileSizeFilter ()
  {
    return m_aFileSizeFilter;
  }

  public void setFilenameFilter (@Nullable final IUploadFilenameFilter aFilenameFilter)
  {
    m_aFilenameFilter = aFilenameFilter;
  }

  public void setFileSizeFilter (@Nullable final IUploadFileSizeFilter aFileSizeFilter)
  {
    m_aFileSizeFilter = aFileSizeFilter;
  }

  @Nullable
  public IUploadMimeTypeFilter getMimeTypeFilter ()
  {
    return m_aMimeTypeFilter;
  }

  public void setMimeTypeFilter (@Nullable final IUploadMimeTypeFilter aFilter)
  {
    m_aMimeTypeFilter = aFilter;
  }

  /**
   * @return The post-processor declared for this upload. May be
   *         <code>null</code>
   */
  @Nullable
  public IUploadPostProcessor getPostProcessor ()
  {
    return m_aPostProcessor;
  }

  public ISimpleURL getUploadServletURL ()
  {
    return m_aUploadServletURL;
  }

  public ISimpleURL getSelfURL ()
  {
    return m_aSelfURL;
  }

  /**
   * Add an optional callback function which is called when the upload is
   * successfully completed. The function will be called with the arguments
   * <code>sFileUploadID</code> and <code>sFileName</code>. The callback must
   * return <code>true</code> if you want the form to be reset and shown again
   * (successive upload), otherwise the success message will stay. In case of an
   * error, the method is called without any parameters (see fileupload-x.y.js)
   * 
   * @param aUploadCompleteCallback
   */
  public void setUploadCompleteCallback (@Nullable final JSAnonymousFunction aUploadCompleteCallback)
  {
    m_aUploadCompleteCallback = aUploadCompleteCallback;
  }

  @Nullable
  public JSAnonymousFunction getUploadCompleteCallback ()
  {
    return m_aUploadCompleteCallback;
  }
}
