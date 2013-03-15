package com.phloc.web.http.digestauth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.phloc.commons.state.ETriState;
import com.phloc.commons.url.SimpleURL;

/**
 * Test class for class {@link DigestAuthResponseBuilder}.
 * 
 * @author philip
 */
public final class DigestAuthResponseBuilderTest
{
  @Test
  public void testBasic ()
  {
    final DigestAuthResponseBuilder b = new DigestAuthResponseBuilder ();
    try
    {
      // Mandatory realm not present
      b.build ();
      fail ();
    }
    catch (final IllegalStateException ex)
    {}
    b.setRealm ("xyz");
    assertEquals ("Digest realm=\"xyz\"", b.build ());
    b.addDomain (new SimpleURL ("/config"));
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\"", b.build ());
    b.setNonce ("blanonce");
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\"", b.build ());
    b.setOpaque ("opaque");
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\", opaque=\"opaque\"", b.build ());
    b.setStale (ETriState.FALSE);
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\", opaque=\"opaque\", stale=false",
                  b.build ());
    b.setAlgorithm (DigestAuthResponseBuilder.ALGORITHM_MD5_SESS);
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\", opaque=\"opaque\", stale=false, algorithm=MD5-sess",
                  b.build ());
    b.addQOP (DigestAuthResponseBuilder.QOP_AUTH);
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\", opaque=\"opaque\", stale=false, algorithm=MD5-sess, qop=\"auth\"",
                  b.build ());
    b.addQOP (DigestAuthResponseBuilder.QOP_AUTH_INT);
    assertEquals ("Digest realm=\"xyz\", domain=\"/config\", nonce=\"blanonce\", opaque=\"opaque\", stale=false, algorithm=MD5-sess, qop=\"auth,auth-int\"",
                  b.build ());
  }
}
