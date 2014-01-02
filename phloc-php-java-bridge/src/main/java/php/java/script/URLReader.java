/**
 * Copyright (C) 2006-2014 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.script;

/*
 * Copyright (C) 2003-2007 Jost Boekemeier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER(S) OR AUTHOR(S) BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import php.java.bridge.Util;
import php.java.bridge.http.AbstractHeaderParser;

/**
 * This class can be used to connect to a HTTP server to allocate and to invoke
 * php scripts. Example:
 * <p>
 * <code>
 * ScriptEngine e = new ScriptEngineManager().getEngineByName("php-invocable");<br>
 * e.eval(new URLReader(new URL("http://localhost:80/JavaProxy.php"));<br>
 * System.out.println(((Invocable)e).invoke("java_get_server_name", new Object[]{}));<br>
 * ((Closeable)e).close();<br>
 * </code>
 * 
 * @author jostb
 */
public class URLReader extends Reader implements IScriptReader
{

  private URL url;
  private final HttpURLConnection conn;

  private HostnameVerifier hostNameVerifier;

  private HostnameVerifier getHostNameVerifier ()
  {
    if (hostNameVerifier != null)
      return hostNameVerifier;
    return hostNameVerifier = new HostnameVerifier ()
    {
      public boolean verify (final String arg0, final SSLSession arg1)
      {
        return true;
      }
    };
  }

  private SSLSocketFactory sslSocketFactory;

  private SSLSocketFactory getSslSocketFactory () throws NoSuchAlgorithmException, KeyManagementException
  {
    if (sslSocketFactory != null)
      return sslSocketFactory;

    final X509TrustManager tm = new X509TrustManager ()
    {
      public void checkClientTrusted (final X509Certificate [] arg0, final String arg1) throws CertificateException
      { /* ignore */}

      public void checkServerTrusted (final X509Certificate [] arg0, final String arg1) throws CertificateException
      { /* ignore */}

      public X509Certificate [] getAcceptedIssuers ()
      {
        return null;
      }
    };
    final KeyManager [] km = null;
    final X509TrustManager [] tma = new X509TrustManager [] { tm };
    SSLContext sslContext = null;
    sslContext = SSLContext.getInstance ("TLS");
    sslContext.init (km, tma, new java.security.SecureRandom ());
    return sslSocketFactory = sslContext.getSocketFactory ();
  }

  /**
   * Create a special reader which can be used to read data from a URL.
   * 
   * @param url
   * @throws IOException
   * @throws UnknownHostException
   */
  public URLReader (final URL url) throws UnknownHostException, IOException
  {
    this.url = url;
    this.conn = (HttpURLConnection) url.openConnection ();
    if (this.conn instanceof HttpsURLConnection)
    {
      allowSelfSignedCertificates ();
    }

    this.conn.setDoInput (true);
    conn.setRequestMethod ("GET");
  }

  /**
   * Create a special reader which can be used to read data from a URL. Example: <br>
   * <blockquote> <code>
   * URL url = new URL("http://....");
   * HttpURLConnection conn = (HttpURLConnection)url.openConnection();
   * conn.setDoInput(true);
   * conn.setRequestMethod("GET");
   * conn.setRequestProperty ("SOME_VAR", SOME_VAL);
   * scriptEngine.eval(new URLReader(conn));
   * ((Invocable)scriptEngine).invokeFunction(...);
   * ((Closeable)scriptEngine).close();
   * </code> </blockquote>
   * 
   * @param conn
   *        the URL connection
   */
  public URLReader (final HttpURLConnection conn)
  {
    this.conn = conn;
  }

  private void allowSelfSignedCertificates ()
  {
    final HttpsURLConnection xcon = (HttpsURLConnection) this.conn;
    try
    {
      xcon.setSSLSocketFactory (getSslSocketFactory ());
    }
    catch (final KeyManagementException e)
    {
      Util.printStackTrace (e);
    }
    catch (final NoSuchAlgorithmException e)
    {
      Util.printStackTrace (e);
    }
    xcon.setHostnameVerifier (getHostNameVerifier ());
  }

  /**
   * Returns the URL to which this reader connects.
   * 
   * @return the URL.
   */
  public URL getURL ()
  {
    return url;
  }

  /** {@inheritDoc} */
  @Override
  public int read (final char [] cbuf, final int off, final int len) throws IOException
  {
    throw new IllegalStateException ("Use urlReader.read(Hashtable, OutputStream) or use a FileReader() instead.");
  }

  private void appendListValues (final StringBuilder buf, final List <String> list)
  {
    for (final Iterator <String> ii = list.iterator (); ii.hasNext ();)
    {
      buf.append (ii.next ());
      if (ii.hasNext ())
        buf.append ("; ");
    }
  }

  /*
   * (non-Javadoc)
   * @see php.java.script.IScriptReader#read(java.util.Map,
   * java.io.OutputStream, php.java.bridge.Util.HeaderParser)
   */
  public void read (final Map <String, String> env, final OutputStream out, final AbstractHeaderParser headerParser) throws IOException
  {
    InputStream natIn = null;

    try
    {

      final byte [] buf = new byte [Util.BUF_SIZE];

      for (final String key : IScriptReader.HEADER)
      {
        final String val = env.get (key);
        if (val != null)
          conn.setRequestProperty (key, val);
      }

      final String overrideHosts = env.get (Util.X_JAVABRIDGE_OVERRIDE_HOSTS);
      if (overrideHosts != null)
      {
        conn.setRequestProperty (Util.X_JAVABRIDGE_OVERRIDE_HOSTS, overrideHosts);
        // workaround for a problem in php (it confuses the OVERRIDE_HOSTS from
        // the environment with OVERRIDE_HOSTS from the request meta-data
        conn.setRequestProperty (Util.X_JAVABRIDGE_OVERRIDE_HOSTS_REDIRECT, overrideHosts);
      }
      natIn = conn.getInputStream ();
      if (headerParser != AbstractHeaderParser.DEFAULT_HEADER_PARSER)
      {
        final StringBuilder sbuf = new StringBuilder ();
        for (final Map.Entry <String, List <String>> element : conn.getHeaderFields ().entrySet ())
        {
          final List <String> list = element.getValue ();
          if (list.size () == 1)
          {
            headerParser.addHeader (element.getKey (), String.valueOf (list.get (0)));
          }
          else
          {
            appendListValues (sbuf, list);
            headerParser.addHeader (element.getKey (), sbuf.toString ());
            sbuf.setLength (0);
          }
        }
      }
      int count;
      while ((count = natIn.read (buf)) > 0)
        out.write (buf, 0, count);
    }
    catch (final IOException x)
    {
      Util.printStackTrace (x);
      throw x;
    }
    finally
    {
      if (natIn != null)
        try
        {
          natIn.close ();
        }
        catch (final IOException e)
        {/* ignore */}
    }
  }

  /** {@inheritDoc} */
  @Override
  public void close () throws IOException
  {}

  /** {@inheritDoc} */
  @Override
  public String toString ()
  {
    return String.valueOf (url);
  }
}
