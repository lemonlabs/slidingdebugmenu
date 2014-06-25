package co.lemonlabs.android.slidingdebugmenu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import co.lemonlabs.android.slidingdebugmenu.SlidingDebugMenu;
import co.lemonlabs.android.slidingdebugmenu.modules.LocationModule;

public class DemoActivity extends Activity {

    private SlidingDebugMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Remove module using editor
        SlidingDebugMenu.edit()
                .remove(LocationModule.class)
                .commit();

        // attach drawer
        menu = SlidingDebugMenu.attach(this, drawerLayout);

        // ... some code

        // re-add module at position 1
        menu.addModule(1, new LocationModule());

        // show/hide the settings at the bottom of the drawer
        menu.setDrawerSettingsVisible(true);
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
