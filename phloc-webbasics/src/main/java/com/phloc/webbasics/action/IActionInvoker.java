package com.phloc.webbasics.action;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.ESuccess;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.web.UnifiedResponse;

public interface IActionInvoker
{
  void setCustomExceptionHandler (@Nullable IActionExceptionHandler aExceptionHandler);

  @Nullable
  IActionExceptionHandler getCustomExceptionHandler ();

  void addAction (@Nonnull String sAction, @Nonnull IActionExecutor aActionExecutor);

  @Nonnull
  ESuccess executeAction (@Nullable String sActionName,
                          @Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                          @Nonnull UnifiedResponse aUnifiedResponse) throws Exception;

  /**
   * @return A map from actionID to action executor. Never <code>null</code>.
   */
  @Nonnull
  @ReturnsMutableCopy
  Map <String, IActionExecutor> getAllActions ();

  /**
   * Check whether an action with the given name is present
   * 
   * @param sName
   *        The name of the action to check. May be <code>null</code>.
   * @return <code>true</code> if an action with the given name is contained,
   *         <code>false</code> otherwise.
   */
  boolean containsAction (@Nullable String sName);

  /**
   * Get the executor associated with the given action.
   * 
   * @param sName
   *        The name of the action to check. May be <code>null</code>.
   * @return <code>null</code> if no such action exists.
   */
  @Nullable
  IActionExecutor getActionExecutor (@Nullable String sName);
}
