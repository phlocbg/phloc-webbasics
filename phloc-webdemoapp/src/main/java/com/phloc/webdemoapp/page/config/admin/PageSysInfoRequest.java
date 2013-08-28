package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.info.BasePageSysInfoRequest;

public class PageSysInfoRequest extends BasePageSysInfoRequest
{
  public PageSysInfoRequest (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Current Request");
  }
}
