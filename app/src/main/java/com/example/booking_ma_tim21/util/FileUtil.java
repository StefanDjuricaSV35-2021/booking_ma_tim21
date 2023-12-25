package com.example.booking_ma_tim21.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;

public class FileUtil {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static File fromUri(@NonNull Uri uri, @NonNull Context context) {
        String filePath = null;

        if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        } else if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    filePath = cursor.getString(columnIndex);
                }
            }
        }

        return filePath != null ? new File(filePath) : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getUriFromFile(@NonNull File file, @NonNull Context context) {
        return FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                file
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static File createImageFile(@NonNull Context context) {
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    "image",
                    ".jpg",
                    storageDir
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
