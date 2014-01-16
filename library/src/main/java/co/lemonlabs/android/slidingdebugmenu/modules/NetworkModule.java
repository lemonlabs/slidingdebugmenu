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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import co.lemonlabs.android.slidingdebugmenu.R;
import co.lemonlabs.android.slidingdebugmenu.controllers.NetworkController;
import co.lemonlabs.android.slidingdebugmenu.modules.events.NetworkChangeEvent;
import co.lemonlabs.android.slidingdebugmenu.views.ModuleSimpleProperty;

/**
 * Module that provides an interface allowing to switch
 * and view the state of Wifi, Mobile network and Bluetooth.
 * A manually registered broadcast receiver is used, so be
 * sure to call {@link #onStop() onStop} and {@link #onStart() onStart}.
 */
public class NetworkModule extends MenuModule implements View.OnClickListener {

    private static final String TITLE = "Network";

    private NetworkController networkController;

    private ModuleSimpleProperty wifiState;
    private ModuleSimpleProperty mobileState;
    private ModuleSimpleProperty bluetoothState;

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public View getContent(Context context) {

        networkController = NetworkController.newInstance(context);

        ViewGroup v = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.sdm__module_network, null);
        assert v != null;

        v.findViewById(R.id.sdm__network_wifi_toggle).setOnClickListener(this);
        v.findViewById(R.id.sdm__network_mobile_toggle).setOnClickListener(this);
        v.findViewById(R.id.sdm__network_bluetooth_toggle).setOnClickListener(this);

        wifiState = new ModuleSimpleProperty(context, R.string.sdm__module_network_wifi);
        v.addView(wifiState);

        mobileState = new ModuleSimpleProperty(context, R.string.sdm__module_network_mobile);
        v.addView(mobileState);

        bluetoothState = new ModuleSimpleProperty(context, R.string.sdm__module_network_bluetooth);
        v.addView(bluetoothState);

        return v;
    }

    /**
     * Receive events from broadcast receiver and update UI
     *
     * @param e
     */
    public void onEventMainThread(NetworkChangeEvent e) {
        final String wifiStateString = e.wifiState.name().toLowerCase();
        final String mobileStateString = e.mobileState.name().toLowerCase();

        wifiState.setPropertyValue(Character.toUpperCase(wifiStateString.charAt(0)) + wifiStateString.substring(1));
        mobileState.setPropertyValue(Character.toUpperCase(mobileStateString.charAt(0)) + mobileStateString.substring(1));
        bluetoothState.setPropertyValue(e.bluetoothState.name().replace("_", " "));
    }

    @Override
    public void onStop() {
        networkController.unregisterReceiver();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        networkController.registerReceiver();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.sdm__network_wifi_toggle) {
            networkController.setWifiEnabled(!networkController.isWifiEnabled());
        } else if (id == R.id.sdm__network_mobile_toggle) {
            networkController.setMobileNetworkEnabled(!networkController.isMobileNetworkEnabled());
        } else if (id == R.id.sdm__network_bluetooth_toggle) {
            networkController.setBluetoothEnabled(!networkController.isBluetoothEnabled());
        }
    }
}
