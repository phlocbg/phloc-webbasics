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
function FormHelperClass(){}
FormHelperClass.prototype = 
{
  getAllFormValues : function(formid,fieldPrefix) {
    var vals={};
    var elems=$('#'+formid).find('*');
    // Prefix each array item for identification in AJAX handler
    elems.filter ('input[type="checkbox"], input[type="radio"], select[multiple="multiple"]').each(
      function(){ vals[fieldPrefix+this.name+'[]']=$(this).val(); }
    );
    elems.filter ('input[type="text"], input[type="hidden"], select[multiple!="multiple"], textarea').each(
      function(){ vals[fieldPrefix+this.name]=$(this).val(); }
    );
    return vals;
  },

  setAllFormValues : function(formid,vals) {
    for (var name in vals) {
      $('#'+formid+' [name='+name+']').val(vals[name]);
    }
  },
 
  // string,string
  updateElementDirect : function(updateFieldId,html){
    $('#'+updateFieldId).empty().append(html);
  },
  
  // string,string
  updateElementViaAjax : function(updateFieldId,ajaxUrl){
    // Update list of all store values
    $.ajax({
      url:ajaxUrl,
      success:function(data){
        if (data.success) 
          FormHelper.updateElementDirect (updateFieldId,data.value.html);
      }
    });
  },
  
  // array
  updateElements : function(updates) {
    for (var i in updates) {
      var update = updates[i];
      if (update.url)
        FormHelper.updateElementViaAjax(update.id,update.url);
      else
        FormHelper.updateElementDirect(update.id,update.html);
    }
  },
  
  // string,string,string,string,array[map{id,url|html}]
  saveFormData : function(formid,fieldPrefix,pageID,ajaxUrl,successUpdates,errorUpdates) {
    var vals=FormHelper.getAllFormValues(formid,fieldPrefix);
    vals.$pageID=pageID;
    $.ajax({
      url:ajaxUrl,
      data:vals,
      success:function(data){
        if (data.success) 
          FormHelper.updateElements(successUpdates);
        else
          FormHelper.updateElements(errorUpdates);
      }
    });
  },
  
  // jQuery-select-obj,map<value,text>
  setSelectOptions : function ($select,newOptions) {
    $select.empty(); // remove old options
    $.each(newOptions, function(value,text) {
      $select.append($("<option></option>").attr("value",value).text(text));
    });    
  }
};

$.ajaxSetup ({cache:false});
var FormHelper = window.FormHelper = new FormHelperClass();
