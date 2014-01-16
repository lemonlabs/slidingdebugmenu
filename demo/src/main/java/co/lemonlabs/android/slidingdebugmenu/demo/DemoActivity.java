package co.lemonlabs.android.slidingdebugmenu.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import co.lemonlabs.android.slidingdebugmenu.SlidingDebugMenu;

public class DemoActivity extends ActionBarActivity {

    private SlidingDebugMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        menu = SlidingDebugMenu.attach(this);

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
