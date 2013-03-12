package com.phloc.webctrls.js;

import com.phloc.html.hc.html.HCScript;

/**
 * A Script to determine, if local storage is available.
 * 
 * @author philip
 */
public class JSLocalStorageDeterminator extends HCScript
{
  public static final String VARNAME = "g_aLocalStorage";

  public JSLocalStorageDeterminator ()
  {
    super ("var " +
           VARNAME +
           "=(function() {var uid=new Date,storage,result;try {" +
           "(storage = window.localStorage).setItem(uid, uid);" +
           "result = storage.getItem(uid) == uid;" +
           "storage.removeItem(uid);" +
           "return result && storage;" +
           "} catch(e) {}" +
           "}());");
  }
}
