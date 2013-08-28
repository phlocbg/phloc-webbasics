package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.info.BasePageSysInfoThreads;

public class PageSysInfoThreads extends BasePageSysInfoThreads
{
  public PageSysInfoThreads (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Threads");
  }
}
