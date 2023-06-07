package com.adverticoLTD.avms.keyLogSolution.customClasses;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CorbelTextView extends AppCompatTextView {

    public CorbelTextView(Context context) {
        super(context);
        init();
    }

    public CorbelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CorbelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "aileronultralight.otf");
        this.setTypeface(face,1);
    }
}
