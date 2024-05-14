package com.adverticoLTD.avms.baseClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adverticoLTD.avms.BuildConfig;
import com.adverticoLTD.avms.MyApplication;
import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.customClasses.ProgressLoader;
import com.adverticoLTD.avms.data.acesstoken.AccessTokenResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseDataModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerMessageResponseModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestModel;
import com.adverticoLTD.avms.data.disclaimerMessage.DisclaimerRequestParamModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeRequestParamModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseDataModel;
import com.adverticoLTD.avms.data.scanQrCode.ScanQrCodeResponseModel;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.DimensionUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.jobQueue.PrintBadgeJob;
import com.adverticoLTD.avms.network.RetrofitClient;
import com.adverticoLTD.avms.network.RetrofitInterface;
import com.adverticoLTD.avms.network.utils.WebApiHelper;
import com.adverticoLTD.avms.ui.Utils;
import com.adverticoLTD.avms.ui.siteSelectionView.SiteSelectionViewActivity;
import com.adverticoLTD.avms.ui.thankYouSceen.ThankYouScreen;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    Unbinder unbinder = null;


    private ProgressLoader loader;

    @Nullable
    @BindView(R.id.txtDate)
    public TextView txtDate;

    @Nullable
    @BindView(R.id.txtTime)
    public TextView txtTime;

    @Nullable
    @BindView(R.id.txtMarquee)
    TextView txtMarquee;

    @Nullable
    @BindView(R.id.imgBack)
    ImageView imgBack;

    @Nullable
    @BindView(R.id.next)
    TextView next;

    CountDownTimer newTimer;

    String disclaimerMessage = "";
    String marqueeMessage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        unbinder = ButterKnife.bind(this);
        Utils.hideKeyBoard(getActivity());

        initCommonView();

        showTime();

        getAccessKeyToken();

        if (marqueeMessage.isEmpty()) {
            getDisclaimerMessage();
        } else {
            setDisclaimerMessage("");
        }
    }

    public String gdprMessage = "<p><b>WELCOME TO 9 COLMOREROW</b></p>\n" +
            "<p><br />WHILST ON OUR PREMISES THE FOLLOWING REGULATIONS MUST BE OBSERVED</p>\n" +
            "<p><br />&bull; <b>HEALTH &amp; SAFETY</b><br />All visitors are subject to the Health &amp; Safety at Work Act 1974 and other site regulations</p>\n" +
            "<p>&bull; <b>FIRE &amp; EMERGENCY</b><br />In the case of emergency, all Visitors must accompany their Host and report to Assembly Point.<br />DO NOT USE THE LIFTS.</p>\n" +
            "<p>&bull; <b>PACKAGES</b><br />Packages and Cases must not be left unattended.</p>\n" +
            "<p>&bull; <b>SMOKING</b><br />It is against the law to smoke on these premises</p>\n" +
            "<p>&bull; <b>PROPERTY</b><br />The company accepts no responsibility for any loss or damage to visitor&rsquo;s property</p>\n" +
            "<p><br />PLEASE SIGN OUT BEFORE LEAVING THE PREMISES</p>" +
            "<br /><b>GDPR Compliance Message</b>\n" +
            "<br /><br />• We collect basic information for the purposes of Health and Safety, and for archiving.\n" +
            "<br /><br />• We are required to know who is present on our premises in case of an emergency evacuation.\n" +
            "<br /><br />• We are required to know who has been present on our premises for our own information.\n" +
            "<br /><br />• Your details are kept archived and encrypted on our premises and on UK based cloud servers for backup.";


    public void getAccessKeyToken() {
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getTokenAccesskey(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader()).enqueue(new Callback<AccessTokenResponseModel>() {
            @Override
            public void onResponse(Call<AccessTokenResponseModel> call, Response<AccessTokenResponseModel> response) {
                if (response.isSuccessful()) {
                    AccessTokenResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().toString().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        Prefs.putString(PreferenceKeys.PREF_ACCESS_TOKEN, responseModel.getData().getApiTokenKey());

                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {

                    Prefs.putString(PreferenceKeys.PREF_ACCESS_TOKEN, "");
                    getAccessKeyToken();

                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponseModel> call, Throwable t) {
                t.printStackTrace();
                showToastMessage(getString(R.string.error_something_went_wrong));
            }
        });

    }

    private void getDisclaimerMessage() {
        RetrofitInterface apiService = RetrofitClient.getRetrofit().create(RetrofitInterface.class);
        apiService.getDisclaimerMessage(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getDisclaimerRequest()).enqueue(new Callback<DisclaimerMessageResponseModel>() {
            @Override
            public void onResponse(Call<DisclaimerMessageResponseModel> call, Response<DisclaimerMessageResponseModel> response) {
                if (response.isSuccessful()) {
                    DisclaimerMessageResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {
                        ArrayList<DisclaimerMessageResponseDataModel> arrDisclaimerData = responseModel.getData();
                        if (!arrDisclaimerData.isEmpty()) {
//                            disclaimerMessage = responseModel.getData().get(0).getMessage();
                            setDisclaimerMessage(responseModel.getData().get(0).getMessage());
                        }
                    }

                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {

                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getDisclaimerMessage();

                }

            }

            @Override
            public void onFailure(Call<DisclaimerMessageResponseModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private DisclaimerRequestModel getDisclaimerRequest() {

        DisclaimerRequestModel requestModel = new DisclaimerRequestModel();
        DisclaimerRequestParamModel requestParamModel = new DisclaimerRequestParamModel();
        requestParamModel.setType(WebApiHelper.MSG_TYPE_MARQUEE);
        requestModel.setParam(requestParamModel);

        return requestModel;
    }

    private void setDisclaimerMessage(String message) {
        for (int i = 0; i < 4; i++) {
            disclaimerMessage = disclaimerMessage + " | " + message;
        }

        if (txtMarquee != null) {
            txtMarquee.setText(disclaimerMessage);
        }

        if (txtMarquee != null) {
            txtMarquee.setSelected(true);
        }
    }

    @Optional
    @OnClick(R.id.imgBack)
    void OnBackButtonClicked() {
        finish();
    }

    @Optional
    @OnClick(R.id.next)
    void onNextPressed() {
        hideKeyBoard();
    }

    private void showTime() {
        newTimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (txtTime != null) {
                    txtTime.setText(String.format("%s", DateTimeUtils.getCurrentTime(getContext())));
                }

                if (txtDate != null) {
                    txtDate.setText(DateTimeUtils.getCurrentDate(getApplicationContext(), DateTimeUtils.dashboardDateFormat));
                }
            }

            public void onFinish() {
                newTimer.start();
            }
        };
        newTimer.start();

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(getApplicationContext(), SiteSelectionViewActivity.class));
                finish();*/
            }
        });
    }

    private void initCommonView() {

        if (txtDate != null) {
            txtDate.setText(DateTimeUtils.getCurrentDate(getApplicationContext(), DateTimeUtils.dashboardDateFormat));
            txtDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*startActivity(new Intent(getApplicationContext(), SiteSelectionViewActivity.class));
                    finish();*/
                }
            });
        }

    }


    protected abstract int getLayoutResource();

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }

        if (newTimer != null) {
            newTimer.cancel();
        }
    }

    public void showToastMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    public void printLogMessage(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    public void showProgressBar() {

        //Check if Activity is null then close activity.
        if (getActivity() == null) {
            return;
        } else {
            //If loader instance is null then re-create object.
            if (loader == null) {
                loader = new ProgressLoader(getActivity());
            }

            //If progress bar is not showing then show progress bar.
            if (!loader.isShowing()) {
                loader.show();
            }
        }

    }

    public void showErrorMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.something_went_wrong));
    }

    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideProgressBar() {

        if (loader != null && loader.isShowing()) {
            loader.dismiss();
        }
    }

    public void showAlertDialog(Context context, String message) {

        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(message);
            builder1.setTitle(context.getResources().getString(R.string.app_name));
            builder1.setCancelable(true);
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();

                }
            });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {

        }

    }

    public void showNoSitesMessage() {
        showToastMessage(getActivity().getResources().getString(R.string.no_site_msg));
    }

    public Context getContext() {
        return BaseActivity.this;
    }

    public BaseActivity getActivity() {
        return BaseActivity.this;
    }

    public Bitmap generatePrintData(String firstName, String organizationName, String staffName,
                                    String companyName, Boolean isFromRegularVisitor, Boolean isContractor,
                                    Bitmap qrCodeBitmap, Boolean isFromStaffVisitor, boolean isFromPatientVisitor) {
        Paint paint = new Paint();
        paint.setTextSize(100.0f);
        paint.setColor(Color.WHITE);

        float headerTextSize = DimensionUtils.getFloatedTextSize(65, getActivity());
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

        float dateTimeSize = DimensionUtils.getFloatedTextSize(16, getActivity());

        Paint dateTimePaint = new Paint();
        dateTimePaint.setTextSize(dateTimeSize);
        dateTimePaint.setColor(Color.BLACK);


        float firstNameSize = DimensionUtils.getFloatedTextSize(35, getActivity());
        Paint firstNamePaint = new Paint();
        firstNamePaint.setTextSize(firstNameSize);
        firstNamePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        firstNamePaint.setColor(Color.BLACK);

        float companyNameSize = DimensionUtils.getFloatedTextSize(24, getActivity());
        Paint companyNamePaint = new Paint();
        companyNamePaint.setTextSize(companyNameSize);
        companyNamePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        companyNamePaint.setColor(Color.BLACK);


        float baseline = paint.ascent();
        int width = (int) (paint.measureText(firstName) + 0.5f);
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap imageToPrint = Bitmap.createBitmap(350 + 380, height + 680, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(imageToPrint);
        canvas.drawRect(0, 0, 350 + 380, height + 680, paint);

        canvas.drawRect(0, 0, 350 + 380, height + 200, paint1);


        String headerTitle = "";

        if (isFromPatientVisitor) {
            headerTitle = "Patient Visitor";
        }else if (isFromStaffVisitor) {
            headerTitle = "Staff";
        } else if (isFromRegularVisitor) {
            headerTitle = "Regular";
        } else {
            headerTitle = isContractor ? "Contractor" : "Visitor";
        }
        canvas.drawText(headerTitle, canvas.getWidth() / 2, 100, header);


        canvas.drawText(firstName, 15, 190, firstNamePaint);
        canvas.drawText(organizationName, 18, 240, companyNamePaint);


        canvas.drawText("Visiting:", 15, 300, companyNamePaint);
        canvas.drawText(staffName, 15, 370, firstNamePaint);
        canvas.drawText(companyName, 15, 420, companyNamePaint);


        Bitmap resizedQrCodeBitmap = Bitmap.createScaledBitmap(qrCodeBitmap, 250, 300, true);

        canvas.drawBitmap(resizedQrCodeBitmap, canvas.getWidth() - 230, 135, paint1);

        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat tm = new SimpleDateFormat("hh:mm aa");

        String formattedTime = "Signed in at " + tm.format(c.getTime());
        String formattedDate = "Date " + df.format(c.getTime());


        canvas.drawLine(15, 440, canvas.getWidth() - 15, 440, paint1);

        canvas.drawText(formattedTime, canvas.getWidth() - 280, 530, dateTimePaint);
        canvas.drawText(formattedDate, canvas.getWidth() - 280, 565, dateTimePaint);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
        Bitmap resizedCompanyLogo = Bitmap.createScaledBitmap(bmp, 215, 141, true);
        canvas.drawBitmap(resizedCompanyLogo, 15, 470, paint1);

        return imageToPrint;
    }


    public void previewPrintBadge(Bitmap bitmap) {

        final Dialog dialog = new Dialog(getContext());
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.print_report_preview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ImageView close_btn = (ImageView) dialog.findViewById(R.id.close_btn);
        ImageView imgView = (ImageView) dialog.findViewById(R.id.imgView);
        imgView.setImageBitmap(bitmap);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public Bitmap generateQrCodeData(String userID) {

        Bitmap qrCodeBitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(userID, BarcodeFormat.QR_CODE, 250, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeBitmap = bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return qrCodeBitmap;
    }

    public String getWorkPath() {
        return getActivity().getCacheDir().getAbsolutePath();
    }

    public void shakeView(View view) {
        Utils.makeMeShake(view, 100, 10);
    }


    public void scanQrCodeForSignOut(String scannedID) {

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

                            //Todo need to send data from response here for printing

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


                            Intent intent = new Intent(getActivity(), ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();

                        } else if (responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS_SIGN_OUT)) {

                            Intent intent = new Intent(getActivity(), ThankYouScreen.class);
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, responseModel.getData().getName());
                            intent.putExtra(ConstantClass.EXTRAA_VIEW_SCAN_STATUS, responseModel.getStatus());
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();

                        } else {
                            showToastMessage(getString(R.string.error_msg_scan_qr_code));
                        }


                    }
                } else if (response.code() == ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    scanQrCodeForSignOut(scannedID);

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
}

