package com.phloc.webctrls.bootstrap.ext;

import java.util.Locale;

import javax.annotation.Nonnull;

import com.phloc.appbasics.security.login.ELoginResult;
import com.phloc.html.hc.html.HCBody;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCHtml;
import com.phloc.html.hc.html.HCSpan;
import com.phloc.scopes.web.domain.IRequestWebScopeWithoutResponse;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.LinkUtils;
import com.phloc.webbasics.login.CLogin;
import com.phloc.webbasics.login.LoginHTMLProvider;
import com.phloc.webctrls.bootstrap.BootstrapButton_Submit;
import com.phloc.webctrls.bootstrap.BootstrapContainer;
import com.phloc.webctrls.bootstrap.BootstrapFormActions;
import com.phloc.webctrls.bootstrap.BootstrapFormLabel;
import com.phloc.webctrls.bootstrap.BootstrapRow;
import com.phloc.webctrls.bootstrap.EBootstrapSpan;
import com.phloc.webctrls.bootstrap.derived.BootstrapErrorBox;
import com.phloc.webctrls.bootstrap.derived.BootstrapTableForm;

public class BootstrapLoginHTMLProvider extends LoginHTMLProvider
{
  public BootstrapLoginHTMLProvider (final boolean bLoginError, @Nonnull final ELoginResult eLoginResult)
  {
    super (bLoginError, eLoginResult);
  }

  @Override
  protected void fillBody (@Nonnull final IRequestWebScopeWithoutResponse aRequestScope,
                           @Nonnull final HCHtml aHtml,
                           @Nonnull final Locale aDisplayLocale)
  {
    final HCForm aForm = new HCForm (LinkUtils.getSelfHref ());
    if (showLoginError ())
      aForm.addChild (BootstrapErrorBox.create (getTextErrorMessage (aDisplayLocale)));

    final BootstrapTableForm aTable = aForm.addAndReturnChild (new BootstrapTableForm (new HCCol (170), HCCol.star ()));
    aTable.setStriped (false);

    // User name and password table
    final String sUserName = EWebBasicsText.EMAIL_ADDRESS.getDisplayText (aDisplayLocale);
    aTable.addItemRow (BootstrapFormLabel.create (sUserName),
                       new HCEdit (CLogin.REQUEST_ATTR_USERID).setPlaceholder (sUserName));
    final String sPassword = EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale);
    aTable.addItemRow (BootstrapFormLabel.create (sPassword),
                       new HCEditPassword (CLogin.REQUEST_ATTR_PASSWORD).setPlaceholder (sPassword));

    // Submit button
    aForm.addChild (new BootstrapFormActions ().addChild (BootstrapButton_Submit.create (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale))));

    final BootstrapRow aRow = new BootstrapRow ();
    aRow.addColumn (EBootstrapSpan.SPAN3);
    aRow.addColumn (EBootstrapSpan.SPAN6, aForm);
    aRow.addColumn (EBootstrapSpan.SPAN3);

    final BootstrapContainer aContentLayout = new BootstrapContainer (true);
    aContentLayout.setContent (aRow.build ());

    // Build body
    final HCBody aBody = aHtml.getBody ();
    final HCSpan aSpan = aBody.addAndReturnChild (new HCSpan ().setID (CLogin.LAYOUT_AREAID_LOGIN));
    aSpan.addChild (aContentLayout.build ());
  }
}
