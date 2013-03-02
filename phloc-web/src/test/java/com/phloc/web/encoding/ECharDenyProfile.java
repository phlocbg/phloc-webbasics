package com.phloc.web.encoding;

import javax.annotation.Nonnull;

public enum ECharDenyProfile
{
  NONE (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return true;
    }
  }),
  ALPHA (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isAlpha (codepoint);
    }
  }),
  ALPHANUM (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isAlphaDigit (codepoint);
    }
  }),
  FRAGMENT (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isFragment (codepoint);
    }
  }),
  IFRAGMENT (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_ifragment (codepoint);
    }
  }),
  PATH (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isPath (codepoint);
    }
  }),
  IPATH (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_ipath (codepoint);
    }
  }),
  IUSERINFO (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iuserinfo (codepoint);
    }
  }),
  USERINFO (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isUserInfo (codepoint);
    }
  }),
  QUERY (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isQuery (codepoint);
    }
  }),
  IQUERY (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iquery (codepoint);
    }
  }),
  SCHEME (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isScheme (codepoint);
    }
  }),
  PATHNODELIMS (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isPathNoDelims (codepoint);
    }
  }),
  IPATHNODELIMS (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_ipathnodelims (codepoint);
    }
  }),
  IPATHNODELIMS_SEG (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_ipathnodelims (codepoint) && codepoint != '@' && codepoint != ':';
    }
  }),
  IREGNAME (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iregname (codepoint);
    }
  }),
  IHOST (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_ihost (codepoint);
    }
  }),
  IPRIVATE (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iprivate (codepoint);
    }
  }),
  RESERVED (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isReserved (codepoint);
    }
  }),
  IUNRESERVED (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iunreserved (codepoint);
    }
  }),
  UNRESERVED (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isUnreserved (codepoint);
    }
  }),
  SCHEMESPECIFICPART (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_iunreserved (codepoint) &&
             !CharUtils.isReserved (codepoint) &&
             !CharUtils.is_iprivate (codepoint) &&
             !CharUtils.isPctEnc (codepoint) &&
             codepoint != '#';
    }
  }),
  AUTHORITY (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.is_regname (codepoint) &&
             !CharUtils.isUserInfo (codepoint) &&
             !CharUtils.isGenDelim (codepoint);
    }
  }),
  ASCIISANSCRLF (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.inRange (codepoint, 1, 9) && !CharUtils.inRange (codepoint, 14, 127);
    }
  }),
  PCT (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.isPctEnc (codepoint);
    }
  }),
  STD3ASCIIRULES (new ICodepointFilter ()
  {
    public boolean accept (final int codepoint)
    {
      return !CharUtils.inRange (codepoint, 0x0000, 0x002C) &&
             !CharUtils.inRange (codepoint, 0x002E, 0x002F) &&
             !CharUtils.inRange (codepoint, 0x003A, 0x0040) &&
             !CharUtils.inRange (codepoint, 0x005B, 0x005E) &&
             !CharUtils.inRange (codepoint, 0x0060, 0x0060) &&
             !CharUtils.inRange (codepoint, 0x007B, 0x007F);
    }
  });
  private final ICodepointFilter m_aFilter;

  private ECharDenyProfile (final ICodepointFilter aFilter)
  {
    m_aFilter = aFilter;
  }

  @Nonnull
  public ICodepointFilter getFilter ()
  {
    return m_aFilter;
  }

  public boolean check (final int codepoint)
  {
    return m_aFilter.accept (codepoint);
  }
}
