package com.phloc.webbasics.app.security;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.OverrideOnDemand;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.commons.url.SimpleURL;
import com.phloc.css.DefaultCSSClassProvider;
import com.phloc.css.ICSSClassProvider;
import com.phloc.html.hc.IHCNode;
import com.phloc.html.hc.html.AbstractHCCell;
import com.phloc.html.hc.html.HCButton_Submit;
import com.phloc.html.hc.html.HCCenter;
import com.phloc.html.hc.html.HCCol;
import com.phloc.html.hc.html.HCDiv;
import com.phloc.html.hc.html.HCEdit;
import com.phloc.html.hc.html.HCEditPassword;
import com.phloc.html.hc.html.HCForm;
import com.phloc.html.hc.html.HCRow;
import com.phloc.html.hc.html.HCTable;
import com.phloc.html.hc.impl.HCNodeList;
import com.phloc.webbasics.EWebBasicsText;
import com.phloc.webbasics.app.html.HTMLCreationManager;

public class LoginHTML extends HTMLCreationManager {
  public static final String REQUEST_ATTR_USERID = "userid";
  public static final String REQUEST_ATTR_PASSWORD = "password";
  protected static final ICSSClassProvider CSS_CLASS_LOGIN_APPLOGO = DefaultCSSClassProvider.create ("login_applogo");
  protected static final ICSSClassProvider CSS_CLASS_LOGIN_HEADER = DefaultCSSClassProvider.create ("login_appheader");
  protected static final ICSSClassProvider CSS_CLASS_LOGIN_ERRORMSG = DefaultCSSClassProvider.create ("login_errormsg");

  private final boolean m_bLoginError;

  public LoginHTML (final boolean bLoginError) {
    m_bLoginError = bLoginError;
  }

  @OverrideOnDemand
  protected boolean showHeaderText () {
    return false;
  }

  @OverrideOnDemand
  protected IHCNode getLoginScreen (final Locale aDisplayLocale) {
    final HCNodeList ret = new HCNodeList ();
    final HCCenter aCenter = ret.addAndReturnChild (new HCCenter ());
    final HCForm aForm = aCenter.addAndReturnChild (new HCForm (new SimpleURL ()));
    aForm.setSubmitPressingEnter (true);

    aForm.addChild (new HCDiv ().addClass (CSS_CLASS_LOGIN_APPLOGO));
    if (showHeaderText ())
      aForm.addChild (new HCDiv (EWebBasicsText.LOGIN_HEADER.getDisplayText (aDisplayLocale)).addClass (CSS_CLASS_LOGIN_HEADER));
    if (m_bLoginError)
      aForm.addChild (new HCDiv (EWebBasicsText.LOGIN_ERROR_MSG.getDisplayText (aDisplayLocale)).addClass (CSS_CLASS_LOGIN_ERRORMSG));
    final HCTable aTable = aForm.addAndReturnChild (new HCTable (new HCCol (200), HCCol.star ()));
    HCRow aRow = aTable.addBodyRow ();
    aRow.addCell (EWebBasicsText.LOGIN_FIELD_USERNAME.getDisplayText (aDisplayLocale));
    aRow.addCell (new HCEdit (REQUEST_ATTR_USERID));

    aRow = aTable.addBodyRow ();
    aRow.addCell (EWebBasicsText.LOGIN_FIELD_PASSWORD.getDisplayText (aDisplayLocale));
    aRow.addCell (new HCEditPassword (REQUEST_ATTR_PASSWORD));

    // Submit button
    final AbstractHCCell aCell = aTable.addBodyRow ().addCell ().setColspan (aTable.getColumnCount ());
    aCell.addChild (new HCButton_Submit (EWebBasicsText.LOGIN_BUTTON_SUBMIT.getDisplayText (aDisplayLocale)));

    return ret;
  }

  @Override
  @Nonnull
  protected final List <String> getAllAreaIDs () {
    // Name of the area ID is used in CSS!
    return ContainerHelper.newList ("login");
  }

  @Override
  @Nullable
  protected final IHCNode getContentOfArea (@Nonnull final String sAreaID, @Nonnull final Locale aDisplayLocale) {
    return getLoginScreen (aDisplayLocale);
  }
}
