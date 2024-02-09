package com.adverticoLTD.avms.keyLogSolution.ui.signInKeyScreen;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.helpers.DateTimeUtils;
import com.adverticoLTD.avms.helpers.PreferenceKeys;
import com.adverticoLTD.avms.keyLogSolution.KeyLogAdapter;
import com.adverticoLTD.avms.keyLogSolution.baseClasses.BaseActivity;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseDataModel;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.signIn.KeySignInRequestModel;
import com.adverticoLTD.avms.keyLogSolution.data.signIn.KeySignInRequestParamModel;
import com.adverticoLTD.avms.keyLogSolution.data.signIn.KeySigninResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListDataModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.upload.UploadFileResponseModel;
import com.adverticoLTD.avms.keyLogSolution.helpers.ConstantClass;
import com.adverticoLTD.avms.keyLogSolution.network.ApiService;
import com.adverticoLTD.avms.keyLogSolution.network.RetrofitClient;
import com.adverticoLTD.avms.keyLogSolution.network.utils.UploadImageHelpers;
import com.adverticoLTD.avms.keyLogSolution.ui.signatureView.SignatureViewActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.signoutKeyScreen.KeySignOutActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.thankyouScreen.ThankYouActivity;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInKeyActivity extends BaseActivity {

    private static final int REQUEST_SIGN_IN_SIGNATURE = 1001;
    private static final int REQUEST_SIGN_IN_STAFF_SIGNATURE = 1002;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.backLayout)
    LinearLayout backLayout;

    @BindView(R.id.loutKeyRefView)
    LinearLayout loutKeyRefView;

    @BindView(R.id.loutSignInView)
    LinearLayout loutSignInView;

    @BindView(R.id.loutStaffView)
    LinearLayout loutStaffView;

    @BindView(R.id.imgStaffSignature)
    ImageView imgStaffSignature;

    @BindView(R.id.txtStaffName)
    TextView txtStaffName;

    @BindView(R.id.txtSelectedKey)
    TextView txtSelectedKey;

    @BindView(R.id.txtStaffSignature)
    TextView txtStaffSignature;

    String staffId = "0";
    String keyRefId = "0";

    String signature1 = "";
    String signature2 = "";


    ArrayList<StaffListDataModel> arrStaffList = new ArrayList<>();
    ArrayList<KeyResponseDataModel> arrKeyRefList = new ArrayList<>();

    private File file1 = null;
    private File file2 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loutSignInView.setOnClickListener(this);
        loutStaffView.setOnClickListener(this);
        loutKeyRefView.setOnClickListener(this);
        imgSignature.setOnClickListener(this);
        imgStaffSignature.setOnClickListener(this);
        backLayout.setOnClickListener(this);
    }

    private void getKeyRefData() {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<KeyResponseModel> call = apiService.getKeySignInList(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader());
        call.enqueue(new Callback<KeyResponseModel>() {
            @Override
            public void onResponse(Call<KeyResponseModel> call, Response<KeyResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    KeyResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        arrKeyRefList = new ArrayList<>();
                        arrKeyRefList.addAll(responseModel.getData());

                        if (!arrKeyRefList.isEmpty()) {
                            keyRefListDialog();
                        } else {
                            showToastMessage("No Key Ref Found");
                        }

                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getKeyRefData();

                } else {
                    showToastMessage(getResources().getString(R.string.response_error_msg));
                }
            }

            @Override
            public void onFailure(Call<KeyResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("getStaffListData", t.getMessage());
            }
        });

    }



    void keyRefListDialog() {
        final Dialog dialog = new Dialog(SignInKeyActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.keylog_searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Key");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);


        List<String> arrKeyList = new ArrayList<String>();
        HashMap<String, Boolean> selectedHashMap = new HashMap<String, Boolean>();

        for (int i = 0; i < arrKeyRefList.size(); i++) {
            arrKeyList.add(arrKeyRefList.get(i).getName());
        }

        if (!keyRefId.isEmpty()) {
            String[] arrKeyRefId = keyRefId.split(",");
            for (int i = 0; i < arrKeyRefList.size(); i++) {
                for (String selectedID : arrKeyRefId) {
                    if (arrKeyRefList.get(i).getId().equals(selectedID)) {
                        selectedHashMap.put(arrKeyRefList.get(i).getName(), true);
                    }
                }
            }
        }

        KeyLogAdapter searchableAdapter = new KeyLogAdapter(SignInKeyActivity.this, arrKeyList, selectedHashMap);
        lv.setAdapter(searchableAdapter);


        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Key Name or scroll down");
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchableAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder selectedIDs = new StringBuilder();
                ArrayList<String> arrSelectedName = new ArrayList<>();

                for (int i = 0; i < arrKeyRefList.size(); i++) {
                    if (selectedHashMap.containsKey(arrKeyList.get(i)) && selectedHashMap.get(arrKeyList.get(i))) {
                        arrSelectedName.add(arrKeyRefList.get(i).getName());
                        selectedIDs.append(arrKeyRefList.get(i).getId()).append(",");
                    }
                }

                keyRefId = selectedIDs.toString();
                if (arrSelectedName.isEmpty()) {
                    txtSelectedKey.setText("");
                } else {
                    txtSelectedKey.setText(arrSelectedName.toString());
                }

                dialog.dismiss();

            }
        });
        dialog.show();

    }


    private void getStaffListData() {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<StaffListResponseModel> call = apiService.getStaffList(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader());
        call.enqueue(new Callback<StaffListResponseModel>() {
            @Override
            public void onResponse(Call<StaffListResponseModel> call, Response<StaffListResponseModel> response) {
                hideProgressBar();
                if (response.isSuccessful()) {
                    StaffListResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        arrStaffList = new ArrayList<>();
                        arrStaffList.addAll(responseModel.getData());

                        if (!arrStaffList.isEmpty()) {
                            companyStaffListDialog();
                        } else {
                            showToastMessage("No Staff Found");
                        }
                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getStaffListData();

                } else {
                    showToastMessage("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<StaffListResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("getStaffListData", t.getMessage());
            }
        });
    }


    void companyStaffListDialog() {
        final Dialog dialog = new Dialog(SignInKeyActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Staff");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        //CREATE AND SET ADAPTER TO LISTVIEW
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignInKeyActivity.this, android.R.layout.select_dialog_singlechoice);


        for (int i = 0; i < arrStaffList.size(); i++) {
            arrayAdapter.add(arrStaffList.get(i).getName());
        }

        lv.setAdapter(arrayAdapter);
        //SEARCH
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strName = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < arrStaffList.size(); i++) {
                    if (arrStaffList.get(i).getName().equalsIgnoreCase(strName)) {
                        txtStaffName.setText(arrStaffList.get(i).getName());
                        staffId = arrStaffList.get(i).getId();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Staff Name or scroll down");
        sv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //BUTTON
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void onClick(View v) {
        if (v == imgSignature) {
            Intent intent = new Intent(SignInKeyActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_IN_SIGNATURE);
        }

        if (v == imgStaffSignature) {
            Intent intent = new Intent(SignInKeyActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_IN_STAFF_SIGNATURE);
        }

        if (v == loutKeyRefView) {
            getKeyRefData();
        }

        if (v == backLayout) {
            finish();
        }

        if (v == loutStaffView) {
            getStaffListData();
        }

        if (v == loutSignInView) {
            doValidation();
        }

    }

    private void doValidation() {
        if (keyRefId.equals("0")) {
            showToastMessage(getResources().getString(R.string.key_validation_msg));
        } else if (staffId.equals("0")) {
            showToastMessage(getResources().getString(R.string.staff_validation_msg));
        } else if (file1 == null) {
            showToastMessage(getResources().getString(R.string.sign_validation));
        } else if (file2 == null) {
            showToastMessage(getResources().getString(R.string.staff_sign_validation));
        } else {
            uploadfile(file1, "1");
        }
    }

    private void uploadfile(File signature, String fileNo) {

        showProgressBar();

        MultipartBody.Part body = UploadImageHelpers.uploadImage(SignInKeyActivity.this, signature,
                "signature");

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<UploadFileResponseModel> call = apiService.uploadFile(body);
        call.enqueue(new Callback<UploadFileResponseModel>() {
            @Override
            public void onResponse(Call<UploadFileResponseModel> call, Response<UploadFileResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    UploadFileResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        if (fileNo.equals("1")) {
                            signature1 = responseModel.getSignature_name();
                            uploadfile(file2, "2");
                        }
                        if (fileNo.equals("2")) {
                            signature2 = responseModel.getSignature_name();
                            callSignIn();
                        }
                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    uploadfile(signature, fileNo);

                } else {
                    showToastMessage(getResources().getString(R.string.response_error_msg));
                }
            }

            @Override
            public void onFailure(Call<UploadFileResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("uploadFailed", t.getMessage());
            }
        });
    }

    private void callSignIn() {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<KeySigninResponseModel> call = apiService.keySignIn(Prefs.getString(PreferenceKeys.PREF_ACCESS_TOKEN, ""),
                DateTimeUtils.getCurrentDateHeader(), getKeySignInRequestModel());
        call.enqueue(new Callback<KeySigninResponseModel>() {
            @Override
            public void onResponse(Call<KeySigninResponseModel> call, Response<KeySigninResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    KeySigninResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        launchThankYouPage();
                    }
                } else if (response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED
                        || response.code() == com.adverticoLTD.avms.helpers.ConstantClass.RESPONSE_UNAUTHORIZED_FOR) {
                    getAccessKeyToken();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    callSignIn();

                } else {
                    showToastMessage(getResources().getString(R.string.response_error_msg));
                }
            }

            @Override
            public void onFailure(Call<KeySigninResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("callSignIn", t.getMessage());
            }
        });
    }

    private KeySignInRequestModel getKeySignInRequestModel() {

        KeySignInRequestModel keySignInRequestModel = new KeySignInRequestModel();

        KeySignInRequestParamModel keySignInRequestParamModel = new KeySignInRequestParamModel();

        keySignInRequestParamModel.setKey_reference_id(keyRefId);
        keySignInRequestParamModel.setSignature1(signature1);
        keySignInRequestParamModel.setSignature2(signature2);
        keySignInRequestParamModel.setStaff_id(staffId);

        keySignInRequestModel.setParam(keySignInRequestParamModel);
        return keySignInRequestModel;
    }

    private void launchThankYouPage() {
        Intent intent = new Intent(SignInKeyActivity.this, ThankYouActivity.class);
        intent.putExtra(ConstantClass.EXTRA_IS_FROM, ConstantClass.SIGN_IN);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgSignature.setImageBitmap(bitmap);

            file1 = new File(filePath);
        }

        if (requestCode == REQUEST_SIGN_IN_STAFF_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgStaffSignature.setImageBitmap(bitmap);
            txtStaffSignature.setVisibility(View.GONE);
            file2 = new File(filePath);
        }
    }
}
