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
package com.phloc.webctrls.typeahead;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.phloc.commons.url.SimpleURL;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.html.JSHtml;

/**
 * Test class for class {@link TypeaheadRemote}.
 * 
 * @author Philip Helger
 */
public class TypeaheadRemoteTest
{
  @Test
  public void testBasic ()
  {
    final TypeaheadRemote p = new TypeaheadRemote (new SimpleURL ("/a.json"));
    assertEquals ("{url:'\\/a.json'}", p.getAsJSObject ().getJSCode ());
    p.setDataType ("js");
    assertEquals ("{url:'\\/a.json',dataType:'js'}", p.getAsJSObject ().getJSCode ());
    p.setCache (false);
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false}", p.getAsJSObject ().getJSCode ());
    p.setTimeout (1000);
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000}", p.getAsJSObject ().getJSCode ());
    p.setWildcard ("$Q");
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q'}", p.getAsJSObject ()
                                                                                             .getJSCode ());
    p.setReplace (new JSAnonymousFunction (JSHtml.windowAlert ("x")));
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');}}",
                  p.getAsJSObject ().getJSCode ());
    p.setRateLimitFn (ETypeaheadRemoteRateLimitFunction.THROTTLE);
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');},rateLimitFn:'throttle'}",
                  p.getAsJSObject ().getJSCode ());
    p.setRateLimitWait (100);
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');},rateLimitFn:'throttle',rateLimitWait:100}",
                  p.getAsJSObject ().getJSCode ());
    p.setMaxParallelRequests (3);
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');},rateLimitFn:'throttle',rateLimitWait:100,maxParallelRequests:3}",
                  p.getAsJSObject ().getJSCode ());
    p.setBeforeSend (new JSAnonymousFunction (JSHtml.windowAlert ("y")));
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');},rateLimitFn:'throttle',rateLimitWait:100,maxParallelRequests:3,beforeSend:function(){window.alert('y');}}",
                  p.getAsJSObject ().getJSCode ());
    p.setFilter (new JSAnonymousFunction (JSHtml.windowAlert ("z")));
    assertEquals ("{url:'\\/a.json',dataType:'js',cache:false,timeout:1000,wildcard:'$Q',replace:function(){window.alert('x');},rateLimitFn:'throttle',rateLimitWait:100,maxParallelRequests:3,beforeSend:function(){window.alert('y');},filter:function(){window.alert('z');}}",
                  p.getAsJSObject ().getJSCode ());
  }
}
