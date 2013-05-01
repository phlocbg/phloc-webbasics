<?php 
/*
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
/* wrapper for Java.inc */ 

if(!function_exists("java_get_base")) require_once("Java.inc"); 

if ($java_script_orig = $java_script = java_getHeader("X_JAVABRIDGE_INCLUDE", $_SERVER)) {

  if ($java_script!="@") {
    if (($_SERVER['REMOTE_ADDR']=='127.0.0.1') || (($java_script = realpath($java_script)) && (!strncmp($_SERVER['DOCUMENT_ROOT'], $java_script, strlen($_SERVER['DOCUMENT_ROOT']))))) {
      chdir (dirname ($java_script));
      require_once($java_script);
    } else {
      trigger_error("illegal access: ".$java_script_orig, E_USER_ERROR);
    }
  }

  java_call_with_continuation();
}
?>
