package com.phloc.webctrls.facebook.xfbml;

import org.junit.Assert;
import org.junit.Test;

import com.phloc.webctrls.facebook.xfbml.FBLike;

public class FBLikeTest
{
  private static final String VALID = "abcdefghijabcdefghijabcdefghijabcdefghij123456789";
  private static final String VALID_PUNCT = "abcdefgh+/=-.:_ijabcdefghijabcdefghijabcdefghij12";
  private static final String INVALID = "a+#()/{}\\`´$\".;,:-_";

  @Test
  public void testCreateRefText ()
  {
    Assert.assertEquals (VALID, FBLike.createRefText (VALID));
    Assert.assertEquals (VALID, FBLike.createRefText (VALID + VALID));
    Assert.assertEquals (VALID_PUNCT, FBLike.createRefText (VALID_PUNCT));
    Assert.assertEquals ("a+/.:-_", FBLike.createRefText (INVALID));
  }

}
