package com.itude.mobile.mobbl.core.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.itude.mobile.mobbl.core.controller.MBViewManager.MBViewState;
import com.itude.mobile.mobbl.core.controller.util.MBBasicViewController;
import com.itude.mobile.mobbl.core.util.Constants;

public class MBPageStackController
{
  private final int                _id;
  private final String             _name;
  private final String             _mode;
  private final MBDialogController _parent;

  public MBPageStackController(MBDialogController parent, int id, String name, String mode)
  {
    _parent = parent;
    _id = id;
    _name = name;
    _mode = mode;
  }

  public int getId()
  {
    return _id;
  }

  public String getName()
  {
    return _name;
  }

  public String getMode()
  {
    return _mode;
  }

  public MBDialogController getParent()
  {
    return _parent;
  }

  void showPage(MBDialogController.ShowPageEntry entry)
  {

    if ("POP".equals(entry.getDisplayMode()))
    {
      getParent().popView();
    }
    else if ((Constants.C_DISPLAY_MODE_REPLACE.equals(entry.getDisplayMode()) //
             || Constants.C_DISPLAY_MODE_BACKGROUNDPIPELINEREPLACE.equals(entry.getDisplayMode()))
             || ("SINGLE".equals(getMode()) && entry.getPage().getCurrentViewState() != MBViewState.MBViewStateModal))
    {
      entry.setAddToBackStack(false);
    }
    else if ("REPLACEDIALOG".equals(entry.getDisplayMode()) && !getParent().getFragmentStack().isBackStackEmpty())
    {
      getParent().getFragmentStack().emptyBackStack(false);
    }

    MBBasicViewController fragment = MBApplicationFactory.getInstance().createFragment(entry.getPage().getPageName());
    fragment.setPage(entry.getPage());
    fragment.setDialogController(getParent());
    Bundle args = new Bundle();
    args.putString("id", entry.getId());
    fragment.setArguments(args);

    FragmentTransaction transaction = getParent().getSupportFragmentManager().beginTransaction();

    if (entry.getPage().getCurrentViewState() == MBViewState.MBViewStateModal
        || MBApplicationController.getInstance().getModalPageID() != null)
    {
      String modalPageId = MBApplicationController.getInstance().getModalPageID();

      if (entry.isAddToBackStack())
      {
        transaction.addToBackStack(entry.getId());
      }

      if (modalPageId != null && MBApplicationController.getInstance().getOutcomeWhichCausedModal() != null)
      {
        entry.setDisplayMode(MBApplicationController.getInstance().getOutcomeWhichCausedModal().getDisplayMode());
      }

      boolean fullscreen = false;
      boolean cancelable = false;

      if ("MODAL".equals(entry.getDisplayMode()))
      {
        fullscreen = true;
        cancelable = true;
      }
      if (entry.getDisplayMode() != null)
      {
        if (entry.getDisplayMode().contains("FULLSCREEN"))
        {
          fullscreen = true;
        }

        if (entry.getDisplayMode().contains("WITHCLOSEBUTTON"))
        {
          args.putBoolean("closable", true);
          fragment.setArguments(args);
        }
      }
      if (fullscreen)
      {
        args.putBoolean("fullscreen", true);
        fragment.setArguments(args);
      }

      if (cancelable)
      {
        args.putBoolean("cancelable", true);
        fragment.setArguments(args);
      }

      Fragment dialogFragment = getParent().getSupportFragmentManager().findFragmentByTag(modalPageId);
      if (dialogFragment != null && !getParent().getFragmentStack().isBackStackEmpty())
      {
        getParent().getSupportFragmentManager().popBackStack();
      }

      transaction.add(fragment, entry.getId());
    }
    else
    {
      if (entry.isAddToBackStack())
      {
        transaction.addToBackStack(entry.getId());
      }
      else
      {
        if (!getParent().getFragmentStack().isBackStackEmpty())
        {
          getParent().getSupportFragmentManager().popBackStack();
          transaction.addToBackStack(entry.getId());
        }
      }
      transaction.replace(getId(), fragment, entry.getId());
    }

    // commitAllowingStateLoss makes sure that the transaction is being
    // commit,
    // even when the target activity is stopped. For now, this comes with
    // the price,
    // that the page being displayed will lose its state after a
    // configuration change (e.g. an orientation change)
    transaction.commitAllowingStateLoss();

  }

}