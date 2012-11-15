package com.phloc.webbasics.web;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.lang.EnumHelper;

/**
 * Determine the response stream type to be used.
 * 
 * @author philip
 */
public enum EResponseStreamType implements IHasID <String> {
  PLAIN ("plain"),
  GZIP ("gzip"),
  DEFLATE ("deflate");

  private final String m_sID;

  private EResponseStreamType (@Nonnull @Nonempty final String sID) {
    m_sID = sID;
  }

  @Nonnull
  @Nonempty
  public String getID () {
    return m_sID;
  }

  /**
   * @return <code>true</code> if the response stream type is uncompressed.
   */
  public boolean isUncompressed () {
    return this == PLAIN;
  }

  /**
   * @return <code>true</code> if the response stream type is compressed.
   */
  public boolean isCompressed () {
    return this == GZIP || this == DEFLATE;
  }

  @Nullable
  public static EResponseStreamType getFromIDOrNull (@Nullable final String sID) {
    return EnumHelper.getFromIDOrNull (EResponseStreamType.class, sID);
  }
}
