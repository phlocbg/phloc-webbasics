package com.phloc.web.servlet.response;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.concurrent.ThreadSafe;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.collections.LockedContainerHelper;

@ThreadSafe
public class HeaderSecuritySettings
{
  private final ReentrantReadWriteLock m_aLock = new ReentrantReadWriteLock ();
  private final List <String> m_aSensitiveHeaders = ContainerHelper.newList ();
  private final AtomicBoolean m_bLogRequestHeadersOnError = new AtomicBoolean (true);
  private final AtomicBoolean m_bLogResponseHeadersOnError = new AtomicBoolean (true);

  public boolean isLogRequestHeadersOnError ()
  {
    return this.m_bLogRequestHeadersOnError.get ();
  }

  public void setLogRequestHeadersOnError (final boolean bLog)
  {
    this.m_bLogRequestHeadersOnError.set (bLog);
  }

  public boolean isLogResponseHeadersOnError ()
  {
    return this.m_bLogResponseHeadersOnError.get ();
  }

  public void setLogResponseHeadersOnError (final boolean bLog)
  {
    this.m_bLogResponseHeadersOnError.set (bLog);
  }

  public void addSensitiveHeader (final String sHeader)
  {
    LockedContainerHelper.add (this.m_aSensitiveHeaders, sHeader, this.m_aLock);
  }

  public boolean isSensitiveHeader (final String sHeader)
  {
    return LockedContainerHelper.contains (this.m_aSensitiveHeaders, sHeader, this.m_aLock);
  }

  public List <String> getSensitiveHeaders ()
  {
    return LockedContainerHelper.getList (this.m_aSensitiveHeaders, this.m_aLock);
  }

  public void clear ()
  {
    this.m_aLock.writeLock ().lock ();
    try
    {
      this.m_aSensitiveHeaders.clear ();
      this.m_bLogRequestHeadersOnError.set (true);
      this.m_bLogResponseHeadersOnError.set (true);
    }
    finally
    {
      this.m_aLock.writeLock ().unlock ();
    }
  }
}
