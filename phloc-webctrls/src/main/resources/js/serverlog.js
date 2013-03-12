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
// Inspiration: http://www.devhands.com/2008/10/javascript-error-handling-and-general-best-practices/
var g_sServerLogURI = null;
var g_sServerLogKey = null;
var g_bServerLogDebugMode = false;

/**
 * Init the server logging. Needs to be called once.
 * @param uri The URI to invoke for server logging. Must be a string.
 * @param key The internal key for validation. Only valid keys should log something. Typically a string.
 * @param debugMode <code>true</code> for debug mode, meaning events are browser handled. Must be a boolean.
 */
function serverLogInit(uri,key,debugMode) {
  g_sServerLogURI = uri;
  g_sServerLogKey = key;
  g_bServerLogDebugMode = debugMode;
}

/**
 * Do a server log call.
 * @param severity Message severity. Number or string.
 * @param message Main message. Should be a string.
 */
function serverLog(severity,message){
  if (g_sServerLogURI && g_sServerLogKey) {
    var img = new Image ();
    img.src = g_sServerLogURI + "?severity=" + encodeURIComponent(severity)
                              + "&message=" + encodeURIComponent(message)
                              + "&key=" + encodeURIComponent(g_sServerLogKey);
  }
}

function setupServerLogForWindow(){
  if (!g_bServerLogDebugMode){
    // IE and Firefox only
    window.onerror = function(msg,url,line) {
      serverLog(1,url+" ("+line+"): " + msg);
      // Return true to indicate not to respond
      return true;
    }
  }
}
