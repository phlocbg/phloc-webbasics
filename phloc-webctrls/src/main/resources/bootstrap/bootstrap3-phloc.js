/* Internet Explorer 10 doesn't differentiate device width 
   from viewport width, and thus doesn't properly apply the 
   media queries in Bootstrap's CSS. To address this, you can
   optionally include the following CSS and JavaScript to work
   around this problem until Microsoft issues a fix. */
if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
  var msViewportStyle = document.createElement("style")
  msViewportStyle.appendChild(
    document.createTextNode(
      "@-ms-viewport{width:auto!important}"
    )
  )
  document.getElementsByTagName("head")[0].appendChild(msViewportStyle)
}
