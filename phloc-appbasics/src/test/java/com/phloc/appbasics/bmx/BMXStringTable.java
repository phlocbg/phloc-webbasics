package com.phloc.appbasics.bmx;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.ReturnsMutableCopy;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.string.ToStringGenerator;

public class BMXStringTable
{
  private final SortedSet <String> m_aWords = new TreeSet <String> ();

  public BMXStringTable ()
  {}

  public void addWord (@Nullable final String sWord)
  {
    if (sWord != null)
      m_aWords.add (sWord);
  }

  public void addWords (@Nullable final Iterable <String> aWords)
  {
    if (aWords != null)
      for (final String sWord : aWords)
        addWord (sWord);
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllWords ()
  {
    return ContainerHelper.newList (m_aWords);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("words", m_aWords).toString ();
  }
}
