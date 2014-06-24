package com.phloc.webbasics.ajax;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.attrs.MapBasedAttributeContainer;
import com.phloc.webbasics.app.layout.ILayoutExecutionContext;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public abstract class AbstractAjaxHandlerWithContext <LECTYPE extends ILayoutExecutionContext> extends AbstractAjaxHandler
{
  @Nonnull
  protected abstract LECTYPE createLayoutExecutionContext (@Nonnull IRequestWebScopeWithoutResponse aRequestScope);

  /**
   * This method must be overridden by every handler
   * 
   * @param aLEC
   *        The lyout execution context. Never <code>null</code>.
   * @return the result object. May not be <code>null</code>
   * @throws Exception
   */
  @OverrideOnDemand
  @Nonnull
  protected abstract IAjaxResponse mainHandleRequest (@Nonnull LECTYPE aLEC) throws Exception;

  @Override
  protected final IAjaxResponse mainHandleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                                   @Nonnull final MapBasedAttributeContainer aParams) throws Exception
  {
    final LECTYPE aLEC = createLayoutExecutionContext (aRequestScope);
    if (aLEC == null)
      throw new IllegalStateException ("Failed to create layout execution context!");
    return mainHandleRequest (aLEC);
  }
}
