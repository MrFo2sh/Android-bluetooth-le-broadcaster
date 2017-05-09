package grad.uz.com.collegeapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

public class HomeActivity extends Activity {
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();
        advertise();
    }

    private void initialize() {

        if (mBluetoothLeAdvertiser == null) {

            BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if (mBluetoothManager != null) {

                BluetoothAdapter mBluetoothAdapter = mBluetoothManager.getAdapter();

                if (mBluetoothAdapter != null) {

                    mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

                } else {

//                    Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();

                }

            } else {

//                Toast.makeText(this, getString(R.string.bt_null), Toast.LENGTH_LONG).show();

            }

        }



    }

    void advertise(){
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable(false)
                .build();
        Log.i("BLE","start of advertise data after settings");
        ParcelUuid pUuid = new ParcelUuid( UUID.randomUUID());
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( false )
//                .addServiceUuid( pUuid )
                .addServiceData( pUuid, "13204215".getBytes())
                .build();
        Log.i("BLE","before callback");
        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Log.i("BLE", "LE Advertise success.");

                showMessage("Ad success");

            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e("BLE", "Advertising onStartFailure: " + errorCode);
                showMessage("Ad fail");
                showMessage(""+errorCode);
                super.onStartFailure(
                        errorCode);
            }
        };
//        showMessage("Starting scan");
        Log.i("BLE", String.valueOf(mBluetoothLeAdvertiser));
        mBluetoothLeAdvertiser.startAdvertising( settings, data, advertisingCallback );
//        showMessage("Done starting");
    }

    void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
