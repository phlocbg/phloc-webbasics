package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.info.BasePageSysInfoSystemProperties;

public class PageSysInfoSystemProperties extends BasePageSysInfoSystemProperties
{
  public PageSysInfoSystemProperties (@Nonnull @Nonempty final String sID)
  {
    super (sID, "System Properties");
  }
}
