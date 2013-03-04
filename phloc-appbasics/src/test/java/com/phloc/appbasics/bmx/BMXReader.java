package com.phloc.appbasics.bmx;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.NonBlockingStack;
import com.phloc.commons.io.file.FileUtils;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroNode;
import com.phloc.commons.microdom.impl.MicroDocument;

public final class BMXReader
{
  @PresentForCodeCoverage
  private static final BMXReader s_aInstance = new BMXReader ();

  private BMXReader ()
  {}

  @Nullable
  public static IMicroNode readFromFile (@Nonnull final File aFile)
  {
    if (aFile == null)
      throw new NullPointerException ("file");

    final FileInputStream aFIS = FileUtils.getInputStream (aFile);
    if (aFIS == null)
      return null;

    return readFromStream (aFIS);
  }

  @Nullable
  public static IMicroNode readFromStream (@Nonnull @WillClose final InputStream aIS)
  {
    if (aIS == null)
      throw new NullPointerException ("inputStream");

    // Wrap the passed input stream in a buffered input stream
    InputStream aISToUse;
    if (aIS instanceof BufferedInputStream)
      aISToUse = aIS;
    else
      aISToUse = new BufferedInputStream (aIS);

    try
    {
      final DataInputStream aDIS = new DataInputStream (aISToUse);

      // Read version
      final byte [] aVersion = new byte [4];
      aDIS.readFully (aVersion);
      final String sVersion = new String (aVersion, CCharset.CHARSET_ISO_8859_1_OBJ);
      if (!sVersion.equals (CBMXIO.VERSION1))
        throw new BMXReadException ("This is not a BMX file!");

      // Read settings
      final int nSettings = aDIS.readInt ();
      final BMXSettings aSettings = BMXSettings.createFromStorageValue (nSettings);

      // Start iterating the main content
      IMicroNode aResultNode = null;
      final NonBlockingStack <IMicroNode> aNodeStack = new NonBlockingStack <IMicroNode> ();
      IMicroNode aLastNode = null;

      int nNodeType;
      while ((nNodeType = aDIS.readByte () & 0xff) != CBMXIO.NODETYPE_EOF)
      {
        IMicroNode aCreatedNode = null;
        switch (nNodeType)
        {
          case CBMXIO.NODETYPE_DOCUMENT:
            aCreatedNode = new MicroDocument ();
            break;
          case CBMXIO.SPECIAL_CHILDREN_START:
            aNodeStack.push (aLastNode);
            break;
          case CBMXIO.SPECIAL_CHILDREN_END:
            aNodeStack.pop ();
            break;
          default:
            throw new BMXReadException ("Unsupported node type " + nNodeType);
        }

        if (aCreatedNode != null)
        {
          if (aResultNode == null)
            aResultNode = aCreatedNode;
          aLastNode = aCreatedNode;
        }
      }
      return aResultNode;
    }
    catch (final IOException ex)
    {
      throw new BMXReadException ("Failed to read from InputStream", ex);
    }
    finally
    {
      StreamUtils.close (aISToUse);
    }
  }
}
