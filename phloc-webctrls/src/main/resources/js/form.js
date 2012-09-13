function FormHelperClass(){}
FormHelperClass.prototype = 
{
  getAllFormValues : function(formid,fieldPrefix) {
    var vals={};
    $('#'+formid+' :input').each(function(){
      // Prefix for identification in AJAX handler
      vals[fieldPrefix+this.name]=$(this).val();
    });
    return vals;
  },

  setAllFormValues : function(formid,vals) {
    for (var name in vals) {
      $('#'+formid+' [name='+name+']').val(vals[name]);
    }
  },
 
  // string,string
  updateElementDirect : function(updateFieldId,html){
    var updateObj = $('#'+updateFieldId);
    updateObj.contents ().remove ();
    updateObj.append(html);
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
    vals["$pageID"]=pageID;
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
  }
}

$.ajaxSetup ({cache:false});
var FormHelper = window.FormHelper = new FormHelperClass();
