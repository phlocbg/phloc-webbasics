package com.phloc.appbasics.app.io;

import javax.annotation.Nonnull;

import com.phloc.commons.annotations.UsedViaReflection;
import com.phloc.commons.io.monitor.FileMonitor;
import com.phloc.commons.io.monitor.FileMonitorManager;
import com.phloc.commons.io.monitor.IFileListener;
import com.phloc.commons.state.EChange;
import com.phloc.scopes.singleton.GlobalSingleton;

/**
 * A global scoped singleton encapsulating a {@link FileMonitorManager}
 * 
 * @author Philip Helger
 */
public class ScopedFileMonitorManager extends GlobalSingleton
{
  private final FileMonitorManager m_aFMM = new FileMonitorManager ();

  @Deprecated
  @UsedViaReflection
  public ScopedFileMonitorManager ()
  {}

  @Nonnull
  public static ScopedFileMonitorManager getInstance ()
  {
    return getGlobalSingleton (ScopedFileMonitorManager.class);
  }

  @Override
  protected void onDestroy ()
  {
    // Stop monitor thread
    m_aFMM.stop ();
  }

  /**
   * Get the delay between runs.
   * 
   * @return The delay period in milliseconds.
   */
  public long getDelay ()
  {
    return m_aFMM.getDelay ();
  }

  /**
   * Set the delay between runs.
   * 
   * @param nDelay
   *        The delay period in milliseconds.
   */
  public void setDelay (final long nDelay)
  {
    m_aFMM.setDelay (nDelay);
  }

  /**
   * get the number of files to check per run.
   * 
   * @return The number of files to check per iteration.
   */
  public int getChecksPerRun ()
  {
    return m_aFMM.getChecksPerRun ();
  }

  /**
   * set the number of files to check per run. a additional delay will be added
   * if there are more files to check
   * 
   * @param nChecksPerRun
   *        a value less than 1 will disable this feature
   */
  public void setChecksPerRun (final int nChecksPerRun)
  {
    m_aFMM.setChecksPerRun (nChecksPerRun);
  }

  /**
   * Create a new {@link FileMonitor} based on the passed file listener.
   * 
   * @param aListener
   *        The listener to be used. May not be <code>null</code>.
   * @return The created {@link FileMonitor} that was already added.
   * @see #addFileMonitor(FileMonitor)
   */
  @Nonnull
  public FileMonitor createFileMonitor (@Nonnull final IFileListener aListener)
  {
    return m_aFMM.createFileMonitor (aListener);
  }

  /**
   * Add a new {@link FileMonitor}.
   * 
   * @param aMonitor
   *        The monitor to be added. May not be <code>null</code>.
   */
  public void addFileMonitor (@Nonnull final FileMonitor aMonitor)
  {
    m_aFMM.addFileMonitor (aMonitor);
  }

  /**
   * Remove a {@link FileMonitor}.
   * 
   * @param aMonitor
   *        The monitor to be remove. May be <code>null</code>.
   * @return {@link EChange}
   */
  // TODO make param @Nullable in phloc-commons > 4.1.2
  @Nonnull
  public EChange removeFileMonitor (@Nonnull final FileMonitor aMonitor)
  {
    return m_aFMM.removeFileMonitor (aMonitor);
  }
}
