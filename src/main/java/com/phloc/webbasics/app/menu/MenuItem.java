package com.phloc.webbasics.app.menu;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.webbasics.app.page.IPage;

/**
 * Default implementation of the {@link IMenuItem} interface.
 *
 * @author philip
 */
public final class MenuItem extends AbstractMenuObject implements IMenuItem
{
  private final IPage m_aPage;

  public MenuItem (@Nonnull @Nonempty final String sItemID, @Nonnull final IPage aPage)
  {
    super (sItemID);
    if (aPage == null)
      throw new NullPointerException ("page");
    m_aPage = aPage;
  }

  @Nonnull
  public IPage getPage ()
  {
    return m_aPage;
  }

  @Nullable
  public String getDisplayText (final Locale aDisplayLocale)
  {
    return m_aPage.getDisplayText (aDisplayLocale);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final MenuItem rhs = (MenuItem) o;
    return m_aPage.getID ().equals (rhs.m_aPage.getID ());
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aPage).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).append ("page", m_aPage).toString ();
  }
}
