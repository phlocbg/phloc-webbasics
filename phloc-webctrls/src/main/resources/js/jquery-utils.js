/*
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
  // Disable jQuery AJAX caching - not always useful!
  // cache:false,
  // Set a default error handler
  error: function (jqXHR, textStatus, errorThrown) {
    if (!window.ajaxSetupErrorShown) {
      window.ajaxSetupErrorShown=true;
      alert('AJAX error!\nStatus=' + textStatus+(errorThrown ? "\nError thrown="+errorThrown : ""));
    }
  }
});

jQuery.cachedScript = function(url, options) {
  // allow user to set any option except for dataType, cache, and url
  options = $.extend(options || {}, {
    dataType : "script",
    cache : true,
    url : url
  });
  // Use $.ajax() since it is more flexible than $.getScript
  // Return the jqXHR object so we can chain callbacks
  return jQuery.ajax(options);
};

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
    
    // Do we have inline JS?
    var aInlineJSEval;
    if(data.inlinejs){
      // Include inline JS
      aInlineJSEval = function() { $.globalEval(data.inlinejs); }
    }

    if(data.externaljs){
      // Include external JS elements
      if (aInlineJSEval) {
        // external JS and inline JS is present
        // => synchronize them so that the inline JS is only evaluated after 
        //    all external JS are loaded
        var left = data.externaljs.length;
        for(var js in data.externaljs){
          $.cachedScript (data.externaljs[js]).always(function() { 
            // One item less
            left--; 
          });
        }
        
        // synchronize via setTimeout (timeout = 100*50ms = 5secs)
        var timeout=100;
        var poll = function(){
          setTimeout(function (){
            if (left > 0) {
              // Still files left to load
              if (--timeout > 0) poll();
            }
            else {
              // All files loaded - eval inline JS
              aInlineJSEval();
            }  
          }, 50); 
        };
        poll ();
      } else {
        // Only external JS present
        // ==> no need to synchronize
        for(var js in data.externaljs){
          $.cachedScript (data.externaljs[js]);
        }
      }  
    }
    else{
      // No external JS - Maybe inline JS?
      if (aInlineJSEval)
        aInlineJSEval();
    }
    
    if(data.externalcss){
      // Include external CSS elements
      var firstcss=document.getElementsByTagName('link')[0];
      for(var css in data.externalcss){
        var cssNode=document.createElement('link');
        cssNode.href=data.externalcss[css];
        cssNode.type='text\/css';
        cssNode.rel='stylesheet';
        cssNode.title='dynamicallyLoadedCSS';
        firstcss.parentNode.insertBefore(cssNode,firstcss);
      }
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
