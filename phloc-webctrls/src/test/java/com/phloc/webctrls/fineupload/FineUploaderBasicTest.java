package com.phloc.webctrls.fineupload;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test class for class {@link FineUploaderBasic}.
 * 
 * @author helger
 */
public class FineUploaderBasicTest {
  @Test
  public void testBasic () {
    final FineUploaderBasic aFUB = new FineUploaderBasic ();
    assertEquals ("new qq.FileUploader({});", aFUB.getJSCode ());

    aFUB.setDebug (true);
    assertEquals ("new qq.FileUploader({\"debug\":true});", aFUB.getJSCode ());
  }
}
