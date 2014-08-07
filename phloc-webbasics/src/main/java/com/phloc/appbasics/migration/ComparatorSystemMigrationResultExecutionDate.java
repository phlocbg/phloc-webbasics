package com.phloc.appbasics.migration;

import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.compare.AbstractIntegerComparator;
import com.phloc.commons.compare.ESortOrder;

/**
 * Comparator for {@link SystemMigrationResult} objects by execution time.
 * 
 * @author Philip Helger
 */
public class ComparatorSystemMigrationResultExecutionDate extends AbstractIntegerComparator <SystemMigrationResult>
{
  public ComparatorSystemMigrationResultExecutionDate ()
  {
    super ();
  }

  /**
   * Compare with a special order.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   */
  public ComparatorSystemMigrationResultExecutionDate (@Nonnull final ESortOrder eSortOrder)
  {
    super (eSortOrder);
  }

  /**
   * Comparator with default sort order and a nested comparator.
   *
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public ComparatorSystemMigrationResultExecutionDate (@Nullable final Comparator <? super SystemMigrationResult> aNestedComparator)
  {
    super (aNestedComparator);
  }

  /**
   * Comparator with sort order and a nested comparator.
   *
   * @param eSortOrder
   *        The sort order to use. May not be <code>null</code>.
   * @param aNestedComparator
   *        The nested comparator to be invoked, when the main comparison
   *        resulted in 0.
   */
  public ComparatorSystemMigrationResultExecutionDate (@Nonnull final ESortOrder eSortOrder,
                                                       @Nullable final Comparator <? super SystemMigrationResult> aNestedComparator)
  {
    super (eSortOrder, aNestedComparator);
  }

  @Override
  protected long asLong (@Nonnull final SystemMigrationResult aObject)
  {
    return aObject.getExecutionDateTime ().getMillis ();
  }
}
