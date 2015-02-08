package com.phloc.webctrls.slider.html;

import com.phloc.html.hc.html.HCDiv;

//t optional  [integer | string]  t="L" | t="0" | t="*" 
//(specify name or index of caption transition for 'Play In', '*' means random transition.)
//t2  optional  [integer | string]  t2="L" | t2="0" | t2="*" (specify name or index of caption transition for 'Play out', '*' means random transition.)
//d optional  [integer] d="300" | d="-300" (delay in milliseconds to play this caption since the previous caption stopped.)
//du  optional  [integer] du="600" (explicitely set duration in milliseconds to 'play in'.)
//du2 optional  [integer] du2="600" (explicitely set duration in milliseconds for 'play out'.)
//position  required  absolute  style="position:absolute;"
//top required  [integer]px style="top:100px;"
//left  required  [integer]px style="left:100px;"
//width required  [integer]px style="width:100px;"
//height  required  [integer]px style="heght:100px;"
//overflow  optional  hidden  style="overflow:hidden;"
public class SliderCaption extends AbstractSliderHTMLElement
{
  // private String m_sTransitionIn;
  // private String m_sTransitionOut;

  public SliderCaption ()
  {
    super ("caption"); //$NON-NLS-1$
  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    // TODO Auto-generated method stub

  }
}
