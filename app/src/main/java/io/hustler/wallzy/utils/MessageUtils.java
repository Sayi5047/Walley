package io.hustler.wallzy.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
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
        try {
            if (null != activity) {
                if (null != view) {
                    Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                    snackbar.setAction(activity.getString(R.string.ok), v -> {
                        snackbar.dismiss();
                    });
                    snackbar.setActionTextColor(ContextCompat.getColor(Objects.requireNonNull(activity.getApplication()), R.color.colorAccent));
                    snackbar.show();
                }
            }
        } catch (IllegalArgumentException illargexception) {
            illargexception.printStackTrace();
            Log.i("MessageUtils", "showDismissableSnackBar: " + illargexception.getMessage());
        }
    }

    public interface BinaryClickListener {
        void onPositiveClick();

        void onNegativeClick();
    }

    /**
     * Helper method for showing alert and progress dialogs.
     *
     * @param context             Application context
     * @param title               Title of dialog
     * @param message             Message of the dialog
     * @param lottieAnimIdmBin    Lottie animation raw resource id
     * @param positiveBtnMessage  Message for the positive button
     * @param negativeBtnMessage  Message for the negative button
     * @param btnId               This handle 3 types dialog states
     *                            {btnId -(0) Both Positive and Negative cases
     *                            *btnId -(1) For only Positive button
     *                            *btnId -(-1) For only Negative Button (Which never comes.)}
     * @param binaryClickListener is the call back listener for Action button
     *                            This dialog is not cancelable by touching out side of the dialog
     */
    public void showBinaryAlertDialog(Activity context,
                                      String title,
                                      String message,
                                      int lottieAnimIdmBin,
                                      String positiveBtnMessage,
                                      String negativeBtnMessage,
                                      int btnId,
                                      BinaryClickListener binaryClickListener) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.binary_dialog_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Objects.requireNonNull(dialog.getWindow()).setClipToOutline(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.Floating_EditTextDialog;

        TextView dialogTitle, dialogMessage;
        Button positiveButton, negativeButton;
        LottieAnimationView lottieIcon;
        CardView rootVIew;
        dialogTitle = dialog.findViewById(R.id.title);
        dialogMessage = dialog.findViewById(R.id.message);
        lottieIcon = dialog.findViewById(R.id.imageView);
        negativeButton = dialog.findViewById(R.id.btn_negative);
        positiveButton = dialog.findViewById(R.id.btn_possitive);
        rootVIew = dialog.findViewById(R.id.root);
        TextUtils.findText_and_applyTypeface(rootVIew, context.getApplication());
        if (btnId == 0) {
            positiveButton.setVisibility(View.VISIBLE);
            negativeButton.setVisibility(View.VISIBLE);
        } else if (btnId == 1) {
            positiveButton.setVisibility(View.VISIBLE);
            negativeButton.setVisibility(View.GONE);
        } else if (btnId == -1) {
            negativeButton.setVisibility(View.VISIBLE);
            positiveButton.setVisibility(View.GONE);
        }
        String justifyMessage = "<html><body style= \"text-align:justify;text-justify: inter-word;\">" + message + "</body></html>";
        String boldTitle = "<html><body style= \"text-align:justify\"><b>" + title + "</b></body></html>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dialogMessage.setText(Html.fromHtml(justifyMessage, Html.FROM_HTML_MODE_COMPACT));
            dialogTitle.setText(Html.fromHtml(boldTitle, Html.FROM_HTML_MODE_COMPACT));

        } else {
            dialogMessage.setText(Html.fromHtml(justifyMessage));
            dialogTitle.setText(Html.fromHtml(boldTitle));
        }

        lottieIcon.setAnimation(lottieAnimIdmBin);
        positiveButton.setText(positiveBtnMessage);
        negativeButton.setText(negativeBtnMessage);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener((dialogInterface, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK) {
                dialogInterface.dismiss();
                return true;
            } else {
                return false;
            }
        });

        positiveButton.setOnClickListener(onClick -> {
            if (null != binaryClickListener) {
                binaryClickListener.onPositiveClick();
            }
            dialog.dismiss();
        });
        negativeButton.setOnClickListener(onClick -> {
            if (null != binaryClickListener) {
                binaryClickListener.onNegativeClick();
            }
            dialog.dismiss();
        });
        dialog.show();

    }
}


