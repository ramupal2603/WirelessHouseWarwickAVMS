package com.adverticoLTD.avms.ui.contractorView.newContractorScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.adverticoLTD.avms.R;
import com.adverticoLTD.avms.baseClasses.BaseActivity;
import com.adverticoLTD.avms.helpers.ConstantClass;
import com.adverticoLTD.avms.network.utils.WebApiHelper;

import butterknife.BindView;

public class WebViewPDFActivity extends BaseActivity {


    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    String jsonData;
    private int i = 0;
    private WebView pdfView;
    private ProgressBar progress;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";

    private String strName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pdfView = findViewById(R.id.pdfView);
        progress = findViewById(R.id.progress);

        showPdfFile(WebApiHelper.pdfURL[i]);

        btnConfirm.setOnClickListener(this::onClick);
        getData();

    }

    private void getData() {
        if (getIntent() != null) {
//            jsonData = getIntent().getStringExtra(ConstantClass.EXTRAA_FORM_DATA);
            strName = getIntent().getStringExtra(ConstantClass.EXTRAA_VIEW_USER_NAME);
        }

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pdf_view;
    }

    private void showPdfFile(final String imageString) {
        showProgress();
        pdfView.invalidate();
        pdfView.getSettings().setJavaScriptEnabled(true);
        pdfView.getSettings().setSupportZoom(true);
        pdfView.setInitialScale(150);
        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + imageString);
        pdfView.setWebViewClient(new WebViewClient() {
            boolean checkOnPageStartedCalled = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    pdfView.loadUrl(removePdfTopIcon);
                    hideProgress();
                } else {
                    showPdfFile(imageString);
                }
            }
        });
    }

    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view == btnConfirm) {
            Intent intent = new Intent(WebViewPDFActivity.this, DeclarationActivity.class);
            intent.putExtra(ConstantClass.EXTRAA_VIEW_USER_NAME, strName);
            startActivityForResult(intent, ConstantClass.REQUEST_NORMAL_CONTRACTOR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantClass.REQUEST_NORMAL_CONTRACTOR && resultCode == RESULT_OK) {
            if (data != null) {
                String descriptionOfWork = data.getStringExtra(ConstantClass.EXTRAA_DESCRIPTION_WORK);
                String imagePath = data.getStringExtra(ConstantClass.EXTRAA_SIGNATURE);
                Intent intent = new Intent();
                intent.putExtra(ConstantClass.EXTRAA_DESCRIPTION_WORK, descriptionOfWork);
                intent.putExtra(ConstantClass.EXTRAA_SIGNATURE, imagePath);
                setResult(RESULT_OK, intent);
                finish();
            }

        }
    }
}