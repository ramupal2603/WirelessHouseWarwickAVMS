package com.adverticoLTD.avms.customClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class FontsRadioButton extends RadioButton {

    public FontsRadioButton(Context context) {
        super(context);
        init();
    }

    public FontsRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public FontsRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/poppins_light.ttf");
        this.setTypeface(face);
    }
}
