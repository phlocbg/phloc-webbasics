/**
 * Copyright (C) 2006-2012 phloc systems
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
package com.phloc.webbasics.app.html;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phloc.commons.GlobalDebug;
import com.phloc.commons.idfactory.GlobalIDFactory;
import com.phloc.commons.lang.StackTraceHelper;
import com.phloc.css.CCSS;
import com.phloc.css.CCSSNames;
import com.phloc.css.property.CCSSProperties;
import com.phloc.html.hc.IHCElementWithChildren;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCH1;
import com.phloc.html.hc.html.HCTextArea;
import com.phloc.html.hc.htmlext.HCUtils;

@ThreadSafe
public final class InternalErrorHandler {
  private static final Logger s_aLogger = LoggerFactory.getLogger (InternalErrorHandler.class);

  private InternalErrorHandler () {}

  public static void handleInternalError (final IHCElementWithChildren <?> aParent, final Throwable t) {
    // Log the error, to ensure the data is persisted!
    s_aLogger.error ("handleInternalError", t);

    final String sErrorNumber = "internal_error_" + GlobalIDFactory.getNewPersistentIntID ();

    // Get error stack trace
    final String sStackTrace = StackTraceHelper.getStackAsString (t, false);

    aParent.addChild (new HCH1 ("Internal error"));
    aParent.addChild (new HCDiv (HCUtils.nl2brList ("Sorry!\nAn internal error was encountered.\n\nAn automated error report was already sent to the technical responsible.\nWrite down your personal error number '" +
                                                    sErrorNumber +
                                                    "' for possible investigation.\n\nYou can continue your work.\nIn case this error occurs again avoid the actions leading to it until the problem is solved.")));

    if (GlobalDebug.isDebugMode ()) {
      final HCTextArea aStackTrace = new HCTextArea ("callstack").setValue (sStackTrace)
                                                                 .setRows (20)
                                                                 .addStyle (CCSSProperties.FONT_SIZE.newValue (CCSS.pt (10)))
                                                                 .addStyle (CCSSProperties.FONT_FAMILY.newValue (CCSSNames.FONT_MONOSPACE));

      aParent.addChild (aStackTrace);

      // In case an unexpected error occurs in the UnitTest, make the test fail!
      if (StackTraceHelper.containsUnitTestElement (t.getStackTrace ()))
        throw new Error ("Error executing unit test", t);
    }
  }
}
