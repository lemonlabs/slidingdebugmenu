package co.lemonlabs.android.slidingdebugmenu.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import co.lemonlabs.android.slidingdebugmenu.SlidingDebugMenu;
import co.lemonlabs.android.slidingdebugmenu.modules.LocationModule;

public class DemoActivity extends ActionBarActivity {

    private SlidingDebugMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);

        // Remove module using editor
        SlidingDebugMenu.edit()
                .remove(LocationModule.class)
                .commit();

        // attach drawer
        menu = SlidingDebugMenu.attach(this);

        // ... some code

        // re-add module at position 1
        menu.addModule(1, new LocationModule(), false);

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
