package com.itude.mobile.mobbl2.client.core.view.components;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.itude.mobile.mobbl2.client.core.services.MBLocalizationService;
import com.itude.mobile.mobbl2.client.core.view.MBComponent;
import com.itude.mobile.mobbl2.client.core.view.MBPanel;
import com.itude.mobile.mobbl2.client.core.view.builders.MBStyleHandler;
import com.itude.mobile.mobbl2.client.core.view.builders.MBViewBuilderFactory;
import com.itude.mobile.mobbl2.client.core.view.components.MBSegmentedControlBar.MBOnSelectedListener;

public class MBSegmentedControlContainer extends RelativeLayout implements MBOnSelectedListener
{

  private final ArrayList<String>     _contentTitleStrings;
  private final ArrayList<View>       _contentItems;
  private int                         _selectedIndex;

  private final LinearLayout          _contentContainer;
  private final MBStyleHandler        _styleHandler;
  private final MBSegmentedControlBar _controlBar;

  public MBSegmentedControlContainer(Context context, MBPanel segmentedControlPanel)
  {
    super(context);

    _styleHandler = MBViewBuilderFactory.getInstance().getStyleHandler();

    _contentTitleStrings = new ArrayList<String>();
    _contentItems = new ArrayList<View>();
    _selectedIndex = 0;

    int childCount = segmentedControlPanel.getChildren().size();
    for (int i = 0; i < childCount; i++)
    {
      MBPanel childPanel = (MBPanel) segmentedControlPanel.getChildren().get(i);

      // Add the title string to the list so we can use this to create our controlbar
      _contentTitleStrings.add(MBLocalizationService.getInstance().getTextForKey(childPanel.getTitle()));

      // Determine if this child should be focused after processing all views and titles
      if (childPanel.isFocused())
      {
        _selectedIndex = i;
      }

      // Add all subchildren to a container that will be our content
      LinearLayout subChildContainer = new LinearLayout(context);
      subChildContainer.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
      subChildContainer.setOrientation(LinearLayout.VERTICAL);
      for (MBComponent subChild : childPanel.getChildren())
      {
        View contentView = subChild.buildView();
        subChildContainer.addView(contentView);
      }

      // Add this container to a ScrollView so all content items will have their own scrollview
      ScrollView subChildScrollView = new ScrollView(context);
      subChildScrollView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
      subChildScrollView.addView(subChildContainer);

      _contentItems.add(subChildScrollView);
    }

    /*
     * We've created our container, now let's add our buttons (tabs)
     */
    _controlBar = new MBSegmentedControlBar(context, _contentTitleStrings, segmentedControlPanel.getStyle());
    _controlBar.setMBOnSelectedListener(this);
    addView(_controlBar);

    /*
     * Items done. Time to create our content container
     */
    _contentContainer = new LinearLayout(context);
    _contentContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    _styleHandler.styleSegmentedControlContentContainer(_contentContainer, segmentedControlPanel);
    addView(_contentContainer);

    /*
     * Seems like everything is set up. Now let's trigger our focused tab
     */
    _controlBar.setFocusedItem(_selectedIndex);

    /*
     * We've got our elements on screen. 
     * Now we want to provide flexibility in where to show the controlbar and the content 
     */
    _styleHandler.styleSegmentedControlLayoutStructure(_controlBar, _contentContainer);
  }

  @Override
  public void onSelected(int selectedIndex, int itemCount)
  {
    /*
     * Update the content of our content container
     */
    _contentContainer.removeAllViews();
    _contentContainer.addView(_contentItems.get(selectedIndex));
  }

  public ArrayList<View> getContentItems()
  {
    return _contentItems;
  }

  public MBSegmentedControlBar getControlBar()
  {
    return _controlBar;
  }

}
