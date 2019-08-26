package io.hustler.wallzy.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextUtils {
    public static final String FONT_SERIF = "fonts/ft_dm_serif.ttf";
    public static final String FONT_RALEWAY = "fonts/Raleway.ttf";

    public static void setFont(@Nullable final Activity activity, @NonNull final TextView tv, final String fontname) {
        assert activity != null;
        tv.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }


    public static void setEdit_Font(Activity activity, EditText et, String fontname) {
        et.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }

    public static void set_Radio_font(Activity activity, RadioButton et, String fontname) {
        et.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(), fontname));
    }

    @Nullable
    public static String getArrayItem_of_String(Activity activity, String name, int index) {
        int arrayid;
        arrayid = activity.getResources().getIdentifier(name, "array", activity.getApplicationContext().getPackageName());
        TypedArray typedArray;
        typedArray = activity.getResources().obtainTypedArray(arrayid);
        String string_from_array = typedArray.getString(index);
        typedArray.recycle();
        return string_from_array;

    }

    public static void applyTextShadow(TextView tv, float raduis, float x, float y, int color) {
        tv.setShadowLayer(raduis, x, y, color);
    }

    public static void findText_and_applyTypeface(final ViewGroup viewGroup, @NonNull final Activity activity) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {


                int childcount = viewGroup.getChildCount();
                for (int i = 0; i < childcount; i++) {
                    View view = viewGroup.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        findText_and_applyTypeface((ViewGroup) view, activity);
                    } else if (view instanceof TextView) {
                        setFont(activity, ((TextView) view), FONT_RALEWAY);
                    } else if (view instanceof EditText) {
                        setEdit_Font(activity, ((EditText) view), FONT_RALEWAY);
                    } else if (view instanceof Button) {
                        setFont(activity, ((TextView) view), FONT_RALEWAY);
                    } else if (view instanceof RadioButton) {
                        set_Radio_font(activity, ((RadioButton) view), FONT_RALEWAY);
                    }
                }
            }
        });
    }


    public static int getMatColor(Activity activity, String md_300) {
        int returnColor = Color.BLACK;
        int arrayId = activity.getResources().getIdentifier(md_300, "array", activity.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = activity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public static int getMainMatColor(String arrayname, Activity activity) {
        int returnColor = Color.BLACK;
        int arrayId = activity.getResources().getIdentifier(arrayname, "array", activity.getApplicationContext().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = activity.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }


}
