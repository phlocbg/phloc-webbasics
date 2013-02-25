package com.phloc.web.smtp.attachment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.state.EChange;

/**
 * This interface represents attachments to be added to a mail message. Messages
 * with attachments are always send as MIME messages.
 * 
 * @author philip
 */
public interface IEmailAttachments extends IReadonlyEmailAttachments
{
  /**
   * Add an attachment.
   * 
   * @param sFilename
   *        The ID/filename of the attachment. May not be <code>null</code>.
   * @param aISS
   *        The {@link IInputStreamProvider} representing the data. May not be
   *        <code>null</code>.
   */
  void addAttachment (@Nonnull String sFilename, @Nonnull IInputStreamProvider aISS);

  /**
   * Remove the passed attachment.
   * 
   * @param sFilename
   *        The file name of the attachment to be removed. The file name is case
   *        sensitive.
   * @return {@link EChange}
   */
  @Nonnull
  EChange removeAttachment (@Nullable String sFilename);
}
