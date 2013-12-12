/**
 * Copyright (C) 2013 phloc systems
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
package com.phloc.report.pdf.element;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.phloc.report.pdf.render.RenderPreparationContext;
import com.phloc.report.pdf.render.RenderingContext;
import com.phloc.report.pdf.spec.SizeSpec;

/**
 * A page break that ensures a new page is started afterwards.
 * 
 * @author Philip Helger
 */
public class PLPageBreak extends AbstractPLElement <PLPageBreak>
{
  private final boolean m_bForcePageBreak;

  public PLPageBreak (final boolean bForcePageBreak)
  {
    m_bForcePageBreak = bForcePageBreak;
  }

  public boolean isForcePageBreak ()
  {
    return m_bForcePageBreak;
  }

  @Override
  protected SizeSpec onPrepare (@Nonnull final RenderPreparationContext aCtx) throws IOException
  {
    // Use the fixed size
    return SizeSpec.SIZE0;
  }

  @Override
  protected void onPerform (@Nonnull final RenderingContext aCtx) throws IOException
  {}
}
