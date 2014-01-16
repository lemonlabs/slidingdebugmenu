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

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import co.lemonlabs.android.slidingdebugmenu.receivers.NetworkReceiver;

/**
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */

public class NetworkController {

    public enum BluetoothState {
        On, Off, Turning_On, Turning_Off, Unknown
    }

    private static NetworkController instance;

    private WifiManager mWifiManager;
    private ConnectivityManager mConnectivityManager;
    private BluetoothAdapter mBluetoothAdapter;

    private NetworkReceiver mReceiver;

    private transient Context mContext;

    public static NetworkController newInstance(Context context) {
        if (instance == null)
            instance = new NetworkController(context);
        return instance;
    }

    /**
     * Controller responsible for switching states related to networks.
     * E.g. wifi, mobile networks
     *
     * @param context
     */
    private NetworkController(Context context) {
        mContext = context.getApplicationContext();
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    /**
     * True if WiFi enabled
     *
     * @return
     */
    public boolean isWifiEnabled() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * Set WiFi state
     *
     * @param enabled
     */
    public void setWifiEnabled(boolean enabled) {
        mWifiManager.setWifiEnabled(enabled);
    }

    /**
     * True if mobile network enabled
     *
     * @return
     */
    public boolean isMobileNetworkEnabled() {
        final NetworkInfo info = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (info != null && info.isConnected());
    }

    /**
     * Try to enabled/disable mobile network state using reflection.
     * Returns true if succeeded
     *
     * @param enabled
     * @return
     */
    public boolean setMobileNetworkEnabled(boolean enabled) {
        try {
            final Class conmanClass = Class.forName(mConnectivityManager.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(mConnectivityManager);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
            return true;
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
        }
        return false;
    }

    /**
     * True if bluetooth is enabled
     */
    public boolean isBluetoothEnabled() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * Try to enable/disabled bluetooth. Returns false if immediate
     * error occurs
     *
     * @param enabled
     * @return
     */
    public boolean setBluetoothEnabled(boolean enabled) {
        return enabled ? mBluetoothAdapter.enable() : mBluetoothAdapter.disable();
    }


    /**
     * Unregister network state broadcast receiver
     */
    public void unregisterReceiver() {
        try {
            mContext.unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Register network state broadcast receiver
     */
    public void registerReceiver() {
        if (mReceiver == null)
            mReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mReceiver, filter);
    }

}
