package com.phloc.webctrls.fineupload;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

/**
 * Test class for class {@link FineUploaderBasic}.
 * 
 * @author helger
 */
public class FineUploaderBasicTest
{
  @Test
  public void testBasic ()
  {
    final FineUploaderBasic aFUB = new FineUploaderBasic (Locale.GERMAN);
    assertTrue (aFUB.getJSCode ().startsWith ("new qq.FileUploader({"));

    aFUB.setDebug (true);
    assertTrue (aFUB.getJSCode ().startsWith ("new qq.FileUploader({\"debug\":true"));
  }
}
