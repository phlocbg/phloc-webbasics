package com.phloc.webctrls.bloodhound;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.Nonempty;
import com.phloc.commons.collections.ArrayHelper;
import com.phloc.commons.collections.ContainerHelper;
import com.phloc.html.js.builder.AbstractJSInvocation;
import com.phloc.html.js.builder.IJSExpression;
import com.phloc.html.js.builder.IJSGeneratable;
import com.phloc.html.js.builder.JSAnonymousFunction;
import com.phloc.html.js.builder.JSArray;
import com.phloc.html.js.builder.JSExpr;

/**
 * Special Bloodhound invocation. Offers all methods as of 0.10.2
 * 
 * @author Philip Helger
 */
public class BloodhoundJSInvocation extends AbstractJSInvocation <BloodhoundJSInvocation>
{
  public BloodhoundJSInvocation (final IJSGeneratable aType)
  {
    super (aType);
  }

  public BloodhoundJSInvocation (@Nullable final IJSExpression aLhs, @Nonnull @Nonempty final String sMethod)
  {
    super (aLhs, sMethod);
  }

  /**
   * Invoke an arbitrary function on this jQuery object.
   * 
   * @param sMethod
   *        The method to be invoked. May neither be <code>null</code> nor
   *        empty.
   * @return A new jQuery invocation object. Never <code>null</code>.
   */
  @Nonnull
  public BloodhoundJSInvocation bloodhoundInvoke (@Nonnull @Nonempty final String sMethod)
  {
    return new BloodhoundJSInvocation (this, sMethod);
  }

  @Nonnull
  public BloodhoundJSInvocation initialize ()
  {
    return bloodhoundInvoke ("initialize");
  }

  @Nonnull
  public BloodhoundJSInvocation initialize (final boolean bReinitialize)
  {
    return initialize ().arg (bReinitialize);
  }

  @Nonnull
  public BloodhoundJSInvocation add (@Nullable final BloodhoundDatum aDatum)
  {
    if (aDatum != null)
      return add (ContainerHelper.newList (aDatum));
    return this;
  }

  @Nonnull
  public BloodhoundJSInvocation add (@Nullable final BloodhoundDatum... aDatums)
  {
    if (ArrayHelper.isNotEmpty (aDatums))
      return add (ContainerHelper.newList (aDatums));
    return this;
  }

  @Nonnull
  public BloodhoundJSInvocation add (@Nullable final List <? extends BloodhoundDatum> aDatums)
  {
    if (ContainerHelper.isNotEmpty (aDatums))
    {
      final JSArray aArray = new JSArray ();
      for (final BloodhoundDatum aDatum : aDatums)
        aArray.add (aDatum.getAsJSObject ());
      return bloodhoundInvoke ("add").arg (aArray);
    }
    return this;
  }

  @Nonnull
  public BloodhoundJSInvocation clear ()
  {
    return bloodhoundInvoke ("clear");
  }

  @Nonnull
  public BloodhoundJSInvocation clearPrefetchCache ()
  {
    return bloodhoundInvoke ("clearPrefetchCache");
  }

  @Nonnull
  public BloodhoundJSInvocation clearRemoteCache ()
  {
    return bloodhoundInvoke ("clearRemoteCache");
  }

  @Nonnull
  public BloodhoundJSInvocation get ()
  {
    return bloodhoundInvoke ("get");
  }

  /**
   * Computes a set of suggestions for <code>query</code>. <code>cb</code> will
   * be invoked with an array of datums that represent said set. <code>cb</code>
   * will always be invoked once synchronously with suggestions that were
   * available on the client. If those suggestions are insufficient (# of
   * suggestions is less than <code>limit</code>) and remote was configured,
   * <code>cb</code> may also be invoked asynchronously with the suggestions
   * available on the client mixed with suggestions from the <code>remote</code>
   * source.
   * 
   * @param sQuery
   *        Query string
   * @param aCallback
   *        Callback function. Takes one argument: array of suggestions
   * @return new {@link BloodhoundJSInvocation}
   */
  @Nonnull
  public BloodhoundJSInvocation get (@Nonnull final String sQuery, @Nonnull final JSAnonymousFunction aCallback)
  {
    return get (JSExpr.lit (sQuery), aCallback);
  }

  /**
   * Computes a set of suggestions for <code>query</code>. <code>cb</code> will
   * be invoked with an array of datums that represent said set. <code>cb</code>
   * will always be invoked once synchronously with suggestions that were
   * available on the client. If those suggestions are insufficient (# of
   * suggestions is less than <code>limit</code>) and remote was configured,
   * <code>cb</code> may also be invoked asynchronously with the suggestions
   * available on the client mixed with suggestions from the <code>remote</code>
   * source.
   * 
   * @param aQuery
   *        Query string
   * @param aCallback
   *        Callback function. Takes one argument: array of suggestions
   * @return new {@link BloodhoundJSInvocation}
   */
  @Nonnull
  public BloodhoundJSInvocation get (@Nonnull final IJSExpression aQuery, @Nonnull final IJSExpression aCallback)
  {
    return get ().arg (aQuery).arg (aCallback);
  }

  /**
   * To be used as the <code>source</code> parameter for Typeahead integration.
   * 
   * @return Invocation of <code>ttAdapter</code>
   */
  @Nonnull
  public BloodhoundJSInvocation ttAdapter ()
  {
    return bloodhoundInvoke ("ttAdapter");
  }
}