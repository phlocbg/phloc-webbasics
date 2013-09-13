package com.phloc.webctrls.custom.tabbox;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.id.IHasID;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.string.StringHelper;
import com.phloc.commons.string.ToStringGenerator;
import com.phloc.commons.url.ISimpleURL;
import com.phloc.commons.url.SimpleURL;
import com.phloc.html.hc.IHCNode;

/**
 * Represents a single tab within a tab box
 * 
 * @author Philip Helger
 */
public class Tab implements IHasID <String>, Serializable
{
  private final String m_sID;
  private final boolean m_bHasGeneratedID;
  private IHCNode m_aLabel;
  private IHCNode m_aContent;
  private boolean m_bDisabled;

  public Tab (@Nullable final String sID,
              @Nullable final IHCNode aLabel,
              @Nullable final IHCNode aContent,
              final boolean bDisabled)
  {
    if (StringHelper.hasText (sID))
    {
      m_sID = sID;
      m_bHasGeneratedID = false;
    }
    else
    {
      m_sID = GlobalIDFactory.getNewStringID ();
      m_bHasGeneratedID = true;
    }
    m_aLabel = aLabel;
    m_aContent = aContent;
    m_bDisabled = bDisabled;
  }

  @Nonnull
  @Nonempty
  public String getID ()
  {
    return m_sID;
  }

  public boolean isGeneratedID ()
  {
    return m_bHasGeneratedID;
  }

  @Nonnull
  public ISimpleURL getLinkURL ()
  {
    return new SimpleURL ("#" + m_sID);
  }

  @Nullable
  public IHCNode getLabel ()
  {
    return m_aLabel;
  }

  @Nonnull
  public Tab setLabel (@Nullable final IHCNode aLabel)
  {
    m_aLabel = aLabel;
    return this;
  }

  @Nullable
  public IHCNode getContent ()
  {
    return m_aContent;
  }

  @Nonnull
  public Tab setContent (@Nullable final IHCNode aContent)
  {
    m_aContent = aContent;
    return this;
  }

  public boolean isDisabled ()
  {
    return m_bDisabled;
  }

  @Nonnull
  public Tab setDisabled (final boolean bDisabled)
  {
    m_bDisabled = bDisabled;
    return this;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("ID", m_sID)
                                       .append ("generatedID", m_bHasGeneratedID)
                                       .append ("label", m_aLabel)
                                       .append ("content", m_aContent)
                                       .append ("disabled", m_bDisabled)
                                       .toString ();
  }
}
