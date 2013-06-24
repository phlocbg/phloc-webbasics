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

// Extend jQuery
(function($)
{
  // Disable an element
  $.fn.disable = function()
  {
    return $(this).each(function()
    {
      $(this).prop('disabled', true);
    });
  };
  // Enable an element
  $.fn.enable = function()
  {
    return $(this).each(function()
    {
      $(this).prop('disabled', false);
    });
  };
  // Enable or disable an element
  $.fn.setDisabled = function(bDisabled)
  {
    return $(this).each(function()
    {
      $(this).prop('disabled', bDisabled);
    });
  };
  
  // Check a checkbox
  $.fn.check = function()
  {
    return $(this).each(function()
    {
      $(this).prop('checked', true);
    });
  };
  // uncheck a checkbox
  $.fn.uncheck = function()
  {
    return $(this).each(function()
    {
      $(this).prop('checked', false);
    });
  };
  // check or uncheck a checkbox
  $.fn.setChecked = function(bChecked)
  {
    return $(this).each(function()
    {
      $(this).prop('checked', bChecked);
    });
  };
})(jQuery)

// Set default AJAX settings
$.ajaxSetup ({
  // Disable jQuery AJAX caching
  cache:false,
  // Set a default error handler
  error: function (jqXHR, textStatus, errorThrown) {
    if (!window.ajaxSetupErrorShown) {
      window.ajaxSetupErrorShown=true;
      alert('AJAX error!\nStatus=' + textStatus+(errorThrown ? "\nError thrown="+errorThrown : ""));
    }
  }
});

/**
 * Default jQuery AJAX success handler for phloc AJAX server side components
 * @param data PlainObject The data returned from the server, formatted according to the dataType parameter
 * @param textStatus String a string describing the status
 * @param xhr jqXHR the jqXHR (in jQuery 1.4.x, XMLHttpRequest) object
 * @param callbackFctStart function Callback to be included before the inclusions take place
 * @param callbackFctEnd function Callback to be executed after the inclusions took place
 */
function jqueryAjaxSuccessHandler(data,textStatus,xhr,callbackFctStart,callbackFctEnd){
  if(data.success){
    if (callbackFctStart) {
      // Invoke callback before the inclusions
      callbackFctStart(data.value,textStatus,xhr);
    }
    if(data.externaljs){
      // Include external JS elements
      var firstjs=document.getElementsByTagName('script')[0];
      for(var js in data.externaljs){
        var jsNode=document.createElement('script');
        jsNode.type='text\/javascript';
        jsNode.src=js;
        jsNode.title='dynamicallyLoadedJS';
        firstjs.parentNode.insertBefore(jsNode,firstjs);
      }
    }
    if(data.externalcss){
      // Include external CSS elements
      var firstcss=document.getElementsByTagName('link')[0];
      for(var css in data.externalcss){
        var cssNode=document.createElement('link');
        cssNode.type='text\/css';
        cssNode.rel='stylesheet';
        cssNode.src=css;
        cssNode.title='dynamicallyLoadedCSS';
        firstcss.parentNode.insertBefore(cssNode,firstcss);
      }
    }
    if(data.inlinejs){
      // Include inline JS
      $.globalEval(data.inlinejs);
    }
    if (callbackFctEnd) {
      // Invoke callback after the inclusions
      callbackFctEnd(data.value,textStatus,xhr);
    }
  }else{
    var msg='Error invoking phloc AJAX function!';
    if(data.errormessage){
      window.alert(msg+' '+data.errormessage);
    }else{
      window.alert(msg);
    }
  }
}