package com.adverticoLTD.avms.keyLogSolution.ui.signoutKeyScreen;

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
import com.adverticoLTD.avms.keyLogSolution.baseClasses.BaseActivity;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseDataModel;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.signOut.KeySignOutRequestModel;
import com.adverticoLTD.avms.keyLogSolution.data.signOut.KeySignOutRequestParamModel;
import com.adverticoLTD.avms.keyLogSolution.data.signOut.KeySignOutResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListDataModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListResponseModel;
import com.adverticoLTD.avms.keyLogSolution.data.upload.UploadFileResponseModel;
import com.adverticoLTD.avms.keyLogSolution.helpers.ConstantClass;
import com.adverticoLTD.avms.keyLogSolution.network.ApiService;
import com.adverticoLTD.avms.keyLogSolution.network.RetrofitClient;
import com.adverticoLTD.avms.keyLogSolution.network.utils.UploadImageHelpers;
import com.adverticoLTD.avms.keyLogSolution.ui.Utils;
import com.adverticoLTD.avms.keyLogSolution.ui.signatureView.SignatureViewActivity;
import com.adverticoLTD.avms.keyLogSolution.ui.thankyouScreen.ThankYouActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class KeySignOutActivity extends BaseActivity {

    private static final int REQUEST_SIGN_OUT_SIGNATURE = 1001;
    private static final int REQUEST_SIGN_OUT_STAFF_SIGNATURE = 1002;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtSurname)
    EditText edtSurname;

    @BindView(R.id.edtCompany)
    EditText edtCompany;

    @BindView(R.id.edtMobile)
    EditText edtMobile;

    @BindView(R.id.txtSelectedKey)
    TextView txtSelectedKey;

    @BindView(R.id.txtSignature)
    TextView txtSignature;

    @BindView(R.id.txtStaffSignature)
    TextView txtStaffSignature;

    @BindView(R.id.loutKeyRefView)
    LinearLayout loutKeyRefView;

    @BindView(R.id.loutStaffView)
    LinearLayout loutStaffView;

    @BindView(R.id.loutSignatureSignOutKey)
    LinearLayout loutSignatureSignOutKey;

    @BindView(R.id.loutStaffSignatureView)
    LinearLayout loutStaffSignatureView;

    @BindView(R.id.imgStaffSignature)
    ImageView imgStaffSignature;

    @BindView(R.id.txtStaffName)
    TextView txtStaffName;

    @BindView(R.id.backLayout)
    LinearLayout loutBackView;

    String staffId = "0";
    String keyRefId = "0";

    String signature1 = "";
    String signature2 = "";


    ArrayList<StaffListDataModel> arrStaffList = new ArrayList<>();
    ArrayList<KeyResponseDataModel> arrKeyRefList = new ArrayList<>();

    private File file1 = null;
    private File file2 = null;

    private Timer timer = new Timer();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loutKeyRefView.setOnClickListener(this);
        loutStaffView.setOnClickListener(this);
        imgSignature.setOnClickListener(this);
        loutStaffSignatureView.setOnClickListener(this);
        loutSignatureSignOutKey.setOnClickListener(this);
        loutBackView.setOnClickListener(this);

        edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                if (!s.toString().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    // TODO: do what you need here (refresh list)
                                    Utils.hideKeyBoard(KeySignOutActivity.this);
                                }
                            },
                            ConstantClass.AUTO_HIDE_KEYBOARD_TIMER
                    );


                }
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sign_out;
    }


    private void getStaffListData() {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<StaffListResponseModel> call = apiService.getStaffList();
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
                            showToastMessage("No staff found");
                        }


                    }
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
        final Dialog dialog = new Dialog(KeySignOutActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Staff");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        //CREATE AND SET ADAPTER TO LISTVIEW
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KeySignOutActivity.this,
                android.R.layout.select_dialog_singlechoice);


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


    private void getKeyRefData() {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<KeyResponseModel> call = apiService.getKeyList();
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
                            showToastMessage("No key ref found");
                        }
                    }
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
        final Dialog dialog = new Dialog(KeySignOutActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.keylog_searchview);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ListView lv = (ListView) dialog.findViewById(R.id.listView1);
        TextView header = (TextView) dialog.findViewById(R.id.header);
        header.setText("Select Key");
        Button btn = (Button) dialog.findViewById(R.id.cancel);
        Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        //CREATE AND SET ADAPTER TO LISTVIEW
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KeySignOutActivity.this,
                android.R.layout.simple_list_item_multiple_choice);


        for (int i = 0; i < arrKeyRefList.size(); i++) {
            arrayAdapter.add(arrKeyRefList.get(i).getName());
        }

        lv.setAdapter(arrayAdapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (!keyRefId.isEmpty()) {
            String[] arrKeyRefId = keyRefId.split(",");
            for (int i = 0; i < arrKeyRefList.size(); i++) {
                for (String selectedID : arrKeyRefId) {
                    if (arrKeyRefList.get(i).getId().equals(selectedID)) {
                        lv.setItemChecked(i, true);
                    }
                }
            }
        }
        //SEARCH
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strName = parent.getItemAtPosition(position).toString();

                for (int i = 0; i < arrKeyRefList.size(); i++) {
                    if (arrKeyRefList.get(i).getName().equalsIgnoreCase(strName)) {
                        txtSelectedKey.setText(arrKeyRefList.get(i).getName());
                        keyRefId = arrKeyRefList.get(i).getId();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });*/

        EditText sv = (EditText) dialog.findViewById(R.id.search);
        sv.setHint("Search Key Name or scroll down");
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sparseBooleanArray = lv.getCheckedItemPositions();
                StringBuilder selectedIDs = new StringBuilder();
                ArrayList<String> arrSelectedName = new ArrayList<>();

                for (int i = 0; i < arrKeyRefList.size(); i++) {
                    if (sparseBooleanArray.get(i)) {
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

    @Override
    public void onClick(View v) {

        if (v == imgSignature) {
            Intent intent = new Intent(KeySignOutActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_OUT_SIGNATURE);
        }

        if (v == loutBackView) {
            finish();
        }

        if (v == loutSignatureSignOutKey) {
            doValidation();
        }

        if (v == loutKeyRefView) {
            getKeyRefData();
        }

        if (v == loutStaffView) {
            getStaffListData();
        }

        if (v == loutStaffSignatureView) {
            Intent intent = new Intent(KeySignOutActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_OUT_STAFF_SIGNATURE);
        }

    }

    private void doValidation() {

        if (Utils.isEditTextEmpty(edtFirstName)) {
            showToastMessage(getResources().getString(R.string.firstname_validation_msg));
        } else if (Utils.isEditTextEmpty(edtSurname)) {
            showToastMessage(getResources().getString(R.string.surname_validation_msg));
        } else if (Utils.isEditTextEmpty(edtCompany)) {
            showToastMessage(getResources().getString(R.string.company_validation_msg));
        } else if (Utils.isEditTextEmpty(edtMobile)) {
            showToastMessage(getResources().getString(R.string.mobile_validation_msg));
        } else if (keyRefId.equals("0")) {
            showToastMessage(getResources().getString(R.string.key_validation_msg));
        } else if (file1 == null) {
            showToastMessage(getResources().getString(R.string.sign_validation));
        } else if (file2 == null) {
            showToastMessage(getResources().getString(R.string.staff_sign_validation));
        } else if (staffId.equals("0")) {
            showToastMessage(getResources().getString(R.string.staff_validation_msg));
        } else {
            uploadfile(file1, "1");
        }
    }

    private void uploadfile(File signature, String fileNo) {

        showProgressBar();

        MultipartBody.Part body = UploadImageHelpers.uploadImage(KeySignOutActivity.this, signature,
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
                            signOutKey(edtFirstName.getText().toString().trim(),
                                    edtSurname.getText().toString().trim(), edtCompany.getText().toString().trim(),
                                    edtMobile.getText().toString().trim());
                        }
                    }
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

    private void signOutKey(String firstName, String surname, String company, String mobileNo) {

        showProgressBar();

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<KeySignOutResponseModel> call =
                apiService.keySignout(getKeySignOutRequestModel(firstName, surname, company, mobileNo));

        call.enqueue(new Callback<KeySignOutResponseModel>() {
            @Override
            public void onResponse(Call<KeySignOutResponseModel> call, Response<KeySignOutResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    KeySignOutResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCES)) {
                        launchThankYouPage();
                    }
                } else {
                    showToastMessage(getResources().getString(R.string.response_error_msg));
                }

            }

            @Override
            public void onFailure(Call<KeySignOutResponseModel> call, Throwable t) {
                hideProgressBar();
                printLogMessage("signOutKey", t.getMessage());
            }
        });
    }

    private KeySignOutRequestModel getKeySignOutRequestModel(String firstName, String surname, String company, String mobileNo) {

        KeySignOutRequestModel keySignOutRequestModel = new KeySignOutRequestModel();

        KeySignOutRequestParamModel keySignOutRequestParamModel = new KeySignOutRequestParamModel();
        keySignOutRequestParamModel.setFirst_name(firstName);
        keySignOutRequestParamModel.setSur_name(surname);
        keySignOutRequestParamModel.setCompany_name(company);
        keySignOutRequestParamModel.setKey_reference_id(keyRefId);
        keySignOutRequestParamModel.setSignature1(signature1);
        keySignOutRequestParamModel.setSignature2(signature2);
        keySignOutRequestParamModel.setStaff_id(staffId);
        keySignOutRequestParamModel.setMobile_no(mobileNo);

        keySignOutRequestModel.setParam(keySignOutRequestParamModel);
        return keySignOutRequestModel;


    }

    private void launchThankYouPage() {
        Intent intent = new Intent(KeySignOutActivity.this, ThankYouActivity.class);
        intent.putExtra(ConstantClass.EXTRA_IS_FROM, ConstantClass.SIGN_OUT);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_OUT_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");

            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgSignature.setImageBitmap(bitmap);
            txtSignature.setVisibility(View.GONE);

            file1 = new File(filePath);
        } else if (requestCode == REQUEST_SIGN_OUT_STAFF_SIGNATURE && resultCode == RESULT_OK) {
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
