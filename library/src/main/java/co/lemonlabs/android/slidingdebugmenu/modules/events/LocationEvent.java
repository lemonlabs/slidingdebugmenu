package co.lemonlabs.android.slidingdebugmenu.modules.events;

import android.location.Location;

import com.google.common.base.Optional;

/**
 * Created by balysv on 14/01/14.
 * www.lemonlabs.co
 */
public class LocationEvent {

    public final Optional<Location> location;

    public LocationEvent(Optional<Location> location) {
        this.location = location;
    }
}
