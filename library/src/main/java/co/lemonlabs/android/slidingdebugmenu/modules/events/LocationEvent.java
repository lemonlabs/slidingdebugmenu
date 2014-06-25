package co.lemonlabs.android.slidingdebugmenu.modules.events;

import android.location.Location;

/**
 * Created by balysv on 14/01/14.
 * www.lemonlabs.co
 */
public class LocationEvent {

    public final Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }
}
