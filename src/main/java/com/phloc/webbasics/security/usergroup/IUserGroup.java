package com.phloc.webbasics.security.usergroup;

import com.phloc.commons.id.IHasID;
import com.phloc.commons.name.IHasName;
import com.phloc.webbasics.security.role.IRoleContainer;
import com.phloc.webbasics.security.user.IUserContainer;

/**
 * Represents a single user group encapsulating 0-n users.
 * 
 * @author philip
 */
public interface IUserGroup extends IHasID <String>, IHasName, IUserContainer, IRoleContainer
{
  /* empty */
}
