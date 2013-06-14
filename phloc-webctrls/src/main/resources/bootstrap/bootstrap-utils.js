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
 * Create a typeAhead that handles the IDs of selected items
 * @param sLabelFieldSelector jQuery selector for the label field
 * @param aSelectedIDFct A function to be invoked with the selected ID
 * @param sAjaxURL The AJAX URL to invoke
 */
function typeaheadKeyValuePair(sLabelFieldSelector,aSelectedIDFct,sAjaxURL) {
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
      // Call callback
      aSelectedIDFct(aIDs[item]);
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
      return typeaheadHighlighterMultiple(item, this.query.trim ().split (/\s+/));
    }
  });
}
