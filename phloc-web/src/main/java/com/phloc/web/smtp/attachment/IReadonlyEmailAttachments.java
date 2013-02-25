package com.phloc.web.smtp.attachment;

import java.util.List;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.IHasSize;

/**
 * This interface represents attachments to be added to a mail message.
 * 
 * @author philip
 */
public interface IReadonlyEmailAttachments extends IHasSize
{
  /**
   * Check if an attachment for the passed file name is contained.
   * 
   * @param sFilename
   *        The file name to be checked. The file name is case sensitive.
   * @return <code>true</code> if such an attachment is present,
   *         <code>false</code> otherwise.
   */
  boolean containsAttachment (@Nullable String sFilename);

  /**
   * @return A list with all registered file names or an empty list.
   */
  @Nonnull
  List <String> getAllAttachmentFilenames ();

  /**
   * @return A list of all attachments as Java Activation DataSource objects.
   */
  @Nonnull
  List <DataSource> getAsDataSourceList ();
}
