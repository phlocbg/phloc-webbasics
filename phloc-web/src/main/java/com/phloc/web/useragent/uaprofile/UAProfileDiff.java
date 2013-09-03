/**
 * Copyright (C) 2006-2013 phloc systems
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
package com.phloc.web.useragent.uaprofile;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.base64.Base64;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.messagedigest.EMessageDigestAlgorithm;
import com.phloc.commons.messagedigest.MessageDigestGeneratorHelper;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.serialize.MicroReader;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;

/**
 * Represents a single UA profile diff.
 * 
 * @author Philip Helger
 */
@Immutable
public class UAProfileDiff
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (UAProfileDiff.class);

  private final String m_sData;
  private final byte [] m_aMD5Digest;

  // State variables
  private final IMicroDocument m_aDocument;

  public UAProfileDiff (@Nonnull @Nonempty final String sData, @Nullable final byte [] aMD5Digest)
  {
    if (StringHelper.hasNoText ("data"))
      throw new IllegalArgumentException ("profileDiffData");
    if (aMD5Digest != null && aMD5Digest.length != 16)
      throw new IllegalArgumentException ("invalid MD5 digest length!");

    m_sData = sData;
    m_aMD5Digest = ArrayHelper.getCopy (aMD5Digest);

    if (m_aMD5Digest != null)
    {
      // Verify MD5 digest
      final byte [] aCalcedDigest = MessageDigestGeneratorHelper.getDigest (EMessageDigestAlgorithm.MD5,
                                                                            sData,
                                                                            CCharset.CHARSET_UTF_8_OBJ);
      if (!Arrays.equals (m_aMD5Digest, aCalcedDigest))
        s_aLogger.warn ("MD5 digest mismatch of profile diff data! Expected '" +
                        Base64.encodeBytes (aCalcedDigest) +
                        "' but have '" +
                        Base64.encodeBytes (m_aMD5Digest) +
                        "'");
    }

    m_aDocument = MicroReader.readMicroXML (sData);
    if (m_aDocument == null)
      s_aLogger.warn ("Failed to parse profile diff data as XML '" + sData + "'");
  }

  @Nonnull
  @Nonempty
  public String getData ()
  {
    return m_sData;
  }

  /**
   * @return A copy of the MD5 digest data or <code>null</code> if non was
   *         provided.
   */
  @Nullable
  @ReturnsMutableCopy
  public byte [] getMD5Digest ()
  {
    return ArrayHelper.getCopy (m_aMD5Digest);
  }

  /**
   * @return The parsed XML document or <code>null</code> in parsing failed.
   */
  @Nullable
  public IMicroDocument getDocument ()
  {
    return m_aDocument;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof UAProfileDiff))
      return false;
    final UAProfileDiff rhs = (UAProfileDiff) o;
    return m_sData.equals (rhs.m_sData) && EqualsUtils.equals (m_aMD5Digest, rhs.m_aMD5Digest);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sData).append (m_aMD5Digest).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("data", m_sData)
                                       .appendIfNotNull ("digest", m_aMD5Digest)
                                       .appendIfNotNull ("document", m_aDocument)
                                       .toString ();
  }
}
