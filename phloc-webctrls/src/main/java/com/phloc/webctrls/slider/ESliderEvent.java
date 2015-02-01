package com.phloc.webctrls.slider;

import javax.annotation.Nullable;

public enum ESliderEvent
{
  /**
   * <code>function(slideIndex[, event])</code>
   * 
   * @param slideIndex
   *        the index of slide which is clicked
   * @param event
   *        native event fired by browser
   */
  CLICK ("$EVT_CLICK"), //$NON-NLS-1$
  /**
   * <code>function(position[, virtualPosition, event])</code>
   * 
   * @param position
   *        real position of the carousel
   * @param virtualPosition
   *        virtual position of the carousel
   * @param event
   *        native event fired by browser
   */
  DRAG_START ("$EVT_DRAG_START"), //$NON-NLS-1$
  /**
   * <code>function(position, startPosition[, virtualPosition, virtualStartPosition,
   * event])</code>
   * 
   * @param position
   *        real position of the carousel
   * @param startPosition
   *        real position that drag starts at
   * @param virtualPosition
   *        virtual position of the carousel
   * @param virtualStartPosition
   *        virtual position that drag starts at
   * @param event
   *        native event fired by browser
   */
  DRAG_END ("$EVT_DRAG_END"), //$NON-NLS-1$
  /**
   * <code>function(position[, virtualPosition])</code>
   * 
   * @param position
   *        real position of the carousel
   * @param virtualPosition
   *        virtual position of the carousel
   */
  SWIPE_START ("$EVT_SWIPE_START"), //$NON-NLS-1$
  /**
   * <code>function(position[, virtualPosition])</code>
   * 
   * @param position
   *        real position of the carousel
   * @param virtualPosition
   *        virtual position of the carousel
   */
  SWIPE_END ("$EVT_SWIPE_END"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex)</code> fires before image load
   * 
   * @param slideIndex
   *        the index of slide
   */
  LOAD_START ("$EVT_LOAD_START"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex)</code> fires after image load
   * 
   * @param slideIndex
   *        the index of slide
   */
  LOAD_END ("$EVT_LOAD_END"), //$NON-NLS-1$
  /**
   * <code>function(position, fromPosition[, virtualPosition, virtualFromPosition])</code>
   * continuously fires while carousel sliding
   * 
   * @param position
   *        current position of the carousel
   * @param fromPosition
   *        previous position of the carousel
   * @param virtualPosition
   *        current virtual position of the carousel
   * @param virtualFromPosition
   *        previous virtual position of the carousel
   */
  POSITION_CHANGE ("$EVT_POSITION_CHANGE"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex, fromIndex)</code> fires when carousel stopped
   * sliding and slides park in correct position
   * 
   * @param slideIndex
   *        the index of current slide
   * @param fromIndex
   *        the index of previous slide
   */
  PARK ("$EVT_PARK"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex, progress[, progressBegin, idleBegin, idleEnd,
   * progressEnd])</code> this event continuously fires within the process of
   * current slide. * <br>
   * <b>Typical usage:</b>
   * <code>var progressString = progress / progressEnd * 100 + "%";</code>
   */
  PROGRESS_CHANGE ("$EVT_PROGRESS_CHANGE"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex, progress[, progressBegin, idleBegin, idleEnd,
   * progressEnd])</code> given a slide parked, the life cycle of current slide
   * is as below, progressBegin --> idleBegin --> idleEnd --> progressEnd. This
   * event fires at any state of 'progressBegin', 'idleBegin', 'idleEnd' and
   * 'progressEnd'
   * 
   * @param slideIndex
   *        the index of slide
   * @param progress
   *        current time in the whole process
   * @param progressBegin
   *        the beginning of the whole process (generally, captions start to
   *        play in)
   * @param idleBegin
   *        captions played in and become idle, will wait for a period which is
   *        specified by option '$AutoPlayInterval'
   * @param idleEnd
   *        the waiting time is over, captions start to play out
   * @param progressEnd
   *        the whole process is complete <br>
   * <br>
   *        <b>Typical usage:</b>
   * 
   *        <pre>
   *     if(progress == progressEnd)
   *     {
   *         //the animation of current slide is complete
   *     }
   *     else if(progress == idleEnd)
   *     {
   *         //the idle period of current slide is over
   *     }
   *     else if(progress == idleBegin)
   *     {
   *         //current slide become idle
   *     }
   *     else if(progress == progressBegin)
   *     {
   *         //the animation of current slide is at the beginning
   *     }
   *     else
   *     {
   *         //the animation of current slide is aborted,
   *             the progress value is negative only in this case
   *         var realProgress = -progress - 1;
   *     }
   * </pre>
   */
  STATE_CHANGE ("$EVT_STATE_CHANGE"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex, progress[, progressBegin, idleBegin, idleEnd,
   * progressEnd])</code> When mousedown/touch while captions are playing out,
   * the process will start to rollback, this event fires when rollback begin
   */
  ROLLBACK_START ("$EVT_ROLLBACK_START"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex, progress[, progressBegin, idleBegin, idleEnd,
   * progressEnd])</code> When mousedown/touch while captions are playing out,
   * the process will start to rollback, this event fires when rollback end
   */
  ROLLBACK_END ("$EVT_ROLLBACK_END"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex[, progressBegin, slideshowBegin, slideshowEnd,
   * progressEnd])</code> Within the whole process of a slide, there maybe
   * slideshow to run this event fires when slideshow begin
   */
  SLIDESHOW_START ("$EVT_SLIDESHOW_START"), //$NON-NLS-1$
  /**
   * <code>function(slideIndex[, progressBegin, slideshowBegin, slideshowEnd,
   * progressEnd])</code> Within the whole process of a slide, there maybe
   * slideshow to run this event fires when slideshow end
   */
  SLIDESHOW_END ("$EVT_SLIDESHOW_END"); //$NON-NLS-1$

  private String m_sID;

  private ESliderEvent (final String sID)
  {
    this.m_sID = sID;
  }

  public String getID ()
  {
    return this.m_sID;
  }

  /**
   * Tries to resolve the enum entry corresponding to the passed ID
   * 
   * @param sID
   * @return The resolved enum entry, or <code>null</code>
   */
  @Nullable
  public static ESliderEvent getFromID (final String sID)
  {
    for (final ESliderEvent eValue : values ())
    {
      if (eValue.getID ().equals (sID))
      {
        return eValue;
      }
    }
    return null;
  }

}
