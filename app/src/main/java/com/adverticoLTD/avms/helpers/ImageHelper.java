package com.adverticoLTD.avms.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageHelper {

    public static void loadImage(Context context, ImageView imageView, String url) {
        try {
            Picasso.get().load(url)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
