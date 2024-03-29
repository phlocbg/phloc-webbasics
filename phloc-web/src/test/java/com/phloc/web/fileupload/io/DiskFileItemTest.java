/**
 * Copyright (C) 2006-2015 phloc systems
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
package com.phloc.web.fileupload.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.phloc.web.fileupload.IFileItem;
import com.phloc.web.fileupload.IFileItemFactory;

/**
 * Serialization Unit tests for {@link DiskFileItem}.
 */
public final class DiskFileItemTest
{
  /**
   * Content type for regular form items.
   */
  private static final String textContentType = "text/plain";

  /**
   * Very low threshold for testing memory versus disk options.
   */
  private static final int threshold = 16;

  /**
   * Test creation of a field for which the amount of data falls below the
   * configured threshold.
   */
  @Test
  public void testBelowThreshold ()
  {

    // Create the FileItem
    final byte [] testFieldValueBytes = _createContentBytes (threshold - 1);
    final IFileItem item = _createFileItem (testFieldValueBytes);

    // Check state is as expected
    assertTrue ("Initial: in memory", item.isInMemory ());
    assertEquals ("Initial: size", item.getSize (), testFieldValueBytes.length);
    _compareBytes ("Initial", item.get (), testFieldValueBytes);

    // Serialize & Deserialize
    try
    {
      final IFileItem newItem = (IFileItem) _serializeDeserialize (item);

      // Test deserialized content is as expected
      assertTrue ("Check in memory", newItem.isInMemory ());
      _compareBytes ("Check", testFieldValueBytes, newItem.get ());

      // Compare FileItem's (except byte[])
      _compareFileItems (item, newItem);

    }
    catch (final Exception e)
    {
      fail ("Error Serializing/Deserializing: " + e);
    }

  }

  /**
   * Test creation of a field for which the amount of data equals the configured
   * threshold.
   */
  @Test
  public void testThreshold ()
  {
    // Create the FileItem
    final byte [] testFieldValueBytes = _createContentBytes (threshold);
    final IFileItem item = _createFileItem (testFieldValueBytes);

    // Check state is as expected
    assertTrue ("Initial: in memory", item.isInMemory ());
    assertEquals ("Initial: size", item.getSize (), testFieldValueBytes.length);
    _compareBytes ("Initial", item.get (), testFieldValueBytes);

    // Serialize & Deserialize
    try
    {
      final IFileItem newItem = (IFileItem) _serializeDeserialize (item);

      // Test deserialized content is as expected
      assertTrue ("Check in memory", newItem.isInMemory ());
      _compareBytes ("Check", testFieldValueBytes, newItem.get ());

      // Compare FileItem's (except byte[])
      _compareFileItems (item, newItem);

    }
    catch (final Exception e)
    {
      fail ("Error Serializing/Deserializing: " + e);
    }
  }

  /**
   * Test creation of a field for which the amount of data falls above the
   * configured threshold.
   */
  @Test
  public void testAboveThreshold ()
  {

    // Create the FileItem
    final byte [] testFieldValueBytes = _createContentBytes (threshold + 1);
    final IFileItem item = _createFileItem (testFieldValueBytes);

    // Check state is as expected
    assertFalse ("Initial: in memory", item.isInMemory ());
    assertEquals ("Initial: size", item.getSize (), testFieldValueBytes.length);
    _compareBytes ("Initial", item.get (), testFieldValueBytes);

    // Serialize & Deserialize
    try
    {
      final IFileItem newItem = (IFileItem) _serializeDeserialize (item);

      // Test deserialized content is as expected
      assertFalse ("Check in memory", newItem.isInMemory ());
      _compareBytes ("Check", testFieldValueBytes, newItem.get ());

      // Compare FileItem's (except byte[])
      _compareFileItems (item, newItem);

    }
    catch (final Exception e)
    {
      fail ("Error Serializing/Deserializing: " + e);
    }
  }

  /**
   * Verify that only the file name, not the path is contained in the toString
   * representation
   */
  @Test
  public void testToString ()
  {
    // Create the FileItem
    final byte [] testFieldValueBytes = _createContentBytes (threshold + 1);
    final IFileItem item = _createFileItem (testFieldValueBytes);
    final String sPath = ((DiskFileItem) item).getStoreLocation ().getAbsolutePath ();
    assertFalse (item.toString ().contains (sPath));
  }

  /**
   * Compare FileItem's (except the byte[] content)
   */
  private void _compareFileItems (final IFileItem origItem, final IFileItem newItem)
  {
    assertTrue ("Compare: is in Memory", origItem.isInMemory () == newItem.isInMemory ());
    assertTrue ("Compare: is Form Field", origItem.isFormField () == newItem.isFormField ());
    assertEquals ("Compare: Field Name", origItem.getFieldName (), newItem.getFieldName ());
    assertEquals ("Compare: Content Type", origItem.getContentType (), newItem.getContentType ());
    assertEquals ("Compare: File Name", origItem.getName (), newItem.getName ());
  }

  /**
   * Compare content bytes.
   */
  private void _compareBytes (final String text, final byte [] origBytes, final byte [] newBytes)
  {
    assertNotNull (origBytes);
    assertNotNull (newBytes);
    assertEquals (text + " byte[] length", origBytes.length, newBytes.length);
    for (int i = 0; i < origBytes.length; i++)
    {
      assertEquals (text + " byte[" + i + "]", origBytes[i], newBytes[i]);
    }
  }

  /**
   * Create content bytes of a specified size.
   */
  private byte [] _createContentBytes (final int size)
  {
    final StringBuffer buffer = new StringBuffer (size);
    byte count = 0;
    for (int i = 0; i < size; i++)
    {
      buffer.append (count + "");
      count++;
      if (count > 9)
      {
        count = 0;
      }
    }
    return buffer.toString ().getBytes ();
  }

  /**
   * Create a FileItem with the specfied content bytes.
   */
  private IFileItem _createFileItem (final byte [] contentBytes)
  {
    final IFileItemFactory factory = new DiskFileItemFactory (threshold);
    final String textFieldName = "textField";

    final IFileItem item = factory.createItem (textFieldName, textContentType, true, "My File Name");
    try
    {
      final OutputStream os = item.getOutputStream ();
      os.write (contentBytes);
      os.close ();
    }
    catch (final IOException e)
    {
      fail ("Unexpected IOException" + e);
    }

    return item;

  }

  /**
   * Do serialization and deserialization.
   */
  private Object _serializeDeserialize (final Object target)
  {

    // Serialize the test object
    final ByteArrayOutputStream baos = new ByteArrayOutputStream ();
    try
    {
      final ObjectOutputStream oos = new ObjectOutputStream (baos);
      oos.writeObject (target);
      oos.flush ();
      oos.close ();
    }
    catch (final Exception e)
    {
      fail ("Exception during serialization: " + e);
    }

    // Deserialize the test object
    Object result = null;
    try
    {
      final ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
      final ObjectInputStream ois = new ObjectInputStream (bais);
      result = ois.readObject ();
      bais.close ();
    }
    catch (final Exception e)
    {
      fail ("Exception during deserialization: " + e);
    }
    return result;

  }

}
