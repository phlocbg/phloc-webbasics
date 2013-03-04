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
package com.phloc.appbasics.exchange.bulkexport.format;

import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;

import com.phloc.appbasics.exchange.EExchangeFileType;
import com.phloc.appbasics.exchange.bulkexport.IExportRecord;
import com.phloc.appbasics.exchange.bulkexport.IExportRecordField;
import com.phloc.appbasics.exchange.bulkexport.IExportRecordProvider;
import com.phloc.appbasics.exchange.bulkexport.IExporterFile;
import com.phloc.commons.collections.iterate.IterableIterator;
import com.phloc.commons.io.streams.StreamUtils;
import com.phloc.commons.microdom.IMicroDocument;
import com.phloc.commons.microdom.IMicroElement;
import com.phloc.commons.microdom.impl.MicroDocument;
import com.phloc.commons.microdom.serialize.MicroWriter;
import com.phloc.commons.state.ESuccess;
import com.phloc.commons.typeconvert.TypeConverter;
import com.phloc.commons.xml.serialize.XMLWriterSettings;

/**
 * Implementation of {@link IExporterFile} for XML files
 * 
 * @author philip
 */
public final class ExporterXML implements IExporterFile
{
  private static final String ELEMENT_ROOT = "root";
  private static final String ELEMENT_HEADER = "header";
  private static final String ELEMENT_BODY = "body";
  private static final String ELEMENT_ROW = "row";
  private static final String ELEMENT_FOOTER = "footer";
  private static final String ELEMENT_FIELD = "field";
  private static final String ATTR_TYPE = "type";

  private Charset m_aCharset = XMLWriterSettings.DEFAULT_XML_CHARSET_OBJ;
  private boolean m_bEmitTypeAttr = true;

  public ExporterXML ()
  {}

  @Nonnull
  public ExporterXML setCharset (@Nonnull final Charset aCharset)
  {
    if (aCharset == null)
      throw new NullPointerException ("charset");
    m_aCharset = aCharset;
    return this;
  }

  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  @Nonnull
  public ExporterXML setEmitTypeAttr (@Nonnull final boolean bEmitTypeAttr)
  {
    m_bEmitTypeAttr = bEmitTypeAttr;
    return this;
  }

  private void _emitRecord (@Nonnull final IMicroElement eParentRow, @Nonnull final IExportRecord aRecord)
  {
    for (final IExportRecordField aField : aRecord.getAllFields ())
    {
      final Object aFieldValue = aField.getFieldValue ();
      final IMicroElement eField = eParentRow.appendElement (ELEMENT_FIELD);
      if (m_bEmitTypeAttr)
        eField.setAttribute (ATTR_TYPE, aField.getFieldType ().getID ());
      if (aFieldValue != null)
        eField.appendText (TypeConverter.convertIfNecessary (aFieldValue, String.class));
    }
  }

  @Nullable
  public IMicroDocument convertRecords (@Nonnull final IExportRecordProvider aProvider)
  {
    if (aProvider == null)
      throw new NullPointerException ("provider");

    final IMicroDocument aDoc = new MicroDocument ();
    final IMicroElement eRoot = aDoc.appendElement (ELEMENT_ROOT);

    // Header
    final IExportRecord aHeaderRecord = aProvider.getHeader ();
    if (aHeaderRecord != null)
      _emitRecord (eRoot.appendElement (ELEMENT_HEADER), aHeaderRecord);

    // Body
    final IMicroElement eBody = eRoot.appendElement (ELEMENT_BODY);
    for (final IExportRecord aBodyRecord : IterableIterator.create (aProvider.getBodyRecords ()))
      _emitRecord (eBody.appendElement (ELEMENT_ROW), aBodyRecord);

    // Footer
    final IExportRecord aFooterRecord = aProvider.getFooter ();
    if (aFooterRecord != null)
      _emitRecord (eRoot.appendElement (ELEMENT_FOOTER), aFooterRecord);

    // The body element is always present
    if (eBody.getChildCount () == 0)
      return null;

    return aDoc;
  }

  @Override
  @Nonnull
  public ESuccess exportRecords (@Nonnull final IExportRecordProvider aProvider,
                                 @Nonnull @WillClose final OutputStream aOS)
  {
    try
    {
      final IMicroDocument aDoc = convertRecords (aProvider);
      if (aDoc == null)
        return ESuccess.FAILURE;

      MicroWriter.writeToStream (aDoc, aOS, new XMLWriterSettings ().setCharset (m_aCharset));
      return ESuccess.SUCCESS;
    }
    finally
    {
      StreamUtils.close (aOS);
    }
  }

  @Override
  @Nonnull
  public EExchangeFileType getFileType ()
  {
    return EExchangeFileType.XML;
  }
}
