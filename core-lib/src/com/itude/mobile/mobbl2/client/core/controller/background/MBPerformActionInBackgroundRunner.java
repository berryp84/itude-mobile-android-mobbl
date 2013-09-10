package com.itude.mobile.mobbl2.client.core.controller.background;

import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBActionDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.controller.util.indicator.MBIndicator;

public class MBPerformActionInBackgroundRunner extends MBApplicationControllerBackgroundRunner<Object[]>
{

  final MBIndicator  _indicator;
  MBOutcome          _outcome          = null;
  MBActionDefinition _actionDefinition = null;

  public MBPerformActionInBackgroundRunner(MBIndicator indicator)
  {
    _indicator = indicator;
  }

  public void setOutcome(MBOutcome mbOutcome)
  {
    _outcome = mbOutcome;
  }

  public void setActionDefinition(MBActionDefinition actionDef)
  {
    _actionDefinition = actionDef;
  }

  @Override
  protected Object[] doInBackground(Object[]... params)
  {
    try
    {
      getController().performAction(_outcome, _actionDefinition);
      return null;
    }
    finally
    {
      _indicator.release();
    }
  }

}
