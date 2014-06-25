package com.phloc.webbasics.specs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.phloc.commons.io.IReadableResource;
import com.phloc.commons.xml.schema.XMLSchemaCache;

public class CWebSpecsTest
{
  private void _testXSD (@Nonnull final IReadableResource aXSD)
  {
    assertNotNull (aXSD);
    assertTrue (aXSD.exists ());
    assertNotNull (XMLSchemaCache.getInstance ().getSchema (aXSD));
    assertNotNull (XMLSchemaCache.getInstance ().getValidator (aXSD));
  }

  @Test
  public void testXSDs ()
  {
    _testXSD (CWebSpecs.JSP_20_XSD);
    _testXSD (CWebSpecs.JSP_21_XSD);
    _testXSD (CWebSpecs.JSP_22_XSD);
    _testXSD (CWebSpecs.JSP_23_XSD);

    _testXSD (CWebSpecs.JSP_TAG_LIBRARY_20_XSD);
    _testXSD (CWebSpecs.JSP_TAG_LIBRARY_21_XSD);

    _testXSD (CWebSpecs.WEB_APP_24_XSD);
    _testXSD (CWebSpecs.WEB_APP_25_XSD);
    _testXSD (CWebSpecs.WEB_APP_30_XSD);
    _testXSD (CWebSpecs.WEB_APP_31_XSD);

    _testXSD (CWebSpecs.WEB_FRAGMENT_30_XSD);
    _testXSD (CWebSpecs.WEB_FRAGMENT_31_XSD);
  }
}
