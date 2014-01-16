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

package co.lemonlabs.android.slidingdebugmenu.modules;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.common.base.Optional;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;
import co.lemonlabs.android.slidingdebugmenu.R;
import co.lemonlabs.android.slidingdebugmenu.controllers.LocationController;
import co.lemonlabs.android.slidingdebugmenu.modules.events.LocationEvent;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleSimpleProperty;

/**
 * Module that provides information about the current
 * location of the device, allows to get new locations,
 * go to settings and open maps.
 * GooglePlayServices {@link com.google.android.gms.location.LocationClient} is used,
 * so be sure to call {@link #onStop() onStop} and {@link #onStart() onStart}.
 * Module will not work in devices without GooglePlayServices installed.
 */
public class LocationModule extends MenuModule {

    private static final String TITLE = "Location";

    private LocationController mLocationController;

    private ModuleSimpleProperty latitude;
    private ModuleSimpleProperty longitude;
    private ModuleSimpleProperty accuracy;
    private ModuleSimpleProperty time;
    private ModuleSimpleProperty provider;

    private Optional<Location> mLocation;

    private boolean mPlayServicesAvailable;

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public View getContent(final Context context) {
        mPlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
        // add module contents only if Google Play Services are available
        if (mPlayServicesAvailable) {
            mLocationController = LocationController.newInstance(context);

            ViewGroup v = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sdm__module_location, null);
            assert v != null;

            final View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id = v.getId();
                    if (id == R.id.sdm__location_map) {
                        openMaps(context);
                    } else if (id == R.id.sdm__location_get) {
                        mLocationController.requestLocation();
                    } else if (id == R.id.sdm__location_settings) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }
            };

            v.findViewById(R.id.sdm__location_get).setOnClickListener(onClickListener);
            v.findViewById(R.id.sdm__location_settings).setOnClickListener(onClickListener);
            v.findViewById(R.id.sdm__location_map).setOnClickListener(onClickListener);

            latitude = new ModuleSimpleProperty(context, R.string.sdm__module_location_latitide);
            v.addView(latitude);

            longitude = new ModuleSimpleProperty(context, R.string.sdm__module_location_longitude);
            v.addView(longitude);

            accuracy = new ModuleSimpleProperty(context, R.string.sdm__module_location_accuracy);
            v.addView(accuracy);

            time = new ModuleSimpleProperty(context, R.string.sdm__module_location_time);
            v.addView(time);

            provider = new ModuleSimpleProperty(context, R.string.sdm__module_location_provider);
            v.addView(provider);
            return v;
        } else {
            TextView errorText = new TextView(context);
            errorText.setTextAppearance(context, R.style.Widget_Sdm_TextView_PropertyValue);
            errorText.setText(R.string.sdm__module_location_play_unavailable);
            return errorText;
        }
    }

    /**
     * Receive location update events
     *
     * @param e
     */
    public void onEventMainThread(LocationEvent e) {
        updateLocation(e.location);
    }

    @Override
    public void onStart() {
        if (mPlayServicesAvailable) {
            EventBus.getDefault().register(this);
            mLocationController.connect();
        }
    }

    @Override
    public void onStop() {
        if (mPlayServicesAvailable) {
            mLocationController.disconnect();
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * Update the module UI
     *
     * @param location
     */
    private void updateLocation(Optional<Location> location) {
        mLocation = location;
        if (location.isPresent()) {
            Location loc = location.get();
            latitude.setPropertyValue(String.valueOf(loc.getLatitude()));
            longitude.setPropertyValue(String.valueOf(loc.getLongitude()));
            accuracy.setPropertyValue(String.valueOf(loc.getAccuracy()) + "m");

            Date date = new Date(loc.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time.setPropertyValue(sdf.format(date));

            provider.setPropertyValue(loc.getProvider());
        } else {
            latitude.setPropertyValue(R.string.sdm__module_location_empty);
            longitude.setPropertyValue(R.string.sdm__module_location_empty);
            accuracy.setPropertyValue(R.string.sdm__module_location_empty);
            time.setPropertyValue(R.string.sdm__module_location_empty);
            provider.setPropertyValue(R.string.sdm__module_location_no_provider);
        }
    }


    /**
     * Tries to open an activity that handles geo coordinates
     *
     * @param context
     */
    private void openMaps(Context context) {
        try {
            if (mLocation.isPresent()) {
                Location loc = mLocation.get();
                String uri = "geo:" + loc.getLatitude() + "," + loc.getLongitude();
                context.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            } else {
                Toast.makeText(context, R.string.sdm__module_location_not_found, Toast.LENGTH_SHORT).show();
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.sdm__module_location_map_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
