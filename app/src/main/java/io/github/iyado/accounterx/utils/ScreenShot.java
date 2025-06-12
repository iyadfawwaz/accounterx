package io.github.iyado.accounterx.utils;


import static io.github.iyado.accounterx.AccounterApplication.noti;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;


public class ScreenShot {
    private final Context context;
    private final AppCompatActivity activity;

    public ScreenShot(AppCompatActivity activity,Context context) {

        this.context = context;
        this.activity = activity;
    }


    @SuppressLint("Recycle")
    public void takeScreenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        String fileName = "screenshot.png";
        ContentResolver contentResolver = context.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/png");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,"DCIM/Screenshots/");
        Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        try {
            assert uri != null;
            contentResolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG,100, Objects.requireNonNull(contentResolver.openOutputStream(uri)));
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        noti(activity,"تم حفظ الصورة بنجاح");

    }
}
