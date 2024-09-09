/**
 * Activity of Searching Bluetooth Printers
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.adverticoLTD.avms.brotherPrinter;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.ui.dashboardScreen.DashboardActivity;
import com.brother.ptouch.sdk.NetPrinter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothPrinterAutoConfigActivity extends ListActivity {

    int noOfPairedDeviceCall = 0;
    LinearLayout loutAutoConnect;
    LinearLayout loutManualConnect;
    TextView txtClick;
    private ProgressDialog mProgressDialog;
    private NetPrinter[] mBluetoothPrinter; // array of storing Printer information
    private ArrayList<String> mItems = null; // List of storing the printer's information

    /**
     * initialize activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_config_printer_list);

        loutAutoConnect = findViewById(R.id.loutAutoConnect);
        loutManualConnect = findViewById(R.id.loutManualConnect);
        txtClick = findViewById(R.id.txtClick);

        txtClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),
                        PrinterSettingsActivity.class));

            }
        });

        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButtonOnClick();

            }
        });


        Button btPrinterSettings = (Button) findViewById(R.id.btPrinterSettings);
        btPrinterSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsButtonOnClick();

            }
        });

        loutManualConnect.setVisibility(View.GONE);
        loutAutoConnect.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getPairedPrinters();
            }
        }, ConstantClass.PRINTER_AUTO_CONNECT_INTERVAL);

        //  setDialog();
        //   this.setTitle(R.string.bluetooth_printer_list_title_label);
    }

    private void setDialog() {
        showMsgNoButton("Bluetooth Printer", "Searching…");
    }

    public void showMsgNoButton(final String title, final String message) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        }

        mProgressDialog = new ProgressDialog(BluetoothPrinterAutoConfigActivity.this);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

    }

    /**
     * Called when [Settings] button is tapped
     */
    private void settingsButtonOnClick() {
        Intent bluetoothSettings = new Intent(
                android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivityForResult(bluetoothSettings,
                Common.ACTION_BLUETOOTH_SETTINGS);
    }

    /**
     * Called when [Refresh] button is tapped
     */
    private void refreshButtonOnClick() {
        getPairedPrinters();

    }

    /**
     * Called when the Settings activity exits
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.ACTION_BLUETOOTH_SETTINGS) {
            getPairedPrinters();
        }
    }

    /**
     * get paired printers
     */
    private void getPairedPrinters() {

        Log.e("Call ", noOfPairedDeviceCall + " yes");

        // get the BluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(enableBtIntent);
            }
        } else {
            return;
        }

        try {
            if (mItems != null) {
                mItems.clear();
            }
            mItems = new ArrayList<String>();

            /*
             * if the paired devices exist, set the paired devices else set the
             * string of "No Bluetooth Printer."
             */
            List<BluetoothDevice> pairedDevices = getPairedBluetoothDevice(bluetoothAdapter);
            if (pairedDevices.size() > 0) {
                mBluetoothPrinter = new NetPrinter[pairedDevices.size()];
                int i = 0;
                for (BluetoothDevice device : pairedDevices) {
                    String strDev = "";
                    strDev += device.getName() + "\n" + device.getAddress();
                    mItems.add(strDev);

                    mBluetoothPrinter[i] = new NetPrinter();
                    mBluetoothPrinter[i].ipAddress = "";
                    mBluetoothPrinter[i].macAddress = device.getAddress();
                    mBluetoothPrinter[i].modelName = device.getName();
                    i++;
                }
            } else {
                mItems.add(getString(R.string.no_bluetooth_device));
                closeProgressBar();
            }
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ArrayAdapter<String> fileList = new ArrayAdapter<String>(
                            BluetoothPrinterAutoConfigActivity.this,
                            android.R.layout.test_list_item, mItems);
                    BluetoothPrinterAutoConfigActivity.this.setListAdapter(fileList);

                    closeProgressBar();

                    chekBluetoothDevice();
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void chekBluetoothDevice() {

        noOfPairedDeviceCall = noOfPairedDeviceCall + 1;

        String modelNo = "QL-820NWB";

        boolean isPrinterFound = false;

        if (mBluetoothPrinter != null) {
            for (NetPrinter netPrinter : mBluetoothPrinter) {
                Log.e("BLEDevice", "Printer Name " + netPrinter.modelName);
                if (netPrinter.modelName.toLowerCase().contains(modelNo.toLowerCase())) {
                    Log.e("BLEDevice", "Model Name Matched");
                    isPrinterFound = true;
                    Prefs.putString(PreferenceKeys.PREF_MAC_ADDRESS, netPrinter.macAddress);
                    Prefs.putString(PreferenceKeys.PREF_PORT, Common.BLUETOOTH);


                    final Intent settings = new Intent(this, DashboardActivity.class);
                    settings.putExtra("ipAddress",
                            netPrinter.ipAddress);
                    settings.putExtra("macAddress",
                            netPrinter.macAddress);
                    settings.putExtra("localName", "");
                    settings.putExtra("printer", netPrinter.modelName);
                    settings.putExtra("port", Common.BLUETOOTH);
                    startActivity(settings);
                    finish();
                    Log.e("BLEDevice", "breaking loop  :: Counter ==" + noOfPairedDeviceCall);
                    break;
                }
            }
        }

        if (!isPrinterFound) {
            if (noOfPairedDeviceCall < ConstantClass.PRINTER_AUTO_CONNECT_RETRY_COUNTER) {
                getPairedPrinters();
                Log.e("BLEDevice", "Manual == false");
            } else {
                Log.e("BLEDevice", "Manual == true");
                loutAutoConnect.setVisibility(View.GONE);
                loutManualConnect.setVisibility(View.VISIBLE);
            }
        }


    }

    private void closeProgressBar() {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private List<BluetoothDevice> getPairedBluetoothDevice(BluetoothAdapter bluetoothAdapter) {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices == null || pairedDevices.size() == 0) {
            return new ArrayList<>();
        }
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                devices.add(device);
            } else {
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                    devices.add(device);
                }
            }
        }

        return devices;
    }

    /**
     * Called when an item in the list is tapped
     */
    @Override
    protected void onListItemClick(ListView listView, View view, int position,
                                   long id) {

        final String item = (String) getListAdapter().getItem(position);
        if (!item.equalsIgnoreCase(getString(R.string.no_bluetooth_device))) {
            // send the selected printer info. to Settings Activity and close
            // the current Activity


            Prefs.putString(PreferenceKeys.PREF_MAC_ADDRESS, mBluetoothPrinter[position].macAddress);
            Prefs.putString(PreferenceKeys.PREF_PORT, Common.BLUETOOTH);

            final Intent settings = new Intent(this, DashboardActivity.class);
            settings.putExtra("ipAddress",
                    mBluetoothPrinter[position].ipAddress);
            settings.putExtra("macAddress",
                    mBluetoothPrinter[position].macAddress);
            settings.putExtra("localName", "");
            settings.putExtra("printer", mBluetoothPrinter[position].modelName);
            settings.putExtra("port", Common.BLUETOOTH);
            startActivity(settings);
//            setResult(RESULT_OK, settings);
        }
        finish();
    }

}