/**
 * Loads the Facebook SDK asynchronously
 * 
 * @param sAppID        
 *        {String} Your Facebook APP-ID
 * @param sLang 
 *        {String} The current display locale
 * @param sContainerID
          {String} The ID of the container element to which necessary DOM elements can be appended
 * @param bCheckLoginStatus [optional] 
 *        {Boolean} Whether or not to check the current login status 
 * @param bEnableCookies [optional] 
 *        {Boolean} Whether or not to enable cookies 
 * @param bUseXFBML [optional] 
 *        {Boolean} Whether or not to use XFBML 
 * @param fnOnLoad [optional] 
 *        {Function} An optional callback (without arguments) which will be executed on initialization 
 */
function facebookLoadSDKAsync (sAppID, sLang, sContainerID, bCheckLoginStatus, bEnableCookies, bUseXFBML, fnOnLoad)
{
  /* workaround since it is currently not possible to set the namespace for metatags on the server side */
 var aHead = document.getElementsByTagName("head")[0];
 if (aHead)
 {
   aHead.setAttribute("xmlns:og", "http://ogp.me/ns#");
 }
  
  /* All Facebook functions should be included in this function, or at least initiated from here */
  window.fbAsyncInit = function() 
  {
    FB.init(
    {
      appId: sAppID, 
      status: !!bCheckLoginStatus, 
      cookie: !!bEnableCookies,
      xfbml: !!bUseXFBML
    });
// API not yet working due to missing access token
//    FB.api('/me', function(response) {
//      console.log(response.name);
//      });
    if (typeof (fnOnLoad) === "function")
    {
      fnOnLoad();
    }
  };
  (function() 
   {
     var e = document.createElement('script'); e.async = true;
     e.src = document.location.protocol + '//connect.facebook.net/' + sLang + '/all.js';
     document.getElementById(sContainerID).appendChild(e);
   }()
  );
}
