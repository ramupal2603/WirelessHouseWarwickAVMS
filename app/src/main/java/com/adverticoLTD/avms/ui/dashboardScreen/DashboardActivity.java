package com.adverticoLTD.avms.ui.dashboardScreen;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.MyApplication;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.brotherPrinter.Common;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestParamModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseDataModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.keyLogSolution.ui.welcomeScreen.WelcomeActivity;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.network.utils.WebApiHelper;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.contractorView.contractorTypeScreen.ContractorTypeActivity;
import com.adverticoLTD.avms.ui.manualDashboard.ManualDashboardActivity;
import com.adverticoLTD.avms.ui.normalVisitorScreen.NormalVisitorScreen;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.adverticoLTD.avms.ui.userSelection.UserTypeActivity;
import com.brother.ptouch.sdk.LabelInfo;
import com.brother.ptouch.sdk.Printer;
import com.brother.ptouch.sdk.PrinterInfo;
import com.brother.ptouch.sdk.PrinterStatus;
import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends BaseActivity {

    private static final int MY_CAMERA_REQUEST_CODE = 1000;
    @BindView(R.id.mCodeScanner)
    CodeScannerView scannerView;
    @BindView(R.id.loutManualSignIn)
    LinearLayout loutManualSignIn;
    @BindView(R.id.loutHomeScreen)
    LinearLayout loutHomeScreen;
    @BindView(R.id.loutVisitorScreen)
    LinearLayout loutVisitorScreen;
    @BindView(R.id.loutContractorScreen)
    LinearLayout loutContractorScreen;
    @BindView(R.id.loutKeyLog)
    LinearLayout loutKeyLog;
    @BindView(R.id.loutSignOut)
    LinearLayout loutSignOut;
    CodeScanner mCodeScanner;
    Printer myPrinter;
    Bitmap badgeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        qrCodeInitialization();

        loutManualSignIn.setOnClickListener(this::onClick);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        showToastMessage(result.getText());
                        scanQrCode(result.getText());
                        mCodeScanner.releaseResources();
                        mCodeScanner.stopPreview();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                mCodeScanner.startPreview();
                            }
                        }, 2000);

                    }
                });
            }
        });

        loutHomeScreen.setOnClickListener(this::onClick);
        loutVisitorScreen.setOnClickListener(this::onClick);
        loutContractorScreen.setOnClickListener(this::onClick);
        loutKeyLog.setOnClickListener(this::onClick);
        loutSignOut.setOnClickListener(this::onClick);

        setUpDateTimeColor();
    }

    private void setUpDateTimeColor() {
        if (txtTime != null) {
            txtTime.setTextColor(getResources().getColor(R.color.whiteColor));
        }
        if (txtDate != null) {
            txtDate.setTextColor(getResources().getColor(R.color.whiteColor));
        }
    }

    private void scanQrCode(String scannedID) {

        showProgressBar();

        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.scanQrCode(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getScanQrCodeRequest(scannedID)).enqueue(new Callback<ScanQrCodeResponseModel>() {
            @Override
            public void onResponse(Call<ScanQrCodeResponseModel> call, Response<ScanQrCodeResponseModel> response) {
                if (response.isSuccessful()) {
                    ScanQrCodeResponseModel responseModel = response.body();
                    if (responseModel != null) {
                        if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_IN)) {


                            ScanQrCodeResponseDataModel responseDataModel = responseModel.getData();


                            Bitmap qrCodeImage = generateQrCodeData(responseDataModel.getVisitor_id());

                            boolean isRegularVisitor = responseDataModel.getVisitor_id().endsWith("@5");
                            boolean isContractor = responseDataModel.getVisitor_id().endsWith("@4");
                            boolean isStaff = responseDataModel.getVisitor_id().endsWith("@3");

                            Bitmap badgeImage = generatePrintData(responseDataModel.getName(),
                                    responseDataModel.getVisitor_organization(), responseDataModel.getStaff_name(),
                                    responseDataModel.getCompany_name(), isRegularVisitor,
                                    isContractor, qrCodeImage, isStaff, false);

                            String bitmapString = Utils.BitMapToString(badgeImage);

                            if (BuildConfig.allowBadgePrint) {
                                MyApplication.getInstance().getMainJobManager().addJobInBackground(
                                        new PrintBadgeJob(bitmapString, getWorkPath()));
                            } else {
                                previewPrintBadge(badgeImage);
                            }


                            Intent intent = new Intent(DashboardActivity.this, ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);

                        } else if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {

                            Intent intent = new Intent(DashboardActivity.this, ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);

                        } else {
                            showToastMessage(getString(R.string.error_msg_scan_qr_code));
                        }


                    }
                }else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scanQrCode(scannedID);

                }
                hideProgressBar();
            }

            @Override
            public void onFailure(Call<ScanQrCodeResponseModel> call, Throwable t) {
                t.printStackTrace();
                hideProgressBar();
            }
        });
    }

    private ScanQrCodeRequestModel getScanQrCodeRequest(String scannedID) {
        ScanQrCodeRequestModel requestModel = new ScanQrCodeRequestModel();
        ScanQrCodeRequestParamModel paramModel = new ScanQrCodeRequestParamModel();
        paramModel.setDevice_type(WebApiHelper.DEVICE_TYPE_TAB);
        paramModel.setUser_id(scannedID);
        requestModel.setParam(paramModel);
        return requestModel;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_CAMERA_REQUEST_CODE);
                return;
            }
        }
        mCodeScanner.releaseResources();
        mCodeScanner.stopPreview();
        mCodeScanner.startPreview();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mCodeScanner.stopPreview();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void onClick(View view) {
        if (view == loutManualSignIn) {
            startActivity(new Intent(DashboardActivity.this,
                    ManualDashboardActivity.class));
            /*//Todo(Ramu):for testing purpose using this to check in jobQueue

            Bitmap qrCodeImage = generateQrCodeData("9@4");

            boolean isRegularVisitor = true;
            boolean isContractor = false;
            badgeImage = generatePrintData("ramu pal",
                    "Advertico LTD", "Abdul Saleem",
                    "Testing Company", isRegularVisitor, isContractor, qrCodeImage);

            String bitmapString = Utils.BitMapToString(badgeImage);

            MyApplication.getInstance().getMainJobManager().addJobInBackground(
                    new PrintBadgeJob(bitmapString, getWorkPath()));*/

            // startActivity(new Intent(DashboardActivity.this, ManualDashboardActivity.class));
        }


        if (view == loutVisitorScreen) {
            Intent intent = new Intent(DashboardActivity.this, NormalVisitorScreen.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_VISITOR);
        }

        if (view == loutContractorScreen) {
            Intent intent = new Intent(DashboardActivity.this, ContractorTypeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
        if (view == loutKeyLog) {
            Intent intent = new Intent(DashboardActivity.this, WelcomeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }

        if (view == loutSignOut) {
            Intent intent = new Intent(DashboardActivity.this, UserTypeActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
    }

    private void qrCodeInitialization() {
        mCodeScanner = new CodeScanner(this, scannerView);

        // Parameters (default values)
        mCodeScanner.setCamera(CodeScanner.CAMERA_FRONT);// or CAMERA_FRONT or specific camera id
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);// ex. listOf(BarcodeFormat.QR_CODE)
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusEnabled(false);
        mCodeScanner.setFlashEnabled(false);
    }

    private void printQRCode(Bitmap qrCodeImage) {
        try {
            String port = Prefs.getString(PreferenceKeys.PREF_PORT, "");
            boolean isBluetoothPrinterSelected = port != null && port.equalsIgnoreCase(Common.BLUETOOTH);

            myPrinter = new Printer();

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

            if (isBluetoothPrinterSelected) {
                myPrinterInfo.macAddress = Prefs.getString(PreferenceKeys.PREF_MAC_ADDRESS, "");
            } else {
                myPrinterInfo.ipAddress = Prefs.getString(PreferenceKeys.PREF_IP_ADDRESS, "");
            }

            myPrinterInfo.printMode = PrinterInfo.PrintMode.ORIGINAL;
            myPrinterInfo.orientation = PrinterInfo.Orientation.LANDSCAPE;
            myPrinterInfo.paperSize = PrinterInfo.PaperSize.CUSTOM;


            myPrinterInfo.numberOfCopies = 1;
            myPrinterInfo.valign = PrinterInfo.VAlign.MIDDLE;
            myPrinterInfo.align = PrinterInfo.Align.CENTER;
            myPrinterInfo.labelNameIndex = LabelInfo.QL700.W62.ordinal();
            myPrinterInfo.scaleValue = 1;
            myPrinterInfo.isAutoCut = true;
            myPrinterInfo.isCutAtEnd = false;
            myPrinterInfo.isHalfCut = false;
            myPrinterInfo.isSpecialTape = false;
            myPrinterInfo.workPath = getActivity().getCacheDir().getAbsolutePath();
            myPrinter.setPrinterInfo(myPrinterInfo);

            Paint paint = new Paint();
            paint.setTextSize(100.0f);
            paint.setColor(Color.WHITE);

            float headerTextSize = getFloatedTextSize(80);
            Paint header = new Paint();
            header.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            header.setTextSize(headerTextSize);
            header.setColor(Color.WHITE);
            header.setTextAlign(Paint.Align.CENTER);

            Paint paint2 = new Paint();
            paint2.setTextSize(60.0f);
            paint2.setColor(Color.BLACK);
            paint2.setTextAlign(Paint.Align.CENTER);

            Paint paint1 = new Paint();
            paint1.setTextSize(35.0f);
            paint1.setColor(Color.BLACK);
            paint1.setTextAlign(Paint.Align.CENTER);

            float dateTimeSize = getFloatedTextSize(18);

            Paint dateTimePaint = new Paint();
            dateTimePaint.setTextSize(dateTimeSize);
            dateTimePaint.setColor(Color.BLACK);


            float firstNameSize = getFloatedTextSize(35);
            Paint firstNamePaint = new Paint();
            firstNamePaint.setTextSize(firstNameSize);
            firstNamePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            firstNamePaint.setColor(Color.BLACK);

            float companyNameSize = getFloatedTextSize(24);
            Paint companyNamePaint = new Paint();
            companyNamePaint.setTextSize(companyNameSize);
            companyNamePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            companyNamePaint.setColor(Color.BLACK);


            float baseline = paint.ascent();
            int width = (int) (paint.measureText("Ramu") + 0.5f);
            int height = (int) (baseline + paint.descent() + 0.5f);
            badgeImage = Bitmap.createBitmap(350 + 380, height + 680, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(badgeImage);
            canvas.drawRect(0, 0, 350 + 380, height + 680, paint);

            canvas.drawRect(0, 0, 350 + 380, height + 200, paint1);


            String headerTitle = "";
            headerTitle = "Contractor";

            canvas.drawText(headerTitle, canvas.getWidth() / 2, 100, header);


            canvas.drawText("Ramu", 15, 190, firstNamePaint);
            canvas.drawText("Advertico LTD", 18, 240, companyNamePaint);


            canvas.drawText("Visiting:", 15, 300, companyNamePaint);
            canvas.drawText("Adbul", 15, 360, firstNamePaint);
            canvas.drawText("C Company", 15, 410, companyNamePaint);


            Bitmap resizedQrCodeBitmap = Bitmap.createScaledBitmap(qrCodeImage, 250, 300, true);

            canvas.drawBitmap(resizedQrCodeBitmap, canvas.getWidth() - 230, 135, paint1);

            Calendar c = Calendar.getInstance();


            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat tm = new SimpleDateFormat("hh:mm aa");

            String formattedTime = "Signed in at " + tm.format(c.getTime());
            String formattedDate = "Date " + df.format(c.getTime());
            String VisitorID = "Visitor ID : " + "123";


            canvas.drawLine(15, 430, canvas.getWidth() - 15, 430, paint1);

            canvas.drawText(formattedTime, canvas.getWidth() - 265, 520, dateTimePaint);
            canvas.drawText(formattedDate, canvas.getWidth() - 265, 555, dateTimePaint);
            canvas.drawText(VisitorID, canvas.getWidth() - 265, 595, dateTimePaint);

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
            Bitmap resizedCompanyLogo = Bitmap.createScaledBitmap(bmp, 215, 141, true);
            canvas.drawBitmap(resizedCompanyLogo, 15, 470, paint1);

            printerThread printThread = new printerThread();
            printThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getFloatedTextSize(int textSizeInSp) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSizeInSp, getResources().getDisplayMetrics());
    }

    protected class printerThread extends Thread {
        @Override
        public void run() {
            try {
                PrinterStatus printresult = new PrinterStatus();
                myPrinter.startCommunication();
                printresult = myPrinter.printImage(badgeImage);


                if (printresult.errorCode != PrinterInfo.ErrorCode.ERROR_NONE) {
                    Log.e("app", "Brother Printer returned an error message: " + printresult.errorCode.toString());
                }

                myPrinter.endCommunication();
            } catch (Exception e) {
                Log.d("issue fixed", "Temp file action failed: " + e.toString());
            }
        }
    }


}
