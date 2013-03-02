package com.phloc.webscopes.servlets;

import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.mime.CMimeType;
import com.phloc.web.http.EHTTPMethod;
import com.phloc.web.servlet.response.UnifiedResponse;
import com.phloc.webscopes.domain.IRequestWebScopeWithoutResponse;

public final class MockUnifiedResponseServlet extends AbstractUnifiedResponseServlet
{
  @Override
  @Nonnull
  protected Set <EHTTPMethod> getAllowedHTTPMethods ()
  {
    return EnumSet.allOf (EHTTPMethod.class);
  }

  @Override
  protected void handleRequest (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                                @Nonnull final UnifiedResponse aUnifiedResponse) throws Exception
  {
    aUnifiedResponse.setContentAndCharset ("mock", CCharset.CHARSET_ISO_8859_1_OBJ).setMimeType (CMimeType.TEXT_PLAIN);
  }
}
