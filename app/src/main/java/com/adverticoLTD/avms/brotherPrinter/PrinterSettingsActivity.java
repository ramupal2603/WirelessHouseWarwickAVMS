/**
 * Activity of printer settings
 *
 * @author Brother Industries, Ltd.
 * @version 2.2
 */

package com.adverticoLTD.avms.brotherPrinter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.adverticoLTD.avms.R;

public class PrinterSettingsActivity extends PreferenceActivity implements
        OnPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // initialize the printerModel ListPreference
        ListPreference printerModelPreference = (ListPreference) getPreferenceScreen()
                .findPreference("printerModel");
        printerModelPreference.setEntryValues(PrinterModelInfo.getModelNames());
        printerModelPreference.setEntries(PrinterModelInfo.getModelNames());


        // initialize the settings
        setPreferenceValue("printerModel");
        String printerModel = sharedPreferences.getString("printerModel", "");



        // initialization for printer
        PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                .findPreference("printer");

        String printer = sharedPreferences.getString("printer", "");
        if (!printer.equals("")) {
            printerPreference.setSummary(printer);
        }


        printerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String printerModel = sharedPreferences.getString(
                                "printerModel", "");
                        setPrinterList(printerModel);
                        return true;
                    }
                });

    }

    /**
     * Called when a Preference has been changed by the user. This is called
     * before the state of the Preference is about to be updated and before the
     * state is persisted.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (newValue != null) {
            if (preference.getKey().equals("printerModel")) {
                String printerModel = sharedPreferences.getString(
                        "printerModel", "");
                if (printerModel.equalsIgnoreCase(newValue.toString())) {
                    return true;
                }

                // initialize if printer model is changed
                printerModelChange(newValue.toString());
                ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                        .findPreference("paperSize");
                paperSizePreference.setValue(paperSizePreference
                        .getEntryValues()[0].toString());
                paperSizePreference.setSummary(paperSizePreference
                        .getEntryValues()[0].toString());

                ListPreference portPreference = (ListPreference) getPreferenceScreen()
                        .findPreference("port");
                portPreference.setValue(portPreference.getEntryValues()[0]
                        .toString());
                portPreference.setSummary(portPreference.getEntryValues()[0]
                        .toString());

                setChangedData();
            }

            if (preference.getKey().equals("port")) {
                setChangedData();
            }

            preference.setSummary((CharSequence) newValue);

            return true;
        }

        return false;

    }

    /**
     * set data of a particular ListPreference
     */
    private void setPreferenceValue(String value) {
        String data = sharedPreferences.getString(value, "");

        ListPreference printerValuePreference = (ListPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);
        if (!data.equals("")) {
            printerValuePreference.setSummary(data);
        }
    }

    /**
     * set data of a particular EditTextPreference
     */
    private void setEditValue(String value) {
        String name = sharedPreferences.getString(value, "");
        EditTextPreference printerValuePreference = (EditTextPreference) getPreferenceScreen()
                .findPreference(value);
        printerValuePreference.setOnPreferenceChangeListener(this);

        if (!name.equals("")) {
            printerValuePreference.setSummary(name);
        }
    }

    /**
     * Called when [printer] is tapped
     */
    private void setPrinterList(String printModel) {
        String port = sharedPreferences.getString("port", "");

        // call the Activity_NetPrinterList when port is NET
        if (port.equalsIgnoreCase(Common.NET)) {
            Intent printerList = new Intent(this, WifiPrinterListActivity.class);
            String printTempModel = printModel.replaceAll("_", "-");
            printerList.putExtra("modelName", printTempModel);
            startActivityForResult(printerList, Common.PRINTER_SEARCH);
        }else if (port.equalsIgnoreCase(Common.BLUETOOTH)){
            Intent printerList = new Intent(this,
                    BluetoothPrinterListActivity.class);
            startActivityForResult(printerList, Common.PRINTER_SEARCH);
        }
    }


    /**
     * set paper size & port information with changing printer model
     */
    private void printerModelChange(String printerModel) {

        // paper size
        ListPreference paperSizePreference = (ListPreference) getPreferenceScreen()
                .findPreference("paperSize");
        // port
        ListPreference portPreference = (ListPreference) getPreferenceScreen()
                .findPreference("port");
        if (!printerModel.equals("")) {

            String[] entryPort;
            String[] entryPaperSize;
            entryPort = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PORT);
            entryPaperSize = PrinterModelInfo.getPortOrPaperSizeInfo(printerModel, Common.SETTINGS_PAPERSIZE);

            portPreference.setEntryValues(entryPort);
            portPreference.setEntries(entryPort);

            paperSizePreference.setEntryValues(entryPaperSize);
            paperSizePreference.setEntries(entryPaperSize);

        }
    }

    /**
     * initialize the address & macAddress information with changing printer
     * model or port
     */
    private void setChangedData() {
        EditTextPreference addressPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference("address");
        EditTextPreference macAddressPreference = (EditTextPreference) getPreferenceScreen()
                .findPreference("macAddress");
        PreferenceScreen printerPreference = (PreferenceScreen) getPreferenceScreen()
                .findPreference("printer");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("address", "");
        editor.putString("macAddress", "");
        editor.putString("printer","Select a destination printer");
        editor.apply();

        addressPreference.setText("");
        macAddressPreference.setText("");
        printerPreference.setSummary("Select a destination printer");
        macAddressPreference.setSummary("Set the number of mac address");
        addressPreference.setSummary("Set the number of ip address");
    }

    /**
     * set the BackgroundForPreferenceScreens to light it is black when at OS
     * 2.1/2.2
     */
    private void setBackgroundForPreferenceScreens(String key) {
        PreferenceScreen preferenceScreen = (PreferenceScreen) getPreferenceScreen()
                .findPreference(key);

        preferenceScreen
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        PreferenceScreen pref = (PreferenceScreen) preference;
                        pref.getDialog()
                                .getWindow()
                                .setBackgroundDrawableResource(
                                        android.R.drawable.screen_background_light);
                        return false;
                    }
                });
    }
}
