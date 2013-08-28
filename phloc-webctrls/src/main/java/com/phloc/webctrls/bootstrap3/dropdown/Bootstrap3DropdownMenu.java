package com.phloc.webctrls.bootstrap3.dropdown;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.string.StringHelper;
import com.phloc.html.EHTMLRole;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.html.HCLI;
import com.phloc.html.hc.html.HCUL;
import com.phloc.webctrls.bootstrap3.CBootstrap3CSS;
import com.phloc.webctrls.bootstrap3.base.Bootstrap3Caret;

public class Bootstrap3DropdownMenu extends HCUL
{
  public Bootstrap3DropdownMenu ()
  {
    addClass (CBootstrap3CSS.DROPDOWN_MENU);
    setRole (EHTMLRole.MENU);
  }

  @Override
  protected void onAddItem (@Nonnull final HCLI aItem)
  {
    if (aItem.getRole () == null)
      aItem.setRole (EHTMLRole.PRESENTATION);
  }

  @Nonnull
  public Bootstrap3DropdownMenu addDivider ()
  {
    addItem ().addClass (CBootstrap3CSS.DIVIDER);
    return this;
  }

  @Nonnull
  public Bootstrap3DropdownMenu addHeader (@Nullable final String sHeaderText)
  {
    if (StringHelper.hasText (sHeaderText))
      addItem (sHeaderText).addClass (CBootstrap3CSS.DROPDOWN_HEADER);
    return this;
  }

  public static void disableItem (@Nonnull final HCLI aItem)
  {
    aItem.addClass (CBootstrap3CSS.DISABLED);
  }

  /**
   * Call this method to convert an element to a dropdown toggle. Important:
   * call this after all children are added, because a caret is added at the
   * end!
   * 
   * @param aElement
   *        The element to use. May not be <code>null</code>.
   */
  @Nonnull
  public static <IMPLTYPE extends IHCElementWithChildren <?>> IMPLTYPE makeDropdownToggle (@Nonnull final IMPLTYPE aElement)
  {
    aElement.addClass (CBootstrap3CSS.DROPDOWN_TOGGLE)
            .setCustomAttr ("data-toggle", "dropdown")
            .addChild (new Bootstrap3Caret ());
    return aElement;
  }
}
