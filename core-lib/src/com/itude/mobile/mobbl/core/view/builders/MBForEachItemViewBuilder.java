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
package com.itude.mobile.mobbl.core.view.builders;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.itude.mobile.mobbl.core.controller.MBApplicationController;
import com.itude.mobile.mobbl.core.view.MBForEachItem;

public class MBForEachItemViewBuilder extends MBViewBuilder
{

  public ViewGroup buildForEachItemView(MBForEachItem row)
  {
    LinearLayout rowView = new LinearLayout(MBApplicationController.getInstance().getBaseContext());
    buildChildren(row.getChildren(), rowView);
    getStyleHandler().applyStyle(row, rowView);
    return rowView;
  }

}
