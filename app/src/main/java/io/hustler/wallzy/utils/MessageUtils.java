package io.hustler.wallzy.utils;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.hustler.wallzy.R;

public class MessageUtils {

    public static void showShortToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

    public static void showDismissableSnackBar(Activity activity, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(activity.getString(R.string.ok), v -> {
            snackbar.dismiss();
        });
        snackbar.setActionTextColor(ContextCompat.getColor(Objects.requireNonNull(activity.getApplication()), R.color.colorAccent));
        snackbar.show();
    }
}


