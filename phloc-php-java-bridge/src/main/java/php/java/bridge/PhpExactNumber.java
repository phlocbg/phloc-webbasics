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
/*-*- mode: Java; tab-width:8 -*-*/

package php.java.bridge;

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

final class PhpExactNumber extends Number
{

  /**
     * 
     */
  private static final long serialVersionUID = 3257566187666749240L;
  private final long l;

  /**
   * @param l
   */
  public PhpExactNumber (final long l)
  {
    this.l = l;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Number#intValue()
   */
  @Override
  public int intValue ()
  {

    return (int) l;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Number#longValue()
   */
  @Override
  public long longValue ()
  {
    return l;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Number#floatValue()
   */
  @Override
  public float floatValue ()
  {
    return l;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Number#doubleValue()
   */
  @Override
  public double doubleValue ()
  {
    return l;
  }

  @Override
  public String toString ()
  {
    return String.valueOf (l);
  }

}
