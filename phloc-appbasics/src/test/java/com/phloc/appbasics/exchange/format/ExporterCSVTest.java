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
package com.phloc.appbasics.exchange.format;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.phloc.appbasics.exchange.bulkexport.ConstantExportRecordProvider;
import com.phloc.appbasics.exchange.bulkexport.EmptyExportRecordProvider;
import com.phloc.appbasics.exchange.bulkexport.ExportRecord;
import com.phloc.appbasics.exchange.bulkexport.format.ExporterCSV;
import com.phloc.appbasics.exchange.bulkexport.format.ExporterExcel;
import com.phloc.commons.charset.CCharset;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.io.streams.NonBlockingByteArrayOutputStream;
import com.phloc.datetime.PDTFactory;

/**
 * Test class for class {@link ExporterExcel}.
 * 
 * @author philip
 */
public final class ExporterCSVTest
{
  @Test
  public void testBasicExport ()
  {
    final ExportRecord aRecordWithAllTypes = new ExportRecord ().addField ("Hallo")
                                                                .addField (PDTFactory.getCurrentLocalTime ())
                                                                .addField (PDTFactory.getCurrentLocalDate ())
                                                                .addField (PDTFactory.getCurrentLocalDateTime ())
                                                                .addField (PDTFactory.getCurrentDateTime ())
                                                                .addField (true)
                                                                .addField (4711)
                                                                .addField (-123456789012345L)
                                                                .addField (new BigInteger ("1234512345123451234512345123451234512345123451234512345"))
                                                                .addField (3.1415)
                                                                .addField (new BigDecimal ("12345123451234512345123451234512345123451234512345.12345"));
    final ExportRecord aEmptyRecord = new ExportRecord ();
    final ExporterCSV aExporter = new ExporterCSV (CCharset.CHARSET_ISO_8859_1_OBJ);
    // Fails because no record is present
    assertTrue (aExporter.exportRecords (new EmptyExportRecordProvider (), new NonBlockingByteArrayOutputStream ())
                         .isFailure ());
    assertTrue (aExporter.exportRecords (new ConstantExportRecordProvider (ContainerHelper.newList (aRecordWithAllTypes)),
                                         new NonBlockingByteArrayOutputStream ())
                         .isSuccess ());
    assertTrue (aExporter.exportRecords (new ConstantExportRecordProvider (ContainerHelper.newList (aRecordWithAllTypes,
                                                                                                    aRecordWithAllTypes,
                                                                                                    aEmptyRecord)),
                                         new NonBlockingByteArrayOutputStream ())
                         .isSuccess ());
    assertTrue (aExporter.exportRecords (new ConstantExportRecordProvider (null,
                                                                           ContainerHelper.newList (aRecordWithAllTypes,
                                                                                                    aRecordWithAllTypes,
                                                                                                    aEmptyRecord),
                                                                           aRecordWithAllTypes),
                                         new NonBlockingByteArrayOutputStream ())
                         .isSuccess ());
    assertTrue (aExporter.exportRecords (new ConstantExportRecordProvider (aRecordWithAllTypes,
                                                                           ContainerHelper.newList (aRecordWithAllTypes,
                                                                                                    aRecordWithAllTypes,
                                                                                                    aEmptyRecord),
                                                                           aRecordWithAllTypes),
                                         new NonBlockingByteArrayOutputStream ())
                         .isSuccess ());
  }
}
