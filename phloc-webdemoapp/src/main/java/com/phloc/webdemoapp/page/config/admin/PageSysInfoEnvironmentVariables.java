package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.info.BasePageSysInfoEnvironmentVariables;

public class PageSysInfoEnvironmentVariables extends BasePageSysInfoEnvironmentVariables
{
  public PageSysInfoEnvironmentVariables (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Environment Variables");
  }
}
