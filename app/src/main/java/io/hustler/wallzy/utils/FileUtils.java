package io.hustler.wallzy.utils;

import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

import io.hustler.wallzy.R;
import io.hustler.wallzy.constants.Constants;

public class FileUtils {

    private static Uri getImageContentUri(@NonNull Context context, @NonNull File imageFile) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), imageFile);
        } else {
            uri = Uri.fromFile(imageFile);
        }
        return uri;

    }

    public static void setwallpaper(@NonNull Context activity, @NonNull String imagepath) {
        try {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intent = new Intent(WallpaperManager.getInstance(activity).getCropAndSetWallpaperIntent(FileUtils.getImageContentUri(activity, new File(imagepath))));
            }
            Objects.requireNonNull(intent).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrash.log(e.getMessage());
        }

    }


    public static File downloadImageToSd_Card(String param, String download_image_name, @NonNull Context applicationContext) {

        File downloading_File;
        FileOutputStream fileOutputStream;
        InputStream inputStream;

        try {
//                GET URL
            URL url = new URL(param);
//                CRETAE DIRECTORY IN SD CARD WITH GIVEN NAME
            File destination_downloading_directory = new File(Constants.APP_WALLPAPERS_FOLDER);
            if (!destination_downloading_directory.exists()) {
                destination_downloading_directory.mkdirs();
            }
//                NOW CREATE ONE MORE FILE INSIDE THE DIRECTORY THAT BEEN MADE
            downloading_File = new File(destination_downloading_directory + File.separator + download_image_name);
            if (downloading_File.exists()) {
                downloading_File.delete();
            }

            try {
//                    OPEN A URL CONNECTION AND ATTACH TO HTTPURLCONNECTION
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

//                    GET DATA FROM INPUT STREAM && ATTACH OOUTPUT STREAM OBJECT TO THE FILE TO BE DOWNLOADED FILE OUTPUT STRAM OBJECT
                inputStream = httpURLConnection.getInputStream();
                fileOutputStream = new FileOutputStream(downloading_File);
//                    WRITE THE DATA TO BUFFER SO WE CAN COPY EVERYTHING AT ONCE TO MEMORY WHICH IMPROOVES EFFECIANCY
                byte[] buffer = new byte[2048];
                int bufferLength;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                }
                inputStream.close();
                fileOutputStream.close();
                ContentValues contentValues = getImageContent(downloading_File);
                applicationContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Log.d("IMAGE SAVED", "Image Saved in sd card");
                return downloading_File;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }


    }

    @NonNull
    public static ContentValues getImageContent(File parent) {
        ContentValues image = new ContentValues();
        image.put(MediaStore.Images.Media.TITLE, parent.getName());
        image.put(MediaStore.Images.Media.DISPLAY_NAME, parent.getName());
        image.put(MediaStore.Images.Media.DESCRIPTION, "Wallzy Image");
        image.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        if (parent.getAbsolutePath().contains(".jpg")) {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        } else if (parent.getAbsolutePath().contains(".png")) {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        } else {
            image.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            image.put(MediaStore.Images.Media.ORIENTATION, 0);
            image.put(MediaStore.Images.ImageColumns.BUCKET_ID, parent.toString().toLowerCase().hashCode());
            image.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, parent.getName().toLowerCase());
        }
        image.put(MediaStore.Images.Media.SIZE, parent.length());
        image.put(MediaStore.Images.Media.DATA, parent.getAbsolutePath());
        return image;
    }
}
