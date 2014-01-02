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

public class JavaProxy
{
  private static final String data = "<?php \n"
                                     + "/* wrapper for Java.inc */ \n"
                                     + "\n"
                                     + "if(!function_exists(\"java_get_base\")) require_once(\"Java.inc\"); \n"
                                     + "\n"
                                     + "if ($java_script_orig = $java_script = java_getHeader(\"X_JAVABRIDGE_INCLUDE\", $_SERVER)) {\n"
                                     + "\n"
                                     + "  if ($java_script!=\"@\") {\n"
                                     + "    if (($_SERVER['REMOTE_ADDR']=='127.0.0.1') || (($java_script = realpath($java_script)) && (!strncmp($_SERVER['DOCUMENT_ROOT'], $java_script, strlen($_SERVER['DOCUMENT_ROOT']))))) {\n"
                                     + "      chdir (dirname ($java_script));\n"
                                     + "      require_once($java_script);\n"
                                     + "    } else {\n"
                                     + "      trigger_error(\"illegal access: \".$java_script_orig, E_USER_ERROR);\n"
                                     + "    }\n"
                                     + "  }\n"
                                     + "\n"
                                     + "  java_call_with_continuation();\n"
                                     + "}\n"
                                     + "?>\n"
                                     + "";
  public static final byte [] bytes = data.getBytes (Util.ISO88591);
}
