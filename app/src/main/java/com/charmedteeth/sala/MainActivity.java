package com.charmedteeth.sala;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DevicesListActivity";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter bluetoothAdapter;
    private Context mContext;
    private List<String> deviceNames;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button scanDevicesBtn = findViewById(R.id.discover_devices);
        RecyclerView devicesRecyclerView = findViewById(R.id.devicesRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        devicesRecyclerView.setLayoutManager(linearLayoutManager);
        deviceNames = new ArrayList<>();
        progressBar   = findViewById(R.id.progress);

        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(broadcastReceiver,intent);

        intent = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(broadcastReceiver,intent);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if(pairedDevice.size() > 0){
            for(BluetoothDevice bluetoothDevice : pairedDevice){
                String blueToothInfo = bluetoothDevice.getName();
                deviceNames.add(blueToothInfo);
            }
        }else{
            deviceNames.add("No device Found");
        }
        DevicesArrayAdaptor devicesArrayAdaptor = new DevicesArrayAdaptor(deviceNames);
        devicesRecyclerView.setAdapter(devicesArrayAdaptor);
        scanDevicesBtn.setOnClickListener(new View.OnClickListener() {

            //setResult(Activity.RESULT_CANCELED);
            @Override
            public void onClick(View view) {
                discoverDevices();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    public void discoverDevices() {

        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }


    /**
     * The BroadcastReceiver that listens for discovered devices and changes the title when
     * discovery is finished
     */

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(bluetoothDevice !=null){
                    if(bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED){
                        deviceNames.add(bluetoothDevice.getName());
                    }
                }

            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                progressBar.setVisibility(View.GONE);
                if(deviceNames.size() == 0){
                    deviceNames.add("No devices found");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bluetoothAdapter !=null){
            bluetoothAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}