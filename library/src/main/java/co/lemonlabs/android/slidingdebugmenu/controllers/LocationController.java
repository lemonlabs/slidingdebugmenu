/*
 * Copyright (C) 2014 Lemon Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.lemonlabs.android.slidingdebugmenu.controllers;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import de.greenrobot.event.EventBus;
import co.lemonlabs.android.slidingdebugmenu.modules.events.LocationEvent;

/**
 * Created by balysv on 14/01/14.
 * www.lemonlabs.co
 */
public class LocationController implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static LocationController instance;

    private LocationClient mLocationClient;

    public static LocationController newInstance(Context context) {
        if (instance == null)
            instance = new LocationController(context);
        return instance;
    }

    private LocationController(Context context) {
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS)
            mLocationClient = new LocationClient(context, this, this);
        else EventBus.getDefault().post(new LocationEvent(null));
    }

    @Override
    public void onConnected(Bundle bundle) {
        final Location location = mLocationClient.getLastLocation();
        EventBus.getDefault().post(new LocationEvent(location));
    }

    @Override
    public void onDisconnected() {
        mLocationClient.removeLocationUpdates(mLocationListener);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        EventBus.getDefault().post(new LocationEvent(null));
    }

    /**
     * Connect to GooglePlayServices location client
     */
    public void connect() {
        if (mLocationClient != null) mLocationClient.connect();
    }

    /**
     * Disconnect from GooglePlayServices location client
     */
    public void disconnect() {
        if (mLocationClient != null) mLocationClient.disconnect();

    }

    /**
     * Get last known location
     *
     * @return
     */
    public Location getLastLocation() {
        return mLocationClient != null ? mLocationClient.getLastLocation() : null;
    }

    /**
     * Request for a new location. Callbacks can be received by handling
     * {@link co.lemonlabs.android.slidingdebugmenu.modules.events.LocationEvent} using
     * {@link de.greenrobot.event.EventBus}.
     */
    public void requestLocation() {
        if (mLocationClient != null)
            mLocationClient.requestLocationUpdates(new LocationRequest(), mLocationListener);
    }

    /**
     * Simple location listener that pushes an event on location updates
     */
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            EventBus.getDefault().post(new LocationEvent(location));
        }
    };
}
