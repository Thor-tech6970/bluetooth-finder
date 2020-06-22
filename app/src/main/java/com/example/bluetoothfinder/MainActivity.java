package com.example.bluetoothfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView , availableTextView , textView;

    ListView listView;

    Button searchButton;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<String> devices = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    BluetoothDevice device;

    private final BroadcastReceiver  broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();

            Log.i("Action" , action);

            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){


                resultTextView.setText("Search finished");

                searchButton.setEnabled(true);

            }

            else if(action.equals(BluetoothDevice.ACTION_FOUND)){

               device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                String name = device.getName();

                String address = device.getAddress();

                String RSSI = Integer.toString( intent.getShortExtra(BluetoothDevice.EXTRA_RSSI , Short.MIN_VALUE));

                Log.i("Device info" , "Device name : " + name + " Device address : " + address + " Device RSSI : " + RSSI);

                if(name == null){

                    devices.add( "  Address : " + address + " , RSSI : " + RSSI);

                }

                else {


                    devices.add(" Name : " + name + ",  Address : " + address + " , RSSI : " + RSSI);

                    arrayAdapter.notifyDataSetChanged();

                }

            }


            if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED) && device == null){

                textView.setText("No device found :(");

                resultTextView.setText("Search finished");

                searchButton.setEnabled(true);





            }

















        }
    };



    public void search(View view){

        availableTextView.setVisibility(View.VISIBLE);

        resultTextView.setText("Searching...");

        searchButton.setEnabled(false);

        bluetoothAdapter.startDiscovery();

        devices.clear();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        textView = (TextView) findViewById(R.id.textView);

        availableTextView = (TextView) findViewById(R.id.availableTextView);

        listView = (ListView) findViewById(R.id.listView);

        searchButton = (Button) findViewById(R.id.searchButton);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver( broadcastReceiver, intentFilter);

        arrayAdapter = new ArrayAdapter(this , android.R.layout.simple_expandable_list_item_1 , devices);

        listView.setAdapter(arrayAdapter);

        availableTextView.setVisibility(View.GONE);










    }
}
