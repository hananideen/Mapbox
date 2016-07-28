package com.android.mapbox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Hanani on 28/7/2016.
 */
public class Utils {

    /**
     * create Bitmap from a view
     * @param context conext
     * @param view view to be converted into Bitmap
     * @return converted Bitmap
     */
    public static Bitmap createBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    /**
     * set a drawable image from Uri
     * @param image an image from drawable
     * @param context context
     * @return return the image in Bitmap
     */
    public static Bitmap setBitmap(String image, Context context) {
        Uri uri = Uri.parse("android.resource://com.android.mapbox/drawable/"+image);
        InputStream stream = null;
        try {
            stream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = resizeBitmap(BitmapFactory.decodeStream(stream),context.getResources().getDimension(R.dimen.profile_picture_height),context.getResources().getDimension(R.dimen.profile_picture_width));
        return bitmap;
    }

    /**
     * to resize the image based on displayed size
     * @param bitmap bitmap to be resized
     * @param height height for view
     * @param width width for view
     * @return return resized bitmap
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, float height , float width){
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, width, height),
                Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }
}
