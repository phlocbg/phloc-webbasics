/**
 * Replace searchTerm in string with <strong>searchTerm</strong>
 * @param bodyText Original text
 * @param searchTerm Term to search
 * @returns {String}
 */
function _doHighlightSingleTerm(bodyText, searchTerm){
  var ret = "";
  var i = -1;
  var lcSearchTerm = searchTerm.toLowerCase();
  var lcBodyText = bodyText.toLowerCase();
  while (bodyText.length > 0) {
    i = lcBodyText.indexOf(lcSearchTerm, i+1);
    if (i < 0) {
      ret += bodyText;
      bodyText = "";
    } else {
      // skip anything inside an HTML tag
      if (bodyText.lastIndexOf(">", i) >= bodyText.lastIndexOf("<", i)) {
        // skip anything inside a <script> block
        if (lcBodyText.lastIndexOf("/script>", i) >= lcBodyText.lastIndexOf("<script", i)) {
          // skip anything inside a <strong> block
          if (lcBodyText.lastIndexOf("/strong>", i) >= lcBodyText.lastIndexOf("<strong", i)) {
            ret += bodyText.substring(0, i) + '<strong>' + bodyText.substr(i, searchTerm.length) + '</strong>';
            bodyText = bodyText.substr(i + searchTerm.length);
            lcBodyText = bodyText.toLowerCase();
            i = -1;
          }  
        }
      }
    }
  }
  return ret;
}

/**
 * Highlight multiple query terms
 * @param item Source item label string
 * @param parts The parts of the original query string to be highlighted
 * @returns The final HTML to be rendered
 */
function typeaheadHighlighterMultiple(item,parts){
  for (var part in parts)
    item = _doHighlightSingleTerm(item, parts[part]);
  return item;
}
/**
 * Create a typeAhead that handles the IDs of selected items. The data is retrieved via AJAX.
 * The only parameter is called "query" and contains the data of the user.
 * 
 * @param sLabelFieldSelector jQuery selector for the label field
 * @param aSelectedIDFct A function to be invoked with the selected ID. May be <code>null</code>.
 * @param sAjaxURL The AJAX URL to invoke.
 * @param nMinLength Minimum length after which the server is queried. Defaults to 1.
 * @param nItems Maximum items to show. Defaults to 8.
 */
function registerTypeaheadKeyValuePair(sLabelFieldSelector,aSelectedIDFct,sAjaxURL,nMinLength,nItems) {
  var aLabels = [];
  var aIDs = {};
  $(sLabelFieldSelector).typeahead({
    source : function(query, process) {
      // data = list-of <label:string, value:string>
      var aLabelToIDMapper = function(data) {
        // Clear existing
        aIDs = {};
        aLabels = [];
        $.each(data, function(index, item) {
          aLabels.push(item.label);
          aIDs[item.label] = item.value;
        });
        // Show the labels
        process(aLabels);
      };
      $.ajax({
        url : sAjaxURL,
        data : {
          'query' : query
        },
        success : function(data, x, y) {
          // function located in jquery-utils.js
          jqueryAjaxSuccessHandler(data, x, y, aLabelToIDMapper, null)
        }
      });
    },
    // Set custom value
    updater : function(item) {
      var id = aIDs[item];
      // Call callback with selected ID
      if (aSelectedIDFct)
        aSelectedIDFct(id);
      
      // If no ID is present (e.g. for "no entry found") no text should be displayed
      if (!id)
        return "";
      
      // Return the data as is
      return item;
    },
    // Already matched on server
    matcher : function(item) {
      return true;
    },
    // Already sorted on server
    sorter : function(items) {
      return items;
    },
    // Highlight multiple terms
    highlighter : function(item) {
      var queryParts = this.query.trim ().split (/\s+/);
      return typeaheadHighlighterMultiple(item, queryParts);
    },
    minLength: nMinLength || 1,
    items: nItems || 8
  });
}
