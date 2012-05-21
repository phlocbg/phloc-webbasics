package com.phloc.webbasics.app.menu;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.phloc.webbasics.app.page.system.SystemPageNotFound;

/**
 * Test class for class {@link MenuTree}
 * 
 * @author philip
 */
public final class MenuTreeTest
{
  @Test
  public void testBasic ()
  {
    final MenuTree aTree = MenuTree.getInstance ();
    final IMenuItem aRoot1 = aTree.createRootItem ("root1", SystemPageNotFound.getInstance ())
                                  .setDisplayFilter (new AbstractMenuObjectFilter ()
                                  {
                                    public boolean matchesFilter (final IMenuObject aValue)
                                    {
                                      return false;
                                    }
                                  });
    assertNotNull (aRoot1);
    assertNotNull (aTree.createItem (aRoot1, SystemPageNotFound.getInstance ()));
    assertNotNull (aTree.getItemWithID ("root1"));
  }
}
