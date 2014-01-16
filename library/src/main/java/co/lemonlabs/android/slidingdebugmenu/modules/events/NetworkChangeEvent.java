package co.lemonlabs.android.slidingdebugmenu.modules.events;

import android.net.NetworkInfo;

import static co.lemonlabs.android.slidingdebugmenu.controllers.NetworkController.BluetoothState;

/**
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */
public class NetworkChangeEvent {

    public final NetworkInfo.State wifiState;
    public final NetworkInfo.State mobileState;
    public final BluetoothState bluetoothState;

    public NetworkChangeEvent(NetworkInfo.State wifiState, NetworkInfo.State mobileState, BluetoothState bluetoothState) {
        this.wifiState = wifiState;
        this.mobileState = mobileState;
        this.bluetoothState = bluetoothState;
    }
}
