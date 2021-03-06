package com.phloc.webscopes.impl;

import java.util.List;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.lang.ServiceLoaderUtils;
import com.phloc.webscopes.IRequestWebScopeInitializer;
import com.phloc.webscopes.domain.IRequestWebScope;

public class RequestInitializerHandler
{
  private static final class SingletonHolder
  {
    /**
     * The singleton instance
     */
    public static final RequestInitializerHandler INSTANCE = new RequestInitializerHandler ();
  }

  /**
   * Ctor for singleton creation
   */
  protected RequestInitializerHandler ()
  {
    this.m_aCustomInitializers = ContainerHelper.newList (ServiceLoaderUtils.getAllSPIImplementations (IRequestWebScopeInitializer.class));
  }

  private final List <IRequestWebScopeInitializer> m_aCustomInitializers;

  /**
   * Ctor
   * 
   * @return the singleton instance
   */
  public static RequestInitializerHandler getInstance ()
  {
    return SingletonHolder.INSTANCE;
  }

  public void initRequestScope (final IRequestWebScope aRequestScope)
  {
    for (final IRequestWebScopeInitializer aCustomInitializer : this.m_aCustomInitializers)
    {
      aCustomInitializer.initRequestWebScope (aRequestScope);
    }
  }
}
