package com.phloc.webbasics.app.layout;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.html.hc.IHCNode;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;

public interface ILayoutManager
{
  void registerAreaContentProvider (@Nonnull String sAreaID, @Nonnull ILayoutAreaContentProvider aContentProvider);

  @Nonnull
  @ReturnsMutableCopy
  List <String> getAllAreaIDs ();

  @Nullable
  IHCNode getContentOfArea (@Nonnull IRequestWebScopeWithoutResponse aRequestScope,
                            @Nonnull String sAreaID,
                            @Nonnull Locale aDisplayLocale);
}
