package com.adverticoLTD.avms.ui.contractorView.newContractorScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.keyLogSolution.data.upload.UploadFileResponseModel;
import com.adverticoLTD.avms.keyLogSolution.network.ApiService;
import com.adverticoLTD.avms.keyLogSolution.network.RetrofitClient;
import com.adverticoLTD.avms.keyLogSolution.network.utils.UploadImageHelpers;
import com.adverticoLTD.avms.keyLogSolution.ui.signatureView.SignatureViewActivity;

import java.io.File;

import butterknife.BindView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeclarationActivity extends BaseActivity {

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtDescriptionWork)
    EditText edtDescriptionWork;


    @BindView(R.id.edtSignature)
    TextView edtSignature;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.loutSubmit)
    LinearLayout loutSubmit;

    @BindView(R.id.loutBackBtn)
    RelativeLayout loutBackBtn;

    @BindView(R.id.chkTermsCondition)
    CheckBox chkTermsCondition;

    String signaturePath = "";

    File file1;
    private String remotePath = "";
    private String strName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getIntentData();

        edtSignature.setOnClickListener(this::onClick);
        imgSignature.setOnClickListener(this::onClick);
        loutSubmit.setOnClickListener(this::onClick);
        loutBackBtn.setOnClickListener(this::onClick);


    }

    private void getIntentData() {
        if (getIntent() != null) {
            strName = getIntent().getStringExtra(ConstantClass.EXTRAA_VIEW_USER_NAME);
            edtFirstName.setText(strName);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_declaration;
    }

    @Override
    public void onClick(View view) {

        if (view == edtSignature || view == imgSignature) {
            Intent intent = new Intent(DeclarationActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, ConstantClass.REQUEST_SIGN_OUT_SIGNATURE);
        }

        if (view == loutSubmit) {

            if (!chkTermsCondition.isChecked()) {
                showAlertDialog(this,"Please agree to terms and conditions for induction form");
            } else {
                uploadFile(file1);
            }


        }
        if (view == loutBackBtn) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_SIGN_OUT_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra("filePath");
            signaturePath = filePath;
            //loads the file
            File file = new File(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            imgSignature.setImageBitmap(bitmap);

            file1 = new File(filePath);
        }
    }


    private void uploadFile(File signature) {

        showProgressBar();

        MultipartBody.Part body = UploadImageHelpers.uploadImage(DeclarationActivity.this, signature,
                "signature");

        ApiService apiService = RetrofitClient.getRetrofit().create(ApiService.class);
        Call<UploadFileResponseModel> call = apiService.uploadDisclaimerFile(body);
        call.enqueue(new Callback<UploadFileResponseModel>() {
            @Override
            public void onResponse(Call<UploadFileResponseModel> call, Response<UploadFileResponseModel> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    UploadFileResponseModel responseModel = response.body();
                    if (responseModel != null && responseModel.getStatus().equals(ConstantClass.RESPONSE_SUCCESS)) {

                        remotePath = responseModel.getSignature_name();
                        String descOfWork = edtDescriptionWork.getText().toString().trim();

                        Intent intent = new Intent();
                        intent.putExtra(ConstantClass.EXTRAA_DESCRIPTION_WORK, descOfWork);
                        intent.putExtra(ConstantClass.EXTRAA_SIGNATURE, remotePath);
                        setResult(RESULT_OK, intent);
                        finish();
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
}
