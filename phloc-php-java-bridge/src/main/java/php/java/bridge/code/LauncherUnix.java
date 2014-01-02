/**
 * Copyright (C) 2006-2014 phloc systems
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
package php.java.bridge.code;

import php.java.bridge.Util;

public class LauncherUnix
{
  private static final String data = "#!/bin/sh\n"
                                     + "# php fcgi launcher\n"
                                     + "#set -x\n"
                                     + "\n"
                                     + "\"$@\" 1>&2 &\n"
                                     + "trap \"kill $! && exit 0;\" 1 2 15\n"
                                     + "read result 1>&2\n"
                                     + "kill $!\n"
                                     + "";
  public static final byte [] bytes = data.getBytes (Util.ISO88591);
}
