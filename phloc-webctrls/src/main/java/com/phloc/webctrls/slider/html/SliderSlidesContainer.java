package com.phloc.webctrls.slider.html;

import java.util.List;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.equals.EqualsUtils;
import com.phloc.css.ECSSUnit;
import com.phloc.css.property.CCSSProperties;
import com.phloc.css.propertyvalue.CCSSValue;
import com.phloc.html.css.DefaultCSSClassProvider;
import com.phloc.html.css.ICSSClassProvider;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCSpan;

// position required  absolute | relative style="position:absolute;" ("absolute" recommended)
//top optional  [integer]px style="top:0px;"
//left  optional  [integer]px style="left:0px;"
//bottom  optional  [integer]px style="bottom:0px;"
//right optional  [integer]px style="right:0px;"
//width required  [integer]px style="width:300px;"
//height  required  [integer]px style="height:30px;"
//overvlow  required  hidden  style="overflow:hidden;"
//cursor  optional    style="cursor:move;"

public class SliderSlidesContainer extends AbstractSliderHTMLElement
{
  private static final ICSSClassProvider CLASS = DefaultCSSClassProvider.create ("SliderSlidesContainer");
  private static final Logger LOG = LoggerFactory.getLogger (SliderSlidesContainer.class);
  // required
  private final String m_sPosition = CCSSValue.ABSOLUTE;
  private int m_nWidth = 300;
  private int m_nHeight = 30;
  private final String m_sOverflow = CCSSValue.HIDDEN;

  // optional
  private final String m_sCursor = CCSSValue.MOVE;
  private final int m_nTop = 0;
  private final int m_nLeft = 0;
  private final int m_nBottom = 0;
  private final int m_nRight = 0;
  private final List <Slide> m_aSlides = ContainerHelper.newList ();

  public SliderSlidesContainer ()
  {
    super ("slides"); //$NON-NLS-1$
  }

  public void addSlide (@Nonnull final Slide aSlide)
  {
    if (aSlide == null)
    {
      throw new NullPointerException ("aSlide"); //$NON-NLS-1$
    }
    this.m_aSlides.add (aSlide);
  }

  public SliderSlidesContainer setWidth (final int nWidth)
  {
    this.m_nWidth = nWidth;
    return this;
  }

  public SliderSlidesContainer setHeight (final int nHeight)
  {
    this.m_nHeight = nHeight;
    return this;
  }

  @Override
  protected void onBuildNode (final HCDiv aNode)
  {
    aNode.addClass (CLASS);
    if (ContainerHelper.isEmpty (this.m_aSlides))
    {
      LOG.warn ("Empty slide container, creating empty slide!"); //$NON-NLS-1$
      addSlide (new Slide (null, null, false, new HCSpan ()));
    }
    for (final Slide aSlide : this.m_aSlides)
    {
      aNode.addChild (aSlide.build ());
    }
    aNode.addStyle (CCSSProperties.POSITION.newValue (this.m_sPosition));
    aNode.addStyle (CCSSProperties.WIDTH.newValue (ECSSUnit.px (this.m_nWidth)));
    aNode.addStyle (CCSSProperties.HEIGHT.newValue (ECSSUnit.px (this.m_nHeight)));
    aNode.addStyle (CCSSProperties.OVERFLOW.newValue (this.m_sOverflow));

    if (EqualsUtils.equals (this.m_sPosition, CCSSValue.ABSOLUTE))
    {
      aNode.addStyle (CCSSProperties.LEFT.newValue (ECSSUnit.px (this.m_nLeft)));
      aNode.addStyle (CCSSProperties.TOP.newValue (ECSSUnit.px (this.m_nTop)));
      aNode.addStyle (CCSSProperties.BOTTOM.newValue (ECSSUnit.px (this.m_nBottom)));
      aNode.addStyle (CCSSProperties.RIGHT.newValue (ECSSUnit.px (this.m_nRight)));
    }
    aNode.addStyle (CCSSProperties.CURSOR.newValue (this.m_sCursor));
  }
}
