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
// Source: https://gist.github.com/823300
// Based on: https://gist.github.com/379601
// Released under MIT license: http://www.opensource.org/licenses/mit-license.php

// Init with: $.placeholder()
jQuery.placeholder = function() {
  $('[placeholder]').focus(function() {
    var input = $(this);
    if (input.hasClass('placeholder')) {
      input.val('');
      input.removeClass('placeholder');
    }
  }).blur(function() {
    var input = $(this);
    if (input.val() === '') {
      input.addClass('placeholder');
      input.val(input.attr('placeholder'));
    }
  }).blur().parents('form').submit(function() {
    $(this).find('[placeholder]').each(function() {
      var input = $(this);
      if (input.hasClass('placeholder')) {
        input.val('');
      }
    });
  });
  // Clear input on refresh so that the placeholder class gets added back
  $(window).unload(function() {
    $('[placeholder]').val('');
  });
};

// If using AJAX, call this on all placeholders after submitting to
// return placeholder
jQuery.fn.addPlaceholder = function() {
  return this.each(function() {
    var input = $(this);
    input.addClass('placeholder');
    input.val(input.attr('placeholder'));
  });
};

$(document).ready (function() { 
  $.placeholder();
});