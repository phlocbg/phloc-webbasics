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
package com.phloc.webctrls.supplementary.bootstrap;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import com.phloc.commons.charset.CCharset;
import com.phloc.commons.io.resource.ClassPathResource;
import com.phloc.css.ECSSVersion;
import com.phloc.css.decl.CSSSelector;
import com.phloc.css.decl.CSSSelectorSimpleMember;
import com.phloc.css.decl.CascadingStyleSheet;
import com.phloc.css.decl.ICSSSelectorMember;
import com.phloc.css.decl.visit.CSSVisitor;
import com.phloc.css.decl.visit.DefaultCSSVisitor;
import com.phloc.css.reader.CSSReader;
import com.phloc.webctrls.bootstrap.EBootstrapCSSPathProvider;

public class MainExtractBootstrapCSSClasses
{
  public static void main (final String [] args)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromStream (new ClassPathResource (EBootstrapCSSPathProvider.BOOTSTRAP_232.getCSSItemPath (true)),
                                                               CCharset.CHARSET_UTF_8_OBJ,
                                                               ECSSVersion.CSS30);
    final Set <String> aClasses = new TreeSet <String> ();
    CSSVisitor.visitCSS (aCSS, new DefaultCSSVisitor ()
    {
      @Override
      public void onStyleRuleSelector (@Nonnull final CSSSelector aSelector)
      {
        for (final ICSSSelectorMember aMember : aSelector.getAllMembers ())
          if (aMember instanceof CSSSelectorSimpleMember)
          {
            final CSSSelectorSimpleMember aSM = (CSSSelectorSimpleMember) aMember;
            if (aSM.isClass ())
              aClasses.add (aSM.getValue ());
            else
              if (aSM.getValue ().contains ("."))
                System.out.println (aSM.getValue ());
          }
      }
    });
    for (final String sClass : aClasses)
    {
      final String sClassName = sClass.substring (1);
      String sFieldName = sClassName.toUpperCase (Locale.US);
      sFieldName = sFieldName.replace ('-', '_');
      System.out.println ("public static final ICSSClassProvider " +
                          sFieldName +
                          " = DefaultCSSClassProvider.create (\"" +
                          sClassName +
                          "\");");
    }

    System.out.println ();

    // Icons
    for (final String sClass : aClasses)
      if (sClass.startsWith (".icon-"))
      {
        final String sClassName = sClass.substring (1);
        String sFieldName = sClassName.toUpperCase (Locale.US);
        sFieldName = sFieldName.replace ('-', '_');
        System.out.println (sFieldName.substring ("icon-".length ()) + " (CBootstrapCSS." + sFieldName + "),");
      }
  }
}
