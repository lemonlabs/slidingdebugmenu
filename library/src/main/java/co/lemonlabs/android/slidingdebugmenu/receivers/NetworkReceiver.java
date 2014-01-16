package co.lemonlabs.android.slidingdebugmenu.receivers;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;
import co.lemonlabs.android.slidingdebugmenu.modules.events.NetworkChangeEvent;

import static co.lemonlabs.android.slidingdebugmenu.controllers.NetworkController.BluetoothState;

/**
 * Receiver that handles wifi, mobile networks and
 * Bluetooth connectivity change intents and sends
 * a NetworkChangeEvent using EventBus
 * <p/>
 * Created by balysv on 10/01/14.
 * www.lemonlabs.co
 */
public class NetworkReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        final int bluetoothState = BluetoothAdapter.getDefaultAdapter().getState();


        EventBus.getDefault().post(new NetworkChangeEvent(
                (wifiInfo != null) ? wifiInfo.getState() : NetworkInfo.State.UNKNOWN,
                (mobileInfo != null) ? mobileInfo.getState() : NetworkInfo.State.UNKNOWN,
                getBluetoothState(bluetoothState))
        );
    }

    /**
     * Converts Bluetooth state representation to an Enum
     *
     * @param state
     * @return
     */
    private BluetoothState getBluetoothState(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_ON:
                return BluetoothState.On;
            case BluetoothAdapter.STATE_OFF:
                return BluetoothState.Off;
            case BluetoothAdapter.STATE_TURNING_ON:
                return BluetoothState.Turning_On;
            case BluetoothAdapter.STATE_TURNING_OFF:
                return BluetoothState.Turning_Off;
        }

        return BluetoothState.Unknown;
    }
}
