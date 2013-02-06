package com.phloc.appbasics.security.audit;

import java.util.List;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.state.EChange;
import com.phloc.commons.state.ESuccess;

public interface IAuditManager extends IAuditor
{
  @Nonnull
  EChange createAuditItem (@Nonnull EAuditActionType eType, @Nonnull ESuccess eSuccess, @Nonnull String sAction);

  @Nonnull
  @ReturnsMutableCopy
  List <IAuditItem> getAllAuditItems ();
}
