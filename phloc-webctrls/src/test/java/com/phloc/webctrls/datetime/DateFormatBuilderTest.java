package com.phloc.webctrls.datetime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import com.phloc.commons.locale.LocaleCache;
import com.phloc.commons.mock.AbstractPhlocTestCase;
import com.phloc.datetime.format.PDTFormatPatterns;

/**
 * Test class for class {@link DateFormatBuilder}.
 * 
 * @author philip
 */
public final class DateFormatBuilderTest extends AbstractPhlocTestCase
{
  @Test
  public void testAll ()
  {
    final DateFormatBuilder aDFB = new DateFormatBuilder ();
    aDFB.append (EDateTimeFormatToken.YEAR_WITH_CENTURY)
        .append ("/")
        .append (EDateTimeFormatToken.MONTH_LZ)
        .append ("/")
        .append (EDateTimeFormatToken.DAY_OF_MONTH_LZ)
        .append (" ")
        .append (EDateTimeFormatToken.HOUR23_LZ)
        .append (":")
        .append (EDateTimeFormatToken.MINUTE_LZ)
        .append (":")
        .append (EDateTimeFormatToken.SECONDS_LZ);
    assertEquals ("%Y/%m/%d %H:%M:%S", aDFB.getJSCalendarFormatString ());
    assertEquals ("yyyy/MM/dd HH:mm:ss", aDFB.getJavaFormatString ());

    final DateTime aDT = aDFB.getDateTimeFormatted ("2010/08/08 12:45:13");
    assertNotNull (aDT);
    assertEquals (2010, aDT.getYear ());
    assertEquals (DateTimeConstants.AUGUST, aDT.getMonthOfYear ());
    assertEquals (8, aDT.getDayOfMonth ());
    assertEquals (12, aDT.getHourOfDay ());
    assertEquals (45, aDT.getMinuteOfHour ());
    assertEquals (13, aDT.getSecondOfMinute ());

    final LocalDate aLD = aDFB.getDateFormatted ("2010/08/08 12:45:13");
    assertNotNull (aLD);
    assertEquals (2010, aLD.getYear ());
    assertEquals (DateTimeConstants.AUGUST, aLD.getMonthOfYear ());
    assertEquals (8, aLD.getDayOfMonth ());

    final LocalTime aLT = aDFB.getTimeFormatted ("2010/08/08 12:45:13");
    assertNotNull (aLT);
    assertEquals (12, aLT.getHourOfDay ());
    assertEquals (45, aLT.getMinuteOfHour ());
    assertEquals (13, aLT.getSecondOfMinute ());
  }

  @Test
  public void getFromPattern ()
  {
    String sPattern;
    IDateFormatBuilder aDFB;

    for (final Locale aLocale : LocaleCache.getAllLocales ())
    {
      sPattern = PDTFormatPatterns.getDefaultPatternDateTime (aLocale);
      aDFB = DateFormatBuilder.fromJavaPattern (sPattern);
      assertNotNull (aDFB);
      assertEquals (sPattern, aDFB.getJavaFormatString ());
      assertNotNull (aDFB.getJSCalendarFormatString ());

      sPattern = PDTFormatPatterns.getDefaultPatternDate (aLocale);
      aDFB = DateFormatBuilder.fromJavaPattern (sPattern);
      assertNotNull (aDFB);
      assertEquals (sPattern, aDFB.getJavaFormatString ());
      assertNotNull (aDFB.getJSCalendarFormatString ());

      sPattern = PDTFormatPatterns.getDefaultPatternTime (aLocale);
      aDFB = DateFormatBuilder.fromJavaPattern (sPattern);
      assertNotNull (aDFB);
      assertEquals (sPattern, aDFB.getJavaFormatString ());
      assertNotNull (aDFB.getJSCalendarFormatString ());
    }

    sPattern = "dd.MM.yy HH:mm:ss";
    aDFB = DateFormatBuilder.fromJavaPattern (sPattern);
    assertNotNull (aDFB);
    assertEquals (sPattern, aDFB.getJavaFormatString ());
    assertNotNull (aDFB.getJSCalendarFormatString ());
  }
}
