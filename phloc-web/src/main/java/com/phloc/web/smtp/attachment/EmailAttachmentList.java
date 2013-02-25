/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.web.smtp.attachment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.annotations.ReturnsMutableObject;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.hash.HashCodeGenerator;
import com.phloc.commons.io.IInputStreamProvider;
import com.phloc.commons.state.EChange;
import com.phloc.commons.string.ToStringGenerator;

/**
 * This is the default implementation of the {@link IEmailAttachmentList}
 * interface.
 * 
 * @author philip
 */
@NotThreadSafe
public class EmailAttachmentList implements IEmailAttachmentList
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (EmailAttachmentList.class);

  private final Map <String, IEmailAttachment> m_aMap = new LinkedHashMap <String, IEmailAttachment> ();

  public EmailAttachmentList ()
  {}

  @Nonnegative
  public int size ()
  {
    return m_aMap.size ();
  }

  public boolean isEmpty ()
  {
    return m_aMap.isEmpty ();
  }

  public void addAttachment (@Nonnull final String sFilename, @Nonnull final IInputStreamProvider aISS)
  {
    addAttachment (new EmailAttachment (sFilename, aISS));
  }

  public void addAttachment (@Nonnull final IEmailAttachment aAttachment)
  {
    if (aAttachment == null)
      throw new NullPointerException ("attachment");

    final String sKey = aAttachment.getFilename ();
    if (m_aMap.containsKey (sKey))
      s_aLogger.warn ("Overwriting email attachment with filename '" + sKey + "'");
    m_aMap.put (sKey, aAttachment);
  }

  @Nonnull
  public EChange removeAttachment (@Nullable final String sFilename)
  {
    return EChange.valueOf (m_aMap.remove (sFilename) != null);
  }

  public boolean containsAttachment (@Nullable final String sFilename)
  {
    return m_aMap.containsKey (sFilename);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllAttachmentFilenames ()
  {
    return ContainerHelper.newList (m_aMap.keySet ());
  }

  @Nonnull
  @ReturnsMutableObject (reason = "speed")
  Collection <IEmailAttachment> directGetAllAttachments ()
  {
    return m_aMap.values ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <IEmailAttachment> getAllAttachments ()
  {
    return ContainerHelper.newList (m_aMap.values ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <DataSource> getAsDataSourceList ()
  {
    final List <DataSource> ret = new ArrayList <DataSource> ();
    for (final IEmailAttachment aAttachment : m_aMap.values ())
      ret.add (aAttachment.getAsDataSource ());
    return ret;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final EmailAttachmentList rhs = (EmailAttachmentList) o;
    return m_aMap.equals (rhs.m_aMap);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMap).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("map", m_aMap).toString ();
  }
}
