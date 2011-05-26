package com.itude.mobile.mobbl2.client.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.itude.mobile.mobbl2.client.core.controller.MBViewManagerFactory;
import com.itude.mobile.mobbl2.client.core.services.MBResourceService;
import com.itude.mobile.mobbl2.client.core.util.Constants;

public class MBSplashScreen extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);

    ImageView imageView = createView();

    setContentView(imageView);

    Thread thread = new Thread()
    {
      @Override
      public void run()
      {
        Intent viewManager = new Intent(getBaseContext(), MBViewManagerFactory.getViewManagerClass());
        startActivity(viewManager);
      }
    };
    // Keep splashscreen visible for at least 1 second
    new Handler().postDelayed(thread, 1000);
  }

  protected ImageView createView()
  {
    ImageView imageView = new ImageView(getApplicationContext());
    imageView.setImageDrawable(MBResourceService.getInstance().getImageByID(Constants.C_SPLASHSCREEN));
    return imageView;
  }
}
