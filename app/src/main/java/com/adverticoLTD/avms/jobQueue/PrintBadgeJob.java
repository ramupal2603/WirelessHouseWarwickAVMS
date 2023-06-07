package com.adverticoLTD.avms.jobQueue;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adverticoLTD.avms.brotherPrinter.Common;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.ui.Utils;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.pixplicity.easyprefs.library.Prefs;

public class PrintBadgeJob extends Job {


    String bitmapString;
    String workPath;

    private static final String TAG = "PrintBadgeJob";

    public PrintBadgeJob(String bitmapString, String workPath) {
        super(new Params(Priority.HIGH).persist().requireNetwork());
        this.bitmapString = bitmapString;
        this.workPath = workPath;
    }

    @Override
    public void onAdded() {

    }

    private void configurePrinter(Bitmap badgeToPrint) {
        try {
            String port = Prefs.getString(PreferenceKeys.PREF_PORT, "");
            boolean isBluetoothPrinterSelected = port != null && port.equalsIgnoreCase(Common.BLUETOOTH);

            Printer myPrinter = new Printer();

            if (isBluetoothPrinterSelected) {
                // when use bluetooth print set the adapter
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                        .getDefaultAdapter();
                myPrinter.setBluetooth(bluetoothAdapter);
            }


            PrinterInfo myPrinterInfo = new PrinterInfo();
            myPrinterInfo = myPrinter.getPrinterInfo();
            myPrinterInfo.printerModel = isBluetoothPrinterSelected
                    ? PrinterInfo.Model.QL_820NWB : PrinterInfo.Model.QL_810W;
            myPrinterInfo.port = isBluetoothPrinterSelected
                    ? PrinterInfo.Port.BLUETOOTH : PrinterInfo.Port.NET;


            myPrinterInfo.printMode = PrinterInfo.PrintMode.ORIGINAL;
            myPrinterInfo.orientation = PrinterInfo.Orientation.LANDSCAPE;
            myPrinterInfo.paperSize = PrinterInfo.PaperSize.CUSTOM;


            if (isBluetoothPrinterSelected) {
                myPrinterInfo.macAddress = Prefs.getString(PreferenceKeys.PREF_MAC_ADDRESS, "");
            } else {
                myPrinterInfo.ipAddress = Prefs.getString(PreferenceKeys.PREF_IP_ADDRESS, "");
            }


            myPrinterInfo.numberOfCopies = 1;
            myPrinterInfo.valign = PrinterInfo.VAlign.MIDDLE;
            myPrinterInfo.align = PrinterInfo.Align.CENTER;
            myPrinterInfo.labelNameIndex = LabelInfo.QL700.W62.ordinal();
            myPrinterInfo.workPath = workPath;
            myPrinterInfo.scaleValue = 1;
            myPrinterInfo.isAutoCut = true;
            myPrinterInfo.isCutAtEnd = false;
            myPrinterInfo.isHalfCut = false;
            myPrinterInfo.isSpecialTape = false;

            myPrinter.setPrinterInfo(myPrinterInfo);


            PrinterStatus printerStatus = new PrinterStatus();
            myPrinter.startCommunication();
            printerStatus = myPrinter.printImage(badgeToPrint);
            myPrinter.endCommunication();
            Log.i(TAG, "configurePrinter: " + printerStatus.errorCode);
        } catch (Exception e) {
            Log.i(TAG, "Exception While Printing: " + e.getMessage());
        }
    }


    @Override
    public void onRun() throws Throwable {
        Bitmap badgeToPrint = Utils.StringToBitMap(bitmapString);
        configurePrinter(badgeToPrint);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
