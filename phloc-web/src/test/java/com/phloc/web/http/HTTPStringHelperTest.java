package com.phloc.web.http;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link HTTPStringHelper}.
 * 
 * @author philip
 */
public final class HTTPStringHelperTest
{
  @Test
  public void testIsChar ()
  {
    assertFalse (HTTPStringHelper.isChar (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      assertTrue (HTTPStringHelper.isChar (i));
    assertFalse (HTTPStringHelper.isChar (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testUpperAlpha ()
  {
    assertFalse (HTTPStringHelper.isUpperAlpha (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i >= 'A' && i <= 'Z')
        assertTrue (HTTPStringHelper.isUpperAlpha (i));
      else
        assertFalse (HTTPStringHelper.isUpperAlpha (i));
    assertFalse (HTTPStringHelper.isUpperAlpha (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testLowerAlpha ()
  {
    assertFalse (HTTPStringHelper.isLowerAlpha (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i >= 'a' && i <= 'z')
        assertTrue (HTTPStringHelper.isLowerAlpha (i));
      else
        assertFalse (HTTPStringHelper.isLowerAlpha (i));
    assertFalse (HTTPStringHelper.isLowerAlpha (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testAlpha ()
  {
    assertFalse (HTTPStringHelper.isAlpha (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if ((i >= 'A' && i <= 'Z') || (i >= 'a' && i <= 'z'))
        assertTrue (HTTPStringHelper.isAlpha (i));
      else
        assertFalse (HTTPStringHelper.isAlpha (i));
    assertFalse (HTTPStringHelper.isAlpha (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testDigit ()
  {
    assertFalse (HTTPStringHelper.isDigit (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i >= '0' && i <= '9')
        assertTrue (HTTPStringHelper.isDigit (i));
      else
        assertFalse (HTTPStringHelper.isDigit (i));
    assertFalse (HTTPStringHelper.isDigit (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testControl ()
  {
    assertFalse (HTTPStringHelper.isControl (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i < ' ' || i == 127)
        assertTrue (HTTPStringHelper.isControl (i));
      else
        assertFalse (HTTPStringHelper.isControl (i));
    assertFalse (HTTPStringHelper.isControl (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testCR ()
  {
    assertFalse (HTTPStringHelper.isCR (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == '\r')
        assertTrue (HTTPStringHelper.isCR (i));
      else
        assertFalse (HTTPStringHelper.isCR (i));
    assertFalse (HTTPStringHelper.isCR (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testLF ()
  {
    assertFalse (HTTPStringHelper.isLF (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == '\n')
        assertTrue (HTTPStringHelper.isLF (i));
      else
        assertFalse (HTTPStringHelper.isLF (i));
    assertFalse (HTTPStringHelper.isLF (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testSpace ()
  {
    assertFalse (HTTPStringHelper.isSpace (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == ' ')
        assertTrue (HTTPStringHelper.isSpace (i));
      else
        assertFalse (HTTPStringHelper.isSpace (i));
    assertFalse (HTTPStringHelper.isSpace (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testTab ()
  {
    assertFalse (HTTPStringHelper.isTab (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == '\t')
        assertTrue (HTTPStringHelper.isTab (i));
      else
        assertFalse (HTTPStringHelper.isTab (i));
    assertFalse (HTTPStringHelper.isTab (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testQuote ()
  {
    assertFalse (HTTPStringHelper.isQuote (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == '"')
        assertTrue (HTTPStringHelper.isQuote (i));
      else
        assertFalse (HTTPStringHelper.isQuote (i));
    assertFalse (HTTPStringHelper.isQuote (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testHex ()
  {
    assertFalse (HTTPStringHelper.isHex (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if ((i >= 'A' && i <= 'F') || (i >= 'a' && i <= 'f') || (i >= '0' && i <= '9'))
        assertTrue (HTTPStringHelper.isHex (i));
      else
        assertFalse (HTTPStringHelper.isHex (i));
    assertFalse (HTTPStringHelper.isHex (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testTokenSpecial ()
  {
    assertFalse (HTTPStringHelper.isTokenSpecial (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (i == '(' ||
          i == ')' ||
          i == '<' ||
          i == '>' ||
          i == '@' ||
          i == ',' ||
          i == ';' ||
          i == ':' ||
          i == '\\' ||
          i == '"' ||
          i == '/' ||
          i == '[' ||
          i == ']' ||
          i == '?' ||
          i == '=' ||
          i == '{' ||
          i == '}' ||
          i == ' ' ||
          i == '\t')
        assertTrue (HTTPStringHelper.isTokenSpecial (i));
      else
        assertFalse (HTTPStringHelper.isTokenSpecial (i));
    assertFalse (HTTPStringHelper.isHex (HTTPStringHelper.MAX_INDEX + 1));
  }

  @Test
  public void testText ()
  {
    assertFalse (HTTPStringHelper.isText (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if (!HTTPStringHelper.isControl (i) ||
          HTTPStringHelper.isCR (i) ||
          HTTPStringHelper.isLF (i) ||
          HTTPStringHelper.isTab (i) ||
          HTTPStringHelper.isSpace (i))
        assertTrue (HTTPStringHelper.isText (i));
      else
        assertFalse (HTTPStringHelper.isText (i));
    // Any other octet is valid!
    assertTrue (HTTPStringHelper.isText (HTTPStringHelper.MAX_INDEX + 1));
    assertTrue (HTTPStringHelper.isText (255));
    assertFalse (HTTPStringHelper.isText (256));
  }

  @Test
  public void testComment ()
  {
    assertFalse (HTTPStringHelper.isComment (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if ((!HTTPStringHelper.isControl (i) && i != '(' && i != ')') ||
          HTTPStringHelper.isCR (i) ||
          HTTPStringHelper.isLF (i) ||
          HTTPStringHelper.isTab (i) ||
          HTTPStringHelper.isSpace (i))
        assertTrue (HTTPStringHelper.isComment (i));
      else
        assertFalse (HTTPStringHelper.isComment (i));
    // Any other octet is valid!
    assertTrue (HTTPStringHelper.isComment (HTTPStringHelper.MAX_INDEX + 1));
    assertTrue (HTTPStringHelper.isComment (255));
    assertFalse (HTTPStringHelper.isComment (256));
  }

  @Test
  public void testQuotedChar ()
  {
    assertFalse (HTTPStringHelper.isQuotedText (HTTPStringHelper.MIN_INDEX - 1));
    for (int i = HTTPStringHelper.MIN_INDEX; i <= HTTPStringHelper.MAX_INDEX; ++i)
      if ((!HTTPStringHelper.isControl (i) && i != '"') ||
          HTTPStringHelper.isCR (i) ||
          HTTPStringHelper.isLF (i) ||
          HTTPStringHelper.isTab (i) ||
          HTTPStringHelper.isSpace (i))
        assertTrue (Integer.toHexString (i), HTTPStringHelper.isQuotedText (i));
      else
        assertFalse (HTTPStringHelper.isQuotedText (i));
    assertFalse (HTTPStringHelper.isQuotedText (HTTPStringHelper.MAX_INDEX + 1));
  }
}
