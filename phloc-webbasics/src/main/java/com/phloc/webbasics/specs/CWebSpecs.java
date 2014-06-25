package com.phloc.webbasics.specs;

import com.phloc.commons.annotations.PresentForCodeCoverage;
import com.phloc.commons.io.resource.ClassPathResource;

public final class CWebSpecs
{
  public static final ClassPathResource JSP_20_XSD = new ClassPathResource ("schemas/servlet/jsp_2_0.xsd");
  public static final ClassPathResource JSP_21_XSD = new ClassPathResource ("schemas/servlet/jsp_2_1.xsd");
  public static final ClassPathResource JSP_22_XSD = new ClassPathResource ("schemas/servlet/jsp_2_2.xsd");
  public static final ClassPathResource JSP_23_XSD = new ClassPathResource ("schemas/servlet/jsp_2_3.xsd");

  public static final ClassPathResource JSP_TAG_LIBRARY_20_XSD = new ClassPathResource ("schemas/servlet/web-jsptaglibrary_2_0.xsd");
  public static final ClassPathResource JSP_TAG_LIBRARY_21_XSD = new ClassPathResource ("schemas/servlet/web-jsptaglibrary_2_1.xsd");

  public static final ClassPathResource WEB_APP_24_XSD = new ClassPathResource ("schemas/servlet/web-app_2_4.xsd");
  public static final ClassPathResource WEB_APP_25_XSD = new ClassPathResource ("schemas/servlet/web-app_2_5.xsd");
  public static final ClassPathResource WEB_APP_30_XSD = new ClassPathResource ("schemas/servlet/web-app_3_0.xsd");
  public static final ClassPathResource WEB_APP_31_XSD = new ClassPathResource ("schemas/servlet/web-app_3_1.xsd");

  public static final ClassPathResource WEB_FRAGMENT_30_XSD = new ClassPathResource ("schemas/servlet/web-fragment_3_0.xsd");
  public static final ClassPathResource WEB_FRAGMENT_31_XSD = new ClassPathResource ("schemas/servlet/web-fragment_3_1.xsd");

  public static final String WEB_XML_PATH = "WEB-INF/web.xml";
  public static final String WEB_FRAGMENT_PATH = "META-INF/web-fragment.xml";

  @SuppressWarnings ("unused")
  @PresentForCodeCoverage
  private static final CWebSpecs s_aInstance = new CWebSpecs ();

  private CWebSpecs ()
  {}
}
