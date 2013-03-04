package com.phloc.appbasics.bmx;

import java.nio.charset.Charset;

import javax.annotation.concurrent.Immutable;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;

@Immutable
public final class CBMXIO
{
  /** The storage encoding of all strings in this table */
  public static final Charset ENCODING = CCharset.CHARSET_UTF_8_OBJ;

  /** Version number of format v1 - must be 4 bytes, all ASCII! */
  public static final String VERSION1 = "BMX1";

  public static final int NODETYPE_CDATA = 0x01;
  public static final int NODETYPE_COMMENT = 0x02;
  public static final int NODETYPE_CONTAINER = 0x03;
  public static final int NODETYPE_DOCUMENT = 0x04;
  public static final int NODETYPE_DOCUMENT_TYPE = 0x05;
  public static final int NODETYPE_ELEMENT = 0x06;
  public static final int NODETYPE_ENTITY_REFERENCE = 0x07;
  public static final int NODETYPE_PROCESSING_INSTRUCTION = 0x08;
  public static final int NODETYPE_TEXT = 0x09;
  public static final int NODETYPE_STRING = 0x0a;
  public static final int SPECIAL_CHILDREN_START = 0x7b;
  public static final int SPECIAL_CHILDREN_END = 0x7d;
  public static final int NODETYPE_EOF = 0xff;

  @PresentForCodeCoverage
  private static final CBMXIO s_aInstance = new CBMXIO ();

  /** The string table index for null strings */
  public static final int INDEX_NULL_STRING = 0;

  private CBMXIO ()
  {}
}
