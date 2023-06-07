package com.adverticoLTD.avms.customClasses;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class FontTextViewBold extends androidx.appcompat.widget.AppCompatTextView {

    public FontTextViewBold(Context context) {
        super(context);
        init();
    }

    public FontTextViewBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontTextViewBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/aileron_bold.otf");
        this.setTypeface(face);
    }


}
