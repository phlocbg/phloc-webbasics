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
function serverLogInit(uri,key,debugMode,setWindowOnerror) {
  g_sServerLogURI = uri;
  g_sServerLogKey = key;
  g_bServerLogDebugMode = debugMode;
  if (setWindowOnerror){
    // IE and Firefox only
    window.onerror = function(msg,url,line) {
      serverLog(1,url+" ("+line+"): " + msg);
      // Return true to indicate not to respond
      return true;
    }
  }
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

// Requires stacktrace.js
function getStackTraceString(ex) {
  if (!ex)
    return null;
  if (typeof printStackTrace == "function")
    return printStackTrace({e:ex,guess:true}).join("\n");
  return ex.message;
}

//by Nicholas C. Zakas (MIT Licensed)
//http://www.nczonline.net/blog/2009/04/28/javascript-error-handling-anti-pattern/
function addExceptionHandlers(object) {
  if (object && typeof object._exceptionHandlersAdded == "undefined"){
    var name, method;
    for (name in object) {
      method = object[name];
      if (typeof method == "function") {
        object[name] = function(name, method) {
          return function() {
            try {
              return method.apply(this, arguments);
            }
            catch (ex) {
              serverLog("error", name + "(): " + getStackTraceString(ex));
            }
          };
        }(name, method);
      }
    }
    object._exceptionHandlersAdded = true;    
  }
}
