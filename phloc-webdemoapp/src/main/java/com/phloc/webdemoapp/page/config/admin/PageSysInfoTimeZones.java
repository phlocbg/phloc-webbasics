package com.phloc.webdemoapp.page.config.admin;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.webpages.info.BasePageSysInfoTimeZones;

public class PageSysInfoTimeZones extends BasePageSysInfoTimeZones
{
  public PageSysInfoTimeZones (@Nonnull @Nonempty final String sID)
  {
    super (sID, "Time Zones");
  }
}
