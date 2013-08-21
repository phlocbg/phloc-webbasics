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
package php.java.bridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import php.java.bridge.code.JavaInc;

public class MainCreateJavaInc
{
  public static void main (final String [] args) throws IOException
  {
    final File f = new File ("src/main/resources/META-INF/java/Java.inc");
    final OutputStream aFOS = new FileOutputStream (f);
    aFOS.write (JavaInc.bytes);
    aFOS.close ();
  }
}
