package co.lemonlabs.android.slidingdebugmenu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import co.lemonlabs.android.slidingdebugmenu.SlidingDebugMenu;
import dagger.ObjectGraph;

import javax.inject.Inject;

public class DemoActivity extends Activity {

    @Inject SlidingDebugMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        ObjectGraph og = ObjectGraph.create(new DemoModule(this));
        og.inject(this);

        // show/hide the settings at the bottom of the drawer
        menu.setDrawerSettingsVisible(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addView(menu);
    }

    @Override
    public void onStop() {
        menu.onStop();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        menu.onStart();
    }
}
