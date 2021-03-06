/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.core.controller.util.indicator;

import android.app.Activity;
import android.app.ProgressDialog;

import com.itude.mobile.mobbl.core.services.MBLocalizationService;
import com.itude.mobile.mobbl.core.util.MBCustomAttributeContainer;

/**
 * {@link MBCountingIndicator} class defines an Activity indicator
 */
public final class MBActivityIndicator extends MBCountingIndicator
{

  private ProgressDialog _dialog = null;

  /**
   * Default private constructor
   */
  MBActivityIndicator()
  {
  }

  /**
   * @see com.itude.mobile.mobbl.core.controller.util.indicator.MBCountingIndicator#show(android.app.Activity, com.itude.mobile.mobbl.core.util.MBCustomAttributeContainer)
   */
  @Override
  protected void show(final Activity activity, MBCustomAttributeContainer customAttributes)
  {

    _dialog = ProgressDialog.show(activity, MBLocalizationService.getInstance().getTextForKey("title_loading"), MBLocalizationService
        .getInstance().getTextForKey("msg_loading"), true, false);
  }

  /**
   * @see com.itude.mobile.mobbl.core.controller.util.indicator.MBCountingIndicator#dismiss(android.app.Activity)
   */
  @Override
  protected void dismiss(final Activity activity)
  {

    _dialog.dismiss();
    _dialog = null;
  }

}
