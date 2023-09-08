package com.adverticoLTD.avms.ui.deliveries;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.helpers.StringUtils;
import com.adverticoLTD.avms.keyLogSolution.baseClasses.BaseActivity;
import com.adverticoLTD.avms.keyLogSolution.data.keyRefList.KeyResponseDataModel;
import com.adverticoLTD.avms.keyLogSolution.data.staffList.StaffListDataModel;
import com.adverticoLTD.avms.keyLogSolution.ui.signatureView.SignatureViewActivity;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

public class DeliverySignatureViewActivity extends BaseActivity {

    private static final int REQUEST_SIGN_IN_SIGNATURE = 1001;

    @BindView(R.id.imgSignature)
    ImageView imgSignature;

    @BindView(R.id.loutNameView)
    LinearLayout loutNameView;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.loutSignInView)
    LinearLayout loutSignInView;

    @BindView(R.id.txtSignature)
    TextView txtSignature;

    @BindView(R.id.loutSignatureView)
    RelativeLayout loutSignatureView;


    String signature1 = "";


    ArrayList<StaffListDataModel> arrStaffList = new ArrayList<>();
    ArrayList<KeyResponseDataModel> arrKeyRefList = new ArrayList<>();

    private File file1 = null;
    private File file2 = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loutSignInView.setOnClickListener(this);
        imgSignature.setOnClickListener(this);

    }


    @Override
    protected int getLayoutResource() {
        return R.layout.lout_delivery_signature;
    }

    @Override
    public void onClick(View v) {
        if (v == imgSignature) {
            Intent intent = new Intent(DeliverySignatureViewActivity.this, SignatureViewActivity.class);
            startActivityForResult(intent, REQUEST_SIGN_IN_SIGNATURE);
        }




        if (v == loutSignInView) {
            doValidation();
        }

    }

    private void doValidation() {
        if (StringUtils.checkEmptyEditText(edtName)) {
            showToastMessage(getResources().getString(R.string.please_enter_your_name));
        } else if (file1 == null) {
            showToastMessage(getResources().getString(R.string.sign_validation));
        } else {

            String filePath = signature1;
            String name = edtName.getText().toString().trim();


            Intent intent = new Intent();
            intent.putExtra("filePath", filePath);
            intent.putExtra("name", name);
            setResult(RESULT_OK, intent);
            finish();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_SIGNATURE && resultCode == RESULT_OK) {
            String filePath = null;
            if (data != null) {
                filePath = data.getStringExtra("filePath");
                //loads the file
                File file = new File(filePath);
                signature1 = filePath;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                txtSignature.setVisibility(View.GONE);
                imgSignature.setImageBitmap(bitmap);

                file1 = new File(filePath);
            }


        }

    }
}
